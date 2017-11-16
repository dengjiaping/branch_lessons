package com.yidiankeyan.science.information.acitivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.ColumnArticleAdapter;
import com.yidiankeyan.science.information.entity.ColumnQueryArticleBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 专栏详情
 * -文章
 */
public class ColumnArticleActivity extends BaseActivity {

    private int pages = 1;
    private RefreshRecyclerView recyclerView;
    private ColumnArticleAdapter articleAdapter;
    private List<ColumnQueryArticleBean> mList = new ArrayList<>();
    private View rootView;
    private String mId;
    private AutoLinearLayout mLlReturn;
    private TextView mMaintitleTxt;
    private String mTitle;
    private String mBuy;

    public ColumnArticleActivity() {
        // Required empty public constructor
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_column_article;
    }

    @Override
    protected void initView() {
        mId = getIntent().getStringExtra("id");//isBuy
        mTitle = getIntent().getStringExtra("title");
        mBuy = getIntent().getStringExtra("isBuy");
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        mLlReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mLlReturn.setOnClickListener(this);
        mMaintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        mMaintitleTxt.setText(mTitle);
        initAction();
    }

    @Override
    protected void initAction() {
        initRecyclerView();
        articleAdapter.setOnItemClickListener(
                new ColumnArticleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, int isPrice) {
                        DemoApplication.mListArticleselect.add(mList.get(position).getId());
                        DemoApplication.isBuyImagetext = false;
                        if (TextUtils.equals("1", mList.get(position).getHaveYouPurchased()) ||
                                mList.get(position).getFree().equals("1")) {
                            Intent intent = new Intent(ColumnArticleActivity.this, ColArticleDetailActivity.class);
                            intent.putExtra("id", mList.get(position).getId());
                            intent.putExtra("mBuy", mBuy);
                            startActivity(intent);
                        } else {
                            ToastMaker.showShortToast("请先购买");
                        }
                    }
                }

        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initRecyclerView() {
        articleAdapter = new ColumnArticleAdapter(ColumnArticleActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(DemoApplication.applicationContext, 1));
        recyclerView.setAdapter(articleAdapter);
        articleAdapter.isShowNoMore = false;
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initDatas();
            }
        });
    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pages);
        map.put("pageSize", 10);
        map.put("columnId", mId);
        map.put("release", "1");
        map.put("sort", 1);   //默认传入1  倒序
        ApiServerManager.getInstance().getApiServer().getColumnArticle(map).enqueue(new RetrofitCallBack<List<ColumnQueryArticleBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<ColumnQueryArticleBean>>> call, Response<RetrofitResult<List<ColumnQueryArticleBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        mList.removeAll(mList);
                    }
                    if (response.body().getData().size() > 0) {
                        mList.addAll(response.body().getData());
                        pages++;
                        articleAdapter.clear();
                        articleAdapter.addAll(mList);
                    } else {
                        articleAdapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                } else {
                    articleAdapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<ColumnQueryArticleBean>>> call, Throwable t) {
                articleAdapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_COLUMN_FLASH:
                pages = 1;
                initDatas();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
