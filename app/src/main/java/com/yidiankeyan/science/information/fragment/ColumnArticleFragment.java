package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.ColArticleDetailActivity;
import com.yidiankeyan.science.information.acitivity.ColumnDetailActivity;
import com.yidiankeyan.science.information.adapter.ColumnArticleAdapter;
import com.yidiankeyan.science.information.entity.ColumnDetailsBean;
import com.yidiankeyan.science.information.entity.ColumnQueryArticleBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.OnMultiClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 专栏详情
 * -文章
 */
public class ColumnArticleFragment extends Fragment {

    private int pages = 1;
    private RefreshRecyclerView recyclerView;
    private ColumnArticleAdapter articleAdapter;
    private List<ColumnQueryArticleBean> mList = new ArrayList<>();
    private View rootView;
    private TextView mtvUpdate;
    private LinearLayout mllOrder;
    private TextView mtvOrder;
    private ImageView mivOrder;
    private ColumnDetailsBean detailsBean;
    private int orientation = 1;   //默认倒序

    public ColumnArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_column_article, container, false);
            initView(rootView);
            initAction();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initAction() {
        initRecyclerView();
        initDatas();
        articleAdapter.setOnItemClickListener(
                new ColumnArticleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, int isPrice) {
                        DemoApplication.mListImageTextselect.add(mList.get(position).getId());
                        DemoApplication.isBuyImagetext = true;
                        if (TextUtils.equals("1", mList.get(position).getHaveYouPurchased()) ||
                                mList.get(position).getFree().equals("1")) {
                            Intent intent = new Intent(getActivity(), ColArticleDetailActivity.class);
                            intent.putExtra("id", mList.get(position).getId());//detailsBean
                            intent.putExtra("mBuy", mList.get(position).getHaveYouPurchased());//detailsBean
                            startActivity(intent);
                        } else {
                            ToastMaker.showShortToast("请先购买");
                        }
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        articleAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        articleAdapter = new ColumnArticleAdapter(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(articleAdapter);
        articleAdapter.isShowNoMore = false;
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initDatas();
            }
        });
        toHttp();
        if (!StringUtils.isEmpty(getArguments().getString("mUpdateNum"))) {
            mtvUpdate.setText("已更新" + getArguments().getString("mUpdateNum") + "篇");
        }
    }


    //专栏详情
    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getArguments().getString("id"));
        ApiServerManager.getInstance().getApiServer().getQueryColumn(map).enqueue(new RetrofitCallBack<ColumnDetailsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ColumnDetailsBean>> call, Response<RetrofitResult<ColumnDetailsBean>> response) {
                if (response.body().getCode() == 200) {
                    detailsBean = response.body().getData();
                    mtvUpdate.setText("已更新" + detailsBean.getArticletotal() + "篇");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ColumnDetailsBean>> call, Throwable t) {

            }
        });
    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pages);
        map.put("pageSize", 10);
        map.put("columnId", getArguments().getString("id"));
        map.put("release", "1");
        map.put("sort", orientation);

        ApiServerManager.getInstance().getApiServer().getColumnArticle(map).enqueue(new RetrofitCallBack<List<ColumnQueryArticleBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<ColumnQueryArticleBean>>> call, Response<RetrofitResult<List<ColumnQueryArticleBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        mList.removeAll(mList);
                    }
                    if (response.body().getData().size() > 0) {
                        mList.addAll(response.body().getData());
                        pages++;
                        articleAdapter.clear();
                        articleAdapter.addAll(mList);
                    } else {
                        articleAdapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                    articleAdapter.notifyDataSetChanged();
                } else {
                    articleAdapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<ColumnQueryArticleBean>>> call, Throwable t) {
                articleAdapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();
            }
        });
    }


    private void initView(View view) {
        recyclerView = (RefreshRecyclerView) view.findViewById(R.id.recyclerView);
        mtvUpdate = (TextView) view.findViewById(R.id.tv_update);
        mllOrder = (LinearLayout) view.findViewById(R.id.ll_order);
        mtvOrder = (TextView) view.findViewById(R.id.tv_order);
        mivOrder = (ImageView) view.findViewById(R.id.iv_order);
        mllOrder.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != mList && orientation == 1) {
                    orientation = 0;
                    mtvOrder.setText("正序");
                    mivOrder.setImageResource(R.drawable.icon_audio_column_normal_list);
                } else if (null != mList && orientation == 0) {
                    orientation = 1;
                    mtvOrder.setText("倒序");
                    mivOrder.setImageResource(R.drawable.icon_purchase_order);
                }
//                Collections.reverse(mList);
//                articleAdapter.clear();
//                articleAdapter.addAll(mList);
//                articleAdapter.notifyDataSetChanged();
                pages = 1;
                initDatas();
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_COLUMN_FLASH:
                pages = 1;
                initDatas();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
