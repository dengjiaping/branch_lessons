package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.entity.TeamDetailBean;
import com.yidiankeyan.science.information.entity.VoteResultBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.JCVideoPlayerStandardShowTitleAfterFullscreen;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 团队介绍
 */
public class TeamIntroductionActivity extends BaseActivity {

    private JCVideoPlayerStandardShowTitleAfterFullscreen videoPlayer;
    private TextView tvTeamDec;
    private ImageView imgCover;
    private Button btnVote;
    private PopupWindow votePopupWindow;
    private int surplusTicket = 3;
    private Button btnCancel;
    private AutoLinearLayout llAll;
    private View btnToLottery;
    private TextView tvSurplusCount;
    private AutoLinearLayout llAudioMore;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private AutoLinearLayout imgShareSina;
    private AutoLinearLayout imgShareQQ;
    private TextView btnShapeCancel;
    private TeamDetailBean teamDetailBean;

    @Override
    protected int setContentView() {
        return R.layout.activity_team_introduction;
    }

    @Override
    protected void initView() {
        videoPlayer = (JCVideoPlayerStandardShowTitleAfterFullscreen) findViewById(R.id.video_player);
        tvTeamDec = (TextView) findViewById(R.id.tv_team_dec);
        imgCover = (ImageView) findViewById(R.id.img_cover);
        btnVote = (Button) findViewById(R.id.btn_vote);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("队伍信息");
        btnVote.setOnClickListener(this);
        llAudioMore.setOnClickListener(this);
        llAudioMore.setVisibility(View.VISIBLE);
        llAudioMore.setEnabled(false);
//        videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + "/static/upload/cmsweb/video/mp42016/10/20/96c174b906424b1cbfcbd066b5254968.mp4", "团队介绍");
//        videoPlayer.post(new Runnable() {
//            @Override
//            public void run() {
//                videoPlayer.startButtonLogic();
//            }
//        });
        toHttpGetTeamDetail();
    }

    private void toHttpGetTeamDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("enrolid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_TEAM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    teamDetailBean = (TeamDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), TeamDetailBean.class);
                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "团队介绍");
//                    videoPlayer.setUp(SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(), "团队介绍");
                    videoPlayer.startButton.performClick();
                    tvTeamDec.setText(teamDetailBean.getContent());
                    Glide.with(TeamIntroductionActivity.this).load(Util.getImgUrl(teamDetailBean.getCoverimgurl())).into(imgCover);
                    llAudioMore.setEnabled(true);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.btn_vote:
                if (!Util.hintLogin(this))
                    return;
                if (surplusTicket > 0) {
                    toHttpVote();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "您的票用完了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:
                Util.finishPop(TeamIntroductionActivity.this, votePopupWindow);
                break;
            case R.id.btn_to_lottery:
                Util.finishPop(TeamIntroductionActivity.this, votePopupWindow);
                intent = new Intent(this, LotteryActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("lotteryId"));
                startActivity(intent);
                break;
            case R.id.ll_audio_more:
                showSharePop();
                break;
        }
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnShapeCancel = (TextView) view.findViewById(R.id.btn_cancel);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(TeamIntroductionActivity.this, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(
                        SHARE_MEDIA.WEIXIN,
                        "墨子活动",
                        teamDetailBean.getName(),
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(),
                        SystemConstant.MYURL + "enrol/info/" + getIntent().getStringExtra("id"),
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getCoverimgurl()
                        , null);
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        teamDetailBean.getName(),
                        "墨子活动",
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(),
                        SystemConstant.MYURL + "enrol/info/" + getIntent().getStringExtra("id"),
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getCoverimgurl()
                        , null);
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(
                        SHARE_MEDIA.QQ,
                        "墨子活动",
                        teamDetailBean.getName(),
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(),
                        SystemConstant.MYURL + "enrol/info/" + getIntent().getStringExtra("id"),
                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getCoverimgurl()
                        , null);
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareVideo(
//                        SHARE_MEDIA.SINA,
//                        "",
//                        teamDetailBean.getName(),
//                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getVediourl(),
//                        SystemConstant.MYURL + "enrol/info/" + getIntent().getStringExtra("id"),
//                        SystemConstant.ACCESS_IMG_HOST + teamDetailBean.getCoverimgurl()
//                        , null);
//            }
//        });
        btnShapeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.finishPop(TeamIntroductionActivity.this, sharePopupWindow);
            }
        });
    }

    /**
     * 投票
     */
    private void toHttpVote() {
        showLoadingDialog("请稍等");
        Map<String, Object> map = new HashMap<>();
        map.put("voteid", getIntent().getStringExtra("voteid"));
        map.put("refid", getIntent().getStringExtra("refid"));
        map.put("enrolid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.MOZ_ACTIVITY_VOTE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    VoteResultBean voteResultBean = (VoteResultBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), VoteResultBean.class);
                    surplusTicket = voteResultBean.getResidueNumber();
                    if (surplusTicket >= 0 && "success".equals(voteResultBean.getResult())) {
                        showVotePopupWindow();
                    } else {
                        Toast.makeText(DemoApplication.applicationContext, "您的票用完了", Toast.LENGTH_SHORT).show();
                    }
                    loadingDismiss();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    private void showVotePopupWindow() {
        if (votePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_vote, null);
            tvSurplusCount = (TextView) view.findViewById(R.id.tv_surplus_count);
            btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            btnToLottery = view.findViewById(R.id.btn_to_lottery);
            votePopupWindow = new PopupWindow(view, -2, -2);
            votePopupWindow.setContentView(view);
            votePopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            votePopupWindow.setFocusable(true);
            votePopupWindow.setOutsideTouchable(true);
            votePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            votePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(TeamIntroductionActivity.this, votePopupWindow);
                }
            });
            btnCancel.setOnClickListener(this);
            btnToLottery.setOnClickListener(this);
            votePopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            votePopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        }
        tvSurplusCount.setText(surplusTicket + "");
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
}
