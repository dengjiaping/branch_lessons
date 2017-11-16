package com.yidiankeyan.science.app.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.DownloadApkProgress;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UpdataBean;
import com.yidiankeyan.science.app.push.ExampleUtil;
import com.yidiankeyan.science.functionkey.activity.SearchActivity;
import com.yidiankeyan.science.information.acitivity.AudioControlActivity;
import com.yidiankeyan.science.information.acitivity.MozReadAudioActivity;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.information.fragment.RecommendFragment;
import com.yidiankeyan.science.knowledge.KnowledgeFragment;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.my.fragment.MyFragment;
import com.yidiankeyan.science.my.fragment.PurchaseMyFragment;
import com.yidiankeyan.science.utils.ApkUpdateUtils;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.ClearInputMethodManagerUtil;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.FileDownloadManager;
import com.yidiankeyan.science.utils.FlymeUtils;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.MIUIUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.NetWorkUtils;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.ExpandableLinearLayout;
import com.yidiankeyan.science.view.SelectPicPopupWindow;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private Intent intent = null;
    //返回键
//    private ImageView titleReturn;
    //定义标题栏上的按钮
//    private ImageButton titleBtn;

//    private TextView txtMainTitle;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //定义Fragment页面
    private RecommendFragment recommendFragment;
    //    private SubscribeFragment subscribeFragment;
//    private FreeAblumFragment freeAblumFragment;
    //    private CollectionFragment collectionFragment;
    private KnowledgeFragment knowledgeFragment;
    private PurchaseMyFragment purchaseMyFragment;
    private MyFragment myFragment;


    private RelativeLayout rlInfo;
    private RelativeLayout rlCollection;
    private RelativeLayout rlPurchase;
    private RelativeLayout rlMine;
    private ImageView imgInfo;
    private ImageView imgCollection;
    private ImageView imgPurchase;
    private ImageView imgMine;
    private ImageView imgCol;
    private View llMain;
    private TextView tvInfoTxtColor;
    private TextView tvCollectionTxtColor;
    private TextView tvPurchaseTxtcolor;
    private TextView tvMyTxtColor;

    //订阅置顶
//    private ImageButton btnDingYue;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    public static final int INTO_SCAN_CODE = 156;

//    private AutoRelativeLayout llTitle;
//    private AutoLinearLayout llPreservationClick;
//    private TextView tvPreservationClick;
    /**
     * 该页面是否已经创建过
     */
    private boolean isCreated = false;

    private IntentFilter intentFilter;
    private MozReadDetailsBean detailsBean;
    private String id;
    private int priceInsufficient = 0;

    private PopupWindow purchasePopupWindow;
    private BalanceBean balanceBean;
    private TextView tvShopPrice;
    private AutoLinearLayout llBalance;
    private AutoLinearLayout llWxMoney;
    private AutoLinearLayout llZfMoney;
    private TextView tvWx;
    private TextView tvZf;
    private TextView tvBalance;
    private TextView tvBalanceMoney;
    private TextView tvSaveMoney;
    private int paymentMode = 0;
    private String ShareId;


    private Long startTime;
    private Long endTime;
    private Long currentTime;
    private double price = 0;
    private boolean statusDarked;

    private ExpandableLinearLayout expandLayout;

    private ImageView imgPlayBtn;
    private ImageView imgStop;

    private TextView tvAudioName;
    private TextView tvAudioCurr;
    private TextView tvAudioLength;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (AudioPlayManager.getInstance().isPlaying()) {
                imgPlayBtn.setImageResource(R.drawable.icon_control_pause);
                tvAudioName.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                tvAudioCurr.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                tvAudioLength.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                mTvSeparate.setText("/");
            }
        }
    };
    private int layoutHeight;
    private TextView mTvSeparate;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    private void setStatusDark(boolean dark) {
        if (MIUIUtils.isMIUI()) {
            if (dark)
                statusDarked = DisplayUtil.MIUISetStatusBarLightMode(getWindow(), dark);
            else DisplayUtil.MIUISetStatusBarLightMode(getWindow(), dark);
        } else if (FlymeUtils.isFlyme()) {
            if (dark)
                statusDarked = DisplayUtil.FlymeSetStatusBarLightMode(getWindow(), dark);
            else DisplayUtil.FlymeSetStatusBarLightMode(getWindow(), dark);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (dark)
                statusDarked = true;
        }
    }

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_main;
    }

    /**
     * 初始化组件
     */
    @Override
    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        LogUtils.e("AppKey:" + ExampleUtil.getAppKey(getApplicationContext()));
        if (MIUIUtils.isMIUI()) {
            LogUtils.e("RegId:" + MiPushClient.getRegId(DemoApplication.applicationContext));
        } else {
            LogUtils.e("RegId:" + JPushInterface.getRegistrationID(getApplicationContext()));
        }
        LogUtils.e("deviceId:" + ExampleUtil.getDeviceId(getApplicationContext()));
        LogUtils.e("IMEI:" + ExampleUtil.getImei(getApplicationContext(), ""));

        imgPlayBtn = (ImageView) findViewById(R.id.img_play_btn);
        imgStop = (ImageView) findViewById(R.id.img_stop);
        tvAudioName = (TextView) findViewById(R.id.tv_audio_name);
        tvAudioCurr = (TextView) findViewById(R.id.tv_audio_curr);
        tvAudioLength = (TextView) findViewById(R.id.tv_audio_length);
        mTvSeparate = (TextView) findViewById(R.id.tv_separate);
        expandLayout = (ExpandableLinearLayout) findViewById(R.id.expand_layout);
        llMain = findViewById(R.id.main);
        rlPurchase = (RelativeLayout) findViewById(R.id.rl_purchase);
        rlMine = (RelativeLayout) findViewById(R.id.rl_mine);
        rlCollection = (RelativeLayout) findViewById(R.id.rl_collection);
        rlInfo = (RelativeLayout) findViewById(R.id.rl_info);
        imgInfo = (ImageView) findViewById(R.id.img_info);
        imgCollection = (ImageView) findViewById(R.id.img_collection);
        imgPurchase = (ImageView) findViewById(R.id.img_purchase);

        imgMine = (ImageView) findViewById(R.id.img_mine);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        tvInfoTxtColor = (TextView) findViewById(R.id.tv_info_txtcolor);
        tvCollectionTxtColor = (TextView) findViewById(R.id.tv_collection_txtcolor);
        tvPurchaseTxtcolor = (TextView) findViewById(R.id.tv_purchase_txtcolor);
        tvMyTxtColor = (TextView) findViewById(R.id.tv_my_txtcolor);
//        btnDingYue = (ImageButton) findViewById(R.id.btn_xiala);
//        findViewById(R.id.title_btn).setVisibility(View.VISIBLE);
        rlPurchase.setOnClickListener(this);
        imgPlayBtn.setOnClickListener(this);
        rlMine.setOnClickListener(this);
        rlCollection.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        imgStop.setOnClickListener(this);

//        LogUtils.e("ABI:" + android.os.Build.CPU_ABI);
//        ToastMaker.showShortToast("ABI:" + android.os.Build.CPU_ABI);
//        ToastMaker.showShortToast("渠道编号:" + Util.getAppMetaData(DemoApplication.applicationContext, "UMENG_CHANNEL"));

//        llTitle = (AutoRelativeLayout) findViewById(R.id.layout_title);
//        txtMainTitle = (TextView) findViewById(R.id.maintitle_txt);
//        titleReturn = (ImageView) findViewById(R.id.title_return);
//        llPreservationClick = (AutoLinearLayout) findViewById(R.id.ll_preservation_click);
//        tvPreservationClick = (TextView) findViewById(R.id.tv_preservation_click);
        //取消返回按钮
//        titleReturn.setVisibility(View.INVISIBLE);

        //实例化标题栏按钮并设置监听
//        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        imgCol = (ImageView) findViewById(R.id.img_col);
//        titleBtn.setOnClickListener(searchOnClickListener);
//        tvPreservationClick.setText("取消");
//        llPreservationClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                collectionFragment.onCancelClick();
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("newIntent", false)) {
            fragmentTransaction = fragmentManager.beginTransaction();
            //隐藏所有的Fragment
            hideFragment(fragmentTransaction);
            if (recommendFragment != null) {
                fragmentTransaction.show(recommendFragment);
            } else {
                recommendFragment = new RecommendFragment();
                fragmentTransaction.add(R.id.layout_main, recommendFragment);
            }
            imgInfo.setEnabled(false);
            tvInfoTxtColor.setTextColor(Color.parseColor("#303030"));
            fragmentTransaction.commitAllowingStateLoss();
            EventMsg onmsg = EventMsg.obtain(SystemConstant.MOZ_REGISTER_HOT);
            onmsg.setArg1(1);
            EventBus.getDefault().post(onmsg);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        ApiServerManager.getInstance().getApiServer().getUpdateNum().enqueue(new RetrofitCallBack<Integer>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<Integer>> call, Response<RetrofitResult<Integer>> response) {
//                if (response.body().getCode() == 200) {
//                    if (response.body().getData() > 0) {
//                        tvUpdateNum.setVisibility(View.VISIBLE);
//                        tvUpdateNum.setText(response.body().getData() + "");
//                    } else tvUpdateNum.setVisibility(View.GONE);
//                } else
//                    tvUpdateNum.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<Integer>> call, Throwable t) {
//                tvUpdateNum.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    protected void initAction() {
        findViewById(R.id.rl_audio_control).setOnClickListener(this);
        expandLayout.post(new Runnable() {
            @Override
            public void run() {
                layoutHeight = expandLayout.getHeight();
                expandLayout.getLayoutParams().height = 0;
                expandLayout.requestLayout();
            }
        });

        SpUtils.putBooleanSp(this, SystemConstant.APP_IS_FIRST_START, true);
        toHttpGetAnswerAlbum();
        initFagment();
        System.gc();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                //每隔0.1秒检查音频播放器是否处于播放中，如果是的话更新进度条和已播放时间
                if (AudioPlayManager.getInstance().isPlaying()) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);
    }

    public boolean isStatusDarked() {
        return statusDarked;
    }

    private void toHttpGetAnswerAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 40);
        ApiServerManager.getInstance().getApiServer().getAnswerAlbum(map).enqueue(new RetrofitCallBack<List<AnswerAlbumBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<AnswerAlbumBean>>> call, Response<RetrofitResult<List<AnswerAlbumBean>>> response) {
                if (response.body().getData() != null && response.body().getData().size() > 0) {
                    for (AnswerAlbumBean answerAlbumBean : response.body().getData()) {
                        DemoApplication.answerAlbumMap.put(answerAlbumBean.getId(), answerAlbumBean);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AnswerAlbumBean>>> call, Throwable t) {

            }
        });
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isCreated) {
            isCreated = true;
            Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .build(), new AcpListener() {
                @Override
                public void onGranted() {

                }

                @Override
                public void onDenied(List<String> permissions) {
                    ToastMaker.showShortToast("有部分功能将无法使用");
                }
            });
            /*按照村长要求去除WIFI监测，在所有情况下提示*/
//            if (NetWorkUtils.isWifi(DemoApplication.applicationContext))
            toHttpCheckVersionCode();
            compareApk();
        }
    }

    /**
     * 判断当前的版本和已下载的版本是否一致，如果是则删除
     */
    private void compareApk() {
        long downloadId = SpUtils.getLongSp(DemoApplication.applicationContext, "downloadId");
        if (downloadId != 0) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(DemoApplication.applicationContext);
            Uri uri = fdm.getDownloadUri(downloadId);
            if (uri != null) {
                if (!ApkUpdateUtils.compare(ApkUpdateUtils.getApkInfo(DemoApplication.applicationContext, uri.getPath()), DemoApplication.applicationContext)) {
                    fdm.getDm().remove(downloadId);
                    Util.deleteFiles(uri.getPath());
                    SpUtils.removeSpKey(DemoApplication.applicationContext, "downloadId");
                }
            }
        }
    }

    /**
     * 检查版本号
     */
    private void toHttpCheckVersionCode() {
//        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_THE_LAST_VERSION, new HashMap<>(), new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    final UpdataBean updataBean = (UpdataBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UpdataBean.class);
//                    if (Util.getVersionCode(MainActivity.this) < Integer.parseInt(updataBean.getVersionCode())) {
//                        showWaringDialog("提示", "最新版本" + updataBean.getVersionName() + ",是否立即更新？\n" + updataBean.getMessage(), new OnDialogButtonClickListener() {
//                            @Override
//                            public void onPositiveButtonClick() {
//                                //检查权限
//                                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
//                                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        .build(), new AcpListener() {
//                                    @Override
//                                    public void onGranted() {
//                                        updataApk(updataBean.getURL(), updataBean.getVersionName());
//                                    }
//
//                                    @Override
//                                    public void onDenied(List<String> permissions) {
//                                        ToastMaker.showShortToast("有部分功能将无法使用");
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onNegativeButtonClick() {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
        ApiServerManager.getInstance().getApiServer().checkUpdate(new HashMap()).enqueue(new RetrofitCallBack<UpdataBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<UpdataBean>> call, Response<RetrofitResult<UpdataBean>> response) {
                if (response.body().getCode() == 200) {
                    final UpdataBean updataBean = response.body().getData();
                    if (Util.getVersionCode(MainActivity.this) < Integer.parseInt(updataBean.getVersionCode())) {
                        showWaringDialog("提示", "最新版本" + updataBean.getVersionName() + ",是否立即更新？\n" + updataBean.getMessage(), new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                //检查权限
                                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .build(), new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        updataApk(updataBean.getURL(), updataBean.getVersionName());
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        ToastMaker.showShortToast("有部分功能将无法使用");
                                    }
                                });
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<UpdataBean>> call, Throwable t) {

            }
        });
    }

    private void updataApk(String url, String versionName) {
        if (!canDownloadState()) {
            ToastMaker.showShortToast("下载服务不可用,请您启用");
            showDownloadSetting();
            return;
        }
        ApkUpdateUtils.download(DemoApplication.applicationContext, url, versionName);
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 是否可以下载
     *
     * @return
     */
    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTO_SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    //获取扫描的结果进入好友验证
                    String resultData = data.getExtras().getString("result");
                    Log.e("resultData", "=====" + resultData);
                }
                break;
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent.getBooleanExtra("gotoDownload", false)) {
//            rlCollection.performClick();
//            knowledgeFragment.showDownloadList();
//        }
//    }

    //为订阅下拉弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    break;
                default:
                    break;
            }
        }
    };

    //初始化默认显示界面
    private void initFagment() {
        fragmentManager = getSupportFragmentManager();
//        txtMainTitle.setText("资讯");
        imgInfo.setEnabled(false);
//        llTitle.setVisibility(View.VISIBLE);
        tvInfoTxtColor.setTextColor(Color.parseColor("#303030"));
        fragmentTransaction = fragmentManager.beginTransaction();
        recommendFragment = new RecommendFragment();
        fragmentTransaction.add(R.id.layout_main, recommendFragment);
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            for (Fragment childFragment : fragmentManager.getFragments()) {
                fragmentTransaction.remove(childFragment);
            }
        }
        fragmentTransaction.commit();
    }

    /**
     * 隐藏所有到fragment
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (recommendFragment != null) {
            fragmentTransaction.hide(recommendFragment);
        }
//        if (knowledgeFragment != null) {
//            fragmentTransaction.hide(knowledgeFragment);
//        }
        if (purchaseMyFragment != null) {
            fragmentTransaction.hide(purchaseMyFragment);
        }
        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
        }

        imgInfo.setEnabled(true);
        imgPurchase.setEnabled(true);
        imgCollection.setEnabled(true);
        imgMine.setEnabled(true);
        tvInfoTxtColor.setTextColor(Color.parseColor("#acacae"));
        tvPurchaseTxtcolor.setTextColor(Color.parseColor("#acacae"));
        tvCollectionTxtColor.setTextColor(Color.parseColor("#acacae"));
        tvMyTxtColor.setTextColor(Color.parseColor("#acacae"));
    }

    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener collectDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            knowledgeFragment.onDeleteClick();
        }
    };

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_audio_control:
                startActivity(new Intent(this, AudioControlActivity.class));
                break;
            case R.id.rl_info:
                fragmentTransaction = fragmentManager.beginTransaction();
                //隐藏所有的Fragment
                hideFragment(fragmentTransaction);
                currentTabIndex = 0;
                if (recommendFragment != null) {
                    fragmentTransaction.show(recommendFragment);
                } else {
                    recommendFragment = new RecommendFragment();
                    fragmentTransaction.add(R.id.layout_main, recommendFragment);
                }
//                llTitle.setVisibility(View.VISIBLE);
//                titleBtn.setVisibility(View.VISIBLE);
//                llPreservationClick.setVisibility(View.GONE);
//                titleBtn.setImageResource(R.drawable.search);
//                titleBtn.setOnClickListener(searchOnClickListener);
//                txtMainTitle.setText("资讯");
                imgInfo.setEnabled(false);
                tvInfoTxtColor.setTextColor(Color.parseColor("#303030"));
                fragmentTransaction.commitAllowingStateLoss();
                setStatusDark(false);
                break;
//            case R.id.rl_collection:
//                fragmentTransaction = fragmentManager.beginTransaction();
//                //隐藏所有的Fragment
//                hideFragment(fragmentTransaction);
//                currentTabIndex = 1;
//                if (knowledgeFragment != null) {
//                    fragmentTransaction.show(knowledgeFragment);
////                    if (knowledgeFragment.getCurrIndex() == 0 || knowledgeFragment.getCurrIndex() == 1) {
////                        if (knowledgeFragment.isDeleteState()) {
//////                            titleBtn.setVisibility(View.GONE);
//////                            llPreservationClick.setVisibility(View.VISIBLE);
////                        } else {
//////                            titleBtn.setVisibility(View.VISIBLE);
//////                            llPreservationClick.setVisibility(View.GONE);
////                        }
////                    } else {
//////                        titleBtn.setVisibility(View.GONE);
//////                        llPreservationClick.setVisibility(View.GONE);
////                    }
//                } else {
//                    knowledgeFragment = new KnowledgeFragment();
//                    fragmentTransaction.add(R.id.layout_main, knowledgeFragment);
////                    titleBtn.setVisibility(View.VISIBLE);
////                    llPreservationClick.setVisibility(View.GONE);
//                }
////                llTitle.setVisibility(View.VISIBLE);
////                txtMainTitle.setText("点藏");
////                titleBtn.setImageResource(R.drawable.icon_subsribe_more);
////                titleBtn.setOnClickListener(collectDeleteOnClickListener);
//                imgCollection.setEnabled(false);
//                tvCollectionTxtColor.setTextColor(Color.parseColor("#303030"));
//                imgCol.setVisibility(View.GONE);
//                Intent intent = new Intent();
//                intent.setAction("action.read.audio");
//                sendBroadcast(intent);
//                fragmentTransaction.commitAllowingStateLoss();
//                setStatusDark(false);
//                break;
            case R.id.rl_purchase:
                fragmentTransaction = fragmentManager.beginTransaction();
                //隐藏所有的Fragment
                hideFragment(fragmentTransaction);
                currentTabIndex = 1;
                if (purchaseMyFragment != null) {
                    fragmentTransaction.show(purchaseMyFragment);
                } else {
                    purchaseMyFragment = new PurchaseMyFragment();
                    fragmentTransaction.add(R.id.layout_main, purchaseMyFragment);
                }
                imgPurchase.setEnabled(false);
                tvPurchaseTxtcolor.setTextColor(Color.parseColor("#303030"));
                fragmentTransaction.commitAllowingStateLoss();
                setStatusDark(true);
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_COLUMN_FLASH);
                EventBus.getDefault().post(msg);
                break;
            case R.id.rl_mine:
                fragmentTransaction = fragmentManager.beginTransaction();
                //隐藏所有的Fragment
                hideFragment(fragmentTransaction);
                currentTabIndex = 2;
                if (myFragment != null) {
                    fragmentTransaction.show(myFragment);
                } else {
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.layout_main, myFragment);
                }
//                llTitle.setVisibility(View.GONE);
//                llPreservationClick.setVisibility(View.GONE);
//                titleBtn.setVisibility(View.GONE);
//                txtMainTitle.setText("我的");
                imgMine.setEnabled(false);
                tvMyTxtColor.setTextColor(Color.parseColor("#303030"));
                fragmentTransaction.commitAllowingStateLoss();
                setStatusDark(false);
                break;
            case R.id.img_play_btn:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PREPARE:
                        AudioPlayManager.getInstance().release();
                        imgPlayBtn.setImageResource(R.drawable.icon_control_play);
                        break;
                    case SystemConstant.ON_PAUSE:
                        AudioPlayManager.getInstance().ijkStart();
                        imgPlayBtn.setImageResource(R.drawable.icon_control_pause);
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        imgPlayBtn.setImageResource(R.drawable.icon_control_play);
                        break;
                    default:
                        AudioPlayManager.getInstance().ijkStart();
                        imgPlayBtn.setImageResource(R.drawable.icon_control_pause);
                        DemoApplication.isPlay = true;
                        break;
                }
                break;
            case R.id.img_stop:
                AudioPlayManager.getInstance().stop();
                AudioPlayManager.getInstance().reset();
                if (expandLayout.getHeight() != 0 && !expandLayout.isAnimating()) {
                    expandLayout.collapsed();
                }
                break;

            case R.id.ll_balance:
                setNormal();
                paymentMode = 1;
                llBalance.setEnabled(false);
                tvBalanceMoney.setTextColor(Color.parseColor("#f1312e"));
                tvBalance.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.ll_wx_money:
                setNormal();
                paymentMode = 2;
                llWxMoney.setEnabled(false);
                tvWx.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.ll_zf_money:
                setNormal();
                paymentMode = 3;
                llZfMoney.setEnabled(false);
                tvZf.setTextColor(Color.parseColor("#f1312e"));
                break;
            case R.id.tv_save_money:
                if (paymentMode == 1) {
                    balancePayment();
                } else if (paymentMode == 2) {
                    ToastMaker.showShortToast("请稍后");
                    wxPay();
                } else if (paymentMode == 3) {
                    ToastMaker.showShortToast("请稍后");
                    aliPay();
                } else {
                    ToastMaker.showShortToast("请选择支付方式");
                }
                break;
        }

    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
//            case SystemConstant.ON_USER_LOGOUT:
//                unreadLabel.setVisibility(View.INVISIBLE);
//                break;
            case SystemConstant.NOTIFY_MAIN_COLLECT_SHOW_OR_HIDE:
                if (msg.getArg1() == 0) {
//                    titleBtn.setVisibility(View.GONE);
//                    llPreservationClick.setVisibility(View.GONE);
                } else {
//                    if (knowledgeFragment.isDeleteState()) {
////                        titleBtn.setVisibility(View.GONE);
////                        llPreservationClick.setVisibility(View.VISIBLE);
//                    } else {
////                        titleBtn.setVisibility(View.VISIBLE);
////                        llPreservationClick.setVisibility(View.GONE);
//                    }
                }
                break;
            case SystemConstant.NOTIFY_MAIN_CANCEL_SHOW_OR_HIDE:
                if (msg.getArg1() == 0) {
//                    titleBtn.setVisibility(View.VISIBLE);
//                    llPreservationClick.setVisibility(View.GONE);
                } else {
//                    titleBtn.setVisibility(View.GONE);
//                    llPreservationClick.setVisibility(View.VISIBLE);
                }
                break;
            case SystemConstant.DOWNLOAD_APK:
                //apk下载监听
                DownloadApkProgress progress = (DownloadApkProgress) msg.getBody();

                break;
            /**
             * 书籍购买
             */
            case SystemConstant.MOZ_READ_PURCHASE:
                onReadDetails(msg);
                break;

            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                try {
                    int result = msg.getArg1();
                    if (result == -100) {
                        ToastMaker.showShortToast("支付失败");
                    } else if (result == 0) {
                        ToastMaker.showShortToast("支付成功");
                        Util.finishPop(MainActivity.this, purchasePopupWindow);
                        //跳转墨子读书播放
                        Intent intent = new Intent(MainActivity.this, MozReadAudioActivity.class);
                        intent.putExtra("id", detailsBean.getId());
                        startActivity(intent);
                    } else if (result == -2) {
                        ToastMaker.showShortToast("支付取消");
                    } else {
                        ToastMaker.showShortToast("支付失败");
                    }
                } catch (Exception e) {

                }
                break;
            case SystemConstant.MOZ_REGISTER_RETURN:
                if (msg.getArg1() == 1) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //隐藏所有的Fragment
                    hideFragment(fragmentTransaction);
                    if (recommendFragment != null) {
                        fragmentTransaction.show(recommendFragment);
                    } else {
                        recommendFragment = new RecommendFragment();
                        fragmentTransaction.add(R.id.layout_main, recommendFragment);
                    }
                    imgInfo.setEnabled(false);
                    fragmentTransaction.commitAllowingStateLoss();
                }

                EventMsg onmsg = EventMsg.obtain(SystemConstant.MOZ_REGISTER_HOT);
                onmsg.setArg1(1);
                EventBus.getDefault().post(onmsg);
                break;
            case SystemConstant.MAINACTIVITY_AUDIO_REFRESH:
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP) {
                    imgPlayBtn.setImageResource(R.drawable.icon_control_play);

                } else {
                    imgPlayBtn.setImageResource(R.drawable.icon_control_pause);
                }
                if (TextUtils.equals("null", AudioPlayManager.getInstance().getCurrAudio().getName())) {
                    tvAudioName.setText("");
                } else {
                    tvAudioName.setText(AudioPlayManager.getInstance().getCurrAudio().getName());
                }

                if (expandLayout.getHeight() != layoutHeight && !expandLayout.isAnimating()) {
                    expandLayout.setExpandHeight(layoutHeight);
                    expandLayout.expand();
                }
                break;
            case SystemConstant.ON_HIDE_PLAY:
                //其他界面音频暂停隐藏主界面播放条
                AudioPlayManager.getInstance().stop();
                AudioPlayManager.getInstance().reset();
                if (expandLayout.getHeight() != 0 && !expandLayout.isAnimating()) {
                    expandLayout.collapsed();
                }
                break;
            case SystemConstant.ON_STOP:
                tvAudioCurr.setText("00:00");
                imgPlayBtn.setImageResource(R.drawable.icon_control_play);
                break;
        }

    }

    float startX;
    float startY;
    float offsetsByX;
    float offsetsByY;
    float movedX;
    float movedY;
    float offsetX;
    float offsetY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下时
                startX = ev.getRawX();//获得按下时的X坐标
                startY = ev.getRawY();//获得按下时的Y坐标
                offsetsByX = 0;//设置X轴方向总偏移量
                offsetsByY = 0;//设置Y轴方向总偏移量
                break;
            case MotionEvent.ACTION_MOVE:
                movedX = ev.getRawX();//获得移动时候的X坐标
                movedY = ev.getRawY();//获得移动时候的Y坐标
                offsetX = startX - movedX;//获得X轴的偏移量
                offsetY = startY - movedY;//获得Y轴的偏移量
                if (Math.abs(offsetX) < Math.abs(offsetY)) {
                    //若X轴的偏移量大于Y轴的偏移量，则表示在横向滑动，否则在纵向滑动，若相同，则表示移动的时候没有移动，
                    // 这里滑动的时候，会不断的调用这个，一次滑动的距离等于多次的偏移量相加
                    if (offsetsByY > 100) {
                        //上滑，隐藏
//                        if (rlAudioControl.getVisibility() == View.VISIBLE) {
//                            rlAudioControl.setVisibility(View.GONE);
//                        }
                        if (expandLayout.getHeight() != 0 && !expandLayout.isAnimating()) {
                            expandLayout.collapsed();
                        }
                    } else if (offsetsByY < -100) {
                        //下滑，显示
                        if (expandLayout.getHeight() != layoutHeight && !expandLayout.isAnimating() && AudioPlayManager.getInstance().getCurrAudio() != null) {
                            expandLayout.setExpandHeight(layoutHeight);
                            expandLayout.expand();
                        }
                    }
                }
                offsetsByX += offsetX;//不断移动的时候获得移动的X轴总偏移量
                offsetsByY += offsetY;//获得Y轴总偏移量
                startX = movedX;//把每次移动后的X坐标作为下次移动开始时的X坐标
                startY = movedY;//把每次移动后的Y坐标作为下次移动开始时的Y坐标
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 监控正在播放的定时器
     */
    private Timer mTimer;
    private TimerTask mTimerTask;


    private void onReadDetails(EventMsg msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msg.getBody());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_READ_DETAILS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    detailsBean = (MozReadDetailsBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), MozReadDetailsBean.class);
                    price = detailsBean.getPrice();
                    Map<String, Object> map = new HashMap<>();
                    if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                        map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                    }else map.put("id", "");
                    ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                            if (response.body().getCode() == 200) {
                                if (response.body().getData().getBalance() < price) {
                                    priceInsufficient = 1;
                                } else {
                                    priceInsufficient = 2;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                        }
                    });
                    if (price > 0) {
                        showPayWindow();
                    } else {
                        toHttpReceiveRead();
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });

    }


    /**
     * 选择支付方式
     */

    private void showPayWindow() {
        hideSoftKeyboard();
        if (purchasePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.pop_purchase_win, null);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            llBalance = (AutoLinearLayout) view.findViewById(R.id.ll_balance);
            llWxMoney = (AutoLinearLayout) view.findViewById(R.id.ll_wx_money);
            llZfMoney = (AutoLinearLayout) view.findViewById(R.id.ll_zf_money);
            tvWx = (TextView) view.findViewById(R.id.tv_wx);
            tvZf = (TextView) view.findViewById(R.id.tv_zf);
            tvBalance = (TextView) view.findViewById(R.id.tv_balance);
            tvBalanceMoney = (TextView) view.findViewById(R.id.tv_balance_money);
            tvShopPrice.setText(price + "");
            Map<String, Object> map = new HashMap<>();
            if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
            }else map.put("id", "");
            ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                    if (response.body().getCode() == 200) {
                        balanceBean = response.body().getData();
                        tvBalanceMoney.setText(balanceBean.getBalance() + " 墨子币");
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                }
            });
            tvSaveMoney = (TextView) view.findViewById(R.id.tv_save_money);
            if (priceInsufficient == 1) {
                llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
                tvBalance.setTextColor(Color.parseColor("#ffffff"));
                tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
                llBalance.setEnabled(false);
            } else {
                llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
                tvBalance.setTextColor(Color.parseColor("#333333"));
                tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
            }
            llBalance.setOnClickListener(this);
            llWxMoney.setOnClickListener(this);
            llZfMoney.setOnClickListener(this);
            tvSaveMoney.setOnClickListener(this);
            purchasePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            purchasePopupWindow.setContentView(view);
            purchasePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            purchasePopupWindow.setFocusable(true);
            purchasePopupWindow.setOutsideTouchable(true);
            purchasePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            purchasePopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
            purchasePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MainActivity.this, purchasePopupWindow);
                    setNormal();
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            purchasePopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
            tvShopPrice.setText(price + "");
        }
    }


    /**
     * 将打赏金额背景全部设为正常
     */
    private void setNormal() {
        llWxMoney.setEnabled(true);
        llZfMoney.setEnabled(true);
        tvWx.setTextColor(Color.parseColor("#333333"));
        tvZf.setTextColor(Color.parseColor("#333333"));
        if (priceInsufficient == 1) {
            llBalance.setBackgroundColor(Color.parseColor("#cccccc"));
            tvBalance.setTextColor(Color.parseColor("#ffffff"));
            tvBalanceMoney.setTextColor(Color.parseColor("#ffffff"));
            llBalance.setEnabled(false);
        } else {
            llBalance.setBackgroundResource(R.drawable.selector_custom_textview);
            tvBalance.setTextColor(Color.parseColor("#333333"));
            tvBalanceMoney.setTextColor(Color.parseColor("#333333"));
            llBalance.setEnabled(true);
        }
    }

    /**
     * 限时领取
     */
    private void toHttpReceiveRead() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "6");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "领取成功", Toast.LENGTH_SHORT).show();
                    id = (String) result.getData();
                    Util.finishPop(MainActivity.this, purchasePopupWindow);
                    //跳转墨子读书播放
                    Intent intent = new Intent(MainActivity.this, MozReadAudioActivity.class);
                    intent.putExtra("id", detailsBean.getId());
                    startActivity(intent);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "领取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 余额支付
     */
    private void balancePayment() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "3");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    ToastMaker.showShortToast("支付成功");
                    Util.finishPop(MainActivity.this, purchasePopupWindow);
                    id = (String) result.getData();
                    //跳转墨子读书播放
                    Intent intent = new Intent(MainActivity.this, MozReadAudioActivity.class);
                    intent.putExtra("id", detailsBean.getId());
                    startActivity(intent);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                } else if (result.getCode() == 417) {
                    ToastMaker.showShortToast("余额不足");
                } else {
                    ToastMaker.showShortToast("支付失败");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "1");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MainActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastMaker.showShortToast("支付成功");
                                        Util.finishPop(MainActivity.this, purchasePopupWindow);
                                        //跳转墨子读书播放
                                        Intent intent = new Intent(MainActivity.this, MozReadAudioActivity.class);
                                        intent.putExtra("id", detailsBean.getId());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastMaker.showShortToast("支付失败");
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 微信支付
     */
    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", detailsBean.getId());
        map.put("price", price);
        map.put("type", "2");
        map.put("casterid", detailsBean.getCasterid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BOOKS_BUYS, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    id = wxPay.getId();
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                } else if (result.getCode() == 306) {
                    Toast.makeText(DemoApplication.applicationContext, "该书籍已经购买", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    private TextView unreadLabel;
    public int currentTabIndex;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setContentView(R.layout.empty_view);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mRefreshBroadcastReceiver);
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
        intent = null;
//        titleBtn = null;
//        txtMainTitle = null;
        fragmentManager = null;
        fragmentTransaction = null;
        recommendFragment = null;
//        knowledgeFragment = null;
        purchaseMyFragment = null;
        myFragment = null;
        rlInfo = null;
        rlCollection = null;
        rlPurchase = null;
        rlMine = null;
        imgInfo = null;
        imgCollection = null;
        imgPurchase = null;
        imgMine = null;
//        btnDingYue = null;
        menuWindow = null;
        ClearInputMethodManagerUtil.fixFocusedViewLeak(getApplication());
        System.gc();
    }
}
