package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.activity.LoginActivity;
import com.yidiankeyan.science.information.acitivity.ClassAudioActivity;
import com.yidiankeyan.science.information.acitivity.ClassImaTxtActivity;
import com.yidiankeyan.science.information.acitivity.ClassVideoActivity;
import com.yidiankeyan.science.information.acitivity.CustomProjectActivity;
import com.yidiankeyan.science.information.adapter.AllAudioAlbumAdapter;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.adapter.ListViewAdapter;
import com.yidiankeyan.science.information.entity.AllAudioListBean;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 专题
 * -更多所有专辑
 */
public class AllAlbumFragment extends Fragment implements View.OnClickListener {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecommend;
    private ListViewAdapter mListViewAdapter;
    //    private List<NewRecommendBean> mList = new ArrayList<>();
    private AllAudioAlbumAdapter allAudioAlbumAdapter;
    private ArrayList<AllAudioListBean> mDatas = new ArrayList<>();
    private RollPagerView vpBanner;
    private LayoutInflater mInflater;
    private View headView;
    private int pages = 1;
    private BannerAdapter mLoopAdapter;
    private List<BannerBean> mBannerList = new ArrayList<>();
    private AutoRelativeLayout rlAudio;
    private AutoRelativeLayout rlVideo;
    private AutoRelativeLayout rlGraphic;
    private AutoRelativeLayout rlCustom;


    public AllAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_all_album, container, false);
        mInflater = inflater;
        initView(view);
        initAction();
        return view;
    }

    private void initView(View view) {
        lvRecommend = (ListViewFinalLoadMore) view.findViewById(R.id.lv_recommend);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        headView = mInflater.inflate(R.layout.head_album, null);
        vpBanner = (RollPagerView) headView.findViewById(R.id.vp_banner);
        rlAudio = (AutoRelativeLayout) headView.findViewById(R.id.rl_audio);
        rlVideo = (AutoRelativeLayout) headView.findViewById(R.id.rl_video);
        rlGraphic = (AutoRelativeLayout) headView.findViewById(R.id.rl_graphic);
        rlCustom = (AutoRelativeLayout) headView.findViewById(R.id.rl_custom);
    }

    private void initAction() {

        mLoopAdapter = new BannerAdapter(vpBanner, getContext(), mBannerList);
        vpBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), getContext(),position);
            }
        });
        mLoopAdapter.setOnCustomPageSelect(new LoopPagerAdapter.OnCustomPageSelect() {
            @Override
            public void onPageSelect(ViewGroup container, int position) {
                ImageView view = new ImageView(container.getContext());
                Glide.with(getContext()).load(Util.getImgUrl(mBannerList.get(position).getImgurl()))
                        .error(R.drawable.icon_banner_load)
                        .placeholder(R.drawable.icon_banner_load).into(view);
            }
        });
        vpBanner.setAdapter(mLoopAdapter);
        if (mBannerList.size() == 0) {
            toHttpGetBanner();
        }

        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetOrderAlbum();
            }
        });

        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvRecommend.setHasLoadMore(true);
        }

        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        allAudioAlbumAdapter = new AllAudioAlbumAdapter(getContext(), mDatas);
        lvRecommend.addHeaderView(headView);
        lvRecommend.setAdapter(allAudioAlbumAdapter);

        lvRecommend.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetOrderAlbum();
            }
        });
        rlAudio.setOnClickListener(this);
        rlVideo.setOnClickListener(this);
        rlGraphic.setOnClickListener(this);
        rlCustom.setOnClickListener(this);
    }

    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 7);
        map.put("pages", 1);
        map.put("pagesize", 6);
//        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) {
//                if (result.getCode() == 200) {
//                    String jsonData = GsonUtils.obj2Json(result.getData());
//                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
//                    mBannerList.removeAll(mBannerList);
//                    mBannerList.addAll(data);
//                    mLoopAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//            }
//        });
        ApiServerManager.getInstance().getApiServer().getBanner(map).enqueue(new RetrofitCallBack<List<BannerBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<BannerBean>>> call, Response<RetrofitResult<List<BannerBean>>> response) {
                if (response.body().getCode() == 200) {
                    List<BannerBean> data = response.body().getData();
                    mBannerList.removeAll(mBannerList);
                    mBannerList.addAll(data);
                    mLoopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<BannerBean>>> call, Throwable t) {

            }
        });
    }

    private void toHttpGetOrderAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 9);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumtype", 2);
        entity.put("belongtype", 1);
        map.put("entity", entity);
//        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_RECOMMEND_PROJECT_ALL, map, new HttpUtil.HttpCallBack() {
//
//            @Override
//            public void successResult(ResultEntity result) {
//                if (result.getCode() == 200) {
//                    String jsonData = GsonUtils.obj2Json(result.getData());
//                    List<NewRecommendBean> mData = GsonUtils.json2List(jsonData, NewRecommendBean.class);
//                    mList.removeAll(mList);
//                    mList.addAll(mData);
//                    mListViewAdapter.notifyDataSetChanged();
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                ptrLayout.onRefreshComplete();
//            }
//        });
//        ApiServerManager.getInstance().getApiServer().getRecommend(map,2).enqueue(new RetrofitCallBack<List<NewRecommendBean>>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<List<NewRecommendBean>>> call, Response<RetrofitResult<List<NewRecommendBean>>> response) {
//                if (response.body().getCode() == 200) {
//                    List<NewRecommendBean> mData = response.body().getData();
//                    mList.removeAll(mList);
//                    mList.addAll(mData);
//                    mListViewAdapter.notifyDataSetChanged();
//                }
//                ptrLayout.onRefreshComplete();
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<List<NewRecommendBean>>> call, Throwable t) {
//                ptrLayout.onRefreshComplete();
//            }
//        });
        ApiServerManager.getInstance().getApiServer().getEditorAlbum(map).enqueue(new RetrofitCallBack<ArrayList<EditorAlbum>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<EditorAlbum>>> call, Response<RetrofitResult<ArrayList<EditorAlbum>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvRecommend.setHasLoadMore(true);
                        ArrayList<EditorAlbum> mLists = new ArrayList<EditorAlbum>();
                        mLists.addAll(response.body().getData());
                        AllAudioListBean editorAlbum = new AllAudioListBean(mLists);
                        mDatas.add(editorAlbum);
                        pages++;
                    } else {
                        lvRecommend.setHasLoadMore(false);
                    }

                    allAudioAlbumAdapter.notifyDataSetChanged();
                } else {
                    lvRecommend.showFailUI();
                }
                ptrLayout.onRefreshComplete();

            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<EditorAlbum>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvRecommend.showFailUI();
            }
        });
    }
//
//    @Subscribe
//    public void onEvent(EventMsg msg) {
//        switch (msg.getWhat()) {
//            case SystemConstant.CUSTOM_PROJECT:
//                //用户定制专题发生了改变
//                ptrLayout.autoRefresh();
//                break;
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_custom:
                if (TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), CustomProjectActivity.class));
                break;
            case R.id.rl_audio:
                startActivity(new Intent(getContext(), ClassAudioActivity.class));
                break;
            case R.id.rl_video:
                startActivity(new Intent(getContext(), ClassVideoActivity.class));
                break;
            case R.id.rl_graphic:
                startActivity(new Intent(getContext(), ClassImaTxtActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
