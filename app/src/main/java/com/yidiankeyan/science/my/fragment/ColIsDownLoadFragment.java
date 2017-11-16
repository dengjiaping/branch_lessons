package com.yidiankeyan.science.my.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.collection.adapter.DownloadListAdapter;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadInfo;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.my.adapter.DownloadFinishAdapter;

import org.xutils.ex.DbException;

/**
 * 点藏
 * -正在下载
 */
public class ColIsDownLoadFragment extends Fragment {

    private ListView lvDownloadList;
    private DownloadListAdapter adapter;

    public ColIsDownLoadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_col_already_load, container, false);
        initView(view);
        adapter = new DownloadListAdapter(getContext());
        adapter.setOnItemClickListener(new DownloadListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                String id = DownloadManager.getInstance().getDownloadInfoList().get(position).getId()+"";
//                removeItem(id,position);
//                adapter.notifyDataSetChanged();
            }
        });
        lvDownloadList.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        lvDownloadList = (ListView) view.findViewById(R.id.lv_download_list);
//        llPauseAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ("全部暂停".equals(tvDownloadState.getText().toString())) {
//                    DownloadManager.getInstance().stopAllDownload();
//                    tvDownloadState.setText("全部继续");
//                } else {
//                    tvDownloadState.setText("全部暂停");
//                    for (int i = 0; i < DownloadManager.getInstance().getDownloadListCount(); i++) {
//                        if (DownloadManager.getInstance().getDownloadInfo(i).getState().value() != DownloadState.STARTED.value()
//                                && DownloadManager.getInstance().getDownloadInfo(i).getState().value() != DownloadState.FINISHED.value()) {
//                            try {
//                                DownloadManager.getInstance().startDownload(
//                                        DownloadManager.getInstance().getDownloadInfo(i).getUrl(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).getLabel(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).getFileSavePath(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).isAutoResume(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).isAutoRename(),
//                                        null,
//                                        DownloadManager.getInstance().getDownloadInfo(i).getContentId(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).getFileType(),
//                                        DownloadManager.getInstance().getDownloadInfo(i).getContentNum()
//                                );
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }
//                            Log.e("state", "====" + DownloadManager.getInstance().getDownloadInfo(i).getState().value());
//                        }
//                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    }).start();
//                }
//            }
//        });
//        llRemoveAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    for (int i = 0; i < DownloadManager.getInstance().getDownloadListCount(); i++) {
//                        DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(i);
//                        switch (downloadInfo.getFileType()) {
//                            case 1:
//                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(downloadInfo.getContentId());
//                                break;
//                            case 2:
//                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadIssues(downloadInfo.getContentId());
//                                break;
//                            case 3:
//                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadBook(downloadInfo.getContentId());
//                                break;
//                        }
//                    }
//                    DownloadManager.getInstance().removeAllDownload();
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    private void removeItem(String id,int position) {
        for (int i = 0; i < DownloadManager.getInstance().getDownloadListCount(); i++) {
            if(!StringUtils.isEmpty(DownloadManager.getInstance().getDownloadInfoList().get(position).getId()+"")
                    &&id.equals(DownloadManager.getInstance().getDownloadInfoList().get(position).getId()+"")){
                try {
                    DownloadManager.getInstance().removeDownload(position);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notifyDataSetChanged() {
        Log.e("====", "====notifyDataSetChanged");
        if (adapter != null) {
//            adapter = new DownloadListAdapter(getContext());
//            lvDownloadList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
