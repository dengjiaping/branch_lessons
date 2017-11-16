package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MyPrizeListAdapter;
import com.yidiankeyan.science.information.entity.MyPrizeBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;

public class MyPrizeActivity extends BaseActivity {

    private int pages = 1;
    //    private PtrClassicFrameLayout ptrLayout;
    private List<MyPrizeBean> myPrize = new ArrayList<>();
    private MyPrizeListAdapter adapter;
    private AutoLinearLayout llReturn;
    private ListViewFinalLoadMore lvPrize;
    private ImageButton titleBtn;
    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private AutoLinearLayout imgShareSina;
    private AutoLinearLayout imgShareQQ;
    private TextView btnCancel;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llAll;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_prize;
    }

    @Override
    protected void initView() {
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
//        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvPrize = (ListViewFinalLoadMore) findViewById(R.id.lv_prize);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
    }

    @Override
    protected void initAction() {
        titleBtn.setOnClickListener(this);
        adapter = new MyPrizeListAdapter(this, myPrize);
//        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                pages = 1;
//                toHttpGetPrize();
//            }
//        });
        llReturn.setOnClickListener(this);
        lvPrize.setHasLoadMore(true);
//        ptrLayout.autoRefresh();
//        ptrLayout.disableWhenHorizontalMove(true);
//        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvPrize.setAdapter(adapter);
        pages = 1;
        toHttpGetPrize();
        lvPrize.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetPrize();
            }
        });
    }

    private void toHttpGetPrize() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_MY_PRIZZE_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<MyPrizeBean> mData = GsonUtils.json2List(jsonData, MyPrizeBean.class);
                    if (pages == 1)
                        myPrize.removeAll(myPrize);
                    if (mData.size() > 0) {
                        lvPrize.setHasLoadMore(true);
                        myPrize.addAll(mData);
                        pages++;
                    } else {
                        lvPrize.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvPrize.showFailUI();
                }
//                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrLayout.onRefreshComplete();
                lvPrize.showFailUI();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 111:
                    pages = 1;
                    toHttpGetPrize();
                    break;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.title_btn:
                showSharePop();
                break;
        }
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(this);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MyPrizeActivity.this, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        "快来试试抽奖",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        "快来试试抽奖",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWeb(
                        SHARE_MEDIA.QQ,
                        "墨子杯密码数学挑战赛，幸运抽奖",
                        "快来试试抽奖",
                        null,
                        SystemConstant.MYURL + "view/appshare/index.jsp"
                        , null);
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareWeb(
//                        SHARE_MEDIA.SINA,
//                        "墨子杯密码数学挑战赛，幸运抽奖",
//                        "快来试试抽奖",
//                        null,
//                        SystemConstant.MYURL + "view/appshare/index.jsp"
//                        , null);
//            }
//        });
    }
}
