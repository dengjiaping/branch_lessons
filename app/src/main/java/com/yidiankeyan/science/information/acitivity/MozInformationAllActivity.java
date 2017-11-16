package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.MozInformationAllAdapter;
import com.yidiankeyan.science.information.entity.MozReadBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.utils.DisplayUtil;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;

/**
 * 墨子读书
 * -全部列表
 */
public class MozInformationAllActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvTitle;
    private RefreshRecyclerView recyclerView;
    private MozInformationAllAdapter informationAllAdapter;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;

    private CoordinatorLayout llMozInformationAll;
    private List<MozReadBean.ListBean> mReadList = new ArrayList<>();
    private MozReadDetailsBean detailsBean;
    private PopupWindow payPopupWindow;
    private String id;
    private TextView tvOrderInfo;
    private int pages = 1;
    private int priceInsufficient = 0;

    private Long startTime;
    private Long endTime;
    private Long currentTime;
    private double price;
    private int isPriceOn;

    @Override
    protected boolean isFullScreen() {
        return true;
    }


    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_moz_information_all;
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        llMozInformationAll = (CoordinatorLayout) findViewById(R.id.ll_moz_information_all);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        initRecyclerView();
        toHttpGetMozRead();
        findViewById(R.id.img_return).setOnClickListener(this);
    }

    private void initRecyclerView() {
        informationAllAdapter = new MozInformationAllAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(informationAllAdapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetMozRead();
            }
        });
        informationAllAdapter.setOnItemClickListener(new MozInformationAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int isPrice) {
                Intent intent = new Intent(mContext, MozReadDetailsActivity.class);
                intent.putExtra("id", mReadList.get(position).getId());
                intent.putExtra("name", mReadList.get(position).getName());
                intent.putExtra("isbuy", mReadList.get(position).getIsbuy() + "");
                mContext.startActivity(intent);
            }
        });
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
//        //通过CollapsingToolbarLayout修改字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.TOP);////设置展开后标题的位置
        collapsingToolbarLayout.setExpandedTitleMarginTop(Util.dip2px(this, 48));
        collapsingToolbarLayout.setExpandedTitleMarginStart(Util.dip2px(this, 48));
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER | Gravity.TOP);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
        AppBarLayout appBar = (AppBarLayout) findViewById(app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                Log.e("verticalOffset", verticalOffset + "");
//                Log.e("getTotalScrollRange", appBarLayout.getTotalScrollRange() + "");
//                Log.e("dip", Util.dip2px(TheNewTodayAudioActivity.this, 206) + "");
                if (Math.abs(verticalOffset) >= Util.dip2px(MozInformationAllActivity.this, 206)) {

                } else {

                }
                if (verticalOffset == 0) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        tvTitle.setBackgroundColor(Color.BLACK);
                        tvTitle.setText("墨子读书");
                        viewStatusBar.setBackgroundColor(Color.BLACK);
                    }
                } else {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                            tvTitle.setText("");
                            tvTitle.setBackgroundColor(Color.TRANSPARENT);
                            viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
                        }
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                    params.height = params.height + DisplayUtil.getStatueBarHeight(MozInformationAllActivity.this);
                    toolbar.setLayoutParams(params);
                }
            });
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        informationAllAdapter.notifyDataSetChanged();
    }


    /**
     * 获取墨子读书列表
     */
    private void toHttpGetMozRead() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
//        map.put("checkstatus", 2);
        ApiServerManager.getInstance().getApiServer().getMozReadList(map).enqueue(new RetrofitCallBack<MozReadBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<MozReadBean>> call, Response<RetrofitResult<MozReadBean>> response) {
                if (response.code() == 200) {
                    MozReadBean mozReadBean = (MozReadBean) GsonUtils.json2Bean(
                            GsonUtils.obj2Json(response.body().getData()), MozReadBean.class);
                    if (pages == 1)
                        mReadList.removeAll(mReadList);
                    if (mozReadBean.getList().size() > 0) {
                        mReadList.addAll(mozReadBean.getList());
                        pages++;
                        informationAllAdapter.clear();
                        informationAllAdapter.addAll(mReadList);
                        informationAllAdapter.notifyDataSetChanged();
                    } else {
                        informationAllAdapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                } else {
                    informationAllAdapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<MozReadBean>> call, Throwable t) {
                informationAllAdapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.MOZ_READS_PURCHASE:
                if (!Util.hintLogin(this))
                    return;
                Map<String, Object> map = new HashMap<>();
                if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                    map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                }else map.put("id", "");
                ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                        if (response.body().getCode() == 200) {
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
                onReadDetails(msg);
                break;
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    setNormal();
                    Util.finishPop(MozInformationAllActivity.this, payPopupWindow);
                    //跳转墨子读书播放
                    Intent intent = new Intent(MozInformationAllActivity.this, MozReadAudioActivity.class);
                    intent.putExtra("name", detailsBean.getName());
                    intent.putExtra("mediaurl", detailsBean.getMediaurl());
                    intent.putExtra("coverimgurl", detailsBean.getCoverimgurl());
                    intent.putExtra("createtime", detailsBean.getCreatetime());
                    intent.putExtra("lastupdatetime", detailsBean.getLastupdatetime());
                    startActivity(intent);
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void onReadDetails(EventMsg msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msg.getBody());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_READ_DETAILS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    detailsBean = (MozReadDetailsBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), MozReadDetailsBean.class);
                    price = detailsBean.getPrice();
                    if (price > 0) {
                        showPayWindow();
                    } else {
                        toHttpReceiveRead();
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });

    }

    /**
     * 限时领取
     */
    private void toHttpReceiveRead() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "6");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "领取成功", Toast.LENGTH_SHORT).show();
                    id = (String) result.getData();
                    Util.finishPop(MozInformationAllActivity.this, payPopupWindow);
                    //跳转墨子读书播放
                    Intent intent = new Intent(MozInformationAllActivity.this, MozReadAudioActivity.class);
                    intent.putExtra("name", detailsBean.getName());
                    intent.putExtra("mediaurl", detailsBean.getMediaurl());
                    intent.putExtra("coverimgurl", detailsBean.getCoverimgurl());
                    intent.putExtra("createtime", detailsBean.getCreatetime());
                    intent.putExtra("lastupdatetime", detailsBean.getLastupdatetime());
                    startActivity(intent);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "领取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private double blance;
    private TextView tvShopPrice;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalance;
    private TextView tvBalanceMoney;
    private TextView tvSaveMoney;
    private int paymentMode = 0;

    /**
     * 选择支付方式
     */
    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_purchase_win, null);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            llBalance = (AutoLinearLayout) view.findViewById(R.id.ll_balance);
            llWxMoney = (AutoLinearLayout) view.findViewById(R.id.ll_wx_money);
            llZfMoney = (AutoLinearLayout) view.findViewById(R.id.ll_zf_money);
            tvWx = (TextView) view.findViewById(R.id.tv_wx);
            tvZf = (TextView) view.findViewById(R.id.tv_zf);
            tvBalance = (TextView) view.findViewById(R.id.tv_balance);
            tvBalanceMoney = (TextView) view.findViewById(R.id.tv_balance_money);
            Map<String, Object> map = new HashMap<>();
            if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
            }else map.put("id", "");
            ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                    if (response.body().getCode() == 200) {
                        blance = response.body().getData().getBalance();
                        tvBalanceMoney.setText(response.body().getData().getBalance() + " 墨子币");

                        if (blance < price) {
                            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
                            tvBalance.setTextColor(Color.parseColor("#ffffff"));
                            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
                            llBalance.setEnabled(false);
                        } else {
//                            llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
//                            tvBalance.setTextColor(Color.parseColor("#333333"));
//                            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
                            paymentMode = 1;
                            llBalance.setEnabled(false);
                            tvBalanceMoney.setTextColor(Color.parseColor("#f1312e"));
                            tvBalance.setTextColor(Color.parseColor("#f1312e"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                }
            });
            tvSaveMoney = (TextView) view.findViewById(R.id.tv_save_money);
            llBalance.setOnClickListener(this);
            llWxMoney.setOnClickListener(this);
            llZfMoney.setOnClickListener(this);
            tvSaveMoney.setOnClickListener(this);
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
            payPopupWindow.showAtLocation(llMozInformationAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MozInformationAllActivity.this, payPopupWindow);
                    setNormal();
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(llMozInformationAll, Gravity.BOTTOM, 0, 0);
        }
//        if (blance < price) {
//            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
//            tvBalance.setTextColor(Color.parseColor("#ffffff"));
//            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
//            llBalance.setEnabled(false);
//        } else {
//            llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
//            tvBalance.setTextColor(Color.parseColor("#333333"));
//            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
//        }
        ((TextView) payPopupWindow.getContentView().findViewById(R.id.tv_shop_price)).setText(price + "");
    }

    private void setNormal() {
        llWxMoney.setEnabled(true);
        llZfMoney.setEnabled(true);
        tvWx.setTextColor(Color.parseColor("#333333"));
        tvZf.setTextColor(Color.parseColor("#333333"));
        if (blance < price) {
            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
            tvBalance.setTextColor(Color.parseColor("#ffffff"));
            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
            llBalance.setEnabled(false);
        } else {
            llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
            tvBalance.setTextColor(Color.parseColor("#333333"));
            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
            llBalance.setEnabled(true);
        }
    }

    /**
     * 余额支付
     */
    private void toHttpBalancePay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "3");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "支付成功", Toast.LENGTH_SHORT).show();
                    setNormal();
                    id = (String) result.getData();
                    Util.finishPop(MozInformationAllActivity.this, payPopupWindow);
                    //跳转墨子读书播放
                    Intent intent = new Intent(MozInformationAllActivity.this, MozReadAudioActivity.class);
                    intent.putExtra("name", detailsBean.getName());
                    intent.putExtra("mediaurl", detailsBean.getMediaurl());
                    intent.putExtra("coverimgurl", detailsBean.getCoverimgurl());
                    intent.putExtra("createtime", detailsBean.getCreatetime());
                    intent.putExtra("lastupdatetime", detailsBean.getLastupdatetime());
                    startActivity(intent);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
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

    /**
     * 支付宝支付
     */
    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "1");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MozInformationAllActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        setNormal();
                                        Util.finishPop(MozInformationAllActivity.this, payPopupWindow);
                                        //跳转墨子读书播放
                                        Intent intent = new Intent(MozInformationAllActivity.this, MozReadAudioActivity.class);
                                        intent.putExtra("name", detailsBean.getName());
                                        intent.putExtra("mediaurl", detailsBean.getMediaurl());
                                        intent.putExtra("coverimgurl", detailsBean.getCoverimgurl());
                                        intent.putExtra("createtime", detailsBean.getCreatetime());
                                        intent.putExtra("lastupdatetime", detailsBean.getLastupdatetime());
                                        startActivity(intent);
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
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 微信支付
     */
    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "2");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
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
                    IWXAPI api = WXAPIFactory.createWXAPI(MozInformationAllActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.ll_balance:
                setNormal();
                paymentMode = 1;
                llBalance.setEnabled(false);
                tvBalanceMoney.setTextColor(Color.parseColor("#f1312e"));
                tvBalance.setTextColor(Color.parseColor("#f1312e"));
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
                    toHttpBalancePay();
                } else if (paymentMode == 2) {
                    ToastMaker.showShortToast("请稍后");
                    wxPay();
                } else if (paymentMode == 3) {
                    ToastMaker.showShortToast("请稍后");
                    aliPay();
                } else {
                    ToastMaker.showShortToast("请选择支付方式");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                //如果mainActivity不存在则跳转主页面
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
