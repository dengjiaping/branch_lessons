package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MoreLectureAdapter;
import com.yidiankeyan.science.information.entity.NotifyActivityBean;
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
 * -讲座信息
 * -更多
 */
public class MoreLectureActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;
    private ListView lvBusAll;
    private MoreLectureAdapter adapter;
    private List<NotifyActivityBean> mDatas = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_more_lecture;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvBusAll = (ListView) findViewById(R.id.lv_more_lecture);
    }

    @Override
    protected void initAction() {
        txtTitle.setText(getIntent().getStringExtra("title"));
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
        adapter = new MoreLectureAdapter(this, mDatas);
        adapter.setType(getIntent().getStringExtra("title"));
        lvBusAll.setAdapter(adapter);
        toHttp();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        if ("讲座信息".equals(getIntent().getStringExtra("title")))
            map.put("type", 1);
        else
            map.put("type", 2);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_NOTIFY_ACTIVITY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<NotifyActivityBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NotifyActivityBean.class);
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
