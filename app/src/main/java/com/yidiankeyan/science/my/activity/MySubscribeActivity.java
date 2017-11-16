package com.yidiankeyan.science.my.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.EditorActivity;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.adapter.MySubscribeAdapter;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 我的已购
 */
public class MySubscribeActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrLayout;
    private MySubscribeAdapter adapter;
    //    private RequestQueue requestQueue;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private boolean flag = true;
    private View ll_all;
    private boolean footViewExited;
    private TextView tvEmpty;
    private Button tvFreeJump;
    private AutoRelativeLayout rlFreeNull;
    //    private AutoLinearLayout llRootFooter;
    private ImageView imgRootFooterBottom;
    private IntentFilter intentFilter;
    private int pages = 1;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_my_subscribe;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvSub = (ListViewFinalLoadMore) findViewById(R.id.lv_Sub);
        ll_all = findViewById(R.id.rl_main);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvFreeJump = (Button) findViewById(R.id.tv_free_jump);
        rlFreeNull = (AutoRelativeLayout) findViewById(R.id.rl_free_null);
//        llRootFooter = (AutoLinearLayout) findViewById(R.id.ll_root_footer);
        imgRootFooterBottom = (ImageView) findViewById(R.id.img_root_footer_bottom);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("订阅");
    }

    @Override
    protected void initAction() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                addData();
            }
        });
        lvSub.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                addData();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        tvFreeJump.setOnClickListener(this);
        findViewById(R.id.ll_return).setOnClickListener(this);
        //添加尾部控件
//        lvSub.addFooterView(bottomView);
//        lvSub.removeFooterView(bottomView);
        adapter = new MySubscribeAdapter(this, mDatas);
        adapter.setLl_all(ll_all);
        lvSub.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
//                startActivity(intent);
//            }
//        });
//        adapter.setOnItemClickListener(new MySubscribeAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(View view, int position) {
//                LogUtils.e("start");
//                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
//                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
//                intent.putExtra("albumName", mDatas.get(position).getName());
//                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
//                mDatas.get(position).setUpdates(0);
//                startActivity(intent);
//                LogUtils.e("end");
//            }
//        });
        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("start");
                Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                mDatas.get(position).setUpdates(0);
                startActivity(intent);
                LogUtils.e("end");
            }
        });
        adapter.setOnAbolishOrderListener(new MySubscribeAdapter.OnAbolishOrderListener() {
            @Override
            public void OnAbolishOrder() {
                if (mDatas.size() > 0 && !footViewExited) {
                    footViewExited = true;
//                    lvSub.addFooterView(bottomView);
//                    llRootFooter.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                    rlFreeNull.setVisibility(View.GONE);
                    tvFreeJump.setVisibility(View.GONE);
                } else if (mDatas.size() == 0 && footViewExited) {
                    footViewExited = false;
//                    lvSub.removeFooterView(bottomView);
//                    llRootFooter.setVisibility(View.VISIBLE);
                    rlFreeNull.setVisibility(View.VISIBLE);
                    tvFreeJump.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
                ptrLayout.autoRefresh();
            }
        }
    };

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("isfree", 0);
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALL_BOOKED_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    List<BusinessAllBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                    mDatas.addAll(datas);
//                    lvSub.addFooterView(bottomView);
                    if (mDatas.size() > 0) {
                        //数据源个数大于0
                        tvEmpty.setVisibility(View.GONE);
                        rlFreeNull.setVisibility(View.GONE);
                        tvFreeJump.setVisibility(View.GONE);
                        if (!footViewExited) {
                            //底部局不存在
//                            lvSub.addFooterView(bottomView);
                            footViewExited = true;
                        }
                        if (datas != null && datas.size() > 0) {
                            lvSub.setHasLoadMore(true);
                            pages++;
                        } else {
                            lvSub.setHasLoadMore(false);
                        }
//                        llRootFooter.setVisibility(View.GONE);
                    } else if (mDatas.size() == 0) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rlFreeNull.setVisibility(View.VISIBLE);
                        tvFreeJump.setVisibility(View.VISIBLE);
                        if (footViewExited) {
//                            lvSub.removeFooterView(bottomView);
                            footViewExited = false;
                        }

//                        llRootFooter.setVisibility(View.VISIBLE);
                    }
                } else {
                    lvSub.showFailUI();
                }
                adapter.notifyDataSetChanged();
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvSub.showFailUI();
            }
        });
    }

    /**
     * 置顶、置底
     *
     * @param msg
     */
    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_TOP:
//                String orderId = (String);
                Map<String, Object> map = new HashMap<>();
                map.put("orderid", msg.getBody());
                HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.POST_CLICK_TOP, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                            ptrLayout.autoRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
                break;

            case SystemConstant.ON_CLICK_BOTTOM:
                Map<String, Object> maps = new HashMap<>();
                maps.put("orderid", msg.getBody());
                HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.POST_CLICK_CANCEL, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                            ptrLayout.autoRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });

                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_free_jump:
                if (!Util.hintLogin(this)) return;
                startActivity(new Intent(this, EditorActivity.class));
                break;
        }
    }
}
