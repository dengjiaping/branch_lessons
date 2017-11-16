package com.yidiankeyan.science.subscribe.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.subscribe.adapter.RewardAdapter;
import com.yidiankeyan.science.subscribe.entity.GratuityPersonInfo;
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

public class RewardTopActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;

    private RewardAdapter adapter;
    private ListView lvReward;
    //页面数据
    private List<GratuityPersonInfo> mDatas = new ArrayList<>();
//    private String imgUrl = "http://img4.imgtn.bdimg.com/it/u=632987630,1244164460&fm=21&gp=0.jpg";

    @Override
    protected int setContentView() {
        return R.layout.activity_reward_top;
    }

    @Override
    protected void initView() {
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        lvReward = (ListView) findViewById(R.id.lv_reward);
    }

    @Override
    protected void initAction() {
        titleBtn.setVisibility(View.GONE);
        txtTitle.setText("打赏记录");
        llReturn.setOnClickListener(this);
        adapter = new RewardAdapter(mDatas, this);
        lvReward.setAdapter(adapter);
        toHttpGetGratuityHistory();
    }

    private void toHttpGetGratuityHistory() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getStringExtra("authorId"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GRATUITY_HISTORY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    mDatas.addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), GratuityPersonInfo.class));
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
