package com.yidiankeyan.science.my.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.activity.MainLoginActivity;
import com.yidiankeyan.science.app.activity.RegisterActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.my.activity.AnswerAuthenticationActivity;
import com.yidiankeyan.science.my.activity.InvitationFriendsActivity;
import com.yidiankeyan.science.my.activity.MyAccountDetailedActivity;
import com.yidiankeyan.science.my.activity.MyCollectionActivity;
import com.yidiankeyan.science.my.activity.MyDownloadActivity;
import com.yidiankeyan.science.my.activity.MyNumberBindingActivity;
import com.yidiankeyan.science.my.activity.MyQrCodeActivity;
import com.yidiankeyan.science.my.activity.MySetUpActivity;
import com.yidiankeyan.science.my.activity.OpinionFeedbackActivity;
import com.yidiankeyan.science.my.activity.PersonalDataActivity;
import com.yidiankeyan.science.my.activity.ProfitCoreActivity;
import com.yidiankeyan.science.my.activity.PurchaseMainActivity;
import com.yidiankeyan.science.my.entity.UpdateProfitBalance;
import com.yidiankeyan.science.utils.DownLoadImageService;
import com.yidiankeyan.science.utils.FileUtils;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.yidiankeyan.science.view.pulltozoomview.PullToZoomBase;
import com.yidiankeyan.science.view.pulltozoomview.PullToZoomScrollViewEx;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 我的
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private PullToZoomScrollViewEx scrollView;
    private ImageView imgUserHead;
    private AutoRelativeLayout llMyRecomment;
    private AutoRelativeLayout rlRegister;
    private AutoRelativeLayout rlUserInformation;
    private TextView tvUserName;
    private TextView tvLogin;
    private TextView tvRegister;
    private PopupWindow mPopupWindow;
    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private ImageView imgShareSina;
    private AutoLinearLayout imgShareQQ;
    //    private TextView tvMyHomepage;
    private TextView btnCancel;
    private View llMain;
    private ImageView imgZoom;
    private TextView tvNumbers;
    private ImageView imgHeadBg;

    //设置
    private AutoRelativeLayout llSetUp;

    private AutoRelativeLayout llAuthentication, llMyBinding;
    private String log;

    public void setLog(String log) {
        this.log = log;
    }

    private PopupWindow bgPopupWindow;
    public static final int REQUEST_CODE_CUT = 103;
    public static final int REQUEST_CODE_PICTURE = 104;
    public static final int REQUEST_CODE_CAMERA = 105;
    public static final int INTO_FOCUS_ACTIVITY = 106;
    public static final int INTO_FANS_ACTIVITY = 107;
    public static final int INTO_INTEGRAL_ACTIVITY = 108;
    private Uri imageUri;
    private Uri imageCropUri;
    private int cropX;
    private int cropY;
    private Map<String, String> updataMap;
    private AutoRelativeLayout llMyShopping;
    private ImageView imgProfit;
    private ImageView imgBlance;


    private ExecutorService singleExecutor = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        if (!TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
            rlRegister.setVisibility(View.GONE);
            rlUserInformation.setVisibility(View.VISIBLE);
            tvUserName.setText(SpUtils.getStringSp(getContext(), "userName"));
            tvNumbers.setText("墨子号：" + SpUtils.getStringSp(getContext(), "scienceNum"));
            initImage();
        } else {
            rlRegister.setVisibility(View.VISIBLE);
            rlUserInformation.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if ((TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "access_token")) || SpUtils.getStringSp(DemoApplication.applicationContext, "access_token").startsWith("-"))) {
                //当前处于非登录状态并且界面显示为已登录
                SpUtils.putStringSp(DemoApplication.applicationContext, "userId", "");
                rlRegister.setVisibility(View.VISIBLE);
                rlUserInformation.setVisibility(View.GONE);
                tvLogin.setOnClickListener(this);
                imgZoom.setImageResource(R.drawable.bg_navy);
                tvNumbers.setText("");
                imgUserHead.setImageResource(R.drawable.icon_default_avatar);
                tvUserName.setText("");
            }
            if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                int mScreenWidth = localDisplayMetrics.widthPixels;
                LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.4235));
                scrollView.setHeaderLayoutParams(localObject);
                imgHeadBg.setVisibility(View.VISIBLE);
            } else {
                DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                int mScreenWidth = localDisplayMetrics.widthPixels;
                LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.208));
                scrollView.setHeaderLayoutParams(localObject);
            }
        }
    }

    /**
     * 初始化背景图片及头像
     */
    private void initImage() {
        String path = Util.getSDCardPath();
        File file = new File(path + "/temp.jpg");
        imageUri = Uri.fromFile(file);
        File cropFile = new File(Util.getSDCardPath() + "/temp_crop.jpg");
        imageCropUri = Uri.fromFile(cropFile);
        if (Util.fileExisted(cropFile)) {
            Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath(), null);
            if (bitmap != null) {
                Bitmap smallBmp = setScaleBitmap(bitmap, 2);
                imgZoom.setImageBitmap(smallBmp);
            }
        }
//        else {
//            onDownloadBg();
//        }
        if (Util.fileExisted(new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg"))) {
            Glide.with(this).load(Util.getSDCardPath() + "/temp_avatar_crop.jpg").error(R.drawable.icon_default_avatar)
                    .placeholder(R.drawable.icon_default_avatar)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imgUserHead);
//            if (Build.VERSION.SDK_INT > 18)
//                Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "userimgurl")).crossFade().bitmapTransform(new BlurTransformation(getActivity())).into(imgZoom);
        } else {
            if (SpUtils.getStringSp(getContext(), "userimgurl") != null && SpUtils.getStringSp(getContext(), "userimgurl").startsWith("http")) {
                Glide.with(this).load(SpUtils.getStringSp(getContext(), "userimgurl"))
                        .error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
            } else {
                Glide.with(this).load(Util.getImgUrl(SpUtils.getStringSp(getContext(), "userimgurl")))
                        .error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
//            if (Build.VERSION.SDK_INT > 18)
//                Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "userimgurl")).crossFade().bitmapTransform(new BlurTransformation(getActivity())).into(imgZoom);
            }
            onDownLoad();
        }
    }

    private void initView(View view) {
        scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        llMain = view1.findViewById(R.id.main);
        loadViewForCode();

        scrollView.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                imgHeadBg.setVisibility(View.GONE);
            }

            @Override
            public void onPullZoomEnd() {
            }
        });

        scrollView.getPullRootView().findViewById(R.id.ll_my_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账户余额
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                imgBlance.setVisibility(View.GONE);
//                startActivity(new Intent(getContext(), AccountBalanceActivity.class));
                startActivity(new Intent(getContext(), MyAccountDetailedActivity.class));
            }
        });

        scrollView.getPullRootView().findViewById(R.id.ll_Profit_core).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收益中心
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                imgProfit.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), ProfitCoreActivity.class);
                intent.putExtra("id", SpUtils.getStringSp(getContext(), "userId"));
                startActivity(intent);
            }
        });

//        scrollView.getPullRootView().findViewById(R.id.ll_my_recomment).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                Intent intent = new Intent(getContext(), InvitationFriendsActivity.class);
//                intent.putExtra("scienceNum", SpUtils.getStringSp(getContext(), "scienceNum"));
//                startActivity(intent);
//                //推荐给好友
////                showSharePop();
//            }
//        });

        scrollView.getPullRootView().findViewById(R.id.ll_my_opinion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                //意见反馈
                startActivity(new Intent(getContext(), OpinionFeedbackActivity.class));
            }
        });
//        scrollView.getPullRootView().findViewById(R.id.ll_my_aboutwe).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //关于我们
//                Intent intent = new Intent(getContext(), MyAboutWeActivity.class);
//                intent.putExtra("id", SpUtils.getStringSp(getContext(), "userId"));
//                startActivity(intent);
//            }
//        });

//        scrollView.getPullRootView().findViewById(R.id.rl_my_clearcache).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataCleanManager.clearAllCache(getContext());
//                try {
//                    tvCacheSize.setText(DataCleanManager.getTotalCacheSize(getContext()));
//                    Toast.makeText(getContext(), "清理成功", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
            int mScreenWidth = localDisplayMetrics.widthPixels;
            LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.4235));
            scrollView.setHeaderLayoutParams(localObject);
            imgHeadBg.setVisibility(View.VISIBLE);
        } else {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
            int mScreenWidth = localDisplayMetrics.widthPixels;
            LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.208));
            scrollView.setHeaderLayoutParams(localObject);
        }
    }

    private void loadViewForCode() {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        imgHeadBg = (ImageView) headView.findViewById(R.id.img_head_bg);
        rlRegister = (AutoRelativeLayout) headView.findViewById(R.id.rl_register_login);
        rlUserInformation = (AutoRelativeLayout) headView.findViewById(R.id.rl_user_information);
        imgUserHead = (ImageView) headView.findViewById(R.id.iv_user_head);
        tvNumbers = (TextView) headView.findViewById(R.id.tv_numbers);
        tvUserName = (TextView) headView.findViewById(R.id.tv_user_name);
        tvLogin = (TextView) headView.findViewById(R.id.tv_login);
        tvRegister = (TextView) headView.findViewById(R.id.tv_register);
//        tvMyHomepage = (TextView) headView.findViewById(R.id.tv_my_homepage);
        imgZoom = (ImageView) zoomView.findViewById(R.id.iv_zoom);
//        headView.findViewById(R.id.img_qrcode).setOnClickListener(this);

//        llMyRecomment = (AutoRelativeLayout) contentView.findViewById(R.id.ll_my_recomment);
        llAuthentication = (AutoRelativeLayout) contentView.findViewById(R.id.ll_authentication);
        llSetUp = (AutoRelativeLayout) contentView.findViewById(R.id.ll_set_up);
        llMyBinding = (AutoRelativeLayout) contentView.findViewById(R.id.ll_my_binding);
        llMyShopping = (AutoRelativeLayout) contentView.findViewById(R.id.ll_my_shopping);
        imgProfit = (ImageView) contentView.findViewById(R.id.img_profit);
        imgBlance = (ImageView) contentView.findViewById(R.id.img_blance);


//        if(!TextUtils.isEmpty(tvNumbers.getText().toString())&&!TextUtils.isEmpty(tvUserName.getText().toString())){
//            tvNumbers.setText(SpUtils.getStringSp(getContext(), "STRING_NUMBER"));
//            tvUserName.setText(SpUtils.getStringSp(getContext(),"STRING_NAME"));
//        }
        headView.findViewById(R.id.ll_my_collection).setOnClickListener(this);
        headView.findViewById(R.id.ll_my_download).setOnClickListener(this);
        headView.findViewById(R.id.ll_my_focus).setOnClickListener(this);
        imgUserHead.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
//        llMyRecomment.setOnClickListener(this);
//        imgZoom.setOnClickListener(this);
//        tvMyHomepage.setOnClickListener(this);
        llSetUp.setOnClickListener(this);
        llAuthentication.setOnClickListener(this);
        llMyBinding.setOnClickListener(this);
//        llMyShopping.setOnClickListener(this);

        toHttpUpdateProfit();
    }

    //收益、余额更新提示
    private void toHttpUpdateProfit() {
        Map<String, Object> map = new HashMap<>();
        ApiServerManager.getInstance().getApiServer().getUpdateProfit(map).enqueue(new RetrofitCallBack<UpdateProfitBalance>() {
            @Override
            public void onSuccess(Call<RetrofitResult<UpdateProfitBalance>> call, Response<RetrofitResult<UpdateProfitBalance>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().getBalanceUpdates() == 1) {
                        imgBlance.setVisibility(View.VISIBLE);
                    } else {
                        imgBlance.setVisibility(View.GONE);
                    }
                    if (response.body().getData().getProfitUpdates() == 1) {
                        imgProfit.setVisibility(View.VISIBLE);
                    } else {
                        imgProfit.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<UpdateProfitBalance>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(final View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_my_focus:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                startActivity(new Intent(getContext(), MyFocusActivity.class));
                startActivity(new Intent(getContext(), PurchaseMainActivity.class));
                break;
            case R.id.img_qrcode:
                startActivity(new Intent(getContext(), MyQrCodeActivity.class));
                break;
//            case R.id.tv_my_homepage:
//                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                intent = new Intent(getContext(), HomePageActivity.class);
//                intent.putExtra("userId", SpUtils.getStringSp(getContext(), "userId"));
//                intent.putExtra("userName", SpUtils.getStringSp(getContext(), "userName"));
//                startActivity(intent);
//                break;
            case R.id.iv_user_head:
//                if (TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
//                    startActivity(new Intent(getContext(), MainLoginActivity.class));
//                    return;
//                }
                startActivity(new Intent(getContext(), PersonalDataActivity.class));
                break;
            case R.id.tv_login:
                //点击登录
                startActivity(new Intent(getContext(), MainLoginActivity.class));
//                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.tv_register:
                //注册
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
//            case R.id.iv_zoom:
//                //更换我的背景图
//                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
//                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                        .build(), new AcpListener() {
//                    @Override
//                    public void onGranted() {
//                        showBgPop();
//                    }
//
//                    @Override
//                    public void onDenied(List<String> permissions) {
//                        Toast.makeText(getContext(), "无法选择头像", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            case R.id.ll_my_recomment:
////                Intent intent = new Intent(Intent.ACTION_SEND);
////                intent.setType("text/plain");//所有可以分享文本的app
////                intent.putExtra(Intent.EXTRA_TEXT, "http://www.baidu.com");
////                startActivity(Intent.createChooser(intent, "赛思,一点一点，改变世界"));
////                showSharePop();
//                break;
            case R.id.btn_cancel:
                Util.finishPop(getActivity(), mPopupWindow);
                break;
//            case R.id.tv_exit:
////                tvUserName.setText("登录/注册" );
////                tvNumbers.setText("" );
////                tvUserName.setOnClickListener(this);
//                ((MainActivity) getActivity()).showWaringDialog(
//                        "警告",
//                        "确认删除所有数据并退出吗？",
//                        new BaseActivity.OnDialogButtonClickListener() {
//                            @Override
//                            public void onPositiveButtonClick() {
//                                Util.deleteFiles(Util.getSDCardPath() + "/temp_crop.jpg", Util.getSDCardPath() + "/temp.jpg"
//                                        , Util.getSDCardPath() + "/temp_document.jpg"
//                                        , Util.getSDCardPath() + "/temp_avatar.jpg"
//                                        , Util.getSDCardPath() + "/temp_avatar.png"
//                                        , Util.getSDCardPath() + "/temp_avatar_crop.jpg"
//                                        , Util.getSDCardPath() + "/temp_authentication_avatar.jpg"
//                                        , Util.getSDCardPath() + "/temp_authentication_avatar_crop.jpg");
//                                SpUtils.clearSp(getContext());
//                                SpUtils.putStringSp(getContext(), "access_token", "-" + java.util.UUID.randomUUID().toString().trim().replace("-", ""));
//                                DemoApplication.getInstance().AppExit();
//                            }
//
//                            @Override
//                            public void onNegativeButtonClick() {
//
//                            }
//                        }
//                );
//                break;
            case R.id.ll_set_up:
                startActivity(new Intent(getContext(), MySetUpActivity.class));
                break;
            case R.id.ll_authentication:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                ((MainActivity) getActivity()).showLoadingDialog("请稍候");
                Map<String, Object> map = new HashMap<>();
                map.put("id", SpUtils.getStringSp(getContext(), "userId"));
//                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        ((MainActivity) getActivity()).loadingDismiss();
//                        if (result.getCode() == 200) {
//                            UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
////                            if (user.getAuthentication() == 0) {
////                                startActivity(new Intent(getContext(), AnswerAuthenticationActivity.class));
////                            } else {
//                            Intent intent = new Intent(getContext(), AnswerAuthenticationActivity.class);
//                            intent.putExtra("state", user.getAuthentication());
//                            startActivity(intent);
////                            }
//                        } else {
//                        }
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//                        ((MainActivity) getActivity()).loadingDismiss();
//                    }
//                });
                HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        ((MainActivity) getActivity()).loadingDismiss();
                        if (result.getCode() == 200) {
                            AnswerPeopleDetail answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                            Intent intent = new Intent(getContext(), AnswerAuthenticationActivity.class);
                            intent.putExtra("state", answerPeopleDetail.getAuthenticated());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                        ((MainActivity) getActivity()).loadingDismiss();
                    }
                });
                break;
//            case R.id.ll_focus:
//                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                //关注
//                intent = new Intent(getContext(), MyFollowActivity.class);
//                intent.putExtra("userId", SpUtils.getStringSp(getContext(), "userId"));
//                startActivityForResult(intent, INTO_FOCUS_ACTIVITY);
//                break;
//            case R.id.ll_fans:
//                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                //粉丝
//                intent = new Intent(getContext(), MyFansActivity.class);
//                intent.putExtra("userId", SpUtils.getStringSp(getContext(), "userId"));
//                startActivityForResult(intent, INTO_FANS_ACTIVITY);
//                break;
            case R.id.ll_my_download:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                intent = new Intent(getContext(), MyDownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_collection:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                intent = new Intent(getContext(), MyCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_binding:
                if (!Util.hintLogin((BaseActivity) getActivity())) return;
                intent = new Intent(getContext(), MyNumberBindingActivity.class);
                startActivity(intent);
                break;
//            case R.id.ll_my_shopping:
//                if (!Util.hintLogin((BaseActivity) getActivity())) return;
//                intent = new Intent(getContext(), PurchaseMainActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    private void showBgPop() {
        cropX = imgZoom.getWidth();
        cropY = scrollView.getmHeaderHeight();
        if (bgPopupWindow == null) {
            View view = View.inflate(getContext(), R.layout.select_picture_popup, null);
            view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    Util.finishPop(getActivity(), bgPopupWindow);
                }
            });
            view.findViewById(R.id.picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                    Util.finishPop(getActivity(), bgPopupWindow);
                }
            });
            view.findViewById(R.id.cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(getActivity(), bgPopupWindow);
                }
            });
            bgPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            bgPopupWindow.setContentView(view);
            bgPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            bgPopupWindow.setFocusable(true);
            bgPopupWindow.setOutsideTouchable(true);
            bgPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            bgPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(getActivity(), bgPopupWindow);
                }
            });
            bgPopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            bgPopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    private void openCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = null;
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            try {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(getContext(), "调用相机失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSharePop() {
        if (mPopupWindow == null) {
            View view = View.inflate(getContext(), R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(this);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), mPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llMain, Gravity.BOTTOM, 0, 0);
        }
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        "我正在使用【墨子学堂】，推荐给你",
                        "我正在使用【墨子学堂】，推荐给你，聪明的随身资讯助手，每天只读符合你的口味",
                        null,
                        "http://a.app.qq.com/o/simple.jsp?pkgname=com.yidiankeyan.science"
                        , null);
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        "我正在使用【墨子学堂】，推荐给你",
                        "我正在使用【墨子学堂】，推荐给你，聪明的随身资讯助手，每天只读符合你的口味",
                        null,
                        "http://a.app.qq.com/o/simple.jsp?pkgname=com.yidiankeyan.science", null);
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).shareWeb(
                        SHARE_MEDIA.QQ,
                        "我正在使用【墨子学堂】，推荐给你",
                        "我正在使用【墨子学堂】，推荐给你，聪明的随身资讯助手，每天只读符合你的口味",
                        null,
                        "http://a.app.qq.com/o/simple.jsp?pkgname=com.yidiankeyan.science", null);
            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).shareWeb(
//                        SHARE_MEDIA.SINA,
//                        "我正在使用【墨子】，推荐给你",
//                        "我正在使用【墨子】，推荐给你，聪明的随身资讯助手，每天只读符合你的口味",
//                        null,
//                        "http://a.app.qq.com/o/simple.jsp?pkgname=com.yidiankeyan.science", null);
//            }
//        });
    }

//    private void savePicture(Bitmap bmap, File filepath) {
//        FileOutputStream foutput = null;
//        try {
//            foutput = new FileOutputStream(filepath);
//            bmap.compress(Bitmap.CompressFormat.PNG, 100, foutput);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != foutput) {
//                try {
//                    foutput.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == Activity.RESULT_OK))
            return;
        switch (requestCode) {
            case MyFragment.REQUEST_CODE_PICTURE:
                String picPath = Util.parsePicturePath(getActivity(), data.getData());
                File file = new File(picPath);
                Uri uri = Uri.fromFile(file);
                cropImg(uri);
                break;
            case MyFragment.REQUEST_CODE_CAMERA:
                cropImg(imageUri);
                break;
//            case REQUEST_CODE_CUT:
//                if (new File(Util.getSDCardPath() + "/temp_crop.jpg") != null) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(new File(Util.getSDCardPath() + "/temp_crop.jpg").getAbsolutePath(), null);
//                    if (bitmap != null) {
//                        Bitmap smallBmp = setScaleBitmap(bitmap, 2);
//                        imgZoom.setImageBitmap(smallBmp);
//                        toHttpUpdataBg();
//                    }
//                }
//                break;
        }

    }

    /**
     * 上传北京图片
     */
//    private void toHttpUpdataBg() {
//        ((BaseActivity) getActivity()).showLoadingDialog("请稍后");
//        HttpUtil.ossUpload(getContext(), Util.getSDCardPath() + "/temp_crop.jpg", new HttpUtil.HttpOSSUploadCallBack() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//
//            }
//
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result, String successUrl) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("bgimgurl", successUrl.substring(successUrl.indexOf("/")));
//                SpUtils.putStringSp(getContext(), "bgimgurl", successUrl.substring(successUrl.indexOf("/")));
//                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.UPDATA_USER_INFO, map, new HttpUtil.HttpCallBack() {
//
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        ((BaseActivity) getActivity()).loadingDismiss();
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                ((BaseActivity) getActivity()).loadingDismiss();
//                Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private Bitmap setScaleBitmap(Bitmap photo, int SCALE) {
        if (photo != null) {
            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            //这里缩小了1/2,但图片过大时仍然会出现加载不了,但系统中一个BITMAP最大是在10M左右,我们可以根据BITMAP的大小
            //根据当前的比例缩小,即如果当前是15M,那如果定缩小后是6M,那么SCALE= 15/6
            Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
            //释放原始图片占用的内存，防止out of memory异常发生
            photo.recycle();
            return smallBitmap;
        }
        return null;
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", cropX);
        intent.putExtra("aspectY", cropY);
//        intent.putExtra("outputX", cropX * 2);
//        intent.putExtra("outputY", cropY * 2);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CUT);
    }


    @SuppressWarnings("ResourceType")
    @Subscribe
    public void onEventMainThread(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_USER_LOGOUT:
                rlRegister.setVisibility(View.VISIBLE);
                rlUserInformation.setVisibility(View.GONE);
            {
                DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                int mScreenWidth = localDisplayMetrics.widthPixels;
                LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.208));
                scrollView.setHeaderLayoutParams(localObject);
            }
            tvUserName.setText("");
            tvLogin.setOnClickListener(this);
            imgZoom.setImageResource(R.drawable.bg_navy);
            tvNumbers.setText("");
            imgUserHead.setImageResource(R.drawable.icon_default_avatar);
            break;
            case SystemConstant.ON_USER_INFORMATION:
                rlRegister.setVisibility(View.GONE);
                rlUserInformation.setVisibility(View.VISIBLE);
            {
                DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                int mScreenWidth = localDisplayMetrics.widthPixels;
                LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (localDisplayMetrics.heightPixels * 0.4235));
                scrollView.setHeaderLayoutParams(localObject);
                imgHeadBg.setVisibility(View.VISIBLE);
            }
            tvUserName.setText(SpUtils.getStringSp(getContext(), "userName"));
            tvNumbers.setText("墨子号：" + SpUtils.getStringSp(getContext(), "scienceNum"));
            tvLogin.setOnClickListener(null);
            if (!SpUtils.getStringSp(getContext(), "userimgurl").startsWith("http")) {
                if (!SpUtils.getStringSp(getContext(), "userimgurl").startsWith("/"))
                    SpUtils.putStringSp(getContext(), "userimgurl", "/" + SpUtils.getStringSp(getContext(), "userimgurl"));
            }
            String userAvatar = SpUtils.getStringSp(getContext(), "userimgurl");
            if (userAvatar != null && userAvatar.startsWith("http")) {
                Glide.with(this).load(userAvatar)
                        .error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
            } else {
                Glide.with(this).load(Util.getImgUrl(userAvatar))
                        .error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
            }
//                if (Build.VERSION.SDK_INT > 18)
//                    Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "userimgurl")).asBitmap().transform(new BlurTransformation(getActivity())).into(imgZoom);
            onDownLoad();
//                onDownloadBg();
            break;
//            case SystemConstant.ON_USER_REGISTER_BACK:
//                login();
//                break;
            case SystemConstant.NOTIFY_USER_INFORMATION_CHANGED:
                updataMap = (Map<String, String>) msg.getBody();
                if (TextUtils.isEmpty(updataMap.get("username")) || TextUtils.equals("null", updataMap.get("username")) || TextUtils.equals(updataMap.get("username"), SpUtils.getStringSp(getContext(), "userName"))) {
                    updataMap.remove("username");
                }
//                else {
//                    SpUtils.putStringSp(getContext(), "userName", updataMap.get("userName"));
//                }
                if (TextUtils.equals("null", updataMap.get("gender")) || TextUtils.equals(updataMap.get("gender"), SpUtils.getIntSp(getContext(), "gender") + "")) {
                    updataMap.remove("gender");
                }
//                else {
//                    SpUtils.putIntSp(getContext(), "gender", Integer.parseInt(updataMap.get("gender")));
//                }
                if (TextUtils.isEmpty(updataMap.get("mysign")) || TextUtils.equals("null", updataMap.get("mysign")) || TextUtils.equals(updataMap.get("mysign"), SpUtils.getStringSp(getContext(), "mysign"))) {
                    updataMap.remove("mysign");
                }
                if (TextUtils.isEmpty(updataMap.get("degree")) || TextUtils.equals("null", updataMap.get("degree")) || TextUtils.equals(updataMap.get("degree"), SpUtils.getStringSp(getContext(), "degree"))) {
                    updataMap.remove("degree");
                }
//                else {
//                    SpUtils.putStringSp(getContext(), "degree", updataMap.get("degree"));
//                }
                if (TextUtils.isEmpty(updataMap.get("birthday")) || TextUtils.equals("null", updataMap.get("birthday")) || TextUtils.equals(updataMap.get("birthday"), SpUtils.getStringSp(getContext(), "birthday"))) {
                    updataMap.remove("birthday");
                }
//                else {
//                    SpUtils.putStringSp(getContext(), "birthday", updataMap.get("birthday"));
//                }
                if (TextUtils.isEmpty(updataMap.get("profession")) || TextUtils.equals("null", updataMap.get("profession")) || TextUtils.equals(updataMap.get("profession"), SpUtils.getStringSp(getContext(), "profession"))) {
                    updataMap.remove("profession");
                }
//                else {
//                    SpUtils.putStringSp(getContext(), "profession", updataMap.get("profession"));
//                }
                if (TextUtils.isEmpty(updataMap.get("position")) || TextUtils.equals("null", updataMap.get("position")) || TextUtils.equals(updataMap.get("position"), SpUtils.getStringSp(getContext(), "position"))) {
                    updataMap.remove("position");
                }
//                else {
//                    SpUtils.putStringSp(getContext(), "position", updataMap.get("position"));
//                }
                if (updataMap.size() > 0) {
                    toHttpUpdataInfo();
                }
                break;
        }
    }

    private void toHttpUpdataInfo() {
        ((BaseActivity) getActivity()).showWaringDialog("提示", "是否保存修改的信息?", new BaseActivity.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick() {
                ((BaseActivity) getActivity()).showLoadingDialog("正在上传");
                if (updataMap.get("avatarIsChanged") != null) {
//                    HttpUtil.ossUpload(DemoApplication.applicationContext, Util.getSDCardPath() + "/temp_avatar_crop2.jpg", new HttpUtil.HttpOSSUploadCallBack() {
//                        @Override
//                        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(final PutObjectRequest request, PutObjectResult result, String successUrl) {
//                            updataMap.put("userimgurl", successUrl.substring(successUrl.indexOf("/")));
//                            updataMap.remove("avatarIsChanged");
//                            SpUtils.putStringSp(getContext(), "userimgurl", successUrl.substring(successUrl.indexOf("/")));
//                            HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.UPDATA_USER_INFO, updataMap, new HttpUtil.HttpCallBack() {
//                                @Override
//                                public void successResult(ResultEntity result) throws JSONException {
//                                    ((BaseActivity) getActivity()).loadingDismiss();
//                                    if (result.getCode() == 200) {
//                                        Set<String> keySet = updataMap.keySet();
//                                        Iterator<String> iterator = keySet.iterator();
//                                        while (iterator.hasNext()) {
//                                            String key = iterator.next();
//                                            if (key.equals("gender")) {
//                                                SpUtils.putIntSp(getContext(), "gender", Integer.parseInt(updataMap.get("gender")));
//                                            } else if (key.equals("username")) {
//                                                SpUtils.putStringSp(getContext(), "userName", updataMap.get(key));
//                                                tvUserName.setText(updataMap.get(key));
//                                            } else {
//                                                SpUtils.putStringSp(getContext(), key, updataMap.get(key));
//                                            }
//                                        }
//                                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
//                                        Util.deleteFiles(Util.getSDCardPath() + "/temp_avatar_crop.jpg");
//                                        new File(Util.getSDCardPath() + "/temp_avatar_crop2.jpg").renameTo(new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg"));
//                                        initImage();
//                                    } else {
//                                        Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void errorResult(Throwable ex, boolean isOnCallback) {
//                                    ((BaseActivity) getActivity()).loadingDismiss();
//                                    Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                            ((BaseActivity) getActivity()).loadingDismiss();
//                            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {
                        @Override
                        public void successResult(UploadResultEntity result) throws JSONException {
                            updataMap.put("userimgurl", result.getFileurl());
                            updataMap.remove("avatarIsChanged");
                            SpUtils.putStringSp(getContext(), "userimgurl", result.getFileurl());
                            HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.UPDATA_USER_INFO, updataMap, new HttpUtil.HttpCallBack() {
                                @Override
                                public void successResult(ResultEntity result) throws JSONException {
                                    ((BaseActivity) getActivity()).loadingDismiss();
                                    if (result.getCode() == 200) {
                                        Set<String> keySet = updataMap.keySet();
                                        Iterator<String> iterator = keySet.iterator();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            if (key.equals("gender")) {
                                                SpUtils.putIntSp(getContext(), "gender", Integer.parseInt(updataMap.get("gender")));
                                            } else if (key.equals("username")) {
                                                SpUtils.putStringSp(getContext(), "userName", updataMap.get(key));
                                                tvUserName.setText(updataMap.get(key));
                                            } else {
                                                SpUtils.putStringSp(getContext(), key, updataMap.get(key));
                                            }
                                        }
                                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                        Util.deleteFiles(Util.getSDCardPath() + "/temp_avatar_crop.jpg");
                                        new File(Util.getSDCardPath() + "/temp_avatar_crop2.jpg").renameTo(new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg"));
                                        initImage();
                                    } else {
                                        Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void errorResult(Throwable ex, boolean isOnCallback) {
                                    ((BaseActivity) getActivity()).loadingDismiss();
                                    Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {
                            ((BaseActivity) getActivity()).loadingDismiss();
                            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }, new File(Util.getSDCardPath() + "/temp_avatar_crop2.jpg"));
                } else {
                    HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.UPDATA_USER_INFO, updataMap, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            ((BaseActivity) getActivity()).loadingDismiss();
                            if (result.getCode() == 200) {
                                Set<String> keySet = updataMap.keySet();
                                Iterator<String> iterator = keySet.iterator();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    if (key.equals("gender")) {
                                        SpUtils.putIntSp(getContext(), "gender", Integer.parseInt(updataMap.get("gender")));
                                    } else if (key.equals("username")) {
                                        SpUtils.putStringSp(getContext(), "userName", updataMap.get(key));
                                        tvUserName.setText(updataMap.get(key));
                                    } else {
                                        SpUtils.putStringSp(getContext(), key, updataMap.get(key));
                                    }
                                }
                                Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {
                            ((BaseActivity) getActivity()).loadingDismiss();
                            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNegativeButtonClick() {
                if (updataMap.get("avatarIsChanged") != null) {
                    Util.deleteFiles(Util.getSDCardPath() + "/temp_avatar_crop2.jpg");
                }
            }
        });
    }

    /**
     * 下载背景图片
     */
//    private void onDownloadBg() {
//        DownLoadImageService service = new DownLoadImageService(getContext(),
//                SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "bgimgurl"),
//                new DownLoadImageService.ImageDownLoadCallBack() {
//
//                    @Override
//                    public void onDownLoadSuccess(final File file) {
//                        // 在这里执行图片保存方法
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                CopyFileUtil.copyFile(file, Util.getSDCardPath() + "/temp_crop.jpg", true);
//                                if (Util.fileExisted(new File(Util.getSDCardPath() + "/temp_crop.jpg"))) {
////                                    Glide.with(getActivity()).load(Util.getSDCardPath() + "/temp_crop.jpg")
////                                            .error(R.drawable.icon_default_avatar)
////                                            .placeholder(R.drawable.icon_default_avatar).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
//                                    Bitmap bitmap = BitmapFactory.decodeFile(new File(Util.getSDCardPath() + "/temp_crop.jpg").getAbsolutePath(), null);
//                                    if (bitmap != null) {
//                                        Bitmap smallBmp = setScaleBitmap(bitmap, 2);
//                                        imgZoom.setImageBitmap(smallBmp);
//                                    }
//
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDownLoadFailed() {
//                        // 图片保存失败
//                    }
//                });
//        //启动图片下载线程
//        new Thread(service).start();
//    }

    /**
     * 启动图片下载线程
     */
    private void onDownLoad() {
        String url;
        if (SpUtils.getStringSp(getContext(), "userimgurl") != null && SpUtils.getStringSp(getContext(), "userimgurl").startsWith("http")) {
            url = SpUtils.getStringSp(getContext(), "userimgurl");
        } else {
            url = SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "userimgurl");
        }
        DownLoadImageService service = new DownLoadImageService(getContext(),
                url,
                new DownLoadImageService.ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(final File file) {
                        // 在这里执行图片保存方法
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtils.copyFile(file, Util.getSDCardPath() + "/temp_avatar_crop.jpg", true);
                                if (Util.fileExisted(new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg"))) {
                                    Glide.with(getActivity()).load(Util.getSDCardPath() + "/temp_avatar_crop.jpg")
                                            .error(R.drawable.icon_default_avatar)
                                            .placeholder(R.drawable.icon_default_avatar).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(getContext())).into(imgUserHead);
//                                    if (Build.VERSION.SDK_INT > 18)
//                                        Glide.with(getActivity()).load(SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(getContext(), "userimgurl")).asBitmap().transform(new BlurTransformation(getActivity())).into(imgZoom);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }

//    private void login() {
//        RequestParams params = new RequestParams(SystemConstant.MYURL + "oauth/token?");
//        params.addBodyParameter("client_id", "d8f01567b81b48e396ca730eb04a462a");
//        params.addBodyParameter("client_secret", "rhw2KJIMZLOpraasToJMEwSXvXdXSnKZ");
//        params.addBodyParameter("grant_type", "password");
//        params.addBodyParameter("scope", "read");
//        params.addBodyParameter("username", SpUtils.getStringSp(getContext(), "phone"));
//        params.addBodyParameter("password", SpUtils.getStringSp(getContext(), "password"));
//        Log.e("网络入参", "参数===" + SystemConstant.MYURL + "oauth/token?" + params.toJSONString());
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                RegusterReturn regusterReturn = new Gson().fromJson(result, RegusterReturn.class);
//                SpUtils.putStringSp(getContext(), "access_token", regusterReturn.getAccess_token());
//                SpUtils.putStringSp(getContext(), "refresh_token", regusterReturn.getRefresh_token());
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("access_token", regusterReturn.getAccess_token().toString());
//                HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_USER_INFORMATION, map, new HttpUtil.HttpCallBack() {
//                    @Override
//                    public void successResult(ResultEntity result) throws JSONException {
//                        UserInforMation datas = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
//                        SpUtils.putStringSp(getContext(), "userId", datas.getUserid());
//                        SpUtils.putStringSp(getContext(), "birthday", datas.getBirthday());
//                        SpUtils.putStringSp(getContext(), "userName", datas.getUsername());
//                        SpUtils.putStringSp(getContext(), "userimgurl", datas.getUserimgurl());
//                        SpUtils.putStringSp(getContext(), "scienceNum", datas.getSizeid() + "");
//                        SpUtils.putStringSp(getContext(), "profession", datas.getProfession());
//                        SpUtils.putStringSp(getContext(), "position", datas.getPosition());
//                        SpUtils.putStringSp(getContext(), "school", datas.getSchool());
//                        SpUtils.putStringSp(getContext(), "phone", datas.getPhone());
//                        SpUtils.putStringSp(getContext(), "email", datas.getEmail());
//                        SpUtils.putStringSp(getContext(), "address", datas.getAddress());
//                        SpUtils.putIntSp(getContext(), "isBoundQQ", datas.getIsBoundQQ());
//                        SpUtils.putIntSp(getContext(), "isBoundWeChat", datas.getIsBoundWeChat());
//                        SpUtils.putIntSp(getContext(), "isBountWeiBo", datas.getIsBountWeiBo());
//                        SpUtils.putIntSp(getContext(), "gender", datas.getGender());
//                        SpUtils.putIntSp(getContext(), "saisiProfession", datas.getSaisiProfession());
//                        SpUtils.putStringSp(getContext(), "specialty", datas.getSpecialty());
//                        SpUtils.putBooleanSp(getContext(), SystemConstant.APP_IS_FIRST_START, true);
//                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
//                        EventBus.getDefault().post(msg);
//                    }
//
//                    @Override
//                    public void errorResult(Throwable ex, boolean isOnCallback) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
