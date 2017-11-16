package com.yidiankeyan.science.subscribe.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.adapter.CheckAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.activity.MyDownloadActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 批量下载
 */
public class BatchdownloadActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private AutoLinearLayout llReturn;
    private ListView lvCheck;
    private CheckAdapter checkAdapter;
    private boolean isFu;
    private ImageView imgOrder;
    private List<AlbumContent> mDatas = new ArrayList<>();

    /**
     * 立即下载
     */
    private ImageButton btn_immediately;
    /**
     * 全选
     */
    private CheckBox btn_click;
    private AutoLinearLayout llQuanxuan;
    private AutoLinearLayout llIntoDownloadList;
    private TextView tvContentNum;

    @Override
    protected int setContentView() {
        return R.layout.activity_batchdownload;
    }

    /**
     * 获取专辑内容
     */
    private void toHttpGetContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 200);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumid", getIntent().getStringExtra("albumId"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_CONTENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AlbumContent> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AlbumContent.class);
                    Iterator<AlbumContent> iter = data.iterator();
                    while (iter.hasNext()) {
                        AlbumContent albumContent = iter.next();
                        AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(albumContent.getArticleid());
                        if (content != null && Util.fileExisted(content.getFilePath())) {
                            iter.remove();
                        } else {
                            albumContent.setAlbumId(getIntent().getStringExtra("albumId"));
                            albumContent.setAlbumName(getIntent().getStringExtra("albumName"));
                            albumContent.setAuthorname(getIntent().getStringExtra("authorName"));
                            albumContent.setAlbumAvatar(getIntent().getStringExtra("albumAvatar"));
                            albumContent.setContentNum(getIntent().getIntExtra("contentNum", 0));
                        }
                    }
                    mDatas.addAll(data);
                    checkAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return_batch);
        lvCheck = (ListView) findViewById(R.id.download_list);
        imgOrder = (ImageView) findViewById(R.id.img_order);
        btn_click = (CheckBox) findViewById(R.id.btn_click);
        btn_immediately = (ImageButton) findViewById(R.id.btn_immediately);
        llQuanxuan = (AutoLinearLayout) findViewById(R.id.ll_quanxuan);
        llIntoDownloadList = (AutoLinearLayout) findViewById(R.id.ll_into_download_list);
        imgOrder.setBackgroundResource(R.drawable.orderupper);
        tvContentNum = (TextView) findViewById(R.id.tv_content_num);
        tvContentNum.setText("共" + getIntent().getIntExtra("contentNum", 0) + "集");

        //排序
        imgOrder.setOnClickListener(this);
        /** 全选*/
        lvCheck.setOnItemClickListener(this);
        btn_immediately.setOnClickListener(this);
        llReturn.setOnClickListener(this);
        llQuanxuan.setOnClickListener(this);
        btn_click.setOnClickListener(this);
        llIntoDownloadList.setOnClickListener(this);
//        btn_click.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                for (AlbumContent content : mDatas) {
//                    content.setChecked(isChecked);
//                }
//                checkAdapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    protected void initAction() {
        checkAdapter = new CheckAdapter(this, mDatas);
        lvCheck.setAdapter(checkAdapter);
        toHttpGetContent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return_batch:
                finish();
                break;
            case R.id.img_order:
                if (isFu) {
                    isFu = false;
                    Collections.reverse(mDatas);
                    imgOrder.setBackgroundResource(R.drawable.orderupper);
                    checkAdapter.notifyDataSetChanged();
                } else {
                    isFu = true;
                    imgOrder.setBackgroundResource(R.drawable.orderlower);
                    Collections.reverse(mDatas);
                    checkAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_immediately:
                try {
                    boolean flag = true;
                    for (AlbumContent albumContent : mDatas) {
                        if (albumContent.isChecked() && !TextUtils.isEmpty(albumContent.getMediaurl()) && !"null".equals(albumContent.getMediaurl())) {
                            if (flag) {
                                Toast.makeText(BatchdownloadActivity.this, "下载中...", Toast.LENGTH_SHORT).show();
                                flag = false;
                            }
                            String suffixes = albumContent.getMediaurl().substring(albumContent.getMediaurl().lastIndexOf("."));
                            albumContent.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getArticlename() + suffixes);
                            DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(albumContent);
                            DownloadManager.getInstance().startDownload(
                                    SystemConstant.ACCESS_IMG_HOST + albumContent.getMediaurl()
                                    , albumContent.getArticlename()
                                    , Util.getSDCardPath() + "/MOZDownloads/" + albumContent.getArticlename() + suffixes, true, false, null, getIntent().getIntExtra("contentNum", 0), albumContent.getArticleid());
                        }
                    }
                    if (flag)
                        Toast.makeText(BatchdownloadActivity.this, "无有效链接，下载失败", Toast.LENGTH_SHORT).show();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_quanxuan:
                btn_click.toggle();
                for (AlbumContent content : mDatas) {
                    content.setChecked(btn_click.isChecked());
                }
                checkAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_click:
                for (AlbumContent content : mDatas) {
                    content.setChecked(btn_click.isChecked());
                }
                checkAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_into_download_list:
                Log.e("Size", "====" + DownloadManager.getInstance().getDownloadListCount());
                Intent intent = new Intent(this, MyDownloadActivity.class);
                intent.putExtra("isDownloading", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckAdapter.ViewHolder holder = (CheckAdapter.ViewHolder) view.getTag();
        holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
        if (!holder.cb.isChecked() && btn_click.isChecked()) {
            btn_click.setChecked(false);
        }
        boolean flag = true;
        for (AlbumContent albumContent : mDatas) {
            if (!albumContent.isChecked()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            btn_click.setChecked(true);
        }
    }
}
