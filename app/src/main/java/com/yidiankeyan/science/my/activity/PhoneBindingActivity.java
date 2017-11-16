package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.entity.PhoneBindingBean;
import com.yidiankeyan.science.my.entity.WXBindingPhoneBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 账号绑定
 * -微信绑定手机
 */

public class PhoneBindingActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private EditText etMobPhone;
    private EditText etInputPhone;
    private TextView tvCountNumber;
    private TextView tvNext;
    private int recLen;
    private int code;
    private AutoRelativeLayout rlPhoneBinding;

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
                        recLen = 3;
                        tvCountNumber.setText("获取验证码");
                        tvCountNumber.setEnabled(true);
                    }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_phone_binding;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        etMobPhone = (EditText) findViewById(R.id.et_mob_phone);
        rlPhoneBinding = (AutoRelativeLayout) findViewById(R.id.rl_phone_binding);
        tvNext = (TextView) findViewById(R.id.tv_next);
        etInputPhone = (EditText) findViewById(R.id.et_input_phone);
        tvCountNumber = (TextView) findViewById(R.id.tv_count_number);
        tvNext = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("账号绑定");
        llReturn.setOnClickListener(this);
        tvCountNumber.setEnabled(true);
        tvCountNumber.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_count_number:
                if (!Util.isMobPhone(etMobPhone.getText().toString())) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                    return;
                }
                if (!TextUtils.isEmpty(etMobPhone.getText().toString())) {
                    ToastMaker.showShortToast("发送成功");
                    handler.removeMessages(1);
                    recLen = 60;
                    tvCountNumber.setText("获取验证码(" + recLen + ")");
                    tvCountNumber.setEnabled(false);
                    Message message = handler.obtainMessage(1);     // Message
                    handler.sendMessageDelayed(message, 1000);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("nationCode", 86);
                    map.put("phoneNumber", etMobPhone.getText().toString());
                    map.put("templateType", 3);
                    ApiServerManager.getInstance().getApiServer().getBinddingCode(map).enqueue(new RetrofitCallBack<PhoneBindingBean>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<PhoneBindingBean>> call, Response<RetrofitResult<PhoneBindingBean>> response) {
                            if (response.body().getCode() == 200) {
                                code = Integer.parseInt(response.body().getData().getCode());
                                SpUtils.putIntSp(DemoApplication.applicationContext, "bindingCode", code);
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<PhoneBindingBean>> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(this, "手机号不能为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_next:
                if (!Util.isMobPhone(etMobPhone.getText().toString())) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                    return;
                }
                if (!TextUtils.isEmpty(etMobPhone.getText().toString()) && etInputPhone.length() == 5) {
                    int code = Integer.parseInt(etInputPhone.getText().toString());
                    if (code == SpUtils.getIntSp(DemoApplication.applicationContext, "bindingCode")) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("loginModel", SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode"));
                        map.put("phone", etMobPhone.getText().toString());
                        ApiServerManager.getInstance().getUserApiServer().getBinddingPhone(map).enqueue(new RetrofitCallBack<WXBindingPhoneBean>() {
                            @Override
                            public void onSuccess(Call<RetrofitResult<WXBindingPhoneBean>> call, Response<RetrofitResult<WXBindingPhoneBean>> response) {
                                if (response.body().getCode() == 200) {
                                    ToastMaker.showShortToast("绑定成功");
                                    SpUtils.putIntSp(mContext, "isBinding", 1);
                                    SpUtils.putStringSp(mContext, "bindingPhone", etMobPhone.getText().toString());
                                    if (response.body().getData().getStatus() == 0) {
                                        Intent intent = new Intent(PhoneBindingActivity.this, PhoneBindingCodeActivity.class);
                                        intent.putExtra("phone", etMobPhone.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        startActivity(new Intent(PhoneBindingActivity.this, PhoneBindingSucceedActivity.class));
                                    }
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
                } else {
                    ToastMaker.showShortToast("手机号或验证码输入有误");
                }

                break;
        }
    }
}
