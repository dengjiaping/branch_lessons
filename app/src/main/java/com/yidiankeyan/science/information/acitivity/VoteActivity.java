package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MozActivityTeamAdapter;
import com.yidiankeyan.science.information.entity.VoteBean;
import com.yidiankeyan.science.information.entity.VoteResultBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.LoadMoreGridView;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 活动投票
 */
public class VoteActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrLayout;
    private LoadMoreGridView gvTeam;
    private MozActivityTeamAdapter teamAdapter;
    private List<VoteBean.PartsBean> mData = new ArrayList<>();
    private ImageView imgActivityAvatar;
    private AutoLinearLayout llAll;
    private View headView;
    private PopupWindow votePopupWindow;
    private int surplusTicket = 3;
    private TextView tvSurplusCount;
    private Button btnCancel;
    private View btnToLottery;
    private int pages = 1;
    private VoteBean voteBean;
    private TextView tvTime;

    @Override
    protected int setContentView() {
        return R.layout.activity_vote;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        gvTeam = (LoadMoreGridView) findViewById(R.id.gv_team);
        headView = LayoutInflater.from(this).inflate(R.layout.head_moz_activity_vote, null, false);
        imgActivityAvatar = (ImageView) headView.findViewById(R.id.img_activity_avatar);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        tvTime = (TextView) headView.findViewById(R.id.tv_time);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText("为国选材");
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) headView.findViewById(R.id.tv_activity_notes)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        ((TextView) headView.findViewById(R.id.tv_activity_notes)).getPaint().setAntiAlias(true);//抗锯齿
        headView.findViewById(R.id.tv_activity_notes).setOnClickListener(this);
        teamAdapter = new MozActivityTeamAdapter(this, mData);
        gvTeam.addHeaderView(headView);
        gvTeam.setAdapter(teamAdapter);
        gvTeam.setHasLoadMore(true);
        gvTeam.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetTeam();
            }
        });
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetTeam();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        teamAdapter.setOnVoteListener(new MozActivityTeamAdapter.OnVoteListener() {
            @Override
            public void onVote(int position) {
                if (!Util.hintLogin(VoteActivity.this)) {
                    return;
                }
                if (surplusTicket > 0) {
                    toHttpVote(position);
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "您的票用完了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        teamAdapter.setOnToTeamDetailListener(new MozActivityTeamAdapter.OnToTeamDetailListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, TeamIntroductionActivity.class);
                intent.putExtra("id", mData.get(position).getEnrolid());
                intent.putExtra("voteid", mData.get(position).getVoteid());
                intent.putExtra("refid", mData.get(position).getRefid());
                if (voteBean.getInfo() != null)
                    intent.putExtra("lotteryId", voteBean.getInfo().getLotteryid() + "");
                mContext.startActivity(intent);
            }
        });
        long startTime = getIntent().getLongExtra("startTime", 0);
        long endTime = getIntent().getLongExtra("endTime", 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd");
        tvTime.setText(simpleDateFormat.format(new Date(startTime)) + "-" + simpleDateFormat.format(new Date(endTime)));
    }

    /**
     * 投票
     *
     * @param position
     */
    private void toHttpVote(int position) {
        showLoadingDialog("请稍等");
        Map<String, Object> map = new HashMap<>();
        map.put("voteid", mData.get(position).getVoteid());
        map.put("refid", mData.get(position).getRefid());
        map.put("enrolid", mData.get(position).getEnrolid());
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
                    Util.finishPop(VoteActivity.this, votePopupWindow);
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

    private void toHttpGetTeam() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_VOTE_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    voteBean = (VoteBean) GsonUtils.json2Bean(jsonData, VoteBean.class);
                    if (voteBean.getInfo() != null) {
//                        ((TextView) findViewById(R.id.maintitle_txt)).setText(voteBean.getInfo().getName());
                        Glide.with(VoteActivity.this).load(Util.getImgUrl(voteBean.getInfo().getCoverimgurl())).into(imgActivityAvatar);
                    }
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (voteBean.getParts().size() > 0) {
                        gvTeam.setHasLoadMore(true);
                        mData.addAll(voteBean.getParts());
                        pages++;
                    } else {
                        gvTeam.setHasLoadMore(false);
                    }
                    teamAdapter.notifyDataSetChanged();
                } else {
                    gvTeam.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                gvTeam.showFailUI();
                ptrLayout.onRefreshComplete();
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
            case R.id.btn_cancel:
                Util.finishPop(VoteActivity.this, votePopupWindow);
                break;
            case R.id.btn_to_lottery:
                Util.finishPop(VoteActivity.this, votePopupWindow);
                intent = new Intent(this, LotteryActivity.class);
                if (voteBean != null && voteBean.getInfo() != null)
                    intent.putExtra("id", voteBean.getInfo().getLotteryid() + "");
                startActivity(intent);
                break;
            case R.id.tv_activity_notes:
                intent = new Intent(this, MozActivityNotesActivity.class);
                if (voteBean != null && voteBean.getInfo() != null)
                    intent.putExtra("id", voteBean.getInfo().getLotteryid() + "");
                startActivity(intent);
                break;
        }
    }

}
