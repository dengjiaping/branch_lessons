package com.yidiankeyan.science.my.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.AudioControlActivity;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.my.adapter.RikeCollectionAdapter;
import com.yidiankeyan.science.utils.AudioPlayManager;
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
 * Created by nby on 2017/7/19.
 * 作用：
 */

public class CollectionRikeFragment extends Fragment {

    private PtrClassicFrameLayout ptrCollection;
    private RikeCollectionAdapter adapter;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<OneDayArticles> mDatas = new ArrayList<>();
    private int pages = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection_graphic, container, false);
        lvSub = (ListViewFinalLoadMore) view.findViewById(R.id.lv_Download);
        ptrCollection = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_collection);
        adapter = new RikeCollectionAdapter(getContext(), mDatas);
        lvSub.setAdapter(adapter);
        ptrCollection.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                pages = 1;
                toHttpGetCollection();
            }
        });
        if (mDatas.size() == 0)
            ptrCollection.autoRefresh();
        lvSub.setHasLoadMore(true);
        ptrCollection.disableWhenHorizontalMove(true);
        ptrCollection.setLastUpdateTimeRelateObject(getContext());
        lvSub.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetCollection();
            }
        });
        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(mDatas.get(position).getId(), AudioPlayManager.getInstance().getCurrId())) {
                    startActivity(new Intent(getContext(), AudioControlActivity.class));
                } else {
                    List<OneDayArticles> list = new ArrayList<>();
                    list.add(mDatas.get(position));
                    AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                    startActivity(new Intent(getContext(), AudioControlActivity.class));
                }
            }
        });
        return view;
    }

    private void toHttpGetCollection() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", 8);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getOneDayArticlesCollection(map).enqueue(new RetrofitCallBack<List<OneDayArticles>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<OneDayArticles>>> call, Response<RetrofitResult<List<OneDayArticles>>> response) {
                if (response.body().getCode() == 200) {
                    List<OneDayArticles> mData = response.body().getData();
                    Iterator<OneDayArticles> iterator = mData.iterator();
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
                    adapter.notifyDataSetChanged();
                } else {
                    lvSub.showFailUI();
                }
                ptrCollection.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<OneDayArticles>>> call, Throwable t) {
                ptrCollection.onRefreshComplete();
                lvSub.showFailUI();
            }
        });
    }
}
