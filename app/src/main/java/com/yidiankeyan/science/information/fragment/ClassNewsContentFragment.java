package com.yidiankeyan.science.information.fragment;


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
import com.yidiankeyan.science.information.adapter.RecentContentAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

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

/**
 * 分类
 * -按专题分类
 * -最新内容
 */
public class ClassNewsContentFragment extends Fragment {

    private ListViewFinalLoadMore lvNews;
    private RecentContentAdapter adapter;
    private PtrClassicFrameLayout ptrLayout;
    private int pages = 1;
    private List<RecentContentBean> mDatas = new ArrayList<>();
    private int id;

    public ClassNewsContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_class_news_content, container, false);
        lvNews = (ListViewFinalLoadMore) view.findViewById(R.id.lv_cls_content);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetData();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvNews.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetData();
            }
        });
        adapter = new RecentContentAdapter(getContext(), mDatas);
        adapter.setListView(lvNews);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDatas == null || mDatas.size() == 0 || position  >= mDatas.size())
                    return;
                Intent intent;
                switch (mDatas.get(position).getType()) {
                    case 1:
                        intent = new Intent(getActivity(), ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mDatas.get(position).getId());
                        startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mDatas.get(position).getName());
                        audio.setArticleid(mDatas.get(position).getId());
                        audio.setLastupdatetime(mDatas.get(position).getCreatetime());
                        audio.setArticletype(2);
                        audio.setMediaurl(mDatas.get(position).getMediaurl());
                        audio.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(getActivity(), AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", position);
                        intent.putExtra("id", listItem.get(0).getArticleid());
                        intent.putExtra("single", true);
                        startActivity(intent);
                        break;
                    case 3:
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mDatas.get(position).getName());
                        video.setArticleid(mDatas.get(position).getId());
                        video.setLastupdatetime(mDatas.get(position).getCreatetime());
                        video.setArticletype(2);
                        video.setMediaurl(mDatas.get(position).getMediaurl());
                        video.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(getActivity(), VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("id", listItemVideo.get(position).getArticleid());
                        intent.putExtra("position", position);
                        startActivity(intent);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 获取专辑最新内容
     */
    private void toHttpGetData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 1);
        entity.put("belongid", id);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_RECENT_CONTENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<RecentContentBean> data = GsonUtils.json2List(jsonData, RecentContentBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (data.size() > 0) {
                        lvNews.setHasLoadMore(true);
                        mDatas.addAll(data);
                        pages++;
                    } else {
                        lvNews.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvNews.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvNews.showFailUI();
            }
        });
    }

    public void destroyMediaPlayer() {
        if (adapter != null && adapter.getMediaPlayer() != null) {
            adapter.getMediaPlayer().stop();
            adapter.getMediaPlayer().reset();
            adapter.getMediaPlayer().release();
        }
    }
}
