package com.yidiankeyan.science.subscribe.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.EmptyActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.adapter.AudioPlayListAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.ContentDetailBean;
import com.yidiankeyan.science.subscribe.entity.RelevantRecommend;
import com.yidiankeyan.science.subscribe.service.TimerService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.MarqueeTextView;
import com.yidiankeyan.science.view.TakePhotoPopWin;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 音频专辑播放详情页
 */
public class AudioAlbumActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private ImageView btnTimer;
    /**
     * 定时PopupWindow
     */
    private TakePhotoPopWin takePhotoPopWin;
    private View rlAll;
    private MarqueeTextView maintitleTxt;

    //订阅
    private boolean isFu;
    //    private ImageView imgAudioSub;
    private ImageView imgInsideBg;

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
    private TextView tvTen;
    private TextView tvTwenty;
    private TextView tvThirty;
    private TextView tvFortyFive;
    private TextView tvSixty;
    private TextView tvNinty;
    private List<CheckBox> cbList = new ArrayList<>();
    private List<TextView> tvList = new ArrayList<>();
    private AutoLinearLayout llShare;
    /**
     * 播放模式。0:顺序;1:循环;2:随机
     */
    private int playModel = 0;

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
    /**
     * 当前播放的条目在播放列表的位置
     */
    private int playPosition;
    /**
     * 上一首
     */
    private ImageView imgLast;
    /**
     * 播放，暂停
     */
    private ImageView mediaPlays;
    /**
     * 下一首
     */
    private ImageView mediaNext;
    /**
     * 点击展开播放列表
     */
    private ImageView imgAudioList;
    private TextView textView1CurrTime;
    private TextView textView1TotalTime;
//    private CheckBox imgReport;

    /**
     * 播放列表
     */
    private ArrayList<AlbumContent> mContentList;
//    private List<AudioBean> fmList = new ArrayList<>();
    /**
     * 监控正在播放的定时器
     */
    private Timer mTimer;
    private TimerTask mTimerTask;

    private SeekBar seekBar;
    /**
     * 播放列表的PopupWindow
     */
    private PopupWindow mPopupWindow;
    private PopupWindow subPopupWindow;
    private EditText etCustomPrice;
    private PopupWindow editPricePopupWindow;
    private ListView lvAudioList;

    /**
     * 分享
     */
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private ImageView llSearchSina;
    private PopupWindow reportPopupwindow;
    private EditText etTitle;
    private EditText etContent;

    /**
     * 打赏
     */
    private ImageView imgGratuity;
    /**
     * 播放模式
     */
    private TextView tvPlayMode;
    /**
     * 排序模式
     */
    private TextView tvSortMode;
    /**
     * 播放列表adapter
     */
    private AudioPlayListAdapter playAdapter;

    /**
     * 随着播放进度更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                try {
                    textView1CurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                    seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                    textView1TotalTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                    if (cbFinish != null && cbFinish.isChecked()) {
                        tvFinish.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration() - AudioPlayManager.getInstance().getCurrentPosition()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private TextView tvAuthorName;
    private ContentDetailBean contentDetailBean;
    //    private CheckBox cbClick;
    private ImageView imgCover;
    private InputMethodManager inputMethodManager;

    /**
     * 相关推荐
     */
    private List<RelevantRecommend> datas = new ArrayList<>();

    /**
     * 点赞的id，当退出这个界面的时候就可以进行循环点赞请求
     */
    private List<String> contentClickList = new ArrayList<>();
    /**
     * 评论的id，退出时开始点赞请求
     */
    private List<String> commentClickList = new ArrayList<>();
    private AlbumDetail album;

    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;
    private String id;
    private ImageView imgCollect;
    /**
     * 打赏金额
     */
    private String price;
    private ImageView imgPlayModel;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_audio;
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (MarqueeTextView) findViewById(R.id.maintitle_txt);
        imgCollect = (ImageView) findViewById(R.id.img_collect);
        btnTimer = (ImageView) findViewById(R.id.btn_timer);
        llShare = (AutoLinearLayout) findViewById(R.id.ll_share);
        imgLast = (ImageView) findViewById(R.id.img_last);
        imgCover = (ImageView) findViewById(R.id.img_audio_avatar);
        mediaPlays = (ImageView) findViewById(R.id.img_media_play);
        imgPlayModel = (ImageView) findViewById(R.id.img_play_model);
        imgInsideBg = (ImageView) findViewById(R.id.img_inside_bg);
        mediaNext = (ImageView) findViewById(R.id.img_next);
        textView1CurrTime = (TextView) findViewById(R.id.tv_curr_time);
        textView1TotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvAuthorName = (TextView) findViewById(R.id.tv_author_name);
        imgAudioList = (ImageView) findViewById(R.id.img_audio_list);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        rlAll = findViewById(R.id.rl_all);
//        imgGratuity = (ImageView) findViewById(R.id.img_gratuity);
//        imgAudioSub = (ImageView) findViewById(R.id.img_audio_sub);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setSelected(true);
        mContentList = getIntent().getParcelableArrayListExtra("list");
        playPosition = getIntent().getIntExtra("position", 0);
        imgCollect.setTag(0);

//        if (album != null) {
//            if (album.getIsorder() == 1) {
//                isFu = true;
////                imgAudioSub.setImageResource(R.drawable.icon_sub_pressed);
//            } else
//                isFu = false;
//        } else {
//        }
        if (mContentList != null) {
            maintitleTxt.setText(mContentList.get(playPosition).getArticlename());

            //正在播放的音频与当前的音频不同，把音频播放器释放然后播放当前的音频
            if (!(mContentList.get(playPosition).getArticleid()).equals(AudioPlayManager.getInstance().getCurrId())) {
                if (mContentList != null && mContentList.size() != 0 && !TextUtils.isEmpty(mContentList.get(playPosition).getMediaurl())) {
                    AudioPlayManager.getInstance().init(mContentList, playPosition, AudioPlayManager.PlayModel.ORDER);
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                    //开始播放
                }
            } else {
                //正在播放的音频与当前音频一致，查看音频播放器的状态是否播放中，如果是则改变UI
                if (SystemConstant.ON_PLAYING == AudioPlayManager.getInstance().CURRENT_STATE) {
                    mediaPlays.setImageResource(R.drawable.icon_today_audio_stop);
                    textView1TotalTime.setText(TimeUtils.getAnswerLength(AudioPlayManager.getInstance().getIntDuration()));
                    seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                }
            }
            toHttpGetDetail();
            toHttpGetCollectState();
        }
        llReturn.setOnClickListener(this);
        btnTimer.setOnClickListener(this);
        llShare.setOnClickListener(this);
        imgPlayModel.setOnClickListener(this);
        imgLast.setOnClickListener(this);
        imgCollect.setOnClickListener(this);
//        imgAudioSub.setOnClickListener(this);
        imgAudioList.setOnClickListener(this);
        mediaPlays.setOnClickListener(this);
//        imgGratuity.setOnClickListener(this);
        findViewById(R.id.img_comment).setOnClickListener(this);
        mediaNext.setOnClickListener(this);
        playAdapter = new AudioPlayListAdapter(mContentList, mContext);
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //用户改变了进度条，音频播放跳到指定地方
                if (fromUser) {
                    if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE)
                        AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //触摸时暂停播放
                AudioPlayManager.getInstance().pause();
                DemoApplication.isPlay = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //释放时开始播放
                if (AudioPlayManager.getInstance().mIjkMediaPlayer != null) {
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                }

            }
        });
    }

    /**
     * 获取专辑详情
     */
    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", contentDetailBean.getAlbumid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    album = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    /**
     * 判断是否收藏
     */
    private void toHttpGetCollectState() {
        Map<String, Object> map = new HashMap<>();
        map.put("contentid", mContentList.get(getPlayPosition()).getArticleid());
        map.put("type", 6);
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

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取此内容的详情
     */
    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mContentList.get(getPlayPosition()).getArticleid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_CONTENT_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    contentDetailBean = (ContentDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), ContentDetailBean.class);
                    initData();
                }
                imgLast.setOnClickListener(AudioAlbumActivity.this);
                mediaNext.setOnClickListener(AudioAlbumActivity.this);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                imgLast.setOnClickListener(AudioAlbumActivity.this);
                mediaNext.setOnClickListener(AudioAlbumActivity.this);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        toHttpGetAlbumDetail();
        Glide.with(this).load(Util.getImgUrl(contentDetailBean.getCoverimgurl())).crossFade().into(imgCover);
//        Glide.with(this).load(Util.getImgUrl(contentDetailBean.getCoverimgurl())).crossFade().bitmapTransform(new BlurTransformation(this)).into(imgInsideBg);
        tvAuthorName.setText(contentDetailBean.getUsername());
        if (!(contentDetailBean.getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
            int index = 0;
            for (int i = 0; i < mContentList.size(); i++) {
                AlbumContent content = mContentList.get(i);
                if (content.getArticleid().equals(contentDetailBean.getId())) {
                    index = i;
                }
            }
            mContentList.get(index).setArticletype(2);
            mContentList.get(index).setMediaurl(contentDetailBean.getMediaurl());
            mContentList.get(index).setLastupdatetime(contentDetailBean.getCreatetime());
            maintitleTxt.setText(contentDetailBean.getName());
            AudioPlayManager.getInstance().init(mContentList, index, AudioPlayManager.PlayModel.ORDER);
            AudioPlayManager.getInstance().ijkStart();
            DemoApplication.isPlay = true;
        }
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
            seekBar.setProgress(0);
            mediaPlays.setImageResource(R.drawable.icon_today_audio_play);
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
        Intent intent;
        switch (v.getId()) {
            case R.id.img_comment:
                ToastMaker.showShortToast("很抱歉，没有相应音频稿");

//                intent = new Intent(this, AllCommentActivity.class);
//                intent.putExtra("id", mContentList.get(getPlayPosition()).getArticleid());
//                startActivity(intent);
                break;
            case R.id.ll_return:
                finish();
                break;
            case R.id.btn_timer:
                if (takePhotoPopWin == null) {
                    initTimerPopupWindow();
                }
                takePhotoPopWin.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
                break;
            case R.id.img_last:
                if (mContentList.size() == 1) {
                    Toast.makeText(DemoApplication.applicationContext, "没有更多资源了", Toast.LENGTH_SHORT).show();
                    return;
                }
                AudioPlayManager.getInstance().last();
                toHttpGetDetail();
                toHttpGetCollectState();
                break;
            case R.id.img_media_play:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PREPARE:
                        //音频播放器在准备过程中
                        Toast.makeText(mContext, "正在加载", Toast.LENGTH_SHORT).show();
                        break;
                    case SystemConstant.ON_PAUSE:
                        //音频播放器暂停中，开始播放
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_STOP:
                        //音频播放器处于停止中，重新开始播放
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        //暂停
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        break;
                }
                break;
            case R.id.img_next:
                //播放下一曲
                if (mContentList.size() == 1) {
                    Toast.makeText(DemoApplication.applicationContext, "没有更多资源了", Toast.LENGTH_SHORT).show();
                    return;
                }
                AudioPlayManager.getInstance().next();
                toHttpGetDetail();
                toHttpGetCollectState();
                break;
            case R.id.img_audio_list:
                //展开播放列表
                showPop();
                break;
            case R.id.tv_sort_mode:
                //播放列表排序
                AlbumContent temp;
                temp = mContentList.get(getPlayPosition());
                Collections.reverse(mContentList);
                for (int i = 0; i < mContentList.size(); i++) {
                    if (temp.equals(mContentList.get(i))) {
                        playPosition = i;
                        break;
                    }
                }
                playAdapter.notifyDataSetChanged();
                if (tvSortMode.getText().toString().equals("正序"))
                    tvSortMode.setText("倒序");
                else
                    tvSortMode.setText("正序");
                break;
//            case R.id.tv_audio_sub:
            //订阅
//                if (isFu) {
//                    isFu = false;
//                    txtAudioSub.setText("订阅");
//                } else {
//                    isFu = true;
//                    txtAudioSub.setText("已订阅");
//                    txtAudioSub.setEnabled(false);
//                }
            //临时，用于测试点赞的清除数据库点赞数据
//                new DB(this).deleteAllClick();
//                break;
            case R.id.ll_share:
                showSharePop();
                break;
//            case R.id.img_audio_sub:
//                if (!Util.hintLogin(this))
//                    return;
//                //订阅专辑
//                if (isFu) {
//                    showCustomPop();
//                } else {
//                    toHttpSubAlbum();
//                }
//                break;
            case R.id.img_collect:
                if (!Util.hintLogin(AudioAlbumActivity.this))
                    return;
                toHttpCollect();
                break;
            case R.id.img_play_model:
                //选择排序模式
                switch (playModel) {
                    case 0:
                        playModel = 1;
                        imgPlayModel.setImageResource(R.drawable.icon_today_circuit_model);
                        Toast.makeText(this, "循环模式", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        playModel = 2;
                        imgPlayModel.setImageResource(R.drawable.icon_today_random_model);
                        Toast.makeText(this, "随机模式", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        playModel = 0;
                        imgPlayModel.setImageResource(R.drawable.icon_today_order_model);
                        Toast.makeText(this, "顺序模式", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
//            case R.id.img_gratuity:
//                //打赏
//                if (!Util.hintLogin(AudioAlbumActivity.this))
//                    return;
//                showEditPricePop();
//                break;
        }
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
        map.put("contentid", getIntent().getStringExtra("id"));
        map.put("type", 6);
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

    private void showEditPricePop() {
        if (editPricePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_custom_rmb, null);
            ((TextView) view.findViewById(R.id.tv_pop_author_name)).setText(contentDetailBean == null ? "" : contentDetailBean.getUsername());
            view.findViewById(R.id.img_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AudioAlbumActivity.this, editPricePopupWindow);
                }
            });
            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etCustomPrice.getText())) {
                        Util.finishPop(AudioAlbumActivity.this, editPricePopupWindow);
                    } else {
                        Toast.makeText(AudioAlbumActivity.this, "请输入您要打赏的金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            etCustomPrice = (EditText) view.findViewById(R.id.et_custom_price);
            etCustomPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String numString = s.toString();
                    if (numString.startsWith("0")) {
                        etCustomPrice.setText("");
                    } else {
                        if (numString.length() > 0) {
                            int num = Integer.parseInt(numString);
                            if (num > 200) {
                                etCustomPrice.setText(200 + "");
                                etCustomPrice.setSelection(3);
                            }
                        }
                    }
                }
            });
            editPricePopupWindow = new PopupWindow(view, -2, -2);
            editPricePopupWindow.setContentView(view);
            editPricePopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            editPricePopupWindow.setFocusable(true);
            editPricePopupWindow.setOutsideTouchable(true);
            editPricePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            editPricePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(etCustomPrice.getText().toString())) {
                        price = "";
                    } else {
                        price = etCustomPrice.getText().toString();
                        showPayWindow();
                    }
                    Util.finishPop(AudioAlbumActivity.this, editPricePopupWindow);
                }
            });
            editPricePopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        } else {
            etCustomPrice.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            editPricePopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        }
    }

    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AudioAlbumActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(AudioAlbumActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(AudioAlbumActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AudioAlbumActivity.this, payPopupWindow);
                    showWaringDialog("支付", "是否使用墨子币打赏音频？", new OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            toHttpBalancePay();
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                }
            });
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = AudioAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            AudioAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AudioAlbumActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = AudioAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            AudioAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥" + price + ",请再次确认购买");
    }

    private void toHttpBalancePay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("amount", price);
        map.put("goodtype", "TIP_ARTICLE");
        map.put("sellerid", contentDetailBean.getUserid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + "size/balance/pay/insert", map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "打赏成功", Toast.LENGTH_SHORT).show();
                    id = (String) result.getData();
                    toHttpBestow();
                } else if (result.getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    private void aliPay() {
        if (contentDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("price", price);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/alipay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(AudioAlbumActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DemoApplication.applicationContext, "打赏成功", Toast.LENGTH_SHORT).show();
                                        toHttpBestow();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DemoApplication.applicationContext, "打赏失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void wxPay() {
        if (contentDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("price", price);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/wechatPay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    id = wxPay.getId();
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(AudioAlbumActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 打赏
     */
    private void toHttpBestow() {
        if (contentDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("tip_type", "TIP_ARTICLE");
        map.put("trade_id", id);
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("tip_price", price);
        map.put("message", "");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.BESTOW, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void showCustomPop() {
        if (subPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_subalbum, null);
//            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
//            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
//            tvFinish.setOnClickListener(this);
//            tvYesClick.setOnClickListener(this);
            subPopupWindow = new PopupWindow(view, -2, -2);
            subPopupWindow.setContentView(view);
            subPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            subPopupWindow.setFocusable(true);
            subPopupWindow.setOutsideTouchable(true);
            subPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            subPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
//                    finishPop(subPopupWindow);
                    Util.finishPop(AudioAlbumActivity.this, subPopupWindow);
                }
            });
            subPopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            subPopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        }
        subPopupWindow.getContentView().findViewById(R.id.tv_no_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.finishPop(AudioAlbumActivity.this, subPopupWindow);
//                imgAudioSub.setImageResource(R.drawable.icon_sub_pressed);
                isFu = true;
            }
        });
        subPopupWindow.getContentView().findViewById(R.id.tv_yes_onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.finishPop(AudioAlbumActivity.this, subPopupWindow);
                toHttpAbolishOrder();
            }
        });
//        imgAudioSub.setImageResource(R.drawable.icon_sub_pressed);
    }

    /**
     * 订阅
     */
    private void toHttpSubAlbum() {
        if (album == null)
            return;
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", album.getAlbumid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (200 == result.getCode()) {
                    isFu = true;
//                    imgAudioSub.setImageResource(R.drawable.icon_sub_pressed);
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_ORDER_ALBUM));
                } else if (306 == result.getCode()) {
                    if ("order-already".equals(result.getMsg())) {
                        isFu = true;
//                        imgAudioSub.setImageResource(R.drawable.icon_sub_pressed);
                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_ORDER_ALBUM));
                    } else
                        Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                } else
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 取消订阅
     */
    private void toHttpAbolishOrder() {
        if (album == null)
            return;
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", album.getAlbumid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ABOLISH_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (200 == result.getCode()) {
//                    imgAudioSub.setImageResource(R.drawable.icon_sub_normal);
                    isFu = false;
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_ABOLISH_ORDER_ALBUM));
                } else {
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }


    /**
     * 弹出分享匡
     */
    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            llSearchSina = (AutoLinearLayout) view.findViewById(R.id.ll_search_sina);
//            imgReport = (CheckBox) view.findViewById(R.id.img_report);
//            cbClick = (CheckBox) view.findViewById(R.id.img_click);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AudioAlbumActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AudioAlbumActivity.this, sharePopupWindow);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADcATEDASIAAhEBAxEB/8QAGwABAAMBAQEBAAAAAAAAAAAAAAIGBwUDBAH/xABAEAACAQMCBAMEBgcHBQEAAAAAAQIDBAUGERIhMVEHE0EUImFxFTJVgaHBNlJzkaOxshYjN0JTcoIXNDWU0dL/xAAbAQEAAgMBAQAAAAAAAAAAAAAAAgQBAwUGB//EACsRAQACAgIBAgUEAgMAAAAAAAABAgMRBBIFITETFEFRYRU0cZEWIjIzwf/aAAwDAQACEQMRAD8AqgAOw5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACIAAkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIgACQAAAAAAAAAAAAAAAAAADWdBYDD09JfTV5ZU72vUlLi4oKflxT22SfTpuzkeIOjqFjGnmsNR2squyq06a3UG+kkvRP8H8zs+FdlmLWyuK1zFU8RXXFCNXlJy/WivSLXXuWCGboYitXtqG1zbb8VJJ8oP1jv2OHyubHEzdr2/1X4pW2OIn0Ytb6fzN1BToYm9qRfRxoS2JVtOZu3g51cRfQiurdCRsFTVt9Jvy6dGC9N05fmfkNW38X78KM18mvzK/+S8bev/Gv4FPuoegdHLNXtS+ydGSx9s9nCacfNn2+S9S26uwGCvtH3eStLCla1LWLlRq04KHGk9tuXVM6tbUNPJRp2tVey0py/vp777x7L5nO8SLDK3unqEcTGNTHUvfr06XOckujXeK7I38fnxy828dvSGzpWtJ16sXAB3VAAAAAAAAAAAAAAAAAAAAAEQABIAAAAAAAAAAAAAAAAC4+Huloagy07m7jvYWm0qifSpPqo/L1ZTt13RtOnaKw3hnbOHu1bteZKS7zf/5RT5vI+BhtduwU7W9Xrm8zK7qO2tnwWsPd2jy49vyJWen/AGnEO8dxwz4XKMduWy7nEPWNzXhQdGNaapS6wUuTPnPzdcuW1+RHbft+Fve59XkACgi7dPT/AB4X272jafA5qDXLY8MPmamNrKMm5W0n70e3xR8Cuq6oOgq0/Kf+Ti5HkXZ5VcdqXwR1mPf8pb+zk+JOl6OOr081j4JWd3LapGC92E3z3Xwlz+/5lANxdGOa0Fk7CrzdKnJwb57NLij+KMN3XLdpH0Xx3J+YwVuq56atuPq/QAX2gAAAAAAAAAAAAAAAAABEAASAAAAAAAAAAAAAANI0NozH1cT9P5yCqUHu6NGX1Wk9uJ9930Rm5uEEoeHOGUeSdGl0+Rz/ACXItg49r1+jfx6xMzMvWV1pm9j7JXw9CNu+SboxSX7uaO1cwx2GwttbzoOraUuGnTg/e2Xp1KKy05ht6UsW3z9z+TPJ8bymfNhyd/eI2t1n3R+mcF9mfw4j6ZwX2Z/DiVgHL/VM32j+jtKz/TOC+zP4cR9M4L7M/hxKwB+qZvtH9MdpWf6ZwX2Z/DiPpnBfZn8OJWAZ/Vc32j+me0r3i6+PyNtc07a2dGm1wVFso77r4HEjW0xi4uztcRQnSj7spKjF7/e+bPfS/KwyHy/JlY9DpZvKZsXGx2pqJtsmfSJeWtNGYyvhJ57A040vKXFXoRW0ZR9Wl6Nduhl5uuLSnpPMxlzj5VTk/wDYzCV0XyPV+K5FuRx4vb3VeRWImJh+gA6SuAAAAAAAAAAAAAAAIgACQAAAAAAAAAAAAABuC/w6wv7Gl/SYebiv8OsL+xpf0nH85+zt/CzxveXDLRl/0Tsf+H8mVctGX/ROx/4fyZ4ng/8AVm/hYr7Sq4AOWiAAAAALPpf/ALDIfL8mVj0LPpf/AMfkPl+TKx6HT5n7XD/EpT/xhYcT+i2Z/ZVP6GYQvqr5G74n9Fsz+yqf0MwhfVXyPa+B/aVV+R7Q/QAdpWAAAAAAAAAAAAAAAEQABIAAAAAAAAAAAAAA2+2l7V4bYmpS96NOlDi29NuT/ExAuWjNdT05TnYXtGVzjZty4Y7cVNvrtv1T7FLyHGnk4Jxx9W7DeKz6rH15LqWrOU5UdL2dOa2lFwTXbkzhvXWirVO5t6Naddc4wVBp7/fyR2ctfrK6Tsb9U3TVxwVFBvfh3T5bnkv0vJw+Nltf6wuVmup1KsAA8ugAAAAALRpWLlZX8Ut20kv3Mq7i4txkmpLk0/Rln0vW9nschW4eLy1xbd9k2cWOvdG5CKubyhWpV2t5QlRbe/zjyZ6anjb83h45pPslMxqImXSsZK10Zl7ir7tN0qmzfr7m38zC10XyLzrLXqzlosXi6ErbHLbjcuUqu3Rbei+BRj1vjuLPGwRjlVz3i0xEAAL7QAAAAAAAAAAAAAAAIgACQAAAAAAAAAAAAAAAAGzaNv7bUui6eK82ML2ySi4vrsvqy27bcjGT2tbq4srmFxa16lGtDnGdOWzRX5XHryMc47fVsxZOk7bDWwORoQnOdBcEE25Kaa2OadTRGayGc0hk62RuHXq0pShGTSTS4E/Q5a6Hzzy/Apw8kVp9Vz0mImH12uLvL2m6lvRc4p7N7pcyVzib60oOtXoOFNNJviR18XcVbTSGUuaMuGrShUnCW2+zUN0eVC/ucp4cWV7d1PMuK0YynPZLd8T9EWa+KxTwvmNzvTOo9nAPvt8LkLqlCrRocVOa3jLiSPg9Ttagyt5hvDmjeWFbya6cIqeyfJy2fUp+K4VOXlml2I1qZl+5m7oaP0jd+dVhK9uoyhTpp/Wk1ty+C35sw/oj6b7IXmTuXc31zVuKz5cdSW727Lsj5j6JxOLXjY4x1VMuTvPoAAtNQAAAAAAAAAAAAAAAAACIAAkAAAAAAAAAAAAAAAAAAA1nwskq+lsxawadXzX7u/eHL+TPPZxbi001yafoUPTWpLvTOUV5bJThJcNWjJ7KpHt8H2ZpEfEHR93H2m6s68Ljq4OhxNv5p7P7zzXmfE5OXeL0lcxZKzSImdadHjVl4fZevX3jCdGpw7+u8eFfifNp9+1eFVkqPvujDhml1TjN7lI1lrupqOnGxsqLtsdBpuMvrVGum+3RLsfPo7WlfS9WpRqUncWFZ71KSe0ov9aP5r12LlPHTHD+B+D41Yv+FrPs14/ZPDa2t63u1alSkoxfXfdyf4B+IGjacVdU7Su6/VU1b7NP9+xn2rNV3Wqb+FWpDybaluqNBPfh36tv1bKHiPD5OLkm95ZyXrWsxEq+AD1CkAAAAAAAAAAAAAAAAAAAACIAAkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIgACQAAAAAAAAAHRwEaM9RY2Nxt5Luqanv24kYmdRtmI3Ol6wHhtaQxkcnqW7dvTlFTVHjVNQT6cUn6/A+/6C8MvtO3/APef/wBPk8Yal2rnGUW5KzcJy29HU39fjt+ZmHMq1rbJXvNli1q0t1irQfEPSmI09j7CvjKU4uvUlGTlUct1w7rqQ0ZoKhlbD6YzVZ0cfzcIKXC5pdZOXpE7Pi1y0/hvhN/0I9ddydn4Y4q2oNwpT8iEkvWPBvt+9EYvaaRXfulNa95nXslQwPh1m6nsGPuKcbrZqLpVpKTfw4uUjPNU6audMZR2lWXmUpxc6NZLbjj+TXqcaFapQqRrUpuFSm+OEk+aa5pmseLEY1dPYi4kl5vmfW+cN3+KJ6tjvEb3tDcZKTOtafLjsN4dVMZazu8hQjcyowdVO8a2nst+XpzPsWhdGZylUp4TJpXEVydK4VXb5xfVGR8/idHA1LulqDHzsXL2nz4KHD1fPmvltuZtimI3FmK5Yn0mr8zmFu8BlK1heRSqw5qUfqzi+kl8DQrXDeG0rSjKtkaSquEXNe1yW0tuZ5+MUaCu8VNbe0OnUUv9m62/Hc+W38I8jcW9KtHK2sVUgppOlLlut+5ibxakTadJRTreYrG3VehdH563rU9P5Je1U47+5W8xLtxJ89vijNaeGvquc+h4Ud73znR4O0k9m9+3rv2NY0toj+xt5cZjI5SlKFOhKPuxcYxi+bcm/kV7R2Qtst4r3l+o8Ma6rToKXXfZJffwpkaZJjep3DN8cTrcal1oaO0dpWzpS1FcwrXM1z8yckm/XhhHnt8Tyv8AQWn9Q4ud9pS5hGrHfhhGblTk/wBVp84sqXiJ7X/ba/8AauLZ8Pk79PL2W23w6/fudfwk9q/tFdunxezez/33bi3XD9/X8RNbRT4nb1ZiazbppXtI4ehlNX2+LyNKflydSNSG7i04xfL96Oll8JhcX4jU8XXfk4pODqupVa2Thu95fPY62N8r/rfW8nbg8+r07+Xz/Hc43iZ+nV5+zp/0onFptfW/oh1itN/lavoPwy+0rf8A91nzZjw1xt9jJZDS9752ybVLzVUhP4KXVP5mX8zRPCKpdLO3tKDl7K6HFUXop7rhfz6mL0tSO0WZret56zDPGnGTUk009mn6M/Dt6xjRhrHLxt9vLVzLkuif+b8dziFis7iJV7RqdAAJMAAAAAAAAAAIgACQAAAAAAAABNppptNc016AAapide4PN4iGM1ZQTlFJOrKDlCbXSXLnGRxNZUdFU8RTlp2dGV550eJQnNvg2e/Xl12KMDTGCIncS2zmmY1MNE8RtQ4rM4XGUMde07irRk+OMU/d9zb1Xc9NbaixOT0VjLGyvada5oypOdOKe8doNPqu5m4EYYjX4JyzO/y/Jc4v5M0jxD1Ficvp3G2+PvadetRqJzjFPde5t6ruZwCdqdrRP2Ri8xEx92oY2j4aPGWrvKlBXTow87edX6+y36fE++jnfDzTTld4uEK10ovh8qE5y+5y5IyAGqcG/eZTjNr2iHW1Hn7nUmYqX9ylBNcFOknuqcF0X5s8I5vKxioxyd4klskq8uX4nwA3RSIjTVNpmdtH0JrWlSp3mO1HfqdnKG9OVzvPrylHfm2tik+2fRGoZ3eKre7b3EpW9RdHFN7fc0c4EYxREzP3TnJMxEfZr0NZ6O1RaUlqG2hQuILpVhJpP14ZR57fM8b/AF7p3T+LqWWlbeE6st9pwpuNOL/WbfOTMnBD5eu/wn8e2vysmjMpQsdZ2uRyNyoU06kqtWe75uL5vbu2dLO5DB5jxLhdXFxTq4ifAqtTmotKD3+PXYpIJzjibdkIyTEaa15HhZ/q2+/7SqQvtd6c07iqllpW3jOrPfapGm4wi/1m5c5MygEIwR9Z2l8afpGkqk5Vakqk5OU5ycpSfVt9WRAN7SAAAAAAAAAAAACIAAkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIgCfCuw4V2HaGeqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7DhXYdoOqAJ8K7HnXflW9WpFLeMHJb/BDtB1foOF9NXH+nR/c/8A6CHeEvhy/9k=");
//                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(AudioAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                            .withText("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (album != null)
                        AudioAlbumActivity.this.shareAudio(
                                SHARE_MEDIA.WEIXIN,
                                album.getAlbumname(),
                                mContentList.get(getPlayPosition()).getArticlename(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getMediaurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl(), mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new ShareAction(AudioAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                            .withTitle("test")
//                            .withText("赛思测试分享")
//                            .withMedia(new UMImage(AudioAlbumActivity.this, "data:image/jpeg;base64,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"))
//                            .withTargetUrl("http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996")
//                            .share();
                    if (album != null)
                        AudioAlbumActivity.this.shareAudio(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                album.getAlbumname(),
                                mContentList.get(getPlayPosition()).getArticlename(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getMediaurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl(), mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
            llSearchQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(AudioAlbumActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                            .withTitle("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (album != null)
                        AudioAlbumActivity.this.shareAudio(
                                SHARE_MEDIA.QQ,
                                album.getAlbumname(),
                                mContentList.get(getPlayPosition()).getArticlename(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getMediaurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl(), mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
//            llSearchSina.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
////                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
////                    new ShareAction(AudioAlbumActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
////                            .withText("赛思测试分享")
////                            .withTitle("test")
////                            .withMedia(image)
////                            //.withExtra(new UMImage(ShareActivity.this,R.drawable.ic_launcher))
////                            .withTargetUrl(url)
////                            .share();
//                    if (album != null)
//                        AudioAlbumActivity.this.shareAudio(
//                                SHARE_MEDIA.SINA,
//                                album.getAlbumname(),
//                                mContentList.get(getPlayPosition()).getArticlename(),
//                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(),
//                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
//                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl(), mContentList.get(getPlayPosition()).getArticleid()
//                        );
//                }
//            });
//            cbClick.setOnClickListener(this);
//            cbClick.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (contentDetailBean == null) {
//                        return;
//                    }
//                    if (DB.getInstance(DemoApplication.applicationContext).isClick(contentDetailBean.getId())) {
//                        Toast.makeText(DemoApplication.applicationContext, "你已经点过赞了", Toast.LENGTH_SHORT).show();
//                    } else {
//                        DB.getInstance(DemoApplication.applicationContext).insertClick(contentDetailBean.getId());
//                        toHttpClick(contentDetailBean.getId());
//                    }
//                }
//            });
//            imgReport.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.finishPop(AudioAlbumActivity.this, sharePopupWindow);
//                    showReportPop();
//                }
//            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
        }
    }

//    private void showReportPop() {
//        if (reportPopupwindow == null) {
//            View view = View.inflate(this, R.layout.popupwindow_report, null);
//            etTitle = (EditText) view.findViewById(R.id.et_title);
//            etContent = (EditText) view.findViewById(R.id.et_content);
//            reportPopupwindow = new PopupWindow(view, -2, -2);
//            reportPopupwindow.setContentView(view);
//            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.8f;
//            getWindow().setAttributes(lp);
//            reportPopupwindow.setFocusable(true);
//            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
//            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//                @Override
//                public void onDismiss() {
//                    Util.finishPop(AudioAlbumActivity.this, reportPopupwindow);
//                }
//            });
//            //发送举报
//            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (TextUtils.isEmpty(etTitle.getText().toString()) || TextUtils.isEmpty(etContent.getText().toString())) {
//                        Toast.makeText(DemoApplication.applicationContext, "标题或内容不能为空", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Util.finishPop(AudioAlbumActivity.this, reportPopupwindow);
//                    toHttpReport(etTitle.getText().toString(), etContent.getText().toString());
//                }
//            });
//            //取消举报
//            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imgReport.setChecked(false);
//                    Util.finishPop(AudioAlbumActivity.this, reportPopupwindow);
//                }
//            });
//            reportPopupwindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
//        } else {
//            etTitle.setText("");
//            etContent.setText("");
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            getWindow().setAttributes(lp);
//            reportPopupwindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
//        }
//    }
//
//    private void toHttpReport(String title, String content) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("title", title);
//        map.put("targetid", getIntent().getStringExtra("id"));
//        map.put("report_type", "ARTICLE");
//        map.put("content", content);
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SUBMIT_REPORT, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    Toast.makeText(DemoApplication.applicationContext, "举报成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
//                    imgReport.setChecked(false);
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
//                imgReport.setChecked(false);
//            }
//        });
//    }

    @Override
    protected void onShareSuccess(String shareId) {
        super.onShareSuccess(shareId);
    }

    /**
     * 播放列表
     */
    private void showPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_play_list, null);
            lvAudioList = (ListView) view.findViewById(R.id.lv_audio_list);
            lvAudioList.setAdapter(playAdapter);
            tvPlayMode = (TextView) view.findViewById(R.id.tv_play_mode);
            tvSortMode = (TextView) view.findViewById(R.id.tv_sort_mode);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            tvSortMode.setOnClickListener(this);
            tvPlayMode.setOnClickListener(this);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AudioAlbumActivity.this, mPopupWindow);
                }
            });
            lvAudioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击播放列表子项，如果当前正在播放的不等于点击时的position则开始播放
                    if (!TextUtils.equals(playAdapter.getItem(position).getArticleid(), AudioPlayManager.getInstance().getCurrId())) {
                        AudioPlayManager.getInstance().playForPosition(position);
                        toHttpGetDetail();
                        playAdapter.notifyDataSetChanged();
                        Util.finishPop(AudioAlbumActivity.this, mPopupWindow);
                    }
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
        }
    }

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

    /**
     * 矫正播放位置
     *
     * @return
     */
    private int getPlayPosition() {
        playPosition = AudioPlayManager.getInstance().getPosition();
        return playPosition;
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_PREPARE:
                //正在准备
                maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                break;
            case SystemConstant.ON_PLAYING:
                Log.e("state", "ON_PLAYING");
                //开始播放
                maintitleTxt.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                mediaPlays.setImageResource(R.drawable.icon_today_audio_stop);
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                textView1TotalTime.setText(TimeUtils.getAnswerLength(AudioPlayManager.getInstance().getIntDuration()));
                break;
            case SystemConstant.ON_BUFFERING:
                //缓冲区变化
                seekBar.setMax(msg.getArg1());
                break;
            case SystemConstant.ON_STOP:
                Log.e("state", "ON_STOP");
                //停止播放
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlays.setImageResource(R.drawable.icon_today_audio_play);
                    }
                });
                break;
            case SystemConstant.ON_PAUSE:
                Log.e("state", "ON_PAUSE");
                //暂停播放
                mediaPlays.setImageResource(R.drawable.icon_today_audio_play);
                break;
            case SystemConstant.AUDIO_COMPLET:
                Log.e("state", "AUDIO_COMPLET");
                if (cbFinish != null && cbFinish.isChecked()) {
                    seekBar.setProgress(0);
                    textView1CurrTime.setText("00:00");
                    mediaPlays.setImageResource(R.drawable.icon_today_audio_play);
                    return;
                } else {
                    seekBar.setProgress(0);
                    textView1CurrTime.setText("00:00");
                    if (mContentList.size() != 1) {
                        toHttpGetDetail();
                        toHttpGetCollectState();
                    } else {
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                    }
                }
                break;
            case SystemConstant.ON_CLICK_CHANGE:
                //评论的赞发生变化
                String commentId = (String) msg.getBody();
                if (msg.getArg1() == 0) {
                    //取消点赞
                    //该评论存在于点赞列表
                    if (commentClickList.contains(commentId)) {
                        //删除数据库的音频
                        DB.getInstance(DemoApplication.applicationContext).deleteClick(commentId);
                        //将该音频从点赞列表移除
                        commentClickList.remove(commentId);
                    } else {
                        //将数据库的音频点赞状态设置成未点赞
                        DB.getInstance(DemoApplication.applicationContext).updataClick(0, commentId);
                    }
                } else {
                    //点赞
                    if (DB.getInstance(DemoApplication.applicationContext).existedClickTable(commentId)) {
                        //将评论状态设置成已点赞
                        DB.getInstance(DemoApplication.applicationContext).updataClick(1, commentId);
                    } else {
                        //该评论未被点赞，将其加到需要点赞的列表
                        commentClickList.add(commentId);
                        //向数据库插入该评论已点赞
                        DB.getInstance(DemoApplication.applicationContext).insertClick(commentId);
                    }
                }
                break;
        }
    }

    private void toHttpClick(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("uptype", "ARTICLE");
        map.put("targetid", id);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoApplication.getInstance().activityExisted(EmptyActivity.class)) {
            DemoApplication.getInstance().finishActivity(EmptyActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将点赞列表里面的音频挨个点赞
//        for (String id : contentClickList) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("uptype", "ARTICLE");
//            map.put("targetid", id);
//            HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
//                @Override
//                public void successResult(ResultEntity result) throws JSONException {
//
//                }
//
//                @Override
//                public void errorResult(Throwable ex, boolean isOnCallback) {
//
//                }
//            });
//        }
        for (String id : commentClickList) {
            Map<String, Object> map = new HashMap<>();
            map.put("uptype", "COMMENT");
            map.put("targetid", id);
            HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {

                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {

                }
            });
        }
        unregisterReceiver(broadcastReceiver);
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
//        if (mediaPlayer != null)
//            mediaPlayer.release();
//        mediaPlayer = null;
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


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
