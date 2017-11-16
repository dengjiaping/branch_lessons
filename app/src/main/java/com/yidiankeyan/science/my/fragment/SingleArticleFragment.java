package com.yidiankeyan.science.my.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.adapter.SingleArticleAdapter;
import com.yidiankeyan.science.my.entity.MyAccountArticleBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoRelativeLayout;

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
 * 主页
 * -全部文章
 */
public class SingleArticleFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvArticle;
    private ArrayList<MyAccountArticleBean> mDatas = new ArrayList<>();
    private SingleArticleAdapter articleAdapter;
    private int pages = 1;
    private AutoRelativeLayout rlAriclePrompt;
    private TextView tvEditorContent, tvOfficial, tvWebUrl;
    private ArrayList<MyAccountArticleBean> list;


    public SingleArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_article, container, false);
        //初始化控件
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        ptrLayout.setVisibility(View.VISIBLE);
        rlAriclePrompt.setVisibility(View.GONE);
        lvArticle.setDivider(new ColorDrawable(Color.parseColor("#eeeeee")));
        lvArticle.setDividerHeight(1);
        //        填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetArticle();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvArticle.setHasLoadMore(true);
        }

        //给listview 设置数据
        articleAdapter = new SingleArticleAdapter(getContext(), mDatas);
        lvArticle.setAdapter(articleAdapter);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);

        lvArticle.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetArticle();
            }
        });


        if (!TextUtils.equals(getArguments().getString("id"), SpUtils.getStringSp(getActivity(), "userId"))) {
            tvEditorContent.setText("真可惜，ta还没有上传单篇文章");
            tvOfficial.setVisibility(View.GONE);
            tvWebUrl.setVisibility(View.GONE);
        } else {
            tvEditorContent.setText("你还没有上传单篇文章");
            tvOfficial.setVisibility(View.VISIBLE);
            tvWebUrl.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_article_layout);
        lvArticle = (ListViewFinalLoadMore) view.findViewById(R.id.lv_article);
        rlAriclePrompt = (AutoRelativeLayout) view.findViewById(R.id.rl_aricle_prompt);
        tvEditorContent = (TextView) view.findViewById(R.id.tv_editor_content);
        tvOfficial = (TextView) view.findViewById(R.id.tv_official);
        tvWebUrl = (TextView) view.findViewById(R.id.tv_web_url);
    }


    /**
     * 获取文章列表
     */
    private void toHttpGetArticle() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", pages);
//        map.put("pagesize", 20);
//        Map<String, Object> entity = new HashMap<>();
//        entity.put("userid", getArguments().getString("id"));
//        entity.put("checkstatus", "-1");
//        map.put("entity", entity);
//        ApiServerManager.getInstance().getApiServer().getSinglesArticle(map).enqueue(new RetrofitCallBack<ArrayList<SinglesArticleBean>>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<ArrayList<SinglesArticleBean>>> call, Response<RetrofitResult<ArrayList<SinglesArticleBean>>> response) {
//                if (response.body().getCode() == 200) {
//
//                    if (pages == 1)
//                        mDatas.removeAll(mDatas);
//                    if (response.body().getData().size() > 0) {
//                        lvArticle.setHasLoadMore(true);
//                        list = new ArrayList<>();
//                        list.addAll(response.body().getData());
//                        SinglesArticleListBean listBean = new SinglesArticleListBean(list);
//                        mDatas.add(listBean);
//                        pages++;
//                    } else {
//                        lvArticle.setHasLoadMore(false);
//                    }
//                    if (list == null || list.size() == 0) {
//                        rlAriclePrompt.setVisibility(View.VISIBLE);
//                        ptrLayout.setVisibility(View.GONE);
//                    } else if (list.size() > 0) {
//                        ptrLayout.setVisibility(View.VISIBLE);
//                        rlAriclePrompt.setVisibility(View.GONE);
//                    }
//                    articleAdapter.notifyDataSetChanged();
//                }else{
//                    lvArticle.showFailUI();
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<ArrayList<SinglesArticleBean>>> call, Throwable t) {
//                ptrLayout.onRefreshComplete();
//                lvArticle.showFailUI();
//            }
//        });
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.equals(getArguments().getString("id"), SpUtils.getStringSp(getActivity(), "userId"))) {
            map.put("userId", getArguments().getString("id"));
        }else{
            map.put("userId", "");
        }

        ApiServerManager.getInstance().getApiServer().getAccountArticle(pages, 20, map).enqueue(new RetrofitCallBack<List<MyAccountArticleBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<MyAccountArticleBean>>> call, Response<RetrofitResult<List<MyAccountArticleBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvArticle.setHasLoadMore(true);
                        list = new ArrayList<>();
                        list.addAll(response.body().getData());
                        mDatas.addAll(list);
                        pages++;
                    } else {
                        lvArticle.setHasLoadMore(false);
                    }
                    if (list == null || list.size() == 0) {
                        rlAriclePrompt.setVisibility(View.VISIBLE);
                        ptrLayout.setVisibility(View.GONE);
                    } else if (list.size() > 0) {
                        ptrLayout.setVisibility(View.VISIBLE);
                        rlAriclePrompt.setVisibility(View.GONE);
                    }
                    articleAdapter.notifyDataSetChanged();
                } else {
                    lvArticle.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<MyAccountArticleBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvArticle.showFailUI();
            }
        });


    }
}
