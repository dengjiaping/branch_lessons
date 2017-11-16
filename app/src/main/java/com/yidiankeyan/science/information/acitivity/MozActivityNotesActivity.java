package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.SystemConstant;


/**
 * 活动须知
 */
public class MozActivityNotesActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected int setContentView() {
        return R.layout.activity_moz_activity_notes;
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.web_view);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText("活动须知");
        findViewById(R.id.ll_return).setOnClickListener(this);
        webView.loadUrl(SystemConstant.MYURL + "view/fore/flashreport/action.jsp");
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
