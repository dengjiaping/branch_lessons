package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.UserResultEntity;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册
 * -输入验证码
 */
public class InputIdentifyingCodeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    //    private TextView tvPhone;
    private EditText etInputPhone;
    private TextView tvNext;
    private TextView tvCountNumber;
    private Button btnCountNumber;
    private EditText edtPassWord;
    private int recLen;
    private int code;
    private UserInforMation datas;
    private RequestParams params;
    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    tvCountNumber.setText("获取验证码(" + recLen + ")");

                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        tvCountNumber.setVisibility(View.GONE);
                        btnCountNumber.setVisibility(View.VISIBLE);
                        recLen = 3;
                    }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_input_identifying_code;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
//        tvPhone = (TextView) findViewById(R.id.tv_phone);
        etInputPhone = (EditText) findViewById(R.id.et_input_phone);
        tvNext = (TextView) findViewById(R.id.tv_nexts);
        edtPassWord = (EditText) findViewById(R.id.reguster_password);
        tvCountNumber = (TextView) findViewById(R.id.tv_count_number);
        btnCountNumber = (Button) findViewById(R.id.btn_count_number);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("发送验证码");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        btnCountNumber.setOnClickListener(this);
        //隐藏用户手机号中间位数
//        if (!TextUtils.isEmpty(getIntent().getStringExtra("phone")) && getIntent().getStringExtra("phone").length() > 6) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < getIntent().getStringExtra("phone").length(); i++) {
//                char c = getIntent().getStringExtra("phone").charAt(i);
//                if (i >= 3 && i <= 6) {
//                    sb.append('*');
//                } else {
//                    sb.append(c);
//                }
//            }
//            tvPhone.setText(sb.toString());
//        }
        etInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5 && edtPassWord.getText().toString().trim().length() >= 6) {
                    tvNext.setEnabled(true);
                    tvNext.setBackgroundResource(R.drawable.select_register_phones);
                } else {
                    tvNext.setEnabled(false);
                    tvNext.setBackgroundResource(R.drawable.select_register_phone);
                }
            }
        });
        edtPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6 && etInputPhone.getText().toString().trim().length() == 5) {
                    tvNext.setEnabled(true);
                    tvNext.setBackgroundResource(R.drawable.select_register_phones);
                } else {
                    tvNext.setEnabled(false);
                    tvNext.setBackgroundResource(R.drawable.select_register_phone);
                }
            }
        });
//        handler.removeMessages(1);
//        recLen = 3;
//        tvCountNumber.setText(recLen + "s后重新获取");
//        tvCountNumber.setVisibility(View.VISIBLE);
//        btnCountNumber.setVisibility(View.GONE);
//        Message message = handler.obtainMessage(1);     // Message
//        handler.sendMessageDelayed(message, 1000);
        params = new RequestParams(SystemConstant.MYURL + SystemConstant.GET_MY_SET_UP + getIntent().getStringExtra("phone"));
        params.setConnectTimeout(10000);
        params.addHeader("token", SpUtils.getStringSp(this, "access_token"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.btn_count_number:
                handler.removeMessages(1);
                recLen = 60;
                tvCountNumber.setText("获取验证码(" + recLen + ")");
                tvCountNumber.setVisibility(View.VISIBLE);
                btnCountNumber.setVisibility(View.GONE);
                Message message = handler.obtainMessage(1);     // Message
                handler.sendMessageDelayed(message, 1000);

//                HttpUtil.get(this, SystemConstant.MYURL + SystemConstant.GET_MY_SET_UP, getIntent().getStringExtra("phone"), new HttpUtil.HttpCallBack() {
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        Log.e(">>>>", ">>>>" + result);
//
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//
//                    }
//                });
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e("网络出参:url===" + SystemConstant.MYURL + SystemConstant.GET_MY_SET_UP + getIntent().getStringExtra("phone") + ",参数===" + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            code = object.getInt("code");
                            if (code == 1) {
                                ToastMaker.showShortToast("该手机号已经注册了");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LogUtils.e("onError:" + ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
                break;
            case R.id.tv_nexts:
                showLoadingDialog("正在注册");
                Map<String, String> map = new HashMap<>();
                map.put("phone", getIntent().getStringExtra("phone"));
                map.put("code", etInputPhone.getText().toString().trim());
                map.put("password", edtPassWord.getText().toString().trim());
                map.put("recommendernum", getIntent().getStringExtra("mozNumber"));
                String channel = Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL");
                if (TextUtils.isEmpty(channel)) {
                    channel = "unknow";
                }
                map.put("apkname", channel);
                HttpUtil.formPost(InputIdentifyingCodeActivity.this, SystemConstant.MYURL + "u/pcode/verify", map, new HttpUtil.HttpUserCallBack() {

                    @Override
                    public void successResult(UserResultEntity result) throws JSONException {
                        SpUtils.putStringSp(mContext, "phone", getIntent().getStringExtra("phone"));
                        SpUtils.putStringSp(mContext, "password", edtPassWord.getText().toString().trim());
//                        String channel = Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL");
//                        if (TextUtils.isEmpty(channel)) {
//                            channel = "unknow";
//                        }
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("apkname", channel);
//                        map.put("mobile", getIntent().getStringExtra("phone"));
//                        ApiServerManager.getInstance().getApiServer().postRegFromChannel(map).enqueue(new RetrofitCallBack<Object>() {
//                            @Override
//                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
//
//                            }
//                        });
                        ToastMaker.showShortToast("注册成功，开始登录");
                        login(getIntent().getStringExtra("phone"), MD5Util.MD5Pswd(edtPassWord.getText().toString()));
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                        loadingDismiss();
                        ToastMaker.showShortToast("注册失败");
                    }
                });
                break;
        }
    }

    private void login(final String userName, final String userPassword) {

        RequestParams params = new RequestParams(SystemConstant.URL + "size/user/login/" + userName + "/" + userPassword + "/" + 0);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtils.e("网络出参:url===" + SystemConstant.URL + "size/user/login/" + userName + "/" + userPassword + "/" + 0 + ",参数===" + result);
                ResultEntity entity = new Gson().fromJson(result, ResultEntity.class);
                loadingDismiss();
                if (entity.getCode() == 200) {
                    ToastMaker.showShortToast("登录成功");
                    datas = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(entity.getData()), UserInforMation.class);
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
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
                    EventBus.getDefault().post(msg);
                    Intent intent = new Intent(mContext, RegisterSuccessActivity.class);
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    intent.putExtra("password", edtPassWord.getText().toString().trim());
                    startActivity(intent);
                    EventBus.getDefault().post(EventMsg.obtain(5588));
                    handler.removeMessages(1);
                    finish();
                } else if (entity.getCode() == -1) {
                    ToastMaker.showShortToast("登录失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                ToastMaker.showShortToast("登录失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
