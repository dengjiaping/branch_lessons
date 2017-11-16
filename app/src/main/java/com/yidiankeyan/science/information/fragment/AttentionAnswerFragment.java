package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.AnswerTopDetailActivity;
import com.yidiankeyan.science.information.adapter.AnswerTopAdapter;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

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
 * 关注答人
 */
public class AttentionAnswerFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListViewFinalLoadMore lvAnswerTop;
    private AnswerTopAdapter adapter;
    private List<AnswerPeopleDetail> listHelp = new ArrayList<>();
    private Intent intent;
    private int pages = 1;
    private PtrClassicFrameLayout ptrLayout;

    public AttentionAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention_answer, container, false);
        initView(view);
        lvAnswerTop.setDivider(new ColorDrawable(Color.parseColor("#E2E2E2")));
        lvAnswerTop.setDividerHeight(1);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                addData();
            }
        });
        if (listHelp.size() == 0)
            ptrLayout.autoRefresh();
        else {
            lvAnswerTop.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvAnswerTop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                addData();
            }
        });
        adapter = new AnswerTopAdapter(getContext(), listHelp);
        lvAnswerTop.setAdapter(adapter);
        lvAnswerTop.setOnItemClickListener(this);
        return view;
    }


    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_ATTENTION_ANSWER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    List<AnswerPeopleDetail> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    if (pages == 1)
                        listHelp.removeAll(listHelp);
                    if (datas.size() > 0) {
                        lvAnswerTop.setHasLoadMore(true);
                        listHelp.addAll(datas);
                        pages++;
                        if (datas.size() < 20)
                            lvAnswerTop.setHasLoadMore(false);
                    } else {
                        lvAnswerTop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
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
        lvAnswerTop = (ListViewFinalLoadMore) view.findViewById(R.id.lv_answer);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent=new Intent(getActivity(), AnswerTopDetailActivity.class);
        intent.putExtra("name",listHelp.get(position).getName()+"的科答");
        intent.putExtra("id",listHelp.get(position).getId());
        startActivity(intent);
    }
}
