package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.wx.MD5Util;
import com.zhy.autolayout.AutoLinearLayout;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 账号绑定
 * -手机绑定
 * -设置密码
 */

public class PhoneBindingCodeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private String mozPhone;
    private EditText edtPassWord;
    private TextView tvDisplay;
    private boolean isFo;
    private TextView tvNext;

    @Override
    protected int setContentView() {
        return R.layout.activity_phone_binding_code;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        edtPassWord = (EditText) findViewById(R.id.edt_reset_password);
        tvDisplay = (TextView) findViewById(R.id.tv_display);
        tvNext = (TextView) findViewById(R.id.tv_next_reset);

    }

    @Override
    protected void initAction() {
        txtTitle.setText("账号绑定");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        mozPhone = getIntent().getStringExtra("phone");
        tvDisplay.setOnClickListener(this);
        tvNext.setOnClickListener(this);
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
                    String pwd = MD5Util.MD5Pswd(edtPassWord.getText().toString());
                    ApiServerManager.getInstance().getUserApiServer().getPhonePassWord(mozPhone, pwd).enqueue(new RetrofitCallBack<Object>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                            if (response.body().getCode() == 200) {
                                ToastMaker.showShortToast("绑定成功");
                                Intent intent = new Intent(PhoneBindingCodeActivity.this, PhoneBindingSucceedActivity.class);
                                intent.putExtra("phone", mozPhone);
                                startActivity(intent);
                            } else if (response.body().getCode() == 600) {
                                ToastMaker.showShortToast("账号不存在");
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                        }
                    });
                }
                break;
        }
    }
}
