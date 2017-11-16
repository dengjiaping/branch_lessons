package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.EavedropTopAdapter;
import com.yidiankeyan.science.information.entity.EavedropMediaBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.greenrobot.eventbus.EventBus;
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
 * 科答
 * -偷听榜
 */
public class EavesdropTopFragment extends Fragment {

    private ListViewFinalLoadMore lvEavesdropTop;
    private PtrClassicFrameLayout ptrLayout;
    private List<NewScienceHelp> listHelp = new ArrayList<>();
    private EavedropTopAdapter adapter;
    private int pages = 1;

    public EavesdropTopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eavesdrop_top, container, false);
        initView(view);
        if (listHelp.size() == 0)
            ptrLayout.autoRefresh();
        else {
            lvEavesdropTop.setHasLoadMore(true);
        }
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetEavesdropTop();
            }
        });
        lvEavesdropTop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetEavesdropTop();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new EavedropTopAdapter(getContext(), listHelp);
        adapter.setOnEavesdropClick(new EavedropTopAdapter.OnEavesdropClick() {
            @Override
            public void onClick(int position) {
//                if (listHelp.get(position).getPayoff() == 0) {
//                    EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_ACTIVITY_SHOW_PAY);
//                    msg.setBody(listHelp.get(position));
//                    EventBus.getDefault().post(msg);
//                } else
                if (!Util.hintLogin((BaseActivity) getActivity()))
                    return;
                toHttpEavedrop(listHelp.get(position));
            }
        });
        lvEavesdropTop.setAdapter(adapter);
        return view;
    }

    /**
     * 获取资源
     *
     * @param scienceHelp
     */
    public void toHttpEavedrop(final NewScienceHelp scienceHelp) {
        long surplusTime = (scienceHelp.getKederDB().getAnswertime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
        if (surplusTime < 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("kederid", scienceHelp.getKederDB().getId());
            HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_KEDA_EAVEDROP_MEDIA, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (result.getCode() == 200) {
                        EavedropMediaBean eavedrop = (EavedropMediaBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), EavedropMediaBean.class);
                        if (eavedrop.getAudiourl().startsWith("/"))
                            AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + eavedrop.getAudiourl());
                        else
                            AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + eavedrop.getAudiourl());
                        AudioPlayManager.getInstance().setmMediaPlayId(scienceHelp.getKederDB().getId());
                        adapter.updatePermission(scienceHelp.getKederDB().getId());
                        adapter.notifyDataSetChanged();
                    } else if (result.getCode() == 101) {
                        EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_ACTIVITY_SHOW_PAY);
                        msg.setBody(scienceHelp);
                        EventBus.getDefault().post(msg);
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {

                }
            });
        } else {
            if (surplusTime == 0)
                surplusTime++;
            if (TextUtils.isEmpty(scienceHelp.getKederDB().getAnswerurl()))
                return;
            if (scienceHelp.getKederDB().getAnswerurl().startsWith("/"))
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + scienceHelp.getKederDB().getAnswerurl());
            else
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + scienceHelp.getKederDB().getAnswerurl());
            AudioPlayManager.getInstance().setmMediaPlayId(scienceHelp.getKederDB().getId());
            adapter.notifyDataSetChanged();
        }
    }

    private void toHttpGetEavesdropTop() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_EAVESDROPTOP, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<NewScienceHelp> mData = GsonUtils.json2List(jsonData, NewScienceHelp.class);
                    if (pages == 1)
                        listHelp.removeAll(listHelp);
                    if (mData.size() > 0) {
                        lvEavesdropTop.setHasLoadMore(true);
                        listHelp.addAll(mData);
                        pages++;
                    } else {
                        lvEavesdropTop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvEavesdropTop.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvEavesdropTop.showFailUI();
                ptrLayout.onRefreshComplete();
            }
        });
    }


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvEavesdropTop = (ListViewFinalLoadMore) view.findViewById(R.id.lv_eavesdrop_top);
    }

    public void notifyList() {
        adapter.notifyDataSetChanged();
    }

}
