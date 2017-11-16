package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.adapter.WholeAlbumAdapter;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.magicwindow.mlink.annotation.MLinkRouter;

/**
 * 推荐
 * -墨子FM
 */

@MLinkRouter(keys={"fm"})
public class ScienceFMActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageView titleReturn;
    private ImageButton titleBtn;

    private WholeAlbumAdapter albumAdapter;
    private ListViewFinalLoadMore lvSciencefmAll;
    private View viewtop;
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private PtrClassicFrameLayout ptrLayout;
    //控制viewpager显示当前的页码
    private int pages = 1;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private Intent intent;
    private List<BannerBean> mBannerList = new ArrayList<>();
//    private TextView tvRecentContent;
//    private TextView tvAllAlbum;
//    private TextView tvSubscribedAlbum;
//    private ViewPager viewPager;
//    private MyPagerAdapter adapter;
//    private List<Fragment> fragments;// Tab页面列表
//    private int currIndex = 0;// 当前页卡编号
//    private RecentContentFMFragment recentContentFMFragment;
//    private long currentTime;

    @Override
    protected int setContentView() {
//        currentTime = System.currentTimeMillis();
        return R.layout.activity_science_fm;
    }

    @Override
    protected void initView() {

        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleReturn = (ImageView) findViewById(R.id.title_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);

        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvSciencefmAll = (ListViewFinalLoadMore) findViewById(R.id.lv_sciencefmall);
        viewtop = LayoutInflater.from(this).inflate(R.layout.head_img_list, null);
        lvSciencefmAll.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), ScienceFMActivity.this,position);
            }
        });
        mRollViewPager.setAdapter(mLoopAdapter = new BannerAdapter(mRollViewPager, this, mBannerList));
        mRollViewPager.setHintAlpha(0);
//        tvRecentContent = (TextView) findViewById(R.id.tv_recent_content);
//        tvAllAlbum = (TextView) findViewById(R.id.tv_all_album);
//        tvSubscribedAlbum = (TextView) findViewById(R.id.tv_subscribed_album);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("墨子FM");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
//        fragments = new ArrayList<>();
////        recentContentFMFragment = new RecentContentFMFragment();
////        fragments.add(recentContentFMFragment);
//        fragments.add(new ScienceFMAllFragment());
//        fragments.add(new BookedAlbumForScienceFMFragment());
//        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
//        //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
//        tvRecentContent.setOnClickListener(this);
//        tvAllAlbum.setOnClickListener(this);
//        tvSubscribedAlbum.setOnClickListener(this);
//        tvAllAlbum.setTextColor(Color.parseColor("#F1312E"));

        //填充数据
        if (mDatas.size() == 0)
            ptrLayout.autoRefresh();
        else
            lvSciencefmAll.setHasLoadMore(true);
//        addData();
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetScienceFMAll();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvSciencefmAll.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetScienceFMAll();
            }
        });
        albumAdapter = new WholeAlbumAdapter(this, mDatas);
        lvSciencefmAll.setAdapter(albumAdapter);
        lvSciencefmAll.setOnItemClickListener(this);
        mLoopAdapter.setOnCustomPageSelect(new LoopPagerAdapter.OnCustomPageSelect() {
            @Override
            public void onPageSelect(ViewGroup container, int position) {
                ImageView view = new ImageView(container.getContext());
                Glide.with(getApplication()).load(Util.getImgUrl(mBannerList.get(position).getImgurl())).error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar).into(view);
            }
        });
        toHttpGetBanner();
        llReturn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 3);
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
                    mBannerList.addAll(data);
                    mLoopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    /**
     * 获取赛思fm所有专辑
     */
    private void toHttpGetScienceFMAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongType", 2);
        entity.put("belongId", 1004);
        entity.put("albumType", 2);
        entity.put("classifyType", "NEW");
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BusinessAllBean> data = GsonUtils.json2List(jsonData, BusinessAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (data.size() > 0) {
                        lvSciencefmAll.setHasLoadMore(true);
                        mDatas.addAll(data);
                        pages++;
                    } else {
                        lvSciencefmAll.setHasLoadMore(false);
                    }
                    albumAdapter.notifyDataSetChanged();
                } else {
                    lvSciencefmAll.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvSciencefmAll.showFailUI();
            }
        });
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        recentContentFMFragment.destroyMediaPlayer();
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        Log.e("耗时", "===" + (System.currentTimeMillis() - currentTime));
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
//            case R.id.tv_recent_content:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.tv_all_album:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.tv_subscribed_album:
//                currIndex = 1;
//                viewPager.setCurrentItem(currIndex);
//                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(this, AlbumDetailsActivity.class);
        intent.putExtra("albumId", mDatas.get(position - 1).getAlbumid());
        intent.putExtra("albumName", mDatas.get(position - 1).getName());
        intent.putExtra("albumAvatar", mDatas.get(position - 1).getShortimgurl());
        startActivity(intent);
    }
//
//    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            currIndex = position;
//            setNormal();
//            switch (currIndex) {
////                case 0:
////                    tvRecentContent.setTextColor(Color.parseColor("#F1312E"));
////                    break;
//                case 0:
//                    tvAllAlbum.setTextColor(Color.parseColor("#F1312E"));
//                    break;
//                case 1:
//                    tvSubscribedAlbum.setTextColor(Color.parseColor("#F1312E"));
//                    break;
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
//
//    private void setNormal() {
////        tvRecentContent.setTextColor(Color.parseColor("#999999"));
//        tvAllAlbum.setTextColor(Color.parseColor("#999999"));
//        tvSubscribedAlbum.setTextColor(Color.parseColor("#999999"));
//    }
}
