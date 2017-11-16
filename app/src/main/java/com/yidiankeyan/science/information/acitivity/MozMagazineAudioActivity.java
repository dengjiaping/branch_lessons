package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.MarqueeTextView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 墨子杂志播放页
 */
public class MozMagazineAudioActivity extends BaseActivity {

    private MarqueeTextView txtTitle;
    private AutoLinearLayout returnTitle;
    private AutoLinearLayout llMozReadAudio;
    //    private MozReadAudioActivity.TestLoopAdapter mLoopAdapter;
    private ImageView imgMediaPlay;
    private SeekBar seekBar;
    private ImageView imgReadCover;

    private AutoLinearLayout llAudioMore;
    private PopupWindow mAudioPopupWindow;
    private AutoLinearLayout llShareWx;
    private AutoLinearLayout llShareFriendCircle;
    private AutoLinearLayout llShareSina;
    private AutoLinearLayout llShareQq;
    private boolean isReport;
    private ImageView imgAudioSketch;
    private TextView tvContentAuthor;
    private AutoRelativeLayout rlContentAuthor;

    /**
     * 接受定时服务发送的广播
     */
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView tvCurrTime, tvTotalTime;
    private MonthlyDetailsBean detailsBean;

    /**
     * 随着播放进度更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                try {
                    tvCurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                    seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                    seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                    tvTotalTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_moz_magazine_audio;
    }

    /**
     * 000
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //&&if (fromUser&& !TextUtils.isEmpty(getIntent().getStringExtra("mediaurl")) && AudioPlayManager.getInstance().mediaPlayer != null)
            if (fromUser) {
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
                    if (progress < AudioPlayManager.getInstance().getIntDuration())
                        AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AudioPlayManager.getInstance().pause();
            DemoApplication.isPlay = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (AudioPlayManager.getInstance().mIjkMediaPlayer != null) {
                AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(seekBar.getProgress());
                AudioPlayManager.getInstance().ijkStart();
                DemoApplication.isPlay = true;
            }
        }
    };

    @Override
    protected void initView() {
        txtTitle = (MarqueeTextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        llMozReadAudio = (AutoLinearLayout) findViewById(R.id.activity_moz_magazine_audio);
        imgMediaPlay = (ImageView) findViewById(R.id.img_media_play);
        tvCurrTime = (TextView) findViewById(R.id.tv_curr_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
        imgReadCover = (ImageView) findViewById(R.id.img_read_cover);
        rlContentAuthor = (AutoRelativeLayout) findViewById(R.id.rl_content_author);
        tvContentAuthor = (TextView) findViewById(R.id.tv_content_author);
    }

    @Override
    protected void initAction() {
        returnTitle.setOnClickListener(this);
        imgMediaPlay.setOnClickListener(this);
        seekBar.setProgress(0);
        tvCurrTime.setText("00:00");
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().GetMonthlyDetails(map).enqueue(new RetrofitCallBack<MonthlyDetailsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<MonthlyDetailsBean>> call, Response<RetrofitResult<MonthlyDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    if (detailsBean != null)
                        initData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<MonthlyDetailsBean>> call, Throwable t) {

            }
        });
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                //每隔0.1秒检查音频播放器是否处于播放中，如果是的话更新进度条和已播放时间
                if (AudioPlayManager.getInstance().isPlaying()) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 100);
    }

    private void initData() {
        txtTitle.setText(detailsBean.getMonthlyDB().getName());
        llAudioMore.setOnClickListener(this);
        Glide.with(this).load(Util.getImgUrl(detailsBean.getMonthlyDB().getCoverimg()))
                .placeholder(R.drawable.icon_banner_load)
                .error(R.drawable.icon_banner_load)
                .into(imgReadCover);
        if (TextUtils.isEmpty(detailsBean.getMonthlyDB().getInterpreter())) {
            rlContentAuthor.setVisibility(View.GONE);
            tvContentAuthor.setText("");
        } else {
            rlContentAuthor.setVisibility(View.VISIBLE);
            tvContentAuthor.setText(detailsBean.getMonthlyDB().getInterpreter());
        }

        List<MonthlyDetailsBean.MonthlyDBBean> list = new ArrayList<>();
        list.add(detailsBean.getMonthlyDB());
        AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);

        AudioPlayManager.getInstance().ijkStart();
        DemoApplication.isPlay = true;
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof MozMagazineAudioActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.AUDIO_COMPLET:
                LogUtils.e("complete");
                AudioPlayManager.getInstance().release();
                seekBar.setProgress(0);
                tvCurrTime.setText("00:00");
                imgMediaPlay.setImageResource(R.drawable.icon_today_audio_play);
                break;
            case SystemConstant.ON_PLAYING:
                //开始播放
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                tvTotalTime.setText(TimeUtils.getAnswerLength(AudioPlayManager.getInstance().getIntDuration()));
                imgMediaPlay.setImageResource(R.drawable.icon_today_audio_stop);
                break;
            case SystemConstant.ON_PAUSE:
                imgMediaPlay.setImageResource(R.drawable.icon_today_audio_play);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_audio_more:
                showMore();
                break;
            case R.id.img_media_play:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PREPARE:
                        ToastMaker.showShortToast("正在加载");
                        break;
                    case SystemConstant.ON_PAUSE:
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        break;
                    default:
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                }
                break;
        }
    }

    private void showMore() {
        if (detailsBean == null)
            return;
        if (mAudioPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
            llShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
//            llShareSina = (AutoLinearLayout) view.findViewById(R.id.ll_share_sina);
            llShareQq = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgAudioSketch = (ImageView) view.findViewById(R.id.img_audio_sketch);
            mAudioPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mAudioPopupWindow.setContentView(view);
            mAudioPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mAudioPopupWindow.setFocusable(true);
            mAudioPopupWindow.setOutsideTouchable(true);
            mAudioPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mAudioPopupWindow.showAtLocation(llMozReadAudio, Gravity.BOTTOM, 0, 0);
            mAudioPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop((Activity) mContext, mAudioPopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop((Activity) mContext, mAudioPopupWindow);
                }
            });

        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mAudioPopupWindow.showAtLocation(llMozReadAudio, Gravity.BOTTOM, 0, 0);
        }
//        llShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MozReadAudioActivity.this.shareWeb(
//                        SHARE_MEDIA.SINA,
//                        detailsBean.getName(),
//                        TextUtils.isEmpty(detailsBean.getDesc()) ? "墨子读书" : detailsBean.getDesc(),
//                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getCoverimgurl(),
//                        SystemConstant.MYURL + "book/share/" + detailsBean.getId(),
//                        null);
//            }
//        });
        llShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MozMagazineAudioActivity.this.shareWeb(
                        SHARE_MEDIA.QQ,
                        detailsBean.getMonthlyDB().getName(),
                        TextUtils.isEmpty(detailsBean.getMonthlyDB().getDesc()) ? "墨子杂志" : detailsBean.getMonthlyDB().getDesc(),
                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getCoverimg(),
                        SystemConstant.MYURL + "/share/magazine/" + detailsBean.getMonthlyDB().getId(),
                        null);
            }

        });

        llShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MozMagazineAudioActivity.this.shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        detailsBean.getMonthlyDB().getName(),
                        TextUtils.isEmpty(detailsBean.getMonthlyDB().getDesc()) ? "墨子杂志" : detailsBean.getMonthlyDB().getDesc(),
                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getCoverimg(),
                        SystemConstant.MYURL + "/share/magazine/" + detailsBean.getMonthlyDB().getId(),
                        null);
            }
        });
        llShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MozMagazineAudioActivity.this.shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        detailsBean.getMonthlyDB().getName(),
                        TextUtils.isEmpty(detailsBean.getMonthlyDB().getDesc()) ? "墨子杂志" : detailsBean.getMonthlyDB().getDesc(),
                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getCoverimg(),
                        SystemConstant.MYURL + "/share/magazine/" + detailsBean.getMonthlyDB().getId(),
                        null);
            }
        });
//        imgAudioSketch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.finishPop(MozReadAudioActivity.this, mAudioPopupWindow);
//                if (TextUtils.isEmpty(getIntent().getStringExtra("id")) && TextUtils.equals("null", getIntent().getStringExtra("id"))) {
//                    Toast.makeText(MozReadAudioActivity.this, "很抱歉，没有相应音频稿", Toast.LENGTH_SHORT).show();
//                } else
//                    mContext.startActivity(new Intent(MozReadAudioActivity.this, RecommendWebActivity.class).putExtra("url", SystemConstant.MYURL + "book/content/" + detailsBean.getId()));
//            }
//        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        } catch (Exception e) {

        }
        EventBus.getDefault().unregister(this);
        Intent intent = new Intent();
        intent.setAction("action.read.audio");
        sendBroadcast(intent);
    }
}
