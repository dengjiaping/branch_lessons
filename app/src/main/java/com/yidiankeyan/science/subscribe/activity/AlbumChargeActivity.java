package com.yidiankeyan.science.subscribe.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
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
import com.yidiankeyan.science.my.activity.AccountBalanceActivity;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
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

import retrofit2.Call;
import retrofit2.Response;


/**
 * 收费专辑
 */
public class AlbumChargeActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private AlbumDetail albumDetail;
    private TextView tvAlbumName;
    private ImageView imgAlbumBg;
    private WebView webDescribes,webGuidelines;
    private TextView tvPrice;
    private PopupWindow payPopupWindow;
    private AutoRelativeLayout rlAlbumCharge;
    private TextView tvOrderInfo, tvWxPay, tvAliPay;
    private int priceInsufficient;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_album_charge;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        tvAlbumName = (TextView) findViewById(R.id.tv_album_name);
        imgAlbumBg = (ImageView) findViewById(R.id.img_album_bg);
        webDescribes = (WebView) findViewById(R.id.web_describes);
        webGuidelines= (WebView) findViewById(R.id.web_guidelines);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        rlAlbumCharge = (AutoRelativeLayout) findViewById(R.id.rl_album_charge);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        albumDetail = getIntent().getParcelableExtra("albumDetail");
        tvAlbumName.setText(albumDetail.getAlbumname());
        Glide.with(mContext).load(Util.getImgUrl(albumDetail.getImgurl()))
                .placeholder(R.drawable.icon_banner_load)
                .error(R.drawable.icon_banner_load)
                .into(imgAlbumBg);
//        webDescribes.loadData(albumDetail.getDescribes(), "text/html", "UTF-8");
        webDescribes.loadDataWithBaseURL(null, albumDetail.getDescribes(), "text/html", "utf-8", null);
        webGuidelines.loadDataWithBaseURL(null, albumDetail.getGuidelines(), "text/html", "utf-8", null);
        tvPrice.setText("购买：¥ " + albumDetail.getPrice());
        toHttpPostBalance();
        tvPrice.setOnClickListener(this);
    }

    private void toHttpPostBalance() {
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("id", "");
        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().getBalance() < albumDetail.getPrice()) {
                        priceInsufficient = 1;
                    } else {
                        priceInsufficient = 2;
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoApplication.getInstance().activityExisted(TransitionActivity.class)) {
            DemoApplication.getInstance().finishActivity(TransitionActivity.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_price:
                showPayWindow();
                break;
        }
    }

    /**
     * 选择支付方式
     */
    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            tvWxPay = (TextView) view.findViewById(R.id.tv_wx_pay);
            tvAliPay = (TextView) view.findViewById(R.id.tv_ali_pay);
            tvOrderInfo.setVisibility(View.GONE);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AlbumChargeActivity.this, payPopupWindow);
                }
            });

            tvWxPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(AlbumChargeActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            tvAliPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(AlbumChargeActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AlbumChargeActivity.this, payPopupWindow);
                    if (priceInsufficient == 1) {
                        ToastMaker.showShortToast("余额不足");
                    } else {
                        showWaringDialog("支付", "是否使用" + albumDetail.getPrice() + "墨子币？", new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                toHttpBalancePay();
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    }
                }
            });
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(rlAlbumCharge, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AlbumChargeActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(rlAlbumCharge, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("");
    }

    /**
     * 余额支付
     */
    private void toHttpBalancePay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", albumDetail.getAlbumid());
        map.put("pay_type", "4");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "支付成功", Toast.LENGTH_SHORT).show();
//                    id = (String) result.getData();
                    toHttpDetails();
                } else if (result.getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", albumDetail.getAlbumid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    albumDetail = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                    if (albumDetail.getPermission() == 1) {
                        Intent intent = new Intent(AlbumChargeActivity.this, AlbumDetailsActivity.class);
                        intent.putExtra("albumId", albumDetail.getAlbumid());
                        intent.putExtra("albumName", albumDetail.getAlbumname());
                        intent.putExtra("albumAvatar", getIntent().getStringExtra("albumAvatar"));
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 微信支付
     */
    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", albumDetail.getAlbumid());
        map.put("pay_type", "2");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {

                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
//                    id = wxPay.getId();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(AlbumChargeActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 支付宝支付
     */
    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", albumDetail.getAlbumid());
        map.put("pay_type", "1");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
//                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(AlbumChargeActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        toHttpDetails();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
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
                    toHttpDetails();
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
