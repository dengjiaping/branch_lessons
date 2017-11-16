package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.adapter.AlbumChartsAdapter;
import com.yidiankeyan.science.information.entity.RankingAlbumBean;
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
 * 图文，音频，视频，订阅榜
 */
public class AlbumChartsRankingFragment extends Fragment {

    private ListView lvAlbCharts;
    private List<RankingAlbumBean> listAlb = new ArrayList<>();
    private AlbumChartsAdapter chartsAdapter;

    public AlbumChartsRankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_charts_ranking, container, false);
        lvAlbCharts = (ListView) view.findViewById(R.id.lv_albcharts);
        chartsAdapter = new AlbumChartsAdapter(listAlb, getContext());
        lvAlbCharts.setAdapter(chartsAdapter);
        if (listAlb.size() == 0)
            toHttpGetAlbum();
        return view;
    }

    /**
     * 获取专辑排行榜
     */
    private void toHttpGetAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getArguments().getInt("type"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_RANKING_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<RankingAlbumBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), RankingAlbumBean.class);
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
