package com.yidiankeyan.science.knowledge.activity;

import android.view.KeyEvent;
import android.view.View;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.knowledge.adapter.NewsFlashCardAdapter;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.card.CardSlidePanel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class NewsFlashCardActivity extends BaseActivity {

    private CardSlidePanel slidePanel;
    private NewsFlashCardAdapter adapter;

    private List<FlashBean> listAll = new ArrayList<>();
    private int pages;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_news_flash_card;
    }

    @Override
    protected void initView() {
        slidePanel = (CardSlidePanel) findViewById(R.id.slide_panel);
    }

    @Override
    protected void initAction() {
        pages = getIntent().getIntExtra("pages", 1);
        listAll = getIntent().getParcelableArrayListExtra("data");
        int position = getIntent().getIntExtra("position", 0);
        if (listAll == null || listAll.size() == 0) {
            pages = 1;
            toHttpGetNews();
        } else {
            DB.getInstance(DemoApplication.applicationContext).updataNews(listAll.get(position).getId());
            listAll = listAll.subList(position, listAll.size());
        }
        findViewById(R.id.img_return).setOnClickListener(this);
        adapter = new NewsFlashCardAdapter(this, listAll);
        CardSlidePanel.CardSwitchListener cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {
                DB.getInstance(DemoApplication.applicationContext).updataNews(listAll.get(index).getId());
            }

            @Override
            public void onCardVanish(int index, int type) {
                if (index == listAll.size() - 1) {
                    toHttpGetNews();
                }
            }
        };
        slidePanel.setAdapter(adapter);
        slidePanel.setCardSwitchListener(cardSwitchListener);
        toHttpBehaviorAcquisition();
    }

    /**
     * 用户行为采集接口
     */
    private void toHttpBehaviorAcquisition() {
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.USER_BEHAVIOR_ACQUISITION_MESSAGE , null, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void toHttpGetNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getNews(map).enqueue(new RetrofitCallBack<List<FlashBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FlashBean>>> call, Response<RetrofitResult<List<FlashBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().size() > 0) {
                        listAll.addAll(response.body().getData());
                        pages++;
                        adapter.notifyDataSetChanged();
                    } else {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FlashBean>>> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
