package com.yidiankeyan.science.information.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.RecentAnswerAdapter;
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
 * -最新问答fragment
 */
public class RecentAnswerFragment extends Fragment {

    private ListViewFinalLoadMore lvRecentAnswer;
    private PtrClassicFrameLayout ptrLayout;
    private List<NewScienceHelp> listHelp = new ArrayList<>();
    private RecentAnswerAdapter adapter;
    private InputMethodManager inputMethodManager;
    private int pages = 1;

    public RecentAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_answer, container, false);
        initView(view);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetRecentAnswer();
            }
        });
        if (listHelp.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvRecentAnswer.setHasLoadMore(true);
        }
//        if (notMore)
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (listHelp.size() == 0)
//            toHttpGetRecentAnswer();
        adapter = new RecentAnswerAdapter(getContext(), listHelp);
        lvRecentAnswer.setAdapter(adapter);
        lvRecentAnswer.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetRecentAnswer();
            }
        });
        adapter.setOnEavesdropClick(new RecentAnswerAdapter.OnEavesdropClick() {
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
        return view;
    }

    private void toHttpGetRecentAnswer() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_RECENT_ANSWER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<NewScienceHelp> mData = GsonUtils.json2List(jsonData, NewScienceHelp.class);
                    if (pages == 1)
                        listHelp.removeAll(listHelp);
                    if (mData.size() > 0) {
                        lvRecentAnswer.setHasLoadMore(true);
                        listHelp.addAll(mData);
                        pages++;
                    } else {
                        lvRecentAnswer.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvRecentAnswer.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvRecentAnswer.showFailUI();
            }
        });
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvRecentAnswer = (ListViewFinalLoadMore) view.findViewById(R.id.lv_recent_answer);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    public void notifyList() {
        adapter.notifyDataSetChanged();
    }
}
