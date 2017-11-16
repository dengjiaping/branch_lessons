package com.yidiankeyan.science.information.acitivity;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.CardAdapter;
import com.yidiankeyan.science.information.entity.CardMode;
import com.yidiankeyan.science.information.flingswipe.SwipeFlingAdapterView;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识红包卡片
 */
public class RedCardActivity extends BaseActivity {

    private ArrayList<CardMode> al;
    private int position;
    private List<List<String>> list = new ArrayList<>();
    private CardAdapter adapter;
    private List<CardMode> cardList = new ArrayList<>();
    private SwipeFlingAdapterView flingContainer;


    private AutoLinearLayout llClose;
    private ImageView imgShare;
    private AutoRelativeLayout rlRedCard;
    private PopupWindow mSharePopupWindow;
    private AutoLinearLayout llShareWx;
    private AutoLinearLayout llShareFriendCircle;
    private AutoLinearLayout llShareQq;


    public final String[] imageUrls = new String[]{
            "http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949442_4562.jpg"};

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_red_card;
    }

    @Override
    protected void initView() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.item_card);
        imgShare= (ImageView) findViewById(R.id.img_share);
        llClose= (AutoLinearLayout) findViewById(R.id.ll_close_card);
        rlRedCard= (AutoRelativeLayout) findViewById(R.id.rl_red_card);
    }

    @Override
    protected void initAction() {
        initCardWindow();
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
//                Log.e("method", "removeFirstObjectInAdapter");
                al.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
//                makeToast(CardRedActivity.this, "不喜欢");
//                Log.e("method", "onLeftCardExit");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
//                Log.e("method", "onRightCardExit");
//                makeToast(CardRedActivity.this, "喜欢");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                    al.add(new CardMode("循环测试", 18, list.get(itemsInAdapter % imageUrls.length - 1)));
//                    adapter.notifyDataSetChanged();
//                    i++;
//                Log.e("method", "onAdapterAboutToEmpty");
                if (itemsInAdapter == 0) {
                    al.addAll(cardList);
                    finish();
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
//                Log.e("method", "onScroll");
                View view = flingContainer.getSelectedView();
//                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

//        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClicked(int itemPosition, Object dataObject) {
//                ToastMaker.showShortToast("点击了第"+itemPosition+"张卡片");
//            }
//        });
        imgShare.setOnClickListener(this);
        llClose.setOnClickListener(this);
    }

    private void initCardWindow() {
        //        Map<String, Object> map = new HashMap();
//        map.put("count", 20);
//        HttpUtil.post(context, SystemConstant.URL + SystemConstant.GET_RED_KNOWLEDGE, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (200 == result.getCode()) {
//                    List<CardMode> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), CardMode.class);
//                    al.addAll(datas);
//                    cardList.addAll(datas);
//                    cardAdapter = new CardAdapter(getContext(), al);
//                    cardAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//            }
//        });
        al = new ArrayList<>();

        for (int i = 0; i < imageUrls.length; i++) {
            List<String> s = new ArrayList<>();
            s.add(imageUrls[i]);
            list.add(s);
        }
        al.add(new CardMode("胡欣语", 21, list.get(0)));
        al.add(new CardMode("Norway", 21, list.get(1)));
        al.add(new CardMode("王清玉", 18, list.get(2)));
        al.add(new CardMode("测试1", 21, list.get(3)));
        cardList.add(new CardMode("胡欣语", 21, list.get(0)));
        cardList.add(new CardMode("Norway", 21, list.get(1)));
        cardList.add(new CardMode("王清玉", 18, list.get(2)));
        cardList.add(new CardMode("测试1", 21, list.get(3)));
        adapter = new CardAdapter(this, al);
        flingContainer.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.KNOWLEDGE_CARD_GOVE:
                View view = flingContainer.getChildAt(flingContainer.getChildCount()-1);
                if (view.getTag() instanceof CardAdapter.ViewHolder){
                    CardAdapter.ViewHolder holder = (CardAdapter.ViewHolder) view.getTag();
                    holder.imgCardRight.setVisibility(View.GONE);
                }
                break;
            case SystemConstant.KNOWLEDGE_CARD_VISIBLE:
                View upView = flingContainer.getChildAt(flingContainer.getChildCount()-1);
                if (upView.getTag() instanceof CardAdapter.ViewHolder){
                    CardAdapter.ViewHolder holder = (CardAdapter.ViewHolder) upView.getTag();
                    holder.imgCardRight.setVisibility(View.VISIBLE);
                }
                break;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_close_card:
                finish();
//                Intent intent = new Intent();
//                intent.setAction("action.refreshFriend");
//                sendBroadcast(intent);
                break;
            case R.id.img_share:
                showSharePop();
                break;
        }
    }

    private void showSharePop() {

        if (mSharePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_news_flash_share, null);
            llShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llShareQq = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            mSharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mSharePopupWindow.setContentView(view);
            mSharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mSharePopupWindow.setFocusable(true);
            mSharePopupWindow.setOutsideTouchable(true);
            mSharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mSharePopupWindow.showAtLocation(rlRedCard, Gravity.BOTTOM, 0, 0);
            mSharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(RedCardActivity.this, mSharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(RedCardActivity.this, mSharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mSharePopupWindow.showAtLocation(rlRedCard, Gravity.BOTTOM, 0, 0);
        }
//        llShareWx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               shareWeb(
//                        SHARE_MEDIA.WEIXIN,
//                        "知识红包",
//                        cardList.get(0).getName(),
//                        null,
////                        SystemConstant.MYURL + "infobag/share/" + cardList.get(0).getId(),
//                       null,
//                        null
//                );
//            }
//        });
//
//        llShareFriendCircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((BaseActivity) getActivity()).shareWeb(
//                        SHARE_MEDIA.WEIXIN_CIRCLE,
//                        cardList.get(position).getName(),
//                        "知识红包",
//                        null,
//                        SystemConstant.MYURL + "flashnews/view/" + cardList.get(position).getId(),
//                        null
//                );
//            }
//        });
//        llShareQq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((BaseActivity) getActivity()).shareWeb(
//                        SHARE_MEDIA.QQ,
//                        "",
//                        cardList.get(position).getName(),
//                        null,
//                        SystemConstant.MYURL + "flashnews/view/" + cardList.get(position).getId(),
//                        null
//                );
//            }
//        });

//                llShareQq.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                        String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                        new ShareAction((TodayAchievementActivity) mContext).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                                .withTitle("赛思测试分享")
//                                .withMedia(image)
//                                .withTargetUrl(url)
//                                .share();
//                    }
//
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventMsg msg = EventMsg.obtain(SystemConstant.KNOWLEDGE_CARD_CLOSE);
        EventBus.getDefault().post(msg);
    }
}
