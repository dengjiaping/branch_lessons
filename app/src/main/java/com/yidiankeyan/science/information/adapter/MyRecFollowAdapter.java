package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.ClassClickItemActivity;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.knowledge.activity.TagDetailActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.JCVideoPlayerStandardShowTitleAfterFullscreen;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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
 * Created by zn on 2017/7/14
 * -我的关注adapter
 */
public class MyRecFollowAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotNewsBean> mData;
    private LayoutInflater mInflater;
    /**
     * 1.图文小图
     */
    public static final int TYPE_TEXT_A = 0;
    /**
     * 2.图文大图
     */
    public static final int TYPE_TEXT_B = 1;
    /**
     * 3.图文三图
     */
    public static final int TYPE_TEXT_C = 2;
    /**
     * 4.图文无图
     */
    public static final int TYPE_TEXT_D = 3;

    /**
     * 5.音频小图
     */
    public static final int TYPE_AUDIO_A = 4;
    /**
     * 7.视频小图
     */
    public static final int TYPE_VIDEO_A = 5;
    /**
     * 8.视频大图
     */
    public static final int TYPE_VIDEO_B = 6;

    public MyRecFollowAdapter(Context context, List<HotNewsBean> data) {
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
    public int getViewTypeCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == 1 && getItem(position).getCovertype() == 1)
            return TYPE_TEXT_A;
        if (getItem(position).getType() == 1 && getItem(position).getCovertype() == 2)
            return TYPE_TEXT_B;
        if (getItem(position).getType() == 1 && getItem(position).getCovertype() == 3)
            return TYPE_TEXT_C;
        if (getItem(position).getType() == 1 && getItem(position).getCovertype() == 4)
            return TYPE_TEXT_D;
        if (getItem(position).getType() == 2 && getItem(position).getCovertype() == 1)
            return TYPE_AUDIO_A;
        if (getItem(position).getType() == 3 && getItem(position).getCovertype() == 1)
            return TYPE_VIDEO_A;
        if (getItem(position).getType() == 3 && getItem(position).getCovertype() == 2)
            return TYPE_VIDEO_B;
        return super.getItemViewType(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextAViewHolder textAViewHolder = null;
        TextBViewHolder textBViewHolder = null;
        TextCViewHolder textCViewHolder = null;
        TextDViewHolder textDViewHolder = null;
        AudioAViewHolder audioAViewHolder = null;
        VideoAViewHolder videoAViewHolder = null;
        VideoBViewHolder videoBViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_TEXT_A:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_text_a, parent, false);
                    textAViewHolder = new TextAViewHolder(convertView);
                    convertView.setTag(textAViewHolder);
                    break;
                case TYPE_TEXT_B:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_text_b, parent, false);
                    textBViewHolder = new TextBViewHolder(convertView);
                    convertView.setTag(textBViewHolder);
                    break;
                case TYPE_TEXT_C:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_text_c, parent, false);
                    textCViewHolder = new TextCViewHolder(convertView);
                    convertView.setTag(textCViewHolder);
                    break;
                case TYPE_TEXT_D:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_text_d, parent, false);
                    textDViewHolder = new TextDViewHolder(convertView);
                    convertView.setTag(textDViewHolder);
                    break;
                case TYPE_AUDIO_A:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_audio_a, parent, false);
                    audioAViewHolder = new AudioAViewHolder(convertView);
                    convertView.setTag(audioAViewHolder);
                    break;
                case TYPE_VIDEO_A:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_audio_a, parent, false);
                    videoAViewHolder = new VideoAViewHolder(convertView);
                    convertView.setTag(videoAViewHolder);
                    break;
                case TYPE_VIDEO_B:
                    convertView = mInflater.inflate(R.layout.item_hot_news_type_video_b, parent, false);
                    videoBViewHolder = new VideoBViewHolder(convertView);
                    convertView.setTag(videoBViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_TEXT_A:
                    textAViewHolder = (TextAViewHolder) convertView.getTag();
                    break;
                case TYPE_TEXT_B:
                    textBViewHolder = (TextBViewHolder) convertView.getTag();
                    break;
                case TYPE_TEXT_C:
                    textCViewHolder = (TextCViewHolder) convertView.getTag();
                    break;
                case TYPE_TEXT_D:
                    textDViewHolder = (TextDViewHolder) convertView.getTag();
                    break;
                case TYPE_AUDIO_A:
                    audioAViewHolder = (AudioAViewHolder) convertView.getTag();
                    break;
                case TYPE_VIDEO_A:
                    videoAViewHolder = (VideoAViewHolder) convertView.getTag();
                    break;
                case TYPE_VIDEO_B:
                    videoBViewHolder = (VideoBViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_TEXT_A:
                textAViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                textAViewHolder.tvTitle.setText(mData.get(position).getName());
                textAViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                textAViewHolder.tvReadCount.setText(mData.get(position).getReadnum() + "看过");
                textAViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                        .placeholder(R.drawable.icon_hotload_failed)
                        .error(R.drawable.icon_hotload_failed).into(textAViewHolder.imgAvatar);
                textAViewHolder.rlAlbumLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid() + "");
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    textAViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    textAViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                    textAViewHolder.imgLabel.setVisibility(View.VISIBLE);
                } else {
                    textAViewHolder.tvAlbumName.setVisibility(View.GONE);
                    textAViewHolder.imgLabel.setVisibility(View.GONE);
                }
                textAViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_TEXT_B:
                textBViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                textBViewHolder.tvTitle.setText(mData.get(position).getName());
                textBViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                textBViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getImgs()))
                        .placeholder(R.drawable.icon_banner_load)
                        .error(R.drawable.icon_banner_load).into(textBViewHolder.imgAvatar);
                textBViewHolder.rlAlbumLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    textBViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    textBViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                    textBViewHolder.imgLabel.setVisibility(View.VISIBLE);
                } else {
                    textBViewHolder.tvAlbumName.setVisibility(View.GONE);
                    textBViewHolder.imgLabel.setVisibility(View.GONE);
                }
                textBViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_TEXT_C:
                textCViewHolder.tvTitle.setText(mData.get(position).getName());
                textCViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                textCViewHolder.llContainerA.setVisibility(View.GONE);
                textCViewHolder.llContainerB.setVisibility(View.GONE);
                textCViewHolder.llContainerC.setVisibility(View.GONE);
                textCViewHolder.rlAlbumLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                if (mData.get(position).getImgs() != null) {
                    String[] imgs = mData.get(position).getImgs().split(",");
                    if (imgs.length > 0) {
                        textCViewHolder.llContainerA.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(Util.getImgUrl(imgs[0])).placeholder(R.drawable.icon_banner_load)
                                .error(R.drawable.icon_banner_load).into(textCViewHolder.imgAvatarA);
                        if (imgs.length > 1) {
                            textCViewHolder.llContainerB.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(Util.getImgUrl(imgs[1])).placeholder(R.drawable.icon_banner_load)
                                    .error(R.drawable.icon_banner_load).into(textCViewHolder.imgAvatarB);
                            if (imgs.length > 2) {
                                textCViewHolder.llContainerC.setVisibility(View.VISIBLE);
                                Glide.with(mContext).load(Util.getImgUrl(imgs[2])).placeholder(R.drawable.icon_banner_load)
                                        .error(R.drawable.icon_banner_load).into(textCViewHolder.imgAvatarC);
                            }
                        }
                    }
                }
                textCViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                textCViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");

                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    textCViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    textCViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                    textCViewHolder.imgLabel.setVisibility(View.VISIBLE);
                } else {
                    textCViewHolder.tvAlbumName.setVisibility(View.GONE);
                    textCViewHolder.imgLabel.setVisibility(View.GONE);
                }
                textCViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_TEXT_D:
                textDViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                textDViewHolder.tvTitle.setText(mData.get(position).getName());
                textDViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                textDViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                textDViewHolder.rlAlbumLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    textDViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    textDViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                    textDViewHolder.imgLabel.setVisibility(View.VISIBLE);
                } else {
                    textDViewHolder.tvAlbumName.setVisibility(View.GONE);
                    textDViewHolder.imgLabel.setVisibility(View.GONE);
                }
                textDViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_AUDIO_A:
                audioAViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                audioAViewHolder.imgHotNewsAudio.setImageResource(R.drawable.animation_column_three);
                audioAViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                audioAViewHolder.tvTitle.setText(mData.get(position).getName());
                audioAViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                        .placeholder(R.drawable.icon_hotload_failed)
                        .error(R.drawable.icon_hotload_failed).into(audioAViewHolder.imgAvatar);
                audioAViewHolder.tvAlbumName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    audioAViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    audioAViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                } else {
                    audioAViewHolder.tvAlbumName.setVisibility(View.GONE);
                }
                audioAViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_VIDEO_A:
                videoAViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                videoAViewHolder.imgHotNewsAudio.setImageResource(R.drawable.jc_play_normal);
                videoAViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                videoAViewHolder.tvTitle.setText(mData.get(position).getName());
                videoAViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                        .placeholder(R.drawable.icon_hotload_failed)
                        .error(R.drawable.icon_hotload_failed).into(videoAViewHolder.imgAvatar);
                videoAViewHolder.tvAlbumName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    videoAViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    videoAViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                } else {
                    videoAViewHolder.tvAlbumName.setVisibility(View.GONE);
                }
                videoAViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_VIDEO_B:
                videoBViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
                videoBViewHolder.tvAuthorName.setText(mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozname() : "#" + mData.get(position).getTagModels().get(0).getName());
                videoBViewHolder.tvTitle.setText(mData.get(position).getName());
                videoBViewHolder.tvCommentCount.setText(mData.get(position).getCommentnum() + "评");
                videoBViewHolder.videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mData.get(position).getMediaurl(), JCVideoPlayer.SCREEN_LAYOUT_LIST, mData.get(position).getName());
                Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                        .placeholder(R.drawable.icon_banner_load)
                        .error(R.drawable.icon_banner_load).into(videoBViewHolder.imgAvatar);
                videoBViewHolder.tvAlbumName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                        intent.putExtra("title", mData.get(position).getSubjectname());
                        intent.putExtra("id", mData.get(position).getSubjectid());
                        mContext.startActivity(intent);
                    }
                });
                videoBViewHolder.rlContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        Intent intent = new Intent(mContext, VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mData.get(position).getId());
                        mContext.startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(mData.get(position).getSubjectname()) && !TextUtils.equals("null", mData.get(position).getSubjectname())) {
                    videoBViewHolder.tvAlbumName.setText(mData.get(position).getSubjectname());
                    videoBViewHolder.tvAlbumName.setVisibility(View.VISIBLE);
                } else {
                    videoBViewHolder.tvAlbumName.setVisibility(View.GONE);
                }
                videoBViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? mData.get(position).getMozid() : mData.get(position).getTagModels().get(0).getId();
                        int type = mData.get(position).getTagModels() == null || mData.get(position).getTagModels().size() == 0 ? 1 : 2;
                        if (type == 1) {
                            Intent intent = new Intent(mContext, MozDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }else{
                            Intent intent = new Intent(mContext, TagDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", type);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
        }
        return convertView;
    }

    class TextAViewHolder {
        private TextView tvAuthorName;
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvCommentCount;
        private TextView tvReadCount;
        private ImageView imgAvatar;
        private TextView tvAlbumName;
        private ImageView imgLabel;
        private AutoRelativeLayout rlAlbumLabel;

        public TextAViewHolder(View convertView) {
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            imgLabel = (ImageView) convertView.findViewById(R.id.img_label);
            rlAlbumLabel = (AutoRelativeLayout) convertView.findViewById(R.id.rl_album_label);
        }
    }

    class TextBViewHolder {
        private TextView tvTitle;
        private ImageView imgAvatar;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private TextView tvAlbumName;
        private ImageView imgLabel;
        private AutoRelativeLayout rlAlbumLabel;

        public TextBViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            imgLabel = (ImageView) convertView.findViewById(R.id.img_label);
            rlAlbumLabel = (AutoRelativeLayout) convertView.findViewById(R.id.rl_album_label);
        }
    }

    class TextCViewHolder {
        private TextView tvTitle;
        private ImageView imgAvatarA;
        private ImageView imgAvatarB;
        private ImageView imgAvatarC;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private View llContainerA;
        private View llContainerB;
        private View llContainerC;
        private TextView tvAlbumName;
        private ImageView imgLabel;
        private AutoRelativeLayout rlAlbumLabel;

        public TextCViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            imgAvatarA = (ImageView) convertView.findViewById(R.id.img_avatar_a);
            imgAvatarB = (ImageView) convertView.findViewById(R.id.img_avatar_b);
            imgAvatarC = (ImageView) convertView.findViewById(R.id.img_avatar_c);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            llContainerA = convertView.findViewById(R.id.ll_container_a);
            llContainerB = convertView.findViewById(R.id.ll_container_b);
            llContainerC = convertView.findViewById(R.id.ll_container_c);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            imgLabel = (ImageView) convertView.findViewById(R.id.img_label);
            rlAlbumLabel = (AutoRelativeLayout) convertView.findViewById(R.id.rl_album_label);
        }
    }

    class TextDViewHolder {
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private TextView tvAlbumName;
        private ImageView imgLabel;
        private AutoRelativeLayout rlAlbumLabel;

        public TextDViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            imgLabel = (ImageView) convertView.findViewById(R.id.img_label);
            rlAlbumLabel = (AutoRelativeLayout) convertView.findViewById(R.id.rl_album_label);
        }
    }

    class AudioAViewHolder {
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private ImageView imgAvatar;
        private ImageView imgHotNewsAudio;
        private TextView tvAlbumName;

        public AudioAViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            imgHotNewsAudio = (ImageView) convertView.findViewById(R.id.img_hot_news_audio);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
        }

    }

    class VideoAViewHolder {
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private ImageView imgAvatar;
        private ImageView imgHotNewsAudio;
        private TextView tvAlbumName;

        public VideoAViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            imgHotNewsAudio = (ImageView) convertView.findViewById(R.id.img_hot_news_audio);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
        }
    }

    class VideoBViewHolder {
        private TextView tvTitle;
        private JCVideoPlayerStandardShowTitleAfterFullscreen videoPlayer;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private TextView tvAlbumName;
        private ImageView imgAvatar;
        private AutoRelativeLayout rlContainer;

        public VideoBViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            videoPlayer = (JCVideoPlayerStandardShowTitleAfterFullscreen) convertView.findViewById(R.id.video_player);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            rlContainer = (AutoRelativeLayout) convertView.findViewById(R.id.rl_container);
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
