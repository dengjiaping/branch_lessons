package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.InfomationVPAdapter;
import com.yidiankeyan.science.information.entity.ColumnDetailsBean;
import com.yidiankeyan.science.information.entity.IsshareonBean;
import com.yidiankeyan.science.information.fragment.ColumnArticleFragment;
import com.yidiankeyan.science.information.fragment.ColumnAudioFragment;
import com.yidiankeyan.science.my.activity.AccountBalanceActivity;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;
import static com.yidiankeyan.science.R.id.toolbar;

/**
 * 专栏
 * -详情
 */

public class ColumnDetailActivity extends BaseActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView imgRoot;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvTitleName;
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TheNewTodayAudioActivity.CollapsingToolbarLayoutState state;
    private AutoLinearLayout llColumnDetail;

    private ColumnArticleFragment articleFragment;
    private ColumnAudioFragment audioFragment;
    private List<Fragment> fragments = new ArrayList<>();

    //购买
    private AutoLinearLayout llColumnShopping;
    private int priceInsufficient = 0;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalanceMoney;

    /**
     * 分享
     */
    private PopupWindow sharePopupWindow;
    private PopupWindow venditionSharePop;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;

    private String mId;

    //分享收益框
    private PopupWindow reportPopupwindow;
    private TextView tvClear;
    private String shareSaleId;

    private ImageView imgMarketingShare;
    private TextView tvColumnDetails;
    private TextView tvContentThree;
    private TextView tvContentFour;
    private TextView tvContentFive;
    private TextView tvContentSix;
    private TextView tvContentSeven;

    private ColumnDetailsBean detailsBean;
    private IsshareonBean isshareonBean;
    private TextView mtvAcale;
    private TextView mtvDetail;
    private String mColumnWriterIntro;
    private int mIshasactivityprice;
    private String mColumnActivityPrice;
    private String mName;
    private String mPrice;
    private String mUpdateNum;
    private String mLinkurl;


    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_imagetext_audio;
    }

    @Override
    protected void initView() {
        imgRoot = (ImageView) findViewById(R.id.img_root);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mToolbar = (Toolbar) findViewById(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llColumnDetail = (AutoLinearLayout) findViewById(R.id.ll_column_detail);
        llColumnShopping = (AutoLinearLayout) findViewById(R.id.ll_column_shopping);
        imgMarketingShare = (ImageView) findViewById(R.id.img_marketing_share);
        mtvDetail = (TextView) findViewById(R.id.tv_detail);
        mtvDetail.setOnClickListener(this);
    }

    @Override
    protected void initAction() {
        mId = getIntent().getStringExtra("id");  //
        mName = getIntent().getStringExtra("name");
        mPrice = getIntent().getStringExtra("price");
        mColumnWriterIntro = getIntent().getStringExtra("columnWriterIntro");
        mIshasactivityprice = getIntent().getIntExtra("ishasactivityprice", 0);
        mColumnActivityPrice = getIntent().getStringExtra("columnActivityPrice");
        mUpdateNum = getIntent().getStringExtra("updateNum");
        mLinkurl = getIntent().getStringExtra("linkurl");
        toHttpIsShareon();
        initMagicIndicator();
        initToolBar();
        findViewById(R.id.img_return).setOnClickListener(this);
        imgMarketingShare.setOnClickListener(ColumnDetailActivity.this);
    }

    //查询分销是否开启
    private void toHttpIsShareon() {
        ApiServerManager.getInstance().getApiServer().isShareon().enqueue(new RetrofitCallBack<IsshareonBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<IsshareonBean>> call, Response<RetrofitResult<IsshareonBean>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().isState() == true) {
                        isshareonBean = response.body().getData();
                        imgMarketingShare.setVisibility(View.VISIBLE);
                    } else {
                        imgMarketingShare.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<IsshareonBean>> call, Throwable t) {

            }
        });
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
                        tvTitle.setText(getIntent().getStringExtra("name"));
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

    private void initMagicIndicator() {
        final List<String> list = new ArrayList<>();
        list.add("看图文");
        list.add("听音频");
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
                simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#000000"));
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
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 180));
                linePagerIndicator.setColors(Color.parseColor("#f1312e"));
                return linePagerIndicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });

        //文章
        articleFragment = new ColumnArticleFragment();
        //音频
        audioFragment = new ColumnAudioFragment();
        Bundle contentBundle = new Bundle();
        if (!TextUtils.isEmpty(mLinkurl)) {
            contentBundle.putString("id", mLinkurl);
        }else {
            contentBundle.putString("id", mId);
        }
        if (!StringUtils.isEmpty(mUpdateNum)) {
            contentBundle.putString("mUpdateNum", mUpdateNum);
        }
        articleFragment.setArguments(contentBundle);
        audioFragment.setArguments(contentBundle);

        fragments.add(articleFragment);
        fragments.add(audioFragment);
        InfomationVPAdapter adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, list);
        viewPager.setAdapter(adapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (detailsBean == null)
            toHttp();
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
            public void onSuccess(Call<RetrofitResult<ColumnDetailsBean>> call, Response<RetrofitResult<ColumnDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    Glide.with(ColumnDetailActivity.this).load(Util.getImgUrl(detailsBean.getColumnImg()))
                            .error(R.drawable.icon_banner_load)
                            .placeholder(R.drawable.icon_banner_load)
                            .into(imgRoot);
                    tvTitleName.setText(detailsBean.getColumnName());
                    tvName.setText(detailsBean.getColumnWriter());
                    if (TextUtils.equals("1", detailsBean.getHaveYouPurchased())) {
                        llColumnShopping.setVisibility(View.GONE);
                    } else {
                        llColumnShopping.setVisibility(View.VISIBLE);
                    }
                    imgMarketingShare.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ColumnDetailsBean>> call, Throwable t) {

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
                    Util.finishPop(ColumnDetailActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnDetailActivity.this, sharePopupWindow);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnDetailActivity.this.shareWeb(
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
                        ColumnDetailActivity.this.shareWeb(
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
                    HttpUtil.post(ColumnDetailActivity.this, SystemConstant.MYURL + SystemConstant.POST_COLUMN_POSTER + SpUtils.getStringSp(mContext, "userId") + "/" + detailsBean.getId(), map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            if (result.getCode() == 200) {
                                String poster = (String) result.getData();
                                Intent intent = new Intent(ColumnDetailActivity.this, ColumnPosterActivity.class);
                                intent.putExtra("poster", poster);
                                startActivity(intent);
                                Util.finishPop(ColumnDetailActivity.this, sharePopupWindow);
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
                    Util.finishPop(ColumnDetailActivity.this, sharePopupWindow);
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
                    Util.finishPop(ColumnDetailActivity.this, venditionSharePop);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnDetailActivity.this, venditionSharePop);
                }
            });

            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnDetailActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/shareSale/" + shareSaleId,
                                ""
                        );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailsBean != null)
                        ColumnDetailActivity.this.shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                detailsBean.getColumnName(),
                                detailsBean.getColumnTitle(),
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getColumnImg(),
                                SystemConstant.MYURL + "column4/shareSale/" + shareSaleId,
                                ""
                        );
                }
            });
            llSearchQQ.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    //获取海报地址
                    Map<String, Object> map = new HashMap<>();
                    HttpUtil.post(ColumnDetailActivity.this, SystemConstant.MYURL + SystemConstant.POST_COLUMN_POSTER + SpUtils.getStringSp(mContext, "userId") + "/" + detailsBean.getId(), map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            if (result.getCode() == 200) {
                                String poster = (String) result.getData();
                                Intent intent = new Intent(ColumnDetailActivity.this, ColumnPosterActivity.class);
                                intent.putExtra("poster", poster);
                                startActivity(intent);
                                Util.finishPop(ColumnDetailActivity.this, venditionSharePop);
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
                    Util.finishPop(ColumnDetailActivity.this, venditionSharePop);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            venditionSharePop.showAtLocation(llColumnDetail, Gravity.BOTTOM, 0, 0);
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
                    Util.finishPop(ColumnDetailActivity.this, reportPopupwindow);
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
                    Util.finishPop(ColumnDetailActivity.this, reportPopupwindow);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_detail:
                Intent intent = new Intent(ColumnDetailActivity.this, ColumnIntroductionActivity.class);
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
                startActivity(intent);
                break;
            case R.id.img_return:
                finish();
                break;
//            case R.id.img_share:
//                //分享
//                showSharePop();
//                break;
            case R.id.img_marketing_share:
                if (!TextUtils.equals("null", detailsBean.getHaveYouPurchased()) &&
                        !TextUtils.isEmpty(detailsBean.getHaveYouPurchased())) {
                    //分销分享
                    Map<String, Object> map = new HashMap<>();
                    if (!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                        map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                    } else map.put("userid", "");
                    map.put("goodsid", detailsBean.getId());
                    map.put("goodstype", "3");
                    ApiServerManager.getInstance().getApiServer().getColumnShareid(map).enqueue(new RetrofitCallBack<String>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                            if (response.body().getCode() == 200) {
                                shareSaleId = response.body().getData();
                                if (shareSaleId != null) {
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
        }
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
        EventBus.getDefault().unregister(this);
    }
}
