package com.yidiankeyan.science.my.activity;

import android.content.Intent;
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
 * -我的粉丝
 */
public class MyFansActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvFansFollow;
    private MyFansFollowAdapter adapter;
    private List<FansFollow> mDatas = new ArrayList<>();
    private String userId;
    private Intent intent;
    private int pages = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_fans;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvFansFollow = (ListViewFinalLoadMore) findViewById(R.id.lv_my_fans);
    }

    @Override
    protected void initAction() {
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.equals(userId, SpUtils.getStringSp(this, "userId"))) {
            txtTitle.setText("我的粉丝");
        } else {
            txtTitle.setText("他的粉丝");
        }
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        lvFansFollow.setDivider(new ColorDrawable(Color.parseColor("#E2E2E2")));
        lvFansFollow.setDividerHeight(1);
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                if (TextUtils.equals(userId, SpUtils.getStringSp(MyFansActivity.this, "userId"))) {
                    initDatas();
                } else {
                    initOnDatas();
                }
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvFansFollow.setHasLoadMore(true);
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new MyFansFollowAdapter(this, mDatas);
        lvFansFollow.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvFansFollow.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                if (TextUtils.equals(userId, SpUtils.getStringSp(MyFansActivity.this, "userId"))) {
                    initDatas();
                } else {
                    initOnDatas();
                }
            }
        });
        lvFansFollow.setOnItemClickListener(this);
    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);

        ApiServerManager.getInstance().getApiServer().getMyFans(map).enqueue(new RetrofitCallBack<List<FansFollow>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FansFollow>>> call, Response<RetrofitResult<List<FansFollow>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvFansFollow.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvFansFollow.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvFansFollow.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FansFollow>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvFansFollow.showFailUI();
            }
        });

//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.MY_FANS_LIST, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    mDatas.removeAll(mDatas);
//                    mDatas.addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), FansFollow.class));
//                    adapter.notifyDataSetChanged();
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

        ApiServerManager.getInstance().getApiServer().getHeFans(map).enqueue(new RetrofitCallBack<List<FansFollow>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FansFollow>>> call, Response<RetrofitResult<List<FansFollow>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvFansFollow.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvFansFollow.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvFansFollow.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FansFollow>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvFansFollow.showFailUI();
            }
        });

//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.HI_FANS_LIST, map, new HttpUtil.HttpCallBack() {
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (!TextUtils.equals(mDatas.get(position).getUserid(), SpUtils.getStringSp(this, "userId"))) {
//            intent = new Intent(MyFansActivity.this, HisDataActivity.class);
//            intent.putExtra("id", mDatas.get(position).getUserid());
//            intent.putExtra("name", mDatas.get(position).getUsername());
//            startActivity(intent);
//        } else {
//            intent = new Intent(this, PersonalDataActivity.class);
////                    intent.putExtra("id",mContentList.get(getPlayPosition()).getArticleid());
//            startActivity(intent);
//        }
        intent = new Intent(MyFansActivity.this, MyHomePageActivity.class);
        intent.putExtra("id", mDatas.get(position).getUserid());
        intent.putExtra("name", mDatas.get(position).getUsername());
        startActivity(intent);
    }
}
