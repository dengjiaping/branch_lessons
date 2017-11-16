package com.yidiankeyan.science.purchase.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.purchase.adapter.RecentAdapter;
import com.yidiankeyan.science.purchase.entity.RecentColumn;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
 * 最新
 */
public class RecentFragment extends Fragment {

    private RecentAdapter adapter;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecent;
    private List<RecentColumn> mLists = new ArrayList<>();
    private int pages = 1;

    public RecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        initView(view);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetRecentColumn();
            }
        });

        if (mLists.size() == 0) {
            ptrLayout.autoRefresh();
        }

        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new RecentAdapter(getContext(), mLists);
        lvRecent.setAdapter(adapter);
        lvRecent.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetRecentColumn();
            }
        });
        return view;
    }

    private void toHttpGetRecentColumn() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_COLUMNS_RECENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    List<RecentColumn> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), RecentColumn.class);
                    if (pages == 1)
                        mLists.removeAll(mLists);
                    if (data.size() > 0) {
                        lvRecent.setHasLoadMore(true);
                        mLists.addAll(data);
                        pages++;
                    } else {
                        lvRecent.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvRecent.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvRecent.showFailUI();
            }
        });
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvRecent = (ListViewFinalLoadMore) view.findViewById(R.id.lv_recent);
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_USER_LOGOUT:
                mLists.removeAll(mLists);
                adapter.notifyDataSetChanged();
                pages = 1;
                lvRecent.setHasLoadMore(false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
