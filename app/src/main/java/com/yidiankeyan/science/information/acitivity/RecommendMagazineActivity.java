package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.RecommendMagazineAdapter;
import com.yidiankeyan.science.information.entity.MagazineBean;
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
 * 科技杂志推荐
 */
public class RecommendMagazineActivity extends BaseActivity {

    private ListView lvMagazine;
    private RecommendMagazineAdapter adapter;
    private List<MagazineBean> mDatas = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_recommend_magazine;
    }

    @Override
    protected void initView() {
        lvMagazine = (ListView) findViewById(R.id.lv_magazine);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText(getIntent().getStringExtra("title"));
        findViewById(R.id.ll_return).setOnClickListener(this);
        findViewById(R.id.title_btn).setVisibility(View.GONE);
        adapter = new RecommendMagazineAdapter(this, mDatas);
        lvMagazine.setAdapter(adapter);
        toHttp();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_MAGAZINE, map, new HttpUtil.HttpCallBack() {
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
