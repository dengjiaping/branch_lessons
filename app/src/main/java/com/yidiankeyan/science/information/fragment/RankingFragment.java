package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.RankingActivity;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资讯-排行
 */
public class RankingFragment extends Fragment implements View.OnClickListener {

    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private Intent intent;
    private List<BannerBean> mBannerList = new ArrayList<>();
    private ImageView viewImgRanking;
    private ImageView viewAudioRanking;
    private ImageView viewVideoRanking;
    private ImageView viewSubscribeRanking;
    private ImageView viewEditorRanking;
    private ImageView viewBuyRanking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        if (mBannerList.size() == 0)
            toHttpGetBanner();

        viewImgRanking = (ImageView) view.findViewById(R.id.view_img_ranking);
        viewAudioRanking = (ImageView) view.findViewById(R.id.view_audio_ranking);
        viewVideoRanking = (ImageView) view.findViewById(R.id.view_video_ranking);
        viewSubscribeRanking = (ImageView) view.findViewById(R.id.view_subscribe_ranking);
        viewEditorRanking = (ImageView) view.findViewById(R.id.view_editor_ranking);
        viewBuyRanking = (ImageView) view.findViewById(R.id.view_buy_ranking);

        viewImgRanking.setOnClickListener(this);
        viewAudioRanking.setOnClickListener(this);
        viewVideoRanking.setOnClickListener(this);
        viewSubscribeRanking.setOnClickListener(this);
        viewEditorRanking.setOnClickListener(this);
        viewBuyRanking.setOnClickListener(this);

        mRollViewPager = (RollPagerView) view.findViewById(R.id.vp_banner);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), getContext(),position);
            }
        });
        mRollViewPager.setAdapter(mLoopAdapter = new BannerAdapter(mRollViewPager, getContext(), mBannerList));
        mRollViewPager.setHintAlpha(0);
        mLoopAdapter.setOnCustomPageSelect(new LoopPagerAdapter.OnCustomPageSelect() {
            @Override
            public void onPageSelect(ViewGroup container, int position) {
                ImageView view = new ImageView(container.getContext());
                Glide.with(getContext()).load(Util.getImgUrl(mBannerList.get(position).getImgurl())).error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar).into(view);
            }
        });
//        mRollViewPager.setHintView(null);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                position--;
//                if (position >= 0) {
//                    switch (mData.get(position).getType()) {
//                        //专辑榜
//                        case 1:
//                        case 2:
//                        case 3:
//                        case 4:
//                        case 5:
//                        case 6:
//                            intent = new Intent(getContext(), AlbumChartsActivity.class);
//                            intent.putExtra("title", mData.get(position).getTitle());
//                            intent.putExtra("type", mData.get(position).getType());
//                            startActivity(intent);
//                            break;
//                        case 7:
//                        case 8:
//                        case 9:
//                            intent = new Intent(getContext(), EditorChartsActivity.class);
//                            intent.putExtra("title", mData.get(position).getTitle());
//                            intent.putExtra("type", mData.get(position).getType());
//                            startActivity(intent);
//                            break;
//                        case 10:
//                        case 11:
//                            intent = new Intent(getContext(), SubjectChartsActivity.class);
//                            intent.putExtra("title", mData.get(position).getTitle());
//                            intent.putExtra("type", mData.get(position).getType());
//                            startActivity(intent);
//                            break;
//                    }
//                }
//            }
//        });
    }

    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 2);
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
                    mBannerList.addAll(data);
                    mLoopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_img_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("title", "图文榜");
                intent.putExtra("type", 4);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.view_audio_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("title", "音频榜");
                intent.putExtra("type", 5);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
            case R.id.view_video_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("title", "视频榜");
                intent.putExtra("type", 6);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
            case R.id.view_subscribe_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("title", "订阅榜");
                intent.putExtra("type", 2);
                intent.putExtra("page", 3);
                startActivity(intent);
                break;
            case R.id.view_editor_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("title", "主编榜");
                intent.putExtra("type", 7);
                intent.putExtra("page", 4);
                startActivity(intent);
                break;
            case R.id.view_buy_ranking:
                intent = new Intent(getContext(), RankingActivity.class);
                intent.putExtra("page", 5);
                startActivity(intent);
                break;
        }
    }

}
