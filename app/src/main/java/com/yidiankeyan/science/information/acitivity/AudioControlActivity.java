package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.subscribe.service.TimerService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.MarqueeTextView;
import com.yidiankeyan.science.view.TakePhotoPopWin;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by nby on 2016/8/12.
 * 日课音频播放器
 */
public class AudioControlActivity extends BaseActivity {

    private AutoLinearLayout llReturn;

    private MarqueeTextView maintitleTxt;
    private AutoLinearLayout llAudioMore;
    //    private TextView tvAudioTitle;

    //控制viewpager显示当前的页码
    private int pageNum = 0;
    private RollPagerView mRollViewPager;
    private TestLoopAdapter mLoopAdapter;


    private ImageView imgTodayCor;
    private SeekBar seekBar;
    private TextView tvCurrTime;
    private TextView tvTotalTime;
    private ImageView imgLast;
    private ImageView imgPlay;
    private ImageView imgNext;
    private TextView tvContentAuthor;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private PopupWindow mAudioPopupWindow;
    private AutoLinearLayout llShareWx;
    private AutoLinearLayout llShareFriendCircle;
    private ImageView llShareSina;
    private AutoLinearLayout llShareQq;
    private AutoLinearLayout llAudioControl;
    private CheckBox imgControlReport;
    private CheckBox imgAudioSketch;
    private CheckBox imgTimeClick;
    private TakePhotoPopWin takePhotoPopWin;

    private boolean isReport;
    private PopupWindow reportPopupwindow;
    private EditText etTitle;
    private EditText etContent;

    private RelativeLayout rlNo;
    private RelativeLayout rlFinish;
    private RelativeLayout rlTen;
    private RelativeLayout rlTwenty;
    private RelativeLayout rlThirty;
    private RelativeLayout rlFortyFive;
    private RelativeLayout rlSixty;
    private RelativeLayout rlNinty;
    private CheckBox cbNo;
    private CheckBox cbFinish;
    private CheckBox cbTen;
    private CheckBox cbTwenty;
    private CheckBox cbThirty;
    private CheckBox cbFortyFive;
    private CheckBox cbSixty;
    private CheckBox cbNinty;
    private TextView tvFinish;
    private ListView lvAudioList;
    private TextView tvTen;
    private TextView tvTwenty;
    private TextView tvThirty;
    private TextView tvFortyFive;
    private TextView tvSixty;
    private TextView tvNinty;
    private List<CheckBox> cbList = new ArrayList<>();
    private List<TextView> tvList = new ArrayList<>();
    private ImageView imgCollect;


    /**
     * 播放完成
     */
    public static final int AUDIO_COMPLET = 12;
    /**
     * 定时PopupWindow Layout
     */
    private View popupWindowView;
    /**
     * 接受定时服务发送的广播
     */
    private BroadcastReceiver broadcastReceiver;
    public static String ACTION_TIME_CHANGED = "action_time_changed";
    /**
     * 为了区分是用户点击checkbox和定时服务发送广播从而触发的checkbox改变事件
     * 如果为true的话则是用户点击的，此时需要向定时服务发送消息，改变定时策略
     * 如果为false的话则是服务发送过来的广播导致触发了改变事件，此时不需要想定时服务发送消息
     */
    private boolean flag = true;

    private ImageView imgAudioList;
    private ImageView imgPlayModel;
    private PlayListAdapter playAdapter;
    private AutoLinearLayout llDownload;
    private AutoLinearLayout llClickContainer;
    private AutoRelativeLayout rlContentAuthor;
    private String ShareId;

    private ImageView imgTodayTiming, imgTodayAudioDraft;

    /**
     * 播放列表的PopupWindow
     */
    private PopupWindow playListPopupWindow;
    /**
     * 播放模式。0:顺序;1:循环;2:随机
     */
//    private int playModel = 0;

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
                    tvTotalTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                    if (cbFinish != null && cbFinish.isChecked()) {
                        tvFinish.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration() - AudioPlayManager.getInstance().getCurrentPosition()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_audio_control;
    }

    /**
     * 000
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (fromUser && playList != null && playList.size() > 0 && AudioPlayManager.getInstance().mediaPlayer != null) {
//                AudioPlayManager.getInstance().mediaPlayer.seekTo(progress);
//            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//            AudioPlayManager.getInstance().pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            AudioPlayManager.getInstance().playUrl();
//            imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
            if (AudioPlayManager.getInstance().mIjkMediaPlayer != null) {
                AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(seekBar.getProgress());
            }
        }
    };


    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (MarqueeTextView) findViewById(R.id.maintitle_txt);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
//        tvAudioTitle = (TextView) findViewById(R.id.tv_audio_title);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvCurrTime = (TextView) findViewById(R.id.tv_curr_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        imgAudioList = (ImageView) findViewById(R.id.img_audio_list);
        imgPlayModel = (ImageView) findViewById(R.id.img_play_model);
        tvContentAuthor = (TextView) findViewById(R.id.tv_content_author);
        imgLast = (ImageView) findViewById(R.id.img_last);
        imgPlay = (ImageView) findViewById(R.id.img_media_play);
        imgNext = (ImageView) findViewById(R.id.img_next);
        llAudioControl = (AutoLinearLayout) findViewById(R.id.ll_audio_control);
        imgTodayCor = (ImageView) findViewById(R.id.img_today_cor);
        imgCollect = (ImageView) findViewById(R.id.img_collect);
//        mRollViewPager = (RollPagerView) findViewById(R.id.vp_control);
        rlContentAuthor = (AutoRelativeLayout) findViewById(R.id.rl_content_author);
        imgTodayTiming = (ImageView) findViewById(R.id.img_today_timing);
        imgTodayAudioDraft = (ImageView) findViewById(R.id.img_today_audio_draft);
    }

    private void isShare() {

        if(DemoApplication.isBuy){  //能够进行分享  可点击
            llAudioMore.setClickable(true);
        }else llAudioMore.setClickable(false);  //不能分享 不可点击
    }

    @Override
    protected void initAction() {
//        llAudioMore.setVisibility(View.VISIBLE);
        if (AudioPlayManager.getInstance().getCurrAudio() == null || TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrAudio().getName())) {
            maintitleTxt.setText("暂无内容");
        } else {
            maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
        }
        if (AudioPlayManager.getInstance().getCurrAudio() != null)
            Glide.with(this).load(Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getCoverImg()))
                    .placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed)
                    .into(imgTodayCor);
        if (AudioPlayManager.getInstance().getCurrAudio() == null || TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrAudio().getAuthorName())) {
            rlContentAuthor.setVisibility(View.GONE);
            tvContentAuthor.setText("");
        } else {
            rlContentAuthor.setVisibility(View.VISIBLE);
            tvContentAuthor.setText(AudioPlayManager.getInstance().getCurrAudio().getAuthorName());
        }
//        mRollViewPager.setNoScroll();
//        mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
////        mRollViewPager.setHintAlpha(0);
//        mRollViewPager.setHintView(null);
//        //禁止手势滑动
//        mRollViewPager.getViewPager().setPageTransformer(true, new SimplePageTransform());
//        try {
//            Field field = ViewPager.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(mRollViewPager.getViewPager().getContext(),
//                    new AccelerateInterpolator());
//            field.set(mRollViewPager.getViewPager(), scroller);
//            scroller.setmDuration(3000);
//        } catch (Exception e) {
//        }
        llReturn.setOnClickListener(this);
        llAudioMore.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgLast.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgPlayModel.setOnClickListener(this);
        imgAudioList.setOnClickListener(this);
        imgTodayTiming.setOnClickListener(this);
        imgTodayAudioDraft.setOnClickListener(this);
        if (AudioPlayManager.getInstance().isInited()) {
            if (AudioPlayManager.getInstance().mIjkMediaPlayer != null) {
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING)
                    imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
                tvCurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                tvTotalTime.setText(AudioPlayManager.getInstance().getDuration());
            }
            seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        } else {
            maintitleTxt.setText("");
            imgLast.setEnabled(false);
            imgPlay.setEnabled(false);
            imgNext.setEnabled(false);
        }
        registerBroadcastReceiver();
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

        imgCollect.setTag(0);
        imgCollect.setOnClickListener(this);
        toHttpGetCollectState();
        isShare();
    }

    /**
     * 判断是否收藏
     */
    private void toHttpGetCollectState() {
        Map<String, Object> map = new HashMap<>();
        map.put("contentid", AudioPlayManager.getInstance().getCurrId());
        if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.OneDayArticles)
            map.put("type", 8);
        else if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.AlbumContent)
            map.put("type", 6);
        else {
            imgCollect.setImageResource(R.drawable.icon_linear_collection);
            return;
        }
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ARTICLE_IS_COLLECTED, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if ((boolean) result.getData()) {
                        imgCollect.setImageResource(R.drawable.icon_silhouete_collection);
                        imgCollect.setTag(1);
                    } else {
                        imgCollect.setImageResource(R.drawable.icon_linear_collection);
                        imgCollect.setTag(0);
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpCollect() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        String url;
        if (imgCollect.getTag().equals(0)) {
            url = SystemConstant.URL + SystemConstant.COLLECT_ARTICLE;
        } else {
            url = SystemConstant.URL + SystemConstant.UNCOLLECT_ARTICLE;
        }
        map.put("contentid", AudioPlayManager.getInstance().getCurrId());
        if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.OneDayArticles)
            map.put("type", 8);
        else if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.AlbumContent)
            map.put("type", 6);
        else {
            imgCollect.setImageResource(R.drawable.icon_linear_collection);
            return;
        }
        HttpUtil.post(this, url, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if (imgCollect.getTag().equals(0)) {
                        imgCollect.setTag(1);
                        imgCollect.setImageResource(R.drawable.icon_silhouete_collection);
                    } else {
                        imgCollect.setTag(0);
                        imgCollect.setImageResource(R.drawable.icon_linear_collection);
                    }
                }
                loadingDismiss();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }


    /**
     * 注册接收定时广播
     */
    private void registerBroadcastReceiver() {
//        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TIME_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //当前是哪种定时策略
                String type = intent.getStringExtra("type");
                //定时剩余时间
                int surplusTime = intent.getIntExtra("surplusTime", 0);
                Log.e("type", "=======" + type);
                Log.e("surplusTime", "=======" + surplusTime);
                //这种情况是用户采取了定时，然后finish activity之后再次进入，初始化定时窗口
                if (takePhotoPopWin == null) {
                    initTimerPopupWindow();
                }
                //是定时服务发送的广播，不需要触发checkbox改变事件
                flag = false;
                setSurplusTimeForTextView(type, surplusTime);
                flag = true;
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 设置剩余时间
     *
     * @param type        定时的类型
     * @param surplusTime 剩余时间
     */
    private void setSurplusTimeForTextView(String type, int surplusTime) {
        if (surplusTime == 0) {
            AudioPlayManager.getInstance().release();
            seekBar.setProgress(0);
            imgPlay.setImageResource(R.drawable.icon_today_audio_play);
        }
        switch (type) {
            case "ten":
                tvTen.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbTen.setChecked(true);
                tvTen.setVisibility(View.VISIBLE);
                break;
            case "twenty":
                tvTwenty.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbTwenty.setChecked(true);
                tvTwenty.setVisibility(View.VISIBLE);
                break;
            case "thirty":
                tvThirty.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbThirty.setChecked(true);
                tvThirty.setVisibility(View.VISIBLE);
                break;
            case "forty_five":
                tvFortyFive.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbFortyFive.setChecked(true);
                tvFortyFive.setVisibility(View.VISIBLE);
                break;
            case "sixty":
                tvSixty.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbSixty.setChecked(true);
                tvSixty.setVisibility(View.VISIBLE);
                break;
            case "ninty":
                tvNinty.setText(TimeUtils.getAnswerLength(surplusTime));
                setNormal(-100000);
                cbNinty.setChecked(true);
                tvNinty.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_collect:
                if (!Util.hintLogin(AudioControlActivity.this))
                    return;
                if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.OneDayArticles || AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.AlbumContent)
                    toHttpCollect();
                else
                    ToastMaker.showShortToast("此音频类型不支持收藏");
                break;
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_audio_more:
                showMore();
                break;
            case R.id.img_last:
                //播放上一曲
                AudioPlayManager.getInstance().last();
                if (AudioPlayManager.getInstance().getCurrAudio() != null) {
                    maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                    Glide.with(this).load(Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getCoverImg()))
                            .placeholder(R.drawable.icon_readload_failed)
                            .error(R.drawable.icon_readload_failed)
                            .into(imgTodayCor);
                }
                imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
                break;
            case R.id.img_media_play:
                if (AudioPlayManager.getInstance().isPlaying()) {
                    AudioPlayManager.getInstance().pause();
                    DemoApplication.isPlay = false;
                    imgPlay.setImageResource(R.drawable.icon_today_audio_play);
                } else {
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                    imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
                }
                break;
            case R.id.img_next:
                AudioPlayManager.getInstance().next();
                if (AudioPlayManager.getInstance().getCurrAudio() != null) {
                    maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                    Glide.with(this).load(Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getCoverImg()))
                            .placeholder(R.drawable.icon_readload_failed)
                            .error(R.drawable.icon_readload_failed)
                            .into(imgTodayCor);
                }
                imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
                break;
            case R.id.img_audio_list:
                showPop();
                break;
            case R.id.img_play_model:
                //选择排序模式
                if (AudioPlayManager.getInstance().getModel() == AudioPlayManager.PlayModel.ORDER) {
                    AudioPlayManager.getInstance().setModel(AudioPlayManager.PlayModel.LOOP);
                    imgPlayModel.setImageResource(R.drawable.icon_today_circuit_model);
                    Toast.makeText(AudioControlActivity.this, "循环模式", Toast.LENGTH_SHORT).show();
                } else if (AudioPlayManager.getInstance().getModel() == AudioPlayManager.PlayModel.LOOP) {
                    AudioPlayManager.getInstance().setModel(AudioPlayManager.PlayModel.RANDOM);
                    imgPlayModel.setImageResource(R.drawable.icon_today_random_model);
                    Toast.makeText(AudioControlActivity.this, "随机模式", Toast.LENGTH_SHORT).show();
                } else {
                    AudioPlayManager.getInstance().setModel(AudioPlayManager.PlayModel.ORDER);
                    imgPlayModel.setImageResource(R.drawable.icon_today_order_model);
                    Toast.makeText(AudioControlActivity.this, "顺序模式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_today_timing:
                if (takePhotoPopWin == null) {
                    initTimerPopupWindow();
                }
                takePhotoPopWin.showAtLocation(llAudioControl, Gravity.CENTER, 0, 0);
                break;
            case R.id.img_today_audio_draft:
                if (AudioPlayManager.getInstance().getCurrAudio() == null || AudioPlayManager.getInstance().getType() != AudioPlayManager.PlayType.OneDayArticles) {
                    Toast.makeText(AudioControlActivity.this, "很抱歉，没有相应音频稿", Toast.LENGTH_SHORT).show();
                } else
                    mContext.startActivity(new Intent(AudioControlActivity.this, RecommendWebActivity.class).putExtra("url", SystemConstant.MYURL + "cmsweb/course/courseinfo/" + AudioPlayManager.getInstance().getCurrId()));
                break;
//            case R.id.ll_download:
//                if (!Util.hintLogin(this))
//                    return;
//                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(AudioPlayManager.getInstance().getmMediaPlayId());
//                if (albumContent == null) {
//                    if (playList.get(playPosition).getAudiourl() == null || TextUtils.isEmpty(playList.get(playPosition).getAudiourl()) || "null".equals(playList.get(playPosition).getAudiourl())) {
//                        Toast.makeText(AudioControlActivity.this, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    String suffixes = AudioPlayManager.getInstance().getUrl().substring(AudioPlayManager.getInstance().getUrl().lastIndexOf("."));
//                    AlbumContent downloadFile = new AlbumContent(playList.get(playPosition).getTitle());
//                    downloadFile.setArticleid(playList.get(playPosition).getId());
//                    downloadFile.setAlbumName("日课");
//                    downloadFile.setAuthorname("");
//                    downloadFile.setAlbumId("rike");
//                    downloadFile.setArticletype(2);
//                    downloadFile.setAlbumAvatar("");
//                    downloadFile.setCoverimgurl("");
//                    if (playList.get(playPosition).getAudiourl().startsWith("/"))
//                        downloadFile.setMediaurl(playList.get(playPosition).getAudiourl());
//                    else
//                        downloadFile.setMediaurl("/" + playList.get(playPosition).getAudiourl());
//                    downloadFile.setDownloadState(0);
//                    downloadFile.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + playList.get(playPosition).getTitle() + suffixes);
//                    DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(downloadFile);
//                    try {
//                        DownloadManager.getInstance().startDownload(
//                                SystemConstant.ACCESS_IMG_HOST + downloadFile.getMediaurl()
//                                , downloadFile.getArticlename()
//                                , Util.getSDCardPath() + "/MOZDownloads/" + downloadFile.getArticlename() + suffixes, true, false, null
//                                , 0, downloadFile.getArticleid());
//                        Toast.makeText(AudioControlActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                        Toast.makeText(AudioControlActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    if (albumContent.getDownloadState() == 0) {
//                        Toast.makeText(AudioControlActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
//                        //代表该内容已下载完成
//                    } else if (albumContent.getDownloadState() == 1) {
//                        if (Util.fileExisted(albumContent.getFilePath())) {
//                            Toast.makeText(AudioControlActivity.this, "该文件已下载", Toast.LENGTH_SHORT).show();
//                        } else {
//                            //开始下载
//                            String suffixes = AudioPlayManager.getInstance().getUrl().substring(AudioPlayManager.getInstance().getUrl().lastIndexOf("."));
//                            AlbumContent downloadFile = new AlbumContent(playList.get(playPosition).getTitle());
//                            downloadFile.setArticleid(playList.get(playPosition).getId());
//                            downloadFile.setAlbumName("日课");
//                            downloadFile.setAuthorname("");
//                            downloadFile.setAlbumId("rike");
//                            downloadFile.setArticletype(2);
//                            downloadFile.setAlbumAvatar("");
//                            downloadFile.setCoverimgurl("");
//                            if (playList.get(playPosition).getAudiourl().startsWith("/"))
//                                downloadFile.setMediaurl(playList.get(playPosition).getAudiourl());
//                            else
//                                downloadFile.setMediaurl("/" + playList.get(playPosition).getAudiourl());
//                            downloadFile.setDownloadState(0);
//                            downloadFile.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + playList.get(playPosition).getTitle() + suffixes);
//                            DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(downloadFile);
//                            try {
//                                DownloadManager.getInstance().startDownload(
//                                        SystemConstant.ACCESS_IMG_HOST + downloadFile.getMediaurl()
//                                        , downloadFile.getArticlename()
//                                        , Util.getSDCardPath() + "/MOZDownloads/" + downloadFile.getArticlename() + suffixes, true, false, null
//                                        , 0, downloadFile.getArticleid());
//                                Toast.makeText(AudioControlActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                                Toast.makeText(AudioControlActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//                break;
//            case R.id.ll_click_container:
//                //点赞
//                if (DB.getInstance(DemoApplication.applicationContext).isClick(AudioPlayManager.getInstance().getmMediaPlayId())) {
//                    Toast.makeText(AudioControlActivity.this, "已经赞过了，不能再赞", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                toHttpClick();
//                break;
        }
    }

    String mediaUrl;
    String name;
    String url;
    String cover;

    private void showMore() {

        if (AudioPlayManager.getInstance().getCurrAudio() != null) {
            mediaUrl = Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getUrl());
            name = AudioPlayManager.getInstance().getCurrAudio().getName();
            cover = Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getCoverImg());
            if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.OneDayArticles) {
                url = SystemConstant.MYURL + "view/appshare/courseShare.jsp?courseid=" + AudioPlayManager.getInstance().getCurrId();
            } else if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.AlbumContent) {
                url = SystemConstant.MYURL + "cmsweb/article/share/" + AudioPlayManager.getInstance().getCurrId();
            } else if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.MozReadDetailsBean) {
                url = SystemConstant.MYURL + "book/share/" + AudioPlayManager.getInstance().getCurrId();
            } else if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.MagazineExcerptBean) {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsid", AudioPlayManager.getInstance().getCurrId());
                ApiServerManager.getInstance().getApiServer().getShareId(map).enqueue(new RetrofitCallBack<String>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                        if (response.body().getCode() == 200) {
                            ShareId = response.body().getData();
                            url = SystemConstant.MYURL + "magazine/sharebuy/" + ShareId;
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {

                    }
                });
            } else {
                url = mediaUrl;
            }
        }
        if (TextUtils.isEmpty(url)) {
            ToastMaker.showShortToast("分享失败");
            return;
        }
        if (mAudioPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
            llShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llShareQq = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
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
            mAudioPopupWindow.showAtLocation(llAudioControl, Gravity.BOTTOM, 0, 0);
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

            if (isReport) {
                imgControlReport.setChecked(true);
                imgControlReport.setEnabled(false);
            }
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mAudioPopupWindow.showAtLocation(llAudioControl, Gravity.BOTTOM, 0, 0);
        }
        llShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                if (AudioPlayManager.getInstance().getCurrAudio() == null) {
                    Toast.makeText(AudioControlActivity.this, "很抱歉，没有相应音频,无法分享", Toast.LENGTH_SHORT).show();
                } else {
                    shareAudio(
                            SHARE_MEDIA.QQ,
                            name,
                            null,
                            mediaUrl,
                            url,
                            cover,
                            AudioPlayManager.getInstance().getCurrId()
                    );
                }
            }

        });

        llShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                shareAudio(
                        SHARE_MEDIA.WEIXIN,
                        name,
                        null,
                        mediaUrl,
                        url,
                        cover,
                        AudioPlayManager.getInstance().getCurrId()
                );

            }
        });
        llShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                if (AudioPlayManager.getInstance().getCurrAudio() == null) {
                    Toast.makeText(AudioControlActivity.this, "很抱歉，没有相应音频,无法分享", Toast.LENGTH_SHORT).show();
                } else {
                    shareAudio(
                            SHARE_MEDIA.WEIXIN_CIRCLE,
                            name,
                            null,
                            mediaUrl,
                            url,
                            cover,
                            AudioPlayManager.getInstance().getCurrId()
                    );
                }
            }
        });
    }


    /**
     * 取消举报
     */
    private void toHttpRemoveReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", AudioPlayManager.getInstance().getCurrId());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.REMOVE_REPORT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "取消举报成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "取消举报失败", Toast.LENGTH_SHORT).show();
                    imgControlReport.setChecked(true);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DemoApplication.applicationContext, "取消举报失败", Toast.LENGTH_SHORT).show();
                imgControlReport.setChecked(true);
            }
        });
    }

    /**
     * 弹出举报框
     */
    private void showReportPop() {
        if (reportPopupwindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_report, null);
            etTitle = (EditText) view.findViewById(R.id.et_title);
            etContent = (EditText) view.findViewById(R.id.et_content);
            reportPopupwindow = new PopupWindow(view, -2, -2);
            reportPopupwindow.setContentView(view);
            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            reportPopupwindow.setFocusable(true);
            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(AudioControlActivity.this, reportPopupwindow);
                }
            });
            //发送举报
            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(etTitle.getText().toString()) || TextUtils.isEmpty(etContent.getText().toString())) {
                        Toast.makeText(DemoApplication.applicationContext, "标题或内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Util.finishPop(AudioControlActivity.this, reportPopupwindow);
                    toHttpReport(etTitle.getText().toString(), etContent.getText().toString());
                }
            });
            //取消举报
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgControlReport.setChecked(false);
                    Util.finishPop(AudioControlActivity.this, reportPopupwindow);
                }
            });
            reportPopupwindow.showAtLocation(llAudioControl, Gravity.CENTER, 0, 0);
        } else {
            etTitle.setText("");
            etContent.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            reportPopupwindow.showAtLocation(llAudioControl, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 发送举报
     *
     * @param title
     * @param content
     */
    private void toHttpReport(String title, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("targetid", AudioPlayManager.getInstance().getCurrId());
        map.put("report_type", "ARTICLE");
        map.put("content", content);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SUBMIT_REPORT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "举报成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
                    imgControlReport.setChecked(false);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
                imgControlReport.setChecked(false);
            }
        });
    }

    /**
     * 播放列表
     */
    private void showPop() {
        if (playListPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_audio_control_list, null);
            playAdapter = new PlayListAdapter(AudioPlayManager.getInstance().getAudioList(), this);
            lvAudioList = (ListView) view.findViewById(R.id.lv_audio_list);
            lvAudioList.setAdapter(playAdapter);
            playListPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            playListPopupWindow.setContentView(view);
            playListPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            playListPopupWindow.setFocusable(true);
            playListPopupWindow.setOutsideTouchable(true);
            playListPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            playListPopupWindow.showAtLocation(llAudioControl, Gravity.BOTTOM, 0, 0);
            playListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AudioControlActivity.this, playListPopupWindow);
                }
            });
            lvAudioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (AudioPlayManager.getInstance().getPosition() != position) {
                        AudioPlayManager.getInstance().playForPosition(position);
                        playAdapter.notifyDataSetChanged();
                        Util.finishPop(AudioControlActivity.this, playListPopupWindow);
                    }
                    Glide.with(AudioControlActivity.this).load(Util.getImgUrl(AudioPlayManager.getInstance().getCurrAudio().getCoverImg()))
                            .placeholder(R.drawable.icon_readload_failed)
                            .error(R.drawable.icon_readload_failed)
                            .into(imgTodayCor);
                    maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                    //点击播放列表子项，如果当前正在播放的不等于点击时的position则开始播放

                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            playListPopupWindow.showAtLocation(llAudioControl, Gravity.BOTTOM, 0, 0);
        }
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.today_everyday_hear_one,
                R.drawable.today_everyday_hear_two,
                R.drawable.today_everyday_hear_three,
        };
        private int count = imgs.length;

        public void add() {
            count++;
            if (count > imgs.length) count = imgs.length;
            notifyDataSetChanged();
        }

        public void minus() {
            count--;
            if (count < 1) count = 1;
            notifyDataSetChanged();
        }

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof AudioControlActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.AUDIO_COMPLET:
                if (cbFinish != null && cbFinish.isChecked()) {
                    seekBar.setProgress(0);
                    tvCurrTime.setText("00:00");
                    imgPlay.setImageResource(R.drawable.icon_today_audio_play);
                    return;
                } else {
                    seekBar.setProgress(0);
                    tvCurrTime.setText("00:00");
                    imgPlay.setImageResource(R.drawable.icon_today_audio_play);
                }
                break;
            case SystemConstant.ON_PLAYING:
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                tvTotalTime.setText(AudioPlayManager.getInstance().getDuration());
                imgPlay.setImageResource(R.drawable.icon_today_audio_stop);
                break;
            case SystemConstant.ON_PREPARE:
                //正在准备
                maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                toHttpGetCollectState();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(AudioControlActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AudioControlActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AudioControlActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AudioControlActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 初始化定时PopupWindow
     */
    private void initTimerPopupWindow() {
        takePhotoPopWin = new TakePhotoPopWin(this, null);
        popupWindowView = takePhotoPopWin.getView();
        rlNo = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_no);
        rlFinish = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_finish);
        rlTen = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_ten);
        rlTwenty = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_twenty);
        rlThirty = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_thirty);
        rlFortyFive = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_forty_five);
        rlSixty = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_sixty);
        rlNinty = (RelativeLayout) popupWindowView.findViewById(R.id.rl_timer_ninty);
        cbNo = (CheckBox) popupWindowView.findViewById(R.id.checkbox_no);
        cbFinish = (CheckBox) popupWindowView.findViewById(R.id.checkbox_finish);
        cbTen = (CheckBox) popupWindowView.findViewById(R.id.checkbox_ten);
        cbTwenty = (CheckBox) popupWindowView.findViewById(R.id.checkbox_twenty);
        cbThirty = (CheckBox) popupWindowView.findViewById(R.id.checkbox_thirty);
        cbFortyFive = (CheckBox) popupWindowView.findViewById(R.id.checkbox_forty_five);
        cbSixty = (CheckBox) popupWindowView.findViewById(R.id.checkbox_sixty);
        cbNinty = (CheckBox) popupWindowView.findViewById(R.id.checkbox_ninty);
        cbList.add(cbNo);
        cbList.add(cbFinish);
        cbList.add(cbTen);
        cbList.add(cbTwenty);
        cbList.add(cbThirty);
        cbList.add(cbFortyFive);
        cbList.add(cbSixty);
        cbList.add(cbNinty);
        for (CheckBox checkBox : cbList) {
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        }
        tvFinish = (TextView) popupWindowView.findViewById(R.id.tv_finish);
        tvTen = (TextView) popupWindowView.findViewById(R.id.tv_ten);
        tvTwenty = (TextView) popupWindowView.findViewById(R.id.tv_twenty);
        tvThirty = (TextView) popupWindowView.findViewById(R.id.tv_thirty);
        tvFortyFive = (TextView) popupWindowView.findViewById(R.id.tv_forty_five);
        tvSixty = (TextView) popupWindowView.findViewById(R.id.tv_sixty);
        tvNinty = (TextView) popupWindowView.findViewById(R.id.tv_ninty);
        tvList.add(tvFinish);
        tvList.add(tvTen);
        tvList.add(tvTwenty);
        tvList.add(tvThirty);
        tvList.add(tvFortyFive);
        tvList.add(tvSixty);
        tvList.add(tvNinty);
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
        unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
    }


    /**
     * 根据选择定时发送定时任务，flag的作用是当开启了定时之后推出了这个activity然后再进来，弹出定时选择框显示所选定时
     */
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (flag) {
                switch (buttonView.getId()) {
                    case R.id.checkbox_no:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            stopTimeService();
                        } else {

                        }
                        break;
                    case R.id.checkbox_finish:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            try {
                                tvFinish.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration() - AudioPlayManager.getInstance().getCurrentPosition()));
                            } catch (Exception e) {
                                tvFinish.setText("00:00");
                            }
                            tvFinish.setVisibility(View.VISIBLE);
                        } else {
                        }
                        break;
                    case R.id.checkbox_ten:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvTen.setText(TimeUtils.getAnswerLength(600));
                            tvTen.setVisibility(View.VISIBLE);
                            startTimeService("ten", 600);
                        } else {
                            stopTimeService();
                            tvTen.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.checkbox_twenty:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvTwenty.setText(TimeUtils.getAnswerLength(1200));
                            tvTwenty.setVisibility(View.VISIBLE);
                            startTimeService("twenty", 1200);
                        } else {
                            stopTimeService();
                            tvTwenty.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.checkbox_thirty:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvThirty.setText(TimeUtils.getAnswerLength(1800));
                            tvThirty.setVisibility(View.VISIBLE);
                            startTimeService("thirty", 1800);
                        } else {
                            tvThirty.setVisibility(View.GONE);
                            stopTimeService();
                        }
                        break;
                    case R.id.checkbox_forty_five:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvFortyFive.setText(TimeUtils.getAnswerLength(2700));
                            tvFortyFive.setVisibility(View.VISIBLE);
                            startTimeService("forty_five", 2700);
                        } else {
                            stopTimeService();
                            tvFortyFive.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.checkbox_sixty:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvSixty.setText(TimeUtils.getAnswerLength(3600));
                            tvSixty.setVisibility(View.VISIBLE);
                            startTimeService("sixty", 3600);
                        } else {
                            stopTimeService();
                            tvSixty.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.checkbox_ninty:
                        if (isChecked) {
                            setNormal(buttonView.getId());
                            tvNinty.setText(TimeUtils.getAnswerLength(5400));
                            tvNinty.setVisibility(View.VISIBLE);
                            startTimeService("ninty", 5400);
                        } else {
                            stopTimeService();
                            tvNinty.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }
    };

    private void stopTimeService() {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }

    private void startTimeService(String type, int time) {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("type", type);
        intent.putExtra("time", time);
        startService(intent);
    }

    /**
     * 设置除传进来的checkbox为未选中，如果传递-100000则表示全部checkbox为未选中
     *
     * @param id 不希望发生改变的checkbox id
     */
    private void setNormal(int id) {
        for (CheckBox checkBox : cbList) {
            if (checkBox.getId() != id) {
                checkBox.setChecked(false);
            }
        }
        for (TextView textView : tvList) {
            textView.setVisibility(View.GONE);
        }
    }

    class PlayListAdapter extends BaseAdapter {

        private List<AudioPlayManager.AudioBean> mDatas;
        private Context mContext;
        private LayoutInflater mInflater;

        public PlayListAdapter(List<AudioPlayManager.AudioBean> mDatas, Context mContext) {
            this.mDatas = mDatas;
            this.mContext = mContext;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public AudioPlayManager.AudioBean getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_audio_play, parent, false);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tvName.setText(mDatas.get(position).getName());
            if (TextUtils.isEmpty(mDatas.get(position).getAuthorName()))
                holder.tvAuthorName.setText("匿名");
            else
                holder.tvAuthorName.setText(mDatas.get(position).getAuthorName());
            if ((mDatas.get(position).getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
                holder.tvName.setTextColor(getResources().getColor(R.color.defaultcolortwo));
            } else {
                holder.tvName.setTextColor(getResources().getColor(R.color.menu));
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            private TextView tvAuthorName;
        }

    }
}