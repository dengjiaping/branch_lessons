package com.yidiankeyan.science.collection.fragment;


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
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.collection.entity.CollectionBean;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.ColDownloadAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.my.activity.DownloadFileContentActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 点藏
 * -本地
 */
public class ColChargeAlbumFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PtrClassicFrameLayout ptrCharge;
    private ColDownloadAdapter adapter;
    private ListView lvSub;
    //页面数据
    private List<CollectionBean> mDatas = new ArrayList<>();
    //轮播图数据
    private List<BannerBean> mBannerList = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pageNum = 0;
    private boolean flag = true;
    private AutoLinearLayout ll_all;
    private Intent intent;
    private AutoRelativeLayout rlDelete;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private boolean isDeleteState;
    private IntentFilter intentFilter;

    public ColChargeAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_col_charge_album, container, false);
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.download.refreshFriend");
        getContext().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        //初始化控件
        initView(view);
        ptrCharge.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                addData();
            }
        });
        ptrCharge.disableWhenHorizontalMove(true);
        ptrCharge.setLastUpdateTimeRelateObject(this);

        lvSub.setOnItemClickListener(this);
        if (mDatas.size() == 0) {
            ptrCharge.autoRefresh();
        }

        adapter.setOnDeleteChangeListener(new ColDownloadAdapter.OnDeleteChangeListener() {
            @Override
            public void onDeleteChanged() {
                //先定义全选和删除都为不可用，遍历后有一条没选中择把allCheck置为1，有一条为选中择把delete置为1
                int allCheck = 0, delete = 0;
                for (CollectionBean collectArticle : mDatas) {
                    if (collectArticle.isNeedDelete()) {
                        //已被选中
                        delete = 1;
                    } else {
                        //未被选中
                        allCheck = 1;
                    }
                    if (delete == 1 && allCheck == 1) {
                        break;
                    }
                }
                if (allCheck == 1) {
                    tvCheckAll.setTag(1);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvCheckAll.setTag(0);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.black_33));
                }
                if (delete == 1) {
                    tvDelete.setTag(1);
                    tvDelete.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvDelete.setTag(0);
                    tvDelete.setTextColor(getResources().getColor(R.color.menu));
                }
            }
        });
        tvCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(1)) {
                    for (CollectionBean collectArticle : mDatas) {
                        collectArticle.setNeedDelete(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(1)) {
                    List<CollectionBean> articleList = new ArrayList<>();
                    for (CollectionBean collectArticle : mDatas) {
                        if (collectArticle.isNeedDelete()) {
                            articleList.add(collectArticle);
                        }
                    }
                    List<AlbumContent> list = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
                    for (AlbumContent content : list) {
                        for (CollectionBean collectionBean : articleList) {
                            if (content.getAlbumId().equals(collectionBean.getId())) {
                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(content.getArticleid());
                                try {
                                    new File(content.getFilePath()).delete();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    mDatas.removeAll(articleList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.download.refreshFriend")) {
                ptrCharge.autoRefresh();
            }
        }
    };

    private void addData() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("pricetype", 1);
//        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_COLLECTION_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (200 == result.getCode()) {
//                    mDatas.removeAll(mDatas);
//                    List<CollectionBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), CollectionBean.class);
//                    mDatas.addAll(datas);
//                    adapter.notifyDataSetChanged();
//                }
//                ptrCharge.onRefreshComplete();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrCharge.onRefreshComplete();
//            }
//
//        });
        mDatas.removeAll(mDatas);
        addLocalFile();
        adapter.notifyDataSetChanged();
        ptrCharge.onRefreshComplete();
    }

    private void addLocalFile() {
        List<AlbumContent> list = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
        List<String> albumIds = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (!albumIds.contains(list.get(i).getAlbumId())) {
                CollectionBean bean = new CollectionBean();
                bean.setShortimgurl(list.get(i).getAlbumAvatar());
                bean.setCoverimgurl(list.get(i).getAlbumAvatar());
                bean.setLastupdatetitle(list.get(i).getArticlename());
                bean.setName(list.get(i).getAlbumName());
                bean.setId(list.get(i).getAlbumId());
                bean.setFile(true);
                bean.setContentnum(1);
                bean.setPraisenum(1);
                albumIds.add(list.get(i).getAlbumId());
                mDatas.add(bean);
            } else {
                for (int j = 0; j < mDatas.size(); j++) {
                    if (mDatas.get(j).getId().equals(list.get(i).getAlbumId())) {
                        mDatas.get(j).setContentnum(mDatas.get(j).getContentnum() + 1);
                        mDatas.get(j).setPraisenum(mDatas.get(j).getPraisenum() + 1);
                        break;
                    }
                }
            }
        }
    }


    private void initView(View view) {
        lvSub = (ListView) view.findViewById(R.id.lv_charge);
        ptrCharge = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_col_charge);
        ll_all = (AutoLinearLayout) getActivity().findViewById(R.id.main);
        adapter = new ColDownloadAdapter(getActivity(), mDatas);
        adapter.setLl_all(ll_all);
        rlDelete = (AutoRelativeLayout) view.findViewById(R.id.rl_delete);
        tvCheckAll = (TextView) view.findViewById(R.id.tv_check_all);
        tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        lvSub.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        getContext().unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDatas == null || mDatas.size() == 0)
            return;
        intent = new Intent(getContext(), DownloadFileContentActivity.class);
        intent.putExtra("albumId", mDatas.get(position).getId());
        intent.putExtra("albumName", mDatas.get(position).getName());
        startActivity(intent);
    }

    public void onDeleteClick() {
        isDeleteState = true;
        adapter.setDeleteState(true);
        adapter.notifyDataSetChanged();
        rlDelete.setVisibility(View.VISIBLE);
        if (mDatas != null && mDatas.size() > 0) {
            tvCheckAll.setTextColor(getResources().getColor(R.color.defaultcolortwo));
            tvCheckAll.setTag(1);
            tvDelete.setTag(0);
            for (CollectionBean collectArticle : mDatas) {
                collectArticle.setNeedDelete(false);
            }
        } else {
            tvCheckAll.setTag(0);
            tvDelete.setTag(0);
        }
        //隐藏更多，显示取消
        EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_MAIN_CANCEL_SHOW_OR_HIDE);
        msg.setArg1(1);
        EventBus.getDefault().post(msg);
    }

    public void onCancelClick() {
        isDeleteState = false;
        adapter.setDeleteState(false);
        adapter.notifyDataSetChanged();
        rlDelete.setVisibility(View.GONE);
        //隐藏取消，显示更多
        EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_MAIN_CANCEL_SHOW_OR_HIDE);
        msg.setArg1(0);
        EventBus.getDefault().post(msg);
    }

    public boolean isDeleteState() {
        return isDeleteState;
    }

    public AutoLinearLayout getLl_all() {
        return ll_all;
    }

    public void setLl_all(AutoLinearLayout ll_all) {
        this.ll_all = ll_all;
    }

}
