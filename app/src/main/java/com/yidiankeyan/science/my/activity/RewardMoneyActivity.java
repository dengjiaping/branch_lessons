package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 奖学金
 * -web详情界面
 */
public class RewardMoneyActivity extends BaseActivity {
    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private WebView mWebRewardMoeny;

    @Override
    protected int setContentView() {
        return R.layout.activity_reward_money;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        mWebRewardMoeny = (WebView) findViewById(R.id.web_reward_moeny);
    }

    //
    @Override
    protected void initAction() {
        WebSettings ws = mWebRewardMoeny.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕x
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        ws.setJavaScriptEnabled(true);
        mWebRewardMoeny.loadUrl("http://mozcamp.poinetech.com/webh5/html/activity/scholarship.html");//线上
//        mWebRewardMoeny.loadUrl("http://xtweb.poinetech.com/webh5/html/activity/scholarship.html");//测试
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebRewardMoeny.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
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
