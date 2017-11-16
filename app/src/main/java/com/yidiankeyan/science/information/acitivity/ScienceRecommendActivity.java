package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.ScienceRecommendAdapter;
import com.yidiankeyan.science.information.entity.MagazineBean;
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
 * 公告
 * -科技公众号, 网站
 * -更多
 */
public class ScienceRecommendActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;
    private ListView lvScience;
    private ScienceRecommendAdapter adapter;
    private List<MagazineBean> mDatas = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_science_recommend;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvScience = (ListView) findViewById(R.id.lv_science_recommend);
    }

    @Override
    protected void initAction() {
        txtTitle.setText(getIntent().getStringExtra("title"));
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
        adapter = new ScienceRecommendAdapter(this, mDatas);
        if ("科技网站推荐".equals(getIntent().getStringExtra("title")))
            adapter.setShowUnderscores(true);
        lvScience.setAdapter(adapter);
        toHttp();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        String url = "科技网站推荐".equals(getIntent().getStringExtra("title")) ? SystemConstant.URL + SystemConstant.GET_WEBSITE : SystemConstant.URL + SystemConstant.GET_WECHAT;
        HttpUtil.post(this, url, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<MagazineBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), MagazineBean.class);
                    mDatas.removeAll(mDatas);
                    mDatas.addAll(list);
                    adapter.notifyDataSetChanged();
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
