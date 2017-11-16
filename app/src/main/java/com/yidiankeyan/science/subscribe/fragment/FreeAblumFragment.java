package com.yidiankeyan.science.subscribe.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.EditorActivity;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.adapter.MySubscribeAdapter;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
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
 * 订阅
 * -免费专辑
 */
public class FreeAblumFragment extends Fragment implements View.OnClickListener {

    private PtrClassicFrameLayout ptrLayout;
    private MySubscribeAdapter adapter;
    //    private RequestQueue requestQueue;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private boolean flag = true;
    private AutoLinearLayout ll_all;
    private ImageView imgBottom;
    private String imgBottomUrl;
    //    private View bottomView;
    private boolean footViewExited;
    private TextView tvEmpty;
    private Button tvFreeJump;
    private AutoRelativeLayout rlFreeNull;
    //    private AutoLinearLayout llRootFooter;
    private ImageView imgRootFooterBottom;
    private IntentFilter intentFilter;
    private int pages = 1;

    public FreeAblumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_free_ablum, container, false);
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        getContext().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        //初始化控件
        initView(view);
        //填充数据
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
        //添加尾部控件
//        lvSub.addFooterView(bottomView);
//        lvSub.removeFooterView(bottomView);
        adapter = new MySubscribeAdapter(getActivity(), mDatas);
        adapter.setLl_all(ll_all);
        lvSub.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        bottomView = LayoutInflater.from(getContext()).inflate(R.layout.sub_buttom, null);
//        imgBottom = (ImageView) bottomView.findViewById(R.id.img_bottom);
        if (imgBottomUrl == null) {
            for (BannerBean banner : new DB(getContext()).queryBanner()) {
                if (102 == banner.getPositionid()) {
                    imgBottomUrl = banner.getImgurl();
                    Glide.with(getContext()).load(imgBottomUrl).into(imgBottom);
                    break;
                }
            }
        } else {
            Glide.with(getContext()).load(imgBottomUrl).into(imgBottom);
        }
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
                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("isfree", 0);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_BOOKED_ALBUM, map, new HttpUtil.HttpCallBack() {
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
                        if (datas!=null&&datas.size()>0) {
                            lvSub.setHasLoadMore(true);
                            pages++;
                        }else{
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
                }else {
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

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvSub = (ListViewFinalLoadMore) view.findViewById(R.id.lv_Sub);
        ll_all = (AutoLinearLayout) getActivity().findViewById(R.id.main);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        tvFreeJump = (Button) view.findViewById(R.id.tv_free_jump);
        rlFreeNull = (AutoRelativeLayout) view.findViewById(R.id.rl_free_null);
//        llRootFooter = (AutoLinearLayout) view.findViewById(R.id.ll_root_footer);
        imgRootFooterBottom = (ImageView) view.findViewById(R.id.img_root_footer_bottom);
        ((TextView) view.findViewById(R.id.maintitle_txt)).setText("订阅");
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
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_TOP, map, new HttpUtil.HttpCallBack() {
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
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_CANCEL, maps, new HttpUtil.HttpCallBack() {
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
        getContext().unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_free_jump:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                startActivity(new Intent(getContext(), EditorActivity.class));
                break;
        }
    }
}
