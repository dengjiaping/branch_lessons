package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.HotNewsAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * 音频收藏
 */
public class CollectionAudioFragment extends Fragment {

    private PtrClassicFrameLayout ptrCollection;
    private HotNewsAdapter adapter;
    private ListViewFinalLoadMore lvSub;
    //页面数据
    private List<HotNewsBean> mDatas = new ArrayList<>();
    private int pages = 1;

    public CollectionAudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection_graphic, container, false);
        lvSub = (ListViewFinalLoadMore) view.findViewById(R.id.lv_Download);
        ptrCollection = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_collection);
        adapter = new HotNewsAdapter(getContext(), mDatas);
        lvSub.setAdapter(adapter);
        ptrCollection.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                pages = 1;
                toHttpGetCollection();
            }
        });
        if (mDatas.size() == 0)
            ptrCollection.autoRefresh();
        lvSub.setHasLoadMore(true);
        ptrCollection.disableWhenHorizontalMove(true);
        ptrCollection.setLastUpdateTimeRelateObject(getContext());
        lvSub.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetCollection();
            }
        });
        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDatas == null || mDatas.size() == 0)
                    return;
                Intent intent;
                AlbumContent audio = new AlbumContent(null);
                audio.setArticlename(mDatas.get(position).getName());
                audio.setArticleid(mDatas.get(position).getId());
                audio.setLastupdatetime(mDatas.get(position).getCreatetime());
                audio.setArticletype(2);
                audio.setMediaurl(mDatas.get(position).getMediaurl());
                audio.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
                if (content != null)
                    audio.setFilePath(content.getFilePath());
                ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                listItem.add(audio);
                intent = new Intent(getContext(), AudioAlbumActivity.class);
                intent.putParcelableArrayListExtra("list", listItem);
                intent.putExtra("position", 0);
                intent.putExtra("single", true);
                intent.putExtra("id", mDatas.get(position).getId());
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void toHttpGetCollection() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", 6);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getCollection(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    List<HotNewsBean> mData = response.body().getData();
                    Iterator<HotNewsBean> iterator = mData.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next() == null)
                            iterator.remove();
                    }
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
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrCollection.onRefreshComplete();
                lvSub.showFailUI();
            }
        });
    }

}
