package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 积分
 *  -使用规则
 */
public class IntegralRuleActivity extends BaseActivity {
    private TextView maintitleTxt;
    private AutoLinearLayout llReturn;

    @Override
    protected int setContentView() {
        return R.layout.activity_integral_rule;
    }

    @Override
    protected void initView() {
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("使用规则");
        llReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
