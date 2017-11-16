package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.adapter.AudioAlbumAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import org.json.JSONException;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;


public class AudioAlbumActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imgRoot;
    private ImageView imgAudioAvatar;
    private TextView tvAudioName;
    private TextView tvDate;
    private TextView tvCount;
    private TextView tvSubscribe;
    private TextView tvDesc;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView imgReturn;
    private RefreshRecyclerView recyclerView;
    private ImageView imgOrientation;

    private AlbumDetail albumDetail;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private AudioAlbumAdapter adapter;
    private int pages = 1;
    private List<AlbumContent> listAll = new ArrayList<>();
    private int orientation = 0;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_audio_album;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        imgRoot = (ImageView) findViewById(R.id.img_root);
        imgAudioAvatar = (ImageView) findViewById(R.id.img_audio_avatar);
        tvAudioName = (TextView) findViewById(R.id.tv_audio_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvSubscribe = (TextView) findViewById(R.id.tv_subscribe);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgOrientation = (ImageView) findViewById(R.id.img_orientation);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        toHttpGetAlbumDetail();
        initRecyclerView();


        findViewById(R.id.img_return).setOnClickListener(this);
        findViewById(R.id.ll_download).setOnClickListener(this);
        findViewById(R.id.ll_play).setOnClickListener(this);
        findViewById(R.id.ll_orientation).setOnClickListener(this);
        tvSubscribe.setOnClickListener(this);
    }

    private void initRecyclerView() {
        adapter = new AudioAlbumAdapter(this);
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
        map.put("orientation", orientation);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumid", getIntent().getStringExtra("albumId"));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getAlbumContent(map).enqueue(new RetrofitCallBack<List<AlbumContent>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AlbumContent>>> call, Response<RetrofitResult<List<AlbumContent>>> response) {
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
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        AlbumContent content = adapter.getData().get(i);
                        if (TextUtils.equals(content.getArticleid(), AudioPlayManager.getInstance().getCurrId()) && i != AudioPlayManager.getInstance().getPosition()) {
                            //同步音频播放的数据
                            AudioPlayManager.getInstance().onDataChanged(adapter.getData(), i);
                        }
                        content.setAlbumId(albumDetail.getAlbumid());
                        content.setAlbumName(albumDetail.getAlbumname());
                        content.setAlbumAvatar(albumDetail.getImgurl());
                        content.setContentNum(albumDetail.getContentnum());
                        AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(content.getArticleid());
                        if (albumContent != null) {
                            //设置音视频存储的路径
                            content.setFilePath(albumContent.getFilePath());
                        }
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AlbumContent>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }

    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        ApiServerManager.getInstance().getApiServer().getAlbummDetail(map).enqueue(new RetrofitCallBack<AlbumDetail>() {
            @Override
            public void onSuccess(Call<RetrofitResult<AlbumDetail>> call, Response<RetrofitResult<AlbumDetail>> response) {
                if (response.body().getCode() == 200) {
                    toHttpGetContent();
                    albumDetail = response.body().getData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<AlbumDetail>> call, Throwable t) {

            }
        });
    }

    private void initData() {
        Glide.with(this).load(Util.getImgUrl(albumDetail.getImgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(imgAudioAvatar);
        Glide.with(this).load(Util.getImgUrl(albumDetail.getImgurl()))
                .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(this, 25, 2))
                .into(imgRoot);
        tvAudioName.setText(albumDetail.getAlbumname());
        tvDate.setText("最近更新日期:" + new SimpleDateFormat("yyyy年MM月dd").format(new Date(albumDetail.getLastupdatetime())));
        tvCount.setText("音频数量:" + albumDetail.getContentnum());
        tvDesc.setText(albumDetail.getDescribes());
        if (albumDetail.getIsorder() == 1) {
            tvSubscribe.setTag(1);
            tvSubscribe.setText("已订阅");
            tvSubscribe.setTextColor(Color.parseColor("#ffffff"));
            tvSubscribe.setBackground(getResources().getDrawable(R.drawable.shape_audio_subscribe));
        } else {
            tvSubscribe.setTag(0);
            tvSubscribe.setText("订阅+");
            tvSubscribe.setTextColor(Color.WHITE);
            tvSubscribe.setBackground(getResources().getDrawable(R.drawable.shape_download_state_black));
        }
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
                if (Math.abs(verticalOffset) >= Util.dip2px(AudioAlbumActivity.this, 206)) {

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
                        tvTitle.setText(albumDetail == null ? "" : albumDetail.getAlbumname());
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
                    params.height = params.height + DisplayUtil.getStatueBarHeight(AudioAlbumActivity.this);
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
            case R.id.ll_orientation:
                if (orientation == 1) {
                    //降序
                    imgOrientation.setImageResource(R.drawable.icon_audio_album_reverse);
                    orientation = 0;
                    ((TextView) findViewById(R.id.tv_orientation)).setText("倒序");
                } else {
                    imgOrientation.setImageResource(R.drawable.icon_audio_album_normal_list);
                    orientation = 1;
                    ((TextView) findViewById(R.id.tv_orientation)).setText("正序");
                }
                pages = 1;
                toHttpGetContent();
                break;
            case R.id.ll_download:
                if (!Util.hintLogin(this)) {
                    return;
                }
                for (AlbumContent albumContent : adapter.getData()) {
                    AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(albumContent.getArticleid());
                    if ((content == null || !Util.fileExisted(content.getFilePath())) && !TextUtils.isEmpty(albumContent.getMediaurl()) && !"null".equals(albumContent.getMediaurl())) {
                        ToastMaker.showShortToast("下载中...");
                        String suffixes = albumContent.getMediaurl().substring(albumContent.getMediaurl().lastIndexOf("."));
                        albumContent.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getArticlename() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(albumContent);
                        try {
                            DownloadManager.getInstance().startDownload(
                                    SystemConstant.ACCESS_IMG_HOST + albumContent.getMediaurl()
                                    , albumContent.getArticlename()
                                    , Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getArticlename() + suffixes, true, false, null, getIntent().getIntExtra("contentNum", 0), albumContent.getArticleid());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.ll_play:
                if (adapter != null && adapter.getData().size() > 0) {
                    AudioPlayManager.getInstance().init(adapter.getData(), 0, AudioPlayManager.PlayModel.ORDER);
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                    Intent intent = new Intent(this, AudioAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) adapter.getData());
                    intent.putExtra("id", adapter.getData().get(0).getArticleid());
                    intent.putExtra("position", 0);
                    startActivity(intent);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastMaker.showShortToast("无播放内容");
                }
                break;
            case R.id.tv_subscribe:
                if (v.getTag() == null)
                    return;
                else if (!Util.hintLogin(this)) {
                    return;
                } else if (((int) v.getTag()) == 1) {
                    //取消订阅
                    showWaringDialog("提示", "是否取消订阅？", new OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            toHttpAbolishOrder();
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                } else {
                    //订阅
                    toHttpSubAlbum();
                }
                break;
        }
    }

    /**
     * 取消订阅
     */
    private void toHttpAbolishOrder() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        tvSubscribe.setEnabled(false);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ABOLISH_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                tvSubscribe.setEnabled(true);
                loadingDismiss();
                if (200 == result.getCode()) {
                    tvSubscribe.setTag(0);
                    tvSubscribe.setText("订阅+");
                    tvSubscribe.setTextColor(Color.WHITE);
                    tvSubscribe.setBackground(getResources().getDrawable(R.drawable.shape_download_state_black));
                } else {
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                tvSubscribe.setEnabled(true);
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void toHttpSubAlbum() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        tvSubscribe.setEnabled(false);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                tvSubscribe.setEnabled(true);
                loadingDismiss();
                if (200 == result.getCode()) {
                    tvSubscribe.setTag(1);
                    tvSubscribe.setText("已订阅");
                    tvSubscribe.setTextColor(Color.parseColor("#ffffff"));
                    tvSubscribe.setBackground(getResources().getDrawable(R.drawable.shape_audio_subscribe));
                } else if (306 == result.getCode()) {
                    if ("order-already".equals(result.getMsg())) {
                        tvSubscribe.setTag(1);
                        tvSubscribe.setText("已订阅");
                        tvSubscribe.setTextColor(Color.parseColor("#ffffff"));
                        tvSubscribe.setBackground(getResources().getDrawable(R.drawable.shape_audio_subscribe));
                    } else
                        Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                } else
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                tvSubscribe.setEnabled(true);
                loadingDismiss();
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }
}
