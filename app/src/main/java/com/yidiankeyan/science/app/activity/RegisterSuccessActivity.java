package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.SystemConstant;

import org.greenrobot.eventbus.EventBus;

/**
 * 用户注册成功
 */
public class RegisterSuccessActivity extends BaseActivity {

    private int recLen = 3;
    private TextView tvTimeDown;
    private TextView tvJump;

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    tvTimeDown.setText("" + recLen);

                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        tvTimeDown.setVisibility(View.GONE);
                        startActivity(new Intent(RegisterSuccessActivity.this,MainActivity.class));
                        finish();
                    }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_register_success;
    }

    @Override
    protected void initView() {
        tvTimeDown = (TextView) findViewById(R.id.tv_time_down);
        tvJump = (TextView) findViewById(R.id.tv_jumps);
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    @Override
    protected void initAction() {
        tvJump.setOnClickListener(this);
        ((TextView)findViewById(R.id.maintitle_txt)).setText("等待中。。。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jumps:
                handler.removeMessages(1);
                startActivity(new Intent(this,MainActivity.class));
                if (DemoApplication.getInstance().activityExisted(MainLoginActivity.class)){
                    DemoApplication.getInstance().finishActivity(MainLoginActivity.class);
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventMsg msg = EventMsg.obtain(SystemConstant.MOZ_REGISTER_RETURN);
        msg.setArg1(1);
        EventBus.getDefault().post(msg);
        EventBus.getDefault().unregister(this);
    }
}
