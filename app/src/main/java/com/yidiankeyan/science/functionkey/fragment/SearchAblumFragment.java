package com.yidiankeyan.science.functionkey.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.functionkey.adapter.SearchAlbumAdapter;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * -搜索结果
 * -专辑
 */
public class SearchAblumFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvBusAll;
    private SearchAlbumAdapter adapter;
    private List<BusinessAllBean> mDatas;
    private String searchReturn;

    public SearchAblumFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_ablum, container, false);
        initView(view);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
            initDatas(getContext());
        }
        adapter = new SearchAlbumAdapter(getContext(), mDatas);
        lvBusAll.setAdapter(adapter);
        lvBusAll.setOnItemClickListener(this);
        return view;
    }

    public void initDatas(Context context) {
        if (searchReturn != null && !TextUtils.isEmpty(searchReturn)) {
            Map<String, Object> map = new HashMap();
            map.put("pages", 1);
            map.put("pagesize", 20);
            map.put("type", 1);
            map.put("range", 1);
            map.put("searchstr", searchReturn);

            HttpUtil.post(context, SystemConstant.URL + SystemConstant.POST_SEARCH_SEARCH, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (200 == result.getCode()) {
                        mDatas.removeAll(mDatas);
                        List<BusinessAllBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                        mDatas.addAll(datas);
                        adapter = new SearchAlbumAdapter(getContext(), mDatas);
                        lvBusAll.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                }
            });
        }
    }

    private void initView(View view) {
        lvBusAll = (ListView) view.findViewById(R.id.lv_search_album);
    }

    public void setSearchReturn(String searchReturn) {
        this.searchReturn = searchReturn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AlbumDetailsActivity.class);
        intent.putExtra("albumId", mDatas.get(position).getAlbumid());
        intent.putExtra("albumName", mDatas.get(position).getName());
        intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
        startActivity(intent);
    }

    public void clean() {
        if (mDatas != null)
        mDatas.removeAll(mDatas);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        mDatas = null;
    }

    public void loadData(Context context) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
            initDatas(context);
        }
    }
}
