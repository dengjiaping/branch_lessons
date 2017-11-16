package com.yidiankeyan.science.subscribe.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.adapter.MySubscribeAdapter;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;


/**
 * 订阅
 * -收费专辑
 */
public class ChargeAblumFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private MySubscribeAdapter adapter;
    //    private RequestQueue requestQueue;
    private ListViewFinal lvSub;
    //页面数据
    private List<BusinessAllBean> mDatas = new ArrayList<>();
    private boolean flag = true;
    private AutoLinearLayout ll_all;
    private String imgBottomUrl;
    private View bottomView;
    private boolean footViewExited;
    private TextView tvEmpty;
    private AutoLinearLayout llRootFooter;
    private ImageView imgRootFooterBottom;

    public ChargeAblumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_charge_ablum, container, false);
        //初始化控件
        initView(view);

        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                addData();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        //添加尾部控件
        adapter = new MySubscribeAdapter(getActivity(), mDatas);
        adapter.setLl_all(ll_all);
        lvSub.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvSub.addFooterView(bottomView);
        lvSub.removeFooterView(bottomView);
//        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
//                intent.putExtra("isCharge", true);
//                startActivity(intent);
//            }
//        });

        adapter.setOnItemClickListener(new MySubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                startActivity(intent);
            }
        });
        adapter.setOnAbolishOrderListener(new MySubscribeAdapter.OnAbolishOrderListener() {
            @Override
            public void OnAbolishOrder() {
                if (mDatas.size() > 0 && !footViewExited) {
                    footViewExited = true;
                    lvSub.addFooterView(bottomView);
                    llRootFooter.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                } else if (mDatas.size() == 0 && footViewExited) {
                    footViewExited = false;
                    lvSub.removeFooterView(bottomView);
                    llRootFooter.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("isfree", 1);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_BOOKED_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    mDatas.removeAll(mDatas);
                    List<BusinessAllBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                    mDatas.addAll(datas);
                    adapter.notifyDataSetChanged();

//                    lvSub.addFooterView(bottomView);
                    if (mDatas.size() > 0) {
                        //数据源个数大于0
                        footViewExited = true;
                        tvEmpty.setVisibility(View.GONE);
                        if (!footViewExited)
                            //底部局不存在
                            lvSub.addFooterView(bottomView);
                        llRootFooter.setVisibility(View.GONE);
                    } else if (mDatas.size() == 0) {
                        footViewExited = false;
                        tvEmpty.setVisibility(View.VISIBLE);
                        if (footViewExited)
                            lvSub.removeFooterView(bottomView);
                        llRootFooter.setVisibility(View.VISIBLE);
                    }
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
            }
        });

    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvSub = (ListViewFinal) view.findViewById(R.id.lv_charge_ablum);
        ll_all = (AutoLinearLayout) getActivity().findViewById(R.id.main);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        llRootFooter = (AutoLinearLayout) view.findViewById(R.id.ll_root_footer);
        imgRootFooterBottom = (ImageView) view.findViewById(R.id.img_root_footer_bottom);
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
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_TOP, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
                ptrLayout.autoRefresh();
                adapter.notifyDataSetChanged();
                break;

            case SystemConstant.ON_CLICK_BOTTOM:
                Map<String, Object> maps = new HashMap<>();
                maps.put("orderid", msg.getBody());
                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.POST_CLICK_CANCEL, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (200 == result.getCode()) {
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                    }
                });
                ptrLayout.autoRefresh();
                adapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        EventBus.getDefault().unregister(this);
    }


    public AutoLinearLayout getLl_all() {
        return ll_all;
    }

    public void setLl_all(AutoLinearLayout ll_all) {
        this.ll_all = ll_all;
    }
}
