package com.yidiankeyan.science.my.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 我的个人资料
 * -修改用户昵称
 */
public class MyUpdateNameActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private TextView tvPreservationClick;
    private AutoLinearLayout llPreservationClick;
    private EditText etCustomPrice;
    private ImageView imgNameEmpty;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_update_name;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        llPreservationClick = (AutoLinearLayout) findViewById(R.id.ll_preservation_click);
        tvPreservationClick = (TextView) findViewById(R.id.tv_preservation_click);
        etCustomPrice = (EditText) findViewById(R.id.et_set_price);
        imgNameEmpty = (ImageView) findViewById(R.id.img_name_empty);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("编辑昵称");
        tvPreservationClick.setText("完成");
        tvPreservationClick.setTextColor(Color.parseColor("#e8e8e8"));
        tvPreservationClick.setEnabled(false);
        etCustomPrice.setText(getIntent().getStringExtra("userName"));
        llReturn.setOnClickListener(this);
        llPreservationClick.setVisibility(View.VISIBLE);
        etCustomPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etCustomPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tvPreservationClick.setText("完成");
                    tvPreservationClick.setTextColor(Color.parseColor("#f1312e"));
                    tvPreservationClick.setEnabled(true);
                } else {
                    tvPreservationClick.setText("完成");
                    tvPreservationClick.setTextColor(Color.parseColor("#e8e8e8"));
                    tvPreservationClick.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        imgNameEmpty.setOnClickListener(this);
        tvPreservationClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_preservation_click:
                EventMsg msg = EventMsg.obtain(SystemConstant.USER_UPDATE_NAME);
                msg.setBody(etCustomPrice.getText());
                EventBus.getDefault().post(msg);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.img_name_empty:
                etCustomPrice.setText("");
                break;
        }
    }
}
