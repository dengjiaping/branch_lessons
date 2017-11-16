package com.yidiankeyan.science.information.acitivity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.ColumnDetailsBean;
import com.yidiankeyan.science.information.entity.IsshareonBean;
import com.yidiankeyan.science.my.activity.AccountBalanceActivity;
import com.yidiankeyan.science.my.entity.BalanceBean;
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
import com.yidiankeyan.science.view.OnMultiClickListener;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

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

import static com.yidiankeyan.science.R.id.app_bar;
import static com.yidiankeyan.science.R.id.toolbar;


/**
 * 专栏详情
 * -简介
 */
public class ColumnIntroductionActivity extends BaseActivity {

    private WebView webColumn;
    private String errorHtml = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
            "\"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "\n" +
            "<html>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<h1 align=\"center\">当前网络没有连接，请重新连接。</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    private String mId;
    private TextView mtvFreePlay;
    private LinearLayout mShop;
    private String mName;
    private String mPrice;
    private String mColumnWriterIntro;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvTitle;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private TextView tvTitleName;
    private int mIshasactivityprice;  //是否有活动价  1：有 0：没有
    private String mColumnActivityPrice; //当前活动价是多少
    private TextView mtvOriginalPrice;
    private TextView mtvColumnShopping;
    private ImageView mImgRoot;
    private ColumnDetailsBean detailsBean;
    private ImageView mImgMarketingShare;
    private TextView mtvName;
    private String mShareSaleId;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private TextView tvColumnDetails;
    private IsshareonBean isshareonBean;
    private AutoLinearLayout llColumnDetail;
    private PopupWindow reportPopupwindow;
    private TextView tvContentThree;
    private TextView tvContentFour;
    private TextView tvContentFive;
    private TextView tvContentSix;
    private TextView tvContentSeven;
    private TextView tvClear;
    private PopupWindow venditionSharePop;
    private TextView mtvAcale;
    private int priceInsufficient;
    private PopupWindow purchasePopupWindow;
    private TextView tvShopPrice;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalanceMoney;
    private BalanceBean balanceBean;
    private TextView tvSaveMoney;
    private int paymentMode = 0;
    private String mIsBuys;
    private AutoLinearLayout mllColumnShopping;
    private AutoLinearLayout mAutoLinearLayout;
    private String mLinkurl;

    @Override
    protected int setContentView() {
        return R.layout.fragment_column_introduction;
    }

    @Override
    protected void initView() {
        mId = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("name");
        mIsBuys = getIntent().getStringExtra("isBuys");
        mPrice = getIntent().getStringExtra("price");
        mLinkurl = getIntent().getStringExtra("linkurl");
        mColumnWriterIntro = getIntent().getStringExtra("columnWriterIntro");
        mIshasactivityprice = getIntent().getIntExtra("ishasactivityprice", 0);
        mColumnActivityPrice = getIntent().getStringExtra("columnActivityPrice");
        mllColumnShopping = (AutoLinearLayout) findViewById(R.id.ll_column_shopping);
        webColumn = (WebView) findViewById(R.id.web_column);
        mtvFreePlay = (TextView) findViewById(R.id.tv_free_play);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mToolbar = (Toolbar) findViewById(toolbar);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        mtvFreePlay.setOnClickListener(this);
        mShop = (LinearLayout) findViewById(R.id.shop);
        mShop.setOnClickListener(this);
        webColumn.setWebViewClient(new myWebViewClient());
        mtvOriginalPrice = (TextView) findViewById(R.id.tv_original_price);
        mtvColumnShopping = (TextView) findViewById(R.id.tv_column_shopping);
        mImgRoot = (ImageView) findViewById(R.id.img_root);
        mImgMarketingShare = (ImageView) findViewById(R.id.img_marketing_share);
        mtvName = (TextView) findViewById(R.id.tv_name);
        llColumnDetail = (AutoLinearLayout) findViewById(R.id.ll_column_detail);//
        mAutoLinearLayout = (AutoLinearLayout) findViewById(R.id.autoLinearLayout);
        if (!StringUtils.isEmpty(mIshasactivityprice + "") &&
                !StringUtils.isEmpty(mColumnActivityPrice)
                && 1 == mIshasactivityprice) {  //显示活动价
            mtvOriginalPrice.setTextSize(10);
            mtvColumnShopping.setTextSize(15);
            mtvColumnShopping.setVisibility(View.VISIBLE);
            mtvOriginalPrice.setText("原价：" + mPrice + "墨子币");
            mtvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mtvColumnShopping.setText("活动价：" + mColumnActivityPrice + "墨子币");
        } else {
            mtvColumnShopping.setVisibility(View.GONE);
            mtvOriginalPrice.setText("价格：" + mPrice + "墨子币");
            mtvOriginalPrice.getPaint().setFlags(0);
            mtvOriginalPrice.setTextSize(15);
        }
        initToolBar();
        findViewById(R.id.img_return).setOnClickListener(this);
        if (!StringUtils.isEmpty(mIsBuys) && "1".equals(mIsBuys)) {
            mAutoLinearLayout.setVisibility(View.GONE);
        } else {
            mAutoLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(" ");
//        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.TOP);////设置展开后标题的位置
        mCollapsingToolbarLayout.setExpandedTitleMarginTop(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setExpandedTitleMarginStart(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER | Gravity.TOP);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
        AppBarLayout appBar = (AppBarLayout) findViewById(app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        tvTitle.setBackgroundColor(Color.BLACK);
                        tvTitle.setText(mName);
                    }
                } else {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                            tvTitle.setBackgroundColor(Color.TRANSPARENT);
                            tvTitle.setText("");
                        }
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toHttp();
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
                    toHttp();
                    iumpColumnImageAudio();
                    EventMsg msg1 = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                    EventBus.getDefault().post(msg1);
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * liuchao  add 跳转至图文与音频页
     */
    private void iumpColumnImageAudio() {
        Intent intent = new Intent(ColumnIntroductionActivity.this, ColumnDetailActivity.class);
        if(!StringUtils.isEmpty(mLinkurl)){
            intent.putExtra("id", mLinkurl);
        }else
        intent.putExtra("id", mId);
        intent.putExtra("name", mName);
        intent.putExtra("columnWriterIntro", mColumnWriterIntro);
        intent.putExtra("price", mPrice);
        intent.putExtra("isBuys", "1");
        intent.putExtra("ishasactivityprice", mIshasactivityprice);
        intent.putExtra("columnActivityPrice", mColumnActivityPrice);
        intent.putExtra("updateNum", detailsBean.getArticletotal());
        startActivity(intent);
        finish();
    }

    //专栏详情
    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(mLinkurl)){
            map.put("id", mLinkurl);
        }else
        map.put("id", mId);
        ApiServerManager.getInstance().getApiServer().getQueryColumn(map).enqueue(new RetrofitCallBack<ColumnDetailsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ColumnDetailsBean>> call,
                                  Response<RetrofitResult<ColumnDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    if(null == detailsBean){
                        return;
                    }
                    Glide.with(ColumnIntroductionActivity.this).load(Util.getImgUrl(detailsBean.getColumnImg()))
                            .error(R.drawable.icon_banner_load)
                            .placeholder(R.drawable.icon_banner_load)
                            .into(mImgRoot);
                    tvTitleName.setText(detailsBean.getColumnName());
                    mtvName.setText(detailsBean.getColumnWriter());
                    mImgMarketingShare.setOnClickListener(ColumnIntroductionActivity.this);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ColumnDetailsBean>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void initAction() {
        WebSettings webSettings = webColumn.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        if (!StringUtils.isEmpty(mLinkurl)) {
            webColumn.loadUrl(SystemConstant.MYURL + "/column4/info/" + mLinkurl);
        }else webColumn.loadUrl(SystemConstant.MYURL + "/column4/info/" + mId);
        toHttpIsShareon();
        webColumn.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mImgMarketingShare.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_free_play:
                //TODO 跳转免费试播页面
                Intent intent = new Intent(ColumnIntroductionActivity.this, ColumnArticleActivity.class);
                if(!StringUtils.isEmpty(mLinkurl)){
                    intent.putExtra("id", mLinkurl);
                }else
                intent.putExtra("id", mId);
                intent.putExtra("isBuy",detailsBean.getHaveYouPurchased());
                intent.putExtra("title", mName);
                startActivity(intent);
                break;
            case R.id.shop:
                //TODO 购买完毕跳转图文及音频页
                //购买
                if (!Util.hintLogin(this))
                    return;
                if (detailsBean != null) {
                    Map<String, Object> maps = new HashMap<>();
                    if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                        maps.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                    }else maps.put("id", "");
                    ApiServerManager.getInstance().getApiServer().getQueryBalance(maps).enqueue(new RetrofitCallBack<BalanceBean>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                            if (response.body().getCode() == 200) {
                                double price;
                                if(!StringUtils.isEmpty(detailsBean.getColumnActivityPrice())){
                                    price = Double.parseDouble(detailsBean.getColumnActivityPrice());
                                }else
                                price = Double.parseDouble(detailsBean.getColumnPrice());
                                if (response.body().getData().getBalance() < price) {
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
                    showPayWindow();
                } else {
                    toHttp();
                }
                break;
            case R.id.img_return:
                finish();
                break;
            case R.id.img_marketing_share:
                if (!TextUtils.equals("null", detailsBean.getHaveYouPurchased()) &&
                        !TextUtils.isEmpty(detailsBean.getHaveYouPurchased())) {
                    //分销分享
                    Map<String, Object> map = new HashMap<>();
                    if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                        map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                    }else map.put("userid", "");
                    map.put("goodsid", detailsBean.getId());
                    map.put("goodstype", "3");
                    ApiServerManager.getInstance().getApiServer().getColumnShareid(map).enqueue(new RetrofitCallBack<String>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                            if (response.body().getCode() == 200) {
                                mShareSaleId = response.body().getData();
                                if (StringUtils.isEmpty(mShareSaleId)) {
                                    showVenditionSharePop();
                                } else {
                                    showSharePop();
                                }
                            } else {
                                showSharePop();
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {
                            showSharePop();
                        }
                    });
                } else {
                    showSharePop();
                }
                break;
            case R.id.ll_balance:
                setNormal();
                paymentMode = 1;
                llBalance.setEnabled(false);
                tvBalanceMoney.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.ll_wx_money:
                setNormal();
                paymentMode = 2;
                llWxMoney.setEnabled(false);
                tvWx.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.ll_zf_money:
                setNormal();
                paymentMode = 3;
                llZfMoney.setEnabled(false);
                tvZf.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.tv_save_money:
                if (paymentMode == 1) {
                    balancePayment();
                } else if (paymentMode == 2) {
                    wxPay();
                } else if (paymentMode == 3) {
                    aliPay();
                } else {
                    ToastMaker.showShortToast("请选择支付方式");
                }
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getId());
        map.put("payment", "1");
        map.put("platform", "1");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ColumnIntroductionActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        toHttp();
                                        iumpColumnImageAudio();
                                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                                        EventBus.getDefault().post(msg);
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
                } else if (result.getCode() == 306) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.again_try), Toast.LENGTH_SHORT).show();
                    toHttp();
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                    EventBus.getDefault().post(msg);
                } else {
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private String id;

    /**
     * 微信支付
     */
    private void wxPay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getId());
        map.put("payment", "2");
        map.put("platform", "1");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {

                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    id = wxPay.getId();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(ColumnIntroductionActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                } else if (result.getCode() == 306) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.again_try), Toast.LENGTH_SHORT).show();
                    toHttp();
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                    EventBus.getDefault().post(msg);
                } else {
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    //余额支付判断
    private void balancePayment() {
        Util.finishPop(this, purchasePopupWindow);
        String price;
        if(null != detailsBean.getColumnActivityPrice() &&
                1 == detailsBean.getIshasactivityprice()){
            price = detailsBean.getColumnActivityPrice();
        }else price = detailsBean.getColumnPrice();
        if (priceInsufficient == 1) {
            ToastMaker.showShortToast("余额不足");
        } else {
            showWaringDialog("支付", "是否使用" + price + "墨者币？", new OnDialogButtonClickListener() {
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

    /**
     * 余额支付
     */
    private void toHttpBalancePay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getId());
        map.put("payment", 4);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                    toHttp();
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                    EventBus.getDefault().post(msg);
                    iumpColumnImageAudio();
                } else if (result.getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else if (result.getCode() == 306) {
                    Toast.makeText(getApplicationContext(), "已购买", Toast.LENGTH_SHORT).show();
                    toHttp();
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                    EventBus.getDefault().post(msg);
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    //购买
    private void showPayWindow() {
        if (purchasePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_buy_column_win, null);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            llBalance = (AutoLinearLayout) view.findViewById(R.id.ll_balance);
            llWxMoney = (AutoLinearLayout) view.findViewById(R.id.ll_wx_money);
            llZfMoney = (AutoLinearLayout) view.findViewById(R.id.ll_zf_money);
            tvWx = (TextView) view.findViewById(R.id.tv_wx);
            tvZf = (TextView) view.findViewById(R.id.tv_zf);
            tvBalanceMoney = (TextView) view.findViewById(R.id.tv_balance_money);


            if (!StringUtils.isEmpty(mIshasactivityprice + "") &&
                    !StringUtils.isEmpty(mColumnActivityPrice)
                    && 1 == mIshasactivityprice) {  //显示活动价
                tvShopPrice.setText(mColumnActivityPrice);
            } else {
                if (!TextUtils.isEmpty(detailsBean.getColumnPrice())) {
                    tvShopPrice.setText(detailsBean.getColumnPrice());
                } else {
                    tvShopPrice.setText("");
                }
            }
            Map<String, Object> map = new HashMap<>();
            if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
            }else map.put("id", "");
            ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                    if (response.body().getCode() == 200) {
                        balanceBean = response.body().getData();
                        tvBalanceMoney.setText(balanceBean.getBalance() + "");
                        if (tvBalanceMoney.getText().length() > 5) {
                            tvBalanceMoney.setTextSize(10);
                        }
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                }
            });
            tvSaveMoney = (TextView) view.findViewById(R.id.tv_save_money);
            if (priceInsufficient == 1) {
                llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
                tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
                llBalance.setEnabled(false);
            } else {
                llBalance.setBackgroundResource(R.drawable.selector_column_textview);
                tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
            }
            llBalance.setOnClickListener(this);
            llWxMoney.setOnClickListener(this);
            llZfMoney.setOnClickListener(this);
            tvSaveMoney.setOnClickListener(this);
            purchasePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            purchasePopupWindow.setContentView(view);
            purchasePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            purchasePopupWindow.setFocusable(true);
            purchasePopupWindow.setOutsideTouchable(true);
            purchasePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            purchasePopupWindow.showAtLocation(findViewById(R.id.ll_column_detail), Gravity.BOTTOM, 0, 0);
            purchasePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop((Activity) mContext, purchasePopupWindow);
                    setNormal();
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            purchasePopupWindow.showAtLocation(findViewById(R.id.ll_column_detail), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 将打赏金额背景全部设为正常
     */
    private void setNormal() {
        llWxMoney.setEnabled(true);
        llZfMoney.setEnabled(true);
        tvWx.setTextColor(Color.parseColor("#333333"));
        tvZf.setTextColor(Color.parseColor("#333333"));
        if (priceInsufficient == 1) {
            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
            llBalance.setEnabled(false);
        } else {
            llBalance.setBackgroundResource(R.drawable.selector_column_textview);
            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
            llBalance.setEnabled(true);
        }
    }

    //查询分销是否开启
    private void toHttpIsShareon() {
        ApiServerManager.getInstance().getApiServer().isShareon().enqueue(new RetrofitCallBack<IsshareonBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<IsshareonBean>> call, Response<RetrofitResult<IsshareonBean>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().isState() == true) {
                        isshareonBean = response.body().getData();
                        mImgMarketingShare.setVisibility(View.VISIBLE);
                    } else {
                        mImgMarketingShare.setVisibility(View.GONE);
                    }
                }
//                imgShare.setClickable(true);
            }

            @Override
            public void onFailure(Call<RetrofitResult<IsshareonBean>> call, Throwable t) {

            }
        });
    }

    /**
     * 弹出分享框
     */
    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_column_share, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            tvColumnDetails = (TextView) view.findViewById(R.id.tv_column_details);
            TextView mtvAcale = (TextView) view.findViewById(R.id.tv_scale); //购买本专栏并且进行分享，赚取高达订单金额的20%的佣奖学金奖励~
            mtvAcale.setText("购买本专栏并且进行分享，赚取高达订单金额的" + isshareonBean.getParent().toString() + "的奖学金奖励~");
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(llColumnDetail, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ColumnIntroductionActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnIntroductionActivity.this, sharePopupWindow);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnIntroductionActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/share/" + detailsBean.getId(),
                                ""
                        );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnIntroductionActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/share/" + detailsBean.getId(),
                                ""
                        );
                }
            });
            llSearchQQ.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    //获取海报地址
                    Map<String, Object> map = new HashMap<>();
                    HttpUtil.post(ColumnIntroductionActivity.this,
                            SystemConstant.MYURL + SystemConstant.POST_COLUMN_POSTER +
                                    SpUtils.getStringSp(mContext, "userId") + "/" +
                                    detailsBean.getId(), map, new HttpUtil.HttpCallBack() {
                                @Override
                                public void successResult(ResultEntity result) throws JSONException {
                                    if (result.getCode() == 200) {
                                        String poster = (String) result.getData();
                                        Intent intent = new Intent(ColumnIntroductionActivity.this, ColumnPosterActivity.class);
                                        intent.putExtra("poster", poster);
                                        startActivity(intent);
                                        Util.finishPop(ColumnIntroductionActivity.this, sharePopupWindow);
                                    } else {
                                        ToastMaker.showShortToast("海报地址请求超时,请稍后再试");
                                    }
                                }

                                @Override
                                public void errorResult(Throwable ex, boolean isOnCallback) {
                                    ToastMaker.showShortToast("海报地址请求超时,请稍后再试");
                                }
                            });
                }
            });
            tvColumnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReportPop();
                    Util.finishPop(ColumnIntroductionActivity.this, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llColumnDetail, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 弹出分享收益框
     */
    private void showReportPop() {
        if (reportPopupwindow == null) {
            View view = View.inflate(this, R.layout.popuwind_column_share, null);
            tvContentThree = (TextView) view.findViewById(R.id.tv_content_three);
            tvContentFour = (TextView) view.findViewById(R.id.tv_content_four);
            tvContentFive = (TextView) view.findViewById(R.id.tv_content_five);
            tvContentSix = (TextView) view.findViewById(R.id.tv_content_six);
            tvContentSeven = (TextView) view.findViewById(R.id.tv_content_seven);
            tvClear = (TextView) view.findViewById(R.id.tv_clear);


            tvContentSix.setText("1、获得的奖学金可以在“我的”->“我的奖学金”中查看；");
            String threeStart = "2. 通过你分享的链接购买专栏可以享受原价";
            String discount = "<font color='red'>" + isshareonBean.getDiscount().toString() + "</font>";
            String threeEnd = "折优惠；";
            String content = threeStart + discount + threeEnd;
            tvContentThree.setText(Html.fromHtml(content));
            tvContentFour.setText("3、通过你分享的链接产生购买时，墨子学堂将颁发给你一等级奖学金；");
            tvContentFive.setText("4、通过购买者再次分享连接产生购买时，墨子学堂将颁发给你二等级奖学金；");

            String fourStart = "5、一等奖学金金额为订单金额的";
            String parent = "<font color='red'>" + isshareonBean.getParent().toString() + "</font>";
            String fourEnd = "，二等奖学金的金额为订单金额的";
            String ancestor = "<font color='red'>" + isshareonBean.getAncestor().toString() + "</font>。";
            String contentFour = fourStart + parent + fourEnd + ancestor;
            tvContentSeven.setText(Html.fromHtml(contentFour));

            tvClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnIntroductionActivity.this, reportPopupwindow);
                }
            });
            reportPopupwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            reportPopupwindow.setContentView(view);
            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            reportPopupwindow.setFocusable(true);
            reportPopupwindow.setOutsideTouchable(true);
            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(ColumnIntroductionActivity.this, reportPopupwindow);
                }
            });
            reportPopupwindow.showAtLocation(llColumnDetail, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            reportPopupwindow.showAtLocation(llColumnDetail, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 弹出分销分享框
     */
    private void showVenditionSharePop() {
        if (venditionSharePop == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_column_vendition, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            tvColumnDetails = (TextView) view.findViewById(R.id.tv_column_details);
            mtvAcale = (TextView) view.findViewById(R.id.tv_scale); //购买本专栏并且进行分享，赚取高达订单金额的20%的奖学金奖励~
            mtvAcale.setText("购买本专栏并且进行分享，赚取高达订单金额的" + isshareonBean.getParent().toString() + "的奖学金奖励~");
            venditionSharePop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            venditionSharePop.setContentView(view);
            venditionSharePop.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            venditionSharePop.setFocusable(true);
            venditionSharePop.setOutsideTouchable(true);
            venditionSharePop.setBackgroundDrawable(new BitmapDrawable());
            venditionSharePop.showAtLocation(llColumnDetail, Gravity.BOTTOM, 0, 0);
            venditionSharePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ColumnIntroductionActivity.this, venditionSharePop);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnIntroductionActivity.this, venditionSharePop);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnIntroductionActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/share/" + detailsBean.getId(),
                                ""
                        );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnIntroductionActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/share/" + detailsBean.getId(),
                                ""
                        );
                }
            });
            llSearchQQ.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    //获取海报地址
                    Map<String, Object> map = new HashMap<>();
                    HttpUtil.post(ColumnIntroductionActivity.this, SystemConstant.MYURL + SystemConstant.POST_COLUMN_POSTER + SpUtils.getStringSp(mContext, "userId") + "/" + detailsBean.getId(), map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            if (result.getCode() == 200) {
                                String poster = (String) result.getData();
                                Intent intent = new Intent(ColumnIntroductionActivity.this, ColumnPosterActivity.class);
                                intent.putExtra("poster", poster);
                                startActivity(intent);
                                Util.finishPop(ColumnIntroductionActivity.this, venditionSharePop);
                            } else {
                                ToastMaker.showShortToast("海报地址请求超时,请稍后再试");
                            }
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {

                        }
                    });
                }
            });
            tvColumnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReportPop();
                    Util.finishPop(ColumnIntroductionActivity.this, venditionSharePop);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            venditionSharePop.showAtLocation(llColumnDetail, Gravity.BOTTOM, 0, 0);
        }
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadData(errorHtml, "text/html", "UTF-8");
        }
    }
}
