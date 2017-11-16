package com.yidiankeyan.science.subscribe.activity;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nineoldandroids.animation.ValueAnimator;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.FileUtils;
import com.yidiankeyan.science.utils.ToastMaker;

import uk.co.senab.photoview.PhotoView;

/**
 * 文章图片缩放
 */
public class PhotoEnlargeActivity extends BaseActivity {
    private ImageView crossIv;
    private ViewPager mPager;
    private ImageView centerIv;
    private TextView photoOrderTv;
    private TextView saveTv;
    private String curImageUrl = "";
    private String[] imageUrls = new String[]{};

    private int curPosition = -1;
    private int[] initialedPositions = null;
    private int leng;
    private String saveImg = "";

    private MyPagerAdapter pagerAdapter;

    @Override
    protected int setContentView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return R.layout.activity_photo_enlarge;
    }

    @Override
    protected void initView() {
        photoOrderTv = (TextView) findViewById(R.id.photoOrderTv);
        saveTv = (TextView) findViewById(R.id.saveTv);
        centerIv = (ImageView) findViewById(R.id.centerIv);
        crossIv = (ImageView) findViewById(R.id.crossIv);
        mPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void initAction() {
        imageUrls = getIntent().getStringArrayExtra("listulrs");
        curImageUrl = getIntent().getStringExtra("image_url");
        initialedPositions = new int[imageUrls.length];
        initInitialedPositions();

        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        pagerAdapter = new MyPagerAdapter();
        mPager.setAdapter(pagerAdapter);

        curPosition = returnClickedPosition() == -1 ? 0 : returnClickedPosition();
        mPager.setCurrentItem(curPosition);
        mPager.setTag(curPosition);
        if (initialedPositions[curPosition] != curPosition) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
            showLoadingAnimation();
        }
        photoOrderTv.setText((curPosition + 1) + "/" + imageUrls.length);//设置页面的编号

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (initialedPositions[position] != position) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoadingAnimation();
                } else {
                    hideLoadingAnimation();
                }
                curPosition = position;
                photoOrderTv.setText((position + 1) + "/" + imageUrls.length);//设置页面的编号
                mPager.setTag(position);//为当前view设置tag
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        saveTv.setOnClickListener(this);
        crossIv.setOnClickListener(this);
    }


    private int returnClickedPosition() {
        if (imageUrls == null || curImageUrl == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.length; i++) {
            if (curImageUrl.equals(imageUrls[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crossIv:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.saveTv:
                savePhotoToLocal();
                break;
            default:
                break;
        }
    }

    private void showLoadingAnimation() {
        centerIv.setVisibility(View.VISIBLE);
        centerIv.setImageResource(R.drawable.icon_imgtxt_loads);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(centerIv, "rotation", 0f, 360f);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private void hideLoadingAnimation() {
        releaseResource();
        centerIv.setVisibility(View.GONE);
    }

    private void showErrorLoading() {
        centerIv.setVisibility(View.VISIBLE);
        releaseResource();
        centerIv.setImageResource(R.drawable.icon_imgtxt_loads);
    }

    private void releaseResource() {
        if (centerIv.getAnimation() != null) {
            centerIv.getAnimation().cancel();
        }
    }

    private void occupyOnePosition(int position) {
        initialedPositions[position] = position;
    }

    private void releaseOnePosition(int position) {
        initialedPositions[position] = -1;
    }

    private void initInitialedPositions() {
        for (int i = 0; i < initialedPositions.length; i++) {
            initialedPositions[i] = -1;
        }
    }

    private void savePhotoToLocal() {
        Object tag = mPager.getTag();
        ViewGroup containerTemp = (ViewGroup) mPager.findViewWithTag(mPager.getCurrentItem());
        PhotoView photoViewTemp = (PhotoView) pagerAdapter.getPrimaryItem();
        if (containerTemp == null) {
            return;
        }
//        PhotoView photoViewTemp = (PhotoView) containerTemp.getChildAt(0);
        if (photoViewTemp != null) {
            try {
                GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) photoViewTemp.getDrawable();
                if (glideBitmapDrawable == null) {
                    return;
                }
                Bitmap bitmap = glideBitmapDrawable.getBitmap();
                if (bitmap == null) {
                    return;
                }
                FileUtils.savePhoto(this, bitmap, new FileUtils.SaveResultCallback() {
                    @Override
                    public void onSavedSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMaker.showShortToast("保存成功");
                            }
                        });
                    }

                    @Override
                    public void onSavedFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMaker.showShortToast("保存失败");
                            }
                        });
                    }
                });
            } catch (ClassCastException e) {
                GifDrawable glideBitmapDrawable = (GifDrawable) photoViewTemp.getDrawable();
                if (glideBitmapDrawable == null) {
                    return;
                }
                Bitmap bitmap = glideBitmapDrawable.getFirstFrame();
                if (bitmap == null) {
                    return;
                }
                FileUtils.savePhoto(this, glideBitmapDrawable.getData(), new FileUtils.SaveResultCallback() {
                    @Override
                    public void onSavedSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMaker.showShortToast("保存成功");
                            }
                        });
                    }

                    @Override
                    public void onSavedFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMaker.showShortToast("保存失败");
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        releaseResource();
        super.onDestroy();
    }

    private class MyPagerAdapter extends PagerAdapter {

        private View mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (View) object;
        }

        public View getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (imageUrls[position] != null && !"".equals(imageUrls[position])) {
                final PhotoView view = new PhotoView(PhotoEnlargeActivity.this);
//                    view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                saveImg = imageUrls[position].toString();
                Glide.with(PhotoEnlargeActivity.this).load(imageUrls[position]).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).fitCenter().crossFade().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (position == curPosition) {
                            hideLoadingAnimation();
                        }
                        showErrorLoading();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        occupyOnePosition(position);
                        if (position == curPosition) {
                            hideLoadingAnimation();
                        }
                        return false;
                    }
                }).into(view);

                container.addView(view);
                return view;
            }
            return null;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            releaseOnePosition(position);
            container.removeView((View) object);
        }

    }
}
