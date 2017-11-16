package com.yidiankeyan.science.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.utdid2.android.utils.StringUtils;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.my.entity.PhoneBindingBean;
import com.yidiankeyan.science.my.entity.WXBindingPhoneBean;
import com.yidiankeyan.science.utils.AESUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.MIUIUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UserResultEntity;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 重置密码
 * -发送验证码
 */
public class VerificationCodeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private EditText mInputPhone;
    private TextView tvNextVerification;
    private TextView tvCountNumber;
    private int recLen;
    private RequestParams params;
    private String mMobile = "";
    private String mPassWord = "";
    private TextView mTvCodePhone;
    private UserInforMation datas;
    private String mType;
    private String Invitation;
    private InputMethodManager imm;
    private AutoRelativeLayout mVerificationCode;
    private String mMessagePhone;

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    tvCountNumber.setText(recLen + "s");

                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        recLen = 3;
                        tvCountNumber.setText("再次获取");
                        tvCountNumber.setEnabled(true);
                    }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_verification_code;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        mInputPhone = (EditText) findViewById(R.id.et_input_phone);
        tvNextVerification = (TextView) findViewById(R.id.tv_next_verification);
        tvCountNumber = (TextView) findViewById(R.id.tv_count_number);
        mTvCodePhone = (TextView) findViewById(R.id.tv_code_phone);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mVerificationCode = (AutoRelativeLayout) findViewById(R.id.rl_verification_code);
    }

    @Override
    protected void initAction() {
        mType = getIntent().getStringExtra("type");
        txtTitle.setText("");
        titleBtn.setVisibility(View.GONE);
        mMobile = getIntent().getStringExtra("mobile");
        mPassWord = getIntent().getStringExtra("password");
        Invitation = getIntent().getStringExtra("invitation");
        handler.removeMessages(1);
        if (!StringUtils.isEmpty(mType)) {
            recLen = 60;
            tvCountNumber.setText(recLen + "s");
            tvCountNumber.setEnabled(false);
            Message message = handler.obtainMessage(1);     // Message
            handler.sendMessageDelayed(message, 1000);
//            if ("1".equals(mType)) {
//                toHttpGetCode();
//            }
            if ("3".equals(mType)) {
                forgetPsdCode();
            }
            if ("4".equals(mType)) {
                toHttpBindPhone();
            }
        }
        mTvCodePhone.setText(mMobile);
        llReturn.setOnClickListener(this);
        tvCountNumber.setOnClickListener(this);
        tvNextVerification.setOnClickListener(this);
        tvCountNumber.setEnabled(true);
    }

    /**
     * 微信强制绑定手机号获取验证码
     */
    private void toHttpBindPhone() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nationCode", 86);
        map.put("phoneNumber", mMobile.toString());
        map.put("templateType", 1);
        ApiServerManager.getInstance().getApiServer().getBinddingCode(map).enqueue(new RetrofitCallBack<PhoneBindingBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<PhoneBindingBean>> call, Response<RetrofitResult<PhoneBindingBean>> response) {
                if (response.body().getCode() == 200) {
                    SpUtils.putIntSp(DemoApplication.applicationContext, "bindingCode", Integer.parseInt(response.body().getData().getCode()));
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<PhoneBindingBean>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_count_number:
                handler.removeMessages(1);
                recLen = 60;
                tvCountNumber.setText(recLen + "s");
                tvCountNumber.setEnabled(false);
                Message message = handler.obtainMessage(1);     // Message
                handler.sendMessageDelayed(message, 1000);

                params = new RequestParams(SystemConstant.MYURL + SystemConstant.POST_PASSWORD_APPLY + mMobile);
                params.setConnectTimeout(10000);
                params.addHeader("token", SpUtils.getStringSp(this, "access_token"));
                if (!StringUtils.isEmpty(mType) && "1".equals(mType)) {
                    toHttpGetCode();
                }
                if ("2".equals(mType)) {
                    toHttpBindPhone();
                } else {
                    forgetPsdCode();
                }
                break;
            case R.id.tv_next_verification:
                imm.hideSoftInputFromWindow(mVerificationCode.getWindowToken(), 0); //强制隐藏键盘
                if (mInputPhone.getText().toString().length() == 5 && "3".equals(mType)) {
                    toHttpResetPassWord();
                } else if (mInputPhone.getText().toString().length() == 5 && "1".equals(mType)) {
                    toHttpRegister();
                } else if (mInputPhone.getText().toString().length() == 5 && "2".equals(mType)) {
                    toHttpPhoneCome();
                } else if (mInputPhone.getText().toString().length() == 5 && "4".equals(mType) &&
                        SpUtils.getIntSp(mContext,"bindingCode") == Integer.parseInt(mInputPhone.getText().toString())) {
                    toHttpPhoneCode();
                } else ToastMaker.showShortToast("验证码输入有误");
                break;
        }
    }

    /**
     * 短信登录
     * -验证码判断
     */
    private void toHttpPhoneCode() {
        mMessagePhone = mMobile.toString();
        String code = mInputPhone.getText().toString();
        ApiServerManager.getInstance().getApiServer().getIsCodePhone(mMessagePhone, code).enqueue(new RetrofitCallBack<UserInforMation>() {
            @Override
            public void onSuccess(Call<RetrofitResult<UserInforMation>> call, Response<RetrofitResult<UserInforMation>> response) {
                if (response.body().getCode() == 200) {
                    ToastMaker.showShortToast("登录成功");
                    startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                    finish();
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
                    SpUtils.putStringSp(mContext, "phone", mMessagePhone);
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
                                startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                            }
                            finish();
                            if (!StringUtils.isEmpty(Invitation) && "1".equals(Invitation)) {
                                showDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                            loadingDismiss();
                            if (DemoApplication.getInstance().activityExisted(MainLoginActivity.class)) {
                                DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                            }
                            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                                startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                            }
                            finish();
                            if (!StringUtils.isEmpty(Invitation) && "1".equals(Invitation)) {
                                showDialog();
                            }
                        }
                    });
                } else if (response.body().getCode() == 40005) {
                    ToastMaker.showShortToast("验证码错误");
                } else if (response.body().getCode() == 40003) {
                    ToastMaker.showShortToast("用户不存在");
                } else {
                    ToastMaker.showShortToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<UserInforMation>> call, Throwable t) {
            }
        });
    }

    /**
     * 绑定成功
     */
    private void toHttpPhoneCome() {
        int code = Integer.parseInt(mInputPhone.getText().toString());
        if (code == SpUtils.getIntSp(DemoApplication.applicationContext, "bindingCode")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loginModel", SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode"));
            map.put("phone", mMobile.toString());
            ApiServerManager.getInstance().getUserApiServer().getBinddingPhone(map).enqueue(new RetrofitCallBack<WXBindingPhoneBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<WXBindingPhoneBean>> call, Response<RetrofitResult<WXBindingPhoneBean>> response) {
                    if (response.body().getCode() == 200) {
                        ToastMaker.showShortToast("绑定成功");
                        SpUtils.putIntSp(mContext, "isBinding", 1);
                        SpUtils.putStringSp(mContext, "bindingPhone", mMobile.toString());
                        startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                        int status = response.body().getData().getStatus();
                        if (status == 0) {
                            showDialog();
                        }
                        if(MainLoginActivity.class != null){
                            DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                        }
                        if(WxBindPhoneActivity.class != null){
                            DemoApplication.getInstance().finishActivity(WxBindPhoneActivity.class);
                        }
                        finish();
                    } else if (response.body().getCode() == 500) {
                        ToastMaker.showShortToast("账号已绑定");
                    } else {
                        ToastMaker.showShortToast("绑定失败");
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<WXBindingPhoneBean>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                }
            });
        } else {
            ToastMaker.showShortToast("验证码输入有误");
        }
    }

    /**
     * 用户已注册忘记密码 获取验证码
     */
    private void forgetPsdCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phoneNumber", mMobile);
        map.put("type", "REPASSWORD");
        HttpUtil.post(VerificationCodeActivity.this, SystemConstant.URL + SystemConstant.POST_PASSWORD_APPLY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    ToastMaker.showShortToast("获取验证码成功");
                } else if (result.getCode() == 207) {
                    Toast.makeText(VerificationCodeActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    ToastMaker.showShortToast("获取验证码失败,请检查手机号输入是否有误");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    /**
     * liuchao  add  用户注册时候获取验证码
     */
    private void toHttpGetCode() {
        if (!TextUtils.isEmpty(mMobile)) {
            String baseValue = null;
            try {
                String inputValue = "{\"nationCode\":\"86\",\"phoneNumber\":\"" + mMobile + "\"}";
                baseValue = AESUtils.aesValue(inputValue, mMobile);
                baseValue = baseValue.replace("\n", "").trim();
                Log.i("baseValue==", baseValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> map = new HashMap();
            map.put("_s", baseValue);
            ApiServerManager.getInstance().getWebApiServer().getCode(map).enqueue(new RetrofitCallBack<Object>() {

                @Override
                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                    if (response.isSuccessful() && response.body().getCode() == 200) {
//                        Intent intent = new Intent(VerificationCodeActivity.this, VerificationCodeActivity.class);
//                        intent.putExtra("mobile", mMobile);
//                        intent.putExtra("password", mPassWord);
//                        intent.putExtra("type", "1");
//                        startActivity(intent);
//                        finish();
                    } else ToastMaker.showShortToast(response.body().getMsg());
                }

                @Override
                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                    LogUtils.e("onError:" + t.toString());
                }
            });
        } else {
            ToastMaker.showShortToast("手机号不能为空，请重新输入");
        }
    }

    private void toHttpRegister() {
        if (mPassWord.length() >= 6) {
            showLoadingDialog("正在注册");
            Map<String, String> map = new HashMap<>();
            map.put("phone", mMobile);
            map.put("code", mInputPhone.getText().toString());
            map.put("password", mPassWord);
            map.put("recommendernum", "");
            String channel = Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL");
            if (TextUtils.isEmpty(channel)) {
                channel = "unknow";
            }
            map.put("apkname", channel);
            HttpUtil.formPost(VerificationCodeActivity.this, SystemConstant.MYURL + "u/pcode/verify", map, new HttpUtil.HttpUserCallBack() {

                @Override
                public void successResult(UserResultEntity result) throws JSONException {
                    if (result.getCode() == 0) {
                        SpUtils.putStringSp(mContext, "phone", mMobile);
                        SpUtils.putStringSp(mContext, "password", mPassWord);
                        mInputPhone.setText("");
                        ToastMaker.showShortToast("注册成功，开始登录");
                        login(mMobile, MD5Util.MD5Pswd(mPassWord));
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
        } else {
            ToastMaker.showShortToast("密码长度不能小于6位");
        }
    }

    private void toHttpResetPassWord() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("mobile", mMobile);
        maps.put("password", MD5Util.MD5Pswd(mPassWord.toString()));
        maps.put("code", mInputPhone.getText().toString().trim());
        HttpUtil.post(VerificationCodeActivity.this, SystemConstant.URL + SystemConstant.POST_PASSWORD_RESET, maps, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 333) {
                    ToastMaker.showShortToast("验证码过期,请重新获取");
                } else {
                    mInputPhone.setText("");
                    ToastMaker.showShortToast("修改成功");
//                    Intent i = new Intent(VerificationCodeActivity.this, MainActivity.class);
//                    i.putExtra("newIntent", true);
//                    startActivity(i);

                    //自动登录
                    login(mMobile, MD5Util.MD5Pswd(mPassWord.toString()));
//                    finish();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
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
        ApiServerManager.getInstance().getUserApiServer().login(userName, userPassword).
                enqueue(new RetrofitCallBack<UserInforMation>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<UserInforMation>> call,
                                          Response<RetrofitResult<UserInforMation>> response) {
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
                                        startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                                    }
                                    finish();
                                    if (!StringUtils.isEmpty(mType) && "1".equals(mType)) {
                                        showDialog();
                                    }
                                }

                                @Override
                                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                                    loadingDismiss();
                                    if (DemoApplication.getInstance().activityExisted(MainLoginActivity.class)) {
                                        DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                                    }
                                    if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                                        startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                                    }
                                    finish();
                                    if (!StringUtils.isEmpty(mType) && "1".equals(mType)) {
                                        showDialog();
                                    }
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
                        startActivity(new Intent(VerificationCodeActivity.this, MainLoginActivity.class));
                        loadingDismiss();
                        ToastMaker.showShortToast("登录失败");
                    }
                });
    }

    /**
     * 用户看到的以为是弹框 其实 呵呵 就是一个activity
     */
    private void showDialog() {
        Intent intent = new Intent(VerificationCodeActivity.this, InvitationCodeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
