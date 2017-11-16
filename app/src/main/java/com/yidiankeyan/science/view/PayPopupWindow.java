package com.yidiankeyan.science.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidiankeyan.science.R;


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
 * Created by nby on 2016/9/21.
 * 作用：
 */
public class PayPopupWindow extends PopupWindow {
    private View contentView;
    private Activity activity;
    private TextView tvOrderInfo;
    private TextView tvWxPay;
    private TextView tvAliPay;
    private OnWxPayClickListener onWxPayClickListener;
    private OnAliPayClickListener onAliPayClickListener;
    private View container;

    public PayPopupWindow(Activity activity, View contentView) {
        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.contentView = contentView;
        this.activity = activity;
        init();
    }

    private void init() {
        tvOrderInfo = (TextView) contentView.findViewById(R.id.tv_order_info);
        tvWxPay = (TextView) contentView.findViewById(R.id.tv_wx_pay);
        tvAliPay = (TextView) contentView.findViewById(R.id.tv_ali_pay);
        setContentView(contentView);
        setAnimationStyle(R.style.AnimBottom);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
            }
        });
        tvWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWxPayClickListener != null)
                    onWxPayClickListener.onClick(v);
            }
        });
        tvAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onAliPayClickListener != null)
                    onAliPayClickListener.onClick(v);
            }
        });
    }

    public void setContainer(View container) {
        this.container = container;
    }

    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    public void setOnWxPayClickListener(OnWxPayClickListener onWxPayClickListener) {
        this.onWxPayClickListener = onWxPayClickListener;
    }

    public void setOnAliPayClickListener(OnAliPayClickListener onAliPayClickListener) {
        this.onAliPayClickListener = onAliPayClickListener;
    }

    public void show(String price) {
        tvOrderInfo.setText("您将支付金额¥" + price + ",请再次确认购买");
        WindowManager.LayoutParams lp = ((Activity) activity).getWindow().getAttributes();
        lp.alpha = 0.6f;
        ((Activity) activity).getWindow().setAttributes(lp);
        showAtLocation(container, Gravity.BOTTOM, 0, 0);
    }

    public interface OnWxPayClickListener {
        void onClick(View v);
    }

    public interface OnAliPayClickListener {
        void onClick(View v);
    }
}
