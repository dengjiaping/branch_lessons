package com.yidiankeyan.science.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yidiankeyan.science.R;


/**
 * 自定义的progressBar
 *
 * @author zhl
 */
public class CBProgressBar extends View {
    private static final int STYLE_ROUND = 1;
    /**进度背景画笔**/
//	private Paint mBgpaint;
    /**进度画笔**/
//	private Paint mPrgpaint;
    /**进度文字画笔**/
//	private Paint mTextpaint;
    /**
     * 圆形进度条边框宽度
     **/
    private int strokeWidth = 5;
    /**
     * 进度条中心X坐标
     **/
    private int centerX;
    /**
     * 进度条中心Y坐标
     **/
    private int centerY;
    /**
     * 进度提示文字大小
     **/
    private int percenttextsize = 18;
    /**
     * 进度提示文字颜色
     **/
    private int percenttextcolor = 0xff009ACD;
    /**
     * 进度条背景颜色
     **/
    private int progressBarBgColor = 0xff636363;
    /**
     * 进度颜色
     **/
    private int progressColor = 0xff00C5CD;
    /**
     * 扇形扫描进度的颜色
     */
    private int sectorColor = 0xaaffffff;
    /**
     * 扇形扫描背景
     */
    private int unSweepColor = 0xaa5e5e5e;
    /**
     * 进度条样式（水平/圆形）
     **/
    private int orientation = STYLE_ROUND;
    /**
     * 圆形进度条半径
     **/
    private int radius = 30;
    /**
     * 进度最大值
     **/
    private int max = 100;
    /**
     * 进度值
     **/
    private int progress = 0;
    /**
     * 水平进度条是否是空心
     **/
    private boolean isHorizonStroke;
    /**
     * 水平进度圆角值
     **/
    private int rectRound = 5;
    /**
     * 进度文字是否显示百分号
     **/
    private boolean showPercentSign;
    private Paint mPaint;

    public CBProgressBar(Context context) {
        this(context, null);
    }

    public CBProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CBProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.cbprogressbar);
        percenttextcolor = array.getColor(R.styleable.cbprogressbar_percent_text_color, percenttextcolor);
//        progressBarBgColor = array.getColor(R.styleable.cbprogressbar_progressBarBgColor, progressBarBgColor);
        progressBarBgColor = Color.parseColor("#F6F6F6");
//        progressColor = array.getColor(R.styleable.cbprogressbar_progressColor, progressColor);
        progressColor = Color.parseColor("#E54E4E");
        sectorColor = array.getColor(R.styleable.cbprogressbar_sectorColor, sectorColor);
        unSweepColor = array.getColor(R.styleable.cbprogressbar_unSweepColor, unSweepColor);
        percenttextsize = (int) array.getDimension(R.styleable.cbprogressbar_percent_text_size, percenttextsize);
        strokeWidth = (int) array.getDimension(R.styleable.cbprogressbar_stroke_width, strokeWidth);
        rectRound = (int) array.getDimension(R.styleable.cbprogressbar_rect_round, rectRound);
        orientation = array.getInteger(R.styleable.cbprogressbar_orientation, STYLE_ROUND);
        isHorizonStroke = array.getBoolean(R.styleable.cbprogressbar_isHorizonStroke, false);
        showPercentSign = array.getBoolean(R.styleable.cbprogressbar_showPercentSign, true);
//		mBgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mPrgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mTextpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        array.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = centerX - strokeWidth / 2;
        drawRoundProgressBar(canvas, mPaint);
    }

    /**
     * 绘制圆形进度条
     *
     * @param canvas
     */
    private void drawRoundProgressBar(Canvas canvas, Paint paint) {
        // 初始化画笔属性
        paint.setColor(progressBarBgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        // 画圆
        canvas.drawCircle(centerX, centerY, radius, paint);
        // 画圆形进度
        paint.setColor(progressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        RectF oval = new RectF(centerX - radius, centerY - radius, radius + centerX, radius + centerY);
        canvas.drawArc(oval, -90, 360 * progress / max, false, paint);
//        // 画进度文字
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(percenttextcolor);
//        paint.setTextSize(percenttextsize);

//        String percent = (int) (progress * 100 / max) + "%";
//        Rect rect = new Rect();
//        paint.getTextBounds(percent, 0, percent.length(), rect);
//        float textWidth = rect.width();
//        float textHeight = rect.height();
//        if (textWidth >= radius * 2) {
//            textWidth = radius * 2;
//        }
//        Paint.FontMetrics metrics = paint.getFontMetrics();
//        float baseline = (getMeasuredHeight() - metrics.bottom + metrics.top) / 2 - metrics.top;
//        canvas.drawText(percent, centerX - textWidth / 2, baseline, paint);
    }

    public void setProgress(int progress) {
        if (progress > max) {
            progress = max;
        } else {
            this.progress = progress;
            postInvalidate();
        }
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getPercenttextsize() {
        return percenttextsize;
    }

    public void setPercenttextsize(int percenttextsize) {
        this.percenttextsize = percenttextsize;
    }

    public int getPercenttextcolor() {
        return percenttextcolor;
    }

    public void setPercenttextcolor(int percenttextcolor) {
        this.percenttextcolor = percenttextcolor;
    }

    public int getProgressBarBgColor() {
        return progressBarBgColor;
    }

    public void setProgressBarBgColor(int progressBarBgColor) {
        this.progressBarBgColor = progressBarBgColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isHorizonStroke() {
        return isHorizonStroke;
    }

    public void setHorizonStroke(boolean isHorizonStroke) {
        this.isHorizonStroke = isHorizonStroke;
    }

    public int getRectRound() {
        return rectRound;
    }

    public void setRectRound(int rectRound) {
        this.rectRound = rectRound;
    }

    public int getMax() {
        return max;
    }

    public int getProgress() {
        return progress;
    }


}
