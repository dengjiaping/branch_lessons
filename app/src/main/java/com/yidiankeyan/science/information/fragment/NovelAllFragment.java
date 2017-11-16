package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.WholeAlbumAdapter;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科技小说
 * -所有专辑
 */
public class NovelAllFragment extends Fragment implements AdapterView.OnItemClickListener {


    private WholeAlbumAdapter albumAdapter;
    private ListView lvNovelAll;
    private View viewtop;
    private List<BusinessAllBean> mDatas = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pageNum = 0;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private TestLoopAdapter mLoopAdapter;
    private Intent intent;

    private List<BannerBean> mBannerList = new ArrayList<>();

    public NovelAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel_all, container, false);
        //初始化控件
        initView(view);

        //填充数据
        if (mDatas.size() == 0)
            toHttpGetNovelAll();
//            addData();
        albumAdapter = new WholeAlbumAdapter(getActivity(), mDatas);
        lvNovelAll.setAdapter(albumAdapter);
        lvNovelAll.setOnItemClickListener(this);

        //因为轮播图的bug导致图片有几率没加载出来，所以重新加载一次
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    mRollViewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoopAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return view;
    }

    /**
     * 获取赛思小说所有专辑
     */
    private void toHttpGetNovelAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("belongtype", 2);
        entity.put("belongid", 1005);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<BusinessAllBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), BusinessAllBean.class);
                    mDatas.addAll(data);
                    albumAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    private void initView(View view) {
        lvNovelAll = (ListView) view.findViewById(R.id.lv_novelall);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        lvNovelAll.addHeaderView(viewtop);
        toDBGetBanner();
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
        mRollViewPager.setHintAlpha(0);
//        mRollViewPager.setHintView(null);
    }

    private void toDBGetBanner() {
        List<BannerBean> mData = new DB(getContext()).queryBanner();
        mBannerList.removeAll(mBannerList);
        for (BannerBean banner : mData) {
            if (1 == banner.getPositionid()) {
                mBannerList.add(banner);
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(getContext(), AlbumDetailsActivity.class);
        intent.putExtra("albumId", mDatas.get(position-1).getAlbumid());
        intent.putExtra("albumName", mDatas.get(position-1).getName());
        intent.putExtra("albumAvatar", mDatas.get(position-1).getShortimgurl());
        startActivity(intent);
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int count;

        public void add() {
            Log.i("RollViewPager", "Add");
            count++;
            if (count > mBannerList.size())
                count = mBannerList.size();
            notifyDataSetChanged();
        }

        public void minus() {
            Log.i("RollViewPager", "Minus");
            count--;
            if (count < 1) count = 1;
            notifyDataSetChanged();
        }

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(getContext()).load(mBannerList.get(position).getImgurl()).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), AlbumDetailsActivity.class);
//                    startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public int getRealCount() {
            if (mBannerList.size() == 0)
                return count;
            else {
                return mBannerList.size();
            }
        }

    }

}
