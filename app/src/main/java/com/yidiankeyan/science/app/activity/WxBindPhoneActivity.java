package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.my.entity.PhoneBindingBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 微信强制绑定手机
 */
public class WxBindPhoneActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private EditText mPhoneNumber;
    private TextView tvNextReset;

    @Override
    protected int setContentView() {
        return R.layout.activity_wx_bind_phone;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        tvNextReset = (TextView) findViewById(R.id.tv_next_reset);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        tvNextReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                toHttpExit();
                startActivity(new Intent(WxBindPhoneActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.tv_next_reset:
                if (!Util.isMobPhone(mPhoneNumber.getText().toString())) {
                    ToastMaker.showShortToast("请输入正确的手机号");
                    return;
                }  else {
                    toHttpBindPhone();
                }
                break;
        }
    }

    /**
     * 微信强制绑定手机号获取验证码
     */
    private void toHttpBindPhone() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nationCode", 86);
        map.put("phoneNumber", mPhoneNumber.getText().toString());
        map.put("templateType", 1);
        ApiServerManager.getInstance().getApiServer().getBinddingCode(map).enqueue(new RetrofitCallBack<PhoneBindingBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<PhoneBindingBean>> call, Response<RetrofitResult<PhoneBindingBean>> response) {
                if (response.body().getCode() == 200) {
                    SpUtils.putIntSp(DemoApplication.applicationContext, "bindingCode", Integer.parseInt(response.body().getData().getCode()));
                    Intent intent = new Intent(WxBindPhoneActivity.this, VerificationCodeActivity.class);
                    intent.putExtra("mobile", mPhoneNumber.getText().toString());
                    intent.putExtra("type", "2");
                    startActivity(intent);
                } else {
                    ToastMaker.showShortToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<PhoneBindingBean>> call, Throwable t) {

            }
        });
    }

    /**
     * 退出登录
     */
    private void toHttpExit() {
        //用户先清除服务器端token再删数据
        Util.logout();
        showLoadingDialog("正在操作");
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
        AudioPlayManager.getInstance().release();
        AudioPlayManager.getInstance().reset();
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_TODAY_FLASH));
        Intent intent = new Intent();
        intent.setAction("action.read.audio");
        sendBroadcast(intent);
        ApiServerManager.getInstance().getUserApiServer().logout().enqueue(new RetrofitCallBack<Object>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                if (response.body().getCode() == 200 || response.body().getCode() == 10000) {
                    finish();
                    DB.getInstance(DemoApplication.applicationContext).deleteAllClick();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Util.logout();
            showLoadingDialog("正在操作");
            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
            AudioPlayManager.getInstance().release();
            AudioPlayManager.getInstance().reset();
            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_TODAY_FLASH));
            Intent intent = new Intent();
            intent.setAction("action.read.audio");
            sendBroadcast(intent);
            ApiServerManager.getInstance().getUserApiServer().logout().enqueue(new RetrofitCallBack<Object>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                    if (response.body().getCode() == 200 || response.body().getCode() == 10000) {
                        finish();
                        DB.getInstance(DemoApplication.applicationContext).deleteAllClick();
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                }
            });
            startActivity(new Intent(WxBindPhoneActivity.this, MainActivity.class));
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
