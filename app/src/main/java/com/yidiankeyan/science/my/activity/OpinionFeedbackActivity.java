package com.yidiankeyan.science.my.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.FeedbackListActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
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
 * 我的
 * -建议反馈
 */
public class OpinionFeedbackActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageView imgUploadCamera;
    private PopupWindow opinionPopupWindow;
    public static final int REQUEST_CODE_PICTURE = 101;
    public static final int REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_CUT = 103;
    private AutoLinearLayout llOpinionFeed;
    private Uri imageDocumentUri;
    private boolean avatarIsChanged;
    private EditText edtAddContent;
    //    private RadioGroup rgProductType;
//    private RadioButton rbTypeOne, rbTypeTwo;
    private boolean isDocument;
    private String documentPath;
    private File file;
    private InputMethodManager imm;
    private TextView mTvRight;
    private int num = 200;
    private TextView mTvSubmitNext;

    @Override
    protected int setContentView() {
        return R.layout.activity_opinion_feedback;
    }

    @Override
    protected void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        imgUploadCamera = (ImageView) findViewById(R.id.img_upload_camera);
        llOpinionFeed = (AutoLinearLayout) findViewById(R.id.ll_opinion_feed);
        edtAddContent = (EditText) findViewById(R.id.edt_add_content);
        mTvSubmitNext = (TextView) findViewById(R.id.tv_submit_next);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("建议反馈");
        llReturn.setOnClickListener(this);
        llOpinionFeed.setOnClickListener(this);
        File documentFile = new File(Util.getSDCardPath() + "/temp_document.jpg");
        imageDocumentUri = Util.getUriForFile(this, documentFile);
        imgUploadCamera.setOnClickListener(this);
        mTvSubmitNext.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        edtAddContent.addTextChangedListener(new TextWatcher() {

            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = edtAddContent.getSelectionStart();
                selectionEnd = edtAddContent.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edtAddContent.setText(s);
                    edtAddContent.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
//                Util.deleteFiles(Util.getSDCardPath() + "/temp_opinion.jpg", Util.getSDCardPath() + "/temp_opinion_crop.jpg");
                imgUploadCamera.setImageResource(R.drawable.icon_upload_feedback);
                edtAddContent.setText("");
//                rbTypeOne.setChecked(true);
                finish();
                break;
            case R.id.ll_opinion_feed:
                imm.hideSoftInputFromWindow(edtAddContent.getWindowToken(), 0);
                break;
            case R.id.tv_right:
                imm.hideSoftInputFromWindow(edtAddContent.getWindowToken(), 0);
//                ToastMaker.showShortToast("后台接口等待中...");
                Intent  intent = new Intent(OpinionFeedbackActivity.this, FeedbackListActivity.class);
                startActivity(intent);
                break;
            case R.id.img_upload_camera:
                imm.hideSoftInputFromWindow(edtAddContent.getWindowToken(), 0);
                //点击选择照相或者相册功能
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
                        Toast.makeText(OpinionFeedbackActivity.this, "无法选择图片", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.tv_submit_next:
                imm.hideSoftInputFromWindow(edtAddContent.getWindowToken(), 0);
                if (!TextUtils.isEmpty(edtAddContent.getText().toString().trim())) {
                    final Map<String, Object> map = new HashMap<>();
                    map.put("title", "");
                    map.put("content", edtAddContent.getText().toString().trim());
                    if (avatarIsChanged) {
                        showLoadingDialog("请稍后");
                        HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

                            @Override
                            public void successResult(UploadResultEntity result) throws JSONException {
                                map.put("imgurl", result.getFileurl());
                                HttpUtil.post(OpinionFeedbackActivity.this, SystemConstant.URL + SystemConstant.MY_OPINION_FEEDBACK, map, new HttpUtil.HttpCallBack() {
                                    @Override
                                    public void successResult(ResultEntity result) throws JSONException {
                                        loadingDismiss();
                                        if (result.getCode() == 200) {
                                            Toast.makeText(DemoApplication.applicationContext, "提交成功", Toast.LENGTH_SHORT).show();
                                            imgUploadCamera.setImageResource(R.drawable.icon_upload_feedback);
                                            edtAddContent.setText("");
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
//                            loadingDismiss();
//                            Toast.makeText(DemoApplication.applicationContext, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }, new File(documentPath));
                    } else {
                        HttpUtil.post(OpinionFeedbackActivity.this, SystemConstant.URL + SystemConstant.MY_OPINION_FEEDBACK, map, new HttpUtil.HttpCallBack() {
                            @Override
                            public void successResult(ResultEntity result) throws JSONException {
                                loadingDismiss();
                                if (result.getCode() == 200) {
                                    Toast.makeText(DemoApplication.applicationContext, "提交成功", Toast.LENGTH_SHORT).show();
                                    imgUploadCamera.setImageResource(R.drawable.icon_upload_feedback);
                                    edtAddContent.setText("");
                                } else {
                                    Toast.makeText(DemoApplication.applicationContext, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void errorResult(Throwable ex, boolean isOnCallback) {
                                loadingDismiss();
                                Toast.makeText(DemoApplication.applicationContext, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
//                    Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
//                    rbTypeOne.setChecked(true);
                } else {
                    Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void card() {
//        filepath = new File(Environment.getExternalStorageDirectory(), "temp_avatar" + ".png");
        if (opinionPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_picture_popup, null);
            view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    Util.finishPop(OpinionFeedbackActivity.this, opinionPopupWindow);
                }
            });
            view.findViewById(R.id.picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                    Util.finishPop(OpinionFeedbackActivity.this, opinionPopupWindow);
                }
            });
            view.findViewById(R.id.cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(OpinionFeedbackActivity.this, opinionPopupWindow);
                }
            });
            opinionPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            opinionPopupWindow.setContentView(view);
            opinionPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            opinionPopupWindow.setFocusable(true);
            opinionPopupWindow.setOutsideTouchable(true);
            opinionPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            opinionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(OpinionFeedbackActivity.this, opinionPopupWindow);
                }
            });
            opinionPopupWindow.showAtLocation(llOpinionFeed, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            opinionPopupWindow.showAtLocation(llOpinionFeed, Gravity.BOTTOM, 0, 0);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    private String path;

    private void openCamera() {
        path = Util.getSDCardPath() + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        imageDocumentUri = Util.getUriForFile(this, file);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageDocumentUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, PersonalDataActivity.REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        documentPath = handleImageOnKitKat(data);
                    } else {
                        documentPath = handleImageBeforeKitKat(data);
                    }
                    avatarIsChanged = true;
                    Glide.with(this).load(documentPath).skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgUploadCamera);
                }
                break;
            case REQUEST_CODE_CAMERA:
//                if (resultCode == Activity.RESULT_OK) {
//                    cropImg(imageUri);
//                }

                if (resultCode == Activity.RESULT_OK) {
                    if (isDocument) {
                        avatarIsChanged = true;
                        documentPath = path;
                        Glide.with(this).load(documentPath).skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgUploadCamera);
                    }
                }
                break;
        }
    }

    @TargetApi(19)
    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        imageDocumentUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageDocumentUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageDocumentUri);
            if ("com.android.providers.media.documents".equals(imageDocumentUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageDocumentUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageDocumentUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageDocumentUri, null);
        } else if ("file".equalsIgnoreCase(imageDocumentUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageDocumentUri.getPath();
        }
        return imagePath;
    }

    private String handleImageBeforeKitKat(Intent intent) {
        imageDocumentUri = intent.getData();
        return getImagePath(imageDocumentUri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @Override
    protected void onDestroy() {
        imgUploadCamera.setImageResource(R.drawable.icon_upload_feedback);
        edtAddContent.setText("");
        super.onDestroy();
    }

}
