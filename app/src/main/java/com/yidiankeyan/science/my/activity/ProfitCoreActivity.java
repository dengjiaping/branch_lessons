package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.LoginActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.ColumnAllActivity;
import com.yidiankeyan.science.my.adapter.ProfitDetailedAdapter;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.my.entity.BlanaceEarnBean;
import com.yidiankeyan.science.my.entity.ProfitDetailedBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的
 * -奖学金
 */
public class ProfitCoreActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private AutoLinearLayout llMore;
    private BlanaceEarnBean earnBean;
    private TextView tvTodayProfit;
    private TextView tvMonthProfit;
    private TextView tvSumMoney;
    private TextView tvCurrentMoney;
    private ImageView imgMoreFunction;
    private ListViewFinalLoadMore lvProfit;
    private List<ProfitDetailedBean> mData = new ArrayList<>();
    private PtrClassicFrameLayout mPtrLayout;
    private ProfitDetailedAdapter adapter;
    private int mPageNum = 1;
    private PopupWindow morePopupWindow;
    private double Balance;
    private AutoLinearLayout mNoSchoolMoney;
    private TextView mDetail;
    private TextView mGoHomeLook;


    @Override
    protected int setContentView() {
        return R.layout.activity_profit_core;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        llMore = (AutoLinearLayout) findViewById(R.id.ll_more);
        tvTodayProfit = (TextView) findViewById(R.id.tv_today_profit);
        tvMonthProfit = (TextView) findViewById(R.id.tv_month_profit);//18511583888  qq20070628
        tvSumMoney = (TextView) findViewById(R.id.tv_sum_money);
        tvCurrentMoney = (TextView) findViewById(R.id.tv_current_money);
        mPtrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvProfit = (ListViewFinalLoadMore) findViewById(R.id.lv_profit);
        imgMoreFunction = (ImageView) findViewById(R.id.img_more_function);
        mNoSchoolMoney = (AutoLinearLayout) findViewById(R.id.ll_no_school_money);
        mDetail = (TextView) findViewById(R.id.tv_detail);
        mGoHomeLook = (TextView) findViewById(R.id.tv_go_home_look);

    }

    @Override
    protected void initAction() {
        txtTitle.setText("我的奖学金");
        initData();
//        toHttpQueryBalance();
        //填充数据
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageNum = 1;
                postDatas();
            }
        });
        if (mData.size() == 0) {
            mPtrLayout.autoRefresh();
        } else {
            lvProfit.setHasLoadMore(true);
        }
        mPtrLayout.disableWhenHorizontalMove(true);
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new ProfitDetailedAdapter(this, mData);
        lvProfit.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvProfit.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (mPtrLayout.isRefreshing())
                    return;
                postDatas();
            }
        });
        llReturn.setOnClickListener(this);
        llMore.setOnClickListener(this);
        mGoHomeLook.setOnClickListener(this);
        mDetail.setOnClickListener(this);
    }


    private void postDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", mPageNum);
        map.put("pagesize", 10);

        ApiServerManager.getInstance().getApiServer().getBalanceEarnRecord(map).enqueue(new RetrofitCallBack<List<ProfitDetailedBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<ProfitDetailedBean>>> call, Response<RetrofitResult<List<ProfitDetailedBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (mPageNum == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvProfit.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        mPageNum++;

                    } else {
                        lvProfit.setHasLoadMore(false);
                    }
                    if (mData.size() > 0) {
                        mPtrLayout.setVisibility(View.VISIBLE);
                        mNoSchoolMoney.setVisibility(View.GONE);
                    } else {
                        mNoSchoolMoney.setVisibility(View.VISIBLE);
                        mPtrLayout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvProfit.showFailUI();
                    mPtrLayout.setVisibility(View.GONE);
                    mNoSchoolMoney.setVisibility(View.VISIBLE);
                }
                mPtrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<ProfitDetailedBean>>> call, Throwable t) {
                mPtrLayout.setVisibility(View.GONE);
                mNoSchoolMoney.setVisibility(View.VISIBLE);
                lvProfit.showFailUI();
            }
        });
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            Map<String, Object> map = new HashMap<>();
            ApiServerManager.getInstance().getApiServer().getBlanaceEarn(map).enqueue(new RetrofitCallBack<BlanaceEarnBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<BlanaceEarnBean>> call, Response<RetrofitResult<BlanaceEarnBean>> response) {
                    if (response.body().getCode() == 200) {
                        earnBean = response.body().getData();
                        tvCurrentMoney.setText(earnBean.getTotal() + "");
                        tvTodayProfit.setText(earnBean.getToday() + "");
                        tvMonthProfit.setText(earnBean.getMonth() + "");
                        tvSumMoney.setText(earnBean.getAllearn() + "");
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<BlanaceEarnBean>> call, Throwable t) {
                }
            });

        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }


    private void showMorePop(View v) {
        if (morePopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.title_profitcore_popup, null);
            morePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_add).setOnClickListener(this);
            morePopupWindow.setContentView(view);
            morePopupWindow.setAnimationStyle(R.style.popwin_msg_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            morePopupWindow.setFocusable(true);
            morePopupWindow.setOutsideTouchable(true);
            morePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            morePopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int i = morePopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (Util.getScreenWidth(this) * scale);
            morePopupWindow.showAsDropDown(v, x - i, 0);
            morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(ProfitCoreActivity.this, morePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            int i = morePopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (SystemConstant.ScreenWidth * scale);
            morePopupWindow.showAsDropDown(v, x - i, 0);
        }
    }


//    /**
//     * 账户余额
//     */
//    private void toHttpQueryBalance() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
//        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
//                if (response.body().getCode() == 200) {
//                    Balance = response.body().getData().getBalance();
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {
//            }
//        });
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_more:
                showMorePop(imgMoreFunction);
                break;
            case R.id.ll_add:
                Intent intent = new Intent(this, WithdrawalsApplyActivity.class);
                intent.putExtra("account", earnBean.getTotal() + "");
                startActivity(intent);
                Util.finishPop(ProfitCoreActivity.this, morePopupWindow);
                break;
            case R.id.tv_go_home_look:
                startActivity(new Intent(this, ColumnAllActivity.class));
                finish();
                break;
            case R.id.tv_detail:
                startActivity(new Intent(this, RewardMoneyActivity.class));
                break;
        }
    }
}
