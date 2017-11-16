package com.yidiankeyan.science.information.acitivity;

import android.Manifest;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
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
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.yidiankeyan.science.view.CircleProgressLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.cl_bottom_answer;
import static com.yidiankeyan.science.R.id.img_play_pause;


public class MyAnswerDetailActivity extends BaseActivity {

    private ImageView imgMakeAvatar;
    private TextView tvMakeName;
    private TextView tvAnswerState;
    private TextView tvPrice;
    private TextView tvMakeContent;
    private TextView tvMakeTime;
    private AutoRelativeLayout rlBottomController;
    private TextView tvBottomDuration;
    private ImageView imgBottomAnswer;
    private TextView tvBottomHint;
    private ImageView imgBottomPlayPause;
    private View llBottomSubmit;
    private CircleProgressLayout clBottomAnswer;
    private NewScienceHelp keda;
    private MediaRecorder recorder;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int recordState;//0:还未开始;1:正在录音;2:录制完成
    private long startTime;
    private View llBottomReRecord;
    private ImageView imgPlayPause;

    private AutoLinearLayout llAnswer;
    private ImageView imgSolverAvatar;
    private TextView tvSolverName;
    private TextView tvSolverProfession;
    private AutoRelativeLayout imgResponderEavedrop;
    private TextView tvPermission;
    private ImageView imgResponderPlay;
    private TextView tvResponderLength;
    private TextView tvFollowNumber;
    private TextView tvAnswerNumber;
    //↑回答信息

    private AutoRelativeLayout rlRequest;
    private ImageView imgRequestMakeAvatar;
    private TextView tvRequestMakeName;
    private TextView tvRequestMakeContent;
    private TextView tvRequestMakeTime;
    //↑追问信息

    private AutoRelativeLayout rlController;
    private TextView tvDuration;
    private ImageView imgAnswer;
    private TextView tvHint;
    private View llReplyRecord;
    private View llSubmit;
    private CircleProgressLayout clAnswer;
    //回答追问的控制台

    private AutoLinearLayout llReplyAnswer;
    private ImageView imgReplySolverAvatar;
    private TextView tvReplySolverName;
    private TextView tvReplySolverProfession;
    private AutoRelativeLayout imgReplyResponderEavedrop;
    private TextView tvReplyPermission;
    private ImageView imgReplyResponderPlay;
    private TextView tvReplyResponderLength;
    private TextView tvReplyFollowNumber;
    private TextView tvReplyAnswerNumber;
    //↑追答信息

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //正在录音
                    long duration = System.currentTimeMillis() - startTime;
                    int second = (int) (duration / 1000);
                    int mill = (int) ((duration % 1000) / 10);
                    if (second < 10) {
                        if (rlBottomController.getVisibility() == View.VISIBLE) {
                            tvBottomDuration.setText("0" + second + ":" + (mill < 10 ? "0" + mill : mill));
                            clBottomAnswer.setProgress(second);
                        } else {
                            tvDuration.setText("0" + second + ":" + (mill < 10 ? "0" + mill : mill));
                            clAnswer.setProgress(second);
                        }
                    } else {
                        if (rlBottomController.getVisibility() == View.VISIBLE) {
                            tvBottomDuration.setText(second + ":" + (mill < 10 ? "0" + mill : mill));
                            clBottomAnswer.setProgress(second);
                        } else {
                            tvDuration.setText("0" + second + ":" + (mill < 10 ? "0" + mill : mill));
                            clAnswer.setProgress(second);
                        }
                    }
                    break;
            }
        }
    };
    private long endTime;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_answer_detail;
    }

    @Override
    protected void initView() {
        imgMakeAvatar = (ImageView) findViewById(R.id.img_make_avatar);
        tvMakeName = (TextView) findViewById(R.id.tv_make_name);
        tvAnswerState = (TextView) findViewById(R.id.tv_answer_state);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvMakeContent = (TextView) findViewById(R.id.tv_make_content);
        tvMakeTime = (TextView) findViewById(R.id.tv_make_time);
        rlBottomController = (AutoRelativeLayout) findViewById(R.id.rl_bottom_controller);
        tvBottomDuration = (TextView) findViewById(R.id.tv_bottom_duration);
        imgBottomAnswer = (ImageView) findViewById(R.id.img_bottom_answer);
        tvBottomHint = (TextView) findViewById(R.id.tv_bottom_hint);
        clBottomAnswer = (CircleProgressLayout) findViewById(cl_bottom_answer);
        llBottomSubmit = findViewById(R.id.ll_bottom_submit);
        imgBottomPlayPause = (ImageView) findViewById(R.id.img_bottom_play_pause);
        llBottomReRecord = findViewById(R.id.ll_bottom_re_record);
        imgPlayPause = (ImageView) findViewById(img_play_pause);

        llAnswer = (AutoLinearLayout) findViewById(R.id.ll_answer);
        imgSolverAvatar = (ImageView) findViewById(R.id.img_solver_avatar);
        tvSolverName = (TextView) findViewById(R.id.tv_solver_name);
        tvSolverProfession = (TextView) findViewById(R.id.tv_solver_profession);
        imgResponderEavedrop = (AutoRelativeLayout) findViewById(R.id.img_responder_eavedrop);
        tvPermission = (TextView) findViewById(R.id.tv_permission);
        imgResponderPlay = (ImageView) findViewById(R.id.img_responder_play);
        tvResponderLength = (TextView) findViewById(R.id.tv_responder_length);
        tvFollowNumber = (TextView) findViewById(R.id.tv_follow_number);
        tvAnswerNumber = (TextView) findViewById(R.id.tv_answer_number);

        rlRequest = (AutoRelativeLayout) findViewById(R.id.rl_request);
        imgRequestMakeAvatar = (ImageView) findViewById(R.id.img_request_make_avatar);
        tvRequestMakeName = (TextView) findViewById(R.id.tv_request_make_name);
        tvRequestMakeContent = (TextView) findViewById(R.id.tv_request_make_content);
        tvRequestMakeTime = (TextView) findViewById(R.id.tv_request_make_time);

        rlController = (AutoRelativeLayout) findViewById(R.id.rl_controller);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        imgAnswer = (ImageView) findViewById(R.id.img_answer);
        tvHint = (TextView) findViewById(R.id.tv_hint);
        llReplyRecord = findViewById(R.id.ll_re_record);
        llSubmit = findViewById(R.id.ll_submit);
        clAnswer = (CircleProgressLayout) findViewById(R.id.cl_answer);

        llReplyAnswer = (AutoLinearLayout) findViewById(R.id.ll_reply_answer);
        imgReplySolverAvatar = (ImageView) findViewById(R.id.img_reply_solver_avatar);
        tvReplySolverName = (TextView) findViewById(R.id.tv_reply_solver_name);
        tvReplySolverProfession = (TextView) findViewById(R.id.tv_reply_solver_profession);
        imgReplyResponderEavedrop = (AutoRelativeLayout) findViewById(R.id.img_reply_responder_eavedrop);
        tvReplyPermission = (TextView) findViewById(R.id.tv_reply_permission);
        imgReplyResponderPlay = (ImageView) findViewById(R.id.img_reply_responder_play);
        tvReplyResponderLength = (TextView) findViewById(R.id.tv_reply_responder_length);
        tvReplyFollowNumber = (TextView) findViewById(R.id.tv_reply_follow_number);
        tvReplyAnswerNumber = (TextView) findViewById(R.id.tv_reply_answer_number);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText("问题详情");
        findViewById(R.id.ll_return).setOnClickListener(this);
        imgBottomAnswer.setOnClickListener(this);
        llBottomSubmit.setOnClickListener(this);
        clBottomAnswer.setOnClickListener(this);
        llBottomReRecord.setOnClickListener(this);
        imgPlayPause.setOnClickListener(this);
        imgResponderEavedrop.setOnClickListener(this);
        imgBottomPlayPause.setOnClickListener(this);
        imgReplyResponderEavedrop.setOnClickListener(this);
        clAnswer.setOnClickListener(this);
        imgAnswer.setOnClickListener(this);
        llSubmit.setOnClickListener(this);
        llReplyRecord.setOnClickListener(this);
        clBottomAnswer.setMax(60);
        clAnswer.setMax(60);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (recordState == 1) {
//                    Message message = Message.obtain();
//                    message.what = 1;
//                    message.arg1 = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 10);
        toHttpGetDetail();
    }

    private void toHttpGetDetail() {
        recordState = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getKeDaDetailList(map).enqueue(new RetrofitCallBack<List<NewScienceHelp>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<NewScienceHelp>>> call, Response<RetrofitResult<List<NewScienceHelp>>> response) {
                if (response.body().getCode() == 200) {
                    keda = response.body().getData() == null || response.body().getData().size() == 0 ? null : response.body().getData().get(0);
                    initData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<NewScienceHelp>>> call, Throwable t) {

            }
        });
    }

    private void initData() {
        if (keda == null)
            return;
        Glide.with(this).load(Util.getImgUrl(keda.getMaker().getCoverimg())).into(imgMakeAvatar);
        tvMakeName.setText(keda.getMaker().getName());
        tvMakeTime.setText(TimeUtils.formatKedaTime(keda.getKedaQuestionsDB().getCreatetime()) + "");
        if (DemoApplication.answerAlbumMap.get(keda.getKedaQuestionsDB().getKedaalbumid()) != null)
            tvMakeContent.setText("【" + DemoApplication.answerAlbumMap.get(keda.getKedaQuestionsDB().getKedaalbumid()).getKdname() + "】 " + keda.getKedaQuestionsDB().getQuestions());
        else
            tvMakeContent.setText(keda.getKedaQuestionsDB().getQuestions());
        tvPrice.setText("¥" + keda.getKedaQuestionsDB().getAmount());
        //↑ 设置提问的信息
        if (keda.getKederDB() == null) {
            //待回答问题
            tvAnswerState.setText("待回答");
            rlBottomController.setVisibility(View.VISIBLE);
        } else {
            //已回答问题
            tvAnswerState.setText("已回答");
            llAnswer.setVisibility(View.VISIBLE);
            rlBottomController.setVisibility(View.GONE);
            Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).into(imgSolverAvatar);
            tvSolverName.setText(keda.getSolver().getName());
            tvSolverProfession.setText(keda.getSolver().getProfession());
            tvPermission.setText(keda.getKederInfo().getPermission() == 1 ? "点击播放" : "一墨子币偷听");
            tvResponderLength.setText(TimeUtils.getAnswerLength(keda.getKederDB().getTaketime()) + "");
            tvFollowNumber.setText(keda.getSolver().getFocusnum() + "");
            tvAnswerNumber.setText(keda.getSolver().getAnswersnum() + "");
            if (keda.getKedaReQuestionDB() != null) {
                rlRequest.setVisibility(View.VISIBLE);
                Glide.with(this).load(Util.getImgUrl(keda.getMaker().getCoverimg())).into(imgRequestMakeAvatar);
                tvRequestMakeName.setText(keda.getMaker().getName());
                if (DemoApplication.answerAlbumMap.get(keda.getKedaQuestionsDB().getKedaalbumid()) != null)
                    tvRequestMakeContent.setText("【" + DemoApplication.answerAlbumMap.get(keda.getKedaQuestionsDB().getKedaalbumid()).getKdname() + "】 " + keda.getKedaReQuestionDB().getQuestion());
                else
                    tvRequestMakeContent.setText(keda.getKedaReQuestionDB().getQuestion());
                tvRequestMakeTime.setText(TimeUtils.formatKedaTime(keda.getKedaReQuestionDB().getCretetime()) + "");
                tvAnswerState.setText("已回答");
                if (keda.getKedaReQuestionDB().getIsanswer() == 1) {
                    //已回答追问
                    rlController.setVisibility(View.GONE);
                    llReplyAnswer.setVisibility(View.VISIBLE);

                    Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).into(imgReplySolverAvatar);
                    tvReplySolverName.setText(keda.getSolver().getName());
                    tvReplySolverProfession.setText(keda.getSolver().getProfession());
                    tvReplyPermission.setText(keda.getKederInfo().getPermission() == 1 ? "点击播放" : "一墨子币偷听");
                    tvReplyResponderLength.setText(TimeUtils.getAnswerLength(keda.getKedaReQuestionDB().getTaketime()) + "");
                    tvReplyFollowNumber.setText(keda.getSolver().getFocusnum() + "");
                    tvReplyAnswerNumber.setText(keda.getSolver().getAnswersnum() + "");
                } else {
                    //待回答追问
                    rlController.setVisibility(View.VISIBLE);
                    llReplyAnswer.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.img_bottom_answer:
                switch (recordState) {
                    case 0:
                        AudioPlayManager.getInstance().release();
                        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.RECORD_AUDIO)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
//                                tvBottomAnswer.setText("点击停止");
                                tvBottomHint.setVisibility(View.GONE);
                                llBottomReRecord.setVisibility(View.GONE);
                                llBottomSubmit.setVisibility(View.GONE);
                                imgBottomAnswer.setVisibility(View.GONE);
                                clBottomAnswer.setVisibility(View.VISIBLE);
                                record(Util.getSDCardPath() + "/answer.aac");
                                startTime = System.currentTimeMillis();
                                recordState = 1;
                                //↑开始录音
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastMaker.showShortToast("无法录音，请开启权限");
                            }
                        });
                        break;
                    case 1:
                        recordState = 2;
//                        tvBottomAnswer.setText("点击试听");
                        llBottomReRecord.setVisibility(View.VISIBLE);
                        llBottomSubmit.setVisibility(View.VISIBLE);
                        recordStop();
                        endTime = System.currentTimeMillis();
                        //↑停止录音进入试听状态
                        break;
                    case 2:
                        AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/answer.aac");
                        break;
                }
                break;
            case cl_bottom_answer:
                clBottomAnswer.setVisibility(View.GONE);
                recordState = 2;
//                        tvBottomAnswer.setText("点击试听");
                llBottomReRecord.setVisibility(View.VISIBLE);
                llBottomSubmit.setVisibility(View.VISIBLE);
                imgBottomPlayPause.setVisibility(View.VISIBLE);
                recordStop();
                endTime = System.currentTimeMillis();
                //↑停止录音进入试听状态
                break;
            case R.id.img_bottom_play_pause:
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING) {
                    AudioPlayManager.getInstance().pause();
                    DemoApplication.isPlay = false;
                    imgBottomPlayPause.setImageResource(R.drawable.icon_answer_play);
                } else if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
                    AudioPlayManager.getInstance().resume();
                    DemoApplication.isPlay = true;
                    imgBottomPlayPause.setImageResource(R.drawable.icon_answer_pause);
                } else {
                    AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/answer.aac");
                    imgBottomPlayPause.setImageResource(R.drawable.icon_answer_pause);
                }
                break;
            case img_play_pause:
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING) {
                    AudioPlayManager.getInstance().pause();
                    DemoApplication.isPlay = false;
                    imgPlayPause.setImageResource(R.drawable.icon_answer_play);
                } else if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
                    AudioPlayManager.getInstance().resume();
                    DemoApplication.isPlay = true;
                    imgPlayPause.setImageResource(R.drawable.icon_answer_pause);
                } else {
                    AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/answer.aac");
                    imgPlayPause.setImageResource(R.drawable.icon_answer_pause);
                }
                break;
            case R.id.ll_bottom_re_record:
                AudioPlayManager.getInstance().release();
                Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.RECORD_AUDIO)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
//                        tvBottomAnswer.setText("点击停止");
                        tvBottomHint.setVisibility(View.GONE);
                        llBottomReRecord.setVisibility(View.GONE);
                        llBottomSubmit.setVisibility(View.GONE);
                        clBottomAnswer.setVisibility(View.VISIBLE);
                        record(Util.getSDCardPath() + "/answer.aac");
                        startTime = System.currentTimeMillis();
                        recordState = 1;
                        //↑开始录音
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastMaker.showShortToast("无法录音，请开启权限");
                    }
                });
                break;
            case R.id.ll_bottom_submit:
                AudioPlayManager.getInstance().release();
                submitAnswer();
                //↑上传录音
                break;
            case R.id.img_responder_eavedrop:
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + keda.getKederDB().getAnswerurl());
                AudioPlayManager.getInstance().setmMediaPlayId("1111");
                AnimationDrawable animationDrawable2 = (AnimationDrawable) imgReplyResponderPlay.getDrawable();
                animationDrawable2.stop();
                //↑播放录音
                break;

            case R.id.img_reply_responder_eavedrop:
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + keda.getKedaReQuestionDB().getAnswerurl());
                AudioPlayManager.getInstance().setmMediaPlayId("2222");
                AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
                animationDrawable.stop();
                //↑播放追答录音
                break;

            case R.id.img_answer:
                switch (recordState) {
                    case 0:
                        AudioPlayManager.getInstance().release();
                        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.RECORD_AUDIO)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
//                                tvAnswer.setText("点击停止");
                                tvHint.setVisibility(View.GONE);
                                imgAnswer.setVisibility(View.GONE);
                                llReplyRecord.setVisibility(View.GONE);
                                llSubmit.setVisibility(View.GONE);
                                clAnswer.setVisibility(View.VISIBLE);
                                record(Util.getSDCardPath() + "/answer.aac");
                                startTime = System.currentTimeMillis();
                                recordState = 1;
                                //↑开始录音
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastMaker.showShortToast("无法录音，请开启权限");
                            }
                        });
                        break;
                    case 1:
                        recordState = 2;
//                        tvAnswer.setText("点击试听");
                        llReplyRecord.setVisibility(View.VISIBLE);
                        llSubmit.setVisibility(View.VISIBLE);
                        recordStop();
                        endTime = System.currentTimeMillis();
                        //↑停止录音进入试听状态
                        break;
                    case 2:
                        AudioPlayManager.getInstance().playRecord(Util.getSDCardPath() + "/answer.aac");
                        break;
                }
                break;
            case R.id.cl_answer:
                clAnswer.setVisibility(View.GONE);
                recordState = 2;
//                        tvAnswer.setText("点击试听");
                llReplyRecord.setVisibility(View.VISIBLE);
                llSubmit.setVisibility(View.VISIBLE);
                imgPlayPause.setVisibility(View.VISIBLE);
                recordStop();
                endTime = System.currentTimeMillis();
                //↑停止录音进入试听状态

                break;
            case R.id.ll_re_record:
                AudioPlayManager.getInstance().release();
                Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.RECORD_AUDIO)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
//                        tvAnswer.setText("点击停止");
                        tvHint.setVisibility(View.GONE);
                        llReplyRecord.setVisibility(View.GONE);
                        llSubmit.setVisibility(View.GONE);
                        clAnswer.setVisibility(View.VISIBLE);
                        record(Util.getSDCardPath() + "/answer.aac");
                        startTime = System.currentTimeMillis();
                        recordState = 1;
                        //↑开始录音
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastMaker.showShortToast("无法录音，请开启权限");
                    }
                });
                break;
            case R.id.ll_submit:
                AudioPlayManager.getInstance().release();
                submitReplyAnswer();
                //↑上传录音
                break;
        }
    }

    /**
     * 追答
     */
    private void submitReplyAnswer() {
        showLoadingDialog("请稍候");
        HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

            @Override
            public void successResult(UploadResultEntity result) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("answerurl", result.getFileurl());
                map.put("kederid", keda.getKedaQuestionsDB().getId());
                long duration = endTime - startTime;
                int time = (int) (duration / 1000);
                map.put("taketime", time);
                ApiServerManager.getInstance().getApiServer().submitReplyAnswer(map).enqueue(new RetrofitCallBack<Object>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                        loadingDismiss();
                        if (response.body().getCode() == 200) {
                            Toast.makeText(mContext, "回答成功", Toast.LENGTH_SHORT).show();
                            toHttpGetDetail();
                        } else {
                            Toast.makeText(mContext, "已回答或回答失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
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

    private void submitAnswer() {
        showLoadingDialog("请稍候");
        HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

            @Override
            public void successResult(UploadResultEntity result) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("audiourl", result.getFileurl());
                map.put("questionid", keda.getKedaQuestionsDB().getId());
                map.put("name", result.getFileurl());
                long duration = endTime - startTime;
                int time = (int) (duration / 1000);
                map.put("taketime", time);
                HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.KEDA_ANSWER, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        loadingDismiss();
                        if (result.getCode() == 200) {
                            Toast.makeText(mContext, "回答成功", Toast.LENGTH_SHORT).show();
                            toHttpGetDetail();
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

    private void record(String path) {
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
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordStop() {
        try {
            recorder.setOnErrorListener(null);
            recorder.setPreviewDisplay(null);
            recorder.stop();// 停止刻录
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
        AnimationDrawable animationDrawable2 = (AnimationDrawable) imgReplyResponderPlay.getDrawable();
        switch (msg.getWhat()) {
            case SystemConstant.ON_PLAYING:
                if (TextUtils.equals(AudioPlayManager.getInstance().getmMediaPlayId(), "1111"))
                    animationDrawable.start();
                else
                    animationDrawable2.start();
                break;
            case SystemConstant.EAVEDROP_COMPLITE:
                if (TextUtils.equals(AudioPlayManager.getInstance().getmMediaPlayId(), "1111"))
                    animationDrawable.stop();
                else
                    animationDrawable2.stop();
                break;
            case SystemConstant.ON_PAUSE:
                if (TextUtils.equals(AudioPlayManager.getInstance().getmMediaPlayId(), "1111"))
                    animationDrawable.stop();
                else
                    animationDrawable2.stop();
                break;
            case SystemConstant.AUDIO_COMPLET:
                //录音播放完成
                if (imgBottomPlayPause.getVisibility() == View.VISIBLE) {
                    imgBottomPlayPause.setImageResource(R.drawable.icon_answer_play);
                } else if (imgPlayPause.getVisibility() == View.VISIBLE) {
                    imgPlayPause.setImageResource(R.drawable.icon_answer_play);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
        mHandler = null;
        EventBus.getDefault().unregister(this);
    }
}

