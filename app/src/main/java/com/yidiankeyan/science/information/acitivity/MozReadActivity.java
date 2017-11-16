package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MozReadItemAdapter;
import com.yidiankeyan.science.information.entity.RankingAlbumBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐
 * -墨子读书
 */
public class MozReadActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;

    private ListView lvMozRead;
    private List<RankingAlbumBean> listAlb;
    private MozReadItemAdapter mozReadItemAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_moz_read;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvMozRead = (ListView) findViewById(R.id.lv_moz_read);
        listAlb = new ArrayList<>();
        mozReadItemAdapter = new MozReadItemAdapter(listAlb, this);
        lvMozRead.setAdapter(mozReadItemAdapter);
        toHttpGetAlbum();
    }

    /**
     * 获取专辑排行榜
     */
    private void toHttpGetAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getIntent().getIntExtra("type", 0));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_RANKING_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<RankingAlbumBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), RankingAlbumBean.class);
                    listAlb.addAll(data);
                    mozReadItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void initAction() {
        txtTitle.setText("墨子读书");
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
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
