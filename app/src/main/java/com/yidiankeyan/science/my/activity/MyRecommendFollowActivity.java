package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.my.adapter.MyFocusRecAdapter;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

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

//我的关注  -推荐关注列表
public class MyRecommendFollowActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecFollow;
    private ArrayList<RecommendFollowBean> magazineLists = new ArrayList<>();
    private int pages = 1;
    private MyFocusRecAdapter focusRecAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_recommend_follow;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
        lvRecFollow = (ListViewFinalLoadMore) findViewById(R.id.lv_rec_follow);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("推荐关注");
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetMagazineList();
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvRecFollow.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetMagazineList();
            }
        });
        focusRecAdapter = new MyFocusRecAdapter(magazineLists, this);
        lvRecFollow.setAdapter(focusRecAdapter);
        llReturn.setOnClickListener(this);
    }


    /**
     * 获取推荐关注列表
     */
    private void toHttpGetMagazineList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getRecommendFollow(map).enqueue(new RetrofitCallBack<List<RecommendFollowBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<RecommendFollowBean>>> call, Response<RetrofitResult<List<RecommendFollowBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        magazineLists.removeAll(magazineLists);
                    if (response.body().getData().size() > 0) {
                        lvRecFollow.setHasLoadMore(true);
                        magazineLists.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvRecFollow.setHasLoadMore(false);
                    }
                    focusRecAdapter.notifyDataSetChanged();
                } else {
                    lvRecFollow.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<RecommendFollowBean>>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
