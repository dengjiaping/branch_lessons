package com.yidiankeyan.science.information.acitivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 科答追问
 */
public class RequestQuestionActivity extends BaseActivity {

    private EditText etContent;
    private Button btnSubmit;

    @Override
    protected int setContentView() {
        return R.layout.activity_request_question;
    }

    @Override
    protected void initView() {
        etContent = (EditText) findViewById(R.id.et_content);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("追问");
        btnSubmit.setOnClickListener(this);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 50) {
                    String s1 = s.toString().substring(0, 50);
                    etContent.setText(s1);
                    etContent.setSelection(s1.length());
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
                if (TextUtils.isEmpty(etContent.getText())) {
                    ToastMaker.showShortToast("问题不能为空");
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("kederid", getIntent().getStringExtra("id"));
                map.put("question", etContent.getText().toString());
                ApiServerManager.getInstance().getApiServer().submitRequestQuestion(map).enqueue(new RetrofitCallBack<Object>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                        if (response.body().getCode() == 200) {
                            ToastMaker.showShortToast("提问成功");
                            setResult(RESULT_OK);
                            finish();
                        } else if (response.body().getCode() == 306) {
                            ToastMaker.showShortToast("超过24小时，不能再追问");
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
