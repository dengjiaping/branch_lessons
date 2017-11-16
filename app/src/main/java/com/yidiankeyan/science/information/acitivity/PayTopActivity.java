package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.PayTopAdapter;
import com.yidiankeyan.science.information.entity.PayTopBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

public class PayTopActivity extends BaseActivity {

    private ListView lvPayTop;
    private PtrClassicFrameLayout ptrLayout;
    private PayTopAdapter adapter;
    private List<PayTopBean> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_pay_top;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvPayTop = (ListView) findViewById(R.id.lv_pay_top);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("付费榜");
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                toHttpGetPayTop();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new PayTopAdapter(mData, this);
        lvPayTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (mData.get(position).getEntitytype() == 1) {
                    intent = new Intent(mContext, ColumnDetailsActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(mContext, MozReadDetailsActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });
        lvPayTop.setAdapter(adapter);
    }

    private void toHttpGetPayTop() {
        Map<String, Object> map = new HashMap<>();
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_PAY_TOP, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<PayTopBean> data = GsonUtils.json2List(jsonData, PayTopBean.class);
                    mData.removeAll(mData);
                    mData.addAll(data);
                    adapter.notifyDataSetChanged();
                    ptrLayout.onRefreshComplete();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
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
