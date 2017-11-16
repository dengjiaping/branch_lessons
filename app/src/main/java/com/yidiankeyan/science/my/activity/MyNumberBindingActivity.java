package com.yidiankeyan.science.my.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.WxBindPhoneActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.entity.WXBindingPhoneBean;
import com.yidiankeyan.science.utils.LogUtils;
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
 * 我的
 * -账号安全
 */

public class MyNumberBindingActivity extends BaseActivity {


    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private AutoRelativeLayout rlPhoneBinding, rlWxBinding;
    private TextView tvAlreadyBinding, tvWxAlreadyBinding;
    private String code;
    private PopupWindow mPopupWindow;
    private IntentFilter intentFilter;
    private AutoLinearLayout llMyNumberSecurity;
    private AutoRelativeLayout rlYesOnclick;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_number_security;
    }

    @Override
    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.wx.login.code");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        rlPhoneBinding = (AutoRelativeLayout) findViewById(R.id.rl_phone_binding);
        rlWxBinding = (AutoRelativeLayout) findViewById(R.id.rl_wx_binding);
        tvAlreadyBinding = (TextView) findViewById(R.id.tv_already_binding);
        tvWxAlreadyBinding = (TextView) findViewById(R.id.tv_wx_already_binding);
        llMyNumberSecurity = (AutoLinearLayout) findViewById(R.id.ll_my_number_security);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            loadingDismiss();
        }

        @Override
        public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA arg0, int arg1) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            loadingDismiss();
        }

    };

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            code = intent.getStringExtra("code");
            toHttpWxBinding();
        }
    };

    @Override
    protected void initAction() {
        txtTitle.setText("账号绑定");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0) {
            rlPhoneBinding.setVisibility(View.GONE);
            rlWxBinding.setVisibility(View.VISIBLE);

            if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
                tvWxAlreadyBinding.setText("未绑定");
                tvWxAlreadyBinding.setTextColor(Color.parseColor("#999999"));

            } else {
                tvWxAlreadyBinding.setText("已绑定");
                tvWxAlreadyBinding.setTextColor(Color.parseColor("#e54e4e"));
            }
        } else if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1) {
            rlWxBinding.setVisibility(View.GONE);
            rlPhoneBinding.setVisibility(View.VISIBLE);

            if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
                tvAlreadyBinding.setText("未绑定");
                tvAlreadyBinding.setTextColor(Color.parseColor("#999999"));

            } else {
                tvAlreadyBinding.setText("已绑定");
                tvAlreadyBinding.setTextColor(Color.parseColor("#e54e4e"));

            }
        }
//        if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
//            tvAlreadyBinding.setText("未绑定");
//            tvAlreadyBinding.setTextColor(Color.parseColor("#999999"));
//
//        } else {
//            tvAlreadyBinding.setText("已绑定");
//            tvAlreadyBinding.setTextColor(Color.parseColor("#e54e4e"));
//
//        }
//        if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
//            tvWxAlreadyBinding.setText("未绑定");
//            tvWxAlreadyBinding.setTextColor(Color.parseColor("#999999"));
//
//        } else {
//            tvWxAlreadyBinding.setText("已绑定");
//            tvWxAlreadyBinding.setTextColor(Color.parseColor("#e54e4e"));
//        }
        rlPhoneBinding.setOnClickListener(this);
        rlWxBinding.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.rl_phone_binding:
                if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0) {
                    startActivity(new Intent(this, PhoneBindingSucceedActivity.class));
                }

                if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
                    startActivity(new Intent(this, WxBindPhoneActivity.class));
                } else if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 1) {
                    startActivity(new Intent(this, PhoneBindingSucceedActivity.class));
                }
                break;
            case R.id.rl_wx_binding:
                if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1) {
                    startActivity(new Intent(this, WxBindingSucceedActivity.class));
                }
                if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0) {
                    showLoadingDialog("请稍候");
                    UMShareAPI.get(this).doOauthVerify(MyNumberBindingActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                } else if (SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 0 && SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 1) {
                    startActivity(new Intent(this, WxBindingSucceedActivity.class));
                }
                break;
        }
    }

    private void toHttpWxBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginModel", SpUtils.getIntSp(this, "loginModel"));
        map.put("code", code);
        ApiServerManager.getInstance().getUserApiServer().getBinddingPhone(map).enqueue(new RetrofitCallBack<WXBindingPhoneBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WXBindingPhoneBean>> call, Response<RetrofitResult<WXBindingPhoneBean>> response) {
                loadingDismiss();
                if (response.body().getCode() == 200) {
                    ToastMaker.showShortToast("绑定成功");
                    SpUtils.putIntSp(mContext, "isBinding", 1);
                    startActivity(new Intent(MyNumberBindingActivity.this, WxBindingSucceedActivity.class));
                    finish();
                } else if (response.body().getCode() == 500) {
                    loadingDismiss();
                    ToastMaker.showShortToast(response.body().getMsg());
                } else {
                    loadingDismiss();
                    ToastMaker.showShortToast("绑定失败");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXBindingPhoneBean>> call, Throwable t) {
                loadingDismiss();
                ToastMaker.showShortToast("网络繁忙");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
