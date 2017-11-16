package com.yidiankeyan.science.my.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.my.adapter.EditorCoreAdapter;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.my.entity.EditorTypeBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

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
 * 主页
 * -他的专辑
 */
public class HisAlbumActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinal lvHisAlbum;
    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private List<EditorTypeBean> mDatas = new ArrayList<>();
    private EditorCoreAdapter coreAdapter;


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
                if (mDatas == null || mDatas.size() == 0)
                    if(!TextUtils.equals(getIntent().getStringExtra("id"), SpUtils.getStringSp(HisAlbumActivity.this, "userId"))){
                        Toast.makeText(mContext, "他/她还没有上传过专辑哦", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "你还没有上传过专辑哦", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_his_album;
    }

    @Override
    protected void initView() {
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
        lvHisAlbum = (ListViewFinal) findViewById(R.id.lv_his_album);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("专辑列表");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);

        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        coreAdapter = new EditorCoreAdapter(this, mDatas);
        lvHisAlbum.setAdapter(coreAdapter);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                textLoadFinish = false;
                audioLoadFinish = false;
                videoLoadFinish = false;
                toHttpGetTextAlbum();
                toHttpGetAudioAlbum();
                toHttpGetVideoAlbum();
            }
        });
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
        entity.put("authorid", SpUtils.getStringSp(this, "userId"));
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
        entity.put("authorid", SpUtils.getStringSp(this, "userId"));
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
        entity.put("authorid", SpUtils.getStringSp(this, "userId"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
