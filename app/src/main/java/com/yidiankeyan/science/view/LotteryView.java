package com.yidiankeyan.science.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.Prize;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/11/15.
 * 作用：
 */
public class LotteryView extends AutoLinearLayout {

    private ImageView img0;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView imgLottery;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private List<ImageView> prizeImg = new ArrayList<>();
    private boolean canStop;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRunning)
                        drawTransfer();
                    break;
                case 1:
                    if (lottery == current) {
                        countDown = 0;
                        count = 0;
                        isRunning = false;
                        Log.e("抽奖结果", current + "");
                        listener.onWinning(current);
                        lottery = -1;
                        lotteryCount++;
                        Log.e("抽奖结果", "当前抽奖次数：" + lotteryCount);
                    }
                    break;
            }
        }
    };
    private int surplus;
    private int stopPosition;
    private int lotteryCount = 0;

    public LotteryView(Context context) {
        super(context);
    }

    public LotteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_lottery, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        img0 = (ImageView) findViewById(R.id.img_0);
        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        imgLottery = (ImageView) findViewById(R.id.img_lottery);
        img5 = (ImageView) findViewById(R.id.img_5);
        img6 = (ImageView) findViewById(R.id.img_6);
        img7 = (ImageView) findViewById(R.id.img_7);
        img8 = (ImageView) findViewById(R.id.img_8);
        prizeImg.add(img0);
        prizeImg.add(img1);
        prizeImg.add(img2);
        prizeImg.add(img3);
        prizeImg.add(imgLottery);
        prizeImg.add(img5);
        prizeImg.add(img6);
        prizeImg.add(img7);
        prizeImg.add(img8);
        imgLottery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    if (onLotteryClickListener != null) {
                        boolean canRun = onLotteryClickListener.onClick();
                        if (!canRun)
                            return;
                    }
                    setStartisRunning(true);
                    start();
                }
            }
        });
    }

    public void notifyDataSetChanged() {
        for (int i = 0; i < prizes.size(); i++) {
            if (i != 4)
                Glide.with(getContext()).load(Util.getImgUrl(prizes.get(i).getImgurl())).into(prizeImg.get(i));
        }
    }

    private List<Prize> prizes;
    /**
     * 是否正在转动
     */
    private boolean isRunning;

    private int lottery = -1; //设置中奖号码

    private int current = 2; //抽奖开始的位置

    private int count = 0; //旋转次数累计

    private int countDown; //倒计次数，快速旋转完成后，需要倒计多少次循环才停止

    private boolean isStarted = false;

    private int transfer = Color.parseColor("#FCE300");

    private int MAX = 50; //最大旋转次数

    private OnTransferWinningListener listener;
    /**
     * 点击抽奖按钮监听
     */
    private OnLotteryClickListener onLotteryClickListener;

    public void setOnTransferWinningListener(OnTransferWinningListener listener) {
        this.listener = listener;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
        switch (surplus) {
            case 0:
                imgLottery.setImageResource(R.drawable.icon_lottery_surplus0);
                break;
            case 1:
                imgLottery.setImageResource(R.drawable.icon_lottery_surplus1);
                break;
            case 2:
                imgLottery.setImageResource(R.drawable.icon_lottery_surplus2);
                break;
            case 3:
                imgLottery.setImageResource(R.drawable.icon_lottery_surplus3);
                break;
        }
    }

    public void setStopPosition(int stopPosition) {
        canStop = true;
        this.lottery = stopPosition;
        this.stopPosition = stopPosition;
    }

    public void lotteryPerformClick() {
        imgLottery.performClick();
    }

    public interface OnTransferWinningListener {
        /**
         * 中奖回调
         *
         * @param position
         */
        void onWinning(int position);
    }


    /**
     * 设置奖品集合
     *
     * @param prizes
     */
    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
        notifyDataSetChanged();
    }

    public void setOnLotteryClickListener(OnLotteryClickListener onLotteryClickListener) {
        this.onLotteryClickListener = onLotteryClickListener;
    }

    private class SurfaceRunnable implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                mHandler.sendEmptyMessage(0);
                controllerTransfer();
            }
        }
    }

    //绘制旋转的方块
    @SuppressLint("NewApi")
    private void drawTransfer() {
        isStarted = true;
        int len = (int) Math.sqrt(9);
//        prizeImg.get(current).setBackgroundColor(getResources().getColor(R.color.white));
        prizeImg.get(current).setBackground(getResources().getDrawable(R.drawable.shape_lottery_prize_normal_bg));
        current = next(current, len);
        prizeImg.get(current).setBackground(getResources().getDrawable(R.drawable.shape_lottery_prize_pressed_bg));
    }

    public void lotteryClick() {
        if (!isRunning) {
            if (onLotteryClickListener != null) {
                boolean canRun = onLotteryClickListener.onClick();
                if (!canRun)
                    return;
            }
            setStartisRunning(true);
            start();
        }
    }

    //绘制旋转的方块
    private void drawTransfer(Canvas canvas, int position) {
        int width = getMeasuredWidth() / 3;
        int x1;
        int y1;

        int x2;
        int y2;
        int len = (int) Math.sqrt(9);
        x1 = getPaddingLeft() + width * (Math.abs(position) % len);
        y1 = getPaddingTop() + width * ((position) / len);

        x2 = x1 + width;
        y2 = y1 + width;

        Paint paint = new Paint();
        paint.setColor(transfer);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectF rectF = new RectF(x1 + width / 32, y1 + width / 32, x2 - width / 32, y2 - width / 32);
        canvas.drawRoundRect(rectF, 10, 10, paint);
    }

    //控制旋转
    private void controllerTransfer() {
        if (count > MAX && canStop) {
            countDown++;
            SystemClock.sleep(count * 5);
        } else {
            SystemClock.sleep(count * 2);
        }

        count++;
        if (countDown > 2) {
            if (lottery == current) {
                Log.e("抽奖结果", "lottery == " + lottery + ",current == " + current);

                if (listener != null) {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }
    }

    public void setStartisRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void start() {
        canStop = false;
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new SurfaceRunnable());
    }

    //下一步
    public int next(int position, int len) {
        int current = position;
        if (current + 1 < len) {
            return ++current;
        }

        if ((current + 1) % len == 0 & current < len * len - 1) {
            return current += len;
        }

        if (current % len == 0) {
            return current -= len;
        }

        if (current < len * len) {
            return --current;
        }

        return current;
    }

    /**
     * 重新测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnLotteryClickListener {
        boolean onClick();
    }

}
