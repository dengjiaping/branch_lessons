package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.adapter.MozReadRelevantAdapter;
import com.yidiankeyan.science.information.entity.BookJudgeBuysBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.information.entity.MozRelevantReadBean;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.GlideRoundTransform;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.xutils.ex.DbException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;
import static com.yidiankeyan.science.R.id.toolbar;


/**
 * 墨子读书
 * -书籍详情
 */

@MLinkRouter(keys = {"MOZ_KEY"})
public class MozReadDetailsActivity extends BaseActivity {

    private ImageView imgBg;
    private ImageView imgAuthor;
    private TextView tvTitle;
    private ImageView imgReturn;
    private TextView tvReadName;
    private TextView tvAnchorName;
    private TextView tvdetail;
    private RecyclerView mRecyclerView;
    private MozReadRelevantAdapter readRelevantAdapter;
    private List<MozRelevantReadBean> mDatas = new ArrayList<>();
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private MozReadDetailsBean detailsBean;
    private AutoLinearLayout llRandomReplace;
    private int priceInsufficient = 0;
    private Long startTime;
    private Long endTime;
    private Long currentTime;
    private CoordinatorLayout activityMonthlySeries;

    private PopupWindow purchasePopupWindow;
    private BalanceBean balanceBean;
    private TextView tvShopPrice;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalance;
    private TextView tvBalanceMoney;
    private TextView tvSaveMoney;
    private double price;
    private int paymentMode = 0;
    private String id;
    private String ShareId;

    private TextView tvReadPrice;
    private TextView tvReadShopping;
    private AutoLinearLayout llDownLoads;
    private AutoLinearLayout llAudioPlayers;
    private TextView tvReadDownload;
    private TextView mtvReadType;
    private TextView mtvReadAuthor;
    private TextView mtvReadTime;
    private TextView mtvUpdateTime;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_moz_read_details;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgBg = (ImageView) findViewById(R.id.img_bg);
        imgAuthor = (ImageView) findViewById(R.id.img_author);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mToolbar = (Toolbar) findViewById(toolbar);
        tvReadName = (TextView) findViewById(R.id.tv_read_name);
        tvAnchorName = (TextView) findViewById(R.id.txt_anchor_name);
        mtvReadType = (TextView) findViewById(R.id.tv_read_type);
        mtvReadAuthor = (TextView) findViewById(R.id.tv_read_author);
        mtvReadTime = (TextView) findViewById(R.id.tv_read_time);
        mtvUpdateTime = (TextView) findViewById(R.id.tv_updatetime);
        tvdetail = (TextView) findViewById(R.id.tv_desc);
        llRandomReplace = (AutoLinearLayout) findViewById(R.id.ll_random_replace);
        activityMonthlySeries = (CoordinatorLayout) findViewById(R.id.activity_monthly_series);
        tvReadPrice = (TextView) findViewById(R.id.tv_read_price);
        tvReadShopping = (TextView) findViewById(R.id.tv_read_shopping);
        llDownLoads = (AutoLinearLayout) findViewById(R.id.ll_down_loads);
        llAudioPlayers = (AutoLinearLayout) findViewById(R.id.ll_audio_players);
        tvReadDownload = (TextView) findViewById(R.id.tv_read_download);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        initToolBar();
        toHttpPostIsBuy();
        toHttpPostDetails();
//        toHttpPostBalance();
        initRecyclerView();
        toHttpPostRelevantRead();
        imgReturn.setOnClickListener(this);
        llRandomReplace.setOnClickListener(this);
        tvReadShopping.setOnClickListener(this);
        llDownLoads.setOnClickListener(this);
        llAudioPlayers.setOnClickListener(this);
        tvReadPrice.setOnClickListener(this);
    }

    /**
     * 是否购买
     */
    private void toHttpPostIsBuy() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().getBookJudgeBuy(map).enqueue(new RetrofitCallBack<BookJudgeBuysBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<BookJudgeBuysBean>> call, Response<RetrofitResult<BookJudgeBuysBean>> response) {
                if (response.body().getCode() == 200) {
                    if (TextUtils.equals("1", response.body().getData().getIsbuy())) {
                        tvReadPrice.setVisibility(View.VISIBLE);
                        llDownLoads.setVisibility(View.VISIBLE);
                        llAudioPlayers.setVisibility(View.VISIBLE);
                        tvReadShopping.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<BookJudgeBuysBean>> call, Throwable t) {

            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
                    params.height = params.height + DisplayUtil.getStatueBarHeight(MozReadDetailsActivity.this);
                    mToolbar.setLayoutParams(params);
                }
            });
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

//                Log.e("verticalOffset", verticalOffset + "");
//                Log.e("getTotalScrollRange", appBarLayout.getTotalScrollRange() + "");
//                Log.e("dip", Util.dip2px(TheNewTodayAudioActivity.this, 206) + "");
                if (Math.abs(verticalOffset) >= Util.dip2px(MozReadDetailsActivity.this, 206)) {
                    tvTitle.setBackgroundColor(Color.BLACK);
                    tvTitle.setText(getIntent().getStringExtra("name") == null ? "" : getIntent().getStringExtra("name"));
                    viewStatusBar.setBackgroundColor(Color.BLACK);
                } else {
                    tvTitle.setText("");
                    tvTitle.setBackgroundColor(Color.TRANSPARENT);
                    viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
                }
                if (verticalOffset == 0) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == TheNewTodayAudioActivity.CollapsingToolbarLayoutState.COLLAPSED) {
                        }
                        state = TheNewTodayAudioActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }


    private void initRecyclerView() {
        readRelevantAdapter = new MozReadRelevantAdapter(this);
//        allAdapter.setHeader(headView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(readRelevantAdapter);
//        mRecyclerView.setLoadMoreAction(new Action() {
//            @Override
//            public void onAction() {
//                toHttpPostRelevantRead();
//            }
//        });
    }

    /**
     * 相关书籍
     */
    private void toHttpPostRelevantRead() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_RELEVANT_READ, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    mDatas.removeAll(mDatas);
                    List<MozRelevantReadBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), MozRelevantReadBean.class);
                    if (datas.size() > 0) {
                        mDatas.addAll(datas);
                        readRelevantAdapter.clear();
                        readRelevantAdapter.addAll(mDatas);
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 书籍详情
     */
    private void toHttpPostDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_READ_DETAILS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    detailsBean = (MozReadDetailsBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), MozReadDetailsBean.class);
                    Glide.with(mContext).load(Util.getImgUrl(detailsBean.getCoverimgurl()))
                            .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(MozReadDetailsActivity.this, 25, 2))
                            .placeholder(R.drawable.icon_banner_load)
                            .error(R.drawable.icon_banner_load)
                            .into(imgBg);
                    Glide.with(mContext).load(Util.getImgUrl(detailsBean.getCoverimgurl()))
                            .placeholder(R.drawable.icon_readload_failed)
                            .transform(new GlideRoundTransform(mContext))
                            .error(R.drawable.icon_readload_failed).into(imgAuthor);
                    mtvReadType.setText("类别：" + detailsBean.getSubjectname());
                    tvReadName.setText(detailsBean.getName());
//                    tvTime.setText("时长：" + TimeUtils.getMagazineLength(detailsBean.getMedialength()));
                    tvdetail.setText(detailsBean.getDesc());
                    DecimalFormat df = new DecimalFormat("0.00");
                    if (detailsBean.getPrice() > 0) {
                        tvReadPrice.setText("价格" + df.format(detailsBean.getPrice()) + " 墨子币");
                        tvReadShopping.setText("立即购买");
                    } else {
                        tvReadPrice.setText("免费");
                        tvReadShopping.setText("立即领取");
                    }
                    tvAnchorName.setText("解读者：" + detailsBean.getAuthor());
                    mtvReadAuthor.setText("作者：" + detailsBean.getAuthor());
//                    if (!TextUtils.isEmpty(detailsBean.getCastername()) && !TextUtils.equals("null", detailsBean.getCastername())) {
//                        tvNarratorName.setText("主播：" + detailsBean.getCastername());
//                    } else {
//                        tvNarratorName.setText("");
//                    }
                    if(!StringUtils.isEmpty(String.valueOf(detailsBean.getMedialength()))){
                        mtvReadTime.setText("时长："+TimeUtils.getMagazineLength(detailsBean.getMedialength()));
                    }
                    if(!StringUtils.isEmpty(String.valueOf(detailsBean.getLastupdatetime()))){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                        mtvUpdateTime.setText("更新时间："+simpleDateFormat.format(new Date(detailsBean.getLastupdatetime())));
                    }
                    MozReadDetailsBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFile(detailsBean.getId());
                    //未下载过,开始下载
                    if (albumContent == null) {
                        tvReadDownload.setText("下载");
                    } else {
                        if (detailsBean.getDownloadState() == 1) {
                            tvReadDownload.setText("已下载");
                        } else {
                            tvReadDownload.setText("下载");
                        }
                    }
//                    tvUpdateTime.setText("上线时间：" + TimeUtils.formatDate(detailsBean.getCreatetime()));
                    tvReadShopping.setEnabled(true);
                    price = detailsBean.getPrice();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

//    private void toHttpPostBalance() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
//        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
//                if (response.body().getCode() == 200) {
//                    if (response.body().getData().getBalance() < price) {
//                        priceInsufficient = 1;
//                    } else {
//                        priceInsufficient = 2;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {
//
//            }
//        });
//    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    Util.finishPop(this, purchasePopupWindow);
                    tvReadPrice.setVisibility(View.VISIBLE);
                    llDownLoads.setVisibility(View.VISIBLE);
                    llAudioPlayers.setVisibility(View.VISIBLE);
                    tvReadShopping.setVisibility(View.GONE);
                    toHttpPostDetails();
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.DOWNLOAD_FINISH:
                if (detailsBean != null && TextUtils.equals(detailsBean.getId(), (String) msg.getBody())) {
                    tvReadDownload.setText("已下载");
                }
                break;
        }
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
                    Util.finishPop(MozReadDetailsActivity.this, purchasePopupWindow);
                    tvReadPrice.setVisibility(View.VISIBLE);
                    llDownLoads.setVisibility(View.VISIBLE);
                    llAudioPlayers.setVisibility(View.VISIBLE);
                    tvReadShopping.setVisibility(View.GONE);
                    toHttpPostDetails();
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


    /**
     * 选择支付方式
     */

    private void showPayWindow() {
        hideSoftKeyboard();
        if (purchasePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_purchase_win, null);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            llBalance = (AutoLinearLayout) view.findViewById(R.id.ll_balance);
            llWxMoney = (AutoLinearLayout) view.findViewById(R.id.ll_wx_money);
            llZfMoney = (AutoLinearLayout) view.findViewById(R.id.ll_zf_money);
            tvWx = (TextView) view.findViewById(R.id.tv_wx);
            tvZf = (TextView) view.findViewById(R.id.tv_zf);
            tvBalance = (TextView) view.findViewById(R.id.tv_balance);
            tvBalanceMoney = (TextView) view.findViewById(R.id.tv_balance_money);
            tvShopPrice.setText(detailsBean.getPrice() + "");
            Map<String, Object> map = new HashMap<>();
            if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
            }else map.put("id", "");
            ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                    if (response.body().getCode() == 200) {
                        balanceBean = response.body().getData();
                        tvBalanceMoney.setText(balanceBean.getBalance() + " 墨子币");
                        if (response.body().getData().getBalance() < price) {
                            priceInsufficient = 1;
                        } else {
                            priceInsufficient = 2;
                        }
                        if (priceInsufficient == 1) {
                            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
                            tvBalance.setTextColor(Color.parseColor("#ffffff"));
                            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
                            llBalance.setEnabled(false);
                        } else {
                            llBalance.setEnabled(true);
                            llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
                            tvBalance.setTextColor(Color.parseColor("#333333"));
                            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
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
            purchasePopupWindow.showAtLocation(findViewById(R.id.activity_monthly_series), Gravity.BOTTOM, 0, 0);
            purchasePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MozReadDetailsActivity.this, purchasePopupWindow);
                    setNormal();
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            purchasePopupWindow.showAtLocation(findViewById(R.id.activity_monthly_series), Gravity.BOTTOM, 0, 0);
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
                    id = (String) result.getData();
                    Util.finishPop(MozReadDetailsActivity.this, purchasePopupWindow);
                    tvReadPrice.setVisibility(View.VISIBLE);
                    llDownLoads.setVisibility(View.VISIBLE);
                    llAudioPlayers.setVisibility(View.VISIBLE);
                    tvReadShopping.setVisibility(View.GONE);
                    toHttpPostDetails();
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
                            PayTask alipay = new PayTask(MozReadDetailsActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        Util.finishPop(MozReadDetailsActivity.this, purchasePopupWindow);
                                        tvReadPrice.setVisibility(View.VISIBLE);
                                        llDownLoads.setVisibility(View.VISIBLE);
                                        llAudioPlayers.setVisibility(View.VISIBLE);
                                        tvReadShopping.setVisibility(View.GONE);
                                        toHttpPostDetails();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(MozReadDetailsActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
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
            case R.id.img_return:
                finish();
                break;
            case R.id.ll_random_replace:
                toHttpPostRelevantRead();
                break;
            case R.id.tv_read_shopping:
                if (!Util.hintLogin(this))
                    return;
                if (detailsBean.getPrice() > 0) {
                    showPayWindow();
                } else {
                    toHttpReceiveRead();
                }

                break;
            case R.id.ll_down_loads:
                if (detailsBean.getMediaurl() == null || TextUtils.isEmpty(detailsBean.getMediaurl()) || "null".equals(detailsBean.getMediaurl())) {
                    Toast.makeText(DemoApplication.applicationContext, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                    break;
                }
                //查询该内容是否下载过
                MozReadDetailsBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFile(detailsBean.getId());
                String suffixes = detailsBean.getMediaurl().substring(detailsBean.getMediaurl().lastIndexOf("."));
                //未下载过,开始下载
                if (albumContent == null) {
                    Toast.makeText(DemoApplication.applicationContext, "开始下载", Toast.LENGTH_SHORT).show();
                    try {
                        detailsBean.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getName() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(detailsBean);
                        DownloadManager.getInstance().startDownload(
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getMediaurl()
                                , detailsBean.getName()
                                , Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getName() + suffixes
                                , true
                                , false
                                , null
                                , detailsBean.getId(), 3, 0);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (albumContent.getDownloadState() == 0) {
                        Toast.makeText(DemoApplication.applicationContext, "下载中...", Toast.LENGTH_SHORT).show();
                        //代表该内容已下载完成
                    } else if (albumContent.getDownloadState() == 1) {
                        ToastMaker.showShortToast("下载完成");
                        tvReadDownload.setText("已下载");
                        //判断本地中是否存在该文件
                        if (Util.fileExisted(albumContent.getFilePath())) {
                            Toast.makeText(DemoApplication.applicationContext, "该文件已下载", Toast.LENGTH_SHORT).show();
                        } else {
                            detailsBean.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getName() + suffixes);
                            try {
                                DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(detailsBean.getId(), 0, 3);
                                DownloadManager.getInstance().startDownload(
                                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getMediaurl()
                                        , detailsBean.getName()
                                        , Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getName() + suffixes, true, false, null, detailsBean.getId(), 3, 0);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case R.id.ll_audio_players:
                if (TextUtils.isEmpty(detailsBean.getId()) || TextUtils.equals("null", detailsBean.getId()) || detailsBean == null || detailsBean.getId() == null)
                    return;
                //跳转墨子读书播放
                Intent intents = new Intent(MozReadDetailsActivity.this, MozReadAudioActivity.class);
                intents.putExtra("id", detailsBean.getId());
                startActivity(intents);
                break;
            case R.id.tv_read_price:
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
                    balancePayment();
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

    //余额支付
    private void balancePayment() {
        Util.finishPop(this, purchasePopupWindow);
        if (priceInsufficient == 1) {
            ToastMaker.showShortToast("余额不足");
        } else {
            showWaringDialog("支付", "是否使用" + detailsBean.getPrice() + " 墨子币？", new OnDialogButtonClickListener() {
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}