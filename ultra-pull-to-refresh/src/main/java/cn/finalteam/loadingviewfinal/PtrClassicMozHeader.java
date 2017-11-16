package cn.finalteam.loadingviewfinal;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Field;

import cn.finalteam.loadingviewfinal.indicator.PtrIndicator;
import cn.finalteam.loadingviewfinal.uptr.R;


public class PtrClassicMozHeader extends FrameLayout implements PtrUIHandler {

    /**
     * 1:准备刷新<p>
     * 2:正在刷新<p>
     * 3:刷新完成<p>
     */
    private int currentState;
    private ImageView imgPull;
    private ImageView imgLoading;
    private ImageView imgComplete;
    private AnimationDrawable animationDrawable;

    public PtrClassicMozHeader(Context context) {
        super(context);
        initViews(null);
    }

    public PtrClassicMozHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PtrClassicMozHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_loading, this);
        imgPull = (ImageView) header.findViewById(R.id.img_pull);
        imgLoading = (ImageView) header.findViewById(R.id.img_loading);
        imgComplete = (ImageView) header.findViewById(R.id.img_complete);
        animationDrawable = (AnimationDrawable) imgLoading.getDrawable();
        animationDrawable.start();
        resetView();
    }

    private void resetView() {
        imgPull.setVisibility(INVISIBLE);
        imgLoading.setVisibility(INVISIBLE);
        imgComplete.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        currentState = 0;
        imgLoading.setVisibility(INVISIBLE);
        imgComplete.setVisibility(INVISIBLE);
        imgPull.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        currentState = 1;
        imgLoading.setVisibility(VISIBLE);
        imgComplete.setVisibility(INVISIBLE);
        imgPull.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        currentState = 2;
        imgLoading.setVisibility(INVISIBLE);
        imgComplete.setVisibility(VISIBLE);
        imgPull.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
//        Log.e("mOffsetToRefresh", "====" + mOffsetToRefresh);
//        Log.e("currentPos", "====" + currentPos);
//        Log.e("lastPos", "===="+lastPos);

        if (currentState == 0) {
            double d = (currentPos * 1.0) / mOffsetToRefresh;
            int i = (int) (d * 30);
            if (i == 0)
                i++;
            if (i >= 30) {
                imgPull.setImageResource(R.drawable.xiala30);
            } else {
                imgPull.setImageResource(getResId("xiala" + i, R.drawable.class));
            }
        } else if (currentState == 2) {
            double d = (currentPos * 1.0) / mOffsetToRefresh;
            int i = (int) (d * 55);
            if (i == 0)
                i++;
            if (i >= 55) {
                imgComplete.setImageResource(R.drawable.letitgo_55);
            } else {
                imgComplete.setImageResource(getResId("letitgo_" + i, R.drawable.class));
            }
        }

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            //到达下拉刷新的临界点
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            //到达可释放刷新的临界点
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 可以进行刷新
     *
     * @param frame
     */
    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            imgLoading.setVisibility(VISIBLE);
            imgComplete.setVisibility(INVISIBLE);
            imgPull.setVisibility(INVISIBLE);
        }
    }

    /**
     * 由可刷新状态进入下拉刷新
     *
     * @param frame
     */
    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        imgLoading.setVisibility(INVISIBLE);
        imgComplete.setVisibility(INVISIBLE);
        imgPull.setVisibility(VISIBLE);
    }

}
