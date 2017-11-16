package com.yidiankeyan.science.subscribe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.yidiankeyan.science.my.activity.HisDataActivity;
import com.yidiankeyan.science.my.activity.PersonalDataActivity;
import com.yidiankeyan.science.subscribe.adapter.ContentCommentAdapter;
import com.yidiankeyan.science.subscribe.adapter.RelevantRecommendAdapter;
import com.yidiankeyan.science.subscribe.adapter.VideoPlayListAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.subscribe.entity.ContentDetailBean;
import com.yidiankeyan.science.subscribe.entity.RelevantRecommend;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.NoScrollGridView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * 视频专辑
 */
public class VideoAlbumActivity extends BaseActivity {


    //    private AutoLinearLayout llReturn;
    //    private ImageButton btnTitle;
    private ListView lvVideoAlbum;
    private List<ContentCommentBean> mDates = new ArrayList<>();
    private ContentCommentAdapter adapter;
    private View viewtop;
    private JCVideoPlayerStandard videoPlayer;
    private int videoState;
    private TextView textView1CurrTime;
    private TextView textView1TotalTime;
    private SeekBar seekBar1;
    private ImageView imgAudioList;
    private ImageButton mediaLast;
    private ImageButton mediaPlays;
    private ImageButton mediaNext;
    private ImageButton btnFullScreen;
    private ArrayList<AlbumContent> mContentList;
    //    private List<VideoBean> videoList = new ArrayList<>();
    private int playPosition;
    //    private TextView maintitleTxt;
    private AutoRelativeLayout rlShare;
    private AutoLinearLayout llJumpData;

    private TextView btnComment;
    private EditText etPopupwindowComm;
    private Button btnSubmitComment;
    private PopupWindow commPopupWindow;
    private PopupWindow subPopupWindow;
    private View commView;
    private CheckBox imgReport;

    private boolean isFu;
    private TextView tvAudioSub;

    private PopupWindow mPopupWindow;
    private PopupWindow sharePopupWindow;
    private ListView lvVideoList;

    private PopupWindow graPopupWindow;
    private ImageView imgAlbumGratuity;
    private EditText etCustomPrice;
    private String price;
    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;
    private String id;
    private ContentDetailBean contentDetail;
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
    private VideoPlayListAdapter playAdapter;
    private AutoLinearLayout llAll;
    private InputMethodManager inputMethodManager;
    private TextView tvClickCount;
    private TextView tvCommentCount;
    private TextView tvShareCount;
    private ImageView imgClick;
    private ImageView imgPaid;
    private AutoLinearLayout llClick;
    private List<String> contentClickList = new ArrayList<>();
    private ContentDetailBean contentDetailBean;
    private ImageView imgPortrait;
    private TextView tvAuthorName;
    private CheckBox cbClick;
    /**
     * 评论的id，退出时开始点赞请求
     */
    private List<String> commentClickList = new ArrayList<>();

    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private AutoLinearLayout llSearchSina;
    private PopupWindow reportPopupwindow;
    private EditText etTitle;
    private EditText etContent;

    /**
     * 相关推荐
     */
    private List<RelevantRecommend> datas = new ArrayList<>();
    private RelevantRecommendAdapter relevantRecommendAdapter;
    private TextView tvFinish, tvYesClick;
    /**
     * 专辑推荐
     */
    private NoScrollGridView gvRecommendAlbum;
    private String albumId;

    private ImageView imgAlbumAvatar;
    private TextView tvAlbumName;
    private TextView tvRecentContent;
    private TextView tvAlbumAuthorName;
    private TextView tvContentNum;
    private TextView tvReadNum;
    private ImageView imgCollect;
    private AlbumDetail album;
    private Intent intent;
    private int isBroadcast = 0;
    private int isCollect = 1;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_video_album;
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btnComment = (TextView) findViewById(R.id.btn_comment);
//        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        imgCollect = (ImageView) findViewById(R.id.img_collect);
        lvVideoAlbum = (ListView) findViewById(R.id.lv_video_album);
        viewtop = LayoutInflater.from(this).inflate(R.layout.head_video_album, null);
        imgPortrait = (ImageView) viewtop.findViewById(R.id.img_portrait);
        tvAuthorName = (TextView) viewtop.findViewById(R.id.tv_author_name);
        llJumpData = (AutoLinearLayout) viewtop.findViewById(R.id.ll_jump_data);
        videoPlayer = (JCVideoPlayerStandard) viewtop.findViewById(R.id.video_player);
        textView1CurrTime = (TextView) viewtop.findViewById(R.id.textView1_curr_time);
        textView1TotalTime = (TextView) viewtop.findViewById(R.id.textView1_total_time);
        seekBar1 = (SeekBar) viewtop.findViewById(R.id.seekBar1);
        imgAudioList = (ImageView) viewtop.findViewById(R.id.img_audio_list);
        mediaLast = (ImageButton) viewtop.findViewById(R.id.media_last);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        mediaPlays = (ImageButton) viewtop.findViewById(R.id.media_plays);
        mediaNext = (ImageButton) viewtop.findViewById(R.id.media_next);
        btnFullScreen = (ImageButton) viewtop.findViewById(R.id.btn_full_screen);
        rlShare = (AutoRelativeLayout) findViewById(R.id.rl_share);
        tvClickCount = (TextView) viewtop.findViewById(R.id.tv_click_count);
        gvRecommendAlbum = (NoScrollGridView) viewtop.findViewById(R.id.gv_recommend_album);
        tvCommentCount = (TextView) viewtop.findViewById(R.id.tv_comment_count);
        tvShareCount = (TextView) viewtop.findViewById(R.id.tv_share_count);
        imgClick = (ImageView) viewtop.findViewById(R.id.img_click);
        imgPaid = (ImageView) viewtop.findViewById(R.id.img_paid);
        llClick = (AutoLinearLayout) viewtop.findViewById(R.id.ll_click);

        imgAlbumAvatar = (ImageView) viewtop.findViewById(R.id.img_album_avatar);
        tvAlbumName = (TextView) viewtop.findViewById(R.id.tv_album_name);
        tvRecentContent = (TextView) viewtop.findViewById(R.id.tv_recent_content);
        tvAlbumAuthorName = (TextView) viewtop.findViewById(R.id.tv_album_author_name);
        tvContentNum = (TextView) viewtop.findViewById(R.id.tv_content_num);
        tvReadNum = (TextView) viewtop.findViewById(R.id.tv_read_num);
        tvAudioSub = (TextView) viewtop.findViewById(R.id.tv_audio_sub);
//        imgAlbumGratuity = (ImageView) viewtop.findViewById(R.id.img_album_gratuity);
    }

    @Override
    protected void initAction() {
        mContentList = getIntent().getParcelableArrayListExtra("list");
        playPosition = getIntent().getIntExtra("position", 0);
        imgCollect.setTag(0);
        AudioPlayManager.getInstance().release();
        AudioPlayManager.getInstance().reset();
        //相关推荐
        relevantRecommendAdapter = new RelevantRecommendAdapter(this, datas);
        gvRecommendAlbum.setAdapter(relevantRecommendAdapter);
        relevantRecommendAdapter.notifyDataSetChanged();
        if (mContentList != null) {
//            maintitleTxt.setText(mContentList.get(getPlayPosition()).getArticlename());
            btnComment.setOnClickListener(this);
            toHttpGetComment();
            toHttpGetDetail();
            toHttpGetCollectState();
            llClick.setOnClickListener(this);
            //为评论输入框设置点击发送的事件
//            etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_SEND) {
//                        if (TextUtils.isEmpty(etComment.getText())) {
//                            Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
//                        } else {
//                            toHttpSendComment();
//                            hideSoftKeyboard();
//                        }
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }
//        btnTitle.setVisibility(View.GONE);
        adapter = new ContentCommentAdapter(mDates, this);
        lvVideoAlbum.addHeaderView(viewtop);
        lvVideoAlbum.setAdapter(adapter);
        videoPlayer.findViewById(R.id.back).setVisibility(View.VISIBLE);
        tvAudioSub.setOnClickListener(this);
        videoPlayer.findViewById(R.id.layout_top).setOnClickListener(this);
        mediaPlays.setOnClickListener(this);
        mediaNext.setOnClickListener(this);
        imgCollect.setOnClickListener(this);
        mediaLast.setOnClickListener(this);
        findViewById(R.id.rl_comment).setOnClickListener(this);
        rlShare.setOnClickListener(this);
        imgAudioList.setOnClickListener(this);
        llJumpData.setOnClickListener(this);
//        imgAlbumGratuity.setOnClickListener(this);
        if (getIntent().getIntExtra("currentState", -222) != -222 && getIntent().getIntExtra("currentState", -222) != 0) {
            try {
                AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mContentList.get(getPlayPosition()).getArticleid());
                if (content != null && Util.fileExisted(content.getFilePath())) {
                    videoPlayer.setUp(content.getFilePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
                } else {
                    videoPlayer.setUp(
                            Util.getImgUrl(mContentList.get(getPlayPosition()).getMediaurl()), JCVideoPlayer.SCREEN_LAYOUT_NORMAL,
                            mContentList.get(getPlayPosition()).getArticlename());
                }
                videoPlayer.setUiWitStateAndScreen(getIntent().getIntExtra("currentState", -1));
                videoPlayer.addTextureView();
                JCVideoPlayerManager.putListener(videoPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mContentList.get(getPlayPosition()).getArticleid());
            if (content != null && Util.fileExisted(content.getFilePath())) {
                videoPlayer.setUp(content.getFilePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
//            videoPlayer.setUp(content.getFilePath(), mContentList.get(getPlayPosition()).getArticlename());
                videoPlayer.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("video", "开始播放");
                        videoPlayer.startButton.performClick();
                        mediaPlays.setImageResource(R.drawable.icon_pause_today);
                    }
                });
            } else {
                if (mContentList.get(getPlayPosition()).getMediaurl() != null) {
                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
//                videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), mContentList.get(getPlayPosition()).getArticlename());
                    videoPlayer.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("video", "开始播放");
                            videoPlayer.startButton.performClick();
                            mediaPlays.setImageResource(R.drawable.icon_pause_today);
                        }
                    });
                }
            }
        }
        videoPlayer.findViewById(R.id.back).setVisibility(View.VISIBLE);
//        Glide.with(mContext).load(Util.getImgUrl(mContentList.get(getPlayPosition()).getCoverimgurl())).into(videoPlayer.thumbImageView);
        mContentList.get(getPlayPosition()).setPlaying(true);
        videoPlayer.titleTextView.setText(mContentList.get(getPlayPosition()).getArticlename());
        playAdapter = new VideoPlayListAdapter(mContentList, mContext);
    }

    /**
     * 获取专辑详情
     */
    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", contentDetail.getAlbumid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    addData();
                    album = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                    Glide.with(VideoAlbumActivity.this).load(Util.getImgUrl(album.getImgurl()))
                            .error(R.drawable.icon_hotload_failed)
                            .placeholder(R.drawable.icon_hotload_failed)
                            .into(imgAlbumAvatar);
                    tvAlbumName.setText(album.getAlbumname());
                    tvAlbumAuthorName.setText(album.getAuthorname());
                    tvContentNum.setText("内容量" + album.getContentnum() + "篇");
                    tvReadNum.setText("浏览量" + album.getReadnum());
                    tvRecentContent.setText(album.getSubjectname());
                    if (album.getIsorder() == 1) {
                        isFu = true;
                        tvAudioSub.setBackgroundResource(R.drawable.shape_subed);
                        tvAudioSub.setText("已订阅");
                        tvAudioSub.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tvAudioSub.setBackgroundResource(R.drawable.shape_sub);
                        tvAudioSub.setText("+订阅");
                        tvAudioSub.setTextColor(getResources().getColor(R.color.defaultcolor));
                        isFu = false;
                    }
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
        map.put("type", 7);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ARTICLE_IS_COLLECTED, map, new HttpUtil.HttpCallBack() {
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
     * 专辑详情
     * -获取相关推荐
     */
    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", contentDetail.getAlbumid());
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.POST_RELEVANT_RECOMMEND, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    datas.removeAll(datas);
                    List<RelevantRecommend> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), RelevantRecommend.class);
                    datas.addAll(data);
                    relevantRecommendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mContentList.get(getPlayPosition()).getArticleid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_CONTENT_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    contentDetail = (ContentDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), ContentDetailBean.class);
                    initData(contentDetail);
                }
                toHttpGetAlbumDetail();
                mediaLast.setOnClickListener(VideoAlbumActivity.this);
                mediaNext.setOnClickListener(VideoAlbumActivity.this);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                mediaLast.setOnClickListener(VideoAlbumActivity.this);
                mediaNext.setOnClickListener(VideoAlbumActivity.this);
            }
        });
    }

    private void initData(ContentDetailBean contentDetail) {
        this.contentDetailBean = contentDetail;
        Glide.with(this).load(Util.getImgUrl(contentDetail.getUserimgurl())).into(imgPortrait);
        tvAuthorName.setText(contentDetail.getUsername());
        videoPlayer.titleTextView.setText(contentDetail.getName());
        tvClickCount.setText(contentDetail.getUps() + "");
        tvCommentCount.setText(contentDetail.getCommentnum() + "");
        tvShareCount.setText(contentDetail.getReprintnum() + "");
        if (new DB(this).isClick(contentDetail.getId())) {
            imgClick.setImageResource(R.drawable.icon_today_click_pressed);
            imgClick.setTag(R.drawable.icon_today_click_pressed);
        } else {
            imgClick.setImageResource(R.drawable.icon_today_click_normal);
            imgClick.setTag(R.drawable.icon_today_click_normal);
        }
        if (TextUtils.isEmpty(videoPlayer.url)) {
            mContentList.get(getPlayPosition()).setMediaurl(contentDetail.getMediaurl());
            videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
//                videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), mContentList.get(getPlayPosition()).getArticlename());
            videoPlayer.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("video", "开始播放");
                    AudioPlayManager.getInstance().release();
                    AudioPlayManager.getInstance().reset();
                    videoPlayer.startButton.performClick();
                    mediaPlays.setImageResource(R.drawable.icon_pause_today);
                }
            });
        }
//        if (TextUtils.isEmpty(videoPlayer.getMediaUrl())) {
//            AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(contentDetail.getId());
//            if (content != null && Util.fileExisted(content.getFilePath())) {
//                videoPlayer.setUp(content.getFilePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, contentDetail.getName());
////            videoPlayer.setUp(content.getFilePath(), contentDetail.getName());
//                videoPlayer.setMediaId(contentDetail.getId());
//                videoPlayer.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("video", "开始播放");
//                        videoPlayer.startButton.performClick();
//                        mediaPlays.setImageResource(R.drawable.icon_pause_today);
//                    }
//                });
//            } else {
//                if (contentDetail.getMediaurl() != null) {
//                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + contentDetail.getMediaurl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, contentDetail.getName());
////                videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + contentDetail.getMediaurl(), contentDetail.getName());
//                    videoPlayer.setMediaId(contentDetail.getId());
//                    videoPlayer.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("video", "开始播放");
//                            videoPlayer.startButton.performClick();
//                            mediaPlays.setImageResource(R.drawable.icon_pause_today);
//                        }
//                    });
//                }
//            }
//        }
    }

    /**
     * 获取评论列表
     */
    private void toHttpGetComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", mContentList.get(getPlayPosition()).getArticleid());
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_MIX_COMMENT_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<ContentCommentBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ContentCommentBean.class);
                    mDates.removeAll(mDates);
                    mDates.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", mContentList.get(getPlayPosition()).getArticleid());
        map.put("content", etPopupwindowComm.getText().toString());
        map.put("type", 1);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
//                    etComment.setText("");
                    toHttpGetComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (msg.getWhat() == SystemConstant.ON_CLICK_CHANGE) {
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
            return;
        }
        switch (msg.getWhat()) {
            case SystemConstant.ON_ORDER_ALBUM:
                isFu = true;
                tvAudioSub.setBackgroundResource(R.drawable.shape_subed);
                tvAudioSub.setText("已订阅");
                tvAudioSub.setTextColor(getResources().getColor(R.color.white));
                album.setIsorder(1);
                return;
            case SystemConstant.ON_ABOLISH_ORDER_ALBUM:
                tvAudioSub.setBackgroundResource(R.drawable.shape_sub);
                tvAudioSub.setText("+订阅");
                tvAudioSub.setTextColor(getResources().getColor(R.color.defaultcolor));
                isFu = false;
                album.setIsorder(0);
                return;
            case SystemConstant.MOZ_COMMENT_DELETE:
                toHttpGetComment();
                return;
        }
        videoState = (int) msg.getBody();
        if (videoState == 555) {
            JCVideoPlayer.releaseAllVideos();
            mediaPlays.setImageResource(R.drawable.bofangs);
        }
        switch (videoState) {
            case 111:
                mediaPlays.setImageResource(R.drawable.bofangs);
                break;
            case 222:
                mediaPlays.setImageResource(R.drawable.icon_pause_today);
                break;
            case 333:
                mediaPlays.setImageResource(R.drawable.icon_pause_today);
                break;
            case 444:
                mediaPlays.setImageResource(R.drawable.bofangs);
                break;
            case 555:
                JCVideoPlayer.releaseAllVideos();
                mediaPlays.setImageResource(R.drawable.bofangs);
                break;
            case 666:
                mediaPlays.setImageResource(R.drawable.icon_pause_today);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //将点赞列表里面的音频挨个点赞
        for (String id : contentClickList) {
            Map<String, Object> map = new HashMap<>();
            map.put("uptype", "ARTICLE");
            map.put("targetid", id);
            HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {

                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {

                }
            });
        }
        for (String id : commentClickList) {
            Map<String, Object> map = new HashMap<>();
            map.put("uptype", "COMMENT");
            map.put("targetid", id);
            HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {

                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {

                }
            });
        }

        if (isCollect == 2) {
            EventMsg msg = EventMsg.obtain(SystemConstant.ON_INFOR_VIDEO);
            EventBus.getDefault().post(msg);
        }
        if (isBroadcast == 1 || isBroadcast == 2) {
            Intent intent = new Intent();
            intent.setAction("action.refreshFriend");
            sendBroadcast(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_comment:
                intent = new Intent(this, AllCommentActivity.class);
                intent.putExtra("id", mContentList.get(getPlayPosition()).getArticleid());
                startActivity(intent);
                break;
            case R.id.layout_top:
                finish();
                break;
            case R.id.ll_jump_data:
                if (!TextUtils.equals(album == null ? null : contentDetail.getUserid(), SpUtils.getStringSp(this, "userId"))) {
                    intent = new Intent(VideoAlbumActivity.this, HisDataActivity.class);
                    intent.putExtra("id", album == null ? null : contentDetail.getUserid());
                    startActivity(intent);
                } else {
                    intent = new Intent(this, PersonalDataActivity.class);
//                    intent.putExtra("id",mContentList.get(getPlayPosition()).getArticleid());
                    startActivity(intent);
                }
                break;
//            case R.id.media_plays:
//                switch (videoState) {
//                    case 111:
//                        //还没有播放
//                        videoPlayer.startButtonLogic();
//                        mediaPlays.setImageResource(R.drawable.icon_pause_today);
//                        break;
//                    case 222:
//                        //准备播放中
//                        JCVideoPlayer.releaseAllVideos();
//                        mediaPlays.setImageResource(R.drawable.bofangs);
//                        break;
//                    case 333:
//                        //正在播放
//                        //暂停
//                        videoPlayer.playingToPause();
//                        mediaPlays.setImageResource(R.drawable.bofangs);
//                        break;
//                    case 444:
//                        //暂停状态
//                        //播放
//                        videoPlayer.pauseToPlaying();
//                        mediaPlays.setImageResource(R.drawable.icon_pause_today);
//                        break;
//                    case 666:
//                        //缓冲状态
//                        videoPlayer.playingToPause();
//                        mediaPlays.setImageResource(R.drawable.bofangs);
//                        break;
//                }
//                break;
            case R.id.media_last:
                if (mContentList.size() == 1) {
                    Toast.makeText(DemoApplication.applicationContext, "没有更多资源了", Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaLast.setOnClickListener(null);
                mediaNext.setOnClickListener(null);
                videoPlayer.release();
                mContentList.get(getPlayPosition()).setPlaying(false);
                playPosition--;
                mContentList.get(getPlayPosition()).setPlaying(true);
//                AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mContentList.get(getPlayPosition()).getArticleid());
//                if (content != null && Util.fileExisted(content.getFilePath())) {
//                    videoPlayer.setUp(content.getFilePath(), mContentList.get(getPlayPosition()).getArticlename());
//                } else {
//                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), mContentList.get(getPlayPosition()).getArticlename());
//                }
//                Glide.with(mContext).load(Util.getImgUrl(mContentList.get(getPlayPosition()).getCoverimgurl())).into(videoPlayer.thumbImageView);
//                videoPlayer.startButtonLogic();
//                mediaPlays.setImageResource(R.drawable.icon_pause_today);
                textView1CurrTime.setText("00:00");
                textView1TotalTime.setText("00:00");
                seekBar1.setProgress(0);
                seekBar1.setSecondaryProgress(0);
                videoPlayer.titleTextView.setText(mContentList.get(getPlayPosition()).getArticlename());
                playAdapter.notifyDataSetChanged();
                toHttpGetComment();
                toHttpGetDetail();
                toHttpGetCollectState();
                break;
            case R.id.media_next:
                if (mContentList.size() == 1) {
                    Toast.makeText(DemoApplication.applicationContext, "没有更多资源了", Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaLast.setOnClickListener(null);
                mediaNext.setOnClickListener(null);
                videoPlayer.release();
                mContentList.get(getPlayPosition()).setPlaying(false);
                playPosition++;
                mContentList.get(getPlayPosition()).setPlaying(true);
//                AlbumContent content2 = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mContentList.get(getPlayPosition()).getArticleid());
//                if (content2 != null && Util.fileExisted(content2.getFilePath())) {
//                    videoPlayer.setUp(content2.getFilePath(), mContentList.get(getPlayPosition()).getArticlename());
//                } else {
//                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), mContentList.get(getPlayPosition()).getArticlename());
//                }
//                Glide.with(mContext).load(Util.getImgUrl(mContentList.get(getPlayPosition()).getCoverimgurl())).into(videoPlayer.thumbImageView);
//                videoPlayer.startButtonLogic();
//                mediaPlays.setImageResource(R.drawable.icon_pause_today);
                textView1CurrTime.setText("00:00");
                textView1TotalTime.setText("00:00");
                seekBar1.setProgress(0);
                seekBar1.setSecondaryProgress(0);
                videoPlayer.titleTextView.setText(mContentList.get(getPlayPosition()).getArticlename());
                playAdapter.notifyDataSetChanged();
                toHttpGetComment();
                toHttpGetDetail();
                toHttpGetCollectState();
                break;
            case R.id.img_audio_list:
                showPop();
                break;
            case R.id.tv_sort_mode:
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
            case R.id.rl_share:
                showSharePop();
                break;
            case R.id.btn_comment:
                showCommPop();
                break;
            case R.id.ll_click:
                break;
//            case R.id.img_click:
//                if (contentDetailBean == null) {
//                    return;
//                }
//                if (DB.getInstance(DemoApplication.applicationContext).isClick(contentDetailBean.getId())) {
//                    Toast.makeText(DemoApplication.applicationContext, "你已经点过赞了", Toast.LENGTH_SHORT).show();
//                } else {
//                    DB.getInstance(DemoApplication.applicationContext).insertClick(contentDetailBean.getId());
//                    toHttpClick(contentDetailBean.getId());
//                }
//                break;
            case R.id.tv_audio_sub:
                if (!Util.hintLogin(this) || contentDetail == null)
                    return;
                //订阅专辑
                if (isFu) {
                    showCustomPop();
                } else {
                    toHttpSubAlbum();
                }
                isBroadcast = 1;
                break;
            case R.id.tv_no_finish:
                Util.finishPop(VideoAlbumActivity.this, subPopupWindow);
                tvAudioSub.setText("已订阅");
                tvAudioSub.setTextColor(getResources().getColor(R.color.white));
                tvAudioSub.setBackgroundResource(R.drawable.select_qadetails_bg);
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                //取消订阅
                Util.finishPop(VideoAlbumActivity.this, subPopupWindow);
                toHttpAbolishOrder();
                isBroadcast = 2;
                break;
//            case R.id.img_album_gratuity:
//                if (!Util.hintLogin(VideoAlbumActivity.this) || contentDetail == null)
//                    return;
//                showCustomPops();
//                break;
            case R.id.img_collect:
                if (!Util.hintLogin(VideoAlbumActivity.this) || contentDetail == null)
                    return;
                toHttpCollect();
                break;
        }

    }

    private void toHttpClick(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("uptype", "ARTICLE");
        map.put("targetid", id);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void showCustomPops() {
        if (graPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_custom_rmb, null);
            view.findViewById(R.id.img_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(VideoAlbumActivity.this, graPopupWindow);
                }
            });
            ((TextView) view.findViewById(R.id.tv_pop_author_name)).setText(album == null ? "" : contentDetail.getUsername());
            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etCustomPrice.getText())) {
                        Util.finishPop(VideoAlbumActivity.this, graPopupWindow);
                    } else {
                        Toast.makeText(VideoAlbumActivity.this, "请输入您要打赏的金额", Toast.LENGTH_SHORT).show();
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
            graPopupWindow = new PopupWindow(view, -2, -2);
            graPopupWindow.setContentView(view);
            graPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            graPopupWindow.setFocusable(true);
            graPopupWindow.setOutsideTouchable(true);
            graPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            graPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(etCustomPrice.getText().toString())) {
                        price = "";
                    } else {
                        price = etCustomPrice.getText().toString();
                        showPayWindow();
                    }
                    Util.finishPop(VideoAlbumActivity.this, graPopupWindow);
                }
            });
            graPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        } else {
            etCustomPrice.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            graPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
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
                    Util.finishPop(VideoAlbumActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(VideoAlbumActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(VideoAlbumActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(VideoAlbumActivity.this, payPopupWindow);
                    showWaringDialog("支付", "是否使用墨子币打赏文章？", new OnDialogButtonClickListener() {
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
            WindowManager.LayoutParams lp = VideoAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            VideoAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(VideoAlbumActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = VideoAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            VideoAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥" + price + ",请再次确认购买");
    }

    private void toHttpBalancePay() {
        if (album == null)
            return;
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("amount", price);
        map.put("goodtype", "TIP_ARTICLE");
        map.put("sellerid", contentDetail.getUserid());
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
        if (album == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetail.getUserid());
        map.put("price", price);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + "size/transactions/alipay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(VideoAlbumActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "打赏成功", Toast.LENGTH_SHORT).show();
                                        toHttpBestow();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "打赏失败", Toast.LENGTH_SHORT).show();
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


    /**
     * 打赏
     */
    private void toHttpBestow() {
        if (album == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("tip_type", "TIP_ARTICLE");
        map.put("trade_id", id);
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetail.getUserid());
        map.put("tip_price", price);
        map.put("message", "");
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BESTOW, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void wxPay() {
        if (album == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetail.getUserid());
        map.put("price", price);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + "size/transactions/wechatPay", map, new HttpUtil.HttpCallBack() {

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
                    IWXAPI api = WXAPIFactory.createWXAPI(VideoAlbumActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
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
        map.put("contentid", getIntent().getStringExtra("id"));
        map.put("type", 7);
        HttpUtil.post(DemoApplication.applicationContext, url, map, new HttpUtil.HttpCallBack() {
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
                    isCollect = 2;
                }
                loadingDismiss();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    private void showCustomPop() {
        if (subPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_subalbum, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
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
                    Util.finishPop(VideoAlbumActivity.this, subPopupWindow);
                }
            });
            subPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            subPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        }
        tvAudioSub.setText("已订阅");
        tvAudioSub.setTextColor(getResources().getColor(R.color.white));
        tvAudioSub.setBackgroundResource(R.drawable.select_qadetails_bg);
    }

    /**
     * 订阅
     */
    private void toHttpSubAlbum() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", contentDetail.getAlbumid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ORDER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (200 == result.getCode()) {
                    isFu = true;
                    tvAudioSub.setBackgroundResource(R.drawable.select_qadetails_bg);
                    tvAudioSub.setText("已订阅");
                    tvAudioSub.setTextColor(getResources().getColor(R.color.white));
                } else if (306 == result.getCode()) {
                    if ("order-already".equals(result.getMsg())) {
                        isFu = true;
                        tvAudioSub.setBackgroundResource(R.drawable.select_qadetails_bg);
                        tvAudioSub.setText("已订阅");
                        tvAudioSub.setTextColor(getResources().getColor(R.color.white));
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
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", contentDetail.getAlbumid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ORDER_ABOLISH_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (200 == result.getCode()) {
                    tvAudioSub.setBackgroundResource(R.drawable.shape_sub);
                    tvAudioSub.setText("+订阅");
                    tvAudioSub.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                    isFu = false;
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

    private void showCommPop() {
        if (commPopupWindow == null) {
            commView = View.inflate(this, R.layout.popupwindow_comm, null);
            etPopupwindowComm = (EditText) commView.findViewById(R.id.et_popupwindow_comm);
            btnSubmitComment = (Button) commView.findViewById(R.id.btn_submit_comment);
            commPopupWindow = new PopupWindow(commView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT) {
                @Override
                public void dismiss() {
                    if (etPopupwindowComm != null)
                        inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    super.dismiss();
                }
            };
            commPopupWindow.setContentView(commView);
            commPopupWindow.setAnimationStyle(R.style.AnimBottom);
            commPopupWindow.setFocusable(true);
            commPopupWindow.setTouchable(true);
            commPopupWindow.setOutsideTouchable(false);
            commPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            btnSubmitComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!Util.hintLogin(VideoAlbumActivity.this)) return;
                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    Util.finishPop(VideoAlbumActivity.this, commPopupWindow);
                    toHttpSendComment();
                    etPopupwindowComm.setText("");
                }
            });
//            commPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
//                    return false;
//                }
//            });
            commPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(VideoAlbumActivity.this, commPopupWindow);
                }
            });
            etPopupwindowComm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        btnSubmitComment.setEnabled(true);
                        btnSubmitComment.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        btnSubmitComment.setEnabled(false);
                        btnSubmitComment.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            });
        }
        commPopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Util.openKeybord(etPopupwindowComm, this);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        commPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
    }


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
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(VideoAlbumActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(VideoAlbumActivity.this, sharePopupWindow);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(VideoAlbumActivity.this, "data:image/jpeg;base64,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");
//                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(VideoAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                            .withText("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (album != null)
                        VideoAlbumActivity.this.shareVideo(
                                SHARE_MEDIA.WEIXIN,
                                album.getAlbumname(),
                                mContentList.get(getPlayPosition()).getArticlename(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl()
                                , mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new ShareAction(VideoAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                            .withTitle("test")
//                            .withText("赛思测试分享")
//                            .withMedia(new UMImage(VideoAlbumActivity.this, "data:image/jpeg;base64,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"))
//                            .withTargetUrl("http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996")
//                            .share();
                    if (album != null)
                        VideoAlbumActivity.this.shareVideo(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                mContentList.get(getPlayPosition()).getArticlename(),
                                album.getAlbumname(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl()
                                , mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
            llSearchQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(VideoAlbumActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                            .withTitle("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (album != null)
                        VideoAlbumActivity.this.shareVideo(
                                SHARE_MEDIA.QQ,
                                album.getAlbumname(),
                                mContentList.get(getPlayPosition()).getArticlename(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl()
                                , mContentList.get(getPlayPosition()).getArticleid()
                        );
                }
            });
//            llSearchSina.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
////                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
////                    new ShareAction(VideoAlbumActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
////                            .withText("赛思测试分享")
////                            .withTitle("test")
////                            .withMedia(image)
////                            //.withExtra(new UMImage(ShareActivity.this,R.drawable.ic_launcher))
////                            .withTargetUrl(url)
////                            .share();
//                    if (album != null)
//                        VideoAlbumActivity.this.shareVideo(
//                                SHARE_MEDIA.SINA,
//                                album.getAlbumname(),
//                                mContentList.get(getPlayPosition()).getArticlename(),
//                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
//                                SystemConstant.MYURL + "cmsweb/article/share/" + mContentList.get(getPlayPosition()).getArticleid(),
//                                SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getCoverimgurl()
//                                , mContentList.get(getPlayPosition()).getArticleid()
//                        );
//                }
//            });
//            cbClick.setOnClickListener(this);
//            imgReport.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.finishPop(VideoAlbumActivity.this, sharePopupWindow);
//                    showReportPop();
//                }
//            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
//        if (contentDetailBean != null) {
//            if (DB.getInstance(DemoApplication.applicationContext).isClick(contentDetailBean.getId())) {
//                cbClick.setChecked(true);
//            } else {
//                cbClick.setChecked(false);
//            }
//        }
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
//                    Util.finishPop(VideoAlbumActivity.this, reportPopupwindow);
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
//                    Util.finishPop(VideoAlbumActivity.this, reportPopupwindow);
//                    toHttpReport(etTitle.getText().toString(), etContent.getText().toString());
//                }
//            });
//            //取消举报
//            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imgReport.setChecked(false);
//                    Util.finishPop(VideoAlbumActivity.this, reportPopupwindow);
//                }
//            });
//            reportPopupwindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
//        } else {
//            etTitle.setText("");
//            etContent.setText("");
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            getWindow().setAttributes(lp);
//            reportPopupwindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
//        }
//    }

    private void toHttpReport(String title, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("report_type", "ARTICLE");
        map.put("content", content);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.SUBMIT_REPORT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "举报成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
                    imgReport.setChecked(false);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
                imgReport.setChecked(false);
            }
        });
    }

    @Override
    protected void onShareSuccess(String shareId) {
        super.onShareSuccess(shareId);
        int shareCount = Integer.parseInt(tvShareCount.getText().toString());
        shareCount++;
        tvShareCount.setText(shareCount + "");
    }

    private void showPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_play_list, null);
            lvVideoList = (ListView) view.findViewById(R.id.lv_audio_list);
            lvVideoList.setAdapter(playAdapter);
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
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(VideoAlbumActivity.this, mPopupWindow);
                }
            });
            lvVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (getPlayPosition() != position) {
                        mediaLast.setOnClickListener(null);
                        mediaNext.setOnClickListener(null);
                        videoPlayer.release();
                        mContentList.get(getPlayPosition()).setPlaying(false);
                        playPosition = position;
                        mContentList.get(getPlayPosition()).setPlaying(true);
                        AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(mContentList.get(getPlayPosition()).getArticleid());
                        if (content != null && Util.fileExisted(content.getFilePath())) {
                            videoPlayer.setUp(content.getFilePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
//                            videoPlayer.setUp(content.getFilePath(), mContentList.get(getPlayPosition()).getArticlename());
                        } else {
                            videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mContentList.get(getPlayPosition()).getArticlename());
//                            videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + mContentList.get(getPlayPosition()).getMediaurl(), mContentList.get(getPlayPosition()).getArticlename());
                        }
                        videoPlayer.findViewById(R.id.back).setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(Util.getImgUrl(mContentList.get(getPlayPosition()).getCoverimgurl())).into(videoPlayer.thumbImageView);
                        videoPlayer.startButton.performClick();
                        mediaPlays.setImageResource(R.drawable.icon_pause_today);
                        textView1CurrTime.setText("00:00");
                        textView1TotalTime.setText("00:00");
                        seekBar1.setProgress(0);
                        seekBar1.setSecondaryProgress(0);
                        videoPlayer.titleTextView.setText(mContentList.get(getPlayPosition()).getArticlename());
                        playAdapter.notifyDataSetChanged();
                        toHttpGetComment();
                        toHttpGetDetail();
                        playAdapter.notifyDataSetChanged();
                        Util.finishPop(VideoAlbumActivity.this, mPopupWindow);
                    }
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoApplication.getInstance().activityExisted(EmptyActivity.class)) {
            DemoApplication.getInstance().finishActivity(EmptyActivity.class);
        }
        if (mDates != null && mDates.size() > 0)
            adapter.notifyDataSetChanged();
    }

    private int getPlayPosition() {
        if (playPosition >= 0) {
            playPosition = playPosition % (mContentList.size());
        } else {
            playPosition = (mContentList.size() - Math.abs(playPosition) % mContentList.size()) % mContentList.size();
        }
        return playPosition;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImgTxtAlbumActivity.INTO_COMMENT_DETAIL:
                    int position = data.getIntExtra("position", -1);
                    if (position > -1) {
                        if (data.getBooleanExtra("isClick", false)) {
                            //在评论详情页面进行了点赞操作
                            mDates.get(position).getFather().setUps(mDates.get(position).getFather().getUps() + 1);
                        }
                        if (data.getBooleanExtra("isComment", false)) {
                            //在评论详情页面进行了评论操作
                            mDates.get(position).getFather().setCommentnum(mDates.get(position).getFather().getCommentnum() + 1);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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
