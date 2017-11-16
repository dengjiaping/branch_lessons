package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.DownLoadImageService;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 专栏
 * -海报
 */
public class ColumnPosterActivity extends BaseActivity {

    private WebView imgPoster;
    private String poster;
    private TextView tvSaveMoney;
    private LinearLayout llReturn;

    @Override
    protected int setContentView() {
        return R.layout.activity_column_poster;
    }

    @Override
    protected void initView() {
        imgPoster = (WebView) findViewById(R.id.img_poster);
        tvSaveMoney = (TextView) findViewById(R.id.tv_save_money);
        llReturn = (LinearLayout) findViewById(R.id.ll_return);
    }

    @Override
    protected void initAction() {
        poster = getIntent().getStringExtra("poster");
        imgPoster.loadUrl(SystemConstant.ACCESS_IMG_HOST + poster);
        WebSettings settings = imgPoster.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        llReturn.setOnClickListener(this);
        tvSaveMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_save_money:
                showWaringDialog(
                        "提示",
                        "将海报保存到相册？",
                        new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                onDownLoad();
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        }
                );
                break;
        }
    }

    private void onDownLoad() {
        DownLoadImageService service = new DownLoadImageService(this,
                SystemConstant.ACCESS_IMG_HOST + poster,
                new DownLoadImageService.ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(final File file) {
                        // 在这里执行图片保存方法
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MediaStore.Images.Media.insertImage(getContentResolver(),
                                            file.getAbsolutePath(), "/temp_avatar_crop.jpg", null);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
//                                FileUtils.copyFile(file, Util.getSDCardPath() + "/temp_avatar_crop.jpg", true);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Util.getSDCardPath()))));
                                ToastMaker.showShortToast("保存成功");
                            }
                        });
                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }
}
