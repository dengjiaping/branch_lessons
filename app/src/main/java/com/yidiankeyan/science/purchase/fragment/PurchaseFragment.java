package com.yidiankeyan.science.purchase.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;


/**
 * 已购
 */
public class PurchaseFragment extends Fragment {

//    private PtrClassicFrameLayout ptrLayout;
//    private GridViewFinal gvPurchase;
//    private PurchaseAdapter adapter;
//    private List<PurchaseBean> mLists = new ArrayList<>();
//    private List<PurchaseBean> tempList = new ArrayList<>();
//    private IntentFilter intentFilter;
//    private int pages = 1;
//    /**
//     * 0:正常状态<p>1:正在加载<p>2:加载完成<p>3:加载失败<p>4:没有更多</p>
//     */
//    private int columnState = 0;
//    private int bookState = 0;
//    private int magazineState = 0;
//
//    private int columnPage = 1;
//    private int bookPage = 1;
//    private int magazinePage = 1;
//
//    private boolean columnRequestFinish;
//    private boolean bookRequestFinish;
//    private boolean magazineRequestFinish;
//
//    private android.os.Handler mHandler = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (columnRequestFinish && bookRequestFinish&&magazineRequestFinish) {
//                if (columnState == 3 && bookState == 3 && magazineState == 3) {
//                    gvPurchase.showFailUI();
//                    ptrLayout.onRefreshComplete();
//                } else {
//                    if (pages == 1) {
//                        mLists.removeAll(mLists);
//                    }
//                    if (tempList.size() == 0) {
//                        gvPurchase.setHasLoadMore(false);
//                    } else {
//                        mLists.addAll(tempList);
//                        gvPurchase.setHasLoadMore(true);
//                    }
//                    ptrLayout.onRefreshComplete();
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
//    };

    public PurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
//        initView(view);
//        //填充数据
//        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                pages = 1;
//                columnPage = 1;
//                bookPage = 1;
//                magazinePage = 1;
//                tempList.removeAll(tempList);
////                toHttpGetPurchaseBook();
//                toHttpGetPurchaseMagazine();
//            }
//        });
//        if (!TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
//            if (mLists.size() == 0) {
//                ptrLayout.autoRefresh();
//            }
//        } else {
//            gvPurchase.setHasLoadMore(false);
//        }
//        ptrLayout.disableWhenHorizontalMove(true);
//        ptrLayout.setLastUpdateTimeRelateObject(this);
//        adapter = new PurchaseAdapter(getContext(), mLists);
//        gvPurchase.setAdapter(adapter);
//        gvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mLists.get(position).isColumn() == 1) {
//                    Intent intent = new Intent(getContext(), ColumnDetailsActivity.class);
//                    intent.putExtra("id", mLists.get(position).getColumnDetailBean().getId());
//                    getContext().startActivity(intent);
//                } else if (mLists.get(position).isColumn() == 2) {
//                    Intent intent = new Intent(getContext(), MozReadDetailsActivity.class);
//                    intent.putExtra("id", mLists.get(position).getBookBean().getBookid());
//                    intent.putExtra("name", mLists.get(position).getBookBean().getBookname());
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getContext(), MagazineDetailsActivity.class);
//                    intent.putExtra("id", mLists.get(position).getMonthlyDBBean().getMonthlyDB().getId());
//                    intent.putExtra("name", mLists.get(position).getMonthlyDBBean().getMonthlyDB().getName());
//                    startActivity(intent);
//                }
//            }
//        });
//        gvPurchase.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void loadMore() {
//                if (ptrLayout.isRefreshing())
//                    return;
//                pages++;
//                tempList.removeAll(tempList);
//                toHttpGetPurchaseBook();
//            }
//        });
        return view;
    }

//    private void toHttpGetPurchaseColumn() {
//        columnState = 1;
//        columnRequestFinish = false;
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", columnPage);
//        map.put("pagesize", 10);
//        Map<String, Object> entity = new HashMap<>();
//        entity.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
//        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_PURCHASE_COLUMN, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) {
//                if (result.getCode() == 200) {
//                    List<ColumnDetailBean> columnList = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ColumnDetailBean.class);
//                    if (columnList != null && columnList.size() > 0) {
//                        List<PurchaseBean> purchaseBeanList = new ArrayList<PurchaseBean>();
//                        for (ColumnDetailBean column : columnList) {
//                            purchaseBeanList.add(new PurchaseBean(column, null, null, 1));
//                        }
//                        tempList.addAll(purchaseBeanList);
//                        columnState = 2;
//                        columnPage++;
//                    } else {
//                        columnState = 4;
//                    }
//                } else {
//                    columnState = 3;
//                }
//                columnRequestFinish = true;
//                mHandler.sendEmptyMessage(0);
//            }
//
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                bookState = 3;
//                columnRequestFinish = true;
//                mHandler.sendEmptyMessage(0);
//            }
//        });
//    }
//
//    private void toHttpGetPurchaseBook() {
//        bookState = 1;
//        bookRequestFinish = false;
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", bookPage);
//        map.put("pagesize", 10);
//        map.put("orderstatus", "2");
//        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_PURCHASE_BOOK, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) {
//                if (result.getCode() == 200) {
//                    PurchaseBook purchaseBook = (PurchaseBook) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), PurchaseBook.class);
//                    if (purchaseBook.getList() != null && purchaseBook.getList().size() > 0) {
//                        List<PurchaseBean> purchaseBeanList = new ArrayList<PurchaseBean>();
//                        for (PurchaseBook.ListBean book : purchaseBook.getList()) {
//                            purchaseBeanList.add(new PurchaseBean(null, book, null, 2));
//                        }
//                        tempList.addAll(purchaseBeanList);
//                        bookState = 2;
//                        bookPage++;
//                    } else {
//                        bookState = 4;
//                    }
//                } else {
//                    bookState = 3;
//                }
//                bookRequestFinish = true;
//                mHandler.sendEmptyMessage(0);
//                toHttpGetPurchaseColumn();
//            }
//
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                bookState = 3;
//                bookRequestFinish = true;
//                mHandler.sendEmptyMessage(0);
//                toHttpGetPurchaseColumn();
//            }
//        });
//    }
//
//    private void toHttpGetPurchaseMagazine() {
//        magazineState = 1;
//        magazineRequestFinish = false;
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", magazinePage);
//        map.put("pagesize", 10);
//        ApiServerManager.getInstance().getApiServer().GetMagazinePurchase(map).enqueue(new RetrofitCallBack<List<MozMagazinePurchaseBean>>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<List<MozMagazinePurchaseBean>>> call, Response<RetrofitResult<List<MozMagazinePurchaseBean>>> response) {
//                if (response.body().getCode() == 200) {
//                    List<MozMagazinePurchaseBean> maList = response.body().getData();
//                    if (maList != null && maList.size() > 0) {
//                        List<PurchaseBean> purchaseBeanList = new ArrayList<PurchaseBean>();
//                        for (MozMagazinePurchaseBean monthlyDBBean : maList) {
//                            purchaseBeanList.add(new PurchaseBean(null, null, monthlyDBBean, 3));
//                        }
//                        tempList.addAll(purchaseBeanList);
//                        magazineState = 2;
//                        magazinePage++;
//                    } else {
//                        magazineState = 4;
//                    }
//                } else {
//                    magazineState = 3;
//                }
//                magazineRequestFinish = true;
//                mHandler.sendEmptyMessage(0);
//                toHttpGetPurchaseBook();
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<List<MozMagazinePurchaseBean>>> call, Throwable t) {
//                bookState = 3;
//                columnRequestFinish = true;
//                toHttpGetPurchaseBook();
//            }
//        });
//    }
//
//    private void initView(View view) {
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("action.read.audio");
//        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
//        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
//        gvPurchase = (GridViewFinal) view.findViewById(R.id.gv_purchase);
//    }
//
//    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("action.read.audio")) {
//                ptrLayout.autoRefresh();
//            }
//        }
//    };
//
//    @Subscribe
//    public void onEvent(EventMsg msg) {
//        switch (msg.getWhat()) {
//            case SystemConstant.ON_USER_LOGOUT:
//                mLists.removeAll(mLists);
//                adapter.notifyDataSetChanged();
//                pages = 1;
//                gvPurchase.setHasLoadMore(false);
//                break;
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
