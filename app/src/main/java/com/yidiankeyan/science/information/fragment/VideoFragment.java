package com.yidiankeyan.science.information.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
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
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by zn on 2016/11/24.
 * 视频
 */
public class VideoFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecommend;
    private AutoLinearLayout llVideo;
    private int pages = 1;
    private List<HotNewsBean> mData = new ArrayList<>();
    private VideoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        initView(view);
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
        adapter = new VideoAdapter(getContext(), mData);
        lvRecommend.setAdapter(adapter);
        lvRecommend.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetNews();
            }
        });
//        lvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent;
////                position--;
//                if (position < 0)
//                    return;
//                switch (mData.get(position).getType()) {
//                    case 1:
//                        intent = new Intent(getContext(), ImgTxtAlbumActivity.class);
//                        intent.putExtra("id", mData.get(position).getId());
//                        getContext().startActivity(intent);
//                        break;
//                    case 2:
//                        AlbumContent audio = new AlbumContent(null);
//                        audio.setArticlename(mData.get(position).getName());
//                        audio.setArticleid(mData.get(position).getId());
//                        audio.setLastupdatetime(mData.get(position).getCreatetime());
//                        audio.setArticletype(2);
//                        audio.setMediaurl(mData.get(position).getMediaurl());
//                        audio.setCoverimgurl(mData.get(position).getCoverimgurl());
//                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
//                        if (content != null)
//                            audio.setFilePath(content.getFilePath());
//                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
//                        listItem.add(audio);
//                        intent = new Intent(getContext(), AudioAlbumActivity.class);
//                        intent.putParcelableArrayListExtra("list", listItem);
//                        intent.putExtra("position", 0);
//                        intent.putExtra("single", true);
//                        intent.putExtra("id", mData.get(position).getId());
//                        getContext().startActivity(intent);
//                        break;
//                    case 3:
//                        if (adapter.getItemViewType(position) == HotNewsAdapter.TYPE_VIDEO_B)
//                            return;
//                        AlbumContent video = new AlbumContent(null);
//                        video.setArticlename(mData.get(position).getName());
//                        video.setArticleid(mData.get(position).getId());
//                        video.setLastupdatetime(mData.get(position).getCreatetime());
//                        video.setArticletype(2);
//                        video.setMediaurl(mData.get(position).getMediaurl());
//                        video.setCoverimgurl(mData.get(position).getCoverimgurl());
//                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
//                        if (contentVideo != null)
//                            video.setFilePath(contentVideo.getFilePath());
//                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
//                        listItemVideo.add(video);
//                        intent = new Intent(getContext(), VideoAlbumActivity.class);
//                        intent.putParcelableArrayListExtra("list", listItemVideo);
//                        intent.putExtra("position", 0);
//                        intent.putExtra("id", mData.get(0).getId());
//                        getContext().startActivity(intent);
//                        break;
//                }
//            }
//        });
        return view;
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

    private void initView(View view) {
        lvRecommend = (ListViewFinalLoadMore) view.findViewById(R.id.lv_video);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        llVideo = (AutoLinearLayout) view.findViewById(R.id.ll_video);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        JCVideoPlayer.releaseAllVideos();
    }

}
