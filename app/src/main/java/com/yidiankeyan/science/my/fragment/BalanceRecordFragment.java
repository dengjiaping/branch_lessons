package com.yidiankeyan.science.my.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.adapter.AccountDetailedAdapter;
import com.yidiankeyan.science.my.entity.AccountDetailedBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 余额收益
 */
public class BalanceRecordFragment extends Fragment {

    private ListViewFinalLoadMore lvBalanceRecord;
    private List<AccountDetailedBean> listData = new ArrayList<>();
    private AccountDetailedAdapter adapter;
    private int pages = 1;

    public BalanceRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_record, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        toHttpPostDetailed();
        if (listData.size() == 0) {

        } else {
            lvBalanceRecord.setHasLoadMore(true);
        }
        adapter = new AccountDetailedAdapter(getContext(), listData);
        lvBalanceRecord.setAdapter(adapter);

        lvBalanceRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpPostDetailed();
            }
        });
    }

    private void initView(View view) {
        lvBalanceRecord = (ListViewFinalLoadMore) view.findViewById(R.id.lv_balance_record);
    }

    /**
     * 明细列表
     */
    private void toHttpPostDetailed() {

        ApiServerManager.getInstance().getApiServer().getAccountDetailed(pages, 10).enqueue(new RetrofitCallBack<List<AccountDetailedBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AccountDetailedBean>>> call, Response<RetrofitResult<List<AccountDetailedBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        listData.remove(listData);
                    if (response.body().getData().size() > 0) {
                        lvBalanceRecord.setHasLoadMore(true);
                        List<AccountDetailedBean> data = response.body().getData();
                        listData.addAll(data.size() > 10 ? data.subList(0, 10) : data);
                        pages++;
                    } else {
                        lvBalanceRecord.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvBalanceRecord.showFailUI();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AccountDetailedBean>>> call, Throwable t) {
                lvBalanceRecord.showFailUI();
            }
        });
    }

}
