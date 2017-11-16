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
import com.yidiankeyan.science.functionkey.adapter.SearchAnswerAdapter;
import com.yidiankeyan.science.information.acitivity.AnswerTopDetailActivity;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
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
 * -答人
 */
public class SearchAnswerFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvSearchAnswer;
    private SearchAnswerAdapter adapter;
    private List<AnswerPeopleDetail> listHelp;
    private String searchReturn;

    public SearchAnswerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_answer, container, false);
        initView(view);
        if (listHelp == null) {
//            initData();
            listHelp = new ArrayList<>();
            toHttpGetAnswerTop(getContext());
        }
        adapter = new SearchAnswerAdapter(getContext(), listHelp);
        lvSearchAnswer.setAdapter(adapter);
        lvSearchAnswer.setOnItemClickListener(this);
        return view;
    }

    /**
     * 获取所有答人
     *
     * @param context
     */
    public void toHttpGetAnswerTop(Context context) {
        if (searchReturn != null && !TextUtils.isEmpty(searchReturn)) {
            Map<String, Object> map = new HashMap();
            map.put("pages", 1);
            map.put("pagesize", 20);
            map.put("type", 4);
            map.put("range", 1);
            map.put("searchstr", searchReturn);

            HttpUtil.post(context, SystemConstant.URL + SystemConstant.POST_SEARCH_SEARCH, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (200 == result.getCode()) {
                        listHelp.removeAll(listHelp);
                        List<AnswerPeopleDetail> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                        listHelp.addAll(datas);
                        adapter = new SearchAnswerAdapter(getContext(), listHelp);
                        lvSearchAnswer.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                }
            });
        }
    }

    public void setSearchReturn(String searchReturn) {
        this.searchReturn = searchReturn;
    }

    private void initView(View view) {
        lvSearchAnswer = (ListView) view.findViewById(R.id.lv_search_answer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AnswerTopDetailActivity.class);
        intent.putExtra("id", listHelp.get(position).getId());
        intent.putExtra("answerName", listHelp.get(position).getName());
        intent.putExtra("answerAvatar", listHelp.get(position).getProfession());
        startActivity(intent);
    }

    public void clean() {
        if (listHelp != null)
            listHelp.removeAll(listHelp);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        listHelp = null;
    }

    public void loadData(Context context) {
        if (listHelp == null) {
//            initData();
            listHelp = new ArrayList<>();
            toHttpGetAnswerTop(context);
        }
    }
}
