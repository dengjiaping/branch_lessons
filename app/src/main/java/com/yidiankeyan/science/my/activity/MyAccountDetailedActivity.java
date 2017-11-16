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

import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.adapter.AccountDetailedAdapter;
import com.yidiankeyan.science.my.entity.AccountDetailedBean;
import com.yidiankeyan.science.my.entity.BalanceBean;
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
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的钱包
 */
public class MyAccountDetailedActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private TextView tvSunMoney;
    private TextView tvProfitCore;
    private TextView tvProfitCoreInk;
    private ImageView imgMoreFunction;
    private PopupWindow morePopupWindow;
    private AutoLinearLayout llMore;

    //    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    private List<String> titles; //tab名称列表
//    private List<Fragment> fragments;// Tab页面列表
//    private InfomationVPAdapter adapter;
    private double Account;
    private ListViewFinalLoadMore lvBalanceRecord;
    private List<AccountDetailedBean> listData = new ArrayList<>();
    private AccountDetailedAdapter adapter;
    private int pages = 1;


    @Override
    protected int setContentView() {
//        EventBus.getDefault().register(this);
        return R.layout.activity_my_account_detailed;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        tvProfitCore = (TextView) findViewById(R.id.tv_profit_core);
        tvSunMoney = (TextView) findViewById(R.id.tv_sun_money);
        imgMoreFunction = (ImageView) findViewById(R.id.img_more_function);
        llMore = (AutoLinearLayout) findViewById(R.id.ll_more);
        lvBalanceRecord = (ListViewFinalLoadMore) findViewById(R.id.lv_balance_record);
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("我的钱包");
        llReturn.setOnClickListener(this);
//        initViewPager();
        toHttpQueryBalance();
        toHttpPostDetailed();
        if (listData.size() == 0) {

        } else {
            lvBalanceRecord.setHasLoadMore(true);
        }
        adapter = new AccountDetailedAdapter(this, listData);
        lvBalanceRecord.setAdapter(adapter);

        lvBalanceRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpPostDetailed();
            }
        });
        tvProfitCore.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }


    /**
     * 账户余额
     */
    private void toHttpQueryBalance() {
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("id", "");
        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                if (response.body().getCode() == 200) {
                    String length = Double.toString(response.body().getData().getBalance());
                    Account = response.body().getData().getBalance();
                    tvSunMoney.setText(response.body().getData().getBalance() + "");
//                    if (length.length() > 4 && length.length() < 7) {
//                        tvSunMoney.setTextSize(30);
//                        tvSunMoney.setText(response.body().getData().getBalance() + "");
//                    } else if (length.length() > 7) {
//                        tvSunMoney.setTextSize(20);
//                        tvSunMoney.setText(response.body().getData().getBalance() + "");
//                    } else {
//                        tvSunMoney.setText(response.body().getData().getBalance() + "");
//                    }
                    if (length.length() > 10) {
                        tvSunMoney.setTextSize(20);
                        tvSunMoney.setText(response.body().getData().getBalance() + "");
                    }
                }
//                toHttpInkBalance();
            }


            @Override
            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {
//                toHttpInkBalance();
            }
        });
    }

    /**
     * 明细列表
     */
    private void toHttpPostDetailed() {

        ApiServerManager.getInstance().getApiServer().getAccountDetailed(pages, 10).enqueue(new RetrofitCallBack<List<AccountDetailedBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AccountDetailedBean>>> call, Response<RetrofitResult<List<AccountDetailedBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        listData.remove(listData);
                    if (response.body().getData().size() > 0) {
                        lvBalanceRecord.setHasLoadMore(true);
                        List<AccountDetailedBean> data = response.body().getData();
                        listData.addAll(data.size() > 10 ? data.subList(0, 10) : data);
                        pages++;
                    } else {
                        lvBalanceRecord.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvBalanceRecord.showFailUI();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AccountDetailedBean>>> call, Throwable t) {
                lvBalanceRecord.showFailUI();
            }
        });
    }

//    /**
//     * 墨水余额
//     */
//    private void toHttpInkBalance() {
//        Map<String, Object> map = new HashMap<>();
//        ApiServerManager.getInstance().getApiServer().getInkBalance(map).enqueue(new RetrofitCallBack<InkBalanceBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<InkBalanceBean>> call, Response<RetrofitResult<InkBalanceBean>> response) {
//                if (response.body().getCode() == 200) {
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<RetrofitResult<InkBalanceBean>> call, Throwable t) {
//            }
//        });
//    }


//    private void initViewPager() {
//        fragments = new ArrayList<>();
//        fragments.add(new BalanceRecordFragment());
////        fragments.add(new InkRecordFragment());
//
//        titles = new ArrayList<>();
//        titles.add("余额记录");
////        titles.add("墨水记录");
//
//        //为TabLayout添加tab名称
//        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
////        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
//        adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, titles);
//
//        //viewpager加载adapter
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
//        tabLayout.setupWithViewPager(viewPager);
//    }

//    @Subscribe
//    public void onEvent(EventMsg msg) {
//        switch (msg.getWhat()) {
//            case SystemConstant.ON_BALANCE_STATE:
//                ptrLayout.onRefreshComplete();
//                break;
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_profit_core:
                startActivity(new Intent(this, AccountBalanceActivity.class));
                break;
            case R.id.ll_more:
                showMorePop(imgMoreFunction);
                break;
            case R.id.ll_create_group:
                Util.finishPop(MyAccountDetailedActivity.this, morePopupWindow);
                startActivity(new Intent(this, RechargeHelpActivity.class));
                break;
            case R.id.ll_create_chat_room:
                Util.finishPop(MyAccountDetailedActivity.this, morePopupWindow);
                startActivity(new Intent(this, RechargeAgreementActivity.class));
                break;
//            case R.id.ll_preservation_click:
//                startActivity(new Intent(this, MyDetailedAllActivity.class));
//                break;
        }
    }

    private void showMorePop(View v) {
        if (morePopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.title_account_popup, null);
            morePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_create_group).setOnClickListener(this);
            view.findViewById(R.id.ll_create_chat_room).setOnClickListener(this);
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
                    Util.finishPop(MyAccountDetailedActivity.this, morePopupWindow);
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

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
