package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.ClassBookedAdapter;
import com.yidiankeyan.science.information.entity.BookedAlbumBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

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
 * -图文专辑
 * -最热
 */
public class ClsTxtImgBookedFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ClassBookedAdapter adapter;
    private ListViewFinalLoadMore lvBooked;
    private AutoLinearLayout llClassTxt;
    //页面数据
    private List<BookedAlbumBean> mDatas = new ArrayList<>();
    private int pages = 1;

    public ClsTxtImgBookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_cls_txt_img_booked, container, false);
        //初始化控件
        initView(view);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                addData();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else
            lvBooked.setHasLoadMore(true);
        lvBooked.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                addData();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new ClassBookedAdapter(getActivity(), mDatas);
        adapter.setLlClassTxt(llClassTxt);
        lvBooked.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }


    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("classifyType", "HOT");
        entity.put("albumType", 1);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BookedAlbumBean> mData = GsonUtils.json2List(jsonData, BookedAlbumBean.class);
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


    /**
     * 置顶、置底
     *
     * @param msg
     */
    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_TOP:
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
                adapter.notifyDataSetChanged();
                ptrLayout.autoRefresh();
                break;

            case SystemConstant.ON_CLICK_BOTTOM:
//                String orderIds = (String) ;
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
                adapter.notifyDataSetChanged();
                ptrLayout.autoRefresh();
                break;

        }
    }


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvBooked = (ListViewFinalLoadMore) view.findViewById(R.id.lv_clstxtbooked);
        llClassTxt = (AutoLinearLayout) getActivity().findViewById(R.id.ll_class_txt);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
