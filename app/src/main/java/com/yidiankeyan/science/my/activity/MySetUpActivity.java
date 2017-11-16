package com.yidiankeyan.science.my.activity;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UpdataBean;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.utils.ApkUpdateUtils;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.DataCleanManager;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 我的
 * -设置
 */
public class MySetUpActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private AutoRelativeLayout llCleanCache;
    private TextView tvCacheSize;
    private TextView tvExit;
    private AutoRelativeLayout llMyAboutwe;
    private Intent intent;
    private SwitchButton switchMyRecomment;
    private AutoRelativeLayout llMyMonitor;
    private TextView tvMonitor;
    private boolean isFo;
    private String edition;
    private PopupWindow mPopupWindow;
    private AutoLinearLayout llMySet;


    @Override
    protected int setContentView() {
        return R.layout.activity_my_set_up;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        llCleanCache = (AutoRelativeLayout) findViewById(R.id.rl_my_clearcache);
        tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        tvExit = (TextView) findViewById(R.id.tv_exit);
        llMyAboutwe = (AutoRelativeLayout) findViewById(R.id.ll_my_aboutwe);
        switchMyRecomment = (SwitchButton) findViewById(R.id.switch_my_recomment);
        llMyMonitor = (AutoRelativeLayout) findViewById(R.id.ll_my_monitor);
        tvMonitor = (TextView) findViewById(R.id.tv_monitor);
        llMySet = (AutoLinearLayout) findViewById(R.id.ll_my_set);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("个人设置");
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        switchMyRecomment.setTag(false);
        try {
            tvMonitor.setText(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        isFo = isNotificationEnabled(this);
        if (isFo) {
            switchMyRecomment.setTag(true);
            switchMyRecomment.setChecked(true);
        } else {
            switchMyRecomment.setTag(false);
            switchMyRecomment.setChecked(false);
        }
        try {
            tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvExit.setOnClickListener(this);
        llCleanCache.setOnClickListener(this);
        llMyAboutwe.setOnClickListener(this);
        switchMyRecomment.setOnClickListener(this);
        llMyMonitor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.switch_my_recomment:
                if (isFo) {
                    showPop("确定关闭推送吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchMyRecomment.setTag(true);
                            switchMyRecomment.setChecked(true);
                            Util.finishPop(MySetUpActivity.this, mPopupWindow);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.finishPop(MySetUpActivity.this, mPopupWindow);
                            goToSet();
                            finish();
                        }
                    });
                } else {
                    showPop("是否打开推送？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchMyRecomment.setTag(true);
                            switchMyRecomment.setChecked(true);
                            Util.finishPop(MySetUpActivity.this, mPopupWindow);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.finishPop(MySetUpActivity.this, mPopupWindow);
                            goToSet();
                            finish();
                        }
                    });
                }
                break;
            case R.id.ll_my_aboutwe:
                intent = new Intent(this, MyAboutWeActivity.class);
                if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                    intent.putExtra("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                }else intent.putExtra("id", "");
                startActivity(intent);
                break;
            case R.id.rl_my_clearcache:
                try {
                    DataCleanManager.clearAllCache(this);
                    tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
                    Toast.makeText(this, "清理成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.ll_my_monitor:
                toHttpCheckVersionCode();
                break;
            case R.id.tv_exit:
//                llScienceNumber.setVisibility(View.GONE);
//                tvUserName.setText("登录/注册" );
//                tvNumbers.setText("" );
//                tvUserName.setOnClickListener(this);
                (this).showWaringDialog(
                        "提示",
                        "是否退出登录？",
                        new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                                    //如果用户没有登录则直接删除数据
                                    finish();
                                } else {
                                    //用户先清除服务器端token再删数据
                                    Util.logout();
                                    showLoadingDialog("正在操作");
                                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
                                    AudioPlayManager.getInstance().release();
                                    AudioPlayManager.getInstance().reset();
                                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_TODAY_FLASH));
                                    Intent intent = new Intent();
                                    intent.setAction("action.read.audio");
                                    sendBroadcast(intent);
                                    ApiServerManager.getInstance().getUserApiServer().logout().enqueue(new RetrofitCallBack<Object>() {
                                        @Override
                                        public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                            loadingDismiss();
                                            if (response.body().getCode() == 200 || response.body().getCode() == 10000) {
                                                finish();
                                                DB.getInstance(DemoApplication.applicationContext).deleteAllClick();
                                            } else {
                                                Toast.makeText(DemoApplication.applicationContext, "登出失败，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                                            loadingDismiss();
                                            Toast.makeText(DemoApplication.applicationContext, "登出失败，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        }
                );
                break;
        }
    }


    private void showPop(String content, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_im_user_detail, null);
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
//            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MySetUpActivity.this, mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(llMySet, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llMySet, Gravity.CENTER, 0, 0);
        }
        ((TextView) mPopupWindow.getContentView().findViewById(R.id.tv_content)).setText(content);
        mPopupWindow.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(cancelListener);
        mPopupWindow.getContentView().findViewById(R.id.tv_confirm).setOnClickListener(confirmListener);
    }

    private void goToSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        }
    }


    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
     /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 检查版本号
     */
    private void toHttpCheckVersionCode() {
        ApiServerManager.getInstance().getApiServer().checkUpdate(new HashMap()).enqueue(new RetrofitCallBack<UpdataBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<UpdataBean>> call, Response<RetrofitResult<UpdataBean>> response) {
                if (response.body().getCode() == 200) {
                    final UpdataBean updataBean = response.body().getData();
                    if (Util.getVersionCode(MySetUpActivity.this) < Integer.parseInt(updataBean.getVersionCode())) {
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
                    } else {
                        ToastMaker.showShortToast("已经是最新版本");
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
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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
}
