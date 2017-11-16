package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.adapter.DetailedAllAdapter;
import com.yidiankeyan.science.my.entity.AccountDetailedBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 账号明细
 */
public class MyDetailedAllActivity extends BaseActivity {


    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvMyDetailed;
    private List<AccountDetailedBean> listData = new ArrayList<>();
    private DetailedAllAdapter adapter;
    private int pages = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_detailed_all;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvMyDetailed = (ListViewFinalLoadMore) findViewById(R.id.lv_my_detailed);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("账号明细");
        llReturn.setOnClickListener(this);

        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpPostDetailed();
            }
        });
        if (listData.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvMyDetailed.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new DetailedAllAdapter(this, listData);
        lvMyDetailed.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMyDetailed.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpPostDetailed();
            }
        });
    }

    /**
     * 明细列表
     */
    private void toHttpPostDetailed() {

        ApiServerManager.getInstance().getApiServer().getAccountDetailed(pages, 20).enqueue(new RetrofitCallBack<List<AccountDetailedBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AccountDetailedBean>>> call, Response<RetrofitResult<List<AccountDetailedBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        listData.removeAll(listData);
                    if (response.body().getData().size() > 0) {
                        lvMyDetailed.setHasLoadMore(true);
                        listData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvMyDetailed.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvMyDetailed.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AccountDetailedBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvMyDetailed.showFailUI();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
