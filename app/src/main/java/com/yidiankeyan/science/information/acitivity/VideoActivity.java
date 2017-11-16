package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.VideoAdapter;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.utils.SystemConstant;
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

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 视频模块
 */
public class VideoActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecommend;
    private AutoLinearLayout llVideo;
    private int pages = 1;
    private List<HotNewsBean> mData = new ArrayList<>();
    private VideoAdapter adapter;
    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        lvRecommend = (ListViewFinalLoadMore) findViewById(R.id.lv_video);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        llVideo = (AutoLinearLayout) findViewById(R.id.activity_video);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("视频");
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetNews();
            }
        });
        if (mData.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvRecommend.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new VideoAdapter(this, mData);
        lvRecommend.setAdapter(adapter);
        lvRecommend.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetNews();
            }
        });
        llReturn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private void toHttpGetNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        map.put("orientation", 3);
        ApiServerManager.getInstance().getApiServer().getHotNewsTime(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvRecommend.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvRecommend.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvRecommend.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvRecommend.showFailUI();
            }
        });
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_INFOR_VIDEO:
                adapter.notifyDataSetChanged();
                break;
        }
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
        EventBus.getDefault().unregister(this);
        JCVideoPlayer.releaseAllVideos();
    }
}
