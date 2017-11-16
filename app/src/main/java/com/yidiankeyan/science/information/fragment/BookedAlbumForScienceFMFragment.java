package com.yidiankeyan.science.information.fragment;

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

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 最热专辑
 * -墨子FM
 */
public class BookedAlbumForScienceFMFragment extends Fragment {


    private BookedAlbumAdapter adapter;
    private ListViewFinalLoadMore lvBooked;
    private PtrClassicFrameLayout ptrLayout;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private int pages = 0;
    private View llForFM;

    public BookedAlbumForScienceFMFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_booked_album_for_science_fm, container, false);

        //初始化控件
        initView(view);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetScienceFMOrder();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else
            lvBooked.setHasLoadMore(true);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvBooked.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetScienceFMOrder();
            }
        });
        adapter = new BookedAlbumAdapter(getActivity(), mDatas);
        adapter.setLlBooked(llForFM);
        lvBooked.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    /**
     * 获取赛思fm已订专辑
     */
    private void toHttpGetScienceFMOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongType", 2);
        entity.put("belongId", 1004);
        entity.put("albumType", 2);
        entity.put("classifyType", "HOT");
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BusinessAllBean> data = GsonUtils.json2List(jsonData, BusinessAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (data.size() > 0) {
                        lvBooked.setHasLoadMore(true);
                        mDatas.addAll(data);
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


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvBooked = (ListViewFinalLoadMore) view.findViewById(R.id.lv_booked_fm);
        llForFM = getActivity().findViewById(R.id.ll_for_fm);
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
    }
}
