package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.MyEavesdropAdapter;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的偷听界面
 */
public class MyEavesdropFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvMyEavesdrop;
    private int pages = 1;
    private List<NewScienceHelp> mData = new ArrayList<>();
    private MyEavesdropAdapter adapter;

    public MyEavesdropFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_eavesdrop, container, false);
        initView(view);
        if (mData.size() == 0)
            ptrLayout.autoRefresh();
        else {
            lvMyEavesdrop.setHasLoadMore(true);
        }
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetEavesdrop();
            }
        });
        lvMyEavesdrop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetEavesdrop();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new MyEavesdropAdapter(getContext(), mData);
        lvMyEavesdrop.setAdapter(adapter);
        return view;
    }

    private void toHttpGetEavesdrop() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getMyEavesdrop(map).enqueue(new RetrofitCallBack<List<NewScienceHelp>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<NewScienceHelp>>> call, Response<RetrofitResult<List<NewScienceHelp>>> response) {
                if (response.body().getCode() == 200) {
                    List<NewScienceHelp> data = response.body().getData();
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (data.size() > 0) {
                        lvMyEavesdrop.setHasLoadMore(true);
                        mData.addAll(data);
                        pages++;
                    } else {
                        lvMyEavesdrop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvMyEavesdrop.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<NewScienceHelp>>> call, Throwable t) {
                lvMyEavesdrop.showFailUI();
                ptrLayout.onRefreshComplete();
            }
        });
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvMyEavesdrop = (ListViewFinalLoadMore) view.findViewById(R.id.lv_my_eavesdrop);
    }

}
