package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.adapter.AuthorTopAdapter;
import com.yidiankeyan.science.information.entity.AuthorTop;
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
 * 主编榜
 */
public class EditorChartsRankingFragment extends Fragment {

    private ListView lvAlbCharts;
    private List<AuthorTop> listAlb = new ArrayList<>();
    private AuthorTopAdapter chartsAdapter;

    public EditorChartsRankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editor_charts_ranking, container, false);
        lvAlbCharts = (ListView) view.findViewById(R.id.lv_editor_charts);
        chartsAdapter = new AuthorTopAdapter(listAlb, getContext());
        lvAlbCharts.setAdapter(chartsAdapter);
        //填充数据
        if (listAlb.size() == 0)
            toHttpGetAlbum();
        return view;
    }

    /**
     * 获取主编排行榜
     */
    private void toHttpGetAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 7);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_AUTHOR_TOP, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AuthorTop> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AuthorTop.class);
                    listAlb.addAll(data);
                    chartsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
