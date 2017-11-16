package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.InfomationVPAdapter;
import com.yidiankeyan.science.information.entity.IsshareonBean;
import com.yidiankeyan.science.information.entity.MagazineDetailsBean;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.information.fragment.ExcerptFragment;
import com.yidiankeyan.science.information.fragment.UnscrambleFragment;
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
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

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

import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;
import static com.yidiankeyan.science.R.id.toolbar;


/**
 * 月刊详情
 */
public class MagazineDetailsActivity extends BaseActivity {

    private static MagazineDetailsActivity instance;

    private AppBarLayout appBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView imgRoot;
    private ImageView imgAvatar;
    private TextView tvName;
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;

    private ExcerptFragment excerptFragment;
    private UnscrambleFragment unscrambleFragment;
    private List<Fragment> fragments = new ArrayList<>();

    public MonthlyDetailsBean detailsBean;

    private MagazineDetailsBean magadetailsBean;

    private int scrollY = 0;
    private View contentView;

    private int priceInsufficient;
    private FragmentTransaction transaction;
    private TextView tvReadPurchase;
    private TextView tvReadPrice;

    private BalanceBean balanceBean;

    private PopupWindow morePopupWindow;

    private PopupWindow sharePopupWindow;
    private ImageView imgFriendscircleShare;
    private ImageView imgWxShare;
    private TextView tvChangeColor;

    private PopupWindow purchasePopupWindow;
    private TextView tvShopPrice;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvContentThree;
    private TextView tvContentFour;
    private TextView tvContentFive;
    private TextView tvContentSix;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalance;
    private TextView tvBalanceMoney;
    private TextView tvSaveMoney;
    private double price;
    private int paymentMode = 0;
    private String id;
    private String ShareId;
    private FloatingActionButton flMagazineTop;

    private IntentFilter intentFilter;
    private IsshareonBean isshareonBean;
    private TextView tvContentThree1;
    private TextView tvContentFour1;
    private TextView tvContentFive1;
    private TextView tvContentSix1;

    @Override
    protected int setContentView() {
        instance = this;
        EventBus.getDefault().register(this);
        return R.layout.activity_magazine_details;
    }

    public static MagazineDetailsActivity getInstance() {
        return instance;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        tvReadPurchase = (TextView) findViewById(R.id.tv_read_purchase);
        tvReadPrice = (TextView) findViewById(R.id.tv_read_price);
        appBar = (AppBarLayout) findViewById(app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        imgRoot = (ImageView) findViewById(R.id.img_root);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mToolbar = (Toolbar) findViewById(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        flMagazineTop = (FloatingActionButton) findViewById(R.id.fl_magazine_top);
    }

    @Override
    protected void initAction() {
        toHttpIsShareon();
        initMagicIndicator();

        initToolBar();

        intentFilter = new IntentFilter();
        intentFilter.addAction("action.unscramble.shopping");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        toHttpMagazineDetails();
        tvReadPurchase.setOnClickListener(this);
        tvReadPrice.setOnClickListener(this);
        findViewById(R.id.img_return).setOnClickListener(this);
        flMagazineTop.setOnClickListener(this);
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
                if (Math.abs(verticalOffset) >= Util.dip2px(MagazineDetailsActivity.this, 206)) {
                    tvTitle.setBackgroundColor(Color.BLACK);
                } else {
                    tvTitle.setBackgroundColor(Color.TRANSPARENT);
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

    private void initMagicIndicator() {
        final List<String> list = new ArrayList<>();
        list.add("解读");
        list.add("节选");
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2));
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 50));
                linePagerIndicator.setColors(Color.parseColor("#f1312e"));
                return linePagerIndicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        unscrambleFragment = new UnscrambleFragment();
        excerptFragment = new ExcerptFragment();
        fragments.add(unscrambleFragment);
        fragments.add(excerptFragment);
        InfomationVPAdapter adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, list);
        viewPager.setAdapter(adapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String isFo = intent.getStringExtra("tab");
            if (TextUtils.equals("tab", isFo)) {
            } else {
                showShopping();
            }

        }
    };


    //查询分销是否开启
    private void toHttpIsShareon() {
        ApiServerManager.getInstance().getApiServer().isShareon().enqueue(new RetrofitCallBack<IsshareonBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<IsshareonBean>> call, Response<RetrofitResult<IsshareonBean>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().isState() == true) {
                        isshareonBean = response.body().getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<IsshareonBean>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_read_price:
                if (!Util.hintLogin(this))
                    return;
                if (balanceBean != null) {
                    showShopping();
                } else {
                    toHttpMagazineDetails();
                    EventMsg msgL = EventMsg.obtain(SystemConstant.ON_MAGAZINE_LOGINE_NOTIFY);
                    EventBus.getDefault().post(msgL);
                }
                break;
            case R.id.tv_read_purchase:
                if (detailsBean.getPermission() == 1) {
                    showSharePop();
                } else {
                    showMorePop();
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
                    ToastMaker.showShortToast("请稍后");
                    wxPay();
                } else if (paymentMode == 3) {
                    ToastMaker.showShortToast("请稍后");
                    aliPay();
                } else {
                    ToastMaker.showShortToast("请选择支付方式");
                }
                break;
            case R.id.fl_magazine_top:
                if (viewPager.getCurrentItem() == 0) {
                    unscrambleFragment.goTop();
                }
                break;
        }
    }

    //余额支付
    private void balancePayment() {
        Util.finishPop(MagazineDetailsActivity.this, purchasePopupWindow);
        if (priceInsufficient == 1) {
            ToastMaker.showShortToast("余额不足");
        } else {
            showWaringDialog("支付", "是否使用" + detailsBean.getMonthlyDB().getPrice() + "墨子币？", new OnDialogButtonClickListener() {
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

    private void showMorePop() {
        if (morePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_distribution_un, null);
            tvContentThree1 = (TextView) view.findViewById(R.id.tv_content_three);
            tvContentFour1 = (TextView) view.findViewById(R.id.tv_content_four);
            tvContentFive1 = (TextView) view.findViewById(R.id.tv_content_five);
            tvContentSix1 = (TextView) view.findViewById(R.id.tv_content_six);


            /*如果接口调用失败或者返回为空*/
            if (null == isshareonBean) {
                isshareonBean = new IsshareonBean();
                isshareonBean.setAncestor(null);
                isshareonBean.setParent("0%");
                isshareonBean.setDiscount("10");
            }

            String threeStart = "1. 通过你分享的链接购买本商品可以享受原价";
            String discount = "<font color='red'>" + isshareonBean.getDiscount().toString() + "</font>";
            String threeEnd = "折优惠；";
            String content = threeStart + discount + threeEnd;
            tvContentThree1.setText(Html.fromHtml(content));

            String fourStart = "2. 通过你分享的链接购买成功，订单金额的";
            String parent = "<font color='red'>" + isshareonBean.getParent().toString() + "</font>";
            String fourEnd = "将做为你的奖学金；";
            String contentFour = fourStart + parent + fourEnd;
            tvContentFour1.setText(Html.fromHtml(contentFour));
            String ancestor = isshareonBean.getAncestor();

            if (!TextUtils.equals("null", ancestor) && !TextUtils.isEmpty(ancestor)) {
                String fiveStart = "3. 通过你分享的链接购买本产品，商品被第二次被分享并产生购买时，订单金额的";
                ancestor = "<font color='red'>" + ancestor + "</font>";
                String fiveEnd = "将做为你的奖学金；";
                String contentfive = fiveStart + ancestor + fiveEnd;
                tvContentFive1.setVisibility(View.VISIBLE);
                tvContentFive1.setText(Html.fromHtml(contentfive));
                tvContentSix1.setText("4. 获得奖学金可以在“我的”->“我的奖学金”中查看。");
            } else {
                tvContentFive1.setVisibility(View.GONE);
                tvContentSix1.setText("3. 获得奖学金可以在“我的”->“我的奖学金”中查看。");
            }

            morePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            morePopupWindow.setContentView(view);
            morePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            morePopupWindow.setFocusable(true);
            morePopupWindow.setOutsideTouchable(true);
            morePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            morePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
            morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop((Activity) mContext, morePopupWindow);
                }
            });
            view.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop((Activity) mContext, morePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            morePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
        }
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_share_magazine, null);
            imgFriendscircleShare = (ImageView) view.findViewById(R.id.img_friendscircle_share);
            imgWxShare = (ImageView) view.findViewById(R.id.img_wx_share);
            tvContentThree = (TextView) view.findViewById(R.id.tv_content_three);
            tvContentFour = (TextView) view.findViewById(R.id.tv_content_four);
            tvContentFive = (TextView) view.findViewById(R.id.tv_content_five);
            tvContentSix = (TextView) view.findViewById(R.id.tv_content_six);

            /*如果接口调用失败或者返回为空*/
            if (null == isshareonBean) {
                isshareonBean = new IsshareonBean();
                isshareonBean.setAncestor("10%");
                isshareonBean.setParent("20%");
                isshareonBean.setDiscount("9.5");
            }

            String threeStart = "1. 通过你分享的链接购买专栏可以享受原价";
            String discount = "<font color='red'>" + isshareonBean.getDiscount().toString() + "</font>";
            String threeEnd = "折优惠；";
            String content = threeStart + discount + threeEnd;
            tvContentThree.setText(Html.fromHtml(content));

            String fourStart = "2. 通过你分享的链接产生购买时，订单金额的";
            String parent = "<font color='red'>" + isshareonBean.getParent().toString() + "</font>";
            String fourEnd = "将做为你的奖学金；";
            String contentFour = fourStart + parent + fourEnd;
            tvContentFour.setText(Html.fromHtml(contentFour));
            String ancestor = isshareonBean.getAncestor();

//            if (!TextUtils.equals("null", ancestor) && !TextUtils.isEmpty(ancestor)) {
            String fiveStart = "3. 通过购买者再次分享连接产生购买时，订单金额的";
            ancestor = "<font color='red'>" + ancestor + "</font>";
            String fiveEnd = "将做为你的奖学金；";
            String contentfive = fiveStart + ancestor + fiveEnd;
            tvContentFive.setVisibility(View.VISIBLE);
            tvContentFive.setText(Html.fromHtml(contentfive));
            tvContentSix.setText("4. 获得的奖学金可以在“我的”->“我的奖学金”中查看。");
//            } else {
//                tvContentFive.setVisibility(View.GONE);
//                tvContentSix.setText("3. 获得奖学金可以在“我的”->“我的奖学金”中查看。");
//            }

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
            sharePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop((Activity) mContext, sharePopupWindow);
                }
            });

            Map<String, Object> map = new HashMap<>();
            map.put("goodsid", detailsBean.getMonthlyDB().getId());
            ApiServerManager.getInstance().getApiServer().getShareId(map).enqueue(new RetrofitCallBack<String>() {
                @Override
                public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                    if (response.body().getCode() == 200) {
                        ShareId = response.body().getData();
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {

                }
            });

            imgWxShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MagazineDetailsActivity.this.shareWeb(
                            SHARE_MEDIA.WEIXIN,
                            detailsBean.getMonthlyDB().getName(),
                            TextUtils.isEmpty(detailsBean.getMonthlyDB().getDesc()) ? "墨子杂志" : detailsBean.getMonthlyDB().getDesc(),
                            SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getCoverimg(),
                            SystemConstant.MYURL + "magazine/sharebuy/" + ShareId,
                            null);
                }
            });
            imgFriendscircleShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MagazineDetailsActivity.this.shareWeb(
                            SHARE_MEDIA.WEIXIN_CIRCLE,
                            detailsBean.getMonthlyDB().getName(),
                            TextUtils.isEmpty(detailsBean.getMonthlyDB().getDesc()) ? "墨子杂志" : detailsBean.getMonthlyDB().getDesc(),
                            SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getCoverimg(),
                            SystemConstant.MYURL + "magazine/sharebuy/" + ShareId,
                            null);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
        }
    }


    private void toHttpMagazineDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().GetMonthlyDetails(map)
                .enqueue(new RetrofitCallBack<MonthlyDetailsBean>() {
                             @Override
                             public void onSuccess(Call<RetrofitResult<MonthlyDetailsBean>> call, Response<RetrofitResult<MonthlyDetailsBean>> response) {
                                 if (response.body().getCode() == 200) {
                                     detailsBean = response.body().getData();
                                     toHttpPostBalance();
                                     if (detailsBean.getPermission() > 0) {
                                         tvReadPrice.setText("已购");
                                         tvReadPrice.setTextColor(Color.parseColor("#f1312e"));
                                         tvReadPrice.setEnabled(false);
                                     } else {
                                         tvReadPrice.setText("购买：" + detailsBean.getMonthlyDB().getPrice() + " 墨子币");
                                         tvReadPrice.setTextColor(Color.parseColor("#f1312e"));
                                         tvReadPrice.setEnabled(true);
                                     }
                                     tvReadPurchase.setEnabled(true);
                                     Glide.with(mContext).load(Util.getImgUrl(detailsBean.getMonthlyDB().getCoverimg()))
                                             .error(R.drawable.icon_banner_load)
                                             .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(MagazineDetailsActivity.this, 25, 2))
                                             .placeholder(R.drawable.icon_banner_load)
                                             .into(imgRoot);
                                     Glide.with(mContext).load(Util.getImgUrl(detailsBean.getMonthlyDB().getCoverimg()))
                                             .error(R.drawable.icon_readload_failed)
                                             .placeholder(R.drawable.icon_readload_failed)
                                             .into(imgAvatar);
                                     tvName.setText("《" + detailsBean.getMonthlyDB().getName() + "》");
                                     unscrambleFragment.setId(detailsBean.getMonthlyDB().getId(), detailsBean.getMonthlyDB().getCoverimg());
                                     excerptFragment.setId(detailsBean.getMonthlyDB().getId());
//                                     toHttpSeriesDetails();
                                 }
                             }

                             @Override
                             public void onFailure(Call<RetrofitResult<MonthlyDetailsBean>> call, Throwable
                                     t) {

                             }
                         }

                );
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
                    balanceBean = response.body().getData();
                    if (response.body().getData().getBalance() < detailsBean.getMonthlyDB().getPrice()) {
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


    //购买
    private void showShopping() {
        if (purchasePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_buy_column_win, null);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            llBalance = (AutoLinearLayout) view.findViewById(R.id.ll_balance);
            llWxMoney = (AutoLinearLayout) view.findViewById(R.id.ll_wx_money);
            llZfMoney = (AutoLinearLayout) view.findViewById(R.id.ll_zf_money);
            tvWx = (TextView) view.findViewById(R.id.tv_wx);
            tvZf = (TextView) view.findViewById(R.id.tv_zf);
            tvBalanceMoney = (TextView) view.findViewById(R.id.tv_balance_money);
            if (!TextUtils.isEmpty(detailsBean.getMonthlyDB().getPrice() + "")) {
                tvShopPrice.setText(detailsBean.getMonthlyDB().getPrice() + "");
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
            purchasePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
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
            purchasePopupWindow.showAtLocation(findViewById(R.id.rl_magazine_details), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 余额支付
     */
    private void toHttpBalancePay() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getMonthlyDB().getId());
        map.put("payment", "4");
        ApiServerManager.getInstance().getApiServer().GetMonthlyBuy(map).enqueue(new RetrofitCallBack<String>() {
            @Override
            public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                loadingDismiss();
                if (response.body().getCode() == 200) {
                    ToastMaker.showShortToast("购买成功");
                    toHttpMagazineDetails();
                    EventMsg msgL = EventMsg.obtain(SystemConstant.ON_MAGAZINE_LOGINE_NOTIFY);
                    EventBus.getDefault().post(msgL);
                } else {
                    ToastMaker.showShortToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {

            }
        });
    }
//

    /**
     * 支付宝支付
     */
    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getMonthlyDB().getId());
        map.put("payment", "1");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.POST_BUY_MAGAZINE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MagazineDetailsActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                        toHttpMagazineDetails();
                                        EventMsg msgL = EventMsg.obtain(SystemConstant.ON_MAGAZINE_LOGINE_NOTIFY);
                                        EventBus.getDefault().post(msgL);
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
//
//

    /**
     * 微信支付
     */
    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", detailsBean.getMonthlyDB().getId());
        map.put("payment", "2");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.POST_BUY_MAGAZINE, map, new HttpUtil.HttpCallBack() {
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
                    IWXAPI api = WXAPIFactory.createWXAPI(MagazineDetailsActivity.this, req.appId);
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
                    toHttpMagazineDetails();
                    EventMsg msgL = EventMsg.obtain(SystemConstant.ON_MAGAZINE_LOGINE_NOTIFY);
                    EventBus.getDefault().post(msgL);
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
