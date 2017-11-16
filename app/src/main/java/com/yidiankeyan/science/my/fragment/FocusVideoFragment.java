package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.HotNewsAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.adapter.MyRecVideoFollowAdapter;
import com.yidiankeyan.science.my.entity.VideoArticleFollowBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

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
 * 我的关注 -视频
 */
public class FocusVideoFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvArticle;
    private int pages = 1;
    private List<VideoArticleFollowBean> mData = new ArrayList<>();
    private MyRecVideoFollowAdapter followAdapter;
    private static long hotspotTimestamp;

    public FocusVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_video, container, false);
        initView(view);
        initAction();
        return view;
    }


    private void initAction() {
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpMyFollowList();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvArticle.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpMyFollowList();
            }
        });
        followAdapter = new MyRecVideoFollowAdapter(getContext(), mData);
        lvArticle.setAdapter(followAdapter);
        lvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (mData.get(position).getType()) {
                    case 1:
                        intent = new Intent(getContext(), ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mData.get(position).getName());
                        audio.setArticleid(mData.get(position).getId());
                        audio.setLastupdatetime(mData.get(position).getCreatetime());
                        audio.setArticletype(2);
                        audio.setMediaurl(mData.get(position).getImgs());
                        audio.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(getContext(), AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", 0);
                        intent.putExtra("single", true);
                        intent.putExtra("id", mData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 3:
                        if (followAdapter.getItemViewType(position) == HotNewsAdapter.TYPE_VIDEO_B)
                            return;
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mData.get(position).getName());
                        video.setArticleid(mData.get(position).getId());
                        video.setLastupdatetime(mData.get(position).getCreatetime());
                        video.setArticletype(2);
                        video.setMediaurl(mData.get(position).getImgs());
                        video.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(getContext(), VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mData.get(0).getId());
                        getContext().startActivity(intent);
                        break;
                }

            }
        });
    }

    /**
     * 获取视频列表
     */
    private void toHttpMyFollowList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        if (pages == 1) {
            hotspotTimestamp = System.currentTimeMillis();
            map.put("periodstart", hotspotTimestamp);
        } else {
            map.put("periodstart", hotspotTimestamp);
        }
        ApiServerManager.getInstance().getApiServer().getMyRecVideoFollow(map).enqueue(new RetrofitCallBack<List<VideoArticleFollowBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<VideoArticleFollowBean>>> call, Response<RetrofitResult<List<VideoArticleFollowBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvArticle.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvArticle.setHasLoadMore(false);
                    }

                    followAdapter.notifyDataSetChanged();
                } else {
                    lvArticle.showFailUI();
                }
                ptrLayout.onRefreshComplete();

            }

            @Override
            public void onFailure(Call<RetrofitResult<List<VideoArticleFollowBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvArticle.showFailUI();
            }
        });
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
        lvArticle = (ListViewFinalLoadMore) view.findViewById(R.id.lv_article);
    }

}
