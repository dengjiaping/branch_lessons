package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.RecentContentAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 赛思小说
 * -最新内容
 */
public class NovelNewestFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvNewest;
    private RecentContentAdapter adapter;
    private List<RecentContentBean> mDatas = new ArrayList<>();
    private Intent intent;


    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareQq;
    private AutoLinearLayout imgShareFriendCircle;
    private ImageView imgShareSina;
    private PopupWindow mPopupWindow;
    private TextView btnCancel;
    private AutoLinearLayout llAll;

    public NovelNewestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_novel_newest, container, false);
        lvNewest = (ListView) view.findViewById(R.id.lv_novelnew);
        if (mDatas.size() == 0)
            toHttpGetRecentNovel();
        adapter = new RecentContentAdapter(getContext(), mDatas);
        lvNewest.setAdapter(adapter);
        lvNewest.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 获取小说
     */
    private void toHttpGetRecentNovel() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 2);
        entity.put("belongid", 1005);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_RECENT_CONTENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<RecentContentBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), RecentContentBean.class);
                    mDatas.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.NEW_FLASH_SHARE_CLICK:
                showSharePop((int) msg.getBody());
                break;
        }
    }

    private void showSharePop(final int position) {
        if (mPopupWindow == null) {
            llAll = (AutoLinearLayout) getActivity().findViewById(R.id.ll_nove);
            View view = View.inflate(getActivity(), R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareQq= (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(getActivity(), mPopupWindow);
                }
            });
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), mPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }

        imgShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        //图文
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(new UMImage(getActivity(), SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl()))
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 1:
                        //音频
                        UMusic music = new UMusic(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl());
                        music.setTitle(mDatas.get(position).getAlbumname());
                        music.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(music)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 2:
                        //视频
                        UMVideo video = new UMVideo("http://192.168.1.197/cmsweb/1.mp4");
                        video.setThumb(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl());
                        video.setTitle(mDatas.get(position).getAlbumname());
                        video.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(video)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                }
            }
        });
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        //图文
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(new UMImage(getActivity(), SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl()))
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 1:
                        //音频
                        UMusic music = new UMusic(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl());
                        music.setTitle(mDatas.get(position).getAlbumname());
                        music.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(music)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 2:
                        //视频
                        UMVideo video = new UMVideo("http://192.168.1.197/cmsweb/1.mp4");
                        video.setThumb(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl());
                        video.setTitle(mDatas.get(position).getAlbumname());
                        video.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(video)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                }
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        //图文
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(new UMImage(getActivity(), SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl()))
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 1:
                        //音频
                        UMusic music = new UMusic(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl());
                        music.setTitle(mDatas.get(position).getAlbumname());
                        music.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(music)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                    case 2:
                        //视频
                        UMVideo video = new UMVideo("http://192.168.1.197/cmsweb/1.mp4");
                        video.setThumb(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl());
                        video.setTitle(mDatas.get(position).getAlbumname());
                        video.setDescription(mDatas.get(position).getName());
                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                .withText(mDatas.get(position).getName())
                                .withMedia(video)
                                .withTargetUrl("http://www.bejson.com/")
                                .share();
                        break;
                }
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (adapter.getItemViewType(position)) {
//                    case 0:
//                        //图文
//                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                                .withText(mDatas.get(position).getName())
//                                .withMedia(new UMImage(getActivity(), SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl()))
//                                .withTargetUrl("http://www.bejson.com/")
//                                .share();
//                        break;
//                    case 1:
//                        //音频
//                        UMusic music = new UMusic(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl());
//                        music.setTitle(mDatas.get(position).getAlbumname());
//                        music.setDescription(mDatas.get(position).getName());
//                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                                .withText(mDatas.get(position).getName())
//                                .withMedia(music)
//                                .withTargetUrl("http://www.bejson.com/")
//                                .share();
//                        break;
//                    case 2:
//                        //视频
//                        UMVideo video = new UMVideo("http://192.168.1.197/cmsweb/1.mp4");
//                        video.setThumb(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getCoverimgurl());
//                        video.setTitle(mDatas.get(position).getAlbumname());
//                        video.setDescription(mDatas.get(position).getName());
//                        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                                .withText(mDatas.get(position).getName())
//                                .withMedia(video)
//                                .withTargetUrl("http://www.bejson.com/")
//                                .share();
//                        break;
//                }
//            }
//        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(getContext(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                intent.putExtra("id", listItem.get(0).getArticleid());
                intent.putExtra("position", position);
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
}
