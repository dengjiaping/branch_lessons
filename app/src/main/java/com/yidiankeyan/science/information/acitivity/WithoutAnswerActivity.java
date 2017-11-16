package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.activity.MainLoginActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 还不是答人时显示
 * 有两种情况
 * 1:用户未注册
 * 2:用户已注册但不是答人
 */
public class WithoutAnswerActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private TextView tvShowState;
    private Button btnRegister;
    private int state;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_without_answer;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        tvShowState = (TextView) findViewById(R.id.tv_show_state);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("我的科答");
        state = getIntent().getIntExtra("state", 0);
        if (state == 2) {
            tvShowState.setText("您还不是答人");
            btnRegister.setText("快速成为答人");
        } else if (state == 3) {
            tvShowState.setVisibility(View.GONE);
            btnRegister.setText("审核中");
            btnRegister.setEnabled(false);
        }
        btnRegister.setOnClickListener(this);
        llReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (state == 2) {
                    startActivity(new Intent(this, CreateKedaUserActivity.class));
                    return;
                }
                startActivity(new Intent(this, MainLoginActivity.class));
                break;
            case R.id.ll_return:
                finish();
                break;
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_USER_INFORMATION:
                //用户登录成功, 判断是否为答人
                showLoadingDialog("请稍候");
                toHttpGetDetail();
                break;
            case SystemConstant.NOTYFY_ANSWER_INFO_SAVE:
                finish();
                break;
        }
    }

    /**
     * 获取答人详情
     */
    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    AnswerPeopleDetail answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    if (answerPeopleDetail.getAuthenticated() == 0) {
                        state = 2;
                        tvShowState.setText("您还不是答人");
                        btnRegister.setText("快速成为答人");
                    } else if (answerPeopleDetail.getAuthenticated() == 1) {
                        tvShowState.setVisibility(View.GONE);
                        btnRegister.setText("审核中");
                        btnRegister.setEnabled(false);
                        state = 3;
                    } else if (answerPeopleDetail.getAuthenticated() == 2) {
                        Intent intent = new Intent(WithoutAnswerActivity.this, MyAnswerActivity.class);
                        intent.putExtra("bean", answerPeopleDetail);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
