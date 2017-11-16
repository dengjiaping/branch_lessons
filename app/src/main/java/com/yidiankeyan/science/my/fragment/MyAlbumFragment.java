package com.yidiankeyan.science.my.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.my.adapter.EditorCoreAdapter;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.my.entity.EditorTypeBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 我的
 * -我的主页
 * -专辑
 */
public class MyAlbumFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinal lvEditor;
    private AutoRelativeLayout rlEditorPrompt;
    private List<EditorTypeBean> mDatas = new ArrayList<>();
    private EditorCoreAdapter coreAdapter;
    private TextView tvEditorContent,tvOfficial,tvWebUrl;

    private boolean textLoadFinish;
    private boolean audioLoadFinish;
    private boolean videoLoadFinish;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (textLoadFinish && audioLoadFinish && videoLoadFinish) {
                coreAdapter.notifyDataSetChanged();
                ptrLayout.onRefreshComplete();
                if (mDatas == null || mDatas.size() == 0) {
                    rlEditorPrompt.setVisibility(View.VISIBLE);
                    ptrLayout.setVisibility(View.GONE);
                }
            }
        }
    };


    public MyAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_album, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        ptrLayout.setVisibility(View.VISIBLE);
        rlEditorPrompt.setVisibility(View.GONE);

        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        coreAdapter = new EditorCoreAdapter(getContext(), mDatas);
        lvEditor.setAdapter(coreAdapter);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                textLoadFinish = false;
                audioLoadFinish = false;
                videoLoadFinish = false;
                mDatas.removeAll(mDatas);
                toHttpGetTextAlbum();
                toHttpGetAudioAlbum();
                toHttpGetVideoAlbum();
            }
        });
        if(!TextUtils.equals(getArguments().getString("id"), SpUtils.getStringSp(getActivity(), "userId"))){
            tvEditorContent.setText("真可惜，ta还没有上传专辑");
            tvOfficial.setVisibility(View.GONE);
            tvWebUrl.setVisibility(View.GONE);
        }else{
            tvEditorContent.setText("你还没有上传专辑");
            tvOfficial.setVisibility(View.VISIBLE);
            tvWebUrl.setVisibility(View.VISIBLE);
        }

    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
        lvEditor = (ListViewFinal) view.findViewById(R.id.lv_editor);
        rlEditorPrompt = (AutoRelativeLayout) view.findViewById(R.id.rl_editor_prompt);
        tvEditorContent= (TextView) view.findViewById(R.id.tv_editor_content);
        tvOfficial= (TextView) view.findViewById(R.id.tv_official);
        tvWebUrl= (TextView) view.findViewById(R.id.tv_web_url);
    }


    /**
     * 获取视频
     */
    private void toHttpGetVideoAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumtype", 3);
        entity.put("authorid",  getArguments().getString("id"));
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<EditorAlbum> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), EditorAlbum.class);
                    if (list.size() > 0) {
                        EditorTypeBean editorTypeBean = new EditorTypeBean(3, "视频专辑", list);
                        mDatas.add(editorTypeBean);
                    }
                }
                videoLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                videoLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 获取音频
     */
    private void toHttpGetAudioAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumtype", 2);
        entity.put("authorid",  getArguments().getString("id"));
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<EditorAlbum> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), EditorAlbum.class);
                    if (list.size() > 0) {
                        EditorTypeBean editorTypeBean = new EditorTypeBean(2, "音频专辑", list);
                        mDatas.add(editorTypeBean);
                    }
                }
                audioLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                audioLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 获取图文
     */
    private void toHttpGetTextAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumtype", 1);
        entity.put("authorid", getArguments().getString("id"));
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<EditorAlbum> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), EditorAlbum.class);
                    if (list.size() > 0) {
                        EditorTypeBean editorTypeBean = new EditorTypeBean(1, "图文专辑", list);
                        mDatas.add(editorTypeBean);
                    }
                }
                textLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                textLoadFinish = true;
                mHandler.sendEmptyMessage(0);
            }
        });
    }

}
