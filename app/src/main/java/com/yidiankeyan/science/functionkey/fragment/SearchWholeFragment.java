package com.yidiankeyan.science.functionkey.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.functionkey.adapter.SearchResultForInfoListAdapter;
import com.yidiankeyan.science.functionkey.entity.SearchInfoBean;
import com.yidiankeyan.science.subscribe.entity.SearchAll;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * -全部
 */
public class SearchWholeFragment extends Fragment {

    private SearchResultForInfoListAdapter adapter;
    private ListView lvSearch;
    private List<SearchInfoBean> mSearchList;

    private String searchReturn;

    public SearchWholeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_whole, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lvSearch = (ListView) view.findViewById(R.id.lv_search_whole);
        if (mSearchList == null) {
            mSearchList = new ArrayList<>();
            toHttp(getContext());
        }
        adapter = new SearchResultForInfoListAdapter(getContext(), mSearchList);
        lvSearch.setAdapter(adapter);
    }

    private void initData(SearchAll searchAll) {
        if (searchAll.getAlbumlist().size() > 0) {
            SearchInfoBean bean1 = new SearchInfoBean("专辑", 0);
            bean1.SearchChild(searchAll.getAlbumlist().get(0).getAlbumid(), searchAll.getAlbumlist().get(0).getCoverimgurl(), searchAll.getAlbumlist().get(0).getAlbumauthor(), searchAll.getAlbumlist().get(0).getAlbumname(), searchAll.getAlbumlist().get(0).getRecenttitle(), searchAll.getAlbumlist().get(0).getSubjectname(), searchAll.getAlbumlist().get(0).getContentnum() + "", searchAll.getAlbumlist().get(0).getReadnum() + "", TimeUtils.formatDate(searchAll.getAlbumlist().get(0).getRecentupdatetime()));
            if (searchAll.getAlbumlist().size() > 1)
                bean1.SearchChild(searchAll.getAlbumlist().get(1).getAlbumid(), searchAll.getAlbumlist().get(1).getCoverimgurl(), searchAll.getAlbumlist().get(1).getAlbumauthor(), searchAll.getAlbumlist().get(1).getAlbumname(), searchAll.getAlbumlist().get(1).getRecenttitle(), searchAll.getAlbumlist().get(1).getSubjectname(), searchAll.getAlbumlist().get(1).getContentnum() + "", searchAll.getAlbumlist().get(1).getReadnum() + "", TimeUtils.formatDate(searchAll.getAlbumlist().get(1).getRecentupdatetime()));
            if (searchAll.getAlbumlist().size() > 2)
                bean1.SearchChild(searchAll.getAlbumlist().get(2).getAlbumid(), searchAll.getAlbumlist().get(2).getCoverimgurl(), searchAll.getAlbumlist().get(2).getAlbumauthor(), searchAll.getAlbumlist().get(2).getAlbumname(), searchAll.getAlbumlist().get(2).getRecenttitle(), searchAll.getAlbumlist().get(2).getSubjectname(), searchAll.getAlbumlist().get(2).getContentnum() + "", searchAll.getAlbumlist().get(2).getReadnum() + "", TimeUtils.formatDate(searchAll.getAlbumlist().get(2).getRecentupdatetime()));
            mSearchList.add(bean1);
        }
        if (searchAll.getArticlelist().size() > 0) {
            SearchInfoBean bean2 = new SearchInfoBean("内容", 1);
            bean2.SearchChild(searchAll.getArticlelist().get(0).getMediaurl(), searchAll.getArticlelist().get(0).getId(), searchAll.getArticlelist().get(0).getType(), searchAll.getArticlelist().get(0).getCoverimgurl(), searchAll.getArticlelist().get(0).getName(), searchAll.getArticlelist().get(0).getName(), searchAll.getArticlelist().get(0).getReadnum() + "", searchAll.getArticlelist().get(0).getPraisenum() + "", TimeUtils.formatDate(searchAll.getArticlelist().get(0).getCreatetime()));
            if (searchAll.getArticlelist().size() > 1)
                bean2.SearchChild(searchAll.getArticlelist().get(1).getMediaurl(), searchAll.getArticlelist().get(1).getId(), searchAll.getArticlelist().get(1).getType(), searchAll.getArticlelist().get(1).getCoverimgurl(), searchAll.getArticlelist().get(1).getName(), searchAll.getArticlelist().get(1).getName(), searchAll.getArticlelist().get(1).getReadnum() + "", searchAll.getArticlelist().get(1).getPraisenum() + "", TimeUtils.formatDate(searchAll.getArticlelist().get(1).getCreatetime()));
            if (searchAll.getArticlelist().size() > 2)
                bean2.SearchChild(searchAll.getArticlelist().get(2).getMediaurl(), searchAll.getArticlelist().get(2).getId(), searchAll.getArticlelist().get(2).getType(), searchAll.getArticlelist().get(2).getCoverimgurl(), searchAll.getArticlelist().get(2).getName(), searchAll.getArticlelist().get(2).getName(), searchAll.getArticlelist().get(2).getReadnum() + "", searchAll.getArticlelist().get(2).getPraisenum() + "", TimeUtils.formatDate(searchAll.getArticlelist().get(2).getCreatetime()));
            mSearchList.add(bean2);
        }

//        SearchInfoBean bean3 = new SearchInfoBean("主编", 2);
//        bean3.SearchChild(searchAll.getAuthorlist().get(0).getCoverimgurl(), searchAll.getArticlelist().get(0).getName(),searchAll.getArticlelist().get(0).getName(),searchAll.getArticlelist().get(0).getReadnum()+"", searchAll.getArticlelist().get(0).getPraisenum()+"",Util.formatDate(searchAll.getArticlelist().get(0).getCreatetime()));
//        bean3.SearchChild(searchAll.getAuthorlist().get(1).getCoverimgurl(), searchAll.getArticlelist().get(1).getName(),searchAll.getArticlelist().get(1).getName(),searchAll.getArticlelist().get(1).getReadnum()+"", searchAll.getArticlelist().get(1).getPraisenum()+"",Util.formatDate(searchAll.getArticlelist().get(1).getCreatetime()));
//        bean3.SearchChild(searchAll.getAuthorlist().get(2).getCoverimgurl(), searchAll.getArticlelist().get(2).getName(),searchAll.getArticlelist().get(2).getName(),searchAll.getArticlelist().get(2).getReadnum()+"", searchAll.getArticlelist().get(2).getPraisenum()+"",Util.formatDate(searchAll.getArticlelist().get(2).getCreatetime()));
        if (searchAll.getAuthorlist().size() > 0) {
            SearchInfoBean bean3 = new SearchInfoBean("主编", 2);
            bean3.SearchChild(searchAll.getAuthorlist().get(0).getAuthorid(), searchAll.getAuthorlist().get(0).getImgurl(), searchAll.getAuthorlist().get(0).getName(), searchAll.getAuthorlist().get(0).getNick(), searchAll.getAuthorlist().get(0).getFansnum());
            if (searchAll.getAuthorlist().size() > 1)
                bean3.SearchChild(searchAll.getAuthorlist().get(1).getAuthorid(), searchAll.getAuthorlist().get(1).getImgurl(), searchAll.getAuthorlist().get(1).getName(), searchAll.getAuthorlist().get(1).getNick(), searchAll.getAuthorlist().get(1).getFansnum());
            if (searchAll.getAuthorlist().size() > 2)
                bean3.SearchChild(searchAll.getAuthorlist().get(2).getAuthorid(), searchAll.getAuthorlist().get(2).getImgurl(), searchAll.getAuthorlist().get(2).getName(), searchAll.getAuthorlist().get(2).getNick(), searchAll.getAuthorlist().get(2).getFansnum());
            mSearchList.add(bean3);
        }

        if (searchAll.getAnswerorlist().size() > 0) {
            SearchInfoBean bean4 = new SearchInfoBean("答人", 3);
//        bean4.SearchChild(imgUrl, "一点科研", "啊实打实大师", "45841万", "543341万");
//        bean4.SearchChild(imgUrl, "一点科研", "啊实打实大师", "45841万", "543341万");
//        bean4.SearchChild(imgUrl, "一点科研", "啊实打实大师", "45841万", "543341万");
            bean4.SearchChild(searchAll.getAnswerorlist().get(0).getUserimgurl(), searchAll.getAnswerorlist().get(0).getKdname(), searchAll.getAnswerorlist().get(0).getProfession(), searchAll.getAnswerorlist().get(0).getAnswersnum(), searchAll.getAnswerorlist().get(0).getEavesdropnum(), searchAll.getAnswerorlist().get(0).getFollowernum());
            if (searchAll.getAnswerorlist().size() > 1)
                bean4.SearchChild(searchAll.getAnswerorlist().get(1).getUserimgurl(), searchAll.getAnswerorlist().get(1).getKdname(), searchAll.getAnswerorlist().get(1).getProfession(), searchAll.getAnswerorlist().get(1).getAnswersnum(), searchAll.getAnswerorlist().get(1).getEavesdropnum(), searchAll.getAnswerorlist().get(1).getFollowernum());
            if (searchAll.getAnswerorlist().size() > 2)
                bean4.SearchChild(searchAll.getAnswerorlist().get(2).getUserimgurl(), searchAll.getAnswerorlist().get(2).getKdname(), searchAll.getAnswerorlist().get(2).getProfession(), searchAll.getAnswerorlist().get(2).getAnswersnum(), searchAll.getAnswerorlist().get(2).getEavesdropnum(), searchAll.getAnswerorlist().get(2).getFollowernum());
            mSearchList.add(bean4);
        }
        adapter = new SearchResultForInfoListAdapter(getContext(), mSearchList);
        lvSearch.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void toHttp(Context context) {
        if (searchReturn != null && !TextUtils.isEmpty(searchReturn)) {
            Map<String, Object> map = new HashMap<>();
            map.put("pages", 1);
            map.put("pagesize", 20);
            map.put("type", 0);
            map.put("searchstr", searchReturn);
            map.put("range", 1);
            HttpUtil.post(context, SystemConstant.URL + SystemConstant.POST_SEARCH_SEARCH, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (result.getCode() == 200) {
                        SearchAll searchAll = (SearchAll) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), SearchAll.class);
                        initData(searchAll);
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

    public void clean() {
        if (mSearchList != null)
            mSearchList.removeAll(mSearchList);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        mSearchList = null;
    }

    public void loadData(Context context) {
        if (mSearchList == null) {
            mSearchList = new ArrayList<>();
            toHttp(context);
        }
    }
}
