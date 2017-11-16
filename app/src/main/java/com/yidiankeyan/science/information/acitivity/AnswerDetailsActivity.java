package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AnswerAlbumDetailAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumDetailUserBean;
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

/**
 * 问答列表
 */
public class AnswerDetailsActivity extends BaseActivity implements View.OnClickListener {

    private AutoLinearLayout llReturn;
    private ImageButton btnTitle;
    private TextView maintitleTxt;
    private ListViewFinalLoadMore lvEavesdropTop;
    private AnswerAlbumDetailAdapter adapter;
    private List<AnswerAlbumDetailUserBean> mData = new ArrayList<>();
    private int pages = 1;
    private PtrClassicFrameLayout ptrLayout;

    @Override
    protected int setContentView() {
        return R.layout.activity_answer_details;
    }

    @Override
    protected void initView() {
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        lvEavesdropTop = (ListViewFinalLoadMore) findViewById(R.id.lv_answer_detail);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
    }

    @Override
    protected void initAction() {
        String name = getIntent().getStringExtra("name");
        btnTitle.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        maintitleTxt.setText(name);
        adapter = new AnswerAlbumDetailAdapter(this, mData);
        lvEavesdropTop.setAdapter(adapter);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttp();
            }
        });
        if (mData.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvEavesdropTop.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvEavesdropTop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttp();
            }
        });
        lvEavesdropTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AnswerTopDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", mData.get(position).getId());
                startActivity(intent);
            }
        });
    }


    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getIntExtra("id", 0));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getKedaAnswerAlbumUserList(map).enqueue(new RetrofitCallBack<List<AnswerAlbumDetailUserBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AnswerAlbumDetailUserBean>>> call, Response<RetrofitResult<List<AnswerAlbumDetailUserBean>>> response) {
                if (response.body().getCode() == 200) {
                    List<AnswerAlbumDetailUserBean> data = response.body().getData();
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (data.size() > 0) {
                        lvEavesdropTop.setHasLoadMore(true);
                        mData.addAll(data);
                        pages++;
                    } else {
                        lvEavesdropTop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvEavesdropTop.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AnswerAlbumDetailUserBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvEavesdropTop.showFailUI();
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
