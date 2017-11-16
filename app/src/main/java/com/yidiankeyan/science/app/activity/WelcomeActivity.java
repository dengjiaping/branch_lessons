package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.adapter.GuidePageAdapter;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends FragmentActivity {

    private ViewPager vpWelcome;
    //    private LinearLayout llPoints;
//    private ImageView imageView;
//    private ImageView[] imageViews;
    private View start;
    private ArrayList<View> pageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initAction();
    }

    protected void initView() {
        vpWelcome = (ViewPager) findViewById(R.id.vp_welcome);
//        llPoints = (LinearLayout) findViewById(R.id.ll_points);
    }

    protected void initAction() {
        Window window = getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.activity_welcome_01, null));
        pageViews.add(inflater.inflate(R.layout.activity_welcome_02, null));
        pageViews.add(inflater.inflate(R.layout.activity_welcome_03, null));
        pageViews.add(inflater.inflate(R.layout.activity_welcome_04, null));
//        ((ImageView) pageViews.get(0).findViewById(R.id.image)).setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.welcome01));
//        ((ImageView) pageViews.get(1).findViewById(R.id.image)).setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.welcome02));
//        ((ImageView) pageViews.get(2).findViewById(R.id.image)).setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.welcome03));
        Glide.with(this).load(R.drawable.welcome01).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(((ImageView) pageViews.get(0).findViewById(R.id.image)));
        Glide.with(this).load(R.drawable.welcome02).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(((ImageView) pageViews.get(1).findViewById(R.id.image)));
        Glide.with(this).load(R.drawable.welcome03).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(((ImageView) pageViews.get(2).findViewById(R.id.image)));
        Glide.with(this).load(R.drawable.welcome04).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(((ImageView) pageViews.get(3).findViewById(R.id.image)));
//        ((ImageView) pageViews.get(3).findViewById(R.id.image)).setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.welcome04));
        start = pageViews.get(3).findViewById(R.id.btn_starts);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SystemConstant.ScreenWidth = Util.getScreenWidth(WelcomeActivity.this);
                SystemConstant.ScreenHeight = Util.getScreenHeight(WelcomeActivity.this);
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        imageViews = new ImageView[pageViews.size()];
//        for (int i = 0; i < pageViews.size(); i++) {
//            imageView = new ImageView(WelcomeActivity.this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(20, 0, 20, 0);
//            imageView.setLayoutParams(params);
//            imageViews[i] = imageView;
//            if (i == 0) {
//                // 默认选中第一张图片
//                imageViews[i]
//                        .setBackgroundResource(R.drawable.welcome_on);
//            } else {
//                imageViews[i].setBackgroundResource(R.drawable.welcome_off);
//            }
//            llPoints.addView(imageViews[i]);
        vpWelcome.setAdapter(new GuidePageAdapter(pageViews));
//            vpWelcome.setOnPageChangeListener(new GuidePageChangeListener());
//        }

        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 1);
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
//            for (int i = 0; i < imageViews.length; i++) {
//                imageViews[position]
//                        .setBackgroundResource(R.drawable.welcome_on);
//                if (position != i) {
//                    imageViews[i]
//                            .setBackgroundResource(R.drawable.welcome_off);
//                }
//            }
            if (position == pageViews.size() - 1) {
                //welcome_tv.setTextColor(Color.argb(255, 0, 255, 0));   //文字全不透明度
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        SystemConstant.ScreenWidth = Util.getScreenWidth(WelcomeActivity.this);
                        SystemConstant.ScreenHeight = Util.getScreenHeight(WelcomeActivity.this);
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setContentView(R.layout.empty_view);
        pageViews.clear();
        vpWelcome.clearOnPageChangeListeners();
        vpWelcome = null;
//        llPoints = null;
//        imageView = null;
//        imageViews = null;
        start = null;
        pageViews = null;
        System.gc();
    }
}
