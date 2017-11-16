package com.yidiankeyan.science.functionkey.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.functionkey.adapter.SearchContentAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessContentBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * -搜索结果
 * -内容
 */
public class SearchContentFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvContent;
    private SearchContentAdapter adapter;
    private List<BusinessContentBean> mDatas;
    private String searchReturn;

    public SearchContentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_content, container, false);
        initView(view);
        if (mDatas==null) {
            mDatas = new ArrayList<>();
            initDatas(getContext());
        }
        adapter = new SearchContentAdapter(getContext(), mDatas);
        lvContent.setAdapter(adapter);
        lvContent.setOnItemClickListener(this);
        return view;
    }

    public void initDatas(Context context) {
        if (searchReturn != null && !TextUtils.isEmpty(searchReturn)) {
            Map<String, Object> map = new HashMap();
            map.put("pages", 1);
            map.put("pagesize", 20);
            map.put("type", 2);
            map.put("range", 1);
            map.put("searchstr", searchReturn);

            HttpUtil.post(context, SystemConstant.URL + SystemConstant.POST_SEARCH_SEARCH, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (200 == result.getCode()) {
                        mDatas.removeAll(mDatas);
                        List<BusinessContentBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessContentBean.class);
                        mDatas.addAll(datas);
                        adapter = new SearchContentAdapter(getContext(), mDatas);
                        lvContent.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                }
            });
        }
    }

    private void initView(View view) {
        lvContent = (ListView) view.findViewById(R.id.lv_content);
    }


    public void setSearchReturn(String searchReturn) {
        this.searchReturn = searchReturn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (mDatas.get(position).getType() == 1) {
            intent = new Intent(getActivity(), ImgTxtAlbumActivity.class);
            intent.putExtra("id", mDatas.get(position).getId());
            startActivity(intent);
        } else if (mDatas.get(position).getType() == 2) {
            ArrayList<AlbumContent> listItem = new ArrayList<>();
            AlbumContent albumContent = new AlbumContent(mDatas.get(position).getName());
            albumContent.setArticleid(mDatas.get(position).getId());
            albumContent.setMediaurl(mDatas.get(position).getMediaurl());
            listItem.add(albumContent);
            intent = new Intent(getActivity(), AudioAlbumActivity.class);
            intent.putParcelableArrayListExtra("list", listItem);
            intent.putExtra("single", true);
            intent.putExtra("id", listItem.get(0).getArticleid());
            intent.putExtra("position", 0);
            startActivity(intent);
        } else if (mDatas.get(position).getType() == 3) {
            ArrayList<AlbumContent> listItem = new ArrayList<>();
            AlbumContent albumContent = new AlbumContent(mDatas.get(position).getName());
            albumContent.setArticleid(mDatas.get(position).getId());
            albumContent.setMediaurl(mDatas.get(position).getMediaurl());
            listItem.add(albumContent);
            intent = new Intent(getActivity(), VideoAlbumActivity.class);
            intent.putParcelableArrayListExtra("list", listItem);
            intent.putExtra("id", listItem.get(0).getArticleid());
            intent.putExtra("position", 0);
            startActivity(intent);
        }
    }

    public void clean() {
        if (mDatas!=null)
        mDatas.removeAll(mDatas);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        mDatas = null;
    }

    public void loadData(Context context) {
        if (mDatas==null) {
            mDatas = new ArrayList<>();
            initDatas(context);
        }

    }
}
