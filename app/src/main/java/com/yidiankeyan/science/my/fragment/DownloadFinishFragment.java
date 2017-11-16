package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.AudioControlActivity;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.my.adapter.DownloadFinishAdapter;
import com.yidiankeyan.science.my.entity.DownloadFinishBean;
import com.yidiankeyan.science.utils.AudioPlayManager;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 下载完成
 */
public class DownloadFinishFragment extends Fragment {

    private List<DownloadFinishBean> mData = new ArrayList<>();
    private ListView lvDownload;
    private DownloadFinishAdapter adapter;
    private PtrClassicFrameLayout ptrLayout;
    private List<AlbumContent> mListAlbum;
    private List<IssuesDetailBean> mListIssues;
    private List<MozReadDetailsBean> mListBook;
    private List<MagazineExcerptBean> mListMagazine;

    public DownloadFinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download_finish, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initView(View view) {
        lvDownload = (ListView) view.findViewById(R.id.lv_download);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
    }

    private void initAction() {
        adapter = new DownloadFinishAdapter(getContext(), mData);
        lvDownload.setAdapter(adapter);
        lvDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioPlayManager.getInstance().init(mData.get(position).getList(), 0, AudioPlayManager.PlayModel.ORDER);
                AudioPlayManager.getInstance().ijkStart();
                DemoApplication.isPlay = true;
                startActivity(new Intent(getContext(), AudioControlActivity.class));
            }
        });

        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mData.removeAll(mData);
                getData();
                adapter.notifyDataSetChanged();
                ptrLayout.onRefreshComplete();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.autoRefresh();
        adapter.setOnItemClickListener(new DownloadFinishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mData.get(position).getId();
                removeItem(id);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void removeItem(String id) {

        if(mListAlbum.size()>0){
            for(AlbumContent albumContent : mListAlbum){
                if(!StringUtils.isEmpty(albumContent.getArticleid())){
                    if(id.equals(albumContent.getArticleid())){
                        //
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(id);
                    }
                }
            }
        }
        if(mListIssues.size()>0){
            for(IssuesDetailBean issuesDetailBean : mListIssues){
                if(!StringUtils.isEmpty(issuesDetailBean.getId())){
                    if(id.equals(issuesDetailBean.getId())){
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadIssues(id);
                    }
                }
            }
        }
        if(mListBook.size()>0){
            for(MozReadDetailsBean mozReadDetailsBean : mListBook){
                if(!StringUtils.isEmpty(mozReadDetailsBean.getId())){
                    if(id.equals(mozReadDetailsBean.getId())){
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadBook(id);
                    }
                }
            }
        }
        if(mListMagazine.size()>0){
            for(MagazineExcerptBean magazineExcerptBean :mListMagazine){
                if(!StringUtils.isEmpty(magazineExcerptBean.getId())){
                    if(id.equals(magazineExcerptBean.getId())){
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadMagazine(id);
                    }
                }
            }
        }
        mData.removeAll(mData);
        getData();
    }

    private void getData() {
        mListAlbum = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
        mListIssues = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFiles();
        mListBook = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFiles();
        mListMagazine = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFiles();

        addListAlbum(mListAlbum);
        addListIssues(mListIssues);

        addListBook(mListBook);
        addListMagazine(mListMagazine);
    }

    private void addListBook(List<MozReadDetailsBean> listBook) {
        for (MozReadDetailsBean book : listBook) {
            List<MozReadDetailsBean> list = new ArrayList<>();
            list.add(book);
            DownloadFinishBean downloadFinishBean = new DownloadFinishBean();
            downloadFinishBean.setId(book.getId());
            downloadFinishBean.setCover(book.getCoverimgurl());
            downloadFinishBean.setAuthor(book.getAuthor());
            downloadFinishBean.setName(book.getName());
            downloadFinishBean.setList(list);
            mData.add(downloadFinishBean);
        }
    }

    private void addListMagazine(List<MagazineExcerptBean> listMagazine) {
        for (MagazineExcerptBean magazine : listMagazine) {
            boolean flag = true;
            for (DownloadFinishBean downloadFinishBean : mData) {
                if (TextUtils.equals(downloadFinishBean.getId(), magazine.getMonthlyId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //当前的音频专辑还没有加入列表中
                List<MagazineExcerptBean> list = new ArrayList<>();
                for (MagazineExcerptBean magazineExcerptBean : listMagazine) {
                    if (TextUtils.equals(magazine.getMonthlyId(), magazineExcerptBean.getMonthlyId())) {
                        list.add(magazineExcerptBean);
                    }
                }
                DownloadFinishBean downloadFinishBean = new DownloadFinishBean();
                downloadFinishBean.setId(magazine.getMonthlyId());
                downloadFinishBean.setCover(magazine.getCoverimg());
                downloadFinishBean.setName(magazine.getName());
                downloadFinishBean.setAuthor(magazine.getauthor());
                downloadFinishBean.setList(list);
                mData.add(downloadFinishBean);
            }
        }

    }


    private void addListIssues(List<IssuesDetailBean> listIssues) {
        for (IssuesDetailBean issues : listIssues) {
            List<IssuesDetailBean> list = new ArrayList<>();
            list.add(issues);
            DownloadFinishBean downloadFinishBean = new DownloadFinishBean();
            downloadFinishBean.setId(issues.getId());
            downloadFinishBean.setCover(issues.getCoverimg());
            downloadFinishBean.setName(issues.getName());
            downloadFinishBean.setAuthor(issues.getName());
            downloadFinishBean.setList(list);
            mData.add(downloadFinishBean);
        }
    }

    private void addListAlbum(List<AlbumContent> listAlbum) {
        for (AlbumContent content : listAlbum) {
            boolean flag = true;
            for (DownloadFinishBean downloadFinishBean : mData) {
                if (TextUtils.equals(downloadFinishBean.getId(), content.getAlbumId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //当前的音频专辑还没有加入列表中
                List<AlbumContent> list = new ArrayList<>();
                for (AlbumContent albumContent : listAlbum) {
                    if (TextUtils.equals(content.getAlbumId(), albumContent.getAlbumId())) {
                        list.add(albumContent);
                    }
                }
                DownloadFinishBean downloadFinishBean = new DownloadFinishBean();
                downloadFinishBean.setId(content.getAlbumId());
                downloadFinishBean.setCover(content.getCoverimgurl());
                downloadFinishBean.setName(content.getAlbumName());
                downloadFinishBean.setAuthor(content.getAuthorname());
                downloadFinishBean.setList(list);
                mData.add(downloadFinishBean);
            }
        }

    }

}
