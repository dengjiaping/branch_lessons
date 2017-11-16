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
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.BookedAlbumAdapter;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 科研号
 * -已订专辑
 */
public class ScNumBookedFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private BookedAlbumAdapter adapter;
    private ListViewFinal lvSc;
    private IntentFilter intentFilter;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private View llBooked;


    public ScNumBookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_sc_num_booked, container, false);

        intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        getContext().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        //初始化控件
        initView(view);

        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                toHttpGetScienceOrder();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);

        adapter = new BookedAlbumAdapter(getActivity(), mDatas);
        adapter.setLlBooked(llBooked);
        lvSc.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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


    /**
     * 获取科研号已订专辑
     */
    private void toHttpGetScienceOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 2);
        entity.put("belongid", 1002);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_BOOKED_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    mDatas.removeAll(mDatas);
                    List<BusinessAllBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                    mDatas.addAll(data);
                    adapter.notifyDataSetChanged();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
            }
        });
    }


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
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvSc = (ListViewFinal) view.findViewById(R.id.lv_scbooked);
        llBooked = getActivity().findViewById(R.id.ll_science);
    }

}
