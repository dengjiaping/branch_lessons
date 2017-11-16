package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.FeedbackListAdapter;
import com.yidiankeyan.science.information.entity.FeedbackListBean;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.PhotoEnlargeActivity;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.RecyclerViewDivider;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.SpaceItemDecoration;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/25.
 */

public class FeedbackListActivity extends BaseActivity {
    private RefreshRecyclerView mRecyclerViewList;
    List<FeedbackListBean> mFeedbackListData = new ArrayList<>();
    private FeedbackListAdapter mFeedbackListAdapter;
    private int mPages = 1;
    private AutoLinearLayout mllReturn;
    private AutoRelativeLayout mlayoutTitle;
    private TextView mMaintitleTxt;
    private TextView mtvRight;
    private ImageView mivMissingPage;

    @Override
    protected int setContentView() {
        return R.layout.activity_feedbacklist;
    }

    @Override
    protected void initView() {
        mRecyclerViewList = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        mllReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mllReturn.setOnClickListener(this);
        mlayoutTitle = (AutoRelativeLayout) findViewById(R.id.layout_title);//
        mMaintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        mMaintitleTxt.setText(getResources().getString(R.string.feedback_his));
        mtvRight = (TextView) findViewById(R.id.tv_right);
        mtvRight.setVisibility(View.GONE);
        mivMissingPage = (ImageView) findViewById(R.id.iv_missing_page);
    }

    @Override
    protected void initAction() {
        initRecyclerView();
        toHttpFeedbackList();
    }

    private void initRecyclerView() {
        mFeedbackListAdapter = new FeedbackListAdapter(this);
        //设置recyclerview的布局样式
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewList.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.VERTICAL));
//        mRecyclerViewList.addItemDecoration(new DividerItemDecoration(
//                this, DividerItemDecoration.VERTICAL));
        mRecyclerViewList.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerViewList.setAdapter(mFeedbackListAdapter);
        mRecyclerViewList.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpFeedbackList();
            }
        });
        mFeedbackListAdapter.setOnItemClickListener(new FeedbackListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String urlId) {
            }
        });
    }

    /**
     * 请求反馈列表接口
     */
    private void toHttpFeedbackList() {

        Map<String, Object> map = new HashMap<>();
        map.put("pages", mPages);
        map.put("pagesize", 10);
        JSONObject jsonObject = new JSONObject();
        jsonObject.toString();
        map.put("entity", jsonObject);
//        map.put("status",);  如果是后台管理员查看整个列表，才需要穿此参数 移动端请勿传此参数
        ApiServerManager.getInstance().getApiServer().getFeedbackList(map).enqueue(new RetrofitCallBack<List<FeedbackListBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FeedbackListBean>>> call, Response<RetrofitResult<List<FeedbackListBean>>> response) {
                if (response.code() == 200) {
                    if (mPages == 1) {
                        mFeedbackListData.removeAll(mFeedbackListData);
                    }
                    if (response.body().getData().size() > 0) {
                        List<FeedbackListBean> listData = response.body().getData();
                        mRecyclerViewList.setVisibility(View.VISIBLE);
                        mFeedbackListData.addAll(listData);
                        mPages++;
                        mFeedbackListAdapter.clear();
                        mFeedbackListAdapter.addAll(mFeedbackListData);
                        mivMissingPage.setVisibility(View.GONE);
                    } else {
                        mFeedbackListAdapter.mNoMoreView.setText("没有更多了");
                        mRecyclerViewList.showNoMore();
                    }
                    if (mPages == 1 && response.body().getData().size() == 0) {
                        mRecyclerViewList.setVisibility(View.GONE);
                        mivMissingPage.setVisibility(View.VISIBLE);
                    }
                    mFeedbackListAdapter.notifyDataSetChanged();
                } else {
                    mFeedbackListAdapter.mNoMoreView.setText("加载失败");
                    mRecyclerViewList.showNoMore();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FeedbackListBean>>> call, Throwable t) {
                mRecyclerViewList.showNoMore();
                mRecyclerViewList.setVisibility(View.GONE);
                mivMissingPage.setVisibility(View.VISIBLE);
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
