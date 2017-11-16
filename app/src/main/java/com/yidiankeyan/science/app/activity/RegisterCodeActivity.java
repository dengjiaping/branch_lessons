package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.utils.MIUIUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UserResultEntity;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 注册
 * -设置密码
 */

public class RegisterCodeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private String mozPhone;
    private String code;
    private EditText edtPassWord;
    private TextView tvNext;
    private EditText etMozNumber;
    private UserInforMation datas;
    private TextView tvDisplay;
    private boolean isFo;

    @Override
    protected int setContentView() {
        return R.layout.activity_register_code;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        edtPassWord = (EditText) findViewById(R.id.edt_reset_password);
        tvNext = (TextView) findViewById(R.id.tv_next_reset);
        etMozNumber = (EditText) findViewById(R.id.et_moz_number);
        tvDisplay = (TextView) findViewById(R.id.tv_display);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("注册");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        mozPhone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        tvNext.setOnClickListener(this);
        tvDisplay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_display:
                if (isFo) {
                    isFo = false;
                    tvDisplay.setText("显示");
                    edtPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPassWord.setSelection(edtPassWord.length());
                } else {
                    isFo = true;
                    tvDisplay.setText("隐藏");
                    edtPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtPassWord.setSelection(edtPassWord.length());
                }
                break;
            case R.id.tv_next_reset:
                if (edtPassWord.getText().toString().trim().length() >= 6) {
                    showLoadingDialog("正在注册");
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", mozPhone);
                    map.put("code", code);
                    map.put("password", edtPassWord.getText().toString().trim());
                    map.put("recommendernum", etMozNumber.getText().toString().trim());
                    String channel = Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL");
                    if (TextUtils.isEmpty(channel)) {
                        channel = "unknow";
                    }
                    map.put("apkname", channel);
                    HttpUtil.formPost(RegisterCodeActivity.this, SystemConstant.MYURL + "u/pcode/verify", map, new HttpUtil.HttpUserCallBack() {

                        @Override
                        public void successResult(UserResultEntity result) throws JSONException {
                            if (result.getCode() == 0) {
                                SpUtils.putStringSp(mContext, "phone", mozPhone);
                                SpUtils.putStringSp(mContext, "password", edtPassWord.getText().toString().trim());
//                            String channel = Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL");
//                            if (TextUtils.isEmpty(channel)) {
//                                channel = "unknow";
//                            }
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map.put("apkname", channel);
//                            map.put("mobile", mozPhone);
//                            ApiServerManager.getInstance().getApiServer().postRegFromChannel(map).enqueue(new RetrofitCallBack<Object>() {
//                                @Override
//                                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
//
//                                }
//                            });
                                ToastMaker.showShortToast("注册成功，开始登录");
                                login(mozPhone, MD5Util.MD5Pswd(edtPassWord.getText().toString()));
                            } else {
                                loadingDismiss();
                                ToastMaker.showShortToast("手机号或验证码错误");
                            }
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {
                            loadingDismiss();
                            ToastMaker.showShortToast("注册失败");
                        }
                    });
                    break;
                } else {
                    ToastMaker.showShortToast("密码长度不能小于6位");
                }
                break;
        }
    }


    private void login(final String userName, final String userPassword) {
        ApiServerManager.getInstance().getUserApiServer().login(userName, userPassword).enqueue(new RetrofitCallBack<UserInforMation>() {

            @Override
            public void onFailure(Call<RetrofitResult<UserInforMation>> call, Throwable t) {
                loadingDismiss();
                ToastMaker.showShortToast("登录失败");
            }

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
                    SpUtils.putIntSp(mContext, "saisiProfession", datas.getSaisiProfession());
                    SpUtils.putStringSp(mContext, "specialty", datas.getSpecialty());
                    SpUtils.putStringSp(mContext, "phone", userName);
                    SpUtils.putStringSp(mContext, "password", userPassword);
                    SpUtils.putStringSp(mContext, "access_token", datas.getToken());
                    SpUtils.putBooleanSp(mContext, SystemConstant.APP_IS_FIRST_START, true);
                    SpUtils.putStringSp(mContext, "sign", datas.getSig());
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
                    EventBus.getDefault().post(msg);
                    EventBus.getDefault().post(EventMsg.obtain(5588));
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
                            Intent intent = new Intent(mContext, RegisterSuccessActivity.class);
                            intent.putExtra("phone", mozPhone);
                            intent.putExtra("password", edtPassWord.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                            loadingDismiss();
                            Intent intent = new Intent(mContext, RegisterSuccessActivity.class);
                            intent.putExtra("phone", mozPhone);
                            intent.putExtra("password", edtPassWord.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (response.body().getCode() == -1) {
                    ToastMaker.showShortToast(response.body().getMsg());
                    loadingDismiss();
                    Util.logout();
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
