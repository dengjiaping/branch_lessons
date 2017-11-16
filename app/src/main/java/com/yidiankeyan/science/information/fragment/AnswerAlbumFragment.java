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
import com.yidiankeyan.science.information.acitivity.AnswerDetailsActivity;
import com.yidiankeyan.science.information.acitivity.ScienceHelpActivity;
import com.yidiankeyan.science.information.adapter.AnswerAlbumAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科答
 * -问答专辑
 */
public class AnswerAlbumFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvAnswerAlbum;
    private AnswerAlbumAdapter adapter;
    private List<AnswerAlbumBean> listHelp = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pageNum = 0;
    private boolean flag = true;
    private Intent intent;

    public AnswerAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer_album, container, false);
        initView(view);
        if (listHelp.size() == 0)
            toHttpGetAnswerAlbum();
        adapter = new AnswerAlbumAdapter(getContext(), listHelp);
        lvAnswerAlbum.setAdapter(adapter);
        lvAnswerAlbum.setOnItemClickListener(this);
        return view;
    }


    private void toHttpGetAnswerAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_ANSWER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<AnswerAlbumBean> mData = GsonUtils.json2List(jsonData, AnswerAlbumBean.class);
                    listHelp.addAll(mData);
                    ((ScienceHelpActivity) getActivity()).setmData((ArrayList<AnswerAlbumBean>) mData);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void initView(View view) {
        lvAnswerAlbum = (ListView) view.findViewById(R.id.lv_answer_album);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String strValue = listHelp.get(position).getKdname();
        intent = new Intent(getActivity(), AnswerDetailsActivity.class);
        intent.putExtra("name", strValue);
        intent.putExtra("id", listHelp.get(position).getId());
        startActivity(intent);
    }

}
