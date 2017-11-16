package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.AudioAlbumAdapter;
import com.yidiankeyan.science.information.adapter.ColumnCommentAdapter;
import com.yidiankeyan.science.information.entity.ColumnArticleDetailsBean;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.subscribe.entity.InterviewCommentBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.xutils.db.annotation.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 专栏
 * -文章详情
 */
public class ColArticleDetailActivity extends BaseActivity {

    private WebView webArticleDetail;
    private ColumnArticleDetailsBean detailsBean;
    private ImageView imgArticleDetail;
    private TextView tvTitleName;
    private TextView tvAuthor;
    private TextView tvTime;
    private ImageView imgMediaState;
    private TextView tvAudioName;
    private SeekBar seekBar;
    private TextView tvCurrTime;
    private TextView tvTotalTime;
    private TextView tvAudioSize;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private AutoFrameLayout flAudio;
    private AutoLinearLayout imgReturn;
    private AutoRelativeLayout rlAudioPlayer;

    //评论
    //最新
    private ArrayList<ContentCommentBean> mNewestDates = new ArrayList<>();
    //最热
    private ArrayList<ContentCommentBean> mHottestDates = new ArrayList<>();
    //合并集合
    private ArrayList<InterviewCommentBean> mDatas = new ArrayList<>();
    private ColumnCommentAdapter commentHotAdapter;
    private AutoLinearLayout llHeadClick;
    private AutoLinearLayout llHeadComment;
    private TextView btnComment;
    private Button btnSubmitComment;
    private EditText etPopupwindowComm;
    private PopupWindow commPopupWindow;
    private View commView;
    private List<String> commentClickList;
    private ShowAllListView lvInterview;
    private int isPup = -1;

    public static final int ITEM = 0;
    public static final int SECTION = 1;
    private boolean addPadding;
    public int isClick = 0;


    private AutoRelativeLayout llColArticleDetail;
    private AutoRelativeLayout mrlShare;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private String mShareSaleId;
    private String mBuy;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * 随着播放进度更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (rlAudioPlayer == null || rlAudioPlayer.getVisibility() != View.VISIBLE
                    || detailsBean == null
                    || detailsBean.getColumnAudioModle() == null || detailsBean.getColumnAudioModle().equals("null")
                    || detailsBean.getColumnAudioModle().getAudioUrl() == null
                    || !TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
                return;
            if (msg.what == 0) {
                try {
                    seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                    tvCurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                    seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                    tvTotalTime.setText("/" + TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 000
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (fromUser) {
//                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
//                    if (progress < AudioPlayManager.getInstance().getIntDuration())
//                        AudioPlayManager.getInstance().mediaPlayer.seekTo(progress);
//                }
//            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//            AudioPlayManager.getInstance().pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING) {
                AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(seekBar.getProgress());
            } else if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
                AudioPlayManager.getInstance().resume();
                DemoApplication.isPlay = true;
                AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(seekBar.getProgress());
            } else if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP) {
                List<ColumnArticleDetailsBean.ColumnAudioModleBean> list = new ArrayList<>();
                list.add(detailsBean.getColumnAudioModle());
                AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
                AudioPlayManager.getInstance().ijkStart();
                DemoApplication.isPlay = true;
            }
        }
    };


    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_col_article_detail;
    }

    @Override
    protected void initView() {
        mBuy = getIntent().getStringExtra("mBuy");
        webArticleDetail = (WebView) findViewById(R.id.web_article_detail);
        imgArticleDetail = (ImageView) findViewById(R.id.img_article_detail);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvTime = (TextView) findViewById(R.id.tv_time);
        imgMediaState = (ImageView) findViewById(R.id.img_media_state);
        tvAudioName = (TextView) findViewById(R.id.tv_audio_name);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvCurrTime = (TextView) findViewById(R.id.tv_curr_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvAudioSize = (TextView) findViewById(R.id.tv_audio_size);
        flAudio = (AutoFrameLayout) findViewById(R.id.fl_audio);
        imgReturn = (AutoLinearLayout) findViewById(R.id.img_return);
        llColArticleDetail = (AutoRelativeLayout) findViewById(R.id.ll_col_article_detail);
        btnComment = (TextView) findViewById(R.id.btn_comment);
        lvInterview = (ShowAllListView) findViewById(R.id.lv_interview_datails);
        commView = View.inflate(this, R.layout.popupwindow_comm, null);
        etPopupwindowComm = (EditText) commView.findViewById(R.id.et_popupwindow_comm);
        btnSubmitComment = (Button) commView.findViewById(R.id.btn_submit_comment);
        rlAudioPlayer = (AutoRelativeLayout) findViewById(R.id.rl_audio_player);//AutoRelativeLayout
        mrlShare = (AutoRelativeLayout) findViewById(R.id.rl_share);
        mrlShare.setOnClickListener(this);
    }

    @Override
    protected void initAction() {
        commentClickList = new ArrayList<String>();
        initDatas();
        if (TextUtils.isEmpty(SpUtils.getStringSp(this, "userId"))) {
            webArticleDetail.loadUrl(SystemConstant.MYURL + "column4/article/" + getIntent().getStringExtra("id"));
        } else {
            webArticleDetail.loadUrl(SystemConstant.MYURL + "column4/article/" + getIntent().getStringExtra("id") + "/" + SpUtils.getStringSp(this, "userId"));
        }
        WebSettings ws = webArticleDetail.getSettings();
        ws.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        webArticleDetail.getSettings().setJavaScriptEnabled(true);
        webArticleDetail.setWebChromeClient(new WebChromeClient());
        webArticleDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webArticleDetail.getSettings().setLoadWithOverviewMode(true);

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
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 100);

        commentHotAdapter = new ColumnCommentAdapter(mDatas, this);
        lvInterview.setAdapter(commentHotAdapter);
        setCommDeleteListener();
        toHttpGetHottestComment();
        initializePadding();
        flAudio.setOnClickListener(this);
        imgReturn.setOnClickListener(this);
        btnComment.setOnClickListener(this);
    }


    //获取详情内容
    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        map.put("release", "1");
        ApiServerManager.getInstance().getApiServer().getArticleDetails(map).enqueue(new RetrofitCallBack<ColumnArticleDetailsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ColumnArticleDetailsBean>> call, Response<RetrofitResult<ColumnArticleDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    Glide.with(ColArticleDetailActivity.this).load(Util.getImgUrl(detailsBean.getArticleImg()))
                            .error(R.drawable.icon_hotload_failed)
                            .placeholder(R.drawable.icon_hotload_failed)
                            .into(imgArticleDetail);
                    tvTitleName.setText(detailsBean.getArticleName());
                    tvAuthor.setText(detailsBean.getWriter());
                    tvTime.setText(detailsBean.getCreateTime());
                    if (detailsBean.getColumnAudioModle() == null || detailsBean.getColumnAudioModle().equals("null") || detailsBean.getColumnAudioModle().getAudioUrl() == null) {
                        rlAudioPlayer.setVisibility(View.GONE);
                    } else {
                        rlAudioPlayer.setVisibility(View.VISIBLE);
                        if (TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId())) {
                            seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                            tvCurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                            seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                            tvTotalTime.setText("/" + TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                            if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING) {
                                imgMediaState.setImageResource(R.drawable.icon_stop_column);
                            }
                        }
                    }
                    tvAudioName.setText(detailsBean.getColumnAudioModle().getAudioName());
                    tvAudioSize.setText(detailsBean.getColumnAudioModle().getAudioAh() + "M");

                    initAudio();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ColumnArticleDetailsBean>> call, Throwable t) {

            }
        });
    }


    /**
     * 初始化音频，如果已下载播放本地，未下载播放网络音频
     */
    private void initAudio() {
        if (TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
            return;
        if (TextUtils.equals("1", detailsBean.getFree()) || detailsBean.getHaveYouPurchased().equals("1")) {
            List<ColumnArticleDetailsBean.ColumnAudioModleBean> list = new ArrayList<>();
            list.add(detailsBean.getColumnAudioModle());
            AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
//            AudioPlayManager.getInstance().start();
        } else {
            return;
        }

    }

    /**
     * 获取热门评论列表
     */
    private void toHttpGetHottestComment() {

        mDatas.removeAll(mDatas);
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 5);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        entity.put("sons", 3);
        entity.put("type", 6);
        map.put("entity", entity);

        ApiServerManager.getInstance().getApiServer().getCommentHotList(map).enqueue(new RetrofitCallBack<ArrayList<ContentCommentBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Response<RetrofitResult<ArrayList<ContentCommentBean>>> response) {
                if (response.body().getCode() == 200) {
                    mHottestDates.removeAll(mHottestDates);
                    mHottestDates.addAll(response.body().getData());
                    if (!mDatas.contains(new InterviewCommentBean(SECTION, "热门评论"))) {
                        mDatas.add(new InterviewCommentBean(SECTION, "热门评论"));
                    }
                    for (int i = 0; i < mHottestDates.size(); i++) {
                        mDatas.add(new InterviewCommentBean(ITEM, mHottestDates.get(i)));
                    }
                    if(response.body().getData().size()<0){

                    }
                }
                toHttpGetNewestComment();
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Throwable t) {
                toHttpGetNewestComment();
            }
        });
    }

    /**
     * 获取最新评论列表
     */
    private void toHttpGetNewestComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        entity.put("sons", 3);
        entity.put("type", 6);
        map.put("entity", entity);

        ApiServerManager.getInstance().getApiServer().getCommentNewestList(map).enqueue(new RetrofitCallBack<ArrayList<ContentCommentBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Response<RetrofitResult<ArrayList<ContentCommentBean>>> response) {
                if (response.body().getCode() == 200) {
                    mNewestDates.removeAll(mNewestDates);
                    mNewestDates.addAll(response.body().getData());
                    if (!mDatas.contains(new InterviewCommentBean(SECTION, "最新评论"))) {
                        mDatas.add(new InterviewCommentBean(SECTION, "最新评论"));
                    }
                    for (int i = 0; i < mNewestDates.size(); i++) {
                        mDatas.add(new InterviewCommentBean(ITEM, mNewestDates.get(i)));
                    }
                    commentHotAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MozForumDetailsActivity.INTO_COMMENT_DETAIL:
                    //从评论详情页面返回
                    int position = data.getIntExtra("position", -1);
                    if (position > -1) {
                        if (data.getBooleanExtra("isClick", false)) {
                            //在评论详情页面进行了点赞操作
                            mDatas.get(position).getContentCommentBean().getFather().setUps(mDatas.get(position).getContentCommentBean().getFather().getUps() + 1);
                        }
                        if (data.getBooleanExtra("isComment", false)) {
                            //在评论详情页面进行了评论操作
                            mDatas.get(position).getContentCommentBean().getFather().setCommentnum(mDatas.get(position).getContentCommentBean().getFather().getCommentnum() + 1);
                        }
                    }
                    break;
            }
        }
    }


    private void showCommPop() {
        if (commPopupWindow == null) {
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

                    if (!Util.hintLogin(ColArticleDetailActivity.this)) return;
                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    Util.finishPop(ColArticleDetailActivity.this, commPopupWindow);
                    if (isPup < 0) {
                        //一级评论
                        toHttpSendComment();
                    } else {
                        //二级评论
                        toHttpSendComments();
                    }
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
                    Util.finishPop(ColArticleDetailActivity.this, commPopupWindow);
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
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        commPopupWindow.showAtLocation(llColArticleDetail, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("content", etPopupwindowComm.getText().toString());
        map.put("type", 6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    mDatas.remove(mDatas);
//                    if (soonBeen != null) {
//                        toHttpGetHottestComment();
//                    }
                    toHttpGetHottestComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 提交子评论
     */
    private void toHttpSendComments() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", mDatas.get(isPup).getContentCommentBean().getFather().getCommentid());
        map.put("content", etPopupwindowComm.getText().toString());
        map.put("type", 2);
        map.put("isreply", 1);
        map.put("replyid", mDatas.get(isPup).getContentCommentBean().getFather().getUserid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    isPup = -1;
                    mDatas.remove(mDatas);
//                    if (soonBeen != null) {
//                        toHttpGetHottestComment();
//                    }
                    if (isClick > 0) {
                        for (String id : commentClickList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("uptype", "COMMENT");
                            map.put("targetid", id);
                            HttpUtil.post(ColArticleDetailActivity.this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                                @Override
                                public void successResult(ResultEntity result) throws JSONException {
                                    if (result.getCode() == 200) {
                                        isClick = 2;
                                    }
                                }

                                @Override
                                public void errorResult(Throwable ex, boolean isOnCallback) {

                                }
                            });

                        }

                    }
                    toHttpGetHottestComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    private void initializePadding() {
        float density = getResources().getDisplayMetrics().density;
        int padding = addPadding ? (int) (16 * density) : 0;
        lvInterview.setPadding(padding, padding, padding, padding);
    }


    /**
     * 设置删除回调及点击后续操作
     */
    private void setCommDeleteListener() {
        commentHotAdapter.setOnDeleteClickListener(new ColumnCommentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mDatas.get(position).getContentCommentBean().getFather().getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    mDatas.remove(position);
                                    toHttpGetHottestComment();
                                }
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
            }
        });
        commentHotAdapter.setOnSonDeleteClickListener(new ColumnCommentAdapter.OnSonDeleteClickListener() {
            @Override
            public void onDeleteClick(final int parentPosition, final int sonPosition) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mDatas.get(parentPosition).getContentCommentBean().getSons().get(sonPosition).getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    mDatas.get(parentPosition).getContentCommentBean().getSons().remove(sonPosition);
                                    mDatas.get(parentPosition).getContentCommentBean().getFather().setCommentnum(mDatas.get(parentPosition).getContentCommentBean().getFather().getCommentnum() - 1);
                                    toHttpGetHottestComment();
                                }
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
            }
        });
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_share, null);
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
            sharePopupWindow.showAtLocation(llColArticleDetail, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ColArticleDetailActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColArticleDetailActivity.this, sharePopupWindow);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(MozInterviewDetailsActivity.this, "data:image/jpeg;base64,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");
//                    String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(MozInterviewDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                            .withText("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if("1".equals(mBuy)){
                        if (detailsBean != null)
                            ColArticleDetailActivity.this.shareWeb(
                                    SHARE_MEDIA.WEIXIN,
                                    detailsBean.getArticleName(),
                                    "墨子专栏",
                                    SystemConstant.ACCESS_IMG_HOST + detailsBean.getArticleImg(),
                                    SystemConstant.URL + "size/shareread/shareRecord?columnArtileid="+
                                                detailsBean.getId()+"&shareUserid="+SpUtils.getStringSp(mContext,"userId"),
                                    ""
                            );
                        Log.i("detailsBean.getId()",SystemConstant.URL + "size/shareread/shareRecord/"+
                                detailsBean.getId()+"/"+SpUtils.getStringSp(mContext,"userId"));
                    }else ToastMaker.showShortToast("请先购买");
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("1".equals(mBuy)){
                        if (detailsBean != null)
                            ColArticleDetailActivity.this.shareWeb(
                                    SHARE_MEDIA.WEIXIN_CIRCLE,
                                    detailsBean.getArticleName(),
                                    "墨子专栏",
                                    SystemConstant.ACCESS_IMG_HOST + detailsBean.getArticleImg(),
                                    SystemConstant.URL + "size/shareread/shareRecord/" +
                                            detailsBean.getId()+"/"+SpUtils.getStringSp(mContext,"userId"),
                                    ""
                            );
                    }else ToastMaker.showShortToast("请先购买");
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llColArticleDetail, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_share:  //分享
                showSharePop();
                break;
            case R.id.img_return:
                finish();
                break;
            case R.id.fl_audio:
                if (TextUtils.equals("1", detailsBean.getFree()) || detailsBean.getHaveYouPurchased().equals("1")) {
                    if (AudioPlayManager.getInstance().isPlaying()) {
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        imgMediaState.setImageResource(R.drawable.icon_plays_column);
                    } else {
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        imgMediaState.setImageResource(R.drawable.icon_stop_column);
                    }
                    DemoApplication.isBuy = false;
                    break;
                } else {
                    ToastMaker.showShortToast("请先购买");
                    break;
                }
            case R.id.btn_comment:
                etPopupwindowComm.setHint("优质评论将会被优先展示");
                Util.openKeybord(etPopupwindowComm, this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showCommPop();
                    }
                }, 50);
                break;
        }
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_PREPARE:
                //正在准备
                break;
            case SystemConstant.ON_PLAYING:
                if (rlAudioPlayer.getVisibility() != View.VISIBLE || !TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
                    return;
                Log.e("state", "ON_PLAYING");
                //开始播放
                imgMediaState.setImageResource(R.drawable.icon_stop_column);
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
//                tvTotalTime.setText(TimeUtils.getAnswerLength(AudioPlayManager.getInstance().getIntDuration()));
                break;
            case SystemConstant.ON_BUFFERING:
                //缓冲区变化
                seekBar.setMax(msg.getArg1());
                break;
            case SystemConstant.ON_STOP:
                if (rlAudioPlayer.getVisibility() != View.VISIBLE || !TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
                    return;
                Log.e("state", "ON_STOP");
                //停止播放
                imgMediaState.setImageResource(R.drawable.icon_plays_column);
                break;
            case SystemConstant.ON_PAUSE:
                if (rlAudioPlayer.getVisibility() != View.VISIBLE || !TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
                    return;
                Log.e("state", "ON_PAUSE");
                //暂停播放
                imgMediaState.setImageResource(R.drawable.icon_plays_column);
                break;
            case SystemConstant.AUDIO_COMPLET:
                if (rlAudioPlayer.getVisibility() != View.VISIBLE || !TextUtils.equals(detailsBean.getColumnAudioModle().getId(), AudioPlayManager.getInstance().getCurrId()))
                    return;
                AudioPlayManager.getInstance().replay();
                seekBar.setProgress(0);
                tvCurrTime.setText("00:00");
                imgMediaState.setImageResource(R.drawable.icon_plays_column);
                break;
            case SystemConstant.ON_CLICK_CHANGE:
                isClick = 1;
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
            case SystemConstant.ON_INTERVIEW_COMMENT:
                isPup = msg.getArg1();
                etPopupwindowComm.setHint("优质评论将会被优先展示");
                if (isPup > 0) {
                    if (!TextUtils.isEmpty(mDatas.get(isPup).getContentCommentBean().getFather().getUsername()) && !TextUtils.equals("null", mDatas.get(isPup).getContentCommentBean().getFather().getUsername())) {
                        etPopupwindowComm.setHint("回复@" + mDatas.get(isPup).getContentCommentBean().getFather().getUsername());
                    } else {
                        etPopupwindowComm.setHint("回复@Ta");
                    }
                }

                showCommPop();
                break;
            case SystemConstant.ON_INTERVIEW_COMMENT_LIST:
                Intent intent = new Intent(this, InterviewCommentListActivity.class);
                intent.putExtra("id", mDatas.get(msg.getArg1()).getContentCommentBean().getFather().getCommentid());
                intent.putExtra("userId", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getUserid());
                intent.putExtra("userImg", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getImgurl());
                intent.putExtra("userName", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getUsername());
                intent.putExtra("comNum", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getCommentnum());
                intent.putExtra("clickNum", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getUps());
                intent.putExtra("content", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getContent());
                intent.putExtra("data", mDatas.get(msg.getArg1()).contentCommentBean.getFather().getCreatetime());
                intent.putExtra("position", msg.getArg1());
                startActivityForResult(intent, MozForumDetailsActivity.INTO_COMMENT_DETAIL);
                break;
            case SystemConstant.ON_INTERVIEW_COMMENT_LISTS:
//                if (soonBeen != null) {
//                    toHttpGetHottestComment();
//                }
                toHttpGetHottestComment();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                //如果mainActivity不存在则跳转主页面
                AudioPlayManager.getInstance().release();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
//        if (null != commentClickList && commentClickList.size() > 0) {
        if (isClick < 2) {
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
        }
    }
}
