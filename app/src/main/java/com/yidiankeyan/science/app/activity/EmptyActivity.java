package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.subscribe.entity.ContentDetailBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EmptyActivity extends BaseActivity {

    private TextView tvRetry;

    @Override
    protected int setContentView() {
        return R.layout.activity_empty;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText("");
        findViewById(R.id.ll_return).setOnClickListener(this);
        tvRetry = (TextView) findViewById(R.id.tv_retry);
        tvRetry.setOnClickListener(this);
        toHttp();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_CONTENT_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    ContentDetailBean contentDetail = (ContentDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), ContentDetailBean.class);
                    Intent intent;
                    switch (contentDetail.getType()) {
                        case 1:
                            intent = new Intent(EmptyActivity.this, ImgTxtAlbumActivity.class);
                            intent.putExtra("id", contentDetail.getId());
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            break;
                        case 2:
                            AlbumContent audio = new AlbumContent(null);
                            audio.setArticlename(contentDetail.getName());
                            audio.setArticleid(contentDetail.getId());
                            audio.setLastupdatetime(contentDetail.getCreatetime());
                            audio.setArticletype(2);
                            audio.setMediaurl(contentDetail.getMediaurl());
                            audio.setCoverimgurl(contentDetail.getCoverimgurl());
                            AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(contentDetail.getId());
                            if (content != null)
                                audio.setFilePath(content.getFilePath());
                            ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                            listItem.add(audio);
                            intent = new Intent(EmptyActivity.this, AudioAlbumActivity.class);
                            intent.putParcelableArrayListExtra("list", listItem);
                            intent.putExtra("position", 0);
                            intent.putExtra("id", listItem.get(0).getArticleid());
                            intent.putExtra("single", true);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            break;
                        case 3:
                            AlbumContent video = new AlbumContent(null);
                            video.setArticlename(contentDetail.getName());
                            video.setArticleid(contentDetail.getId());
                            video.setLastupdatetime(contentDetail.getCreatetime());
                            video.setArticletype(2);
                            video.setMediaurl(contentDetail.getMediaurl());
                            video.setCoverimgurl(contentDetail.getCoverimgurl());
                            AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(contentDetail.getId());
                            if (contentVideo != null)
                                video.setFilePath(contentVideo.getFilePath());
                            ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                            listItemVideo.add(video);
                            intent = new Intent(EmptyActivity.this, VideoAlbumActivity.class);
                            intent.putParcelableArrayListExtra("list", listItemVideo);
                            intent.putExtra("id", listItemVideo.get(0).getArticleid());
                            intent.putExtra("position", 0);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            break;
                    }
                } else {
                    tvRetry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                tvRetry.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_retry:
                tvRetry.setVisibility(View.GONE);
                toHttp();
                break;
        }
    }
}
