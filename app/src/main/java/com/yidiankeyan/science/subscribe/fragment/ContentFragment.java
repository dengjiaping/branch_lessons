package com.yidiankeyan.science.subscribe.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.subscribe.adapter.LvConAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;

/**
 * 订阅-专辑详情-内容
 */
public class ContentFragment extends Fragment implements View.OnClickListener {

    private LvConAdapter adapter;
    //排序
    private ImageButton btnOrder;
    private ListViewFinalLoadMore lvItem;
    //正序
    private ArrayList<AlbumContent> listItem = new ArrayList<>();
    private boolean isFu;
    private TextView EpisodesSum;
    private int sum;
    private String authorName;
    private AlbumDetail album;
    private String isFocus = "0";
    private static final int INTO_WEB_ACTIVITY = 101;
    private int pages = 1;

    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        //初始化
        initView(view);
        lvItem.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetContent();
            }
        });
        return view;
    }

    private void initView(View view) {
        lvItem = (ListViewFinalLoadMore) view.findViewById(R.id.lv_item);
        btnOrder = (ImageButton) view.findViewById(R.id.img_orders);
        EpisodesSum = (TextView) view.findViewById(R.id.episodes_sum);
        btnOrder.setBackgroundResource(R.drawable.orderupper);
        btnOrder.setOnClickListener(this);
        //填充数据
//        lvItem.setHasLoadMore(true);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listItem == null || listItem.size() == 0 || album == null)
                    return;
                Intent intent;
                if (listItem.get(position).getArticletype() == 1) {
                    intent = new Intent(getActivity(), ImgTxtAlbumActivity.class);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    intent.putExtra("bean", listItem.get(position));
                    intent.putExtra("isFocus", isFocus);
                    startActivityForResult(intent, INTO_WEB_ACTIVITY);
                } else if (listItem.get(position).getArticletype() == 2) {
                    intent = new Intent(getActivity(), AudioAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItem);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else if (listItem.get(position).getArticletype() == 3) {
                    intent = new Intent(getActivity(), VideoAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItem);
                    intent.putExtra("id", listItem.get(position).getArticleid());
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
        adapter = new LvConAdapter(listItem, getActivity());
        lvItem.setAdapter(adapter);
    }

    /**
     * 获取专辑内容
     */
    private void toHttpGetContent() {
        if (album == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumid", album.getAlbumid());
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALBUM_CONTENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AlbumContent> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AlbumContent.class);
                    if (data.size() > 0) {
                        lvItem.setHasLoadMore(true);
                        //这么做是为了在adapter下载时填入专辑信息
                        for (AlbumContent content : data) {
                            content.setAlbumId(album.getAlbumid());
                            content.setAlbumName(album.getAlbumname());
                            content.setAlbumAvatar(album.getImgurl());
                            content.setContentNum(album.getContentnum());
                            content.setAuthorname(authorName);
                            AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(content.getArticleid());
                            if (albumContent != null) {
                                //设置音视频存储的路径
                                content.setFilePath(albumContent.getFilePath());
                            }
                        }
                        listItem.addAll(data);
                        pages++;
                        if (data.size() < 20) {
                            lvItem.setHasLoadMore(false);
                        }
                    } else {
                        lvItem.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvItem.showFailUI();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.DOWNLOAD_FINISH:
                //文件下载完成
                String id = (String) msg.getBody();
                for (AlbumContent content : listItem) {
                    if (id.equals(content.getArticleid())) {
                        //设置下载完成的文件的路径
                        content.setFilePath(DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(id).getFilePath());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_FOCUS_CHANGED:
                isFocus = (String) msg.getBody();
                break;
            case SystemConstant.ON_STOP:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == INTO_WEB_ACTIVITY) {
            isFocus = data.getStringExtra("isFocus");
            EventMsg msg = EventMsg.obtain(SystemConstant.DETAUKS_FRAGMENT_RECEIVER_FOCUS);
            msg.setBody(isFocus);
            EventBus.getDefault().post(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
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
    }

    public void setAlbum(AlbumDetail album) {
        this.album = album;
        isFocus = album.getIsfocus() + "";
        toHttpGetContent();
        EpisodesSum.setText("共" + album.getContentnum() + "篇");
    }
}
