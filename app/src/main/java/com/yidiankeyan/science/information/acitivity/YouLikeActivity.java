package com.yidiankeyan.science.information.acitivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AlbumPagerAdapter;
import com.yidiankeyan.science.information.fragment.CapacitySortFragment;
import com.yidiankeyan.science.information.fragment.HeatSortFragment;
import com.yidiankeyan.science.information.fragment.UpdateSortFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 *  -猜你喜欢
 */
public class YouLikeActivity extends BaseActivity {

    private ViewPager vPager;
    private AlbumPagerAdapter adapter;
    private ImageButton imgTitle;
    private AutoLinearLayout llReturn;
    private TextView txtTitle;
    private TextView tvUp;
    private TextView tvHeat;
    private TextView tvCapa;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected void initView() {
        llReturn= (AutoLinearLayout) findViewById(R.id.ll_return);
        txtTitle= (TextView) findViewById(R.id.maintitle_txt);
        imgTitle= (ImageButton) findViewById(R.id.title_btn);
        tvUp = (TextView) findViewById(R.id.tv_likeup);
        tvHeat = (TextView) findViewById(R.id.tv_likeheat);
        tvCapa = (TextView) findViewById(R.id.tv_likecapa);
        vPager = (ViewPager) findViewById(R.id.vp_youlike);
    }

    @Override
    protected void initAction() {
        imgTitle.setVisibility(View.GONE);
        txtTitle.setText("猜你喜欢");
        findViewById(R.id.ll_return).setOnClickListener(this);
        fragmentList.add(new UpdateSortFragment());
        fragmentList.add(new HeatSortFragment());
        fragmentList.add(new CapacitySortFragment());
        adapter = new AlbumPagerAdapter(getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);

        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        llReturn.setOnClickListener(this);
        tvUp.setOnClickListener(this);
        tvHeat.setOnClickListener(this);
        tvCapa.setOnClickListener(this);
        tvUp.setTextColor(Color.parseColor("#F1312E"));
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_you_like;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_likeup:
                currIndex = 0;
                vPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_likeheat:
                currIndex = 1;
                vPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_likecapa:
                currIndex = 2;
                vPager.setCurrentItem(currIndex);
                break;
        }
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
                    tvUp.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
                    tvHeat.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 2:
                    tvCapa.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        tvUp.setTextColor(Color.parseColor("#999999"));
        tvHeat.setTextColor(Color.parseColor("#999999"));
        tvCapa.setTextColor(Color.parseColor("#999999"));
    }
}
