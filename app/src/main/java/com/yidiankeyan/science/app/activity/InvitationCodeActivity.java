package com.yidiankeyan.science.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.entity.PhoneBindingBean;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/24.
 */

public class InvitationCodeActivity extends Activity implements View.OnClickListener {


    private ImageView mivClose;
    private Context mContext;
    private EditText metInvitationCode;
    private Button mbtnCommit;
    private TextView mtvHint;
    private InputMethodManager minputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invitation_code);
        minputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
    }

    private void initView() {
        mivClose = (ImageView) findViewById(R.id.iv_close);
        metInvitationCode = (EditText) findViewById(R.id.et_invitation_code);
        mbtnCommit = (Button) findViewById(R.id.btn_commit); //tv_hint
//        mtvHint = (TextView) findViewById(R.id.tv_hint);
        mivClose.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_commit:
//                mtvHint.setText("");
                String etInvitationCode = metInvitationCode.getText().toString().trim();
                if (StringUtils.isEmpty(etInvitationCode)) {
                    ToastMaker.showShortToast(getResources().getString(R.string.please_input_code));
                }
                toHttpInvitationCode(etInvitationCode);
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                minputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 验证 邀请码是否正确，是就直接关闭邀请码弹框 否则就提示用户输入错误
     */
    private void toHttpInvitationCode(String etInvitationCode) {

        Map<String, Object> map = new HashMap<>();
        map.put("code", etInvitationCode);
        map.put("sourcecode", "");  //暂时直接传kong就行
        ApiServerManager.getInstance().getApiServer().getInvitationCode(map).
                enqueue(new RetrofitCallBack<Object>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<Object>> call,
                                          Response<RetrofitResult<Object>> response) {
                        int code = response.body().getCode();
                        if (!StringUtils.isEmpty(String.valueOf(code)) && code == 200) {
                            //TODO
                            finish();
                            hideSoftKeyboard();
                        } else {
                            ToastMaker.showShortToast(response.body().getMsg());
//                            mtvHint.setText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                    }
                });
    }
}
