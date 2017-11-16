package com.yidiankeyan.science.information.acitivity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AboutWeAdapter;
import com.yidiankeyan.science.information.entity.AboutUs;
import com.yidiankeyan.science.information.entity.AboutWe;
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
 * 公告
 * -关于我们
 * -更多
 */
public class AboutWeActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout returnTitle;
    private ImageButton btnTitle;

    private AboutWeAdapter adapter;
    private ListView lvAbout;
    private List<AboutWe> mAboutList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (AboutWe aboutWe : mAboutList) {
                if (!aboutWe.isLoadFinish())
                    return;
            }
            lvAbout.setAdapter(adapter);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_about_we;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        returnTitle = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        lvAbout = (ListView) findViewById(R.id.lv_about);

    }

    @Override
    protected void initAction() {
        txtTitle.setText(getIntent().getStringExtra("title"));
        returnTitle.setOnClickListener(this);
        btnTitle.setVisibility(View.GONE);
        AboutWe bean1 = new AboutWe("新手指南", 0);
//        bean1.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");

        AboutWe bean2 = new AboutWe("墨子与你", 1);
//        bean2.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");
//        bean2.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");
//        bean2.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");

        AboutWe bean3 = new AboutWe("墨子活动", 2);
//        bean3.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");
//        bean3.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");
//        bean3.AboutChild(imgUrl, "一点科研", "VR遍地开花", "2016-07-31");


        mAboutList.add(bean1);
        mAboutList.add(bean2);
        mAboutList.add(bean3);
        adapter = new AboutWeAdapter(this, mAboutList);
        toHttp();
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 1);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", 1);
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ABOUT_US, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AboutUs> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AboutUs.class);
                    mAboutList.get(0).removeAll();
                    for (AboutUs aboutUs : list) {
                        mAboutList.get(0).createChild(aboutUs);
                    }
                    mAboutList.get(0).setLoadFinish(true);
                }
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                mHandler.sendEmptyMessage(0);
            }
        });

        Map<String, Object> map1 = new HashMap<>();
        map1.put("pages", 1);
        map1.put("pagesize", 3);
        Map<String, Object> entity1 = new HashMap<>();
        entity1.put("type", 2);
        map1.put("entity", entity1);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ABOUT_US, map1, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AboutUs> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AboutUs.class);
                    mAboutList.get(1).removeAll();
                    for (AboutUs aboutUs : list) {
                        mAboutList.get(1).createChild(aboutUs);
                    }
                    mAboutList.get(1).setLoadFinish(true);
                }
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                mHandler.sendEmptyMessage(0);
            }
        });

        Map<String, Object> map2 = new HashMap<>();
        map2.put("pages", 1);
        map2.put("pagesize", 3);
        Map<String, Object> entity2 = new HashMap<>();
        entity2.put("type", 3);
        map2.put("entity", entity2);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ABOUT_US, map2, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<AboutUs> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AboutUs.class);
                    mAboutList.get(2).removeAll();
                    for (AboutUs aboutUs : list) {
                        mAboutList.get(2).createChild(aboutUs);
                    }
                    mAboutList.get(2).setLoadFinish(true);
                }
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
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
