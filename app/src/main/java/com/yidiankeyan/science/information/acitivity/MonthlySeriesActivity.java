package com.yidiankeyan.science.information.acitivity;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MonthlyAllAdapter;
import com.yidiankeyan.science.information.entity.MagazineDetailsBean;
import com.yidiankeyan.science.information.entity.MonthlySeriesBean;
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
import static com.yidiankeyan.science.R.id.toolbar;


/**
 * 月刊系列列表
 */
public class MonthlySeriesActivity extends BaseActivity {

    private MagazineDetailsBean detailsBean;
    private TextView tvName;
    private TextView tvDesc;
    private ImageView imgAuthor;
    private ImageView imgBg;
    private TextView tvTitle;
    private ImageView imgReturn;

    private RefreshRecyclerView mRecyclerView;

    private MonthlyAllAdapter adapter;
    private List<MonthlySeriesBean> listAll = new ArrayList<>();
    private int pages = 1;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_monthly_series;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        imgAuthor = (ImageView) findViewById(R.id.img_author);
        imgBg = (ImageView) findViewById(R.id.img_bg);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mToolbar = (Toolbar) findViewById(toolbar);
    }

    @Override
    protected void initAction() {
        initStatusBar();
//        if (TextUtils.isEmpty(getIntent().getStringExtra("name")) && TextUtils.equals("null", getIntent().getStringExtra("name"))) {
//            tvTitle.setText("月刊系列");
//        } else {
//            tvTitle.setText(getIntent().getStringExtra("name"));
//        }
        initToolBar();
        imgReturn.setOnClickListener(this);
        toHttpMagazineDetails();

        initRecyclerView();

        toHttpGetMonthly();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
                    params.height = params.height + DisplayUtil.getStatueBarHeight(MonthlySeriesActivity.this);
                    mToolbar.setLayoutParams(params);
                }
            });
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(" ");
//        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.TOP);////设置展开后标题的位置
        mCollapsingToolbarLayout.setExpandedTitleMarginTop(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setExpandedTitleMarginStart(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER | Gravity.TOP);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
        AppBarLayout appBar = (AppBarLayout) findViewById(app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                Log.e("verticalOffset", verticalOffset + "");
//                Log.e("getTotalScrollRange", appBarLayout.getTotalScrollRange() + "");
//                Log.e("dip", Util.dip2px(TheNewTodayAudioActivity.this, 206) + "");
                if (Math.abs(verticalOffset) >= Util.dip2px(MonthlySeriesActivity.this, 206)) {
                    tvTitle.setBackgroundColor(Color.BLACK);
                    tvTitle.setText(detailsBean == null ? "" : detailsBean.getName());
                    viewStatusBar.setBackgroundColor(Color.BLACK);
                } else {
                    tvTitle.setText("");
                    tvTitle.setBackgroundColor(Color.TRANSPARENT);
                    viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
                }
                if (verticalOffset == 0) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        }
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        adapter = new MonthlyAllAdapter(this);
//        allAdapter.setHeader(headView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetMonthly();
            }
        });
    }

    private void toHttpGetMonthly() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("magazineId", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().GetMonthlyAll(map).enqueue(new RetrofitCallBack<List<MonthlySeriesBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<MonthlySeriesBean>>> call, Response<RetrofitResult<List<MonthlySeriesBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        listAll.removeAll(listAll);
                    }
                    if (response.body().getData().size() > 0) {
                        listAll.addAll(response.body().getData());
                        pages++;
                        adapter.clear();
                        adapter.addAll(listAll);
                    } else {
                        adapter.mNoMoreView.setText("没有更多了");
                        mRecyclerView.showNoMore();
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    mRecyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<MonthlySeriesBean>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                mRecyclerView.showNoMore();
            }
        });
    }

    private void toHttpMagazineDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().GetMagazineDetails(map).enqueue(new RetrofitCallBack<MagazineDetailsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<MagazineDetailsBean>> call, Response<RetrofitResult<MagazineDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    Glide.with(mContext).load(Util.getImgUrl(detailsBean.getCoverimg()))
                            .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(MonthlySeriesActivity.this, 25, 2))
                            .placeholder(R.drawable.icon_readload_failed)
                            .error(R.drawable.icon_readload_failed).into(imgBg);
                    Glide.with(mContext).load(Util.getImgUrl(detailsBean.getCoverimg())).placeholder(R.drawable.icon_readload_failed)
                            .error(R.drawable.icon_readload_failed).into(imgAuthor);
                    tvName.setText("《" + detailsBean.getName() + "》");
                    tvDesc.setText(detailsBean.getDesc());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<MagazineDetailsBean>> call, Throwable t) {

            }
        });
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
