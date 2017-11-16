package com.yidiankeyan.science.my.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.adapter.MyFansFollowAdapter;
import com.yidiankeyan.science.my.entity.FansFollow;
import com.yidiankeyan.science.utils.SpUtils;
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
 * 主页
 * -关注
 */
public class MyFollowActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvMyFollow;
    private MyFansFollowAdapter adapter;
    private List<FansFollow> mDatas = new ArrayList<>();
    private String userId;
    private int pages = 1;
    private IntentFilter intentFilter;
    private Intent intent;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_follow;
    }

    @Override
    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.home.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvMyFollow = (ListViewFinalLoadMore) findViewById(R.id.lv_my_follow);
    }

    @Override
    protected void initAction() {
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.equals(userId, SpUtils.getStringSp(this, "userId"))) {
            txtTitle.setText("我的关注");
        } else {
            txtTitle.setText("他的关注");
        }
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        lvMyFollow.setDivider(new ColorDrawable(Color.parseColor("#E2E2E2")));
        lvMyFollow.setDividerHeight(1);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                if (TextUtils.equals(userId, SpUtils.getStringSp(MyFollowActivity.this, "userId"))) {
                    initDatas();
                } else {
                    initOnDatas();
                }
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvMyFollow.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new MyFansFollowAdapter(this, mDatas);
        lvMyFollow.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMyFollow.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                if (TextUtils.equals(userId, SpUtils.getStringSp(MyFollowActivity.this, "userId"))) {
                    initDatas();
                } else {
                    initOnDatas();
                }
            }
        });
        lvMyFollow.setOnItemClickListener(this);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.home.refreshFriend")) {
                ptrLayout.autoRefresh();
            }
        }
    };

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);


        ApiServerManager.getInstance().getApiServer().getMyFollow(map).enqueue(new RetrofitCallBack<List<FansFollow>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FansFollow>>> call, Response<RetrofitResult<List<FansFollow>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvMyFollow.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvMyFollow.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvMyFollow.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FansFollow>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvMyFollow.showFailUI();
            }
        });

//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.MY_FOCUS_LIST, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    mDatas.removeAll(mDatas);
//                    mDatas.addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), FansFollow.class));
//                    adapter.notifyDataSetChanged();
//                    Intent intent = new Intent();
//                    intent.putExtra("size", mDatas.size());
//                    setResult(RESULT_OK, intent);
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrLayout.onRefreshComplete();
//            }
//        });
    }

    private void initOnDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", userId);
        map.put("entity", entity);

        ApiServerManager.getInstance().getApiServer().getHeFollow(map).enqueue(new RetrofitCallBack<List<FansFollow>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FansFollow>>> call, Response<RetrofitResult<List<FansFollow>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvMyFollow.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvMyFollow.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvMyFollow.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FansFollow>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvMyFollow.showFailUI();
            }
        });

//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.HI_FOCUS_LIST, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    mDatas.removeAll(mDatas);
//                    mDatas.addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), FansFollow.class));
//                    adapter.notifyDataSetChanged();
//                    Intent intent = new Intent();
//                    intent.putExtra("size", mDatas.size());
//                    setResult(RESULT_OK, intent);
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrLayout.onRefreshComplete();
//            }
//        });
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
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (!TextUtils.equals(mDatas.get(position).getUserid(), SpUtils.getStringSp(this, "userId"))) {
//            intent = new Intent(MyFollowActivity.this, HisDataActivity.class);
//            intent.putExtra("id", mDatas.get(position).getUserid());
//            startActivity(intent);
//        } else {
//            intent = new Intent(this, PersonalDataActivity.class);
////                    intent.putExtra("id",mContentList.get(getPlayPosition()).getArticleid());
//            startActivity(intent);
//        }
        intent = new Intent(MyFollowActivity.this, MyHomePageActivity.class);
        intent.putExtra("id", mDatas.get(position).getUserid());
        intent.putExtra("name", mDatas.get(position).getUsername());
        startActivity(intent);
    }
}
