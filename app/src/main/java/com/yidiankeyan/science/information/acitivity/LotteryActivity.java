package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.WinnersAdapter;
import com.yidiankeyan.science.information.entity.Prize;
import com.yidiankeyan.science.information.entity.WinnerBean;
import com.yidiankeyan.science.utils.BitmapUtils;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.LotteryView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LotteryActivity extends BaseActivity {

    private LotteryView lotteryView;
    private int surplusCount;
    private ListView lvWinners;
    private WinnersAdapter winnerAdapter;
    private List<WinnerBean> winners = new ArrayList<>();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private AutoLinearLayout llAll;
    private double lotteryViewWidth;
    private PopupWindow mPopupWindow;
    private PopupWindow sharePopupWindow;
    private Button btnResult;
    private Button btnMyPrize;
    private List<Prize> prizes = new ArrayList<>();
    private List<Integer> emptyPrize = new ArrayList<>();
    private ImageView imgResult;
    private TextView tvResult;
    private ImageView imgHorseRaceLamp;
    private AnimationDrawable animationDrawable;
    private int position;
    private ImageButton titleBtn;
    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private AutoLinearLayout imgShareSina;
    private AutoLinearLayout imgShareQQ;
    private TextView btnCancel;

    private AutoRelativeLayout rlLotteryBg;
    private ImageView imgTextLottery;
    private ImageView imgWinningRecord;
    private ImageView imgBottom;

    private boolean canLottery;
    private boolean lotteryFail;
    private String prizeId = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lvWinners.smoothScrollBy(1, 0);
        }
    };

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_lottery;
    }

    @Override
    protected void initView() {
        lotteryView = (LotteryView) findViewById(R.id.lottery_view);
        lvWinners = (ListView) findViewById(R.id.lv_winners);
        lotteryViewWidth = SystemConstant.ScreenWidth * (500f / 750);
        imgHorseRaceLamp = (ImageView) findViewById(R.id.img_horse_race_lamp);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        btnMyPrize = (Button) findViewById(R.id.btn_my_prize);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        rlLotteryBg = (AutoRelativeLayout) findViewById(R.id.rl_lottery_bg);
        imgTextLottery = (ImageView) findViewById(R.id.img_text_lottery);
        imgWinningRecord = (ImageView) findViewById(R.id.img_winning_record);
        imgBottom = (ImageView) findViewById(R.id.img_bottom);
    }

    @Override
    protected void initAction() {
        rlLotteryBg.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.readBitMap(this, R.drawable.activity_lottery_layout_bg)));
        imgTextLottery.setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.text_lottery));
        imgWinningRecord.setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.winning_record));
        imgBottom.setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.activity_lottery_bottom_bg));
        titleBtn.setImageResource(R.drawable.icon_subsribe_more);
        findViewById(R.id.ll_return).setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("为国选材赢大奖");
        animationDrawable = (AnimationDrawable) imgHorseRaceLamp.getDrawable();
        animationDrawable.start();
        btnMyPrize.setOnClickListener(this);
        titleBtn.setOnClickListener(this);
        titleBtn.setVisibility(View.VISIBLE);
        lotteryView.setPrizes(prizes);
        lotteryView.setSurplus(surplusCount);
        //点击抽奖回调
        lotteryView.setOnLotteryClickListener(new LotteryView.OnLotteryClickListener() {
            @Override
            public boolean onClick() {
                if (!Util.hintLogin(LotteryActivity.this)) {
                    return false;
                }
//                if (surplusCount > 0) {
//                    if (canLottery) {
//                        surplusCount--;
//                        toHttpLottery();
//                        return true;
//                    } else {
//                        Toast.makeText(DemoApplication.applicationContext, "抽奖失败", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                } else {
//                    Toast.makeText(DemoApplication.applicationContext, "不好意思，您的机会已用完", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
                if (surplusCount > 0 && canLottery) {
//                if (true) {
                    surplusCount--;
                    prizeId = "";
                    Log.e("抽奖结果", "第一步，点击抽奖");
                    toHttpLottery();
                    return true;
                } else if (!canLottery) {
                    Toast.makeText(DemoApplication.applicationContext, "抽奖失败", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "不好意思，您的机会已用完", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        //抽奖结束回调
        lotteryView.setOnTransferWinningListener(new LotteryView.OnTransferWinningListener() {

            @Override
            public void onWinning(int position) {
                if (lotteryFail) {
                    Toast.makeText(DemoApplication.applicationContext, "网络错误", Toast.LENGTH_SHORT).show();
                    surplusCount++;
                    lotteryFail = false;
                    return;
                }
                Log.e("抽奖结果", "第四步，抽奖完成后回调，position==" + position);
                showCustomPop(position);
                LotteryActivity.this.position = position;
                lotteryView.setSurplus(surplusCount);
            }
        });

        winnerAdapter = new WinnersAdapter(winners, this);
        lvWinners.setAdapter(winnerAdapter);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 50);
        toHttpGetWinnerList();
        toHttpGetPrizeList();
        toHttpGetLotteryCount();
    }

    private void toHttpGetLotteryCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_LOTTERY_COUNT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Double count = (Double) result.getData();
                    surplusCount = count.intValue();
                    lotteryView.setSurplus(surplusCount);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 抽奖
     */
    private void toHttpLottery() {
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.LOTTERY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                lotteryFail = false;
                if (result.getCode() == 200) {
                    prizeId = (String) result.getData();
                }
                Log.e("抽奖结果", "第二步，抽奖接口调用完毕返回奖品id==" + prizeId);
                if (TextUtils.isEmpty(prizeId)) {
                    Random r = new Random();
                    int i = r.nextInt(emptyPrize.size());
                    Log.e("抽奖结果", "第三部， 设置停下位置==" + emptyPrize.get(i));
                    lotteryView.setStopPosition(emptyPrize.get(i));
                } else {
                    boolean flag = true;
                    for (int i = 0; i < prizes.size(); i++) {
                        if (TextUtils.equals(prizeId, prizes.get(i).getId())) {
                            flag = false;
                            lotteryView.setStopPosition(i);
                        }
                    }
                    if (flag) {
                        Random r = new Random();
                        int i = r.nextInt(emptyPrize.size());
                        lotteryView.setStopPosition(emptyPrize.get(i));
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lotteryFail = true;
                if (emptyPrize.size() > 0) {
                    Random r = new Random();
                    int i = r.nextInt(emptyPrize.size());
                    lotteryView.setStopPosition(emptyPrize.get(i));
                } else {
                    lotteryView.setStopPosition(0);
                }
//                String prizeId = "";
//                if (TextUtils.isEmpty(prizeId)) {
//                    Random r = new Random();
//                    int i = r.nextInt(emptyPrize.size());
//                    lotteryView.setStopPosition(emptyPrize.get(i));
//                } else {
//                    for (int i = 0; i < prizes.size(); i++) {
//                        if (TextUtils.equals(prizeId, prizes.get(i).getId()))
//                            lotteryView.setStopPosition(i);
//                    }
//                }
            }
        }, true);
    }

    /**
     * 获取奖品列表
     */
    private void toHttpGetPrizeList() {
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_PRIZE_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    prizes.removeAll(prizes);
                    List<Prize> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), Prize.class);
                    if (data.size() > 0)
                        canLottery = true;
                    else
                        canLottery = false;
                    prizes.addAll(data);
                    prizes.add(4, new Prize());
                    emptyPrize.removeAll(emptyPrize);
                    for (int i = 0; i < prizes.size(); i++) {
                        if (TextUtils.isEmpty(prizes.get(i).getId()) && i != 4) {
                            emptyPrize.add(i);
                        }
                    }
                    if (emptyPrize.size() > 0)
                        canLottery = true;
                    else
                        canLottery = false;
                    lotteryView.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                canLottery = false;
                LogUtils.e("errorResult");
            }
        }, true);
    }

    private void toHttpGetWinnerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("lotteryid", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_WINNER_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<WinnerBean> winnerBeanList = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), WinnerBean.class);
                    winners.addAll(winnerBeanList);
                    winnerAdapter.notifyDataSetChanged();
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
            case R.id.btn_result:
                Util.finishPop(LotteryActivity.this, mPopupWindow);
                if ((boolean) v.getTag()) {
                    startActivity(new Intent(this, MyPrizeActivity.class));
                } else {
                    lotteryView.lotteryClick();
                }
                break;
            case R.id.btn_my_prize:
                if (!Util.hintLogin(this))
                    return;
                startActivity(new Intent(this, MyPrizeActivity.class));
                break;
            case R.id.title_btn:
                showSharePop();
                break;
            case R.id.btn_cancel:
                Util.finishPop(this, sharePopupWindow);
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
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
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
            btnCancel.setOnClickListener(this);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(LotteryActivity.this, sharePopupWindow);
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
                shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        "墨子快报",
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        "墨子快报",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.QQ,
                        "墨子快报",
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareWeb(
//                        SHARE_MEDIA.SINA,
//                        "",
//                        "墨子杯密码数学挑战赛，幸运抽奖",
//                        null,
//                        SystemConstant.MYURL + "view/appshare/index.jsp"
//                        , null);
//            }
//        });
    }

    private void showCustomPop(int position) {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_lottery_result, null);
            imgResult = (ImageView) view.findViewById(R.id.img_result);
            tvResult = (TextView) view.findViewById(R.id.tv_result);
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            btnResult = (Button) view.findViewById(R.id.btn_result);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            btnResult.setOnClickListener(this);
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(LotteryActivity.this, mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        }
        if (TextUtils.isEmpty(prizes.get(position).getId())) {
            imgResult.setImageResource(R.drawable.text_not_winning);
            tvResult.setText("你的运气差一点点");
            btnResult.setText("再来一次");
            btnResult.setTag(false);
            Log.e("抽奖结果", "第五步，抽奖结果未中奖" + position);
//            Util.finishPop(this, mPopupWindow);
//            lotteryView.lotteryPerformClick();
//            btnResult.performClick();
        } else {
            imgResult.setImageResource(R.drawable.text_winning);
            tvResult.setText("人品爆发抽中" + prizes.get(position).getName());
            btnResult.setText("领取奖品");
            btnResult.setTag(true);
            Log.e("抽奖结果", "第五步， 抽奖结果中奖======================================================================" + position);
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_USER_INFORMATION:
                toHttpGetLotteryCount();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            lotteryView.setStartisRunning(false);
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        } catch (Exception e) {

        }
    }

}
