package com.yidiankeyan.science.information;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.functionkey.activity.SearchActivity;
import com.yidiankeyan.science.information.acitivity.AudioControlActivity;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 资讯
 */
public class InformationFragment extends Fragment implements View.OnClickListener {


    private AnimationDrawable animationDrawable;
    private ImageView imgPlay;
    private AutoRelativeLayout llShare;


    public ArrayList<OneDayArticles> playList;
    public SimpleDateFormat sdf;
    public Map<String, List<OneDayArticles>> dataMap = new HashMap<>();
    public int playPosition;

    public int playModel = 0;

    private static InformationFragment instance;

    public static InformationFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ((TextView) view.findViewById(R.id.maintitle_txt)).setText("资讯");
        view.findViewById(R.id.title_btn).setVisibility(View.VISIBLE);
        imgPlay = (ImageView) view.findViewById(R.id.img_play);
        llShare = (AutoRelativeLayout) view.findViewById(R.id.ll_share);
        animationDrawable = (AnimationDrawable) imgPlay.getDrawable();

        view.findViewById(R.id.title_btn).setOnClickListener(this);
        //初始化各fragment
        InitViewPager();
    }

    /**
     * 初始化Viewpager页
     */
    private void InitViewPager() {

        llShare.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            JCVideoPlayer.releaseAllVideos();
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
//            case SystemConstant.MOZ_REGISTER_HOT:
//                if (msg.getArg1() == 0) {
//                    viewPager.setCurrentItem(0);
//                }
//                break;
            case SystemConstant.ON_PLAYING:
                if (!animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case SystemConstant.ON_PAUSE:
            case SystemConstant.ON_STOP:
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_btn:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_share:
//                EventMsg msg = EventMsg.obtain(SystemConstant.INFO_AUDIO_PLAYER);
//                EventBus.getDefault().post(msg);
                JCVideoPlayer.releaseAllVideos();
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP
                        || AudioPlayManager.getInstance().CURRENT_STATE == 0) {
                    InformationFragment.getInstance().playList = (ArrayList<OneDayArticles>) dataMap.get(sdf.format(new Date()));
                }
                if (InformationFragment.getInstance().playList == null || InformationFragment.getInstance().playList.size() == 0) {
                    ToastMaker.showShortToast("今天没有可播放的内容");
                    return;
                }
                intent = new Intent(getContext(), AudioControlActivity.class);
                LogUtils.e(InformationFragment.getInstance().playList.toString());
                intent.putParcelableArrayListExtra("playList", InformationFragment.getInstance().playList);
                for (int i = 0; i < InformationFragment.getInstance().playList.size(); i++) {
                    if (TextUtils.equals(InformationFragment.getInstance().playList.get(i).getId(), AudioPlayManager.getInstance().getmMediaPlayId())) {
                        playPosition = i;
                        break;
                    }
                }
                intent.putExtra("playPosition", playPosition);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        EventBus.getDefault().unregister(this);
    }
}
