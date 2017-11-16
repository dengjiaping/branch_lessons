package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.my.fragment.HomePageFragment;
import com.yidiankeyan.science.my.fragment.MyAlbumFragment;
import com.yidiankeyan.science.my.fragment.SingleArticleFragment;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 我的
 * -我的主页
 */
public class MyHomePageActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private ImageView ivUserHead;
    private TextView tvName;
    private TextView tvMyFans;
    private TextView tvHomePage;
    private TextView tvAlbum;
    private TextView tvArticle;
    private ViewPager viewPager;
    private AutoLinearLayout llMyHomePage;
    private PopupWindow qadPopupWindow;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private HomePageFragment homePageFragment;
    private MyAlbumFragment myAlbumFragment;
    private SingleArticleFragment singleArticleFragment;
    private String userId;
    private TextView tvHomeFollow;
    private UserInforMation user;
    private TextView tvFinish, tvYesClick;
    private AutoLinearLayout llHomeFans;
    private boolean isFu;

    private int isSelect;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_home_page;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        ivUserHead = (ImageView) findViewById(R.id.iv_user_head);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvMyFans = (TextView) findViewById(R.id.tv_my_fans);
        tvHomeFollow = (TextView) findViewById(R.id.tv_home_follow);
        tvHomePage = (TextView) findViewById(R.id.tv_home_page);
        tvAlbum = (TextView) findViewById(R.id.tv_album);
        tvArticle = (TextView) findViewById(R.id.tv_article);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        llHomeFans = (AutoLinearLayout) findViewById(R.id.ll_home_fans);
        llMyHomePage = (AutoLinearLayout) findViewById(R.id.ll_my_home_page);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        userId = getIntent().getStringExtra("id");
        if (TextUtils.equals(userId, SpUtils.getStringSp(this, "userId"))) {
            tvHomeFollow.setVisibility(View.GONE);
        } else {
            tvHomeFollow.setVisibility(View.VISIBLE);
        }
        toHttpGetUserInfo();
        singleArticleFragment = new SingleArticleFragment();
        Bundle singleArticleBundle = new Bundle();
        singleArticleBundle.putString("id", userId);
        singleArticleFragment.setArguments(singleArticleBundle);

        myAlbumFragment = new MyAlbumFragment();
//        if (!TextUtils.isEmpty(userId)) {
        Bundle myAlbumBundle = new Bundle();
        myAlbumBundle.putString("id", userId);
        myAlbumFragment.setArguments(myAlbumBundle);
//        }

        homePageFragment = new HomePageFragment();
//        if (!TextUtils.isEmpty(userId)) {
        Bundle homePageBundle = new Bundle();
        homePageBundle.putString("id", userId);
        homePageBundle.putString("name", SpUtils.getStringSp(this, "userName"));
        homePageFragment.setArguments(homePageBundle);
//        }
        if (((MainActivity) DemoApplication.getInstance().getActivity(MainActivity.class)).currentTabIndex == 2) {
            //消息
            fragments = new ArrayList<>();
            fragments.add(singleArticleFragment);
            fragments.add(myAlbumFragment);
            fragments.add(homePageFragment);
            adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
            //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
            viewPager.setOffscreenPageLimit(3);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(2);
            viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
            llReturn.setOnClickListener(this);
//        tvRecentContent.setOnClickListener(this);
            tvArticle.setOnClickListener(this);
            tvAlbum.setOnClickListener(this);
            tvHomePage.setOnClickListener(this);
            tvHomePage.setTextColor(Color.parseColor("#F1312E"));
        } else {
            //其他
            fragments = new ArrayList<>();
            fragments.add(singleArticleFragment);
            fragments.add(myAlbumFragment);
            fragments.add(homePageFragment);
            adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
            //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
            viewPager.setOffscreenPageLimit(3);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
            llReturn.setOnClickListener(this);
//        tvRecentContent.setOnClickListener(this);
            tvArticle.setOnClickListener(this);
            tvAlbum.setOnClickListener(this);
            tvHomePage.setOnClickListener(this);
            tvArticle.setTextColor(Color.parseColor("#F1312E"));
        }
    }

    private void toHttpGetUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    tvName.setText(user.getUsername());
                    Glide.with(MyHomePageActivity.this).load(Util.getImgUrl(user.getUserimgurl())).bitmapTransform(new CropCircleTransformation(MyHomePageActivity.this))
                            .placeholder(R.drawable.icon_default_avatar)
                            .error(R.drawable.icon_default_avatar)
                            .into(ivUserHead);
                    tvMyFans.setText(user.getFollowernum() + " 粉丝");

                    if (user.getIsfocus() == 0) {
                        isFu = false;
                        tvHomeFollow.setText("+关注");
                        tvHomeFollow.setBackgroundResource(R.drawable.today_item_frame);
                        tvHomeFollow.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        isFu = true;
                        tvHomeFollow.setText("已关注");
                        tvHomeFollow.setBackgroundResource(R.drawable.today_item_frames);
                        tvHomeFollow.setTextColor(Color.parseColor("#f1312e"));
                    }
                    tvHomeFollow.setOnClickListener(MyHomePageActivity.this);
                    llHomeFans.setOnClickListener(MyHomePageActivity.this);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_article:
                currIndex = 0;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_album:
                currIndex = 1;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_home_page:
                currIndex = 2;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.ll_home_fans:
                Intent intent = new Intent(this, MyFansActivity.class);
                intent.putExtra("userId", getIntent().getStringExtra("id"));
                startActivity(intent);
                break;
            case R.id.tv_home_follow:
                if (isFu) {
                    //取消关注
                    FollowCustomPop();
                } else {
                    //加关注
                    Map<String, Object> map = new HashMap<>();
                    map.put("targetid", userId);
                    map.put("oparetion", 1);
                    HttpUtil.post(this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            if (result.getCode() == 200) {
                                isFu = true;
//                            rlFollowClicks.setVisibility(View.VISIBLE);
//                            rlFollowClick.setVisibility(View.GONE);

//                                tvHomeFollow.setText("已关注");
//                                tvHomeFollow.setBackgroundResource(R.drawable.today_item_frames);
//                                tvHomeFollow.setTextColor(Color.parseColor("#f1312e"));

                                toHttpGetUserInfo();
                            }
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {

                        }
                    });
                }
                break;
            case R.id.tv_no_finish:
                finishPop(qadPopupWindow);
//                rlFollowClicks.setVisibility(View.VISIBLE);
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                //取消关注
                finishPop(qadPopupWindow);
                Map<String, Object> maps = new HashMap<>();
                maps.put("targetid", userId);
                maps.put("oparetion", 0);
                HttpUtil.post(this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
//                            rlFollowClick.setVisibility(View.VISIBLE);
//                            rlFollowClicks.setVisibility(View.GONE);
                            isFu = false;

//                            tvHomeFollow.setText("+关注");
//                            tvHomeFollow.setBackgroundResource(R.drawable.today_item_frame);
//                            tvHomeFollow.setTextColor(Color.parseColor("#ffffff"));
                            toHttpGetUserInfo();
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                break;
        }
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
                    tvArticle.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
                    tvAlbum.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 2:
                    tvHomePage.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        tvArticle.setTextColor(Color.parseColor("#ffffff"));
        tvAlbum.setTextColor(Color.parseColor("#ffffff"));
        tvHomePage.setTextColor(Color.parseColor("#ffffff"));


    }

    private void FollowCustomPop() {
        if (qadPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            qadPopupWindow = new PopupWindow(view, -2, -2);
            qadPopupWindow.setContentView(view);
            qadPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            qadPopupWindow.setFocusable(true);
            qadPopupWindow.setOutsideTouchable(true);
            qadPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            qadPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(qadPopupWindow);
                }
            });
            qadPopupWindow.showAtLocation(llMyHomePage, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            qadPopupWindow.showAtLocation(llMyHomePage, Gravity.CENTER, 0, 0);
        }
//        rlFollowClicks.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏PopupWindow
     *
     * @param popupWindow 要隐藏的PopupWindow
     */
    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
        EventBus.getDefault().post(msg);
    }
}
