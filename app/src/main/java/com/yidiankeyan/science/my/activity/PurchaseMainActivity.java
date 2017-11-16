package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.ColumnDetailActivity;
import com.yidiankeyan.science.information.acitivity.MagazineDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.adapter.PurchaseHotRecAdapter;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.my.adapter.PurchaseMyAdapter;
import com.yidiankeyan.science.purchase.adapter.PurchaseAdapter;
import com.yidiankeyan.science.purchase.entity.ColumnPurchaseBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;


//我的已购
public class PurchaseMainActivity extends BaseActivity {

    private ImageView llReturn;
    private PurchaseAdapter adapter;
    private int Page = 1;
    private PtrClassicFrameLayout ptrLayout;
    private GridViewFinal gvPurchase;
    private List<ColumnPurchaseBean> mLists = new ArrayList<>();
    private AutoLinearLayout llOrder;
    private AutoLinearLayout llScreen;
    private PopupWindow morePopupWindow;
    private PopupWindow screenPopupWindow;
    private int type = 0;
    private int orientation = 0;
    private TextView tvScreen;
    private TextView tvOrder;
    private AutoRelativeLayout rlNoContent;

    @Override
    protected int setContentView() {
        return R.layout.activity_purchase_main;
    }

    @Override
    protected void initView() {
        llReturn = (ImageView) findViewById(R.id.img_return);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        gvPurchase = (GridViewFinal) findViewById(R.id.gv_purchase);
        llOrder = (AutoLinearLayout) findViewById(R.id.ll_order);
        llScreen = (AutoLinearLayout) findViewById(R.id.ll_screen);
        tvScreen = (TextView) findViewById(R.id.tv_screen);
        tvOrder = (TextView) findViewById(R.id.tv_order);
        rlNoContent = (AutoRelativeLayout) findViewById(R.id.rl_no_content);
    }

    @Override
    protected void initAction() {
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Page = 1;
                toHttpGetPurchaseColumn();
            }
        });
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "userId"))) {
            if (mLists.size() == 0) {
                ptrLayout.autoRefresh();
            }
        } else {
            gvPurchase.setHasLoadMore(false);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new PurchaseAdapter(PurchaseMainActivity.this, mLists);
        gvPurchase.setAdapter(adapter);
        gvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLists.get(position).getType() == 1) {
                    Intent intent = new Intent(PurchaseMainActivity.this, MozReadDetailsActivity.class);
                    intent.putExtra("id", mLists.get(position).getId());
                    intent.putExtra("name", mLists.get(position).getName());
                    intent.putExtra("isbuy", "1");
                    startActivity(intent);
                } else if (mLists.get(position).getType() == 2) {
                    Intent intent = new Intent(PurchaseMainActivity.this, MagazineDetailsActivity.class);
                    intent.putExtra("id", mLists.get(position).getId());
                    intent.putExtra("name", mLists.get(position).getName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PurchaseMainActivity.this, ColumnDetailActivity.class);
                    intent.putExtra("id", mLists.get(position).getId());
                    intent.putExtra("name", mLists.get(position).getName());
                    startActivity(intent);
                }
            }
        });
        gvPurchase.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetPurchaseColumn();
            }
        });
        llOrder.setOnClickListener(this);
        llScreen.setOnClickListener(this);
        llReturn.setOnClickListener(this);
    }

    private void toHttpGetPurchaseColumn() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", Page);
        map.put("pagesize", 20);
        map.put("orientation", orientation);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", type);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getColumnPurchased(map)
                .enqueue(new RetrofitCallBack<List<ColumnPurchaseBean>>() {
                             @Override
                             public void onSuccess(Call<RetrofitResult<List<ColumnPurchaseBean>>> call, Response<RetrofitResult<List<ColumnPurchaseBean>>> response) {
                                 if (response.body().getCode() == 200) {
                                     if (Page == 1) {
                                         mLists.removeAll(mLists);
                                     }
                                     gvPurchase.setHasLoadMore(true);
                                     mLists.addAll(response.body().getData());
                                     Page++;
                                     ptrLayout.onRefreshComplete();
                                     adapter.notifyDataSetChanged();
                                 } else {
                                     gvPurchase.setHasLoadMore(false);
                                     gvPurchase.showFailUI();
                                     ptrLayout.onRefreshComplete();
                                 }
                             }

                             @Override
                             public void onFailure(Call<RetrofitResult<List<ColumnPurchaseBean>>> call, Throwable t) {
                                 gvPurchase.setHasLoadMore(false);
                                 gvPurchase.showFailUI();
                                 ptrLayout.onRefreshComplete();
                             }
                         }

                );
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.ll_order:
                showMorePop(llOrder);
                break;
            case R.id.ll_lately_book:
                tvOrder.setText("最近阅读");
                orientation = 0;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, morePopupWindow);
                break;
            case R.id.ll_lately_shopping:
                tvOrder.setText("最近购买");
                orientation = 1;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, morePopupWindow);
                break;
            case R.id.ll_lately_update:
                tvOrder.setText("最近更新");
                orientation = 2;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, morePopupWindow);
                break;
            case R.id.ll_screen:
                showScreenPop(llScreen);
                break;
            case R.id.ll_screen_all:
                tvScreen.setText("全部");
                type = 0;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, screenPopupWindow);
                break;
            case R.id.ll_screen_column:
                tvScreen.setText("专栏");
                type = 3;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, screenPopupWindow);
                break;
            case R.id.ll_screen_magazine:
                tvScreen.setText("杂志");
                type = 2;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, screenPopupWindow);
                break;
            case R.id.ll_screen_book:
                tvScreen.setText("书籍");
                type = 1;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(this, screenPopupWindow);
                break;
        }
    }


    /**
     * 排序
     *
     * @param v
     */
    private void showMorePop(View v) {
        if (morePopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_purchase_order, null);
            morePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_lately_book).setOnClickListener(this);
            view.findViewById(R.id.ll_lately_shopping).setOnClickListener(this);
            view.findViewById(R.id.ll_lately_update).setOnClickListener(this);
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
                    Util.finishPop(PurchaseMainActivity.this, morePopupWindow);
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


    /**
     * 筛选
     */
    private void showScreenPop(View v) {
        if (screenPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_purchase_screen, null);
            screenPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_screen_all).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_column).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_magazine).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_book).setOnClickListener(this);
            screenPopupWindow.setContentView(view);
            screenPopupWindow.setAnimationStyle(R.style.popwin_msg_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            screenPopupWindow.setFocusable(true);
            screenPopupWindow.setOutsideTouchable(true);
            screenPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            screenPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int i = screenPopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (Util.getScreenWidth(this) * scale);
            screenPopupWindow.showAsDropDown(v, x - i, 0);
            screenPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(PurchaseMainActivity.this, screenPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            int i = screenPopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (SystemConstant.ScreenWidth * scale);
            screenPopupWindow.showAsDropDown(v, x - i, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
