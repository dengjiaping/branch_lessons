package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;

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
 * 墨子FM
 * -最新专辑
 */
public class ScienceFMAllFragment extends Fragment implements AdapterView.OnItemClickListener {

    private WholeAlbumAdapter albumAdapter;
    private ListViewFinalLoadMore lvSciencefmAll;
    private View viewtop;
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private PtrClassicFrameLayout ptrLayout;
    //控制viewpager显示当前的页码
    private int pages = 0;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private Intent intent;
    private List<BannerBean> mBannerList = new ArrayList<>();

    public ScienceFMAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_science_fmall, container, false);
        //初始化控件
        initView(view);

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
        albumAdapter = new WholeAlbumAdapter(getActivity(), mDatas);
        lvSciencefmAll.setAdapter(albumAdapter);
        lvSciencefmAll.setOnItemClickListener(this);
        mLoopAdapter.setOnCustomPageSelect(new LoopPagerAdapter.OnCustomPageSelect() {
            @Override
            public void onPageSelect(ViewGroup container, int position) {
                ImageView view = new ImageView(container.getContext());
                Glide.with(getContext()).load(Util.getImgUrl(mBannerList.get(position).getImgurl())).error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar).into(view);
            }
        });
        toHttpGetBanner();
        return view;
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
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
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


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvSciencefmAll = (ListViewFinalLoadMore) view.findViewById(R.id.lv_sciencefmall);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        lvSciencefmAll.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), getContext(),position);
            }
        });
        mRollViewPager.setAdapter(mLoopAdapter = new BannerAdapter(mRollViewPager, getContext(), mBannerList));
        mRollViewPager.setHintAlpha(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(getContext(), AlbumDetailsActivity.class);
        intent.putExtra("albumId", mDatas.get(position - 1).getAlbumid());
        intent.putExtra("albumName", mDatas.get(position - 1).getName());
        intent.putExtra("albumAvatar", mDatas.get(position - 1).getShortimgurl());
        startActivity(intent);
    }

}
