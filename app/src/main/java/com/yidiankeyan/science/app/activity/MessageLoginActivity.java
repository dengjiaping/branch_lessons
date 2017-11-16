package com.yidiankeyan.science.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;


import retrofit2.Call;
import retrofit2.Response;

/**
 * 短信登录
 */
public class MessageLoginActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private EditText mPhoneNumber;
    private TextView tvNext;
    private AutoRelativeLayout mRlMessageLogin;


    @Override
    protected int setContentView() {
        return R.layout.activity_message_login;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        tvNext = (TextView) findViewById(R.id.tv_next);
        mRlMessageLogin= (AutoRelativeLayout) findViewById(R.id.rl_message_login);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_next:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRlMessageLogin.getWindowToken(), 0); //强制隐藏键盘
                tvNext.setClickable(false);
                if (!Util.isMobPhone(mPhoneNumber.getText().toString())) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                    tvNext.setClickable(true);
                    return;
                } else {
                    toHttpRegistPhone(mPhoneNumber.getText().toString());
                }
                break;
        }
    }

    /**
     * 判断手机号是否存在
     * 1  已注册   0为注册
     */
    private void toHttpRegistPhone(final String phonenumber) {
        ApiServerManager.getInstance().getApiServer().getIsRegistPhone(phonenumber)
                .enqueue(new RetrofitCallBack<Integer>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<Integer>> call, Response<RetrofitResult<Integer>> response) {
                        if (response.body().getCode() == 200) {
                            if (response.body().getData() == 1) {
                                Intent intent = new Intent(MessageLoginActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("mobile", phonenumber);
                                intent.putExtra("type", "4");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MessageLoginActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("mobile", phonenumber);
                                intent.putExtra("type", "4");
                                intent.putExtra("invitation", "1");
                                startActivity(intent);
                            }
                        }
                        tvNext.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<Integer>> call, Throwable t) {
                        ToastMaker.showShortToast("网络异常，请稍后重试");
                        tvNext.setClickable(true);
                    }
                });
    }
}
