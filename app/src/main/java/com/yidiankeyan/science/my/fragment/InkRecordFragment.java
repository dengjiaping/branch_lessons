package com.yidiankeyan.science.my.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.adapter.InkRecordDetailedAdapter;
import com.yidiankeyan.science.my.entity.InKRecordBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 墨水明细
 */
public class InkRecordFragment extends Fragment {

    private ListViewFinalLoadMore lvInkRecord;
    private List<InKRecordBean> listData = new ArrayList<>();
    private InkRecordDetailedAdapter adapter;
    private int pages = 1;

    public InkRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ink_record, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        toHttpPostDetailed();
        if (listData.size() == 0) {

        } else {
            lvInkRecord.setHasLoadMore(true);
        }
        adapter = new InkRecordDetailedAdapter(getContext(), listData);
        lvInkRecord.setAdapter(adapter);

        lvInkRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpPostDetailed();
            }
        });
    }

    private void initView(View view) {
        lvInkRecord = (ListViewFinalLoadMore) view.findViewById(R.id.lv_ink_record);
    }

    /**
     * 明细列表
     */
    private void toHttpPostDetailed() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pages);
        map.put("pageSize", 10);
        ApiServerManager.getInstance().getApiServer().getInkDetailed(map).enqueue(new RetrofitCallBack<List<InKRecordBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<InKRecordBean>>> call, Response<RetrofitResult<List<InKRecordBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        listData.remove(listData);
                    if (response.body().getData().size() > 0) {
                        lvInkRecord.setHasLoadMore(true);
                        List<InKRecordBean> data = response.body().getData();
                        listData.addAll(data.size() > 10 ? data.subList(0, 10) : data);
                        pages++;
                    } else {
                        lvInkRecord.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvInkRecord.showFailUI();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<InKRecordBean>>> call, Throwable t) {
                lvInkRecord.showFailUI();
            }
        });
    }
}
