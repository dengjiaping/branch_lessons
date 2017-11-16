package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 充值协议
 */
public class RechargeAgreementActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private WebView wbRecharge;

    @Override
    protected int setContentView() {
        return R.layout.activity_recharge_agreement;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        wbRecharge = (WebView) findViewById(R.id.wb_recharge);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("墨子学堂用户充值协议");
        llReturn.setOnClickListener(this);

        //启用支持javascript
        WebSettings settings = wbRecharge.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载web资源
        wbRecharge.loadUrl(SystemConstant.MYURL + "view/fore/appPay/payProtocol.html");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wbRecharge.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

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
