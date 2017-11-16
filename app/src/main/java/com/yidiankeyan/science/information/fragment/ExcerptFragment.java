package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.MagazineExcerptAdapter;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 杂志详情
 * -节选
 */
public class ExcerptFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private MagazineExcerptAdapter adapter;
    private List<MagazineExcerptBean> mDatas = new ArrayList<>();
    private int pages = 1;
    private RefreshRecyclerView recyclerView;

    private String id;

    public void setId(String id) {
        this.id = id;
        initDatas();
    }

    public ExcerptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_excerpt, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        adapter = new MagazineExcerptAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initDatas();
            }
        });
    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("monthlyId", id);
        map.put("entity", entity);

        ApiServerManager.getInstance().getApiServer().getExcerptList(map).enqueue(new RetrofitCallBack<ArrayList<MagazineExcerptBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<MagazineExcerptBean>>> call, Response<RetrofitResult<ArrayList<MagazineExcerptBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        mDatas.removeAll(mDatas);
                    }
                    if (response.body().getData().size() > 0) {
                        mDatas.addAll(response.body().getData());
                        pages++;
                        adapter.clear();
                        adapter.addAll(mDatas);
                    } else {
                        adapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<MagazineExcerptBean>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }


    private void initView(View view) {
        recyclerView = (RefreshRecyclerView) view.findViewById(R.id.recyclerView);
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_DOWNLOAD_STATE:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
