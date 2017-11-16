package com.yidiankeyan.science.information.acitivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.information.adapter.MyAnswerAdapter;
import com.yidiankeyan.science.information.adapter.MyDragAdapter;
import com.yidiankeyan.science.information.adapter.MyOtherAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.information.entity.EavedropMediaBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.my.activity.AnswerAuthenticationActivity;
import com.yidiankeyan.science.my.activity.MyFansActivity;
import com.yidiankeyan.science.my.activity.MyFollowActivity;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.FileUtils;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.yidiankeyan.science.view.PayPopupWindow;
import com.yidiankeyan.science.view.drag.adapter.MyDragGrid;
import com.yidiankeyan.science.view.drag.bean.ChannelItem;
import com.yidiankeyan.science.view.drag.view.OtherGridView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的回答
 */
public class MyAnswerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final int INTO_CREATE_KEDA_ACTIVITY = 101;
    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private ImageButton titleBtn;
    private ListViewFinalLoadMore lvMyAnswer;
    private ImageView imgAvatar;
    private TextView tvJob;
    //    private TextView tvChange;
    private TextView tvAnswer;
    private TextView tvAnswered;
    private TextView tvDuration;
    private View headView;
    private MyAnswerAdapter adapter;
    private ImageView imgEdit;
    private TextView tvFansNumber;
    private TextView tvFocusNumber;
    private AutoLinearLayout llFansClick, llFocusClick;

    /**
     * 用户栏目的GRIDVIEW
     */
    private MyDragGrid userGridView;
    /**
     * 其它栏目的GRIDVIEW
     */
    private OtherGridView otherGridView;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    MyDragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    MyOtherAdapter otherAdapter;
    /**
     * 未选择的方向列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /**
     * 已选择的方向列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    /**
     * 0:+;1:完成;2:修改方向
     */
    private int state = 0;
    /**
     * 已回答的页码
     */
    private int answerPages = 1;
    /**
     * 未回答的页码
     */
    private int answerWaitPages = 1;
    private LinearLayout llCreateVoice;
    private TextView tvCreateVoice;
    private ImageView imgCreateVoice;
    //    private ImageView tvPlayVoice;
//    private LinearLayout llEditVoice;
    private TextView tvEditVoice;
    //    private ImageView imgEditVoice;
    private boolean isRecording;
    private MediaRecorder recorder;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private RelativeLayout rlContainer;
    private ImageView micImage;
    protected Drawable[] micImages;
    private TextView etPrice;
    private AnswerPeopleDetail answerPeopleDetail;
    private RelativeLayout rlTopContainer;
    private ImageView micTopImage;
    /**
     * 展开用户未选中的方向
     */
    private AutoLinearLayout llExpand;

    /**
     * 问答专辑列表
     */
    private List<AnswerAlbumBean> listHelp = new ArrayList<>();
    private TextView tvTotalMoney;
    private TextView tvName;

    /**
     * 0:没有录音，1:有录音，未修改，2:有录音，修改了
     */
    private int recordState;
    private Button btnSave;

    /**
     * 判断是否退出时更新方向
     */
    private List<String> oldDomanList;
    /**
     * 判断退出时是否更新润口费
     */
    private double oldMouthprice;

    private String questionId;
    private long startTime;
    private long endTime;
    private PayPopupWindow payPopupWindow;
    private PopupWindow recordPopupWindow;
    private AutoRelativeLayout rlAll;
    private NewScienceHelp keda;
    private boolean isAnswering;
    private boolean isPressed;
    private ImageView imgExpand;
    private AutoRelativeLayout rlAnswerTop;
    private ProgressBar pbDuration;
    private InputMethodManager imm;
    private AutoLinearLayout llSpeak;
    private TextView tvSaveRecord;
    private boolean tempRecordVaild;
    private long animationStartTime;
    private AutoLinearLayout llPlayContainer;
    private View llPlay;
    private AutoLinearLayout llSpeakContainer;
    private ImageView imgMessage;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    micImage.setImageDrawable(micImages[msg.arg1]);
                    if (rlTopContainer.getVisibility() == View.VISIBLE) {
                        micTopImage.setImageDrawable(micImages[msg.arg1]);
                        long duration = System.currentTimeMillis() - startTime;
                        int second = (int) (duration / 1000);
                        int mill = (int) ((duration % 1000) / 10);
                        if (second < 10)
                            tvDuration.setText("0" + second + ":" + (mill < 10 ? "0" + mill : mill));
                        else
                            tvDuration.setText(second + ":" + (mill < 10 ? "0" + mill : mill));
                        if (second >= 60) {
                            pbDuration.setProgress(0);
                            mHandler.removeMessages(3);
                            rlTopContainer.setVisibility(View.GONE);
                            if (!isAnswering) {
                                isPressed = false;
                                return;
                            }
                            isAnswering = false;
                            endTime = System.currentTimeMillis();
                            tempRecordVaild = true;
                            try {
                                recorder.setOnErrorListener(null);
                                recorder.setPreviewDisplay(null);
                                recorder.stop();// 停止刻录
//                        recorder.reset();
                            } catch (Exception e) {

                            }
//                    recorder.reset(); // 重新启动MediaRecorder.
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                            if (endTime - startTime < 1000)
                                Toast.makeText(mContext, "留言时间时间必须大于1秒", Toast.LENGTH_SHORT).show();
                            else
                                llPlayContainer.setVisibility(View.VISIBLE);
                        } else {
                            pbDuration.setMax(60);
                            pbDuration.setProgress(second);
                        }
                    }
                    break;
                case 2:
                    if (!isPressed)
                        return;
                    AudioPlayManager.getInstance().release();
                    questionId = (String) msg.obj;
                    startTime = System.currentTimeMillis();
                    Acp.getInstance(mContext).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.RECORD_AUDIO)
                            .build(), new AcpListener() {
                        @Override
                        public void onGranted() {
                            if (!isPressed)
                                return;
//                            imgCreateVoice.setImageResource(R.drawable.record_pressed);
                            tvCreateVoice.setText("正在录制");
                            rlContainer.setVisibility(View.VISIBLE);
                            recordState = 1;
                            recordStart(Util.getSDCardPath() + "/answer.aac");
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            Toast.makeText(mContext, "无法录音，请开启权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 3:
                    if (!isPressed)
                        return;
                    AudioPlayManager.getInstance().release();
//                    tvPlayVoice.setEnabled(false);
                    startTime = System.currentTimeMillis();
//                    imgEditVoice.setImageResource(R.drawable.record_pressed);
//                    tvEditVoice.setText("正在录制");
                    isRecording = true;
                    Acp.getInstance(mContext).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.RECORD_AUDIO)
                            .build(), new AcpListener() {
                        @Override
                        public void onGranted() {
                            if (!isPressed)
                                return;
                            rlTopContainer.setVisibility(View.VISIBLE);
                            recordStart(Util.getSDCardPath() + "/temp_record.aac");
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            Toast.makeText(mContext, "录制失败", Toast.LENGTH_SHORT).show();
//                            tvPlayVoice.setEnabled(true);
                            recordState = 1;
                        }
                    });
                    break;
                case 4:
                    if (AudioPlayManager.getInstance().getIntDuration() != 0) {
                        pbDuration.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                        LogUtils.e("duration=" + AudioPlayManager.getInstance().getIntDuration() + ",current=" + AudioPlayManager.getInstance().getCurrentPosition());
                        if (AudioPlayManager.getInstance().getCurrentPosition() < AudioPlayManager.getInstance().getIntDuration())
                            pbDuration.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                        else
                            pbDuration.setProgress((int) AudioPlayManager.getInstance().getIntDuration());
                    }
                    break;
            }
        }
    };

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_answer;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        lvMyAnswer = (ListViewFinalLoadMore) findViewById(R.id.lv_my_answer);
        rlTopContainer = (RelativeLayout) findViewById(R.id.rl_top_container);
        micTopImage = (ImageView) findViewById(R.id.mic_top_image);
        headView = LayoutInflater.from(this).inflate(R.layout.head_my_answer, null, false);
        etPrice = (TextView) headView.findViewById(R.id.et_price);
        imgAvatar = (ImageView) headView.findViewById(R.id.img_avatar);
        imgEdit = (ImageView) headView.findViewById(R.id.img_edit);
        imgExpand = (ImageView) headView.findViewById(R.id.img_expand);
        tvFansNumber = (TextView) headView.findViewById(R.id.tv_fans_number);
        tvFocusNumber = (TextView) headView.findViewById(R.id.tv_focus_number);
        tvJob = (TextView) headView.findViewById(R.id.tv_job);
//        tvChange = (TextView) headView.findViewById(R.id.tv_change);
        tvAnswer = (TextView) headView.findViewById(R.id.tv_answer);
        tvAnswered = (TextView) headView.findViewById(R.id.tv_answered);
        btnSave = (Button) headView.findViewById(R.id.btn_save);
        llExpand = (AutoLinearLayout) headView.findViewById(R.id.ll_expand);
        userGridView = (MyDragGrid) headView.findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) headView.findViewById(R.id.otherGridView);
        imgMessage = (ImageView) headView.findViewById(R.id.img_message);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        rlAll = (AutoRelativeLayout) findViewById(R.id.rl_all);

        rlAnswerTop = (AutoRelativeLayout) headView.findViewById(R.id.rl_answer_top);
        llCreateVoice = (LinearLayout) headView.findViewById(R.id.ll_create_voice);
        tvCreateVoice = (TextView) headView.findViewById(R.id.tv_create_voice);
        imgCreateVoice = (ImageView) headView.findViewById(R.id.img_create_voice);
//        tvPlayVoice = (ImageView) headView.findViewById(R.id.tv_play_voice);
//        llEditVoice = (LinearLayout) headView.findViewById(R.id.ll_edit_voice);
//        tvEditVoice = (TextView) headView.findViewById(R.id.tv_edit_voice);
//        imgEditVoice = (ImageView) headView.findViewById(R.id.img_edit_voice);
        llFansClick = (AutoLinearLayout) headView.findViewById(R.id.ll_fans_click);
        llFocusClick = (AutoLinearLayout) headView.findViewById(R.id.ll_focus_click);
        tvTotalMoney = (TextView) headView.findViewById(R.id.tv_total_money);
        tvName = (TextView) headView.findViewById(R.id.tv_name);
        payPopupWindow = new PayPopupWindow(this, View.inflate(this, R.layout.popupwindow_pay, null));
    }

    /**
     * 获取追问数据
     */
    private void toHttpGetAnswer() {
        payPopupWindow.setContainer(rlAll);
        Map<String, Object> map = new HashMap<>();
        map.put("pages", answerPages);
        map.put("pagesize", 20);
//        Map<String, Object> entity = new HashMap<>();
//        entity.put("solverid", SpUtils.getStringSp(this, "userId"));
//        map.put("entity", entity);
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_CONDITIONS, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    List<NewScienceHelp> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NewScienceHelp.class);
//                    if (answerPages == 1)
//                        adapter.getAnsweredDatas().removeAll(listHelp);
//                    if (data.size() > 0) {
//                        lvMyAnswer.setHasLoadMore(true);
//                        adapter.getAnsweredDatas().addAll(data);
//                        answerPages++;
//                    } else {
//                        lvMyAnswer.setHasLoadMore(false);
//                    }
////                    adapter.getAnsweredDatas().removeAll(adapter.getAnsweredDatas());
////                    adapter.getAnsweredDatas().addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ScienceHelp.class));
//                    if (adapter.isFlag())
//                        adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                lvMyAnswer.setHasLoadMore(false);
//            }
//        });
        ApiServerManager.getInstance().getApiServer().getReplyQuestion(map).enqueue(new RetrofitCallBack<List<NewScienceHelp>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<NewScienceHelp>>> call, Response<RetrofitResult<List<NewScienceHelp>>> response) {
                if (response.body().getCode() == 200) {
                    List<NewScienceHelp> data = response.body().getData();
                    if (answerPages == 1)
                        adapter.getAnsweredDatas().removeAll(listHelp);
                    if (data.size() > 0) {
                        lvMyAnswer.setHasLoadMore(true);
                        adapter.getAnsweredDatas().addAll(data);
                        answerPages++;
                    } else {
                        lvMyAnswer.setHasLoadMore(false);
                    }
//                    adapter.getAnsweredDatas().removeAll(adapter.getAnsweredDatas());
//                    adapter.getAnsweredDatas().addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ScienceHelp.class));
                    if (adapter.isFlag())
                        adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<NewScienceHelp>>> call, Throwable t) {
                lvMyAnswer.setHasLoadMore(false);
            }
        });
    }

    /**
     * 获取问答专辑列表
     */
    private void toHttpGetAnswerAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALL_ANSWER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<AnswerAlbumBean> mData = GsonUtils.json2List(jsonData, AnswerAlbumBean.class);
                    listHelp.addAll(mData);
                    initDomans();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 获取问题列表
     */
    private void toHttpGetWaitAnswer() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", answerWaitPages);
        map.put("pagesize", 20);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_WAIT_ANSWER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<NewScienceHelp> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NewScienceHelp.class);
                    if (answerWaitPages == 1)
                        adapter.getWaitAnswerDatas().removeAll(listHelp);
                    if (data.size() > 0) {
                        lvMyAnswer.setHasLoadMore(true);
                        adapter.getWaitAnswerDatas().addAll(data);
                        answerWaitPages++;
                    } else {
                        lvMyAnswer.setHasLoadMore(false);
                    }
//                    adapter.getWaitAnswerDatas().removeAll(adapter.getWaitAnswerDatas());
//                    adapter.getWaitAnswerDatas().addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ScienceHelp.class));
                    if (!adapter.isFlag())
                        adapter.notifyDataSetChanged();
                } else {
                    lvMyAnswer.showFailUI();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvMyAnswer.showFailUI();
            }
        });
    }

    @Override
    protected void initAction() {
        lvMyAnswer.setHasLoadMore(true);
        lvMyAnswer.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (adapter.isFlag()) {
                    toHttpGetAnswer();
                } else {
                    toHttpGetWaitAnswer();
                }
            }
        });
        answerPeopleDetail = getIntent().getParcelableExtra("bean");
        initData();
        if (answerPeopleDetail != null) {
            toHttpGetWaitAnswer();
            toHttpGetAnswer();
//            tvChange.setText("修改方向");
//            tvChange.setBackgroundResource(R.drawable.shape_answer_top_send_enable);
//            otherGridView.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            //表示未选中的方向隐藏了
//            llExpand.setTag(0);
            state = 2;
            oldDomanList = !TextUtils.isEmpty(answerPeopleDetail.getDomain()) && !TextUtils.equals("null", answerPeopleDetail.getDomain()) ? Arrays.asList(answerPeopleDetail.getDomain().split(",")) : new ArrayList<String>();
            oldMouthprice = answerPeopleDetail.getMouthprice();
        } else {
            //表示未选中的方向展开了
//            llExpand.setTag(1);
            imgMessage.setVisibility(View.GONE);
        }
        toHttpGetAnswerAlbum();
        titleBtn.setVisibility(View.GONE);
        maintitleTxt.setText("我的科答");
        llReturn.setOnClickListener(this);
//        llExpand.setOnClickListener(this);
        tvAnswer.setOnClickListener(this);
        tvAnswered.setOnClickListener(this);
        imgMessage.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        lvMyAnswer.addHeaderView(headView);
        adapter = new MyAnswerAdapter(this);
//        tvChange.setOnClickListener(this);
        lvMyAnswer.setAdapter(adapter);
        imgCreateVoice.setOnClickListener(this);
//        tvPlayVoice.setOnClickListener(this);
//        imgEditVoice.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        rlAnswerTop.setOnClickListener(this);
        llFansClick.setOnClickListener(this);
        llFocusClick.setOnClickListener(this);
//        imgAvatar.setOnClickListener(this);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if ((isRecording || isAnswering) && recorder != null && (rlContainer.getVisibility() == View.VISIBLE || rlTopContainer.getVisibility() == View.VISIBLE)) {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                    mHandler.sendMessage(message);
                } else if (llPlayContainer != null && recordPopupWindow.isShowing() && llPlayContainer.getVisibility() == View.VISIBLE) {
                    mHandler.sendEmptyMessage(4);
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 10);
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14),};
//        etPrice.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // 限定输入的出价只显示小数点后两位
////                if (s.toString().contains(".")) {
////                    if (s.length() - 1 - s.toString().indexOf(".") > 1) {
////                        s = s.toString().subSequence(0,
////                                s.toString().indexOf(".") + 2);
////                        etPrice.setText(s);
////                        etPrice.setSelection(s.length());
////                    }
////                }
////                if (s.toString().trim().substring(0).equals(".")) {
////                    etPrice.setText("");
////                    etPrice.setSelection(0);
////                    return;
////                }
////
////                if (s.toString().startsWith("0")) {
////                    etPrice.setText("");
////                    etPrice.setSelection(0);
////                    return;
////                }
////                if (s.length() > 0) {
////                    double price = Double.parseDouble(s.toString());
////                    if (price > 1000) {
////                        s = "1000.0";
////                        etPrice.setText(s);
////                        etPrice.setSelection(s.length());
////                    }
////                }
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
//                        etPrice.setText(s);
//                        etPrice.setSelection(s.length());
//                    }
//                }
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    etPrice.setText(s);
//                    etPrice.setSelection(2);
//                }
//
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        etPrice.setText(s.subSequence(0, 1));
//                        etPrice.setSelection(1);
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        adapter.setOnRecordTouchListener(new MyAnswerAdapter.OnRecordTouchListener() {
            @Override
            public void onTouch(final View v, MotionEvent event, String id) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isPressed = true;
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = id;
                    mHandler.sendMessageDelayed(message, 1000);
//                    mHandler.sendEmptyMessageDelayed(2, 1000);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mHandler.removeMessages(2);
                    if (!isAnswering) {
                        isPressed = false;
                        return;
                    }
                    isAnswering = false;
                    endTime = System.currentTimeMillis();
                    rlContainer.setVisibility(View.GONE);
                    try {
                        recorder.setOnErrorListener(null);
                        recorder.setPreviewDisplay(null);
                        recorder.stop();// 停止刻录
//                        recorder.reset();
                    } catch (Exception e) {

                    }
//                    recorder.reset(); // 重新启动MediaRecorder.
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                    if (endTime - startTime <= 5 * 60 * 1000)
                        showWaringDialog("提示", "是否上传这段录音？", new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                toHttpAnswer();
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    else
                        Toast.makeText(mContext, "回答时间必须小于5分钟", Toast.LENGTH_SHORT).show();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mHandler.removeMessages(2);
                    if (!isAnswering) {
                        isPressed = false;
                        return;
                    }
                    isAnswering = false;
                    endTime = System.currentTimeMillis();
                    rlContainer.setVisibility(View.GONE);
                    try {
                        recorder.setOnErrorListener(null);
                        recorder.setPreviewDisplay(null);
                        recorder.stop();// 停止刻录0
//                        recorder.reset(); // 重新启动MediaRecorder.
                    } catch (Exception e) {

                    }
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                    if (endTime - startTime > 1000)
                        Toast.makeText(mContext, "取消回答", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapter.setOnEavesdropClick(new MyAnswerAdapter.OnEavesdropClick() {
            @Override
            public void onClick(NewScienceHelp keda) {
                toHttpEavedrop(keda);
            }
        });
        payPopupWindow.setOnAliPayClickListener(new PayPopupWindow.OnAliPayClickListener() {
            @Override
            public void onClick(View v) {
                aliPay(keda);
            }
        });
        payPopupWindow.setOnWxPayClickListener(new PayPopupWindow.OnWxPayClickListener() {
            @Override
            public void onClick(View v) {
                wxPay(keda);
            }
        });
        tvAnswer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvAnswer.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (adapter.isFlag()) {
                            tvAnswer.setTextColor(Color.parseColor("#3C3C3C"));
                        } else {
                            tvAnswer.setTextColor(Color.parseColor("#ff0000"));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (adapter.isFlag()) {
                            tvAnswer.setTextColor(Color.parseColor("#3C3C3C"));
                        } else {
                            tvAnswer.setTextColor(Color.parseColor("#ff0000"));
                        }
                        break;
                }
                return false;
            }
        });
        tvAnswered.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvAnswered.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (adapter.isFlag()) {
                            tvAnswered.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            tvAnswered.setTextColor(Color.parseColor("#3C3C3C"));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (adapter.isFlag()) {
                            tvAnswered.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            tvAnswered.setTextColor(Color.parseColor("#3C3C3C"));
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void wxPay(NewScienceHelp keda) {
        this.keda = keda;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> kederDB = new HashMap<>();
        Map<String, Object> payHistory = new HashMap<>();
        kederDB.put("id", keda.getKederDB().getId());
        payHistory.put("pay_type", 2);
        map.put("kederDB", kederDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().kedaEavedropWxPay(map).enqueue(new RetrofitCallBack<WXPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WXPay>> call, Response<RetrofitResult<WXPay>> response) {
                if (response.body().getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = response.body().getData();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(MyAnswerActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXPay>> call, Throwable t) {

            }
        });
    }

    private void aliPay(final NewScienceHelp keda) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> kederDB = new HashMap<>();
        Map<String, Object> payHistory = new HashMap<>();
        kederDB.put("id", keda.getKederDB().getId());
        payHistory.put("pay_type", 1);
        map.put("kederDB", kederDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().kedaEavedropAliPay(map).enqueue(new RetrofitCallBack<AliPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<AliPay>> call, Response<RetrofitResult<AliPay>> response) {
                if (response.body().getCode() == 200) {
                    final AliPay aliPay = response.body().getData();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MyAnswerActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                toHttpEavedrop(keda);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<RetrofitResult<AliPay>> call, Throwable t) {

            }
        });
    }

    /**
     * 获取资源
     *
     * @param scienceHelp
     */
    public void toHttpEavedrop(final NewScienceHelp scienceHelp) {
        keda = scienceHelp;
        Map<String, Object> map = new HashMap<>();
        map.put("kederid", scienceHelp.getKederDB().getId());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_EAVEDROP_MEDIA, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    EavedropMediaBean eavedrop = (EavedropMediaBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), EavedropMediaBean.class);
                    AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + eavedrop.getAudiourl());
                    AudioPlayManager.getInstance().setmMediaPlayId(scienceHelp.getKederDB().getId());
                    adapter.notifyDataSetChanged();
                } else if (result.getCode() == 101) {
//                    EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_ACTIVITY_SHOW_PAY);
//                    msg.setBody(scienceHelp);
//                    EventBus.getDefault().post(msg);
//                    showPayWindow(scienceHelp);
                    payPopupWindow.show("1");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpAnswer() {
        showLoadingDialog("请稍候");
//        HttpUtil.ossUpload(this, Util.getSDCardPath() + "/answer.aac", new HttpUtil.HttpOSSUploadCallBack() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//
//            }
//
//            @Override
//            public void onSuccess(final PutObjectRequest request, PutObjectResult result, String successUrl) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("audiourl", successUrl.substring(successUrl.indexOf("/")));
//                map.put("questionid", questionId);
//                map.put("name", successUrl.substring(successUrl.lastIndexOf("/")));
//                long duration = endTime - startTime;
//                int time = (int) (duration / 1000);
//                map.put("taketime", time);
//                HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.KEDA_ANSWER, map, new HttpUtil.HttpCallBack() {
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        loadingDismiss();
//                        if (result.getCode() == 200) {
//                            List<ScienceHelp> list = adapter.getWaitAnswerDatas();
//                            int index = -1;
//                            for (int i = 0; i < list.size(); i++) {
//                                if (list.get(i).getQuestionid().equals(questionId)) {
//                                    index = i;
//                                    break;
//                                }
//                            }
//                            if (index != -1) {
//                                adapter.getWaitAnswerDatas().remove(index);
//                                adapter.notifyDataSetChanged();
//                            }
//                            Toast.makeText(mContext, "回答成功", Toast.LENGTH_SHORT).show();
//                            toHttpGetAnswer();
//                            toHttpGetWaitAnswer();
//                        } else {
//                            Toast.makeText(mContext, "回答失败", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//                        loadingDismiss();
//                        Toast.makeText(mContext, "回答失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                loadingDismiss();
//            }
//        });
        HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

            @Override
            public void successResult(UploadResultEntity result) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("audiourl", result.getFileurl());
                map.put("questionid", questionId);
                map.put("name", result.getFileurl());
                long duration = endTime - startTime;
                int time = (int) (duration / 1000);
                map.put("taketime", time);
                HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.KEDA_ANSWER, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        loadingDismiss();
                        if (result.getCode() == 200) {
                            List<NewScienceHelp> list = adapter.getWaitAnswerDatas();
                            int index = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getKedaQuestionsDB().getId().equals(questionId)) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                adapter.getWaitAnswerDatas().remove(index);
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(mContext, "回答成功", Toast.LENGTH_SHORT).show();
                            toHttpGetAnswer();
                            toHttpGetWaitAnswer();
                        } else {
                            Toast.makeText(mContext, "已回答或回答失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                        loadingDismiss();
                        Toast.makeText(mContext, "回答失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                Toast.makeText(DemoApplication.applicationContext, "上传录音失败", Toast.LENGTH_SHORT).show();
            }
        }, new File(Util.getSDCardPath() + "/answer.aac"));
    }

    private void initData() {

        if (answerPeopleDetail == null) {
            if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "profession")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "profession"))) {
                tvJob.setText(SpUtils.getStringSp(this, "profession"));

                if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "position")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "position"))) {
                    tvJob.setText(SpUtils.getStringSp(this, "profession") + "/" + SpUtils.getStringSp(this, "position"));
                } else {
                    tvJob.setText("");
                }
            } else {
                tvJob.setText("");
                if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "position")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "position"))) {
                    tvJob.setText(SpUtils.getStringSp(this, "position"));
                } else {
                    tvJob.setText("");
                }
            }
            tvName.setText(SpUtils.getStringSp(this, "userName"));
            String userAvatar = SpUtils.getStringSp(this, "userimgurl");
            Glide.with(this).load(Util.getImgUrl(userAvatar)).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar)
                    .into(imgAvatar);
            return;
        }
        if (!TextUtils.isEmpty(answerPeopleDetail.getMessageurl())) {
            llCreateVoice.setVisibility(View.GONE);
//            tvPlayVoice.setVisibility(View.VISIBLE);
//            llEditVoice.setVisibility(View.VISIBLE);
            recordState = 1;
        }
        tvFansNumber.setText(" " + answerPeopleDetail.getFollowers() + " 粉丝");
        tvFocusNumber.setText(" " + answerPeopleDetail.getFocusnum() + " 关注");
        tvTotalMoney.setText(answerPeopleDetail.getTotalincome() + "");
        tvJob.setText(answerPeopleDetail.getProfession());
        tvName.setText(answerPeopleDetail.getName());

        etPrice.setText(answerPeopleDetail.getMouthprice() + "");
        Glide.with(this).load(Util.getImgUrl(answerPeopleDetail.getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar)
                .into(imgAvatar);
        initDomans();
    }

    /**
     * 初始化科答方向
     */
    private void initDomans() {
        if (answerPeopleDetail != null && !TextUtils.isEmpty(answerPeopleDetail.getDomain()) && !TextUtils.equals(answerPeopleDetail.getDomain(), "null")) {
            String[] ids = answerPeopleDetail.getDomain().split(",");
            List<String> idList = Arrays.asList(ids);
            userChannelList.clear();
            otherChannelList.clear();
            for (int i = 0; i < listHelp.size(); i++) {
                if (idList.contains(listHelp.get(i).getId() + "")) {
                    userChannelList.add(new ChannelItem(listHelp.get(i).getId(), listHelp.get(i).getKdname(), userChannelList.size() + 1, 1));
                } else {
                    otherChannelList.add(new ChannelItem(listHelp.get(i).getId(), listHelp.get(i).getKdname(), otherChannelList.size() + 1, 0));
                }
            }
        } else {
            for (int i = 0; i < listHelp.size(); i++) {
                otherChannelList.add(new ChannelItem(listHelp.get(i).getId(), listHelp.get(i).getKdname(), otherChannelList.size() + 1, 0));
            }
        }
        userAdapter = new MyDragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new MyOtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTO_CREATE_KEDA_ACTIVITY:
                    toHttpGetDetail();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit:
                Intent intent = new Intent(this, CreateKedaUserActivity.class);
                intent.putExtra("edit", true);
                intent.putParcelableArrayListExtra("userList", userChannelList);
                startActivityForResult(intent, INTO_CREATE_KEDA_ACTIVITY);
                break;
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_answer:
                tvAnswer.setTextColor(Color.parseColor("#ff0000"));
                tvAnswered.setTextColor(Color.parseColor("#3C3C3C"));
                adapter.setFlag(false);
                lvMyAnswer.setHasLoadMore(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_answered:
                tvAnswered.setTextColor(Color.parseColor("#ff0000"));
                tvAnswer.setTextColor(Color.parseColor("#3C3C3C"));
                adapter.setFlag(true);
                lvMyAnswer.setHasLoadMore(true);
                adapter.notifyDataSetChanged();
                break;
//            case R.id.tv_change:
//                switch (state) {
//                    case 0:
//                        otherGridView.setEnabled(true);
//                        userGridView.setEnabled(true);
//                        tvChange.setText("完成");
//                        state = 1;
//                        break;
//                    case 1:
//                        otherGridView.setEnabled(false);
//                        userGridView.setEnabled(false);
//                        tvChange.setText("修改方向");
//                        tvChange.setBackgroundResource(R.drawable.shape_answer_top_send_enable);
//                        otherGridView.setVisibility(View.GONE);
//                        state = 2;
//                        break;
//                    case 2:
//                        otherGridView.setVisibility(View.VISIBLE);
//                        otherGridView.setEnabled(true);
//                        userGridView.setEnabled(true);
//                        tvChange.setText("完成");
//                        tvChange.setBackgroundResource(R.drawable.shape_add_item);
//                        state = 1;
//                        break;
//                }
//                break;
            case R.id.img_create_voice:
                //科答修改前
                /*if (isRecording) {
                    llCreateVoice.setVisibility(View.GONE);
                    tvPlayVoice.setVisibility(View.VISIBLE);
                    llEditVoice.setVisibility(View.VISIBLE);
                    isRecording = false;
                    rlContainer.setVisibility(View.GONE);
                    try {
                        recorder.setOnErrorListener(null);
                        recorder.setPreviewDisplay(null);
                        recorder.stop();// 停止刻录
//                        recorder.reset(); // 重新启动MediaRecorder.
                    } catch (Exception e) {

                    }
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                } else {
                    //录制我的答人留言
                    Acp.getInstance(mContext).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.RECORD_AUDIO)
                            .build(), new AcpListener() {
                        @Override
                        public void onGranted() {
                            imgCreateVoice.setImageResource(R.drawable.record_pressed);
                            tvCreateVoice.setText("正在录制");
                            isRecording = true;
                            rlContainer.setVisibility(View.VISIBLE);
                            recordState = 1;
                            recordStart(Util.getSDCardPath() + "/peipei.aac");
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            Toast.makeText(mContext, "有部分功能将无法使用", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
                //科答修改后
                showRecordPop();
                break;
            case R.id.img_message:
                if (recordState == 1) {
                    if (answerPeopleDetail == null || TextUtils.isEmpty(answerPeopleDetail.getMessageurl()) || answerPeopleDetail.getMessageurl().equals("null")) {
                        recordState = 0;
                        Toast.makeText(this, "您还没有录制留言", Toast.LENGTH_SHORT).show();
                    }
                    //播放网络音频
                    if (answerPeopleDetail != null) {
                        if (!TextUtils.isEmpty(answerPeopleDetail.getMessageurl()) && answerPeopleDetail.getMessageurl().startsWith("/"))
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + answerPeopleDetail.getMessageurl());
                        else
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + "/" + answerPeopleDetail.getMessageurl());
                    } else {
                        AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/peipei.aac");
                    }
                } else if (recordState == 2) {
                    AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/peipei.aac");
                } else
                    Toast.makeText(this, "您还没有录制留言", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.tv_play_voice:
////                        mediaPlayer.setDataSource("/sdcard/peipei.aac");
//                if (recordState == 1) {
//                    //播放网络音频
//                    if (answerPeopleDetail != null) {
//                        if (answerPeopleDetail.getMessageurl().startsWith("/"))
//                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + answerPeopleDetail.getMessageurl());
//                        else
//                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + "/" + answerPeopleDetail.getMessageurl());
//                    } else {
//                        AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/peipei.aac");
//                    }
//                } else if (recordState == 2) {
//                    AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/peipei.aac");
//                }
//                break;
//            case R.id.img_edit_voice:
//                if (isRecording) {
////                    tvPlayVoice.setEnabled(true);
//                    isRecording = false;
//                    try {
//                        recorder.setOnErrorListener(null);
//                        recorder.setPreviewDisplay(null);
//                        recorder.stop();
////                        recorder.reset();
////                        recorder.release();
//                    } catch (Exception e) {
//
//                    }
////                    recorder = null;
//                    imgEditVoice.setImageResource(R.drawable.record);
//                    tvEditVoice.setText("修改录音");
//                    rlContainer.setVisibility(View.GONE);
////                    sendToHttp();
//                } else {
//                    AudioPlayManager.getInstance().release();
////                    tvPlayVoice.setEnabled(false);
//                    recordState = 2;
//                    imgEditVoice.setImageResource(R.drawable.record_pressed);
//                    tvEditVoice.setText("正在录制");
//                    isRecording = true;
//                    rlContainer.setVisibility(View.VISIBLE);
//                    Acp.getInstance(mContext).request(new AcpOptions.Builder()
//                            .setPermissions(Manifest.permission.RECORD_AUDIO)
//                            .build(), new AcpListener() {
//                        @Override
//                        public void onGranted() {
//                            recordStart(Util.getSDCardPath() + "/peipei.aac");
//                        }
//
//                        @Override
//                        public void onDenied(List<String> permissions) {
//                            Toast.makeText(mContext, "录制失败", Toast.LENGTH_SHORT).show();
////                            tvPlayVoice.setEnabled(true);
//                            recordState = 1;
//                        }
//                    });
//                }
//                break;
            case R.id.btn_save:
                if (recordState == 0) {
                    Toast.makeText(this, "请录制你的留言", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userChannelList.size() == 0) {
                    Toast.makeText(this, "请至少选择一个方向", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPrice.getText().toString()) || TextUtils.equals("0", etPrice.getText().toString())) {
                    Toast.makeText(this, "请设置你的润口费", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingDialog("请稍候");
//                HttpUtil.ossUpload(this, Util.getSDCardPath() + "/peipei.aac", new HttpUtil.HttpOSSUploadCallBack() {
//                    @Override
//                    public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(final PutObjectRequest request, PutObjectResult result, String successUrl) {
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("mouthprice", Double.parseDouble(etPrice.getText().toString()));
//                        map.put("messageurl", successUrl.substring(successUrl.indexOf("/")));
//                        final StringBuffer sb = new StringBuffer();
//                        for (int i = 0; i < userChannelList.size(); i++) {
//                            if (i == userChannelList.size() - 1) {
//                                sb.append(userChannelList.get(i).getId());
//                            } else {
//                                sb.append(userChannelList.get(i).getId() + ",");
//                            }
//                        }
//                        map.put("domain", sb.toString());
//                        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.INSERT_KEDA_USER, map, new HttpUtil.HttpCallBack() {
//                            @Override
//                            public void successResult(ResultEntity result) throws JSONException {
//                                if (result.getCode() == 200) {
//                                    oldDomanList = new ArrayList<String>();
//                                    oldDomanList = Arrays.asList(sb.toString().split(","));
//                                    oldMouthprice = Double.parseDouble(etPrice.getText().toString());
//                                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.NOTYFY_ANSWER_INFO_SAVE));
//                                    btnSave.setVisibility(View.GONE);
//                                    recordState = 1;
////                                    Toast.makeText(DemoApplication.applicationContext, "注册成功", Toast.LENGTH_SHORT).show();
////                                    showWaringDialog("提示", "是否进行下一步身份认证", new OnDialogButtonClickListener() {
////                                        @Override
////                                        public void onPositiveButtonClick() {
////
////                                        }
////
////                                        @Override
////                                        public void onNegativeButtonClick() {
////
////                                        }
////                                    });
//                                    toHttpGetUserInfo();
//                                } else {
//                                    loadingDismiss();
//                                    Toast.makeText(DemoApplication.applicationContext, "注册失败", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void errorResult(Throwable ex, boolean isOnCallback) {
//                                loadingDismiss();
//                                Toast.makeText(DemoApplication.applicationContext, "注册失败", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//
//                    }
//                });
                HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

                    @Override
                    public void successResult(UploadResultEntity result) throws JSONException {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("mouthprice", Double.parseDouble(etPrice.getText().toString()));
                        map.put("messageurl", result.getFileurl());
                        final StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < userChannelList.size(); i++) {
                            if (i == userChannelList.size() - 1) {
                                sb.append(userChannelList.get(i).getId());
                            } else {
                                sb.append(userChannelList.get(i).getId() + ",");
                            }
                        }
                        map.put("domain", sb.toString());
                        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.INSERT_KEDA_USER, map, new HttpUtil.HttpCallBack() {
                            @Override
                            public void successResult(ResultEntity result) throws JSONException {
                                if (result.getCode() == 200) {
                                    oldDomanList = new ArrayList<String>();
                                    oldDomanList = Arrays.asList(sb.toString().split(","));
                                    oldMouthprice = Double.parseDouble(etPrice.getText().toString());
                                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.NOTYFY_ANSWER_INFO_SAVE));
                                    btnSave.setVisibility(View.GONE);
                                    recordState = 1;
//                                    Toast.makeText(DemoApplication.applicationContext, "注册成功", Toast.LENGTH_SHORT).show();
//                                    showWaringDialog("提示", "是否进行下一步身份认证", new OnDialogButtonClickListener() {
//                                        @Override
//                                        public void onPositiveButtonClick() {
//
//                                        }
//
//                                        @Override
//                                        public void onNegativeButtonClick() {
//
//                                        }
//                                    });
                                    toHttpGetUserInfo();
                                } else {
                                    loadingDismiss();
                                    Toast.makeText(DemoApplication.applicationContext, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void errorResult(Throwable ex, boolean isOnCallback) {
                                loadingDismiss();
                                Toast.makeText(DemoApplication.applicationContext, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                        loadingDismiss();
                        Toast.makeText(DemoApplication.applicationContext, "上传录音失败", Toast.LENGTH_SHORT).show();
                    }
                }, new File(Util.getSDCardPath() + "/peipei.aac"));
                break;
//            case R.id.img_avatar:
//                Intent intent = new Intent(this, HomePageActivity.class);
//                intent.putExtra("userId", SpUtils.getStringSp(this, "userId"));
//                startActivity(intent);
//                break;
            case R.id.ll_fans_click:
                Intent fans = new Intent(this, MyFansActivity.class);
                fans.putExtra("userId", SpUtils.getStringSp(this, "userId"));
                startActivity(fans);
                break;
            case R.id.ll_focus_click:
                Intent follows = new Intent(this, MyFollowActivity.class);
                follows.putExtra("userId", SpUtils.getStringSp(this, "userId"));
                startActivity(follows);
                break;
            case R.id.rl_answer_top:
                imm.hideSoftInputFromWindow(etPrice.getWindowToken(), 0);
                break;
//            case R.id.ll_expand:
//                if (llExpand.getTag().equals(0)) {
//                    //当先未选中的方向为折叠，需要展开
//                    otherGridView.setVisibility(View.VISIBLE);
//                    llExpand.setTag(1);
//                    RotateAnimation expandAnim;
//                    if (System.currentTimeMillis() - startTime > 360)
//                        expandAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    else
//                        expandAnim = new RotateAnimation((System.currentTimeMillis() - startTime) / 4, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    expandAnim.setDuration(360);
//                    expandAnim.setFillAfter(true);
//                    startTime = System.currentTimeMillis();
//                    imgExpand.startAnimation(expandAnim);
//                } else {
//                    //当前未选中的方向为展开，需要折叠
//                    otherGridView.setVisibility(View.GONE);
//                    llExpand.setTag(0);
//                    RotateAnimation collapseAnim;
//                    if (System.currentTimeMillis() - startTime > 360)
//                        collapseAnim = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    else
//                        collapseAnim = new RotateAnimation((System.currentTimeMillis() - startTime) / 4, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    collapseAnim.setDuration(360);
//                    collapseAnim.setFillAfter(true);
//                    startTime = System.currentTimeMillis();
//                    imgExpand.startAnimation(collapseAnim);
//                }
//                break;
        }
    }

    /**
     * 获取答人详情
     */
    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    AnswerPeopleDetail answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    MyAnswerActivity.this.answerPeopleDetail = answerPeopleDetail;
                    initData();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    /**
     * 弹出录音PopupWindow
     */
    private void showRecordPop() {
        tempRecordVaild = false;
        if (recordPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_record, null);
            llSpeak = (AutoLinearLayout) view.findViewById(R.id.ll_speak);
            tvSaveRecord = (TextView) view.findViewById(R.id.tv_save_record);
            llPlayContainer = (AutoLinearLayout) view.findViewById(R.id.ll_play_container);
            llSpeakContainer = (AutoLinearLayout) view.findViewById(R.id.ll_speak_container);
            pbDuration = (ProgressBar) view.findViewById(R.id.pb_duration);
            tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            llPlay = view.findViewById(R.id.ll_play);
            llPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempRecordVaild) {
                        AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/temp_record.aac");
                        llPlay.setEnabled(false);
                    } else {
                        Toast.makeText(mContext, "请先录制", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            tvSaveRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llPlayContainer.getVisibility() == View.VISIBLE) {
                        imgMessage.setVisibility(View.VISIBLE);
                        recordState = 2;
                        FileUtils.copyFile(Util.getSDCardPath() + "/temp_record.aac", Util.getSDCardPath() + "/peipei.aac", true);
                        Util.deleteFiles(Util.getSDCardPath() + "/temp_record.aac");
                        Util.finishPop(MyAnswerActivity.this, recordPopupWindow);
                    } else {
                        recordState = 1;
                        Toast.makeText(mContext, "请先录制", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            llSpeak.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        isPressed = true;
                        Message message = Message.obtain();
                        message.what = 3;
                        mHandler.sendMessage(message);
//                    mHandler.sendEmptyMessageDelayed(2, 1000);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        pbDuration.setProgress(0);
                        mHandler.removeMessages(3);
                        rlTopContainer.setVisibility(View.GONE);
                        if (!isAnswering) {
                            isPressed = false;
                            return true;
                        }
                        isAnswering = false;
                        endTime = System.currentTimeMillis();
                        tempRecordVaild = true;
                        try {
                            recorder.setOnErrorListener(null);
                            recorder.setPreviewDisplay(null);
                            recorder.stop();// 停止刻录
//                        recorder.reset();
                        } catch (Exception e) {

                        }
//                    recorder.reset(); // 重新启动MediaRecorder.
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                        if (endTime - startTime < 1000)
                            Toast.makeText(mContext, "留言时间时间必须大于1秒", Toast.LENGTH_SHORT).show();
                        else
                            llPlayContainer.setVisibility(View.VISIBLE);
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        rlTopContainer.setVisibility(View.GONE);
                        mHandler.removeMessages(3);
                        if (!isAnswering) {
                            isPressed = false;
                            return true;
                        }
                        isAnswering = false;
                        endTime = System.currentTimeMillis();
                        try {
                            recorder.setOnErrorListener(null);
                            recorder.setPreviewDisplay(null);
                            recorder.stop();// 停止刻录
//                        recorder.reset(); // 重新启动MediaRecorder.
                        } catch (Exception e) {

                        }
//                    recorder.release(); // 刻录完成一定要释放资源
//                    recorder = null;
                        if (endTime - startTime > 1000)
                            Toast.makeText(mContext, "取消回答", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            recordPopupWindow = new PopupWindow(view, -2, -2);
            recordPopupWindow.setContentView(view);
            recordPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            recordPopupWindow.setFocusable(true);
            recordPopupWindow.setOutsideTouchable(true);
            recordPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            recordPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(MyAnswerActivity.this, recordPopupWindow);
                }
            });
            recordPopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        } else {
            llPlayContainer.setVisibility(View.GONE);
            tvDuration.setText("00:00");
            pbDuration.setProgress(0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            recordPopupWindow.showAtLocation(rlAll, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 获取用户信息
     */
    private void toHttpGetUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    if (user.getAuthentication() == 0) {
                        Toast.makeText(DemoApplication.applicationContext, "注册成功", Toast.LENGTH_SHORT).show();
                        showWaringDialog("提示", "是否进行下一步身份认证", new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                startActivity(new Intent(mContext, AnswerAuthenticationActivity.class));
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    }
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "注册成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                Toast.makeText(DemoApplication.applicationContext, "注册成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recordStart(String path) {
        if (recorder == null) {
            recorder = new MediaRecorder();// new出MediaRecorder对象
            recorder.setOnErrorListener(null);
        } else
            recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioChannels(1); // MONO
        recorder.setAudioSamplingRate(8000); // 8000Hz
        recorder.setAudioEncodingBitRate(64);
        recorder.setOutputFile(path);
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
            isAnswering = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.userGridView:
//                if (llExpand.getTag().equals(0)) {
//                    otherGridView.setVisibility(View.VISIBLE);
//                    llExpand.setTag(1);
//                }
                //position为 0，1 的不可以进行任何操作
                if (true) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((MyDragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        //添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                if (userAdapter.getCount() > 4) {
                    Toast.makeText(this, "最多选择五个方向", Toast.LENGTH_SHORT).show();
                    break;
                }
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((MyOtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof MyDragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof MyAnswerActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    toHttpEavedrop(keda);
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.EAVEDROP_COMPLITE:
                adapter.notifyDataSetChanged();
                break;
            case SystemConstant.AUDIO_COMPLET:
                if (pbDuration != null)
                    pbDuration.setProgress(0);
                llPlay.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_KEDA_NOTICE);
        msg.setArg1(1);
        EventBus.getDefault().post(msg);
        AudioPlayManager.getInstance().release();
        EventBus.getDefault().unregister(this);
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
        mHandler = null;
        //oldDomanList只有用户不是答人且没有注册为答人时才为空，此时则不往下走
//        if (oldDomanList == null) {
//            return;
//        }
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < userChannelList.size(); i++) {
//            if (i == userChannelList.size() - 1) {
//                sb.append(userChannelList.get(i).getId());
//            } else {
//                sb.append(userChannelList.get(i).getId() + ",");
//            }
//        }
//        AudioPlayManager.getInstance().release();
//        Map<String, Object> map = new HashMap();
//        List<String> newDomanList = Arrays.asList(sb.toString().split(","));
//        boolean f = false;
//        if (oldDomanList.size() == newDomanList.size()) {
//            for (String s : newDomanList) {
//                if (!oldDomanList.contains(s)) {
//                    f = true;
//                    break;
//                }
//            }
//        } else {
//            f = true;
//        }
//        //退出时当前的方向与上一次保存的方向不同，需要更新
//        if (f) {
//            map.put("domain", sb.toString());
//        }
//        //退出时当前的润口费与上一次保存的润口费不同，需要更新
//        if (!TextUtils.isEmpty(etPrice.getText().toString()) && Double.parseDouble(etPrice.getText().toString()) != oldMouthprice) {
//            map.put("mouthprice", Double.parseDouble(etPrice.getText().toString()));
//        }
//        if (recordState == 2) {
//            map.put("messageurl", Util.getSDCardPath() + "/peipei.aac");
//        }
//        if (map.keySet().size() > 0) {
//            EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_INFORMATION_CHANGED);
//            msg.setBody(map);
//            EventBus.getDefault().post(msg);
//        }
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }
}
