package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.entity.WXBindingPhoneBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 微信绑定成功
 */

public class WxBindingSucceedActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private AutoLinearLayout llAudioMore;
    private PopupWindow mPopupWindow;
    private AutoRelativeLayout rlWxBindingSucceed;
    private AutoLinearLayout llBindingCancel;
    private AutoLinearLayout llRemoveBinding;
    private PopupWindow mPopupWindows;
    private AutoRelativeLayout rlYesOnclick;

    @Override
    protected int setContentView() {
        return R.layout.activity_wx_binding_succeed;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
        rlWxBindingSucceed = (AutoRelativeLayout) findViewById(R.id.rl_wx_binding_succeed);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("微信绑定");
        llAudioMore.setVisibility(View.VISIBLE);
        llReturn.setOnClickListener(this);
        llAudioMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_audio_more:
                if (SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
                    relieveFailPop();
                } else {
                    PopupWindowBinding();
                }
                break;
            case R.id.ll_remove_binding:
                toHttpRemoveBinding();
                break;
            case R.id.ll_binding_cancel:
                finishPop(mPopupWindow);
                break;
        }
    }

    private void toHttpRemoveBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginModel", SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode"));
        progressDialog.setMessage("解绑中...");
        progressDialog.show();
        ApiServerManager.getInstance().getUserApiServer().getRemoveBindding(map).enqueue(new RetrofitCallBack<WXBindingPhoneBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WXBindingPhoneBean>> call, Response<RetrofitResult<WXBindingPhoneBean>> response) {
                if (response.body().getCode() == 200) {
                    ToastMaker.showShortToast("解绑成功");
                    SpUtils.putIntSp(mContext, "isBinding", 0);
                    finishPop(mPopupWindow);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("newIntent", true);
                    startActivity(intent);
                    finish();
                } else if (response.body().getCode() == 501) {
                    ToastMaker.showShortToast("账号未绑定");
                    finishPop(mPopupWindow);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXBindingPhoneBean>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void PopupWindowBinding() {
        if (mPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popup_window_remove_binding, null);
            llRemoveBinding = (AutoLinearLayout) view.findViewById(R.id.ll_remove_binding);
            llBindingCancel = (AutoLinearLayout) view.findViewById(R.id.ll_binding_cancel);
            llRemoveBinding.setOnClickListener(this);
            llBindingCancel.setOnClickListener(this);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAtLocation(rlWxBindingSucceed, Gravity.BOTTOM, 0, 0);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    finishPop(mPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(rlWxBindingSucceed, Gravity.BOTTOM, 0, 0);
        }
    }


    private void relieveFailPop() {
        if (mPopupWindows == null) {
            View view = View.inflate(this, R.layout.popup_window_relievefail, null);
            rlYesOnclick = (AutoRelativeLayout) view.findViewById(R.id.rl_yes_onclick);
            mPopupWindows = new PopupWindow(view, -2, -2);
            mPopupWindows.setContentView(view);
            mPopupWindows.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mPopupWindows.setFocusable(true);
            mPopupWindows.setOutsideTouchable(true);
            mPopupWindows.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(mPopupWindows);
                }
            });
            mPopupWindows.showAtLocation(rlWxBindingSucceed, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindows.showAtLocation(rlWxBindingSucceed, Gravity.CENTER, 0, 0);
        }

        rlYesOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPop(mPopupWindows);
            }
        });
    }

    /**
     * 隐藏PopupWindow
     *
     * @param popupWindow 要隐藏的PopupWindow
     */
    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }
}
