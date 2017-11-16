package com.yidiankeyan.science.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.AESUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {

    private EditText etMobPhone;
    private TextView tvNext;
    private int recLen;
    private EditText etInputPhone;
    private InputMethodManager inputMethodManager;
    private TextView mtvRight;
    private AutoRelativeLayout mRlRegister;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etMobPhone = (EditText) findViewById(R.id.et_mob_phone);
        tvNext = (TextView) findViewById(R.id.tv_next);
        etInputPhone = (EditText) findViewById(R.id.et_input_phone);
        mtvRight = (TextView) findViewById(R.id.tv_right);
        mRlRegister = (AutoRelativeLayout) findViewById(R.id.rl_register);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        mtvRight.setTextColor(getResources().getColor(R.color.login_state));
        mtvRight.setVisibility(View.VISIBLE);
        etMobPhone.requestFocus();
        tvNext.setOnClickListener(this);
        mtvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.tv_next:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRlRegister.getWindowToken(), 0); //强制隐藏键盘
                final String phoneNum = etMobPhone.getText().toString();
                final String psdWord = etInputPhone.getText().toString();
                tvNext.setClickable(false);
                if (!Util.isMobPhone(phoneNum)) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                } else if (psdWord.length() < 6) {
                    ToastMaker.showShortToast("密码长度不能小于6位");
                } else if (phoneNum.length() <= 0) {
                    ToastMaker.showShortToast("手机号不能为空");
                } else {
                    String baseValue = null;
                    try {
                        String inputValue = "{\"nationCode\":\"86\",\"phoneNumber\":\"" + phoneNum + "\"}";
                        baseValue = AESUtils.aesValue(inputValue, phoneNum);
                        baseValue = baseValue.replace("\n", "").trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Map<String, Object> map = new HashMap();
                    map.put("_s", baseValue);
                    progressDialog.setMessage("注册中");
                    progressDialog.show();
                    ApiServerManager.getInstance().getWebApiServer().getCode(map).enqueue(new RetrofitCallBack<Object>() {

                        @Override
                        public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                Intent intent = new Intent(RegisterActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("mobile", phoneNum);
                                intent.putExtra("password", psdWord);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                finish();
                            } else ToastMaker.showShortToast(response.body().getMsg());
                            tvNext.setClickable(true);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                            tvNext.setClickable(true);
                            ToastMaker.showShortToast("服务器响应超时,请稍后再试");
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.tv_right:
                Intent intent = new Intent(RegisterActivity.this, MainLoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
    }
}
