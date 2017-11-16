package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.ColumnContentAdapter;
import com.yidiankeyan.science.information.entity.FreeIssues;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 试看内容列表
 */
public class ColumnContentListActivity extends BaseActivity {

    private ListViewFinalLoadMore lvColumnContent;
    private PtrClassicFrameLayout ptrLayout;
    private ColumnContentAdapter adapter;
    private List<FreeIssues> mData = new ArrayList<>();
    private int pages = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_column_content_list;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvColumnContent = (ListViewFinalLoadMore) findViewById(R.id.lv_column_content);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText(getIntent().getStringExtra("name"));
        findViewById(R.id.ll_return).setOnClickListener(this);
        adapter = new ColumnContentAdapter(this, mData);
        lvColumnContent.setAdapter(adapter);
        lvColumnContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, PreviewColumnContentActivity.class);
                intent.putExtra("id", mData.get(i).getId());
                startActivity(intent);
            }
        });
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetIssues();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvColumnContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetIssues();
            }
        });
    }

    private void toHttpGetIssues() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("columnid", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_COLUMNS_FREE_ISSUES_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<FreeIssues> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), FreeIssues.class);
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (data.size() > 0) {
                        lvColumnContent.setHasLoadMore(true);
                        mData.addAll(data);
                        pages++;
                    } else {
                        lvColumnContent.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvColumnContent.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvColumnContent.showFailUI();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
