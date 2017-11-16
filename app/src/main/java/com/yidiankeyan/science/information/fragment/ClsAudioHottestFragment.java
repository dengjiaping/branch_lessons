package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.ClassAdapter;
import com.yidiankeyan.science.information.entity.ClassAllBean;
import com.yidiankeyan.science.subscribe.activity.TransitionActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

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
 * 分类
 * -音频专辑
 * -最热
 */
public class ClsAudioHottestFragment extends Fragment {

    private ClassAdapter albumAdapter;
    private ListViewFinalLoadMore lvSciencefmAll;
    private List<ClassAllBean> mDatas = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pages = 1;
    private boolean flag = true;
    private PtrClassicFrameLayout ptrLayout;

    public ClsAudioHottestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cls_audio_booked, container, false);
        //初始化控件
        initView(view);

        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetAllTextAlbum();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else
            lvSciencefmAll.setHasLoadMore(true);
        lvSciencefmAll.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetAllTextAlbum();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        albumAdapter = new ClassAdapter(getActivity(), mDatas);
        lvSciencefmAll.setAdapter(albumAdapter);
        lvSciencefmAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TransitionActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getAlbumname());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return view;
    }

    private void toHttpGetAllTextAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("classifyType", "HOT");
        entity.put("albumType", 2);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_CLASS_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<ClassAllBean> mData = GsonUtils.json2List(jsonData, ClassAllBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (mData.size() > 0) {
                        lvSciencefmAll.setHasLoadMore(true);
                        mDatas.addAll(mData);
                        pages++;
                    } else {
                        lvSciencefmAll.setHasLoadMore(false);
                    }
                    albumAdapter.notifyDataSetChanged();
                } else {
                    lvSciencefmAll.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvSciencefmAll.showFailUI();
            }
        });
    }


    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        lvSciencefmAll = (ListViewFinalLoadMore) view.findViewById(R.id.lv_audio_hottest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }
}
