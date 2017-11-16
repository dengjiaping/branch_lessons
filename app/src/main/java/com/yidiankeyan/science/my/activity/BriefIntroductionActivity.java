package com.yidiankeyan.science.my.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 用户个人信息设置
 * -简介
 */
public class BriefIntroductionActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private TextView tvPreservationClick;
    private AutoLinearLayout llPreservationClick;
    private TextView tvHasNum;
    private EditText edtPersonalityAutograph;
    private int num = 200;

    @Override
    protected int setContentView() {
        return R.layout.activity_brief_introduction;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        llPreservationClick = (AutoLinearLayout) findViewById(R.id.ll_preservation_click);
        tvPreservationClick = (TextView) findViewById(R.id.tv_preservation_click);
        tvHasNum = (TextView) findViewById(R.id.tv_hasnum);
        edtPersonalityAutograph = (EditText) findViewById(R.id.edt_personality_autograph);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("修改简介");
        tvPreservationClick.setText("完成");
        tvPreservationClick.setTextColor(Color.parseColor("#e8e8e8"));
        tvPreservationClick.setEnabled(false);
        tvHasNum.setText(SpUtils.getStringSp(this,"mysign").length()+"/200");
        edtPersonalityAutograph.setText(getIntent().getStringExtra("preservation"));
        Editable etext = edtPersonalityAutograph.getText();
        Selection.setSelection(etext, etext.length());
        llReturn.setOnClickListener(this);
        llPreservationClick.setVisibility(View.VISIBLE);
        edtPersonalityAutograph.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
                if(s.length()>0){
                    tvPreservationClick.setText("完成");
                    tvPreservationClick.setTextColor(Color.parseColor("#f1312e"));
                    tvPreservationClick.setEnabled(true);
                }else{
                    tvPreservationClick.setText("完成");
                    tvPreservationClick.setTextColor(Color.parseColor("#e8e8e8"));
                    tvPreservationClick.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                tvHasNum.setText(s.length() + "/200");
                selectionStart = edtPersonalityAutograph.getSelectionStart();
                selectionEnd = edtPersonalityAutograph.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edtPersonalityAutograph.setText(s);
                    edtPersonalityAutograph.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
        tvPreservationClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_preservation_click:
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_BRIEF_INTRODUCTION);
                if (TextUtils.isEmpty(edtPersonalityAutograph.getText())) {
                    msg.setBody("让更多的人认识你吧");
                } else
                    msg.setBody(edtPersonalityAutograph.getText());
                EventBus.getDefault().post(msg);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
