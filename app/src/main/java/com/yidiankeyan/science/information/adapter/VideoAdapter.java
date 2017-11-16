package com.yidiankeyan.science.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
 * Created by nby on 2016/11/24.
 * 作用：视频adapter
 */
public class VideoAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotNewsBean> mData;
    private LayoutInflater mInflater;

    public VideoAdapter(Context context, List<HotNewsBean> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public HotNewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final VideoBViewHolder videoBViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_video_type_fragment, parent, false);
            videoBViewHolder = new VideoBViewHolder(convertView);
            convertView.setTag(videoBViewHolder);
        } else {
            videoBViewHolder = (VideoBViewHolder) convertView.getTag();
        }

        videoBViewHolder.position = position;
        AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
        if (content != null && Util.fileExisted(content.getFilePath())) {
            videoBViewHolder.videoPlayer.setUp(content.getFilePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, mData.get(position).getName());
        } else {
            videoBViewHolder.videoPlayer.setUp(
                    Util.getImgUrl(mData.get(position).getMediaurl()), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    mData.get(position).getName());
        }
        videoBViewHolder.tvAuthorName.setText(mData.get(position).getAuthor());
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getUserCoverImg()))
                .placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(videoBViewHolder.imgVideoAuthor);


        if (mData.get(position).getLength() == 0) {
            videoBViewHolder.videoPlayer.tvTime.setText("");
            videoBViewHolder.videoPlayer.tvTime.setVisibility(View.INVISIBLE);
        } else {
            videoBViewHolder.videoPlayer.tvTime.setVisibility(View.VISIBLE);
            videoBViewHolder.videoPlayer.tvTime.setText(TimeUtils.getInterviewTime(mData.get(position).getLength()));
        }


        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                .placeholder(R.drawable.icon_banner_load)
                .error(R.drawable.icon_banner_load).into(videoBViewHolder.videoPlayer.thumbImageView);

//        videoBViewHolder.imgVideoAuthor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Util.hintLogin((BaseActivity) mContext)) return;
//                Intent intent = new Intent(mContext, MyHomePageActivity.class);
//                intent.putExtra("id", mData.get(position).getUserid());
//                intent.putExtra("name", mData.get(position).getAuthor());
//                mContext.startActivity(intent);
//            }
//        });
        videoBViewHolder.videoPlayer.setMyOnClickListener(new JCVideoPlayer.MyOnClickListener() {
            @Override
            public void onClick() {
                AudioPlayManager.getInstance().release();
                AudioPlayManager.getInstance().reset();
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_HIDE_PLAY);
                EventBus.getDefault().post(msg);
            }
        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlbumContent video = new AlbumContent(null);
//                video.setArticlename(mData.get(position).getName());
//                video.setArticleid(mData.get(position).getId());
//                video.setLastupdatetime(mData.get(position).getCreatetime());
//                video.setArticletype(2);
//                video.setMediaurl(mData.get(position).getMediaurl());
//                video.setCoverimgurl(mData.get(position).getCoverimgurl());
//                AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
//                if (contentVideo != null)
//                    video.setFilePath(contentVideo.getFilePath());
//                ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
//                listItemVideo.add(video);
//                Intent intent = new Intent(mContext, VideoAlbumActivity.class);
//                intent.putParcelableArrayListExtra("list", listItemVideo);
//                intent.putExtra("position", 0);
//                intent.putExtra("id", mData.get(position).getId());
//                videoBViewHolder.videoPlayer.textureViewContainer.removeView(JCMediaManager.textureView);
//                intent.putExtra("currentState", videoBViewHolder.videoPlayer.currentState);
//                mContext.startActivity(intent);
//                videoBViewHolder.videoPlayer.resetUi();
//            }
//        });
//        videoBViewHolder.rlBestow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Util.hintLogin((BaseActivity) mContext))
//                    return;
//                Map<String, Object> map = new HashMap<>();
//                String url;
//                if (videoBViewHolder.imgCollect.getTag().equals(0)) {
//                    url = SystemConstant.URL + SystemConstant.COLLECT_ARTICLE;
//                } else {
//                    url = SystemConstant.URL + SystemConstant.UNCOLLECT_ARTICLE;
//                }
//                map.put("contentid", mData.get(position).getId());
//                HttpUtil.post(mContext, url, map, new HttpUtil.HttpCallBack() {
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        if (result.getCode() == 200) {
//                            if (videoBViewHolder.imgCollect.getTag().equals(0)) {
//                                videoBViewHolder.imgCollect.setTag(1);
//                                videoBViewHolder.imgCollect.setImageResource(R.drawable.icon_collection_pressed);
//                            } else {
//                                videoBViewHolder.imgCollect.setTag(0);
//                                videoBViewHolder.imgCollect.setImageResource(R.drawable.icon_collection_normal);
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//                    }
//                });
//            }
//        });
        videoBViewHolder.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharePop(position, videoBViewHolder);
            }
        });
        return convertView;
    }


    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private AutoLinearLayout llShareUrl;
    private AutoLinearLayout llShareCollect;
    private ImageView imgCloseShare;
    private ImageView imgCollect;

    private void showSharePop(final int position, final VideoBViewHolder videoBViewHolder) {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popup_window_imgtxt_share, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            llShareUrl = (AutoLinearLayout) view.findViewById(R.id.ll_share_url);
            llShareCollect = (AutoLinearLayout) view.findViewById(R.id.ll_share_collect);
            imgCloseShare = (ImageView) view.findViewById(R.id.img_close_share);
            imgCollect = (ImageView) view.findViewById(R.id.img_collect);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimHead);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.7f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(((Activity) mContext).findViewById(R.id.activity_video), Gravity.CENTER, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop((Activity) mContext, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.7f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(((Activity) mContext).findViewById(R.id.activity_video), Gravity.CENTER, 0, 0);
        }
        imgCollect.setTag(videoBViewHolder);
        Map<String, Object> map = new HashMap<>();
        map.put("contentid", mData.get(position).getId());
        map.put("type", 7);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ARTICLE_IS_COLLECTED, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if ((boolean) result.getData()) {
                        imgCollect.setTag(1);
                        imgCollect.setImageResource(R.drawable.icon_video_collections);
                    } else {
                        imgCollect.setTag(0);
                        imgCollect.setImageResource(R.drawable.icon_share_imgtxt_collents);
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
        llSearchWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).shareVideo(
                        SHARE_MEDIA.WEIXIN,
                        mData.get(position).getAlbumname(),
                        mData.get(position).getName(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.ACCESS_IMG_HOST + mData.get(position).getCoverimgurl()
                        , mData.get(position).getId()
                );
            }
        });
        llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).shareVideo(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        mData.get(position).getAlbumname(),
                        mData.get(position).getName(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.ACCESS_IMG_HOST + mData.get(position).getCoverimgurl()
                        , mData.get(position).getId()
                );
            }
        });
        llSearchQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).shareVideo(
                        SHARE_MEDIA.QQ,
                        mData.get(position).getAlbumname(),
                        mData.get(position).getName(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId(),
                        SystemConstant.ACCESS_IMG_HOST + mData.get(position).getCoverimgurl()
                        , mData.get(position).getId()
                );
            }
        });

        llShareUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(SystemConstant.MYURL + "cmsweb/article/share/" + mData.get(position).getId());
                ToastMaker.showShortToast("复制成功！");
            }
        });
        llShareCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.hintLogin((BaseActivity) mContext))
                    return;
                Map<String, Object> map = new HashMap<>();
                String url;
                if (imgCollect.getTag().equals(0)) {
                    url = SystemConstant.URL + SystemConstant.COLLECT_ARTICLE;
                } else {
                    url = SystemConstant.URL + SystemConstant.UNCOLLECT_ARTICLE;
                }
                map.put("contentid", mData.get(position).getId());
                map.put("type", 7);
                HttpUtil.post(mContext, url, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
                            if (imgCollect.getTag().equals(0)) {
                                imgCollect.setTag(1);
                                imgCollect.setImageResource(R.drawable.icon_video_collections);
                                mData.get(position).setIsCollected(1);
                            } else {
                                mData.get(position).setIsCollected(0);
                                imgCollect.setTag(0);
                                imgCollect.setImageResource(R.drawable.icon_share_imgtxt_collents);

                            }
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
            }
        });

        imgCloseShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.finishPop((Activity) mContext, sharePopupWindow);
            }
        });
    }


    class VideoBViewHolder {
        private TextView tvTitle;
        //        private JCVideoPlayerStandardShowTitleAfterFullscreen videoPlayer;
        private TextView tvAuthorName;
        private TextView tvTime;
        //        private ImageView imgAvatar;
        private JCVideoPlayerStandard videoPlayer;
        //        private ImageView imgCollect;
        private AutoRelativeLayout rlContainer;
        private AutoRelativeLayout rlBestow, rlShare;
        private ImageView imgVideoAuthor;
        int position;

        public VideoBViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
//            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            rlContainer = (AutoRelativeLayout) convertView.findViewById(R.id.rl_container);
            rlShare = (AutoRelativeLayout) convertView.findViewById(R.id.rl_share);
            rlBestow = (AutoRelativeLayout) convertView.findViewById(R.id.rl_bestow);
            imgVideoAuthor = (ImageView) convertView.findViewById(R.id.img_video_author);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.video_player);
//            imgCollect = (ImageView) convertView.findViewById(R.id.img_collect);
        }

    }

    private String getTime(long millisecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millisecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0) {
            return new SimpleDateFormat("MM-dd").format(new Date(millisecond));
        }
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 0)
            return minute + "分钟前";
        long second = duration / 1000;
        if (second == 0)
            return "1秒前";
        else
            return second + "秒前";
    }
}
