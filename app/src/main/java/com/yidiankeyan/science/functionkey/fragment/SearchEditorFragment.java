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
import com.yidiankeyan.science.functionkey.adapter.SearchEditorAdapter;
import com.yidiankeyan.science.functionkey.entity.SearchEditorBean;
import com.yidiankeyan.science.my.activity.MyHomePageActivity;
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
 * -主编
 */
public class SearchEditorFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvEditorTop;
    private SearchEditorAdapter adapter;
    private List<SearchEditorBean> mDatas;
    private String searchReturn;

    public SearchEditorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_editor, container, false);
        initView(view);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
            initDatas(getContext());
        }
        adapter = new SearchEditorAdapter(mDatas, getContext());
        lvEditorTop.setAdapter(adapter);
        lvEditorTop.setOnItemClickListener(this);
        return view;
    }

    public void initDatas(Context context) {
        if (searchReturn != null && !TextUtils.isEmpty(searchReturn)) {
            Map<String, Object> map = new HashMap();
            map.put("pages", 1);
            map.put("pagesize", 20);
            map.put("type", 3);
            map.put("range", 1);
            map.put("searchstr", searchReturn);

            HttpUtil.post(context, SystemConstant.URL + SystemConstant.POST_SEARCH_SEARCH, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (200 == result.getCode()) {
                        mDatas.removeAll(mDatas);
                        List<SearchEditorBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), SearchEditorBean.class);
                        mDatas.addAll(datas);
                        adapter = new SearchEditorAdapter(mDatas, getContext());
                        lvEditorTop.setAdapter(adapter);
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
        lvEditorTop = (ListView) view.findViewById(R.id.lv_editor_top);
    }

    public void setSearchReturn(String searchReturn) {
        this.searchReturn = searchReturn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), MyHomePageActivity.class);
            intent.putExtra("id", mDatas.get(position).getAuthorid());
            intent.putExtra("name", mDatas.get(position).getName());
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
