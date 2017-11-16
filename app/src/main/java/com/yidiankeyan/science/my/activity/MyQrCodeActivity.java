package com.yidiankeyan.science.my.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.QRCodeUtil;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 我的二维码
 */
public class MyQrCodeActivity extends BaseActivity {

    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvScienceNumber;
    private ImageView imgQrCode;
    private String filePath;
    private View root;
    private TextView tvSaveCode;
    private AutoLinearLayout llCodeImg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                imgQrCode.setImageBitmap(BitmapFactory.decodeFile(filePath));
            }
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_my_qr_code;
    }

    @Override
    protected void initView() {
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvScienceNumber = (TextView) findViewById(R.id.tv_science_number);
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        root = findViewById(R.id.activity_mu_qr_code);
        llCodeImg = (AutoLinearLayout) findViewById(R.id.ll_code_img);
        tvSaveCode = (TextView) findViewById(R.id.tv_save_code);
    }

    @Override
    protected void initAction() {
        ((ImageView) findViewById(R.id.title_return)).setImageResource(R.drawable.receive_prize_return);
        ((ImageView) findViewById(R.id.img_audio_more)).setImageResource(R.drawable.icon_white_more);
        ((TextView) findViewById(R.id.maintitle_txt)).setText("我的二维码");
        findViewById(R.id.ll_audio_more).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_audio_more).setOnClickListener(this);
        tvSaveCode.setVisibility(View.VISIBLE);
//        Glide.with(this).load(Util.getImgUrl(SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl"))).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
//                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .build(), new AcpListener() {
//                            @Override
//                            public void onGranted() {
//                                filePath = Util.getSDCardPath() + "/MOZDownloads/qr_code.jpg";
//                                try {
//                                    File dir = new File(Util.getSDCardPath() + "/MOZDownloads/");
//                                    if (!dir.exists()) {
//                                        dir.mkdirs();
//                                    }
//                                    new File(filePath).createNewFile();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onDenied(List<String> permissions) {
//                                ToastMaker.showShortToast("有部分功能将无法使用");
//                            }
//                        });
//                        return false;
//                    }
//                })
//                .into(imgAvatar);
        Glide.with(this).load(Util.getImgUrl(SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl"))).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        BitmapPool mBitmapPool = Glide.get(MyQrCodeActivity.this).getBitmapPool();
                        int size = Math.min(resource.getWidth(), resource.getHeight());

                        int width = (resource.getWidth() - size) / 2;
                        int height = (resource.getHeight() - size) / 2;

                        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
                        if (bitmap == null) {
                            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                        }

                        Canvas canvas = new Canvas(bitmap);
                        Paint paint = new Paint();
                        BitmapShader shader =
                                new BitmapShader(resource, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                        if (width != 0 || height != 0) {
                            // resource isn't square, move viewport to center
                            Matrix matrix = new Matrix();
                            matrix.setTranslate(-width, -height);
                            shader.setLocalMatrix(matrix);
                        }
                        paint.setShader(shader);
                        paint.setAntiAlias(true);
                        float r = size / 2f;
                        canvas.drawCircle(r, r, r, paint);

                        final Bitmap source = BitmapResource.obtain(bitmap, mBitmapPool).get();
                        imgAvatar.setImageBitmap(source);
                        Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                filePath = Util.getSDCardPath() + "/MOZDownloads/qr_code.jpg";
                                try {
                                    File dir = new File(Util.getSDCardPath() + "/MOZDownloads/");
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    new File(filePath).createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (source == null) {
                                            return;
                                        }
                                        boolean success = QRCodeUtil.createQRImage("墨子号" + SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"), (int) (SystemConstant.ScreenWidth * 0.613), (int) (SystemConstant.ScreenWidth * 0.613),
                                                source, filePath);
                                        if (success) {
                                            mHandler.sendEmptyMessage(0);
                                        } else {

                                        }
                                    }
                                }).start();
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastMaker.showShortToast("有部分功能将无法使用");
                            }
                        });
                    }
                });
        tvName.setText(SpUtils.getStringSp(DemoApplication.applicationContext, "userName"));
        tvScienceNumber.setText("墨子号: " + SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"));
        findViewById(R.id.ll_return).setOnClickListener(this);
    }

    private static Bitmap convertViewToBitmap(View tempview, Display display) {
        Bitmap bitmap = Bitmap.createBitmap(tempview.getWidth(),
                tempview.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tempview.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_audio_more:
                showWaringDialog(
                        "提示",
                        "将二维码保存到相册？",
                        new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                Display display = getWindowManager().getDefaultDisplay();
                                tvSaveCode.setVisibility(View.GONE);
                                saveBitmap(convertViewToBitmap(llCodeImg, display));
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        }
                );
                break;
            case R.id.ll_return:
                finish();
                break;
        }
    }

    /**
     * 保存方法
     */
    private void saveBitmap(Bitmap bm) {
        String s = "";
        s = "/DCIM/my" + System.currentTimeMillis() + ".png";
        File f = new File(Util.getSDCardPath() + s);
        try {
            f.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (f.exists()) {
            f.delete();
        } else {
            f.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Util.getSDCardPath() + s))));
            ToastMaker.showShortToast("保存成功");
            tvSaveCode.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
