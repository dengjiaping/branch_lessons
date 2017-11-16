package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
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

/**
 * 分类
 * -按专题分类
 * -item
 */
public class ClassClickItemActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
//    private ViewPager viewPager;// 页卡内容
////    private TextView txtContent;
//    private TextView txtAllAlbum, txtBooked;// 选项名称
//    private List<Fragment> fragments;// Tab页面列表
//    private int currIndex = 0;// 当前页卡编号
//    private MyPagerAdapter adapter;
//    //    private ClassNewsContentFragment classNewsContentFragment;
//    private ClassAllAblumFragment classAllAblumFragment;
//    private ClassBookedFragment classBookedFragment;


    private WholeAlbumAdapter albumAdapter;
    private ListViewFinalLoadMore lvWhole;
    private View viewtop;
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private PtrClassicFrameLayout ptrLayout;
    //控制viewpager显示当前的页码
    private int pages = 0;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private List<BannerBean> mBannerList = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_class_click_item;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
//        txtContent = (TextView) findViewById(R.id.txt_cls_content);
//        txtAllAlbum = (TextView) findViewById(R.id.txt_cls_all);
//        txtBooked = (TextView) findViewById(R.id.txt_cls_booked);
//        viewPager = (ViewPager) findViewById(R.id.vp_cls_ablum);


        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvWhole = (ListViewFinalLoadMore) findViewById(R.id.lv_cls_allab);
        viewtop = LayoutInflater.from(this).inflate(R.layout.head_img_list, null);
        lvWhole.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), ClassClickItemActivity.this,position);
            }
        });
        mRollViewPager.setAdapter(mLoopAdapter = new BannerAdapter(mRollViewPager, this, mBannerList));
        mRollViewPager.setHintAlpha(0);
    }

    @Override
    protected void initAction() {
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
//        fragments = new ArrayList<>();
//        classNewsContentFragment = new ClassNewsContentFragment();
//        classAllAblumFragment = new ClassAllAblumFragment();
//        classBookedFragment = new ClassBookedFragment();
//        if (id > 0) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("id", id);
////            classNewsContentFragment.setArguments(bundle);
//            classAllAblumFragment.setArguments(bundle);
//            classBookedFragment.setArguments(bundle);
//        }
////        fragments.add(classNewsContentFragment);
//        fragments.add(classAllAblumFragment);
//        fragments.add(classBookedFragment);
//        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
//        viewPager.setAdapter(adapter);
////        txtContent.setOnClickListener(this);
//        txtAllAlbum.setOnClickListener(this);
//        txtBooked.setOnClickListener(this);
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
////        txtContent.setTextColor(Color.parseColor("#F1312E"));
//        txtAllAlbum.setTextColor(Color.parseColor("#F1312E"));
        txtTitle.setText(getIntent().getStringExtra("title"));


        //填充数据
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvWhole.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetData();
            }
        });
        lvWhole.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetData();
            }
        });
        albumAdapter = new WholeAlbumAdapter(this, mDatas);
        lvWhole.setAdapter(albumAdapter);
        lvWhole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                intent = new Intent(ClassClickItemActivity.this, AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position - 1).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position - 1).getName());
                intent.putExtra("albumAvatar", mDatas.get(position - 1).getCoverimgurl());
                startActivity(intent);
            }
        });

        toHttpGetBanner();
    }


    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 5);
        map.put("belongid",  getIntent().getStringExtra("id"));
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
                    mBannerList.addAll(data);
                    mLoopAdapter.notifyDataSetChanged();
                }
                if (mBannerList.size() == 0)
                    mRollViewPager.setVisibility(View.GONE);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mBannerList.size() == 0)
                    mRollViewPager.setVisibility(View.GONE);
            }
        });
    }

    private void toHttpGetData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 1);
        entity.put("belongid",  getIntent().getStringExtra("id"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BusinessAllBean> data = GsonUtils.json2List(jsonData, BusinessAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (data.size() > 0) {
                        lvWhole.setHasLoadMore(true);
                        mDatas.addAll(data);
                        pages++;
                    } else {
                        lvWhole.setHasLoadMore(false);
                    }
                    albumAdapter.notifyDataSetChanged();
                } else {
                    lvWhole.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvWhole.showFailUI();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
//        classNewsContentFragment.destroyMediaPlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
//            case R.id.txt_cls_content:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.txt_cls_all:
//                currIndex = 1;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.txt_cls_booked:
//                currIndex = 2;
//                viewPager.setCurrentItem(currIndex);
//                break;

//            case R.id.txt_cls_all:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.txt_cls_booked:
//                currIndex = 1;
//                viewPager.setCurrentItem(currIndex);
//                break;
        }
    }


//    /**
//     * 为选项卡绑定监听器
//     */
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
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
////                    txtContent.setTextColor(Color.parseColor("#F1312E"));
////                    break;
//                case 0:
//                    txtAllAlbum.setTextColor(Color.parseColor("#F1312E"));
//                    break;
//                case 1:
//                    txtBooked.setTextColor(Color.parseColor("#F1312E"));
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
////        txtContent.setTextColor(Color.parseColor("#999999"));
//        txtAllAlbum.setTextColor(Color.parseColor("#999999"));
//        txtBooked.setTextColor(Color.parseColor("#999999"));
//    }

}
