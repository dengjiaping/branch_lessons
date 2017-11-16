package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.Button;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.view.ExpandableLinearLayout;


public class TestActivity extends BaseActivity {

    private ExpandableLinearLayout expandLayout;
    private Button btnCollapsed;
    private Button btnExpand;

    @Override
    protected int setContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        expandLayout = (ExpandableLinearLayout) findViewById(R.id.expand_layout);
        btnCollapsed = (Button) findViewById(R.id.btn_collapsed);
        btnExpand = (Button) findViewById(R.id.btn_expand);
    }

    @Override
    protected void initAction() {
        btnCollapsed.setOnClickListener(this);
        btnExpand.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_collapsed:
                expandLayout.collapsed();
                break;
            case R.id.btn_expand:
                expandLayout.expand();
                break;
        }
    }
}
