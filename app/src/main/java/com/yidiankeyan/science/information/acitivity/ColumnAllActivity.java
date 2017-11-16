package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.RecColumnAllAdapter;
import com.yidiankeyan.science.information.entity.ColumnAllListBean;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;


/**
 * 精品专栏
 * -全部列表
 */
public class ColumnAllActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvTitle;
    private RefreshRecyclerView recyclerView;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private CoordinatorLayout llColumnAll;
    private RecColumnAllAdapter recColumnAllAdapter;
    private int pages = 1;
    private ArrayList<ColumnAllListBean> mColumnList = new ArrayList<>();

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_column_all;
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        llColumnAll = (CoordinatorLayout) findViewById(R.id.ll_column_all);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        initRecyclerView();
//        toHttpGetSubColumn();
        findViewById(R.id.img_return).setOnClickListener(this);
    }

    private void initRecyclerView() {
        recColumnAllAdapter = new RecColumnAllAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(recColumnAllAdapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetSubColumn();
            }
        });
        recColumnAllAdapter.setOnItemClickListener(new RecColumnAllAdapter.OnItemClickListener() {
            public Intent intent;

            @Override
            public void onItemClick(int position, int isPrice) {
                DemoApplication.mListselect.add(mColumnList.get(position).getId());
                if(!StringUtils.isEmpty(mColumnList.get(position).getHaveYouPurchased())&&
                        "0".equals(mColumnList.get(position).getHaveYouPurchased())){//getIntent().getStringExtra("name")
                    intent = new Intent(mContext, ColumnIntroductionActivity.class);
                    intent.putExtra("id", mColumnList.get(position).getId());
                    intent.putExtra("name", mColumnList.get(position).getColumnName());
                    intent.putExtra("columnWriterIntro", mColumnList.get(position).getColumnWriterIntro());
                    intent.putExtra("price", mColumnList.get(position).getColumnPrice().replace(".00", ""));
                    intent.putExtra("ishasactivityprice", mColumnList.get(position).getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", mColumnList.get(position).getColumnActivityPrice());
                    mContext.startActivity(intent);
                }else if(!StringUtils.isEmpty(mColumnList.get(position).getHaveYouPurchased())&&
                        "1".equals(mColumnList.get(position).getHaveYouPurchased())){
                    intent = new Intent(mContext, ColumnDetailActivity.class);
                    intent.putExtra("id", mColumnList.get(position).getId());
                    intent.putExtra("name", mColumnList.get(position).getColumnName());
                    intent.putExtra("columnWriterIntro", mColumnList.get(position).getColumnWriterIntro());
                    intent.putExtra("price", mColumnList.get(position).getColumnPrice().replace(".00", ""));
                    intent.putExtra("isBuys", mColumnList.get(position).getHaveYouPurchased());
                    intent.putExtra("ishasactivityprice", mColumnList.get(position).getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", mColumnList.get(position).getColumnActivityPrice());
                    mContext.startActivity(intent);
                }
            }
        });
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
//        //通过CollapsingToolbarLayout修改字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.TOP);////设置展开后标题的位置
        collapsingToolbarLayout.setExpandedTitleMarginTop(Util.dip2px(this, 48));
        collapsingToolbarLayout.setExpandedTitleMarginStart(Util.dip2px(this, 48));
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER | Gravity.TOP);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
        AppBarLayout appBar = (AppBarLayout) findViewById(app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                Log.e("verticalOffset", verticalOffset + "");
//                Log.e("getTotalScrollRange", appBarLayout.getTotalScrollRange() + "");
//                Log.e("dip", Util.dip2px(TheNewTodayAudioActivity.this, 206) + "");
                if (Math.abs(verticalOffset) >= Util.dip2px(ColumnAllActivity.this, 206)) {

                } else {

                }
                if (verticalOffset == 0) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        tvTitle.setBackgroundColor(Color.BLACK);
                        tvTitle.setText("墨子专栏");
                        viewStatusBar.setBackgroundColor(Color.BLACK);
                    }
                } else {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                            tvTitle.setText("");
                            tvTitle.setBackgroundColor(Color.TRANSPARENT);
                            viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
                        }
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                    params.height = params.height + DisplayUtil.getStatueBarHeight(ColumnAllActivity.this);
                    toolbar.setLayoutParams(params);
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        pages = 1;
        toHttpGetSubColumn();
        recColumnAllAdapter.notifyDataSetChanged();
    }


    /**
     * 获取订阅专栏列表
     */
    private void toHttpGetSubColumn() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pages);
        map.put("pageSize", 20);
        map.put("release", 1);
        ApiServerManager.getInstance().getApiServer().getColumns(map).enqueue(new RetrofitCallBack<List<ColumnAllListBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<ColumnAllListBean>>> call, Response<RetrofitResult<List<ColumnAllListBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mColumnList.removeAll(mColumnList);
                    if (response.body().getData().size() > 0) {
                        List<ColumnAllListBean> mData = response.body().getData();
                        mColumnList.addAll(mData);
                        pages++;
                        recColumnAllAdapter.clear();
                        recColumnAllAdapter.addAll(mColumnList);
                    } else {
                        recColumnAllAdapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                    recColumnAllAdapter.notifyDataSetChanged();
                } else {
                    recColumnAllAdapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<ColumnAllListBean>>> call, Throwable t) {
                recColumnAllAdapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                //如果mainActivity不存在则跳转主页面
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }
}
