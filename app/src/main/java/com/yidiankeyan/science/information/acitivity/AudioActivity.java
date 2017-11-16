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


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MozAudioAdapter;
import com.yidiankeyan.science.information.entity.MozAudioBean;
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
 * 音频模块
 */
public class AudioActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView imgReturn;
    private RefreshRecyclerView recyclerView;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;

    private MozAudioAdapter adapter;
    private int pages = 1;
    private List<MozAudioBean> listAll = new ArrayList<>();

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_audio2;
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        findViewById(R.id.img_return).setOnClickListener(this);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        initRecyclerView();
        toHttpGetContent();
//        initPtrFrame();
    }

//    private void initPtrFrame() {
//        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                pages = 1;
//                toHttpGetContent();
//            }
//        });
//        ptrLayout.autoRefresh();
//        ptrLayout.disableWhenHorizontalMove(true);
//        ptrLayout.setLastUpdateTimeRelateObject(this);
//    }

    private void initRecyclerView() {
        adapter = new MozAudioAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetContent();
            }
        });
    }

    private void toHttpGetContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getMozAudio(map).enqueue(new RetrofitCallBack<List<MozAudioBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<MozAudioBean>>> call, Response<RetrofitResult<List<MozAudioBean>>> response) {
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
                        recyclerView.showNoMore();
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<MozAudioBean>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
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
                if (Math.abs(verticalOffset) >= Util.dip2px(AudioActivity.this, 206)) {

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
                        tvTitle.setText("墨子音频");
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
                    params.height = params.height + DisplayUtil.getStatueBarHeight(AudioActivity.this);
                    toolbar.setLayoutParams(params);
                }
            });
        }
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
