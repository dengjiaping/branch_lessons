package com.yidiankeyan.science.information.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.CkassSubscribeAdapter;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 分类
 * -按专题分类
 * -已订专辑
 */
public class ClassBookedFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private CkassSubscribeAdapter adapter;
    private ListViewFinalLoadMore lvBooked;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private int id;
    private AutoRelativeLayout rlClassClick;
    private int pages = 1;

    public ClassBookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        id = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_class_booked, container, false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        getContext().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        //初始化控件
        initView(view);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetData();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvBooked.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new CkassSubscribeAdapter(getActivity(), mDatas);
        lvBooked.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetData();
            }
        });
        adapter.setLl_all(rlClassClick);
        lvBooked.setAdapter(adapter);
        adapter.setOnItemClickListener(new CkassSubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                startActivity(intent);
            }
        });
        return view;
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
                ptrLayout.autoRefresh();
            }
        }
    };

    private void toHttpGetData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 1);
        entity.put("belongid", id);
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALL_BOOKED_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BusinessAllBean> mData = GsonUtils.json2List(jsonData, BusinessAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (mData.size() > 0) {
                        lvBooked.setHasLoadMore(true);
                        mDatas.addAll(mData);
                        pages++;
                    } else {
                        lvBooked.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvBooked.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvBooked.showFailUI();
            }
        });
    }

//    private void addData() {
//
//        BusinessAllBean allBean1 = new BusinessAllBean();
//        allBean1.setTitle("牛博士");
//        allBean1.setImgUrl(R.drawable.niuboshi);
//        allBean1.setContentId("No145:");
//        allBean1.setContent("论如何做一个称职的绝症患者");
//        allBean1.setName("by 田阳");
//        allBean1.setContentNumber(156);
//        allBean1.setToPeekNumber(6481);
//        allBean1.setUpdateTime("20分钟前");
//
//        BusinessAllBean allBean2 = new BusinessAllBean();
//        allBean2.setTitle("院士之声");
//        allBean2.setImgUrl(R.drawable.yuanshi);
//        allBean2.setContentId("No311:");
//        allBean2.setContent("潘建伟：令人惊叹的存在！");
//        allBean2.setName("by 夏宇");
//        allBean2.setContentNumber(185);
//        allBean2.setToPeekNumber(15187);
//        allBean2.setUpdateTime("40分钟前");
//
//
//        mDatas.add(allBean1);
//        mDatas.add(allBean2);
//
//
//    }


    /**
     * 置顶、置底
     *
     * @param msg
     */
    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_TOP:
//                String orderId = (String);
                Map<String, Object> map = new HashMap<>();
                map.put("orderid", msg.getBody());
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_TOP, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
                ptrLayout.autoRefresh();
                adapter.notifyDataSetChanged();
                break;

            case SystemConstant.ON_CLICK_BOTTOM:
                Map<String, Object> maps = new HashMap<>();
                maps.put("orderid", msg.getBody());
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_CANCEL, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
                ptrLayout.autoRefresh();
                adapter.notifyDataSetChanged();
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getContext().unregisterReceiver(mRefreshBroadcastReceiver);
    }


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_class_booked);
        lvBooked = (ListViewFinalLoadMore) view.findViewById(R.id.lv_cls_booked);
        rlClassClick = (AutoRelativeLayout) getActivity().findViewById(R.id.rl_class_click);
    }
}
