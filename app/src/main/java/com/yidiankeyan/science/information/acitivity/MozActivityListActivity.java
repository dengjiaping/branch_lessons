package com.yidiankeyan.science.information.acitivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MozActivityItemAdapter;
import com.yidiankeyan.science.information.entity.MozClassActivityBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.yidiankeyan.science.view.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

public class MozActivityListActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrLayout;
    private ListView lvMozActivity;
    private int pages = 1;
    private String id;
    private MozActivityItemAdapter itemAdapter;
    private View headView;
    private RollPagerView vpSchoolCourse;
    private ActivityPageAdapter pageAdapter;
    private List<MozClassActivityBean.ActivityListBean> mActivityList = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_moz_activity_list;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvMozActivity = (ListView) findViewById(R.id.lv_moz_activity);
        headView = LayoutInflater.from(this).inflate(R.layout.head_moz_activity_list, null);
        vpSchoolCourse = (RollPagerView) headView.findViewById(R.id.vp_school_course);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("活动列表");
        id = getIntent().getStringExtra("id");
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetActivityList();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        itemAdapter = new MozActivityItemAdapter(this);
        lvMozActivity.addHeaderView(headView);
        lvMozActivity.setAdapter(itemAdapter);
        pageAdapter = new ActivityPageAdapter(vpSchoolCourse, this, mActivityList);
        vpSchoolCourse.setAdapter(pageAdapter);
        vpSchoolCourse.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, MozActivityDetailActivity.class);
                intent.putExtra("id", mActivityList.get(position).getId());
                intent.putExtra("title", mActivityList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void toHttpGetActivityList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        map.put("flashreportId", id);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ACTIVITY_ITEM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    MozClassActivityBean mozClassActivityBean = (MozClassActivityBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), MozClassActivityBean.class);
                    mActivityList.removeAll(mActivityList);
                    mActivityList.addAll(mozClassActivityBean.getActivityList());
                    mozClassActivityBean.getActivityList().removeAll(mozClassActivityBean.getActivityList());
                    itemAdapter.setMozClassActivityBean(mozClassActivityBean);
                    itemAdapter.notifyDataSetChanged();
                    pageAdapter.notifyDataSetChanged();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
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

    public class ActivityPageAdapter extends LoopPagerAdapter {

        private Context mContext;
        private List<MozClassActivityBean.ActivityListBean> mBannerList;

        public ActivityPageAdapter(RollPagerView viewPager, Context mContext, List<MozClassActivityBean.ActivityListBean> mBannerList) {
            super(viewPager);
            this.mContext = mContext;
            this.mBannerList = mBannerList;
            if (mBannerList.size() < 2) {
                vpSchoolCourse.setHintView(null);
            } else {
                vpSchoolCourse.initHint(new ColorPointHintView(mContext, Color.parseColor("#E3AC42"), Color.parseColor("#88ffffff")));
            }
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(mContext).load(Util.getImgUrl(mBannerList.get(position).getCoverimgurl())).error(R.drawable.icon_banner_load)
                    .placeholder(R.drawable.icon_banner_load).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return mBannerList.size();
        }
    }
}
