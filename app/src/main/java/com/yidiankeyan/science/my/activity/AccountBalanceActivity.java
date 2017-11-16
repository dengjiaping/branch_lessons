package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 我的
 * -账户余额
 */
public class AccountBalanceActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageView tvRecharge6;
    private ImageView tvRecharge68;
    private ImageView tvRecharge128;
    private ImageView tvRecharge200;
    private ImageView tvRecharge500;
    private ImageView tvRecharge980;
    private TextView tvMoz6, tvMoz68, tvMoz128, tvMoz200, tvMoz500, tvMoz980;
    private AutoRelativeLayout llTenElement, llFiftyElement, llOneHundredElement, llTwoHundred, llFiveHundred, llNineHundredAndEighty;
    private PopupWindow payPopupWindow;
    private double price = 0;
    private TextView tvOrderInfo;
    private AutoLinearLayout llAccountBalance;
    private TextView tvSaveMoney;
//    private AutoLinearLayout llAccountHelp;
    private AutoRelativeLayout rlSelectWx;
    private AutoRelativeLayout rlSelectZfb;
    private ImageView imgWxOn;
    private ImageView imgZfbOn;
    private int isSelectMoney = 0;
    private int isPrice = 0;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_account_balance;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        llTenElement = (AutoRelativeLayout) findViewById(R.id.ll_Ten_element);
        llFiftyElement = (AutoRelativeLayout) findViewById(R.id.ll_Fifty_element);
        llOneHundredElement = (AutoRelativeLayout) findViewById(R.id.ll_One_hundred_element);
        llTwoHundred = (AutoRelativeLayout) findViewById(R.id.ll_two_hundred);
        llFiveHundred = (AutoRelativeLayout) findViewById(R.id.ll_five_hundred);
        llNineHundredAndEighty = (AutoRelativeLayout) findViewById(R.id.ll_nine_hundred_and_eighty);
        llAccountBalance = (AutoLinearLayout) findViewById(R.id.ll_account_balance);
        tvSaveMoney = (TextView) findViewById(R.id.tv_save_money);
        tvMoz6 = (TextView) findViewById(R.id.tv_moz_6);
        tvMoz68 = (TextView) findViewById(R.id.tv_moz_68);
        tvMoz128 = (TextView) findViewById(R.id.tv_moz_128);
        tvMoz200 = (TextView) findViewById(R.id.tv_moz_200);
        tvMoz500 = (TextView) findViewById(R.id.tv_moz_500);
        tvMoz980 = (TextView) findViewById(R.id.tv_moz_980);
        tvRecharge6 = (ImageView) findViewById(R.id.tv_recharge_6);
        tvRecharge68 = (ImageView) findViewById(R.id.tv_recharge_68);
        tvRecharge128 = (ImageView) findViewById(R.id.tv_recharge_128);
        tvRecharge200 = (ImageView) findViewById(R.id.tv_recharge_200);
        tvRecharge500 = (ImageView) findViewById(R.id.tv_recharge_500);
        tvRecharge980 = (ImageView) findViewById(R.id.tv_recharge_980);
//        llAccountHelp = (AutoLinearLayout) findViewById(R.id.ll_account_help);
        rlSelectWx = (AutoRelativeLayout) findViewById(R.id.rl_select_wx);
        rlSelectZfb = (AutoRelativeLayout) findViewById(R.id.rl_select_zfb);
        imgWxOn = (ImageView) findViewById(R.id.img_wx_on);
        imgZfbOn = (ImageView) findViewById(R.id.img_zfb_on);

    }

    @Override
    protected void initAction() {
        txtTitle.setText("账户充值");
        llReturn.setOnClickListener(this);
        llTenElement.setOnClickListener(this);
        llFiftyElement.setOnClickListener(this);
        llOneHundredElement.setOnClickListener(this);
        llTwoHundred.setOnClickListener(this);
        llFiveHundred.setOnClickListener(this);
        llNineHundredAndEighty.setOnClickListener(this);
        tvSaveMoney.setOnClickListener(this);
//        llAccountHelp.setOnClickListener(this);
        rlSelectWx.setOnClickListener(this);
        rlSelectZfb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_Ten_element:
                setNormal();
                price = 6;
                llTenElement.setEnabled(false);
                tvMoz6.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge6.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.ll_Fifty_element:
                setNormal();
                price = 12;
                llFiftyElement.setEnabled(false);
                tvMoz68.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge68.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.ll_One_hundred_element:
                setNormal();
                price = 50;
                llOneHundredElement.setEnabled(false);
                tvMoz128.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge128.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.ll_two_hundred:
                setNormal();
                price = 108;
                llTwoHundred.setEnabled(false);
                tvMoz500.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge500.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.ll_five_hundred:
                setNormal();
                price = 218;
                llFiveHundred.setEnabled(false);
                tvMoz200.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge200.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.ll_nine_hundred_and_eighty:
                setNormal();
                price = 618;
                llNineHundredAndEighty.setEnabled(false);
                tvMoz980.setTextColor(Color.parseColor("#f1312e"));
                tvRecharge980.setVisibility(View.VISIBLE);
                isPrice = 1;
//                showPayWindow();
                break;
            case R.id.rl_select_wx:
                imgWxOn.setImageResource(R.drawable.icon_moneys_check_on);
                imgZfbOn.setImageResource(R.drawable.icon_moneys_check);
                isSelectMoney = 1;
                break;
            case R.id.rl_select_zfb:
                imgZfbOn.setImageResource(R.drawable.icon_moneys_check_on);
                imgWxOn.setImageResource(R.drawable.icon_moneys_check);
                isSelectMoney = 2;
                break;
            case R.id.tv_save_money:
                if (isSelectMoney == 0) {
                    ToastMaker.showShortToast("请选择支付方式");
                } else if (isSelectMoney == 1 && isPrice == 1) {
                    wxPay();
                } else if (isSelectMoney == 2 && isPrice == 1) {
                    aliPay();
                } else if (isPrice == 0) {
                    ToastMaker.showShortToast("请选择充值金额");
                }
//                showPayWindow();
                break;
//            case R.id.ll_account_help:
//                startActivity(new Intent(this, RechargeHelpActivity.class));
//                break;
        }
    }

    /**
     * 将打赏金额背景全部设为正常
     */
    private void setNormal() {
        llTenElement.setEnabled(true);
        llFiftyElement.setEnabled(true);
        llOneHundredElement.setEnabled(true);
        llTwoHundred.setEnabled(true);
        llFiveHundred.setEnabled(true);
        llNineHundredAndEighty.setEnabled(true);
        tvMoz6.setTextColor(Color.parseColor("#333333"));
        tvMoz68.setTextColor(Color.parseColor("#333333"));
        tvMoz128.setTextColor(Color.parseColor("#333333"));
        tvMoz200.setTextColor(Color.parseColor("#333333"));
        tvMoz500.setTextColor(Color.parseColor("#333333"));
        tvMoz980.setTextColor(Color.parseColor("#333333"));
        tvRecharge6.setVisibility(View.GONE);
        tvRecharge68.setVisibility(View.GONE);
        tvRecharge128.setVisibility(View.GONE);
        tvRecharge200.setVisibility(View.GONE);
        tvRecharge500.setVisibility(View.GONE);
        tvRecharge980.setVisibility(View.GONE);
    }

    private void aliPay() {
        showLoadingDialog("请等待");
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("price", price);
        map.put("goods_id", "RECHARGE");
        map.put("goods_type", "RECHARGE");
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("seller_id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("seller_id", "");
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BALANCE_RECHARGE_FOR_ALIPAY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(AccountBalanceActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        setNormal();
                                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_BALANCE_STATE);
                                        EventBus.getDefault().post(msg);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                LogUtils.e(ex + "");
            }
        });
    }

    private void wxPay() {
        showLoadingDialog("请等待");
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("price", price);
        map.put("goods_id", "RECHARGE");
        map.put("goods_type", "RECHARGE");
        map.put("seller_id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        map.put("platform", 1);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL +
                SystemConstant.BALANCE_RECHARGE_FOR_WX, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(AccountBalanceActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof AccountBalanceActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    setNormal();
                    EventMsg msg1 = EventMsg.obtain(SystemConstant.ON_BALANCE_STATE);
                    EventBus.getDefault().post(msg1);
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
