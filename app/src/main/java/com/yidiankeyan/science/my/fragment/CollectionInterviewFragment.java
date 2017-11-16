package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.yidiankeyan.science.my.adapter.InterviewCollectionAdapter;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * 专访收藏
 */
public class CollectionInterviewFragment extends Fragment {


    private PtrClassicFrameLayout ptrCollection;
    private InterviewCollectionAdapter interviewAdapter;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<InterviewSoonBean> mDatas = new ArrayList<>();
    private int pages = 1;

    public CollectionInterviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_interview, container, false);
        lvSub = (ListViewFinalLoadMore) view.findViewById(R.id.lv_Download);
        ptrCollection = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_collection);
        interviewAdapter = new InterviewCollectionAdapter(getContext(), mDatas);
        lvSub.setAdapter(interviewAdapter);
        ptrCollection.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                pages = 1;
                toHttpGetCollection();
            }
        });
        if (mDatas.size() == 0) {
            ptrCollection.autoRefresh();
        } else {
            lvSub.setHasLoadMore(true);
        }
        lvSub.setHasLoadMore(true);
        ptrCollection.disableWhenHorizontalMove(true);
        ptrCollection.setLastUpdateTimeRelateObject(getContext());
        lvSub.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrCollection.isRefreshing())
                    return;
                toHttpGetCollection();
            }
        });
        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MozInterviewDetailsActivity.class);
                intent.putExtra("id", mDatas.get(position).getId());
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
        return view;
    }


    private void toHttpGetCollection() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", 4);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getInterviewCollection(map).enqueue(new RetrofitCallBack<List<InterviewSoonBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<InterviewSoonBean>>> call, Response<RetrofitResult<List<InterviewSoonBean>>> response) {
                if (response.body().getCode() == 200) {
                    List<InterviewSoonBean> mData = response.body().getData();
                    Iterator<InterviewSoonBean> iterator = mData.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next() == null)
                            iterator.remove();
                    }
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (mData.size() > 0) {
                        lvSub.setHasLoadMore(true);
                        mDatas.addAll(mData);
                        pages++;
                    } else {
                        lvSub.setHasLoadMore(false);
                    }
                    interviewAdapter.notifyDataSetChanged();
                } else {
                    lvSub.showFailUI();
                }
                ptrCollection.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<InterviewSoonBean>>> call, Throwable t) {
                ptrCollection.onRefreshComplete();
                lvSub.showFailUI();
            }
        });
    }

}
