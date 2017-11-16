package com.yidiankeyan.science.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidiankeyan.science.R;


public class SelectPicturePopup implements OnClickListener {
    public PopupWindow mPopupWindow;
    private TextView bt_camera, bt_picture, cancle_btn;

    private OnClickFlagDialogListener onClickFlagDialogListener;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    private Context mContext;

    @SuppressWarnings("deprecation")
    public SelectPicturePopup(Context context) {
        mContext = context;

        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());

        mPopupWindow.getContentView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.setFocusable(false);
                mPopupWindow.dismiss();
                return true;
            }
        });

    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.select_picture_popup, null);
        bt_camera = (TextView) view.findViewById(R.id.camera_btn);
        bt_camera.setOnClickListener(this);
        bt_picture = (TextView) view.findViewById(R.id.picture_btn);
        bt_picture.setOnClickListener(this);
        cancle_btn = (TextView) view.findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn:
                onClickFlagDialogListener.getFlag(0);
                break;
            case R.id.picture_btn:
                onClickFlagDialogListener.getFlag(1);
                break;
            case R.id.cancle_btn:
                mPopupWindow.dismiss();
                break;

            default:
                break;
        }
    }

    public interface OnCancelClickListener {
        void onCancel(View view);

    }

    public interface OnClickFlagDialogListener {
        void getFlag(int flag);
    }

    public void setOnClickFlagDialogListener(
            OnClickFlagDialogListener onClickFlagDialogListener) {
        this.onClickFlagDialogListener = onClickFlagDialogListener;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }


}
