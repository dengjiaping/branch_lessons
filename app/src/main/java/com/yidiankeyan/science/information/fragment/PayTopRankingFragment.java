package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.adapter.PayTopAdapter;
import com.yidiankeyan.science.information.entity.PayTopBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 付费榜
 */
public class PayTopRankingFragment extends Fragment {


    private ListView lvPayTop;
    private PtrClassicFrameLayout ptrLayout;
    private PayTopAdapter adapter;
    private  int isPrice;
    private List<PayTopBean> mData = new ArrayList<>();

    public PayTopRankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_top_ranking, container, false);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvPayTop = (ListView) view.findViewById(R.id.lv_pay_top);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                toHttpGetPayTop();
            }
        });
        if (mData.size() == 0)
            ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new PayTopAdapter(mData, getContext());
//        lvPayTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent;
//                if (mData.get(position).getEntitytype() == 1) {
//                    intent = new Intent(getContext(), ColumnDetailsActivity.class);
//                    intent.putExtra("id", mData.get(position).getId());
//                    getContext().startActivity(intent);
//                } else {
//                    intent = new Intent(getContext(), MozReadDetailsActivity.class);
//                    intent.putExtra("id", mData.get(position).getId());
//                    intent.putExtra("isPrice", mData.get(position).getPrice());
//                    getContext().startActivity(intent);
//                }
//            }
//        });
        lvPayTop.setAdapter(adapter);
        return view;
    }

    private void toHttpGetPayTop() {
        Map<String, Object> map = new HashMap<>();
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_PAY_TOP, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<PayTopBean> data = GsonUtils.json2List(jsonData, PayTopBean.class);
                    mData.removeAll(mData);
                    mData.addAll(data);
                    adapter.notifyDataSetChanged();
                    ptrLayout.onRefreshComplete();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
            }
        });
    }

}
