package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.my.fragment.MyFragment;
import com.yidiankeyan.science.utils.MIUIUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的
 * -登录界面
 */
public class LoginActivity extends BaseActivity {

    private AutoLinearLayout returnTitle;
    private TextView maintitleTxt;
    private EditText userName;
    private EditText userPassword;
    private TextView tvLogin;
    private TextView tvForgetPassword;
    private boolean isFo;
    private MyFragment myFragment;
    private UserInforMation datas;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        userName = (EditText) findViewById(R.id.user_name);
        userPassword = (EditText) findViewById(R.id.user_password);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
//        imgPassEye = (ImageView) findViewById(R.id.img_pass_eye);
        myFragment = new MyFragment();
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("登录");
        returnTitle.setOnClickListener(this);
//        mShareAPI = UMShareAPI.get(this);
        tvLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
//        imgPassEye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_forget_password:
                /**
                 * 忘记密码
                 */
                Intent intent = new Intent(mContext, VerificationCodeActivity.class);
                intent.putExtra("phone", userName.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.tv_login:
                login(userName.getText().toString().trim(), MD5Util.MD5Pswd(userPassword.getText().toString()));
                break;
        }
    }


    private void login(final String userName, final String userPassword) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
            ToastMaker.showShortToast("账号或密码不能为空");
            return;
        }
        progressDialog.setMessage("登录中");
        progressDialog.show();
        ApiServerManager.getInstance().getUserApiServer().login(userName, userPassword).enqueue(new RetrofitCallBack<UserInforMation>() {
            @Override
            public void onSuccess(Call<RetrofitResult<UserInforMation>> call, Response<RetrofitResult<UserInforMation>> response) {
                if (response.body().getCode() == 200) {
                    datas = response.body().getData();
                    if (TextUtils.isEmpty(datas.getToken())) {
                        loadingDismiss();
                        ToastMaker.showShortToast("登录失败");
                        return;
                    }
                    SpUtils.putStringSp(mContext, "userId", datas.getUserid());
                    SpUtils.putStringSp(mContext, "birthday", datas.getBirthday());
                    SpUtils.putStringSp(mContext, "userName", datas.getUsername());
                    SpUtils.putStringSp(mContext, "userimgurl", datas.getUserimgurl());
                    SpUtils.putStringSp(mContext, "scienceNum", datas.getSizeid() + "");
                    SpUtils.putStringSp(mContext, "profession", datas.getProfession());
                    SpUtils.putStringSp(mContext, "position", datas.getPosition());
                    SpUtils.putStringSp(mContext, "school", datas.getSchool());
                    SpUtils.putStringSp(mContext, "phone", datas.getPhone());
                    SpUtils.putStringSp(mContext, "email", datas.getEmail());
                    SpUtils.putStringSp(mContext, "bgimgurl", datas.getBgimgurl());
                    SpUtils.putStringSp(mContext, "address", datas.getAddress());
                    SpUtils.putStringSp(mContext, "degree", datas.getDegree());
                    SpUtils.putStringSp(mContext, "mysign", datas.getMysign());
                    SpUtils.putIntSp(mContext, "isBoundQQ", datas.getIsBoundQQ());
                    SpUtils.putIntSp(mContext, "isBoundWeChat", datas.getIsBoundWeChat());
                    SpUtils.putIntSp(mContext, "isBountWeiBo", datas.getIsBountWeiBo());
                    SpUtils.putIntSp(mContext, "gender", datas.getGender());
                    SpUtils.putIntSp(mContext, "loginMode", datas.getLoginMode());
                    SpUtils.putIntSp(mContext, "isBinding", datas.getIsBinding());
                    SpUtils.putIntSp(mContext, "saisiProfession", datas.getSaisiProfession());
                    SpUtils.putStringSp(mContext, "specialty", datas.getSpecialty());
                    SpUtils.putStringSp(mContext, "phone", userName);
                    SpUtils.putStringSp(mContext, "password", userPassword);
                    SpUtils.putStringSp(mContext, "access_token", datas.getToken());
                    SpUtils.putBooleanSp(mContext, SystemConstant.APP_IS_FIRST_START, true);
                    SpUtils.putStringSp(mContext, "sign", datas.getSig());
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
                    EventBus.getDefault().post(msg);
                    Intent intent = new Intent();
                    intent.setAction("action.read.audio");
                    sendBroadcast(intent);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("user_id", datas.getUserid());
                    if (MIUIUtils.isMIUI()) {
                        map.put("reg_id", MiPushClient.getRegId(DemoApplication.applicationContext));
                        map.put("type", 2);
                    } else {
                        map.put("reg_id", JPushInterface.getRegistrationID(getApplicationContext()));
                        map.put("type", 1);
                    }
                    ApiServerManager.getInstance().getUserApiServer().postPushId(map).enqueue(new RetrofitCallBack<Object>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                            loadingDismiss();
                            if (DemoApplication.getInstance().activityExisted(MainLoginActivity.class)) {
                                DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                            }
                            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                            loadingDismiss();
                            if (DemoApplication.getInstance().activityExisted(MainLoginActivity.class)) {
                                DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                            }
                            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    });
                } else if (response.body().getCode() == -1) {
                    ToastMaker.showShortToast(response.body().getMsg());
                    loadingDismiss();
                    Util.logout();
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
                } else if (response.body().getCode() == 40003) {
                    ToastMaker.showShortToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<UserInforMation>> call, Throwable t) {
                loadingDismiss();
                ToastMaker.showShortToast("登录失败");
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (msg.getWhat() == 5588)
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        mShareAPI = null;
    }

}

