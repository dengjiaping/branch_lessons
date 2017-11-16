package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
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
 * -所有专辑
 */
public class ClassAllAblumFragment extends Fragment {


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
    private int id;

    public ClassAllAblumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_class_all_ablum, container, false);
        //初始化控件
        initView(view);

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
        albumAdapter = new WholeAlbumAdapter(getActivity(), mDatas);
        lvWhole.setAdapter(albumAdapter);
        lvWhole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                intent = new Intent(getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position - 1).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position - 1).getName());
                intent.putExtra("albumAvatar", mDatas.get(position - 1).getCoverimgurl());
                startActivity(intent);
            }
        });

        toHttpGetBanner();

        return view;
    }

    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 5);
        map.put("belongid", id);
        map.put("pages",1);
        map.put("pagesize",6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
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
        entity.put("belongid", id);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
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


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvWhole = (ListViewFinalLoadMore) view.findViewById(R.id.lv_cls_allab);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        lvWhole.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), getContext(),position);
            }
        });
        mRollViewPager.setAdapter(mLoopAdapter = new BannerAdapter(mRollViewPager, getContext(), mBannerList));
        mRollViewPager.setHintAlpha(0);
//        mRollViewPager.setHintView(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

}
