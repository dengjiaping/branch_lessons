package com.yidiankeyan.science.information.acitivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MagazineAllAdapter;
import com.yidiankeyan.science.information.entity.MozMagazineAllBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 墨子杂志
 * -杂志列表
 */
public class MozMagazineAllActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private MagazineAllAdapter allAdapter;
    private PtrClassicFrameLayout ptrLayout;
    private List<MozMagazineAllBean> listAll = new ArrayList<>();
    private ListViewFinalLoadMore lvMagazineAll;
    private int pages = 1;
    private IntentFilter intentFilter;

    @Override
    protected int setContentView() {
        return R.layout.activity_moz_magazine_all;
    }

    @Override
    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.read.audio");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvMagazineAll = (ListViewFinalLoadMore) findViewById(R.id.lv_magazine_all);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("杂志列表");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);

        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetMagazine();
            }
        });

        if (listAll.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvMagazineAll.setHasLoadMore(true);
        }

        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        allAdapter = new MagazineAllAdapter(listAll, this);
        lvMagazineAll.setAdapter(allAdapter);

        lvMagazineAll.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetMagazine();
            }
        });
    }

    private void toHttpGetMagazine() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("checkstatus",2);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().GetMagazineAll(map).enqueue(new RetrofitCallBack<List<MozMagazineAllBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<MozMagazineAllBean>>> call, Response<RetrofitResult<List<MozMagazineAllBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        listAll.removeAll(listAll);
                    if (response.body().getData().size() > 0) {
                        lvMagazineAll.setHasLoadMore(true);
                        List<MozMagazineAllBean> mData = response.body().getData();
                        listAll.addAll(mData);
                        pages++;
                    } else {
                        lvMagazineAll.setHasLoadMore(false);
                    }
                    allAdapter.notifyDataSetChanged();
                } else {
                    lvMagazineAll.showFailUI();
                }
                ptrLayout.onRefreshComplete();

            }

            @Override
            public void onFailure(Call<RetrofitResult<List<MozMagazineAllBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvMagazineAll.showFailUI();
            }
        });
    }



    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.read.audio")) {
                ptrLayout.autoRefresh();
            }
        }
    };


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
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
