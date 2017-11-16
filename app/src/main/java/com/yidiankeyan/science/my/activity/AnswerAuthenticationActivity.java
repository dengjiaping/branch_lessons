package com.yidiankeyan.science.my.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.CreateKedaUserActivity;
import com.yidiankeyan.science.information.acitivity.WithoutAnswerActivity;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 答人认证
 */
public class AnswerAuthenticationActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private AutoRelativeLayout llAll;
    private PopupWindow avatarPopupWindow;
    public static final int REQUEST_CODE_PICTURE = 101;
    public static final int REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_CUT = 103;
    private Uri imageDocumentUri;
    private Uri imageEpUri;
    private AutoRelativeLayout rlDocuments;
    private ImageView imgAmera;
    private TextView tvDes;
    private boolean isDocument;
    private ImageView imgDocument;
    private Button btnUpload;
    private String documentPath;
    private String epPath;
    private AutoRelativeLayout llSex;
    private TextView tvSexMale;
    private PopupWindow sexPopupWindow;
    private EditText tvMyName;
    private EditText tvDocumentNum;
    private int gender;
    private TextView tvState;
    private TextView tvAuthenPrompt;
    private AutoLinearLayout llContainer;
    private int state;
    private boolean epSetup;
    private boolean authenticctionSetup;
    private AutoRelativeLayout rlEp;
    private ImageView imgEp;
    private ImageView imgEpAmera;
    private TextView tvEpDes;

    @Override
    protected int setContentView() {
        return R.layout.activity_answer_authentication;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        llAll = (AutoRelativeLayout) findViewById(R.id.ll_all);
        rlDocuments = (AutoRelativeLayout) findViewById(R.id.rl_documents);
        imgAmera = (ImageView) findViewById(R.id.img_amera);
        tvDes = (TextView) findViewById(R.id.tv_des);
        imgDocument = (ImageView) findViewById(R.id.img_document);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        llSex = (AutoRelativeLayout) findViewById(R.id.ll_sex);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvAuthenPrompt = (TextView) findViewById(R.id.tv_authen_prompt);
        llContainer = (AutoLinearLayout) findViewById(R.id.ll_container);

        tvSexMale = (TextView) findViewById(R.id.tv_sex_male);
        tvMyName = (EditText) findViewById(R.id.tv_my_name);
        tvDocumentNum = (EditText) findViewById(R.id.tv_document_num);

        rlEp = (AutoRelativeLayout) findViewById(R.id.rl_ep);
        imgEp = (ImageView) findViewById(R.id.img_ep);
        imgEpAmera = (ImageView) findViewById(R.id.img_ep_amera);
        tvEpDes = (TextView) findViewById(R.id.tv_ep_des);
    }

    @Override
    protected void initAction() {
        state = getIntent().getIntExtra("state", 0);
        if (state == 1) {
            tvState.setVisibility(View.VISIBLE);
            tvAuthenPrompt.setVisibility(View.VISIBLE);
            llContainer.setVisibility(View.GONE);
        } else if (state == 2) {
            tvState.setVisibility(View.VISIBLE);
            tvAuthenPrompt.setVisibility(View.GONE);
            llContainer.setVisibility(View.GONE);
            tvState.setText("认证已通过");
        } else if (state == 3) {
            Toast.makeText(this, "认证失败，请重新认证", Toast.LENGTH_SHORT).show();
        }
        txtTitle.setText("认证审核");

        switch (SpUtils.getIntSp(this, "gender")) {
            case 0:
                tvSexMale.setText("保密");
                break;
            case 1:
                tvSexMale.setText("男");
                break;
            case 2:
                tvSexMale.setText("女");
                break;
        }
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        rlEp.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        llSex.setOnClickListener(this);
        File documentFile = new File(Util.getSDCardPath() + "/temp_document.jpg");
        imageDocumentUri = Uri.fromFile(documentFile);
        File epFile = new File(Util.getSDCardPath() + "/temp_ep.jpg");
        imageEpUri = Uri.fromFile(epFile);
        rlDocuments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.rl_documents:
                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        isDocument = true;
                        card();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(AnswerAuthenticationActivity.this, "无法选择图片", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.rl_ep:
                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        isDocument = false;
                        card();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(AnswerAuthenticationActivity.this, "无法选择图片", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_upload:
                if (TextUtils.isEmpty(tvMyName.getText().toString()) || TextUtils.equals("null", tvMyName.getText().toString())) {
                    Toast.makeText(DemoApplication.applicationContext, "请填写您的名字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(tvSexMale.getText().toString()) || TextUtils.equals("null", tvSexMale.getText().toString())) {
                    Toast.makeText(DemoApplication.applicationContext, "请选择您的性别", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(tvDocumentNum.getText().toString()) || TextUtils.equals("null", tvDocumentNum.getText().toString())) {
                    Toast.makeText(DemoApplication.applicationContext, "请填写您的身份证号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!authenticctionSetup) {
                    Toast.makeText(DemoApplication.applicationContext, "请上传您的身份证照片", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!epSetup) {
                    Toast.makeText(DemoApplication.applicationContext, "请上传您的工作证照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingDialog("请稍候");
                upload();
                break;
            case R.id.ll_sex:
                showSexPopupWindow();
                break;
        }
    }

    private void upload() {
        final Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("name", tvMyName.getText().toString());
        map.put("gender", gender);
        map.put("idnumber", tvDocumentNum.getText().toString());
        //上传身份证
        HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {
            @Override
            public void successResult(UploadResultEntity result) throws JSONException {
//                Log.e("url=", result.getFileurl());
//                map.put("epcardimg", result.getFileurl());
                map.put("idcardimg", result.getFileurl());
                //上传工作证
                HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

                    @Override
                    public void successResult(UploadResultEntity result) throws JSONException {
                        map.put("epcardimg", result.getFileurl());
                        //提交资料
                        HttpUtil.post(AnswerAuthenticationActivity.this, SystemConstant.URL + SystemConstant.ANSWER_AUTHENTICATION, map, new HttpUtil.HttpCallBack() {
                            @Override
                            public void successResult(ResultEntity result) throws JSONException {
                                loadingDismiss();
                                if (result.getCode() == 200) {
                                    Toast.makeText(DemoApplication.applicationContext, "上传成功", Toast.LENGTH_SHORT).show();
//                                            finish();
                                    btnUpload.setText("认证中");
                                    btnUpload.setEnabled(false);
                                    DemoApplication.getInstance().finishActivity(CreateKedaUserActivity.class);
                                    DemoApplication.getInstance().finishActivity(WithoutAnswerActivity.class);
                                } else {
                                    Toast.makeText(DemoApplication.applicationContext, "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void errorResult(Throwable ex, boolean isOnCallback) {
                                loadingDismiss();
                                Toast.makeText(DemoApplication.applicationContext, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {
                        loadingDismiss();
                        Toast.makeText(DemoApplication.applicationContext, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }, new File(epPath));
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                Toast.makeText(DemoApplication.applicationContext, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }, new File(documentPath));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    /**
     * 弹出选择性别
     */
    private void showSexPopupWindow() {
        if (sexPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_sex_popup, null);
            view.findViewById(R.id.man_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //男
                    gender = 1;
                    tvSexMale.setText("男");
                    Util.finishPop(AnswerAuthenticationActivity.this, sexPopupWindow);
                }
            });
            view.findViewById(R.id.girl_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //女
                    gender = 2;
                    tvSexMale.setText("女");
                    Util.finishPop(AnswerAuthenticationActivity.this, sexPopupWindow);
                }
            });
            view.findViewById(R.id.tv_secrecy_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保密
                    gender = 0;
                    tvSexMale.setText("保密");
                    Util.finishPop(AnswerAuthenticationActivity.this, sexPopupWindow);
                }
            });
            view.findViewById(R.id.sex_cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AnswerAuthenticationActivity.this, sexPopupWindow);
                }
            });
            sexPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            sexPopupWindow.setContentView(view);
            sexPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            sexPopupWindow.setFocusable(true);
            sexPopupWindow.setOutsideTouchable(true);
            sexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sexPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(AnswerAuthenticationActivity.this, sexPopupWindow);
                }
            });
            sexPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            sexPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
    }

    public void card() {
//        filepath = new File(Environment.getExternalStorageDirectory(), "temp_avatar" + ".png");
        if (avatarPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_picture_popup, null);
            view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    Util.finishPop(AnswerAuthenticationActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                    Util.finishPop(AnswerAuthenticationActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AnswerAuthenticationActivity.this, avatarPopupWindow);
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
                    Util.finishPop(AnswerAuthenticationActivity.this, avatarPopupWindow);
                }
            });
            avatarPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            avatarPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
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
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            if (isDocument) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageDocumentUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageEpUri);
            }
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
                    if (isDocument) {
                        authenticctionSetup = true;
                        documentPath = picPath;
                        Glide.with(this).load(file).skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDocument);
                        imgAmera.setVisibility(View.GONE);
                        tvDes.setVisibility(View.GONE);
                    } else {
                        epSetup = true;
                        epPath = picPath;
                        Glide.with(this).load(file).skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgEp);
                        imgEpAmera.setVisibility(View.GONE);
                        tvEpDes.setVisibility(View.GONE);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (isDocument) {
                        authenticctionSetup = true;
                        documentPath = Util.getSDCardPath() + "/temp_document.jpg";
                        Glide.with(this).load(Util.getSDCardPath() + "/temp_document.jpg").skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDocument);
                        imgAmera.setVisibility(View.GONE);
                        tvDes.setVisibility(View.GONE);
                    } else {
                        epSetup = true;
                        epPath = Util.getSDCardPath() + "/temp_ep.jpg";
                        Glide.with(this).load(Util.getSDCardPath() + "/temp_ep.jpg").skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgEp);
                        imgEpAmera.setVisibility(View.GONE);
                        tvEpDes.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }


}
