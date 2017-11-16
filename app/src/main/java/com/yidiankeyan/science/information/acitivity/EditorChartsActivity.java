package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AuthorTopAdapter;
import com.yidiankeyan.science.information.entity.AuthorTop;
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
 * 排行榜
 * -主编榜详情
 */
public class EditorChartsActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;

    private ListView lvAlbCharts;
    private List<AuthorTop> listAlb;
    private AuthorTopAdapter chartsAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_editor_charts;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvAlbCharts = (ListView) findViewById(R.id.lv_editor_charts);
        listAlb = new ArrayList<>();
//        for (int i = 1; i <=30; i++) {
//            listAlb.add("主编排行" + i);
//        }
        chartsAdapter = new AuthorTopAdapter(listAlb, this);
        lvAlbCharts.setAdapter(chartsAdapter);
        //填充数据
        toHttpGetAlbum();
    }

    @Override
    protected void initAction() {
        txtTitle.setText(getIntent().getStringExtra("title"));
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
    }


    /**
     * 获取主编排行榜
     */
    private void toHttpGetAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getIntent().getIntExtra("type", 0));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_AUTHOR_TOP, map, new HttpUtil.HttpCallBack() {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
