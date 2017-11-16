package com.yidiankeyan.science.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.utils.LogUtils;
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
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 登录总入口
 */
public class MainLoginActivity extends BaseActivity {

    private AutoLinearLayout MllReturn;
    private ImageView mImgWxPicture;
    private TextView mTvRegister;
    private IntentFilter intentFilter;

    private TextView mTvResetPassword;
    private TextView mTvLogin;
    private EditText mUserName;
    private EditText mUserPassword;
    private UserInforMation datas;
    private AutoRelativeLayout mMainLogin;
    private ImageView mImgMessagePicture;
    private TextView mtvRegisterAgreement;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_main_login;
    }

    @Override
    protected void initView() {
        MllReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mImgWxPicture = (ImageView) findViewById(R.id.img_wx_picture);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvResetPassword = (TextView) findViewById(R.id.tv_reset_password);
        mTvLogin = (TextView) findViewById(R.id.tv_login);
        mUserName = (EditText) findViewById(R.id.user_name);
        mUserPassword = (EditText) findViewById(R.id.user_password);
        mMainLogin = (AutoRelativeLayout) findViewById(R.id.activity_main_login);
        mImgMessagePicture = (ImageView) findViewById(R.id.img_message_picture);
        mtvRegisterAgreement = (TextView) findViewById(R.id.tv_register_agreement);
        mtvRegisterAgreement.setOnClickListener(this);
    }

    @Override
    protected void initAction() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.wx.login.code");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        mImgWxPicture.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        MllReturn.setOnClickListener(this);

        mTvResetPassword.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mImgMessagePicture.setOnClickListener(this);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            loadingDismiss();
        }

        @Override
        public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA arg0, int arg1) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            loadingDismiss();
        }

    };


    /**
     * 微信登录
     */
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra("code");
            ApiServerManager.getInstance().getUserApiServer().wxLogin(code).enqueue(new RetrofitCallBack<UserInforMation>() {
                @Override
                public void onSuccess(Call<RetrofitResult<UserInforMation>> call, Response<RetrofitResult<UserInforMation>> response) {
                    if (response.body().getCode() == 200) {
                        UserInforMation datas = response.body().getData();
                        LogUtils.e("登录");
                        SpUtils.putStringSp(mContext, "access_token", datas.getToken());
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
                        SpUtils.putStringSp(mContext, "bindingPhone", datas.getBindingPhone());
                        SpUtils.putIntSp(mContext, "isBoundQQ", datas.getIsBoundQQ());
                        SpUtils.putIntSp(mContext, "isBoundWeChat", datas.getIsBoundWeChat());
                        SpUtils.putIntSp(mContext, "isBountWeiBo", datas.getIsBountWeiBo());
                        SpUtils.putIntSp(mContext, "loginMode", datas.getLoginMode());
                        SpUtils.putIntSp(mContext, "isBinding", datas.getIsBinding());
                        SpUtils.putIntSp(mContext, "gender", datas.getGender());
                        SpUtils.putIntSp(mContext, "saisiProfession", datas.getSaisiProfession());
                        SpUtils.putStringSp(mContext, "specialty", datas.getSpecialty());
                        SpUtils.putBooleanSp(mContext, SystemConstant.APP_IS_FIRST_START, true);
                        SpUtils.putStringSp(mContext, "password", MD5Util.MD5Pswd("123456"));
                        SpUtils.putStringSp(mContext, "sign", datas.getSig());
                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
                        EventBus.getDefault().post(msg);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("user_id", datas.getUserid());
                        if (MIUIUtils.isMIUI()) {
                            map.put("reg_id", MiPushClient.getRegId(DemoApplication.applicationContext));
                            map.put("type", 2);
                        } else {
                            map.put("reg_id", JPushInterface.getRegistrationID(getApplicationContext()));
                            map.put("type", 1);
                        }
                        if (datas.getLoginMode() == 1 && datas.getIsBinding() == 0) {
                            loadingDismiss();
                            startActivity(new Intent(MainLoginActivity.this, WxBindPhoneActivity.class));
                        } else {
                            ApiServerManager.getInstance().getUserApiServer().postPushId(map).enqueue(new RetrofitCallBack<Object>() {
                                @Override
                                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                    loadingDismiss();
                                    Intent i = new Intent(MainLoginActivity.this, MainActivity.class);
                                    i.putExtra("newIntent", true);
                                    MainLoginActivity.this.startActivity(i);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                                    loadingDismiss();
                                    Intent i = new Intent(MainLoginActivity.this, MainActivity.class);
                                    i.putExtra("newIntent", true);
                                    MainLoginActivity.this.startActivity(i);
                                    finish();
                                }
                            });
                        }
                    } else {
                        loadingDismiss();
                        ToastMaker.showShortToast("登录失败");
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<UserInforMation>> call, Throwable t) {
//                    Log.e("onFailure", t.getMessage());
                    loadingDismiss();
                    ToastMaker.showShortToast("登录失败");
                }
            });
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_agreement:
                Intent intentAgress = new Intent(MainLoginActivity.this,AgreementActivity.class);
                startActivity(intentAgress);
                break;
            case R.id.img_wx_picture:
                //取消授权
//                UMShareAPI.get(this).deleteOauth(MainLoginActivity.this, SHARE_MEDIA.WEIXIN, umdelAuthListener);
                //登录授权
                showLoadingDialog("请稍候");
                UMShareAPI.get(this).doOauthVerify(MainLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_register:
                //注册
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.ll_return:
                //关闭登录
                if (TextUtils.equals("true", SpUtils.getStringSp(mContext, "returnLogin"))) {
                    finish();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("newIntent", true);
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.tv_reset_password:
                // 重置密码
                Intent mPass = new Intent(mContext, ResetPassWordActivity.class);
                startActivity(mPass);
                break;
            case R.id.tv_login:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mMainLogin.getWindowToken(), 0); //强制隐藏键盘
                login(mUserName.getText().toString().trim(), MD5Util.MD5Pswd(mUserPassword.getText().toString()));
                break;
            case R.id.img_message_picture:
                //短信登录
                Intent mMessage = new Intent(mContext, MessageLoginActivity.class);
                startActivity(mMessage);
                break;
        }
    }


    /**
     * 手机号登录
     *
     * @param userName
     * @param userPassword
     */
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
                                startActivity(new Intent(MainLoginActivity.this, MainActivity.class));
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
                                startActivity(new Intent(MainLoginActivity.this, MainActivity.class));
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
                loadingDismiss();
            }

            @Override
            public void onFailure(Call<RetrofitResult<UserInforMation>> call, Throwable t) {
                loadingDismiss();
                ToastMaker.showShortToast("登录失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
