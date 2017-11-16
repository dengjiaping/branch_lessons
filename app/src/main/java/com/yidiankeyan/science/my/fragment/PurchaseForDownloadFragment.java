package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.AudioControlActivity;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.my.adapter.DownloadFileBookAdapter;
import com.yidiankeyan.science.my.adapter.DownloadFileIssuesAdapter;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 已购的下载内容
 */
public class PurchaseForDownloadFragment extends Fragment {

    private List<IssuesDetailBean> issuesFileList = new ArrayList<>();
    private List<MozReadDetailsBean> bookFileList = new ArrayList<>();
    private ShowAllListView lvIssues;
    private DownloadFileIssuesAdapter issuesAdapter;
    private DownloadFileBookAdapter bookAdapter;
    private ShowAllListView lvBook;
    private PtrClassicFrameLayout ptrCharge;
    private TextView tvDownloadCount;

    private AutoRelativeLayout rlDelete;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private boolean isDeleteState;

    public PurchaseForDownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_purchase_for_download, container, false);
        initView(view);
        ptrCharge.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //填充数据
                if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                    issuesFileList.removeAll(issuesFileList);
                    bookFileList.removeAll(bookFileList);
                    issuesFileList.addAll(DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFiles());
                    bookFileList.addAll(DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFiles());
                    issuesAdapter.notifyDataSetChanged();
                    bookAdapter.notifyDataSetChanged();
                    tvDownloadCount.setText("已缓存 (" + (issuesFileList.size() + bookFileList.size()) + ")");
                } else {
                    tvDownloadCount.setText("已缓存 (0)");
                }
                ptrCharge.onRefreshComplete();
            }
        });
        ptrCharge.autoRefresh();
        ptrCharge.disableWhenHorizontalMove(true);
        ptrCharge.setLastUpdateTimeRelateObject(this);
        issuesAdapter = new DownloadFileIssuesAdapter(issuesFileList, getContext());
        lvIssues.setAdapter(issuesAdapter);
        bookAdapter = new DownloadFileBookAdapter(bookFileList, getContext());
        lvBook.setAdapter(bookAdapter);
        lvIssues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), PreviewColumnContentActivity.class);
//                intent.putExtra("id", issuesFileList.get(position).getId());
//                intent.putExtra("title", issuesFileList.get(position).getColumnname());
//                if (issuesFileList.get(position).getAlreadyWatch() == 0) {
//                    //未观看
//                    issuesFileList.get(position).setAlreadyWatch(1);
//                    DB.getInstance(DemoApplication.applicationContext).updataIssuesWatch(issuesFileList.get(position).getId(), 1);
//                }
                AudioPlayManager.getInstance().init(issuesFileList, position, AudioPlayManager.PlayModel.ORDER);
                startActivity(new Intent(getContext(), AudioControlActivity.class));
            }
        });
        issuesAdapter.setOnDeleteChangeListener(new DownloadFileIssuesAdapter.OnDeleteChangeListener() {
            @Override
            public void onDeleteChanged() {
                //先定义全选和删除都为不可用，遍历后有一条没选中择把allCheck置为1，有一条为选中择把delete置为1
                int allCheck = 0, delete = 0;
                for (IssuesDetailBean collectArticle : issuesFileList) {
                    if (collectArticle.isNeedDelete()) {
                        //已被选中
                        delete = 1;
                    } else {
                        //未被选中
                        allCheck = 1;
                    }
                    if (delete == 1 && allCheck == 1) {
                        break;
                    }
                }
                for (MozReadDetailsBean book : bookFileList) {
                    if (delete == 1 && allCheck == 1) {
                        break;
                    }
                    if (book.isNeedDelete()) {
                        delete = 1;
                    } else {
                        allCheck = 1;
                    }
                }
                if (allCheck == 1) {
                    tvCheckAll.setTag(1);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvCheckAll.setTag(0);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.black_33));
                }
                if (delete == 1) {
                    tvDelete.setTag(1);
                    tvDelete.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvDelete.setTag(0);
                    tvDelete.setTextColor(getResources().getColor(R.color.menu));
                }
            }
        });
        lvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
//                if (bookFileList.get(position).getName() != null && bookFileList.get(position).getName().startsWith("类型：杂志")) {
//                    intent = new Intent(getContext(), MozMagazineAudioActivity.class);
//                } else {
//                    intent = new Intent(getContext(), MozReadAudioActivity.class);
//                }
//                intent.putExtra("id", bookFileList.get(position).getId());
//                if (bookFileList.get(position).getAlreadyWatch() == 0) {
//                    //未观看
//                    bookFileList.get(position).setAlreadyWatch(1);
//                    DB.getInstance(DemoApplication.applicationContext).updataBookWatch(bookFileList.get(position).getId(), 1);
//                }
                AudioPlayManager.getInstance().init(bookFileList, position, AudioPlayManager.PlayModel.ORDER);
                startActivity(new Intent(getContext(), AudioControlActivity.class));
//                startActivity(intent);
            }
        });
        bookAdapter.setOnDeleteChangeListener(new DownloadFileBookAdapter.OnDeleteChangeListener() {
            @Override
            public void onDeleteChanged() {
                //先定义全选和删除都为不可用，遍历后有一条没选中择把allCheck置为1，有一条为选中择把delete置为1
                int allCheck = 0, delete = 0;
                for (IssuesDetailBean collectArticle : issuesFileList) {
                    if (collectArticle.isNeedDelete()) {
                        //已被选中
                        delete = 1;
                    } else {
                        //未被选中
                        allCheck = 1;
                    }
                    if (delete == 1 && allCheck == 1) {
                        break;
                    }
                }
                for (MozReadDetailsBean book : bookFileList) {
                    if (delete == 1 && allCheck == 1) {
                        break;
                    }
                    if (book.isNeedDelete()) {
                        delete = 1;
                    } else {
                        allCheck = 1;
                    }
                }
                if (allCheck == 1) {
                    tvCheckAll.setTag(1);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvCheckAll.setTag(0);
                    tvCheckAll.setTextColor(getResources().getColor(R.color.black_33));
                }
                if (delete == 1) {
                    tvDelete.setTag(1);
                    tvDelete.setTextColor(getResources().getColor(R.color.defaultcolortwo));
                } else {
                    tvDelete.setTag(0);
                    tvDelete.setTextColor(getResources().getColor(R.color.menu));
                }
            }
        });
        tvCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(1)) {
                    for (IssuesDetailBean collectArticle : issuesFileList) {
                        collectArticle.setNeedDelete(true);
                    }
                    for (MozReadDetailsBean book : bookFileList) {
                        book.setNeedDelete(true);
                    }
                    issuesAdapter.notifyDataSetChanged();
                    bookAdapter.notifyDataSetChanged();
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(1)) {
                    final List<IssuesDetailBean> articleList = new ArrayList<>();
                    for (IssuesDetailBean collectArticle : issuesFileList) {
                        if (collectArticle.isNeedDelete()) {
                            articleList.add(collectArticle);
                        }
                    }
                    final List<MozReadDetailsBean> deleteBookList = new ArrayList<MozReadDetailsBean>();
                    for (MozReadDetailsBean book : bookFileList) {
                        if (book.isNeedDelete()) {
                            deleteBookList.add(book);
                        }
                    }
                    ((BaseActivity) getActivity()).showWaringDialog("警告", "是否删除这" + (articleList.size() + deleteBookList.size()) + "个文件", new BaseActivity.OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            for (IssuesDetailBean collectArticle : articleList) {
                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadIssues(collectArticle.getId());
                                try {
                                    new File(collectArticle.getFilePath()).delete();
                                } catch (Exception e) {
                                }
                            }
                            for (MozReadDetailsBean book : deleteBookList) {
                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadBook(book.getId());
                                try {
                                    new File(book.getFilePath()).delete();
                                } catch (Exception e) {
                                }
                            }
                            issuesFileList.removeAll(articleList);
                            bookFileList.removeAll(deleteBookList);
                            issuesAdapter.notifyDataSetChanged();
                            bookAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "请选择要删除的文件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        issuesAdapter.notifyDataSetChanged();
        bookAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        lvIssues = (ShowAllListView) view.findViewById(R.id.lv_issues);
        lvBook = (ShowAllListView) view.findViewById(R.id.lv_book);
        ptrCharge = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_col_charge);
        rlDelete = (AutoRelativeLayout) view.findViewById(R.id.rl_delete);
        tvCheckAll = (TextView) view.findViewById(R.id.tv_check_all);
        tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        tvDownloadCount = (TextView) view.findViewById(R.id.tv_download_count);
    }

    public void onCancelClick() {
        isDeleteState = false;
        issuesAdapter.setDeleteState(false);
        issuesAdapter.notifyDataSetChanged();
        bookAdapter.setDeleteState(false);
        bookAdapter.notifyDataSetChanged();
        rlDelete.setVisibility(View.GONE);
    }

    public void onDeleteClick() {
        isDeleteState = true;
        issuesAdapter.setDeleteState(true);
        issuesAdapter.notifyDataSetChanged();
        bookAdapter.setDeleteState(true);
        bookAdapter.notifyDataSetChanged();
        rlDelete.setVisibility(View.VISIBLE);
        if (bookFileList != null && issuesFileList != null && bookFileList.size() + issuesFileList.size() > 0) {
            tvCheckAll.setTextColor(getResources().getColor(R.color.defaultcolortwo));
            tvCheckAll.setTag(1);
            tvDelete.setTag(0);
            for (IssuesDetailBean collectArticle : issuesFileList) {
                collectArticle.setNeedDelete(false);
            }
            for (MozReadDetailsBean book : bookFileList) {
                book.setNeedDelete(false);
            }
        } else {
            tvCheckAll.setTag(0);
            tvDelete.setTag(0);
        }
    }
}
