package com.yidiankeyan.science.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.adapter.DownloadFileContentAdapter;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点藏
 * -下载后免费专辑信息
 */
public class DownloadFileContentActivity extends BaseActivity {

    private DownloadFileContentAdapter adapter;
    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    //排序
    private ImageButton btnOrder;
    private ListView lvItem;
    private ArrayList<AlbumContent> listItem = new ArrayList<>();
    private boolean isFu;
    private String albumId;
    private String albumName;
    private TextView tvPartCount;
    private AlbumDetail albumDetail;

    @Override
    protected int setContentView() {
        return R.layout.activity_download_file_content;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        lvItem = (ListView) findViewById(R.id.lv_item);
        btnOrder = (ImageButton) findViewById(R.id.img_orders);
        tvPartCount = (TextView) findViewById(R.id.tv_part_count);
    }

    @Override
    protected void initAction() {
        albumId = getIntent().getStringExtra("albumId");
        albumName = getIntent().getStringExtra("albumName");
        txtTitle.setText(albumName);
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        toHttpGetAlbumDetail();
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals("日课", listItem.get(position).getAlbumName())) {
                    return;
                } else if (listItem == null || listItem.size() == 0)
                    return;
                Intent intent;
                if (listItem.get(position).getArticletype() == 1) {
                    intent = new Intent(mContext, ImgTxtAlbumActivity.class);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    startActivity(intent);
                } else if (listItem.get(position).getArticletype() == 2) {
                    intent = new Intent(mContext, AudioAlbumActivity.class);
                    ArrayList<AlbumContent> mData = new ArrayList<AlbumContent>();
                    mData.add(listItem.get(position));
                    intent.putParcelableArrayListExtra("list", mData);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    intent.putExtra("position", position);
                    intent.putExtra("single", true);
                    if (listItem.get(position).getAlreadyWatch() == 0) {
                        listItem.get(position).setAlreadyWatch(1);
                        DB.getInstance(DemoApplication.applicationContext).updateDownloadFileAlready(listItem.get(position).getArticleid(), 1);
                    }
                    startActivity(intent);
                } else if (listItem.get(position).getArticletype() == 3) {
                    intent = new Intent(mContext, VideoAlbumActivity.class);
                    ArrayList<AlbumContent> mData = new ArrayList<AlbumContent>();
                    mData.add(listItem.get(position));
                    intent.putParcelableArrayListExtra("list", mData);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    intent.putExtra("position", position);
                    if (listItem.get(position).getAlreadyWatch() == 0) {
                        listItem.get(position).setAlreadyWatch(1);
                        DB.getInstance(DemoApplication.applicationContext).updateDownloadFileAlready(listItem.get(position).getArticleid(), 1);
                    }
                    startActivity(intent);
                }
            }
        });
        adapter = new DownloadFileContentAdapter(listItem, mContext);
        lvItem.setAdapter(adapter);
        btnOrder.setBackgroundResource(R.drawable.orderupper);
        btnOrder.setOnClickListener(this);
        ((TextView) findViewById(R.id.maintitle_txt)).setText(albumName);
        initContent();
    }

    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    albumDetail = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    private void initContent() {
        List<AlbumContent> list = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
        for (AlbumContent content : list) {
            if (TextUtils.equals(content.getAlbumId(), albumId)) {
                listItem.add(content);
            }
        }
        tvPartCount.setText("共" + listItem.size() + "集");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.img_orders:
                if (isFu) {
                    isFu = false;
                    Collections.reverse(listItem);
                    btnOrder.setBackgroundResource(R.drawable.orderupper);
                    adapter.notifyDataSetChanged();
                } else {
                    isFu = true;
                    Collections.reverse(listItem);
                    btnOrder.setBackgroundResource(R.drawable.orderlower);
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
