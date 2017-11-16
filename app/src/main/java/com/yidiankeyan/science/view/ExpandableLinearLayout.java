package com.yidiankeyan.science.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.zhy.autolayout.AutoLinearLayout;

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
 * Created by nby on 2017/4/13.
 * 作用：
 */

public class ExpandableLinearLayout extends AutoLinearLayout {

    private int expandHeight;
    private boolean mAnimating;
    private boolean collapsed;
    private int collapsedHeight;

    public boolean isAnimating() {
        return mAnimating;
    }

    public ExpandableLinearLayout(Context context) {
        super(context);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLinearLayout);
//        if (a.getBoolean(R.styleable.ExpandableLinearLayout_collapsed, false)) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        if (expandHeight != 0) {
//                            post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    collapsed = true;
//                                    getLayoutParams().height = 0;
//                                    requestLayout();
//                                    setVisibility(VISIBLE);
//                                }
//                            });
//                            break;
//                        }
//                    }
//                }
//            }).start();
//        }
//        a.recycle();
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!collapsed && !mAnimating)
            expandHeight = getMeasuredHeight();
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        this.collapsedHeight = collapsedHeight;
    }

    public void setExpandHeight(int expandHeight) {
        this.expandHeight = expandHeight;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(300);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public void collapsed() {
        if (mAnimating)
            return;
        collapsed = true;
        mAnimating = true;
        Animation animation = new ExpandCollapseAnimation(this, getHeight(), collapsedHeight == 0 ? 0 : collapsedHeight);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimating = false;
                clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    public void expand() {
        Animation animation;
        if (getVisibility() == INVISIBLE) {
            collapsed = true;
            getLayoutParams().height = 0;
            requestLayout();
            setVisibility(VISIBLE);
            animation = new ExpandCollapseAnimation(this, 0, expandHeight);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mAnimating = false;
                    clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            if (mAnimating)
                return;
            collapsed = false;
            mAnimating = true;
            animation = new ExpandCollapseAnimation(this, getHeight(), expandHeight);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mAnimating = false;
                    clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        clearAnimation();
        startAnimation(animation);
    }

}
