package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.entity.UserIntegrate;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的
 * -积分
 */
public class MyIntegralActivity extends BaseActivity {
    private TextView maintitleTxt;
    private AutoLinearLayout llReturn;
    private TextView tvIntegralNumber;
    private TextView tvMyIntegral;
    private TextView tvSiZi;

    /**
     * 使用规则
     */
//    private TextView tvIntegralRule;
    @Override
    protected int setContentView() {
        return R.layout.activity_my_integral;
    }

    @Override
    protected void initView() {
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        tvIntegralNumber = (TextView) findViewById(R.id.tv_integral_number);
        tvMyIntegral = (TextView) findViewById(R.id.tv_my_integral);
        tvSiZi = (TextView) findViewById(R.id.tv_sizi);
//        tvIntegralRule= (TextView) findViewById(R.id.tv_integral_rule);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("我的积分");
        llReturn.setOnClickListener(this);
        String s = getIntent().getStringExtra("integrate");
        if (s != null) {
            tvIntegralNumber.setText(getIntent().getStringExtra("integrate"));
            tvMyIntegral.setText("当前积分：" + getIntent().getStringExtra("integrate") + "分");
        } else {
            tvIntegralNumber.setText("0");
            tvMyIntegral.setText("0");
        }
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "userId"))) {
            toHttpQueryIntegrate();
        }
//        tvIntegralRule.setOnClickListener(this);
    }

    private void toHttpQueryIntegrate() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_INTEGRATE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    UserIntegrate userIntegrate = (UserIntegrate) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserIntegrate.class);
                    tvIntegralNumber.setText(userIntegrate.getPoints() + "");
                    tvMyIntegral.setText("当前积分：" + userIntegrate.getPoints() + "分");
                    Intent intent = new Intent();
                    intent.putExtra("size", userIntegrate.getPoints());
                    setResult(RESULT_OK, intent);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
//            case R.id.tv_integral_rule:
//                startActivity(new Intent(this,IntegralRuleActivity.class));
//                break;
        }
    }
}
