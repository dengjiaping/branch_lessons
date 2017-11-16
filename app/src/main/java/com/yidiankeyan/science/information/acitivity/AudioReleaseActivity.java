package com.yidiankeyan.science.information.acitivity;

import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;


public class AudioReleaseActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected int setContentView() {
        return R.layout.activity_audio_release;
    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel(); // Android默认的处理方式
//                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("gb2312");
        webView.loadUrl(url);
    }

    @Override
    protected void initAction() {

    }

    @Override
    public void onClick(View v) {

    }
}
