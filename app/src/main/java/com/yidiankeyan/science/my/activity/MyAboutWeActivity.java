package com.yidiankeyan.science.my.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 我的
 * -关于我们
 */
public class MyAboutWeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private TextView tvWeMoz;

    private String edition;
    @Override
    protected int setContentView() {
        return R.layout.activity_my_about_we;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        tvWeMoz= (TextView) findViewById(R.id.tv_we_moz);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("关于我们");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        try {
            edition=getVersionName();
            tvWeMoz.setText("墨子学堂 version"+edition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
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
