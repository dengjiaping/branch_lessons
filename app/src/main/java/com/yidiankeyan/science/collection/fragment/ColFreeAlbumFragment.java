package com.yidiankeyan.science.collection.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.collection.adapter.CollectArticleAdapter;
import com.yidiankeyan.science.collection.entity.CollectArticle;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
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
 * 点藏
 * -收藏的内容
 */
public class ColFreeAlbumFragment extends Fragment {

    private PtrClassicFrameLayout ptrCollection;
    private CollectArticleAdapter adapter;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<CollectArticle> mDatas = new ArrayList<>();
    //轮播图数据
    private List<BannerBean> mBannerList = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pages = 1;
    private boolean flag = true;
    private AutoLinearLayout ll_all;
    private Intent intent;
    private AutoRelativeLayout rlDelete;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private boolean isDeleteState;

    public ColFreeAlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_col_free_album, container, false);
        //初始化控件
        initView(view);
        ptrCollection.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                pages = 1;
                addData();
            }
        });
        ptrCollection.disableWhenHorizontalMove(true);
        ptrCollection.setLastUpdateTimeRelateObject(this);

        if (mDatas.size() == 0) {
            ptrCollection.autoRefresh();
        } else {
            lvSub.setHasLoadMore(true);
        }
        lvSub.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                addData();
            }
        });
        adapter.setOnDeleteChangeListener(new CollectArticleAdapter.OnDeleteChangeListener() {
            @Override
            public void onDeleteChanged() {
                //先定义全选和删除都为不可用，遍历后有一条没选中择把allCheck置为1，有一条为选中择把delete置为1
                int allCheck = 0, delete = 0;
                for (CollectArticle collectArticle : mDatas) {
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
                    for (CollectArticle collectArticle : mDatas) {
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
                    toHttpBatchdelUncollect();
                }
            }
        });
        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDatas == null || mDatas.size() == 0)
                    return;
                Intent intent;
                switch (mDatas.get(position).getArticletype()) {
                    case 1:
                        intent = new Intent(getActivity(), ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mDatas.get(position).getArticleid());
                        startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mDatas.get(position).getName());
                        audio.setArticleid(mDatas.get(position).getArticleid());
                        audio.setArticletype(2);
                        audio.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getArticleid());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(getActivity(), AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", position);
                        intent.putExtra("id", listItem.get(position).getArticleid());
                        intent.putExtra("single", true);
                        startActivity(intent);
                        break;
                    case 3:
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mDatas.get(position).getName());
                        video.setArticleid(mDatas.get(position).getArticleid());
                        video.setArticletype(2);
                        video.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getArticleid());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(getActivity(), VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("id", listItemVideo.get(position).getArticleid());
                        intent.putExtra("position", position);
                        startActivity(intent);
                        break;
                }
            }
        });
        return view;
    }

    /**
     * 批量取消收藏
     */
    private void toHttpBatchdelUncollect() {
        List<String> list = new ArrayList<>();
        final List<CollectArticle> articleList = new ArrayList<>();
        for (CollectArticle collectArticle : mDatas) {
            if (collectArticle.isNeedDelete()) {
                list.add(collectArticle.getCollectid());
                articleList.add(collectArticle);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("collectids", list);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.BATCHDER_UNCOLLECT_ARTICLE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    mDatas.removeAll(articleList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_COLLECTION_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<CollectArticle> mData = GsonUtils.json2List(jsonData, CollectArticle.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (mData.size() > 0) {
                        lvSub.setHasLoadMore(true);
                        mDatas.addAll(mData);
                        pages++;
                    } else {
                        lvSub.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvSub.showFailUI();
                }
                ptrCollection.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrCollection.onRefreshComplete();
                lvSub.showFailUI();
            }

        });
    }

    private void initView(View view) {
        lvSub = (ListViewFinalLoadMore) view.findViewById(R.id.lv_Download);
        ptrCollection = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_collection);
        ll_all = (AutoLinearLayout) getActivity().findViewById(R.id.main);
        adapter = new CollectArticleAdapter(getActivity(), mDatas);
        rlDelete = (AutoRelativeLayout) view.findViewById(R.id.rl_delete);
        tvCheckAll = (TextView) view.findViewById(R.id.tv_check_all);
        tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        lvSub.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    public AutoLinearLayout getLl_all() {
        return ll_all;
    }

    public void setLl_all(AutoLinearLayout ll_all) {
        this.ll_all = ll_all;
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
            for (CollectArticle collectArticle : mDatas) {
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
}
