package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.collection.entity.CollectionBean;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.ColDownloadAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.activity.DownloadFileContentActivity;
import com.yidiankeyan.science.utils.LogUtils;
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
 * 订阅的下载内容
 */
public class SubscribeForDownloadFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PtrClassicFrameLayout ptrCharge;
    private ColDownloadAdapter adapter;
    private ListView lvSub;
    //页面数据
    private List<CollectionBean> mDatas = new ArrayList<>();
    private AutoLinearLayout ll_all;
    private Intent intent;
    private AutoRelativeLayout rlDelete;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private boolean isDeleteState;
    private IntentFilter intentFilter;
    private View headView;
    private TextView tvDownloadCount;

    public SubscribeForDownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_col_charge_album, container, false);
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
                    final List<CollectionBean> articleList = new ArrayList<>();
                    for (CollectionBean collectArticle : mDatas) {
                        if (collectArticle.isNeedDelete()) {
                            articleList.add(collectArticle);
                        }
                    }
                    ((BaseActivity) getActivity()).showWaringDialog("警告", "是否删除这" + articleList.size() + "个专辑", new BaseActivity.OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
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

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "请选择要删除的专辑", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void addData() {
        mDatas.removeAll(mDatas);
        addLocalFile();
        adapter.notifyDataSetChanged();
        ptrCharge.onRefreshComplete();
    }

    private void addLocalFile() {
        List<AlbumContent> list = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
        LogUtils.e(list.toString());
        tvDownloadCount.setText("已缓存 (" + (list.size()) + ")");
        List<String> albumIds = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (!albumIds.contains(list.get(i).getAlbumId())) {
                //当前的专辑id不存在专辑id list
                CollectionBean bean = new CollectionBean();
                bean.setShortimgurl(list.get(i).getAlbumAvatar());
                bean.setCoverimgurl(list.get(i).getAlbumAvatar());
                bean.setLastupdatetitle(list.get(i).getArticlename());
                bean.setName(list.get(i).getAlbumName());
                bean.setId(list.get(i).getAlbumId());
                bean.setType(list.get(i).getArticletype());
                bean.setFile(true);
                bean.setContentnum(1);
                bean.setPraisenum(list.get(i).getContentNum());
                albumIds.add(list.get(i).getAlbumId());
                mDatas.add(bean);
            } else {
                //当前的专辑id存在专辑id list，缓存的内容数+1
                for (int j = 0; j < mDatas.size(); j++) {
                    if (TextUtils.equals(mDatas.get(j).getId(), list.get(i).getAlbumId())) {
                        mDatas.get(j).setContentnum(mDatas.get(j).getContentnum() + 1);
                        break;
                    }
                }
            }
        }
    }


    private void initView(View view) {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.head_download_subscribe, null);
        tvDownloadCount = (TextView) headView.findViewById(R.id.tv_download_count);
        lvSub = (ListView) view.findViewById(R.id.lv_charge);
        lvSub.addHeaderView(headView);
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position--;
        if (mDatas == null || mDatas.size() == 0 || position < 0)
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
