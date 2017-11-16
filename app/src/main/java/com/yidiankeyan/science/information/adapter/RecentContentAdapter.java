package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.JCVideoPlayerStandardShowTitleAfterFullscreen;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 科研号
 * -最新内容
 */
public class RecentContentAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RecentContentBean> mDatas;
    private static final int TYPE_IMG = 0;
    private static final int TYPE_VOICE = 1;
    private static final int TYPE_VIDEO = 2;
    private int width;
    private MediaPlayer mediaPlayer;
    private ImageButton lastImageButton;
    private SeekBar lastSeek;
    private TextView lastSurplusTextView;
    private int selectPosition = -1;
    private ListView listView;
    private boolean audioPlaying;
    private boolean videoPlaying;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mDatas.get(selectPosition).setDuration(TimeUtils.getTimeFromInt(mp.getDuration()));
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            try {
                if (mediaPlayer != null && mediaPlayer.isPlaying() && listView != null && !videoPlaying) {
                    if (selectPosition >= listView.getFirstVisiblePosition() && selectPosition <= listView.getLastVisiblePosition()) {
                        View view = listView.getChildAt(selectPosition - listView.getFirstVisiblePosition());
                        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
                        final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
                        if (percent == 100) {
                            mDatas.get(selectPosition).setBuffered(true);
                        }
                        seekBar.setMax(mediaPlayer.getDuration());
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        seekBar.setSecondaryProgress((int) (mediaPlayer.getDuration() * percent * 0.01));
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer != null)
                                    textView.setText(TimeUtils.getTimeFromInt(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
                            }
                        });
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int position = (int) seekBar.getTag();
            if (fromUser) {
                if (selectPosition == position) {
                    mediaPlayer.seekTo(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (mediaPlayer != null)
                mediaPlayer.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (((int) seekBar.getTag()) == selectPosition) {
                JCVideoPlayer.releaseAllVideos();
                mediaPlayer.start();
                audioPlaying = true;
                videoPlaying = false;
            } else {
                View view = listView.getChildAt(((int) seekBar.getTag()) - listView.getFirstVisiblePosition());
                final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
                if (selectPosition >= 0) {
                    mDatas.get(selectPosition).setPlayState(0);
                    mDatas.get(selectPosition).setBuffered(false);
                }
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }
                }
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
                mediaPlayer.setOnPreparedListener(onPreparedListener);
                try {
                    mediaPlayer.setDataSource(SystemConstant.ACCESS_IMG_HOST + mDatas.get(((int) seekBar.getTag())).getMediaurl());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                seekBar.setMax(mediaPlayer.getDuration());
                lastSeek = seekBar;
                lastSurplusTextView = textView;
//                mediaPlayer.start();
                audioPlaying = true;
                videoPlaying = false;
                mDatas.get((int) seekBar.getTag()).setPlayState(1);
                selectPosition = (int) seekBar.getTag();
                notifyDataSetChanged();
            }
        }
    };

    /**
     * 视频开始播放,如果当前有音频正在播放则停止
     */
    public void onVideoPlaying() {
        videoPlaying = true;
        if (audioPlaying) {
            audioPlaying = false;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (selectPosition >= 0) {
                mDatas.get(selectPosition).setPlayState(0);
                mDatas.get(selectPosition).setBuffered(false);
                if (selectPosition >= listView.getFirstVisiblePosition() && selectPosition <= listView.getLastVisiblePosition()) {
                    View view = listView.getChildAt(selectPosition - listView.getFirstVisiblePosition());
                    SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
                    final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
                    seekBar.setProgress(0);
                    seekBar.setSecondaryProgress(0);
                    textView.setText(mDatas.get(selectPosition).getDuration());
                    ((ImageButton) view.findViewById(R.id.media_play)).setImageResource(R.drawable.icon_play_today_white);
                }
            }
            selectPosition = -1;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public RecentContentAdapter(Context mContext, List<RecentContentBean> mDatas) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        width = Util.getScreenWidth(mContext);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (1 == mDatas.get(position).getType()) {
            return TYPE_IMG;
        } else if (2 == mDatas.get(position).getType()) {
            return TYPE_VOICE;
        } else {
            return TYPE_VIDEO;
        }
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void updataProgress() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying() && listView != null && !videoPlaying && lastSeek != null && lastSurplusTextView != null) {
                if (selectPosition >= listView.getFirstVisiblePosition() && selectPosition <= listView.getLastVisiblePosition()) {
                    View view = listView.getChildAt(selectPosition - listView.getFirstVisiblePosition());
                    SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
                    final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null)
                                textView.setText(TimeUtils.getTimeFromInt(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
                        }
                    });
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public RecentContentBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderImg viewHolderImg = null;
        ViewHolderVoice viewHolderVoice = null;
        ViewHolderVideo viewHolderVideo = null;
        int type = getItemViewType(position);
//        if (convertView != null && convertView.getTag() != null) {
//            boolean b = convertView.getTag() instanceof ViewHolderVideo;
//            if (b)
//                JCVideoPlayer.releaseAllVideos();
//        }
        if (convertView == null) {
            switch (type) {
                case TYPE_IMG:
                    convertView = mInflater.inflate(R.layout.item_layout_for_img, parent, false);
                    viewHolderImg = new ViewHolderImg();
                    viewHolderImg.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderImg.tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
                    viewHolderImg.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
                    viewHolderImg.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
                    viewHolderImg.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    viewHolderImg.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                    viewHolderImg.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    viewHolderImg.imgPopwin = (ImageView) convertView.findViewById(R.id.img_popwin);
                    convertView.setTag(viewHolderImg);
                    break;
                case TYPE_VOICE:
                    convertView = mInflater.inflate(R.layout.item_layout_for_voice, parent, false);
                    viewHolderVoice = new ViewHolderVoice();
                    viewHolderVoice.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderVoice.tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
                    viewHolderVoice.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
                    viewHolderVoice.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
                    viewHolderVoice.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    viewHolderVoice.llMediaContainer2 = (AutoLinearLayout) convertView.findViewById(R.id.ll_media_container2);
                    viewHolderVoice.mediaPlay = (ImageButton) convertView.findViewById(R.id.media_play);
                    viewHolderVoice.seekBar1 = (SeekBar) convertView.findViewById(R.id.seekBar1);
                    viewHolderVoice.textView1TotalTime = (TextView) convertView.findViewById(R.id.textView1_total_time);
                    viewHolderVoice.imgPopwin = (ImageView) convertView.findViewById(R.id.img_popwin);
                    convertView.setTag(viewHolderVoice);
                    break;
                case TYPE_VIDEO:
                    convertView = mInflater.inflate(R.layout.item_layout_for_video, parent, false);
                    viewHolderVideo = new ViewHolderVideo();
                    viewHolderVideo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderVideo.tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
                    viewHolderVideo.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
                    viewHolderVideo.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
                    viewHolderVideo.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    viewHolderVideo.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    viewHolderVideo.videoPlayer = (JCVideoPlayerStandardShowTitleAfterFullscreen) convertView.findViewById(R.id.video_player);
                    viewHolderVideo.imgPopwin = (ImageView) convertView.findViewById(R.id.img_popwin);
                    convertView.setTag(viewHolderVideo);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_IMG:
                    viewHolderImg = (ViewHolderImg) convertView.getTag();
                    break;
                case TYPE_VOICE:
                    viewHolderVoice = (ViewHolderVoice) convertView.getTag();
                    break;
                case TYPE_VIDEO:
                    viewHolderVideo = (ViewHolderVideo) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_IMG:
                ViewGroup.LayoutParams params = viewHolderImg.imgAvatar.getLayoutParams();
                int height = (int) (width * 0.89 * 0.5625);
                params.height = height;
                viewHolderImg.imgAvatar.setLayoutParams(params);
                if (TextUtils.isEmpty(mDatas.get(position).getId())) {
                    Glide.with(mContext).load(mDatas.get(position).getImgUrl()).into(viewHolderImg.imgAvatar);
                    viewHolderImg.tvTitle.setText(mDatas.get(position).getTitle());
                    viewHolderImg.tvAlbumName.setText(mDatas.get(position).getAlbumName());
                    viewHolderImg.tvReadCount.setText(mDatas.get(position).getReadCount());
                    viewHolderImg.tvClickCount.setText(mDatas.get(position).getClickCount());
                    viewHolderImg.tvTime.setText(mDatas.get(position).getTime());
                    if (!TextUtils.isEmpty(mDatas.get(position).getContent()))
                        viewHolderImg.tvContent.setText(mDatas.get(position).getContent());
                    else
                        viewHolderImg.tvContent.setVisibility(View.GONE);
                } else {
                    Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).crossFade().into(viewHolderImg.imgAvatar);
                    viewHolderImg.tvTitle.setText(mDatas.get(position).getName());
                    viewHolderImg.tvAlbumName.setText(mDatas.get(position).getAlbumname());
                    viewHolderImg.tvReadCount.setText(mDatas.get(position).getReadnum() + "");
                    viewHolderImg.tvClickCount.setText(mDatas.get(position).getPraisenum() + "");
                    viewHolderImg.tvTime.setText(TimeUtils.questionCreateDuration(mDatas.get(position).getCreatetime()));
                    if (!TextUtils.isEmpty(mDatas.get(position).getAbstracts()))
                        viewHolderImg.tvContent.setText(mDatas.get(position).getAbstracts());
                    else
                        viewHolderImg.tvContent.setVisibility(View.GONE);
                }
                viewHolderImg.imgPopwin.setTag(position);
                viewHolderImg.imgPopwin.setOnClickListener(this);
                break;
            case TYPE_VOICE:
                if (TextUtils.isEmpty(mDatas.get(position).getId())) {
                    viewHolderVoice.tvTitle.setText(mDatas.get(position).getTitle());
                    viewHolderVoice.tvAlbumName.setText(mDatas.get(position).getAlbumName());
                    viewHolderVoice.tvReadCount.setText(mDatas.get(position).getReadCount());
                    viewHolderVoice.tvClickCount.setText(mDatas.get(position).getClickCount());
                    viewHolderVoice.tvTime.setText(mDatas.get(position).getTime());
                } else {
                    viewHolderVoice.tvTitle.setText(mDatas.get(position).getName());
                    viewHolderVoice.tvAlbumName.setText(mDatas.get(position).getAlbumname());
                    viewHolderVoice.tvReadCount.setText(mDatas.get(position).getReadnum() + "");
                    viewHolderVoice.tvClickCount.setText(mDatas.get(position).getPraisenum() + "");
                    viewHolderVoice.tvTime.setText(TimeUtils.questionCreateDuration(mDatas.get(position).getCreatetime()));
                }
                viewHolderVoice.seekBar1.setTag(position);
                viewHolderVoice.textView1TotalTime.setTag(position);
                switch (mDatas.get(position).getPlayState()) {
                    case 0:
                        viewHolderVoice.mediaPlay.setImageResource(R.drawable.icon_play_today_white);
                        break;
                    case 1:
                        viewHolderVoice.mediaPlay.setImageResource(R.drawable.icon_pause_today);
                        break;
                    case 2:
                        viewHolderVoice.mediaPlay.setImageResource(R.drawable.bofangs);
                        break;
                }
                viewHolderVoice.seekBar1.setOnSeekBarChangeListener(onSeekBarChangeListener);
                if (selectPosition == position) {
                    viewHolderVoice.seekBar1.setMax(mediaPlayer.getDuration());
                    viewHolderVoice.seekBar1.setProgress(mediaPlayer.getCurrentPosition());
                    viewHolderVoice.textView1TotalTime.setText(TimeUtils.getTimeFromInt(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
                    if (mDatas.get(position).isBuffered()) {
                        viewHolderVoice.seekBar1.setSecondaryProgress(viewHolderVoice.seekBar1.getMax());
                    }
                } else {
                    viewHolderVoice.textView1TotalTime.setText(mDatas.get(position).getDuration());
                    viewHolderVoice.seekBar1.setProgress(0);
                    viewHolderVoice.seekBar1.setSecondaryProgress(0);
                    mDatas.get(position).setBuffered(false);
                }
                final ViewHolderVoice finalViewHolderVoice = viewHolderVoice;
                viewHolderVoice.mediaPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (mDatas.get(position).getPlayState()) {
                            case 0:
                                JCVideoPlayer.releaseAllVideos();
                                if (selectPosition >= 0) {
                                    mDatas.get(selectPosition).setPlayState(0);
                                    mDatas.get(selectPosition).setBuffered(false);
                                }
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                        mediaPlayer.release();
                                    }
                                }
                                mediaPlayer = null;
//                                mediaPlayer = MediaPlayer.create(mContext, mDatas.get(position).getVoiceRes());
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
                                mediaPlayer.setOnPreparedListener(onPreparedListener);
                                try {
                                    mediaPlayer.setDataSource(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl());
                                    mediaPlayer.prepareAsync();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

//                                finalViewHolderVoice.seekBar1.setMax(mediaPlayer.getDuration());
                                lastSeek = finalViewHolderVoice.seekBar1;
                                lastSurplusTextView = finalViewHolderVoice.textView1TotalTime;
//                                mediaPlayer.start();
                                audioPlaying = true;
                                videoPlaying = false;
                                mDatas.get(position).setPlayState(1);
                                break;
                            case 1:
                                mediaPlayer.pause();
                                mDatas.get(position).setPlayState(2);
                                break;
                            case 2:
                                JCVideoPlayer.releaseAllVideos();
                                mediaPlayer.start();
                                audioPlaying = true;
                                videoPlaying = false;
                                mDatas.get(position).setPlayState(1);
                                break;
                        }
                        selectPosition = position;
                        notifyDataSetChanged();
                    }
                });
                viewHolderVoice.imgPopwin.setTag(position);
                viewHolderVoice.imgPopwin.setOnClickListener(this);
                break;
            case TYPE_VIDEO:
                ViewGroup.LayoutParams paramsVideo = viewHolderVideo.videoPlayer.getLayoutParams();
                int videoHeight = (int) (width * 0.89 * 0.5625);
                paramsVideo.height = videoHeight;
                viewHolderVideo.videoPlayer.setLayoutParams(paramsVideo);
                if (TextUtils.isEmpty(mDatas.get(position).getId())) {
                    viewHolderVideo.videoPlayer.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "测试");
//                    viewHolderVideo.videoPlayer.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", "测试");
                    Glide.with(mContext).load("http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg").into(viewHolderVideo.videoPlayer.thumbImageView);
                    viewHolderVideo.tvTitle.setText(mDatas.get(position).getTitle());
                    viewHolderVideo.tvAlbumName.setText(mDatas.get(position).getAlbumName());
                    viewHolderVideo.tvReadCount.setText(mDatas.get(position).getReadCount());
                    viewHolderVideo.tvClickCount.setText(mDatas.get(position).getClickCount());
                    viewHolderVideo.tvTime.setText(mDatas.get(position).getTime());
                    viewHolderVideo.tvContent.setText(mDatas.get(position).getContent());
                } else {
                    viewHolderVideo.videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl(), JCVideoPlayer.SCREEN_LAYOUT_LIST, mDatas.get(position).getName());
//                    viewHolderVideo.videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getMediaurl(), mDatas.get(position).getName());
                    Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).into(viewHolderVideo.videoPlayer.thumbImageView);
                    viewHolderVideo.tvTitle.setText(mDatas.get(position).getName());
                    viewHolderVideo.tvAlbumName.setText(mDatas.get(position).getAlbumname());
                    viewHolderVideo.tvReadCount.setText(mDatas.get(position).getReadnum() + "");
                    viewHolderVideo.tvClickCount.setText(mDatas.get(position).getPraisenum() + "");
                    viewHolderVideo.tvTime.setText(TimeUtils.questionCreateDuration(mDatas.get(position).getCreatetime()));
                    if (!TextUtils.isEmpty(mDatas.get(position).getAbstracts()))
                        viewHolderVideo.tvContent.setText(mDatas.get(position).getAbstracts());
                    else
                        viewHolderVideo.tvContent.setVisibility(View.GONE);
                }
                viewHolderVideo.imgPopwin.setTag(position);
                viewHolderVideo.imgPopwin.setOnClickListener(this);
                break;
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_popwin:
                EventMsg msg = EventMsg.obtain(SystemConstant.NEW_FLASH_SHARE_CLICK);
                msg.setBody(v.getTag());
                EventBus.getDefault().post(msg);
                break;
        }
    }

    class ViewHolderImg {
        private TextView tvTitle;
        private TextView tvAlbumName;
        private TextView tvReadCount;
        private TextView tvClickCount;
        private TextView tvTime;
        private ImageView imgAvatar;
        private TextView tvContent;
        private ImageView imgPopwin;
    }

    class ViewHolderVoice {
        private TextView tvTitle;
        private TextView tvAlbumName;
        private TextView tvReadCount;
        private TextView tvClickCount;
        private TextView tvTime;
        private AutoLinearLayout llMediaContainer2;
        private ImageButton mediaPlay;
        private SeekBar seekBar1;
        private TextView textView1TotalTime;
        private ImageView imgPopwin;
    }

    class ViewHolderVideo {
        private TextView tvTitle;
        private TextView tvAlbumName;
        private TextView tvReadCount;
        private TextView tvClickCount;
        private TextView tvTime;
        private TextView tvContent;
        private JCVideoPlayerStandardShowTitleAfterFullscreen videoPlayer;
        private ImageView imgPopwin;
    }
}
