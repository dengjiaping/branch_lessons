package com.yidiankeyan.science.my.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.my.activity.MyRecommendFollowActivity;
import com.yidiankeyan.science.my.adapter.FocusTagAdapter;
import com.yidiankeyan.science.my.adapter.MyRecomFollowAdapter;
import com.yidiankeyan.science.my.entity.FocusBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

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

public class FocusTagFragment extends Fragment implements View.OnClickListener {
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvTag;
    private List<FocusBean> mData = new ArrayList<>();
    private FocusTagAdapter adapter;
    private int pages = 1;

    private View headView;
    //推荐关注
    private MyRecomFollowAdapter mAdapter;
    private ArrayList<RecommendFollowBean> magazineLists = new ArrayList<>();
    private RecyclerView idRecyclerHorizontal;
    private AutoLinearLayout llHorizontalRec;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_focus_tag, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetMagazineList();
                toHttpGetFocusList();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvTag.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetFocusList();
            }
        });
        adapter = new FocusTagAdapter(mData, getContext());
        lvTag.addHeaderView(headView);
        lvTag.setAdapter(adapter);
        //设置水平横向滑动的参数
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        idRecyclerHorizontal.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecomFollowAdapter(getContext(), magazineLists);
        idRecyclerHorizontal.setAdapter(mAdapter);
        llHorizontalRec.setOnClickListener(this);
    }

    private void toHttpGetFocusList() {
        Map<String, Object> map = new ArrayMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        ApiServerManager.getInstance().getApiServer().getFocusList(map).enqueue(new RetrofitCallBack<List<FocusBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FocusBean>>> call, Response<RetrofitResult<List<FocusBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvTag.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvTag.setHasLoadMore(false);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    lvTag.showFailUI();
                }
                ptrLayout.onRefreshComplete();

            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FocusBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvTag.showFailUI();
            }
        });
    }

    /**
     * 获取推荐关注列表
     */
    private void toHttpGetMagazineList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 5);
        ApiServerManager.getInstance().getApiServer().getRecommendFollow(map).enqueue(new RetrofitCallBack<List<RecommendFollowBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<RecommendFollowBean>>> call, Response<RetrofitResult<List<RecommendFollowBean>>> response) {
                if (response.body().getCode() == 200) {
                    magazineLists.removeAll(magazineLists);
                    if (response.body().getData().size() > 0) {
                        magazineLists.addAll(response.body().getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llHorizontalRec.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<RecommendFollowBean>>> call, Throwable t) {
            }
        });
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
        lvTag = (ListViewFinalLoadMore) view.findViewById(R.id.lv_tag);
        headView = LayoutInflater.from(getContext()).inflate(R.layout.item_top__my_focus, null, false);
        idRecyclerHorizontal = (RecyclerView) headView.findViewById(R.id.id_recyclerview_horizontal);
        llHorizontalRec = (AutoLinearLayout) headView.findViewById(R.id.ll_horizontal_rec);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_horizontal_rec:
                //推荐关注列表
                startActivity(new Intent(getContext(), MyRecommendFollowActivity.class));
                break;
        }
    }
}
