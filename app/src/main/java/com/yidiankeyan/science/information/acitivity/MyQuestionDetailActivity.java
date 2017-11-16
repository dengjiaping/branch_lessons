package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.EavedropMediaBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class MyQuestionDetailActivity extends BaseActivity {

    private ImageView imgMakeAvatar;
    private TextView tvMakeName;
    private TextView tvMakeContent;
    private ImageView imgSolverAvatar;
    private TextView tvSolverName;
    private TextView tvSolverProfession;
    private TextView btnRequestion;
    private AutoRelativeLayout imgResponderEavedrop;
    private TextView tvPermission;
    private ImageView imgResponderPlay;
    private TextView tvResponderLength;
    private TextView tvEavesdropNum;
    private TextView tvIncome;
    private ImageView imgMakeRequestionAvatar;
    private TextView tvMakeRequestionName;
    private TextView tvMakeRequestionContent;
    private ImageView imgRequestionSolverAvatar;
    private TextView tvRequestionSolverName;
    private TextView tvRequestionSolverProfession;
    private AutoRelativeLayout imgRequestionResponderEavedrop;
    private TextView tvRequestionPermission;
    private ImageView imgRequestionResponderPlay;
    private TextView tvRequestionResponderLength;
    private AutoLinearLayout llRequestion;
    private AutoLinearLayout llReanswer;
    private NewScienceHelp keda;
    private TextView tvPrice;
    private TextView tvCreateTime;
    private View lineNoAnswer;
    private TextView tvIntoAnswerDetail;
    private AutoLinearLayout llAnswer;
    private AutoLinearLayout tvSolverInfo;
    private static final int INTO_REQUEST_QUESTION = 101;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_question_detail;
    }

    @Override
    protected void initView() {
        imgMakeAvatar = (ImageView) findViewById(R.id.img_make_avatar);
        tvMakeName = (TextView) findViewById(R.id.tv_make_name);
        tvMakeContent = (TextView) findViewById(R.id.tv_make_content);
        imgSolverAvatar = (ImageView) findViewById(R.id.img_solver_avatar);
        tvSolverName = (TextView) findViewById(R.id.tv_solver_name);
        tvSolverProfession = (TextView) findViewById(R.id.tv_solver_profession);
        btnRequestion = (TextView) findViewById(R.id.btn_requestion);
        imgResponderEavedrop = (AutoRelativeLayout) findViewById(R.id.img_responder_eavedrop);
        tvPermission = (TextView) findViewById(R.id.tv_permission);
        imgResponderPlay = (ImageView) findViewById(R.id.img_responder_play);
        tvResponderLength = (TextView) findViewById(R.id.tv_responder_length);
        tvEavesdropNum = (TextView) findViewById(R.id.tv_eavesdrop_num);
        tvIncome = (TextView) findViewById(R.id.tv_income);
        imgMakeRequestionAvatar = (ImageView) findViewById(R.id.img_make_requestion_avatar);
        tvMakeRequestionName = (TextView) findViewById(R.id.tv_make_requestion_name);
        tvMakeRequestionContent = (TextView) findViewById(R.id.tv_make_requestion_content);
        imgRequestionSolverAvatar = (ImageView) findViewById(R.id.img_requestion_solver_avatar);
        tvRequestionSolverName = (TextView) findViewById(R.id.tv_requestion_solver_name);
        tvRequestionSolverProfession = (TextView) findViewById(R.id.tv_requestion_solver_profession);
        imgRequestionResponderEavedrop = (AutoRelativeLayout) findViewById(R.id.img_requestion_responder_eavedrop);
        tvRequestionPermission = (TextView) findViewById(R.id.tv_requestion_permission);
        imgRequestionResponderPlay = (ImageView) findViewById(R.id.img_requestion_responder_play);
        tvRequestionResponderLength = (TextView) findViewById(R.id.tv_requestion_responder_length);
        llRequestion = (AutoLinearLayout) findViewById(R.id.ll_requestion);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        llReanswer = (AutoLinearLayout) findViewById(R.id.ll_reanswer);
        llAnswer = (AutoLinearLayout) findViewById(R.id.ll_answer);
        tvCreateTime = (TextView) findViewById(R.id.tv_create_time);
        lineNoAnswer = (View) findViewById(R.id.line_no_answer);
        tvIntoAnswerDetail = (TextView) findViewById(R.id.tv_into_answer_detail);
        tvSolverInfo = (AutoLinearLayout) findViewById(R.id.tv_solver_info);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        imgResponderEavedrop.setOnClickListener(this);
        tvIntoAnswerDetail.setOnClickListener(this);
        btnRequestion.setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("问题详情");
        imgRequestionResponderEavedrop.setOnClickListener(this);
        toHttpGetDetail();
    }

    private void toHttpGetDetail() {
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
        tvMakeContent.setText(keda.getKedaQuestionsDB().getQuestions());
        tvPrice.setText("¥" + keda.getKedaQuestionsDB().getAmount());
        if (keda.getKedaQuestionsDB().getIsanswer() == 0) {
            //还没有回答
            lineNoAnswer.setVisibility(View.VISIBLE);
            tvIntoAnswerDetail.setVisibility(View.VISIBLE);
            tvSolverName.setText(keda.getSolver().getName());
            tvSolverProfession.setText(keda.getSolver().getProfession());
            tvIntoAnswerDetail.setText(keda.getSolver().getName() + "的全部回答>");
            Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).into(imgSolverAvatar);
        } else {
            //已回答
            btnRequestion.setVisibility(View.VISIBLE);
            imgResponderEavedrop.setVisibility(View.VISIBLE);
            tvSolverInfo.setVisibility(View.VISIBLE);
            Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).into(imgSolverAvatar);
            tvSolverName.setText(keda.getSolver().getName());
            tvSolverProfession.setText(keda.getSolver().getProfession());
            tvPermission.setText(keda.getKederInfo().getPermission() == 1 ? "点击播放" : "一墨子币偷听");
            tvResponderLength.setText(TimeUtils.getAnswerLength(keda.getKederDB().getTaketime()) + "");
            tvEavesdropNum.setText(keda.getKederDB().getEavesdropnum() + "");
            tvIncome.setText("¥" + keda.getKederInfo().getProfit());
        }
        if (keda.getKedaReQuestionDB() == null) {
            //没有追问过
            btnRequestion.setEnabled(true);
            llRequestion.setVisibility(View.GONE);
            llReanswer.setVisibility(View.GONE);
            tvMakeRequestionContent.setVisibility(View.GONE);
        } else {
            btnRequestion.setEnabled(false);
            btnRequestion.setText("已追问");
            llRequestion.setVisibility(View.VISIBLE);
            tvMakeRequestionContent.setVisibility(View.VISIBLE);
            Glide.with(this).load(Util.getImgUrl(keda.getMaker().getCoverimg())).into(imgMakeRequestionAvatar);
            tvMakeRequestionName.setText(keda.getMaker().getName());
            tvMakeRequestionContent.setText(keda.getKedaReQuestionDB().getQuestion());
            if (TextUtils.isEmpty(keda.getKedaReQuestionDB().getAnswerurl())) {
                //追问了但未回答
                llReanswer.setVisibility(View.GONE);
            } else {
                llReanswer.setVisibility(View.VISIBLE);
                Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).into(imgRequestionSolverAvatar);
                tvRequestionSolverName.setText(keda.getSolver().getName());
                tvRequestionSolverProfession.setText(keda.getSolver().getProfession());
                tvRequestionPermission.setText(keda.getKederInfo().getPermission() == 1 ? "点击播放" : "一墨子币偷听");
                tvRequestionResponderLength.setText(TimeUtils.getAnswerLength(keda.getKedaReQuestionDB().getTaketime()) + "");
//                tvRequestionResponderLength.setText(TimeUtils.getAnswerLength(keda.getKedaReQuestionDB().getTaketime()) + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_responder_eavedrop:
                if (!Util.hintLogin(MyQuestionDetailActivity.this))
                    return;
                toHttpEavedrop(keda.getKederDB().getId());
                AnimationDrawable animationDrawable2 = (AnimationDrawable) imgRequestionResponderPlay.getDrawable();
                animationDrawable2.stop();
                //↑播放录音
                break;
            case R.id.tv_into_answer_detail:
                if (keda == null)
                    return;
                Intent intent = new Intent(this, AnswerTopDetailActivity.class);
                intent.putExtra("id", keda.getSolver().getId());
                startActivity(intent);
                break;
            case R.id.btn_requestion:
                Intent intentRequest = new Intent(this, RequestQuestionActivity.class);
                intentRequest.putExtra("id", keda.getKederDB().getId());
                startActivityForResult(intentRequest, INTO_REQUEST_QUESTION);
                break;
            case R.id.img_requestion_responder_eavedrop:
                if (keda.getKedaReQuestionDB().getAnswerurl().startsWith("/"))
                    AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + keda.getKedaReQuestionDB().getAnswerurl());
                else
                    AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + keda.getKedaReQuestionDB().getAnswerurl());
                AudioPlayManager.getInstance().setmMediaPlayId("2222");
                AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
                animationDrawable.stop();
                //↑播放追答录音
                break;
            case R.id.ll_return:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTO_REQUEST_QUESTION:
                    toHttpGetDetail();
                    break;
            }
        }
    }

    /**
     * 获取资源
     *
     * @param id
     */
    public void toHttpEavedrop(final String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("kederid", id);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_EAVEDROP_MEDIA, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    EavedropMediaBean eavedrop = (EavedropMediaBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), EavedropMediaBean.class);
                    if (eavedrop.getAudiourl().startsWith("/"))
                        AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + eavedrop.getAudiourl());
                    else
                        AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + eavedrop.getAudiourl());
                    AudioPlayManager.getInstance().setmMediaPlayId("1111");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof MyQuestionDetailActivity))
            return;
        AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
        AnimationDrawable animationDrawable2 = (AnimationDrawable) imgRequestionResponderPlay.getDrawable();
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AudioPlayManager.getInstance().release();
    }
}
