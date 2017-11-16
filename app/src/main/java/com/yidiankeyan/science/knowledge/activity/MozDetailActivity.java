package com.yidiankeyan.science.knowledge.activity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.TheNewTodayAudioActivity;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.knowledge.adapter.TagContentAdapter;
import com.yidiankeyan.science.knowledge.entity.TagBean;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;


/**
 * 墨子号详情
 */
public class MozDetailActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imgRoot;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvContentNum;
    private TextView tvFansNum;
    private TextView tvFocus;
    private TextView tvDesc;
    private Toolbar toolbar;
    private TextView tvTitle;
    private RefreshRecyclerView recyclerView;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private TagBean tagBean;
    private TagContentAdapter adapter;
    private int pages = 1;
    private List<HotNewsBean> listAll = new ArrayList<>();

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_moz_detail;
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        imgRoot = (ImageView) findViewById(R.id.img_root);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
//        tvContentNum = (TextView) findViewById(R.id.tv_content_num);
//        tvFansNum = (TextView) findViewById(R.id.tv_fans_num);
//        tvFocus = (TextView) findViewById(R.id.tv_focus);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        toHttpGetDetail();
        initRecyclerView();
        findViewById(R.id.img_return).setOnClickListener(this);
//        tvFocus.setOnClickListener(this);
    }

    private void initRecyclerView() {
        adapter = new TagContentAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
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
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        entity.put("type", getIntent().getIntExtra("type", 1));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getTagContentList(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
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
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
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
                if (Math.abs(verticalOffset) >= Util.dip2px(MozDetailActivity.this, 206)) {

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
                        tvTitle.setText(tagBean == null ? "" : tagBean.getName());
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
                    params.height = params.height + DisplayUtil.getStatueBarHeight(MozDetailActivity.this);
                    toolbar.setLayoutParams(params);
                }
            });
        }
    }

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("type", getIntent().getIntExtra("type", 1));
        ApiServerManager.getInstance().getApiServer().getTagDetail(map).enqueue(new RetrofitCallBack<TagBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<TagBean>> call, Response<RetrofitResult<TagBean>> response) {
                if (response.body().getCode() == 200) {
                    tagBean = response.body().getData();
                    toHttpGetContent();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<TagBean>> call, Throwable t) {

            }
        });
    }

    private void initData() {
        Glide.with(this).load(Util.getImgUrl(tagBean.getCoverimgurl()))
                .error(R.drawable.icon_default_avatar)
                .placeholder(R.drawable.icon_default_avatar)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgAvatar);
        Glide.with(this).load(Util.getImgUrl(tagBean.getCoverimgurl()))
                .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(this, 25, 2))
                .into(imgRoot);
        tvName.setText(tagBean.getName());
//        tvContentNum.setText("内容：" + tagBean.getContentnum());
//        tvFansNum.setText("粉丝数：" + tagBean.getFollowernum());
        tvDesc.setText(tagBean.getSynopsis());
//        if (tagBean.getIsFocus() == 1) {
//            tvFocus.setText("已关注");
//            tvFocus.setBackground(getResources().getDrawable(R.drawable.shape_download_state_white));
//            tvFocus.setTextColor(Color.parseColor("#333333"));
//            tvFocus.setTag(1);
//        } else {
//            tvFocus.setText("关注+");
//            tvFocus.setBackground(getResources().getDrawable(R.drawable.shape_download_state_black));
//            tvFocus.setTextColor(Color.WHITE);
//            tvFocus.setTag(0);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
//            case R.id.tv_focus:
//                if (v.getTag() == null)
//                    return;
//                else if (((int) v.getTag()) == 1) {
//                    //取消关注
//                    showWaringDialog("提示", "是否取消关注？", new OnDialogButtonClickListener() {
//                        @Override
//                        public void onPositiveButtonClick() {
//                            toHttpUnfocus();
//                        }
//
//                        @Override
//                        public void onNegativeButtonClick() {
//
//                        }
//                    });
//                } else {
//                    //关注
//                    toHttpFocus();
//                }
//                break;
        }
    }

    private void toHttpUnfocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("type", getIntent().getIntExtra("type", 1));
        ApiServerManager.getInstance().getApiServer().unfocusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                if (response.body().getCode() == 200) {
                    tvFocus.setText("关注+");
                    tvFocus.setBackground(getResources().getDrawable(R.drawable.shape_download_state_black));
                    tvFocus.setTextColor(Color.WHITE);
                    tvFocus.setTag(0);
                } else {
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void toHttpFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("type", getIntent().getIntExtra("type", 1));
        ApiServerManager.getInstance().getApiServer().focusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                if (response.body().getCode() == 200) {
                    tvFocus.setText("已关注");
                    tvFocus.setBackground(getResources().getDrawable(R.drawable.shape_download_state_white));
                    tvFocus.setTextColor(Color.parseColor("#333333"));
                    tvFocus.setTag(1);
                } else if (306 == response.body().getCode()) {
                    if ("order-already".equals(response.body().getMsg())) {
                        tvFocus.setText("已关注");
                        tvFocus.setBackground(getResources().getDrawable(R.drawable.shape_download_state_white));
                        tvFocus.setTextColor(Color.parseColor("#333333"));
                        tvFocus.setTag(1);
                    } else
                        Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                } else
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }
}
