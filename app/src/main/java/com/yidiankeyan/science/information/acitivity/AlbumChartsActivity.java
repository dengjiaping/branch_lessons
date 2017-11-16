package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AlbumChartsAdapter;
import com.yidiankeyan.science.information.entity.RankingAlbumBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行
 * -专辑榜详情
 */
public class AlbumChartsActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;

    private ListView lvAlbCharts;
    private List<RankingAlbumBean> listAlb;
    private AlbumChartsAdapter chartsAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_album_charts;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvAlbCharts = (ListView) findViewById(R.id.lv_albcharts);
        listAlb = new ArrayList<>();
//        for (int i = 1; i <=30; i++) {
//            listAlb.add("专辑排行" + i);
//        }
        chartsAdapter = new AlbumChartsAdapter(listAlb, this);
        lvAlbCharts.setAdapter(chartsAdapter);
        toHttpGetAlbum();
    }

    /**
     * 获取专辑排行榜
     */
    private void toHttpGetAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getIntent().getIntExtra("type", 0));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_RANKING_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
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

    @Override
    protected void initAction() {
        txtTitle.setText(getIntent().getStringExtra("title"));
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
