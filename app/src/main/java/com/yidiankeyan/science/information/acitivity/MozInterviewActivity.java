package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.InterviewMyAdapter;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 墨子专访
 */
public class MozInterviewActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView mTvTitle;
    private AutoLinearLayout mLlReturn;
    private InterviewMyAdapter adapter;
    private PtrClassicFrameLayout mPtrLayout;
    private ListViewFinalLoadMore mLvInterview;
    private ArrayList<InterviewSoonBean> mDatas = new ArrayList<>();
    private int mPageNum = 1;


    @Override
    protected int setContentView() {
        return R.layout.activity_moz_interview;
    }

    @Override
    protected void initView() {
        mTvTitle = (TextView) findViewById(R.id.maintitle_txt);
        mLlReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mPtrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        mLvInterview = (ListViewFinalLoadMore) findViewById(R.id.lv_interview_all);
    }

    @Override
    protected void initAction() {
        //填充数据
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageNum = 1;
                toHttpInterviewSoon();
            }
        });
        if (mDatas.size() == 0) {
            mPtrLayout.autoRefresh();
        } else {
            mLvInterview.setHasLoadMore(true);
        }
        mPtrLayout.disableWhenHorizontalMove(true);
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new InterviewMyAdapter(this, mDatas);
        mLvInterview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mLvInterview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (mPtrLayout.isRefreshing())
                    return;
                toHttpInterviewSoon();
            }
        });
        mLlReturn.setOnClickListener(this);
        mLvInterview.setOnItemClickListener(this);
    }


    //专访
    private void toHttpInterviewSoon() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", mPageNum);
        map.put("pageSize", 50);
        map.put("onlineStatus", "1");
        map.put("top", "1");

        ApiServerManager.getInstance().getApiServer().getInterViewSoonList(map).enqueue(new RetrofitCallBack<ArrayList<InterviewSoonBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<InterviewSoonBean>>> call, Response<RetrofitResult<ArrayList<InterviewSoonBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (mPageNum == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        mLvInterview.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        mPageNum++;
                    } else {
                        mLvInterview.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    mLvInterview.showFailUI();
                }
                mPtrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<InterviewSoonBean>>> call, Throwable t) {
                mPtrLayout.onRefreshComplete();
                mLvInterview.showFailUI();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, MozInterviewDetailsActivity.class);
        intent.putParcelableArrayListExtra("list", mDatas);
        intent.putExtra("id", mDatas.get(position).getId());
        intent.putExtra("position", position);
        mContext.startActivity(intent);
    }
}
