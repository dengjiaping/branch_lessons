package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.AnswerTopDetailActivity;
import com.yidiankeyan.science.information.adapter.AnswerTopAdapter;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
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
 * 答人榜
 */
public class AnswerTopFragment extends Fragment {

    private ListViewFinalLoadMore lvAnswerTop;
    private AnswerTopAdapter adapter;
    private List<AnswerPeopleDetail> listHelp = new ArrayList<>();
    private PtrClassicFrameLayout ptrLayout;
    private int pages = 1;

    public AnswerTopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer_top, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetAnswerTop();
            }
        });
        if (listHelp.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvAnswerTop.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvAnswerTop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetAnswerTop();
            }
        });
        adapter = new AnswerTopAdapter(getContext(), listHelp);
        lvAnswerTop.setAdapter(adapter);
        lvAnswerTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //我的科答
                Intent intent = new Intent(getContext(), AnswerTopDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", listHelp.get(position).getId());
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 获取所有答人
     */
    private void toHttpGetAnswerTop() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ANSWER_TOP, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AnswerPeopleDetail> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    if (pages == 1)
                        listHelp.removeAll(listHelp);
                    if (data.size() > 0) {
                        lvAnswerTop.setHasLoadMore(true);
                        listHelp.addAll(data);
                        pages++;
                    } else {
                        lvAnswerTop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    lvAnswerTop.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvAnswerTop.showFailUI();
            }
        });
    }

    private void initView(View view) {
        lvAnswerTop = (ListViewFinalLoadMore) view.findViewById(R.id.lv_answer_top);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_FOCUS_CHANGED:
                int position = msg.getArg1();
                int isFocus = msg.getArg2();
                if (position > -1 && isFocus > -1) {
                    listHelp.get(position).setIsFocus(isFocus);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
