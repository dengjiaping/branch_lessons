package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.ColumnDetailActivity;
import com.yidiankeyan.science.information.acitivity.ColumnIntroductionActivity;
import com.yidiankeyan.science.information.acitivity.MagazineDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.adapter.PurchaseHotRecAdapter;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.my.adapter.PurchaseMyAdapter;
import com.yidiankeyan.science.purchase.entity.ColumnPurchaseBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

/**
 * 已购
 */
public class PurchaseMyFragment extends Fragment implements View.OnClickListener {

    private TextView mainTitleTxt;
    private int Page = 1;
    private PtrClassicFrameLayout ptrLayout;
    private GridViewFinal gvPurchase;
    private PurchaseMyAdapter adapter;
    private List<ColumnPurchaseBean> mLists = new ArrayList<>();
    private AutoLinearLayout llOrder;
    private AutoLinearLayout llScreen;
    private PopupWindow morePopupWindow;
    private PopupWindow screenPopupWindow;
    private int type = 0;
    private int orientation = 0;
    private TextView tvScreen;
    private TextView tvOrder;
    private AutoLinearLayout rlNoContent;
    private GridViewFinal gvRecShopping;
    private ArrayList<SubscribeColumnBean.ListBean> mColumnList = new ArrayList<>();
    private SubscribeColumnBean columnBean;
    private PurchaseHotRecAdapter hotRecAdapter;
    private AutoRelativeLayout mRlSelector;


    public PurchaseMyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_purchase_my, container, false);
        initView(view);
        initAction();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initAction() {
        mainTitleTxt.setText("已购");
        toHttpGetPurchaseNoContent();
        hotRecAdapter = new PurchaseHotRecAdapter(mColumnList, getContext());
        gvRecShopping.setAdapter(hotRecAdapter);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Page = 1;
                toHttpGetPurchaseColumn();
            }

        });
        if (!TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
            if (mLists.size() == 0) {
                ptrLayout.autoRefresh();
            }
        } else {
            gvPurchase.setHasLoadMore(false);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new PurchaseMyAdapter(getContext(), mLists);
        gvPurchase.setAdapter(adapter);
        gvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLists.get(position).getType() == 1) {
                    Intent intent = new Intent(getContext(), MozReadDetailsActivity.class);
                    intent.putExtra("id", mLists.get(position).getId());
                    intent.putExtra("name", mLists.get(position).getName());
                    intent.putExtra("isbuy", "1");
                    startActivity(intent);
                } else if (mLists.get(position).getType() == 2) {
                    Intent intent = new Intent(getContext(), MagazineDetailsActivity.class);
                    intent.putExtra("id", mLists.get(position).getId());
                    intent.putExtra("name", mLists.get(position).getName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ColumnDetailActivity.class);
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
        gvRecShopping.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ColumnIntroductionActivity.class);
                intent.putExtra("id", mColumnList.get(position).getId());
                intent.putExtra("name", mColumnList.get(position).getColumnName());
                intent.putExtra("price", mColumnList.get(position).getColumnPrice().replace(".00", ""));
                intent.putExtra("columnWriterIntro", mColumnList.get(position).getColumnWriterIntro());
                intent.putExtra("ishasactivityprice", mColumnList.get(position).getIshasactivityprice());
                intent.putExtra("columnActivityPrice", mColumnList.get(position).getColumnActivityPrice());
                startActivity(intent);
            }
        });
    }

    //热门推荐
    private void toHttpGetPurchaseNoContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageSize", 3);
        ApiServerManager.getInstance().getApiServer().getRandomColumn(map).enqueue(new RetrofitCallBack<SubscribeColumnBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<SubscribeColumnBean>> call, Response<RetrofitResult<SubscribeColumnBean>> response) {
                if (response.body().getCode() == 200) {
                    mColumnList.removeAll(mColumnList);
                    columnBean = response.body().getData();
                    mColumnList.addAll(columnBean.getList());
                    hotRecAdapter.notifyDataSetChanged();
                    if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                        rlNoContent.setVisibility(View.VISIBLE);
                        ptrLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<SubscribeColumnBean>> call, Throwable t) {
            }
        });
    }

    private void initView(View view) {
        mainTitleTxt = (TextView) view.findViewById(R.id.maintitle_txt);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        gvPurchase = (GridViewFinal) view.findViewById(R.id.gv_purchase);
        llOrder = (AutoLinearLayout) view.findViewById(R.id.ll_order);
        llScreen = (AutoLinearLayout) view.findViewById(R.id.ll_screen);
        tvScreen = (TextView) view.findViewById(R.id.tv_screen);
        tvOrder = (TextView) view.findViewById(R.id.tv_order);
        rlNoContent = (AutoLinearLayout) view.findViewById(R.id.rl_no_content);
        gvRecShopping = (GridViewFinal) view.findViewById(R.id.gv_rec_shopping);
        mRlSelector = (AutoRelativeLayout) view.findViewById(R.id.rl_selector);
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

                                     mLists.addAll(response.body().getData());
                                     if (mLists.size() > 0) {
                                         ptrLayout.setVisibility(View.VISIBLE);
                                         rlNoContent.setVisibility(View.GONE);
                                         gvPurchase.setHasLoadMore(true);
                                         Page++;
                                         ptrLayout.onRefreshComplete();
                                         adapter.notifyDataSetChanged();
                                     } else {
                                         rlNoContent.setVisibility(View.VISIBLE);
                                         ptrLayout.setVisibility(View.GONE);
                                         gvPurchase.setHasLoadMore(false);
                                         gvPurchase.showFailUI();
                                         ptrLayout.onRefreshComplete();
                                     }
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
            case R.id.ll_order:
                showMorePop(llOrder);
                break;
            case R.id.ll_lately_book:
                tvOrder.setText("最近阅读");
                orientation = 0;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), morePopupWindow);
                break;
            case R.id.ll_lately_shopping:
                tvOrder.setText("最近购买");
                orientation = 1;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), morePopupWindow);
                break;
            case R.id.ll_lately_update:
                tvOrder.setText("最近更新");
                orientation = 2;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), morePopupWindow);
                break;
            case R.id.ll_screen:
                showScreenPop(llScreen);
                break;
            case R.id.ll_screen_all:
                tvScreen.setText("全部");
                type = 0;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), screenPopupWindow);
                break;
            case R.id.ll_screen_column:
                tvScreen.setText("专栏");
                type = 3;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), screenPopupWindow);
                break;
            case R.id.ll_screen_magazine:
                tvScreen.setText("杂志");
                type = 2;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), screenPopupWindow);
                break;
            case R.id.ll_screen_book:
                tvScreen.setText("书籍");
                type = 1;
                Page = 1;
                toHttpGetPurchaseColumn();
                Util.finishPop(getActivity(), screenPopupWindow);
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_purchase_order, null);
            morePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_lately_book).setOnClickListener(this);
            view.findViewById(R.id.ll_lately_shopping).setOnClickListener(this);
            view.findViewById(R.id.ll_lately_update).setOnClickListener(this);
            morePopupWindow.setContentView(view);
            morePopupWindow.setAnimationStyle(R.style.popwin_msg_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            morePopupWindow.setFocusable(true);
            morePopupWindow.setOutsideTouchable(true);
            morePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            morePopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int i = morePopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (Util.getScreenWidth(getContext()) * scale);
            morePopupWindow.showAsDropDown(v, x - i, 0);
            morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), morePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_purchase_screen, null);
            screenPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.findViewById(R.id.ll_screen_all).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_column).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_magazine).setOnClickListener(this);
            view.findViewById(R.id.ll_screen_book).setOnClickListener(this);
            screenPopupWindow.setContentView(view);
            screenPopupWindow.setAnimationStyle(R.style.popwin_msg_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            screenPopupWindow.setFocusable(true);
            screenPopupWindow.setOutsideTouchable(true);
            screenPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            screenPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int i = screenPopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (Util.getScreenWidth(getContext()) * scale);
            screenPopupWindow.showAsDropDown(v, x - i, 0);
            screenPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), screenPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            int i = screenPopupWindow.getContentView().getMeasuredWidth();
            double scale = 21.0 / 750.0;
            int x = (int) (SystemConstant.ScreenWidth * scale);
            screenPopupWindow.showAsDropDown(v, x - i, 0);
        }
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_COLUMN_FLASH:
                if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                    rlNoContent.setVisibility(View.VISIBLE);
                    ptrLayout.setVisibility(View.GONE);
                } else {
                    toHttpGetPurchaseColumn();
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
