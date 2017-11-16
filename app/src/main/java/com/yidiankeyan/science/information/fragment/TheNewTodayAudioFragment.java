package com.yidiankeyan.science.information.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.TheNewTodayAllAdapter;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2017/7/10.
 * 作用：
 */

public class TheNewTodayAudioFragment extends Fragment {

    private RefreshRecyclerView mRecyclerView;
    private TheNewTodayAllAdapter adapter;
    private int currentId;
    private int pages = 1;
    private List<OneDayArticles> mData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentId = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.layout_the_new_today_audio, container, false);
        initView(view);
        if (mData.size() == 0) {
            toHttpGetArticles(currentId);
        }
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new TheNewTodayAllAdapter(getContext());
//        allAdapter.setHeader(headView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetArticles(currentId);
            }
        });
    }

    private void toHttpGetArticles(final int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("tagid", id);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getAllArticles(map).enqueue(new RetrofitCallBack<List<OneDayArticles>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<OneDayArticles>>> call, Response<RetrofitResult<List<OneDayArticles>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        mData.removeAll(mData);
                    }
                    if (response.body().getData().size() > 0) {
                        mData.addAll(response.body().getData());
                        pages++;
                        adapter.clear();
                        adapter.addAll(mData);
                    } else {
                        adapter.mNoMoreView.setText("没有更多了");
                        mRecyclerView.showNoMore();
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    mRecyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<OneDayArticles>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                mRecyclerView.showNoMore();
            }
        });
    }

}
