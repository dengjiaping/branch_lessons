package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.IssuesForColumnAdapter;
import com.yidiankeyan.science.information.entity.ColumnDetailBean;
import com.yidiankeyan.science.information.entity.IssuesForColumnBean;
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
import com.yidiankeyan.science.view.ObservableScrollView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 订阅专栏
 * -专栏详情
 */
public class ColumnDetailsActivity extends BaseActivity {

    private AutoLinearLayout returnTitle;
    private TextView tvColumnContentList;
    private AutoRelativeLayout layoutTitle;
    private ImageView imgColumn;
    private TextView tvColumnDesc;
    private TextView tvColumnSub;
    private TextView tvColumnTitle;
    private WebView wvAlbumSummaryInfo;
    private TextView tvIssuessDesc;
    private TextView tvIssuessTitle;
    //    private TextView tvLearners;
//    private TextView tvLearnersInfo;
//    private TextView tvDesc;
//    private TextView tvDescInfo;
//    private TextView tvRecent;
//    private TextView tvRecentInfo;
    private ListViewFinalLoadMore lvIssues;
    private ImageView imgHeadAvatar;
    private TextView tvHear;
    private TextView tvContentNum;
    private ImageView imgOrder;
    private ObservableScrollView scrollView;
    private AutoLinearLayout llBottomColumn;
    private View headView;
    private IssuesForColumnAdapter adapter;
    private List<IssuesForColumnBean.S2CIssueModelsBean> mData = new ArrayList<>();
    private ColumnDetailBean columnDetailBean;
    private TextView tvPrice;
    private PopupWindow payPopupWindow;
    private AutoRelativeLayout rlAll;
    private TextView tvOrderInfo;
    private AutoRelativeLayout rlTitleBar;
    private ImageView titleReturn;
    private ImageView imgAudioMore;
    private int pages = 1;
    private AutoLinearLayout llAudioMore;
    private PopupWindow sharePopupWindow;

    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private ImageView imgShareSina;
    private AutoLinearLayout imgShareQQ;
    private TextView btnCancel;

    private SimpleDateFormat sdf;
    private Date date;
    private boolean isPurchase;
    private int priceInsufficient = 0;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_column_details;
    }

    private ListViewFinalLoadMore.OnScrollChangedListener scrollListener = new ListViewFinalLoadMore.OnScrollChangedListener() {

        @Override
        public void onScrollChanged(ListView who, int left, int top, int oldl, int oldt) {
            double height = SystemConstant.ScreenHeight * 0.2;
            double alpha = mY / height;
            if (alpha <= 1.0) {
                rlTitleBar.setAlpha((float) alpha);
                if (alpha > 0.5) {
                    titleReturn.setImageResource(R.drawable.returns);
                    imgAudioMore.setImageResource(R.drawable.icon_subsribe_more);
                } else {
                    titleReturn.setImageResource(R.drawable.receive_prize_return);
                    imgAudioMore.setImageResource(R.drawable.icon_white_more);
                }
            } else {
                rlTitleBar.setAlpha(1);
            }
        }
    };

    int mY;
    AbsListView.OnScrollListener scrollListener2 = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && view.getLastVisiblePosition() + 1 == view.getCount()) {// 如果滚动到最后一行
                lvIssues.onScorllBootom();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mY = getScrollY();
        }
    };

    public int getScrollY() {//获取滚动距离
        View c = lvIssues.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = lvIssues.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = lvIssues.getHeight();
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    @Override
    protected void initView() {
        rlAll = (AutoRelativeLayout) findViewById(R.id.rl_all);
        tvColumnContentList = (TextView) findViewById(R.id.tv_column_content_list);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        layoutTitle = (AutoRelativeLayout) findViewById(R.id.layout_title);
        imgColumn = (ImageView) findViewById(R.id.img_column);
        tvColumnDesc = (TextView) findViewById(R.id.tv_column_desc);
        tvColumnSub = (TextView) findViewById(R.id.tv_column_sub);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
        wvAlbumSummaryInfo = (WebView) findViewById(R.id.wv_album_summary_info);
        tvColumnTitle = (TextView) findViewById(R.id.tv_column_title);
//        tvLearners = (TextView) findViewById(R.id.tv_learners);
//        tvLearnersInfo = (TextView) findViewById(R.id.tv_learners_info);
//        tvDesc = (TextView) findViewById(R.id.tv_desc);
//        tvDescInfo = (TextView) findViewById(R.id.tv_desc_info);
        tvPrice = (TextView) findViewById(R.id.tv_price);
//        tvRecent = (TextView) findViewById(R.id.tv_recent);
//        tvRecentInfo = (TextView) findViewById(R.id.tv_recent_info);
        lvIssues = (ListViewFinalLoadMore) findViewById(R.id.lv_issues);
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        llBottomColumn = (AutoLinearLayout) findViewById(R.id.ll_bottom_column);
        headView = LayoutInflater.from(this).inflate(R.layout.head_column_detail_issues, null);
        tvContentNum = (TextView) headView.findViewById(R.id.tv_content_num);
        imgHeadAvatar = (ImageView) headView.findViewById(R.id.img_head_avatar);
        tvIssuessTitle = (TextView) headView.findViewById(R.id.tv_issuess_title);
        tvIssuessDesc = (TextView) headView.findViewById(R.id.tv_issuess_desc);
        imgOrder = (ImageView) headView.findViewById(R.id.img_order);
        //设置为正序
        imgOrder.setTag(0);
        rlTitleBar = (AutoRelativeLayout) findViewById(R.id.rl_title_bar);
        titleReturn = (ImageView) findViewById(R.id.title_return);
        imgAudioMore = (ImageView) findViewById(R.id.img_audio_more);
    }

    @Override
    protected void initAction() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        date = curr.getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        tvColumnContentList.setOnClickListener(this);
        returnTitle.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        llAudioMore.setOnClickListener(this);
        imgOrder.setOnClickListener(this);
//        layoutTitle.getBackground().setAlpha(0);
        rlTitleBar.setAlpha(0);
        lvIssues.addHeaderView(headView);
        WebSettings ws = wvAlbumSummaryInfo.getSettings();
        wvAlbumSummaryInfo.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        ws.setSupportZoom(true);          //支持缩放
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕x
        //设置自适应屏幕，两者合用
        ws.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);//
        adapter = new IssuesForColumnAdapter(this, mData);
        lvIssues.setAdapter(adapter);
        lvIssues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                Intent intent = new Intent(mContext, PreviewColumnContentActivity.class);
                intent.putExtra("id", mData.get(position - 1).getId());
                mData.get(position - 1).setEverRead(1);
                startActivity(intent);
            }
        });
        lvIssues.setOnScrollChangedListener(scrollListener);
        lvIssues.setOnScrollListener(scrollListener2);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                double height = SystemConstant.ScreenHeight * 0.2;
                double alpha = y / height;
                if (alpha <= 1.0) {
                    rlTitleBar.setAlpha((float) alpha);
                    if (alpha > 0.5) {
                        titleReturn.setImageResource(R.drawable.returns);
                        imgAudioMore.setImageResource(R.drawable.icon_subsribe_more);
                    } else {
                        titleReturn.setImageResource(R.drawable.receive_prize_return);
                        imgAudioMore.setImageResource(R.drawable.icon_white_more);
                    }
                } else {
                    rlTitleBar.setAlpha(1);
                }
            }
        });
        lvIssues.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetIssues();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (columnDetailBean == null || columnDetailBean.getPermission() == 0)
            toHttp();
        if (mData.size() > 0)
            adapter.notifyDataSetChanged();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_COLUMNS_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    columnDetailBean = (ColumnDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), ColumnDetailBean.class);
                    if (columnDetailBean.getPermission() == 0) {
                        lvIssues.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        llBottomColumn.setVisibility(View.VISIBLE);
                        Glide.with(ColumnDetailsActivity.this).load(Util.getImgUrl(columnDetailBean.getPoster()))
                                .error(R.drawable.icon_banner_load)
                                .placeholder(R.drawable.icon_banner_load)
                                .into(imgColumn);
//                        tvAlbumSummaryInfo.setText(columnDetailBean.getDescription());
                        wvAlbumSummaryInfo.loadUrl(SystemConstant.MYURL + "columns/desc/" + columnDetailBean.getId());
                        tvColumnSub.setText(columnDetailBean.getPurcharsings() + "人订阅");
//                        tvLearnersInfo.setText(columnDetailBean.getLearnersinfo());
//                        tvDescInfo.setText(columnDetailBean.getDescription());
//                        tvRecentInfo.setText(columnDetailBean.getLastupdatetitle());
                        tvColumnTitle.setText(columnDetailBean.getName());
                        tvColumnDesc.setText(columnDetailBean.getSummary());
                        tvPrice.setText("订购：¥" + columnDetailBean.getPrice() + "/年");
                    } else {
                        scrollView.setVisibility(View.GONE);
                        llBottomColumn.setVisibility(View.GONE);
                        lvIssues.setVisibility(View.VISIBLE);
                        toHttpGetIssues();
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpGetIssues() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        map.put("orientation", (int) imgOrder.getTag());
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_COLUMNS_ISSUES_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    IssuesForColumnBean data = (IssuesForColumnBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), IssuesForColumnBean.class);
                    if (pages == 1) {
                        mData.removeAll(mData);
                    }
                    if (data.getS2CIssueModels().size() > 0) {
                        lvIssues.setHasLoadMore(true);
                        mData.addAll(data.getS2CIssueModels());
                        pages++;
                    } else {
                        lvIssues.setHasLoadMore(false);
                    }
                    tvContentNum.setText("已更新" + data.getVols() + "篇文章");
                    tvIssuessTitle.setText(columnDetailBean.getName());
                    tvIssuessDesc.setText(columnDetailBean.getSummary());
                    Glide.with(ColumnDetailsActivity.this).load(Util.getImgUrl(data.getPoster())).into(imgHeadAvatar);
                    adapter.notifyDataSetChanged();
                } else {
                    lvIssues.showFailUI();
                }
                imgOrder.setEnabled(true);
                if ((int) imgOrder.getTag() == 0) {
                    imgOrder.setImageResource(R.drawable.orderupper);
                } else {
                    imgOrder.setImageResource(R.drawable.orderlower);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvIssues.showFailUI();
                imgOrder.setEnabled(true);
                if ((int) imgOrder.getTag() == 0) {
                    imgOrder.setImageResource(R.drawable.orderupper);
                } else {
                    imgOrder.setImageResource(R.drawable.orderlower);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_return:
                if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                    //如果mainActivity不存在则跳转主页面
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.tv_column_content_list:
                intent = new Intent(this, ColumnContentListActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                if (columnDetailBean != null)
                    intent.putExtra("name", columnDetailBean.getName());
                startActivity(intent);
                break;
            case R.id.tv_price:
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
                            if ( response.body().getData().getBalance() < columnDetailBean.getPrice()) {
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
                break;
            case R.id.img_order:
                int i = (int) v.getTag();
                i = i == 0 ? 1 : 0;
                imgOrder.setTag(i);
                imgOrder.setEnabled(false);
                pages = 1;
                toHttpGetIssues();
                break;
            case R.id.ll_audio_more:
                if (columnDetailBean == null)
                    return;
                showSharePop();
                break;
        }
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnDetailsActivity.this, sharePopupWindow);
                }
            });
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ColumnDetailsActivity.this, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
        }
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        columnDetailBean.getName(),
                        "墨子专栏",
                        SystemConstant.ALI_CLOUD + columnDetailBean.getCoverimg(),
                        SystemConstant.MYURL + "columns/share/" + getIntent().getStringExtra("id")
                        , null);
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        columnDetailBean.getName(),
                        "墨子专栏",
                        SystemConstant.ALI_CLOUD + columnDetailBean.getCoverimg(),
                        SystemConstant.MYURL + "columns/share/" + getIntent().getStringExtra("id")
                        , null);
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.QQ,
                        "墨子专栏",
                        columnDetailBean.getName(),
                        SystemConstant.ALI_CLOUD + columnDetailBean.getCoverimg(),
                        SystemConstant.MYURL + "columns/share/" + getIntent().getStringExtra("id")
                        , null);
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareWeb(
//                        SHARE_MEDIA.SINA,
//                        "墨子专栏",
//                        columnDetailBean.getName(),
//                        SystemConstant.ALI_CLOUD + columnDetailBean.getCoverimg(),
//                        SystemConstant.MYURL + "columns/share/" + getIntent().getStringExtra("id")
//                        , null);
//            }
//        });
    }

    private void showPayWindow() {
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnDetailsActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(ColumnDetailsActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(ColumnDetailsActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ColumnDetailsActivity.this, payPopupWindow);
                    if (priceInsufficient == 1) {
                        ToastMaker.showShortToast("余额不足");
                    } else {
                        showWaringDialog("支付", "是否使用" + columnDetailBean.getPrice() + "墨子币？", new OnDialogButtonClickListener() {
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
            WindowManager.LayoutParams lp = ColumnDetailsActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ColumnDetailsActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ColumnDetailsActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ColumnDetailsActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ColumnDetailsActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(rlAll, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将订阅" + sdf.format(new Date()) + "至" + sdf.format(date) + "，总金额为" + columnDetailBean.getPrice() + "。订阅后不支持退订、转让，请再次确认");
    }

    private void toHttpBalancePay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("payment", 4);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                    isPurchase = true;
                    toHttp();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("payment", 2);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ColumnDetailsActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                                        isPurchase = true;
                                        toHttp();
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

    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("payment", 1);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
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
                    IWXAPI api = WXAPIFactory.createWXAPI(ColumnDetailsActivity.this, req.appId);
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
        switch (msg.getWhat()) {
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                if (!(DemoApplication.getInstance().currentActivity() instanceof ColumnDetailsActivity))
                    return;
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                    isPurchase = true;
                    toHttp();
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.ON_USER_INFORMATION:
                toHttp();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (isPurchase == true) {
            Intent intent = new Intent();
            intent.setAction("action.read.audio");
            sendBroadcast(intent);
        }

    }
}
