package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.adapter.MyQuestionAdapter;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的提问
 */
public class MyQuestionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListViewFinalLoadMore lvAnswerTop;
    private MyQuestionAdapter adapter;
    private List<NewScienceHelp> listHelp = new ArrayList<>();
    private int pages = 1;
    private PtrClassicFrameLayout ptrLayout;
    private Intent intent;

    public MyQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authenticate_answer, container, false);
        initView(view);
        lvAnswerTop.setDivider(new ColorDrawable(Color.parseColor("#E2E2E2")));
        lvAnswerTop.setDividerHeight(1);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                    pages = 1;
                    addData();
                } else
                    ptrLayout.onRefreshComplete();
            }
        });
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            if (listHelp.size() == 0)
                ptrLayout.autoRefresh();
            else {
                lvAnswerTop.setHasLoadMore(true);
            }
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
        adapter = new MyQuestionAdapter(listHelp, getContext());
        lvAnswerTop.setAdapter(adapter);
        lvAnswerTop.setOnItemClickListener(this);
        return view;
    }

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            entity.put("makerid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else entity.put("makerid", "");
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getKeDaDetailList(map).enqueue(new RetrofitCallBack<List<NewScienceHelp>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<NewScienceHelp>>> call, Response<RetrofitResult<List<NewScienceHelp>>> response) {
                if (200 == response.body().getCode()) {
                    List<NewScienceHelp> datas = response.body().getData();
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
            public void onFailure(Call<RetrofitResult<List<NewScienceHelp>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvAnswerTop.showFailUI();
            }
        });
//        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_AUTHENTICATE_ANSWER, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (200 == result.getCode()) {
//                    List<AnswerPeopleDetail> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
//                    if (pages == 1)
//                        listHelp.removeAll(listHelp);
//                    if (datas.size() > 0) {
//                        lvAnswerTop.setHasLoadMore(true);
//                        listHelp.addAll(datas);
//                        pages++;
//                        if (datas.size() < 20)
//                            lvAnswerTop.setHasLoadMore(false);
//                    } else {
//                        lvAnswerTop.setHasLoadMore(false);
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    lvAnswerTop.showFailUI();
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrLayout.onRefreshComplete();
//                lvAnswerTop.showFailUI();
//            }
//        });
    }

    private void initView(View view) {
        lvAnswerTop = (ListViewFinalLoadMore) view.findViewById(R.id.lv_authenticate);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        intent = new Intent(getActivity(), AnswerTopDetailActivity.class);
//        intent.putExtra("name", listHelp.get(position).getName() + "的科答");
//        intent.putExtra("id", listHelp.get(position).getId());
//        startActivity(intent);
    }
}