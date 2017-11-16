package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.HotNewsAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.subscribe.activity.*;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 资讯
 */

public class InformationActivity extends BaseActivity {
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvInformation;
    private int pages = 1;
    private List<HotNewsBean> mData = new ArrayList<>();
    private HotNewsAdapter adapter;
    private static long hotspotTimestamp;
    private TextView maintitleTxt;
    private AutoLinearLayout mTitleReturn;
    private HashMap<String, Object> mMap;

    @Override
    protected int setContentView() {
        return R.layout.activity_information;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
        lvInformation = (ListViewFinalLoadMore) findViewById(R.id.lv_information);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        maintitleTxt.setText(getResources().getString(R.string.info));//title_return
        mTitleReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mTitleReturn.setOnClickListener(this);
    }

    @Override
    protected void initAction() {
        toHttpBehaviorAcquisition();
        adapter = new HotNewsAdapter(this, mData);
        lvInformation.setAdapter(adapter);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpInformation();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.autoRefresh();
        lvInformation.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpInformation();
            }
        });
        lvInformation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                JCVideoPlayer.releaseAllVideos();
//                position--;
                if (position < 0)
                    return;
                switch (mData.get(position).getType()) {
                    case 1:
                        intent = new Intent(InformationActivity.this, ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mData.get(position).getId());
                        startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mData.get(position).getName());
                        audio.setArticleid(mData.get(position).getId());
                        audio.setLastupdatetime(mData.get(position).getCreatetime());
                        audio.setArticletype(2);
                        audio.setMediaurl(mData.get(position).getMediaurl());
                        audio.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(InformationActivity.this, com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", 0);
                        intent.putExtra("single", true);
                        intent.putExtra("id", mData.get(position).getId());
                        startActivity(intent);
                        break;
                    case 3:
                        if (adapter.getItemViewType(position) == HotNewsAdapter.TYPE_VIDEO_B)
                            return;
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mData.get(position).getName());
                        video.setArticleid(mData.get(position).getId());
                        video.setLastupdatetime(mData.get(position).getCreatetime());
                        video.setArticletype(2);
                        video.setMediaurl(mData.get(position).getMediaurl());
                        video.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(InformationActivity.this, VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mData.get(0).getId());
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    /**
     * 用户行为采集接口
     */
    private void toHttpBehaviorAcquisition() {
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.USER_BEHAVIOR_ACQUISITION_DAYLY , null, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    private void toHttpInformation() {
        mMap = new HashMap<>();
        mMap.put("pages", pages);
        mMap.put("pagesize", 10);
        if (pages == 1) {
            hotspotTimestamp = System.currentTimeMillis();
            mMap.put("periodstart", hotspotTimestamp);
        } else {
            mMap.put("periodstart", hotspotTimestamp);
        }
//        mMap.put("orientation", 1);
        ApiServerManager.getInstance().getApiServer().getHotRecommend(mMap).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvInformation.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvInformation.setHasLoadMore(false);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    lvInformation.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvInformation.showFailUI();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
