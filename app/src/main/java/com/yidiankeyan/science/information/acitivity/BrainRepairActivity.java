package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.BraunRepairAdapter;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 * -脑补
 */
public class BrainRepairActivity extends BaseActivity {

    private TextView txtTitle;
    private ListView lvRepair;
    private AutoLinearLayout llReturn;
    private ImageButton btnTitle;
    //页面数据
    private List<RecentContentBean> listitem = new ArrayList<>();
    private BraunRepairAdapter adapter;

    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private AutoLinearLayout imgShareQQ;
    private ImageView imgShareSina;
    private PopupWindow mPopupWindow;
    private TextView btnCancel;
    private AutoLinearLayout llAll;

    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_brain_repair;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_brain);
        lvRepair = (ListView) findViewById(R.id.lv_repair);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        txtTitle.setText("脑补");
        btnTitle.setVisibility(View.GONE);

        RecentContentBean bean1 = new RecentContentBean();
        bean1.setTitle("拼智商");
        bean1.setAlbumName("51423位小伙伴在拼");
        bean1.setImgUrl(R.drawable.icon_naobu01);
        bean1.setReadCount("45128");
        bean1.setClickCount("35124");
        bean1.setTime("10分钟前");
        bean1.setContent("皮肤一被划破或擦伤");

//        RecentContentBean bean2 = new RecentContentBean();
//        bean2.setTitle("数独");
//        bean2.setAlbumName("43254位小伙伴在拼");
//        bean2.setImgUrl(R.drawable.icon_naobu02);
//        bean2.setReadCount("45128");
//        bean2.setClickCount("35124");
//        bean2.setTime("10分钟前");
//        bean2.setContent("皮肤一被划破或擦伤");

        listitem.add(bean1);
//        listitem.add(bean2);

        adapter = new BraunRepairAdapter(this, listitem);
        lvRepair.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvRepair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, RecommendWebActivity.class).putExtra("url", "http://101.200.82.170:8088/game-IQ/"));
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.NEW_FLASH_SHARE_CLICK:
                showSharePop((int) msg.getBody());
                break;
        }
    }

    private void showSharePop(final int position) {
        if (mPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(this);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(BrainRepairActivity.this, mPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(BrainRepairActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                        .withTitle("赛思快讯")
//                        .withText(listitem.get(position).getContent())
//                        .share();
                BrainRepairActivity.this.shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        "墨子游戏",
                        "拼智商",
                        null,
                        "http://101.200.82.170:8088/game-IQ/", null
                );
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(BrainRepairActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                        .withTitle("赛思快讯")
//                        .withText(listitem.get(position).getContent())
//                        .share();
                BrainRepairActivity.this.shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        "墨子游戏",
                        "拼智商",
                        null,
                        "http://101.200.82.170:8088/game-IQ/", null
                );
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(BrainRepairActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                        .withText(listitem.get(position).getContent())
//                        .withTitle("赛思测试分享")
//                        .share();
                BrainRepairActivity.this.shareWeb(
                        SHARE_MEDIA.QQ,
                        "墨子游戏",
                        "拼智商",
                        null,
                        "http://101.200.82.170:8088/game-IQ/", null
                );
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                new ShareAction(BrainRepairActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
////                        .withText(listitem.get(position).getContent())
////                        .withTitle("赛思快讯")
////                        .share();
//                BrainRepairActivity.this.shareWeb(
//                        SHARE_MEDIA.SINA,
//                        "墨子游戏",
//                        "拼智商",
//                        null,
//                        "http://101.200.82.170:8088/game-IQ/", null
//                );
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
