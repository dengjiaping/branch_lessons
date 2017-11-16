package com.yidiankeyan.science.my.activity;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.entity.WitthdrawalsContentBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * -提现申请
 */
public class WithdrawalsApplyActivity extends BaseActivity {

    private TextView maintitleTxt;
    private AutoLinearLayout llReturn;
    private EditText edtApplyMoney;
    private EditText edtAccountNumberl;
    private EditText edtWithdrawalsName;
    private TextView tvAvailableBalance;
    private TextView tvWithdrawAllow;
    private TextView tvProfitWithdrawals;
    private WitthdrawalsContentBean contentBean;
    private TextView tvReturnContent;
    private AutoLinearLayout llWithdrawalsShopping;
    private InputMethodManager imm;
    private boolean isLegal;
    private AutoLinearLayout llAppWith;

    public WithdrawalsApplyActivity() {
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_withdrawals_apply;
    }

    @Override
    protected void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        edtApplyMoney = (EditText) findViewById(R.id.edt_apply_money);
        edtAccountNumberl = (EditText) findViewById(R.id.edt_account_number);
        edtWithdrawalsName = (EditText) findViewById(R.id.edt_withdrawals_name);
        tvAvailableBalance = (TextView) findViewById(R.id.tv_available_balance);
        tvWithdrawAllow = (TextView) findViewById(R.id.tv_withdraw_allow);
        tvProfitWithdrawals = (TextView) findViewById(R.id.tv_profit_withdrawals);
        tvReturnContent = (TextView) findViewById(R.id.tv_return_content);
        llWithdrawalsShopping = (AutoLinearLayout) findViewById(R.id.ll_withdrawals_shopping);
    }

    @Override
    protected void initAction() {
        maintitleTxt.setText("领取奖学金");
        llReturn.setOnClickListener(this);
        tvAvailableBalance.setText(getIntent().getStringExtra("account"));
        toHttpWithdrawAllow();
        edtApplyMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtApplyMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initStop();
            }
        });
        edtAccountNumberl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initStop();
            }
        });
        edtWithdrawalsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initStop();
            }
        });
        edtApplyMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    toHttpReturnContent();
                    initStop();
                }
            }
        });
        edtAccountNumberl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    initStop();
                }
            }
        });
        edtWithdrawalsName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    initStop();
                }
            }
        });
        tvProfitWithdrawals.setOnClickListener(this);
        edtAccountNumberl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        edtWithdrawalsName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }


    private void initStop() {
        if (!TextUtils.isEmpty(edtApplyMoney.getText().toString().trim()) && !TextUtils.isEmpty(edtAccountNumberl.getText().toString().trim()) && !TextUtils.isEmpty(edtWithdrawalsName.getText().toString().trim())) {
            tvProfitWithdrawals.setEnabled(true);
            tvProfitWithdrawals.setBackgroundResource(R.drawable.select_withdrawals_on);
        } else {
            tvProfitWithdrawals.setEnabled(false);
            tvProfitWithdrawals.setBackgroundResource(R.drawable.select_withdrawals_is);
        }
    }

    //失去焦点返回信息
    private void toHttpReturnContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", edtApplyMoney.getText().toString());
        ApiServerManager.getInstance().getApiServer().getReturnContent(map).enqueue(new RetrofitCallBack<WitthdrawalsContentBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WitthdrawalsContentBean>> call, Response<RetrofitResult<WitthdrawalsContentBean>> response) {
                if (response.body().getCode() == 200) {
                    contentBean = response.body().getData();
                    tvReturnContent.setText(contentBean.getResult());
                    isLegal = contentBean.isLegal();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WitthdrawalsContentBean>> call, Throwable t) {
            }
        });
    }

    //可提现金额
    private void toHttpWithdrawAllow() {
        Map<String, Object> map = new HashMap<>();
        ApiServerManager.getInstance().getApiServer().getWithdrawApplyAllow(map).enqueue(new RetrofitCallBack<Double>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Double>> call, Response<RetrofitResult<Double>> response) {
                if (response.body().getCode() == 200) {
                    tvWithdrawAllow.setText("今日可提现金额：" + response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Double>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_profit_withdrawals:
                if (!TextUtils.isEmpty(edtApplyMoney.getText().toString().trim()) && !TextUtils.isEmpty(edtAccountNumberl.getText().toString().trim()) && !TextUtils.isEmpty(edtWithdrawalsName.getText().toString().trim()) && contentBean.isLegal() == true) {
                    double money = Double.parseDouble(edtApplyMoney.getText().toString());
                    double balance = Double.parseDouble(tvAvailableBalance.getText().toString());
                    if (money > balance) {
                        Toast.makeText(this, "提现金额不能超过可提现余额,请重新输入！", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("amount", edtApplyMoney.getText().toString().trim());
                        map.put("account", edtAccountNumberl.getText().toString().trim());
                        map.put("accountname", edtWithdrawalsName.getText().toString().trim());
                        HttpUtil.post(this, SystemConstant.URL + SystemConstant.WITHDRAWALS_APPLY, map, new HttpUtil.HttpCallBack() {
                            @Override
                            public void successResult(ResultEntity result) throws JSONException {
                                if (200 == result.getCode()) {
                                    Toast.makeText(WithdrawalsApplyActivity.this, "申请提交成功", Toast.LENGTH_SHORT).show();
                                    edtApplyMoney.setText("");
                                    edtAccountNumberl.setText("");
                                    edtWithdrawalsName.setText("");
                                    tvProfitWithdrawals.setEnabled(false);
                                    tvProfitWithdrawals.setBackgroundResource(R.drawable.select_withdrawals_is);
                                    finish();
                                } else if (412 == result.getCode()) {
                                    Toast.makeText(WithdrawalsApplyActivity.this, "您尚有未处理的提现请求,请耐心等待", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void errorResult(Throwable ex, boolean isOnCallback) {
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, contentBean.getResult(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_withdrawals_shopping:
                imm.hideSoftInputFromWindow(edtApplyMoney.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edtAccountNumberl.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edtWithdrawalsName.getWindowToken(), 0);
                break;
        }
    }
}
