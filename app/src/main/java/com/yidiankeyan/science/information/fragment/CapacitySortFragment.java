package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.WholeAlbumAdapter;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
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
 * 猜你喜欢
 *      -按容量排序
 */
public class CapacitySortFragment extends Fragment {
    private WholeAlbumAdapter albumAdapter;
    private ListView lvSciencefmAll;
    private List<BusinessAllBean> mDatas = new ArrayList<>();

    public CapacitySortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_content_sort, container, false);
        //初始化控件
        initView(view);

        //填充数据
        if (mDatas.size() == 0)
            addData();
        albumAdapter = new WholeAlbumAdapter(getActivity(), mDatas);
        lvSciencefmAll.setAdapter(albumAdapter);
        lvSciencefmAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getAlbumname());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                startActivity(intent);
            }
        });
        return view;
    }

    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", "SIZE");
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_GUESS_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<BusinessAllBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                    mDatas.removeAll(mDatas);
                    mDatas.addAll(datas);
                    albumAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void initView(View view) {
        lvSciencefmAll = (ListView) view.findViewById(R.id.lv_capa);
    }

}
