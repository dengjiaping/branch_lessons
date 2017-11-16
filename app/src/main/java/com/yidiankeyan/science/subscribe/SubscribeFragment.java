package com.yidiankeyan.science.subscribe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidiankeyan.science.R;


/**
 * 订阅
 */
public class SubscribeFragment extends Fragment {


//    private ViewPager viewPager;// 页卡内容
//    private TextView txtFreeAblum, txtChargeAblum;// 选项名称
//    private List<Fragment> fragments;// Tab页面列表
//    private int currIndex = 0;// 当前页卡编号
//    private int selectedColor, unSelectedColor;
//    private MyPagerAdapter adapter;

    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
//        selectedColor = getResources()
//                .getColor(R.color.main_orange);
//        unSelectedColor = getResources().getColor(
//                R.color.deepgray);

//        InitTextView(view);
//        InitViewPager(view);
    }
    /**
     * 初始化Viewpager页
     */
//    private void InitViewPager(View view) {
//        viewPager = (ViewPager) view.findViewById(R.id.vp_subscribe);
//        fragments = new ArrayList<>();
//        fragments.add(new FreeAblumFragment());
//        fragments.add(new ChargeAblumFragment());
//        adapter=new MyPagerAdapter(getFragmentManager(),fragments);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
//    }

    /**
     * 初始化头标
     */
//    private void InitTextView(View view) {
//        txtFreeAblum = (TextView) view.findViewById(R.id.tab_free_ablum);
//        txtChargeAblum = (TextView) view.findViewById(R.id.tab_charge_ablum);

//        txtFreeAblum.setTextColor(selectedColor);
//        txtChargeAblum.setTextColor(unSelectedColor);
//
//        txtFreeAblum.setText("免费专辑");
//        txtChargeAblum.setText("收费专辑");
//
//        txtFreeAblum.setOnClickListener(new MyOnClickListener(0));
//        txtChargeAblum.setOnClickListener(new MyOnClickListener(1));
//    }


    /**
     * 头标点击监听
     */
//    private class MyOnClickListener implements View.OnClickListener {
//        private int index = 0;
//
//        public MyOnClickListener(int i) {
//            index = i;
//        }
//
//        public void onClick(View v) {
//
//            switch (index) {
//                case 0:
//                    txtFreeAblum.setTextColor(selectedColor);
//                    txtChargeAblum.setTextColor(unSelectedColor);
//                    break;
//                case 1:
//                    txtChargeAblum.setTextColor(selectedColor);
//                    txtFreeAblum.setTextColor(unSelectedColor);
//                    break;
//            }
//            viewPager.setCurrentItem(index);
//        }
//
//    }

    /**
     * 为选项卡绑定监听器
     */
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//
//        public void onPageScrollStateChanged(int index) {
//        }
//
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        public void onPageSelected(int index) {
//            currIndex = index;
//            switch (index) {
//                case 0:
//                    txtFreeAblum.setTextColor(selectedColor);
//                    txtChargeAblum.setTextColor(unSelectedColor);
//                    break;
//                case 1:
//                    txtChargeAblum.setTextColor(selectedColor);
//                    txtFreeAblum.setTextColor(unSelectedColor);
//                    break;
//            }
//        }
//    }
}
