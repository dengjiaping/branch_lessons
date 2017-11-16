package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.adapter.ColumnAudioAdapter;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.OnMultiClickListener;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 专栏详情
 * -音频
 */
public class ColumnAudioFragment extends Fragment implements View.OnClickListener {

    private RefreshRecyclerView recyclerView;
    private int pages = 1;
    private List<ColumnAudioBean> listAll = new ArrayList<>();
    private int orientation = 0;
    private ColumnAudioAdapter adapter;
    private AutoLinearLayout llPlay;
    private ImageView imgOrientation;
    private TextView tvOrientation;
    private AutoLinearLayout llOrientation;
    private AutoLinearLayout llDownload;
    private View rootView;


    public ColumnAudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_column_audio, container, false);
            initView(rootView);
            initAction();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initAction() {
        initRecyclerView();
        toHttpGetAudio();
        llOrientation.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (orientation == 1) {
                    //降序
                    imgOrientation.setImageResource(R.drawable.icon_audio_column_reverse);
                    orientation = 0;
                    tvOrientation.setText("倒序");
                } else {
                    imgOrientation.setImageResource(R.drawable.icon_audio_column_normal_list);
                    orientation = 1;
                    tvOrientation.setText("正序");
                }
//                List<ColumnAudioBean> listData = adapter.getData();
//                Collections.reverse(listData);
//                adapter.notifyDataSetChanged();
                pages = 1;
                toHttpGetAudio();
            }
        });
        llDownload.setOnClickListener(this);
        llPlay.setOnClickListener(this);
    }

    private void initRecyclerView() {
        adapter = new ColumnAudioAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.isShowNoMore = false;
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                toHttpGetAudio();
            }
        });
    }


    private void toHttpGetAudio() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pages);
        map.put("pageSize", 10);
        map.put("columnId", getArguments().getString("id"));
        map.put("release", "1");
        map.put("sort", orientation + "");
        ApiServerManager.getInstance().getApiServer().getColumnAudio(map).enqueue(new RetrofitCallBack<List<ColumnAudioBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<ColumnAudioBean>>> call, Response<RetrofitResult<List<ColumnAudioBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1) {
                        listAll.removeAll(listAll);
                    }
                    if (response.body().getData().size() > 0) {
                        listAll.addAll(response.body().getData());
                        pages++;
                        adapter.clear();
                        adapter.addAll(listAll);
                    } else {
                        adapter.mNoMoreView.setText("没有更多了");
                        recyclerView.showNoMore();
                    }
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        ColumnAudioBean content = adapter.getData().get(i);
                        if (TextUtils.equals(content.getId(), AudioPlayManager.getInstance().getCurrId()) && i != AudioPlayManager.getInstance().getPosition()) {
                            //同步音频播放的数据
                            AudioPlayManager.getInstance().onDataChanged(adapter.getData(), i);
                        }
                        content.setId(listAll.get(i).getId());
                        content.setAudioName(listAll.get(i).getAudioName());
                        content.setAudioImg(listAll.get(i).getAudioImg());
                        ColumnAudioBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryColumnDownloadFile(content.getId());
                        if (albumContent != null) {
                            //设置音视频存储的路径
                            content.setFilePath(albumContent.getFilePath());
                        }
                    }
                } else {
                    adapter.mNoMoreView.setText("加载失败");
                    recyclerView.showNoMore();

                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<ColumnAudioBean>>> call, Throwable t) {
                adapter.mNoMoreView.setText("加载失败");
                recyclerView.showNoMore();

            }
        });
    }

    private void initView(View view) {
        recyclerView = (RefreshRecyclerView) view.findViewById(R.id.recyclerView);
        llPlay = (AutoLinearLayout) view.findViewById(R.id.ll_play);
        imgOrientation = (ImageView) view.findViewById(R.id.img_orientation);
        tvOrientation = (TextView) view.findViewById(R.id.tv_orientation);
        llOrientation = (AutoLinearLayout) view.findViewById(R.id.ll_orientation);
        llDownload = (AutoLinearLayout) view.findViewById(R.id.ll_download);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_play:
                if (adapter != null && adapter.getData().size() > 0) {
                    if (TextUtils.equals("1", listAll.get(0).getHaveYouPurchased())) {
                        AudioPlayManager.getInstance().init(adapter.getData(), 0, AudioPlayManager.PlayModel.ORDER);
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isBuy = false;
                        DemoApplication.isPlay = true;
                        adapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < listAll.size(); i++) {
                            if (TextUtils.equals("1", listAll.get(i).getFree())) {
                                AudioPlayManager.getInstance().init(adapter.getData(), 0, AudioPlayManager.PlayModel.ORDER);
                                AudioPlayManager.getInstance().ijkStart();
                                DemoApplication.isBuy = false;
                                DemoApplication.isPlay = true;
                                adapter.notifyDataSetChanged();
                            } else {
                                ToastMaker.showShortToast("请先购买");
                            }
                        }
                    }
                } else {
                    ToastMaker.showShortToast("无播放内容");
                }
                break;
            case R.id.ll_download:
                if (!Util.hintLogin((BaseActivity) getActivity())) {
                    return;
                }
                for (ColumnAudioBean albumContent : adapter.getData()) {
                    IssuesDetailBean content = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(albumContent.getId());
                    if ((content == null || !Util.fileExisted(content.getFilePath())) && !TextUtils.isEmpty(albumContent.getAudioUrl()) && !"null".equals(albumContent.getAudioUrl())) {
                        ToastMaker.showShortToast("下载中...");
                        String suffixes = albumContent.getAudioUrl().substring(albumContent.getAudioUrl().lastIndexOf("."));
                        albumContent.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getAudioName() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(albumContent);
                        try {
                            DownloadManager.getInstance().startDownload(
                                    Util.getImgUrl(albumContent.getAudioUrl())
                                    , albumContent.getAudioName()
                                    , Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getAudioName() + suffixes
                                    , true
                                    , false
                                    , null
                                    , albumContent.getId(), 2, 0);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.DOWNLOAD_FINISH:
                adapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_COLUMN_FLASH:
                pages = 1;
                toHttpGetAudio();
                break;
            case SystemConstant.ON_STOP:
                adapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_PLAYING:
                adapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_PAUSE:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
