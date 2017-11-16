package com.yidiankeyan.science.information.acitivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 领取奖品
 */
public class ReceivePrizeActivity extends BaseActivity {

    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private Button btnSubmit;

    @Override
    protected int setContentView() {
        return R.layout.activity_receive_prize;
    }

    @Override
    protected void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initAction() {
        btnSubmit.setOnClickListener(this);
        findViewById(R.id.ll_return).setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Util.isMobPhone(s.toString())) {
                    btnSubmit.setTag(true);
                } else {
                    btnSubmit.setTag(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.btn_submit:
                toHttpReceivePrize();
                break;
        }
    }

    /**
     * 领奖
     */
    private void toHttpReceivePrize() {
        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etPhone.getText()) || TextUtils.isEmpty(etAddress.getText()))
            return;
        if (TextUtils.isEmpty(getIntent().getStringExtra("prizeId"))) {
            Toast.makeText(DemoApplication.applicationContext, "该奖品无效", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(boolean) btnSubmit.getTag()) {
            Toast.makeText(DemoApplication.applicationContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("prizeid", getIntent().getStringExtra("prizeId"));
        map.put("name", etName.getText().toString());
        map.put("address", etAddress.getText().toString());
        map.put("mobile", etPhone.getText().toString());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.RECEIVE_PRIZE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    setResult(RESULT_OK);
                    Toast.makeText(DemoApplication.applicationContext, "领取成功", Toast.LENGTH_SHORT).show();
                }
                loadingDismiss();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }
}
