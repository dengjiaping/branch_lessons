package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 充值中心
 * -充值帮助
 */

public class RechargeHelpActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private WebView wbRechargeHelp;

    @Override
    protected int setContentView() {
        return R.layout.activity_recharge_help;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        wbRechargeHelp= (WebView) findViewById(R.id.web_recharge_help);
    }


    @Override
    protected void initAction() {
        txtTitle.setText("常见问题-充值");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        WebSettings ws = wbRechargeHelp.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕x
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        wbRechargeHelp.loadUrl(SystemConstant.MYURL+"view/fore/appPay/payFAQ.html");
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
