package com.yidiankeyan.science.subscribe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.InfomationVPAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.QueryAlbumBean;
import com.yidiankeyan.science.subscribe.fragment.ContentFragment;
import com.yidiankeyan.science.subscribe.fragment.DetauksFragment;
import com.yidiankeyan.science.subscribe.service.TimerService;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * -专辑详情
 */
public class AlbumDetailsActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;

    //    private TextView tvDetails;
//    private TextView tvContent;
    private ImageView imgGratuity;

    private ViewPager viewPager;
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private boolean isCharge;
    private Intent intent;
    private ImageView imgAlbums;
    private TextView txtAlbumname;
    private TextView txtEditor;
    private TextView txtRead;
    private TextView txtForm;
    private TextView txtAlbum;
    private TextView txtSubscribe;
    private ContentFragment contentFragment;
    private DetauksFragment detauksFragment;
    private String albumId;
    private Button imgBatchDown;

    //订阅
    private Button imgSub;
    private boolean isFu;
    private PopupWindow mPopupWindow;
    private TextView tvFinish, tvYesClick;
    private AutoLinearLayout llMdetails;
    private AlbumDetail albumDetail;
    private int isBroadcast = 0;

    private InfomationVPAdapter adapter;


    private TabLayout tab_FindFragment_title; //定义TabLayout
    private List<String> list_title; //tab名称列表

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_album_details;
    }

    @Override
    protected void initView() {
        tab_FindFragment_title = (TabLayout) findViewById(R.id.tab_FindFragment_title);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
//        tvDetails = (TextView) findViewById(R.id.tab_details);
//        tvContent = (TextView) findViewById(R.id.tab_content);
        viewPager = (ViewPager) findViewById(R.id.vp_albbuttom);
        txtTitle.setText("专辑详情");
        imgGratuity = (ImageView) findViewById(R.id.img_gratuity);
        llMdetails = (AutoLinearLayout) findViewById(R.id.ll_mdetails);
        imgBatchDown = (Button) findViewById(R.id.img_batchdown);
//        imgNull = (ImageView) findViewById(R.id.img_null);

        imgSub = (Button) findViewById(R.id.img_ablum_sub);
        imgAlbums = (ImageView) findViewById(R.id.img_albums);
        txtAlbumname = (TextView) findViewById(R.id.txt_albumname);
        txtEditor = (TextView) findViewById(R.id.txt_editor);
        txtRead = (TextView) findViewById(R.id.txt_read);
        txtForm = (TextView) findViewById(R.id.txt_form);
        txtAlbum = (TextView) findViewById(R.id.txt_album);
        txtSubscribe = (TextView) findViewById(R.id.txt_subscribe);

        //测试反射
//        Class<?> clz = AlbumDetailAuthorAlbum.class;
//        try {
//            Constructor<?> constructor = clz.getConstructor(String.class);
//            constructor.setAccessible(true);
//            Object obj = constructor.newInstance("测试反射");
//            Log.e("测试反射", "===="+obj.toString());
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void initAction() {
        isCharge = getIntent().getBooleanExtra("isCharge", false);
        albumId = getIntent().getStringExtra("albumId");

        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        contentFragment = new ContentFragment();
//        if (!TextUtils.isEmpty(albumId)) {
//            Bundle contentBundle = new Bundle();
//            contentBundle.putString("albumId", albumId);
//            contentBundle.putString("albumName", getIntent().getStringExtra("albumName"));
//            contentBundle.putString("albumAvatar", getIntent().getStringExtra("albumAvatar"));
//            contentFragment.setArguments(contentBundle);
//        }
        detauksFragment = new DetauksFragment();
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
        fragments = new ArrayList<>();
        fragments.add(detauksFragment);//详情
        fragments.add(contentFragment);//内容
        list_title = new ArrayList<>();
        list_title.add("详情");
        list_title.add("内容");
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
        adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, list_title);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        tab_FindFragment_title.setupWithViewPager(viewPager);
        llReturn.setOnClickListener(this);
//        tvDetails.setOnClickListener(this);
//        tvContent.setOnClickListener(this);
        imgGratuity.setOnClickListener(this);
        imgSub.setOnClickListener(this);
//        tvContent.setTextColor(Color.parseColor("#F1312E"));

        findViewById(R.id.img_batchdown).setOnClickListener(this);
        if (isCharge) {
            imgGratuity.setImageResource(R.drawable.subalbum);
        }
        imgBatchDown.setEnabled(false);
        if (!TextUtils.isEmpty(albumId)) {
            showLoadingDialog("请稍候");
            toHttpGetAlbumDetail();
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        albumId = getIntent().getStringExtra("albumId");
//    }

    /**
     * 获取专辑详情
     */
    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    albumDetail = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                    contentFragment.setAlbum(albumDetail);
                    initData();
                    loadingDismiss();
                } else {
                    imgBatchDown.setEnabled(false);
                    imgBatchDown.setTextColor(Color.parseColor("#999999"));
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                imgBatchDown.setEnabled(false);
                imgBatchDown.setTextColor(Color.parseColor("#999999"));
            }
        });
    }

    private void initData() {
        Glide.with(this).load(Util.getImgUrl(albumDetail.getImgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(imgAlbums);
        if (!TextUtils.isEmpty(albumDetail.getAlbumname()) && !TextUtils.equals("null", albumDetail.getAlbumname())) {
            txtAlbumname.setText(albumDetail.getAlbumname());
        } else {
            txtAlbumname.setText("");
        }
        if (albumDetail.getIsorder() == 1) {
            isFu = true;
            imgSub.setBackgroundResource(R.drawable.shape_subed);
            imgSub.setText("已订阅");
            imgSub.setTextColor(getResources().getColor(R.color.white));
        } else {
            imgSub.setBackgroundResource(R.drawable.shape_sub);
            imgSub.setText("订阅专辑");
            imgSub.setTextColor(getResources().getColor(R.color.main_orange));
            isFu = false;
        }

        if (!TextUtils.isEmpty(albumDetail.getAuthorname()) && !TextUtils.equals("null", albumDetail.getAuthorname())) {
            txtEditor.setText("主编：" + albumDetail.getAuthorname());
        } else {
            txtEditor.setText("主编");
        }
        toHttpPostQueryAlbum();
        switch (albumDetail.getType()) {
            case 1:
                //图文
                txtForm.setText("专辑形式:图文");
//                imgBatchDown.setVisibility(View.GONE);
                imgBatchDown.setEnabled(false);
                imgBatchDown.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                //音频
                txtForm.setText("专辑形式:音频");
                imgBatchDown.setEnabled(true);
//                imgNull.setVisibility(View.GONE);
                break;
            case 3:
                //视频
                txtForm.setText("专辑形式:视频");
                imgBatchDown.setEnabled(true);
//                imgNull.setVisibility(View.GONE);
                break;
        }

        if (!TextUtils.isEmpty(albumDetail.getSubjectname()) && !TextUtils.equals("null", albumDetail.getSubjectname())) {
            txtAlbum.setText("所属专题：" + albumDetail.getSubjectname());
        } else {
            if (albumDetail.getBelongid() == 1004)
                txtAlbum.setText("所属专题：墨子FM");
            else
                txtAlbum.setText("所属专题");
        }


        detauksFragment.loadInfo(albumDetail);
    }

    private void toHttpPostQueryAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", getIntent().getStringExtra("albumId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_SUBNUMBER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    QueryAlbumBean queryAlbumBean = (QueryAlbumBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), QueryAlbumBean.class);
                    if (!TextUtils.isEmpty(queryAlbumBean.getOrdernum() + "") && !TextUtils.equals("null", queryAlbumBean.getOrdernum() + "")) {
                        txtSubscribe.setText("订阅人数：" + queryAlbumBean.getOrdernum());
                    } else {
                        txtSubscribe.setText("订阅人数");
                    }
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
            case SystemConstant.ON_ORDER_ALBUM:
                isFu = true;
                imgSub.setBackgroundResource(R.drawable.shape_subed);
                imgSub.setText("已订阅");
                imgSub.setTextColor(getResources().getColor(R.color.white));
                albumDetail.setIsorder(1);
                break;
            case SystemConstant.ON_ABOLISH_ORDER_ALBUM:
                imgSub.setBackgroundResource(R.drawable.shape_sub);
                imgSub.setText("订阅专辑");
                imgSub.setTextColor(getResources().getColor(R.color.main_orange));
                isFu = false;
                albumDetail.setIsorder(0);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (DemoApplication.getInstance().activityExisted(TransitionActivity.class)) {
            DemoApplication.getInstance().finishActivity(TransitionActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, TimerService.class));
        EventBus.getDefault().unregister(this);
        if (isBroadcast == 1 || isBroadcast == 2) {
            Intent intent = new Intent();
            intent.setAction("action.refreshFriend");
            sendBroadcast(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                    //如果mainActivity不存在则跳转主页面
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
//            case R.id.tab_details:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
//            case R.id.tab_content:
//                currIndex = 1;
//                viewPager.setCurrentItem(currIndex);
//                break;
            case R.id.img_ablum_sub:
                if (!Util.hintLogin(this))
                    return;
                //订阅专辑
                if (isFu) {
                    showCustomPop();
                } else {
                    toHttpSubAlbum();
                }
                isBroadcast = 1;
                break;
            case R.id.tv_no_finish:
                finishPop(mPopupWindow);
                imgSub.setBackgroundResource(R.drawable.shape_subed);
                imgSub.setText("已订阅");
                imgSub.setTextColor(getResources().getColor(R.color.white));
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                //取消订阅
                finishPop(mPopupWindow);
                toHttpAbolishOrder();
                isBroadcast = 2;
                break;
            case R.id.img_batchdown:
                if (!Util.hintLogin(this))
                    return;
                //批量下载
                if (albumDetail.getType() == 1) {
                    Toast.makeText(this, "无可下载内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this, BatchdownloadActivity.class);
                //这些属性都是下载需要的
                intent.putExtra("albumId", albumId);
                intent.putExtra("albumName", albumDetail.getAlbumname());
                intent.putExtra("authorName", albumDetail.getAuthorname());
                intent.putExtra("albumAvatar", albumDetail.getImgurl());
                intent.putExtra("contentNum", albumDetail.getContentnum());
                startActivity(intent);
                break;
            case R.id.img_gratuity:
                if (albumDetail == null)
                    return;
                //打赏主播
                if (isCharge) {
                    imgGratuity.setEnabled(false);
                    imgGratuity.setImageResource(R.drawable.subablumclick);
                } else {
                    intent = new Intent(this, GratuityActivity.class);
                    intent.putExtra("authorId", albumDetail.getAuthorid());
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 取消订阅
     */
    private void toHttpAbolishOrder() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", albumId);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ABOLISH_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (200 == result.getCode()) {
                    imgSub.setBackgroundResource(R.drawable.shape_sub);
                    imgSub.setText("订阅专辑");
                    imgSub.setTextColor(getResources().getColor(R.color.main_orange));
                    isFu = false;
                } else {
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 订阅
     */
    private void toHttpSubAlbum() {
        showLoadingDialog("正在操作");
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", albumId);
        imgSub.setEnabled(false);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ORDER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                imgSub.setEnabled(true);
                loadingDismiss();
                if (200 == result.getCode()) {
                    isFu = true;
                    imgSub.setBackgroundResource(R.drawable.shape_subed);
                    imgSub.setText("已订阅");
                    imgSub.setTextColor(getResources().getColor(R.color.white));
                } else if (306 == result.getCode()) {
                    if ("order-already".equals(result.getMsg())) {
                        isFu = true;
                        imgSub.setBackgroundResource(R.drawable.shape_subed);
                        imgSub.setText("已订阅");
                        imgSub.setTextColor(getResources().getColor(R.color.white));
                    } else
                        Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
                } else
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                imgSub.setEnabled(true);
                loadingDismiss();
                Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
//                    tvDetails.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
//                    tvContent.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
//        tvDetails.setTextColor(Color.parseColor("#999999"));
//        tvContent.setTextColor(Color.parseColor("#999999"));
    }


    private void showCustomPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_subalbum, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(llMdetails, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llMdetails, Gravity.CENTER, 0, 0);
        }
        imgSub.setBackgroundResource(R.drawable.shape_subed);
        imgSub.setText("已订阅");
        imgSub.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 隐藏PopupWindow
     *
     * @param popupWindow 要隐藏的PopupWindow
     */
    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                //如果mainActivity不存在则跳转主页面
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
