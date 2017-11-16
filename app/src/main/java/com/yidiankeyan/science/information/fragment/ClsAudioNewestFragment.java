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
import com.yidiankeyan.science.information.adapter.ClassAdapter;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.ClassAllBean;
import com.yidiankeyan.science.subscribe.activity.TransitionActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;

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
 * -音频专辑
 * -最新
 */
public class ClsAudioNewestFragment extends Fragment {

    private ClassAdapter albumAdapter;
    private ListViewFinalLoadMore lvSciencefmAll;
    private View viewtop;
    private List<BannerBean> mBannerList = new ArrayList<>();
    private List<ClassAllBean> mDatas = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pages = 0;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private PtrClassicFrameLayout ptrLayout;

    public ClsAudioNewestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cls_audio_all, container, false);
        //初始化控件
        initView(view);

        //填充数据
        if (mDatas.size() == 0)
//            addData();
            ptrLayout.autoRefresh();
        else
            lvSciencefmAll.setHasLoadMore(true);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetAllTextAlbum();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        albumAdapter = new ClassAdapter(getActivity(), mDatas);
        lvSciencefmAll.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetAllTextAlbum();
            }
        });
        lvSciencefmAll.setAdapter(albumAdapter);
        lvSciencefmAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0) {
                    Intent intent = new Intent(getActivity(), TransitionActivity.class);
                    intent.putExtra("albumId", mDatas.get(position - 1).getAlbumid());
                    intent.putExtra("albumName", mDatas.get(position - 1).getAlbumname());
                    intent.putExtra("albumAvatar", mDatas.get(position - 1).getCoverimgurl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void toHttpGetAllTextAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("classifyType", "NEW");
        entity.put("albumType", 2);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<ClassAllBean> mData = GsonUtils.json2List(jsonData, ClassAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (mData.size() > 0) {
                        lvSciencefmAll.setHasLoadMore(true);
                        mDatas.addAll(mData);
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
        lvSciencefmAll = (ListViewFinalLoadMore) view.findViewById(R.id.lv_audio_newest);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
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
//        mRollViewPager.setHintView(null);
        if (mBannerList.size() == 0)
            toHttpGetBanner();
    }

    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 7);
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
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

}
