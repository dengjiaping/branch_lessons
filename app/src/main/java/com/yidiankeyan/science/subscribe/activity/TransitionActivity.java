package com.yidiankeyan.science.subscribe.activity;

import android.content.Intent;
import android.view.View;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 专辑缓冲页
 */
public class TransitionActivity extends BaseActivity {

    private AlbumDetail albumDetail;

    @Override
    protected int setContentView() {
        return R.layout.activity_transition;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initAction() {
        toHttp();
    }

    /**
     * 获取专辑详情
     */
    private void toHttp() {

        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    albumDetail = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                    if (albumDetail.getPermission() == 0) {
                        Intent intent = new Intent(TransitionActivity.this, AlbumChargeActivity.class);
                        intent.putExtra("albumDetail", albumDetail);
                        intent.putExtra("albumAvatar", getIntent().getStringExtra("albumAvatar"));
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        Intent intent = new Intent(TransitionActivity.this, AlbumDetailsActivity.class);
                        intent.putExtra("albumId", getIntent().getStringExtra("albumId"));
                        intent.putExtra("albumName", getIntent().getStringExtra("albumName"));
                        intent.putExtra("albumAvatar", getIntent().getStringExtra("albumAvatar"));
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
