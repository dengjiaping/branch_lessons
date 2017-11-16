package com.yidiankeyan.science.information.acitivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.MyDragAdapter;
import com.yidiankeyan.science.information.adapter.MyOtherAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.my.activity.AnswerAuthenticationActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.yidiankeyan.science.view.drag.adapter.MyDragGrid;
import com.yidiankeyan.science.view.drag.bean.ChannelItem;
import com.yidiankeyan.science.view.drag.view.OtherGridView;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

public class CreateKedaUserActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView tvKedaUsername;
    private ImageView imgAvatar;
    private EditText etName;
    private EditText etProfession;
    //    private EditText etDesc;
    private MyDragGrid userGridView;
    private OtherGridView otherGridView;
    private EditText etPrice;
    private Button btnNext;
    private View rootView;
    private TextView tvIntroduce;
    private AutoRelativeLayout rlPersonality;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    MyDragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    MyOtherAdapter otherAdapter;
    /**
     * 未选择的方向列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /**
     * 已选择的方向列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    private PopupWindow avatarPopupWindow;
    public static final int REQUEST_CODE_PICTURE = 101;
    public static final int REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_CUT = 103;
    private Uri imageUri;
    private Uri imageCropUri;
    private boolean avatarIsChanged;
    private AnswerPeopleDetail answerPeopleDetail;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_create_keda_user;
    }

    @Override
    protected void initView() {
        tvKedaUsername = (TextView) findViewById(R.id.tv_keda_username);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        etName = (EditText) findViewById(R.id.et_name);
        etProfession = (EditText) findViewById(R.id.et_profession);
//        etDesc = (EditText) findViewById(R.id.et_desc);
        userGridView = (MyDragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        etPrice = (EditText) findViewById(R.id.et_price);
        btnNext = (Button) findViewById(R.id.btn_next);
        rootView = findViewById(R.id.activity_create_keda_user);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        rlPersonality = (AutoRelativeLayout) findViewById(R.id.rl_personality_autograph);
    }

    @Override
    protected void initAction() {
        ((TextView) findViewById(R.id.maintitle_txt)).setText("编辑资料");
        findViewById(R.id.ll_return).setOnClickListener(this);

        imgAvatar.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        rlPersonality.setOnClickListener(this);
        if (DemoApplication.answerAlbumMap.size() == 0) {
            toHttpGetAnswerAlbum();
        } else {
            initDomains();
        }

        String path = Util.getSDCardPath();
        File file = new File(path + "/temp_keda_avatar.jpg");
        imageUri = Uri.fromFile(file);
        imageCropUri = Uri.fromFile(new File(Util.getSDCardPath() + "/temp_keda_avatar_crop.jpg"));
    }

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    AnswerPeopleDetail answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);


                    if (TextUtils.isEmpty(answerPeopleDetail.getCoverimg()) && TextUtils.equals("null", answerPeopleDetail.getCoverimg())) {
                        imgAvatar.setImageResource(R.drawable.icon_default_avatar);
                    } else {
//                        Glide.with(CreateKedaUserActivity.this).load(answerPeopleDetail.getCoverimg() + "/temp_avatar_crop.jpg").skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(CreateKedaUserActivity.this)).into(imgAvatar);
                        Glide.with(CreateKedaUserActivity.this).load(Util.getImgUrl(answerPeopleDetail.getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext))
                                .placeholder(R.drawable.icon_default_avatar)
                                .error(R.drawable.icon_default_avatar)
                                .into(imgAvatar);
                    }

                    if (TextUtils.isEmpty(answerPeopleDetail.getName()) && TextUtils.equals("null", answerPeopleDetail.getName())) {
                        tvKedaUsername.setText("头像");
                    } else {
                        tvKedaUsername.setText(answerPeopleDetail.getName());
                    }
                    if (TextUtils.isEmpty(answerPeopleDetail.getName()) && TextUtils.equals("null", answerPeopleDetail.getName())) {
                        etName.setText("");
                    } else {
                        etName.setText(answerPeopleDetail.getName());
                    }

                    if (TextUtils.isEmpty(answerPeopleDetail.getProfession()) && TextUtils.equals("null", answerPeopleDetail.getProfession())) {
                        etProfession.setText("");
                    } else {
                        etProfession.setText(answerPeopleDetail.getProfession());
                    }
                    if (TextUtils.isEmpty(answerPeopleDetail.getSign()) && TextUtils.equals("null", answerPeopleDetail.getSign())) {
                        tvIntroduce.setText("");
                    } else {
                        tvIntroduce.setText(answerPeopleDetail.getSign());
                    }
                    if (answerPeopleDetail.getTotalincome() == 0) {
                        etPrice.setText("");
                    } else {
                        etPrice.setText(answerPeopleDetail.getTotalincome() + "");
                    }

                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    private void initDomains() {
        if (getIntent().getParcelableArrayListExtra("userList") == null) {
            for (Map.Entry<Integer, AnswerAlbumBean> entry : DemoApplication.answerAlbumMap.entrySet()) {
                otherChannelList.add(new ChannelItem(entry.getKey(), entry.getValue().getKdname(), otherChannelList.size() + 1, 0));
            }
        } else {
            toHttpGetDetail();
            ArrayList<ChannelItem> list = getIntent().getParcelableArrayListExtra("userList");
            userChannelList.addAll(list);
            for (Map.Entry<Integer, AnswerAlbumBean> entry : DemoApplication.answerAlbumMap.entrySet()) {
                boolean flag = true;
                for (ChannelItem channel : userChannelList) {
                    if (entry.getKey() == channel.getSubjectid()) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    otherChannelList.add(new ChannelItem(entry.getKey(), entry.getValue().getKdname(), otherChannelList.size() + 1, 0));
            }
        }
        userAdapter = new MyDragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new MyOtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
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
                    initDomains();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<AnswerAlbumBean>>> call, Throwable t) {

            }
        });
    }


    /**
     * @param msg
     */
    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_KEDA_INTRODUCTION:
                tvIntroduce.setText(msg.getBody().toString() + "");
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                //点击头像选择照相或者相册功能
                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        card();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(CreateKedaUserActivity.this, "无法选择头像", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.ll_return:
                finish();
                break;
            case R.id.rl_personality_autograph:
                Intent intent = new Intent(this, KedaUserSignActivity.class);
                intent.putExtra("preservation", tvIntroduce.getText().toString());
                startActivity(intent);
                break;
            case R.id.btn_next:
                if (!avatarIsChanged && !getIntent().getBooleanExtra("edit", false)) {
                    //如果当前处于新建科答用户且头像没有设置则提示必须设置头像
                    ToastMaker.showShortToast("请设置头像");
                    return;
                } else if (TextUtils.isEmpty(etName.getText())) {
                    ToastMaker.showShortToast("请设置名字");
                    return;
                } else if (TextUtils.isEmpty(etProfession.getText())) {
                    ToastMaker.showShortToast("请设置职业身份");
                    return;
                } else if (TextUtils.isEmpty(tvIntroduce.getText())) {
                    ToastMaker.showShortToast("请设置简介");
                    return;
                } else if (TextUtils.isEmpty(etPrice.getText())) {
                    ToastMaker.showShortToast("请设置润喉费");
                    return;
                } else if (userChannelList.size() == 0) {
                    ToastMaker.showShortToast("请选择方向");
                    return;
                }
                registerKeda();
                break;
        }
    }

    private void registerKeda() {
        showLoadingDialog("请稍后");
        if (avatarIsChanged) {
            //头像改变了上传头像
            HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

                @Override
                public void successResult(UploadResultEntity result) throws JSONException {
                    if (result.getCode() == 1) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("coverimg", result.getFileurl());
                        map.put("profession", etProfession.getText().toString());
                        map.put("sign", tvIntroduce.getText().toString());
                        map.put("name", etName.getText().toString());
                        SpUtils.putStringSp(CreateKedaUserActivity.this, "kedaName", etName.getText().toString());
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < userChannelList.size(); i++) {
                            if (i == userChannelList.size() - 1) {
                                sb.append(userChannelList.get(i).getId());
                            } else {
                                sb.append(userChannelList.get(i).getId() + ",");
                            }
                        }
                        map.put("domain", sb.toString());
                        map.put("mouthprice", etPrice.getText().toString());
                        ApiServerManager.getInstance().getApiServer().updateKedaUserInfo(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                loadingDismiss();
                                if (response.body().getCode() == 200) {
                                    if (getIntent().getBooleanExtra("edit", false)) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(CreateKedaUserActivity.this, AnswerAuthenticationActivity.class);
                                        intent.putExtra("state", 0);
                                        startActivity(intent);
                                    }
                                } else {
                                    ToastMaker.showShortToast("更新失败");
                                }
                            }

                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                                loadingDismiss();
                                ToastMaker.showShortToast("更新失败");
                            }
                        });
                    } else {
                        loadingDismiss();
                        ToastMaker.showShortToast("更新失败");
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                    loadingDismiss();
                    ToastMaker.showShortToast("更新失败");
                }
            }, new File(Util.getSDCardPath() + "/temp_keda_avatar_crop.jpg"));
        } else {
            //头像没有改动，直接上传其他资料
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("profession", etProfession.getText().toString());
            map.put("sign", tvIntroduce.getText().toString());
            map.put("name", etName.getText().toString());
            SpUtils.putStringSp(this, "kedaName", etName.getText().toString());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < userChannelList.size(); i++) {
                if (i == userChannelList.size() - 1) {
                    sb.append(userChannelList.get(i).getId());
                } else {
                    sb.append(userChannelList.get(i).getId() + ",");
                }
            }
            map.put("domain", sb.toString());
            map.put("mouthprice", etPrice.getText().toString());
            ApiServerManager.getInstance().getApiServer().updateKedaUserInfo(map).enqueue(new RetrofitCallBack<Object>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                    loadingDismiss();
                    if (response.body().getCode() == 200) {
                        if (getIntent().getBooleanExtra("edit", false)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Intent intent = new Intent(CreateKedaUserActivity.this, AnswerAuthenticationActivity.class);
                            intent.putExtra("state", 0);
                            startActivity(intent);
                        }
                    } else {
                        ToastMaker.showShortToast("更新失败");
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                    loadingDismiss();
                    ToastMaker.showShortToast("更新失败");
                }
            });
        }
    }

    public void card() {
        if (avatarPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_picture_popup, null);
            view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    Util.finishPop(CreateKedaUserActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                    Util.finishPop(CreateKedaUserActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(CreateKedaUserActivity.this, avatarPopupWindow);
                }
            });
            avatarPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            avatarPopupWindow.setContentView(view);
            avatarPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            avatarPopupWindow.setFocusable(true);
            avatarPopupWindow.setOutsideTouchable(true);
            avatarPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            avatarPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(CreateKedaUserActivity.this, avatarPopupWindow);
                }
            });
            avatarPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            avatarPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    private void openCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = null;
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            try {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(this, "调用相机失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    String picPath = Util.parsePicturePath(this, data.getData());
                    File file = new File(picPath);
                    Uri uri = Uri.fromFile(file);
                    cropImg(uri);
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    cropImg(imageUri);
                }
                break;
            case REQUEST_CODE_CUT:
                if (resultCode == Activity.RESULT_OK) {
                    if (getTempFile() != null) {
                        avatarIsChanged = true;
                        Glide.with(this).load(imageCropUri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(this)).into(imgAvatar);
                    }
                }
                break;
        }
    }

    private File getTempFile() {
        try {
            return new File(Util.getSDCardPath() + "/temp_keda_avatar_crop.jpg");
        } catch (Exception e) {
        }
        return null;
    }

    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", imgAvatar.getHeight());
        intent.putExtra("aspectY", imgAvatar.getHeight());
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CUT);
    }

    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof MyDragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
//                if (llExpand.getTag().equals(0)) {
//                    otherGridView.setVisibility(View.VISIBLE);
//                    llExpand.setTag(1);
//                }
                //position为 0，1 的不可以进行任何操作
                if (true) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((MyDragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        //添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                if (userAdapter.getCount() > 4) {
                    Toast.makeText(this, "最多选择五个方向", Toast.LENGTH_SHORT).show();
                    break;
                }
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((MyOtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
