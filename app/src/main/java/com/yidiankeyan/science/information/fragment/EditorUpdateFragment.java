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
import com.yidiankeyan.science.information.adapter.EditorRecommentAdapter;
import com.yidiankeyan.science.information.entity.EditorRecommentBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
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
 * 推荐
 * -总编推荐
 * -按更新排序
 */
public class EditorUpdateFragment extends Fragment {

    private ListView lvSciencefmAll;
    private List<EditorRecommentBean> mDatas = new ArrayList<>();
    private EditorRecommentAdapter adapter;

    public EditorUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor_update, container, false);
        //初始化控件
        initView(view);
        //填充数据
        if (mDatas.size() == 0)
            toHttp();
        adapter = new EditorRecommentAdapter(getContext(), mDatas);
        lvSciencefmAll.setAdapter(adapter);
        lvSciencefmAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getShortimgurl());
                startActivity(intent);
            }
        });
        return view;
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        map.put("ordertype", 1);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_RECOMMEND_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<EditorRecommentBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), EditorRecommentBean.class);
                    mDatas.removeAll(mDatas);
                    mDatas.addAll(datas);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void initView(View view) {
        lvSciencefmAll = (ListView) view.findViewById(R.id.lv_edupdate);
    }
}