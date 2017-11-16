package com.yidiankeyan.science.collection.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.my.fragment.ColIsDownLoadFragment;
import com.yidiankeyan.science.utils.SystemConstant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 点藏
 */
public class CollectionFragment extends Fragment {


    private ViewPager viewPager;// 页卡内容
    private TextView txtDownload, txtPurchase, txtIsDownload;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private int selectedColor, unSelectedColor;
    private MyPagerAdapter adapter;
    private boolean showDownloadList;
    private ColIsDownLoadFragment downloadList;
    private ColFreeAlbumFragment collectFragment;
    private ColChargeAlbumFragment localFragment;

    public CollectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        initView(view);
//        if (DownloadManager.getInstance().getDownloadListCount() == 0) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/13/eGiMx6929/HD/eGiMx6929-mobile.mp4"
//                                , "封神传奇",
//                                "/sdcard/xUtils/" + "封神传奇" + ".mp4"
//                                , true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1512/02/ZkTNS2477/HD/ZkTNS2477-mobile.mp4"
//                                , "绝地逃亡",
//                                "/sdcard/xUtils/" + "绝地逃亡" + ".mp4"
//                                , true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/11/AaXIN8439/HD/AaXIN8439-mobile.mp4"
//                                , "泰山归来：险战丛林"
//                                , "/sdcard/xUtils/" + "泰山归来：险战丛林" + ".mp4", true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/21/SZlOq1305/HD/SZlOq1305-mobile.mp4"
//                                , "神秘世界历险记3"
//                                , "/sdcard/xUtils/" + "神秘世界历险记3" + ".mp4", true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/14/fyZVU7852/HD/fyZVU7852-mobile.mp4"
//                                , "哆啦A梦：新·大雄的日本诞生"
//                                , "/sdcard/xUtils/" + "哆啦A梦：新·大雄的日本诞生" + ".mp4", true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/13/eGiMx6929/HD/eGiMx6929-mobile.mp4"
//                                , "封神传奇2",
//                                "/sdcard/xUtils/" + "封神传奇2" + ".mp4"
//                                , true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1512/02/ZkTNS2477/HD/ZkTNS2477-mobile.mp4"
//                                , "绝地逃亡2",
//                                "/sdcard/xUtils/" + "绝地逃亡2" + ".mp4"
//                                , true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/11/AaXIN8439/HD/AaXIN8439-mobile.mp4"
//                                , "泰山归来：险战丛林2"
//                                , "/sdcard/xUtils/" + "泰山归来：险战丛林2" + ".mp4", true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/21/SZlOq1305/HD/SZlOq1305-mobile.mp4"
//                                , "神秘世界历险记32"
//                                , "/sdcard/xUtils/" + "神秘世界历险记32" + ".mp4", true, false, null);
//                        DownloadManager.getInstance().startDownload(
//                                "http://flv.bn.netease.com/videolib3/1607/14/fyZVU7852/HD/fyZVU7852-mobile.mp4"
//                                , "哆啦A梦：新·大雄的日本诞生2"
//                                , "/sdcard/xUtils/" + "哆啦A梦：新·大雄的日本诞生2" + ".mp4", true, false, null);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
        return view;
    }

    private void initView(View view) {
        selectedColor = getResources()
                .getColor(R.color.main_orange);
        unSelectedColor = getResources().getColor(
                R.color.deepgray);
        ((TextView) view.findViewById(R.id.maintitle_txt)).setText("收藏");
        InitTextView(view);
        InitViewPager(view);
    }

    /**
     * 初始化Viewpager页
     */
    private void InitViewPager(View view) {
        Log.e("sdsds", "=====InitViewPager");
        viewPager = (ViewPager) view.findViewById(R.id.vp_collection);
        //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
        viewPager.setOffscreenPageLimit(2);
        fragments = new ArrayList<>();
        downloadList = new ColIsDownLoadFragment();
        collectFragment = new ColFreeAlbumFragment();
        localFragment = new ColChargeAlbumFragment();
        fragments.add(collectFragment);
        fragments.add(localFragment);
        fragments.add(downloadList);
        adapter = new MyPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        if (showDownloadList) {
            viewPager.setCurrentItem(2);
        } else
            viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView(View view) {
        txtDownload = (TextView) view.findViewById(R.id.tab_download);
        txtPurchase = (TextView) view.findViewById(R.id.tab_purchase);
        txtIsDownload = (TextView) view.findViewById(R.id.tab_downloadthe);
        if (showDownloadList) {
            txtDownload.setTextColor(unSelectedColor);
            txtIsDownload.setTextColor(selectedColor);
        } else {
            txtDownload.setTextColor(selectedColor);
            txtIsDownload.setTextColor(unSelectedColor);
        }
        txtPurchase.setTextColor(unSelectedColor);
        txtDownload.setText("点藏");
        txtPurchase.setText("本地");
        txtIsDownload.setText("正在下载");

        txtDownload.setOnClickListener(new MyOnClickListener(0));
        txtPurchase.setOnClickListener(new MyOnClickListener(1));
        txtIsDownload.setOnClickListener(new MyOnClickListener(2));
    }

    public void showDownloadList() {
        showDownloadList = true;
        if (viewPager != null) {
            viewPager.setCurrentItem(2);
            downloadList.notifyDataSetChanged();
        }
    }

    public void onDeleteClick() {
        if (currIndex == 0)
            collectFragment.onDeleteClick();
        else if (currIndex == 1)
            localFragment.onDeleteClick();

    }

    public void onCancelClick() {
        if (currIndex == 0)
            collectFragment.onCancelClick();
        else if (currIndex == 1)
            localFragment.onCancelClick();
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    txtDownload.setTextColor(selectedColor);
                    txtPurchase.setTextColor(unSelectedColor);
                    txtIsDownload.setTextColor(unSelectedColor);
                    break;
                case 1:
                    txtPurchase.setTextColor(selectedColor);
                    txtIsDownload.setTextColor(unSelectedColor);
                    txtDownload.setTextColor(unSelectedColor);
                    Intent intent = new Intent();
                    intent.setAction("action.download.refreshFriend");
                    getActivity().sendBroadcast(intent);
                    break;
                case 2:
                    txtIsDownload.setTextColor(selectedColor);
                    txtDownload.setTextColor(unSelectedColor);
                    txtPurchase.setTextColor(unSelectedColor);
                    break;
            }
            viewPager.setCurrentItem(index);
        }

    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            currIndex = index;
            EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_MAIN_COLLECT_SHOW_OR_HIDE);
            switch (index) {
                case 0:
                    txtDownload.setTextColor(selectedColor);
                    txtPurchase.setTextColor(unSelectedColor);
                    txtIsDownload.setTextColor(unSelectedColor);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                    break;
                case 1:
                    txtPurchase.setTextColor(selectedColor);
                    txtIsDownload.setTextColor(unSelectedColor);
                    txtDownload.setTextColor(unSelectedColor);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                    Intent intent = new Intent();
                    intent.setAction("action.download.refreshFriend");
                    getActivity().sendBroadcast(intent);
                    break;
                case 2:
                    txtIsDownload.setTextColor(selectedColor);
                    txtPurchase.setTextColor(unSelectedColor);
                    txtDownload.setTextColor(unSelectedColor);
                    msg.setArg1(0);
                    EventBus.getDefault().post(msg);
                    break;
            }
        }
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public boolean isDeleteState() {
        if (currIndex == 0) {
            return collectFragment.isDeleteState();
        } else if (currIndex == 1) {
            return localFragment.isDeleteState();
        } else
            return false;
    }
}
