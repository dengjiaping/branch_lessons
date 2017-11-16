package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码
 * -重设密码
 */
public class ResetPassWordActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private EditText mPhoneNumber;
    private EditText mResetPassword;
    private TextView tvNextReset;
    private UserInforMation datas;

    @Override
    protected int setContentView() {
        return R.layout.activity_reset_pass_word;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mResetPassword = (EditText) findViewById(R.id.edt_reset_password);
        tvNextReset = (TextView) findViewById(R.id.tv_next_reset);
    }

    @Override
    protected void initAction() {
        mPhoneNumber.addTextChangedListener(new TextWatcher() {

            // 第二个执行
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged:" + "start:" + start + "before:" + before + "count:" + count);
            }

            // 第一个执行
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged:" + "start:" + start + "count:" + count + "after:" + after);
            }

            // 第三个执行
            @Override
            public void afterTextChanged(Editable s) { // Edittext中实时的内容
                if (s.toString().length() > 0 && mResetPassword.getText().toString().length() > 0) {
                    tvNextReset.setEnabled(true);
                    tvNextReset.setBackgroundResource(R.drawable.shape_login_state);
                } else {
                    tvNextReset.setEnabled(false);
                    tvNextReset.setBackgroundResource(R.drawable.shape_reset_password);
                }

            }
        });
        mResetPassword.addTextChangedListener(new TextWatcher() {

            // 第二个执行
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            // 第一个执行
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // 第三个执行
            @Override
            public void afterTextChanged(Editable s) { // Edittext中实时的内容
                if (s.toString().length() > 0 && mPhoneNumber.getText().length() > 0) {
                    tvNextReset.setEnabled(true);
                    tvNextReset.setBackgroundResource(R.drawable.shape_login_state);
                } else {
                    tvNextReset.setEnabled(false);
                    tvNextReset.setBackgroundResource(R.drawable.shape_reset_password);
                }

            }
        });
        llReturn.setOnClickListener(this);
        tvNextReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_next_reset:
                if (!Util.isMobPhone(mPhoneNumber.getText().toString())) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                    return;
                } else if (mResetPassword.getText().length() < 6) {
                    ToastMaker.showShortToast("密码长度不能小于6位");
                } else {
                    Intent intent = new Intent(this, VerificationCodeActivity.class);
                    intent.putExtra("mobile", mPhoneNumber.getText().toString());
                    intent.putExtra("password", mResetPassword.getText().toString());
                    intent.putExtra("type", "3");
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

}
