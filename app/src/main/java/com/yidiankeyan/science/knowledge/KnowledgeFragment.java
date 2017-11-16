package com.yidiankeyan.science.knowledge;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.NewsFlashActivity;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.adapter.HotNewsAdapter;
import com.yidiankeyan.science.information.adapter.MyRecFollowAdapter;
import com.yidiankeyan.science.information.adapter.RecommendFollowAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.knowledge.activity.NewsFlashCardActivity;
import com.yidiankeyan.science.my.activity.MyFocusActivity;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.MarqueeView;
import com.yidiankeyan.science.view.ShowAllListView;
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
 * 知识
 */
public class KnowledgeFragment extends Fragment implements View.OnClickListener {

    private ListViewFinalLoadMore lvHot;
    private int pages = 1;
    private List<HotNewsBean> mData = new ArrayList<>();
    private HotNewsAdapter adapter;
    private View headView;
    private RollPagerView rollPager;
    private BannerAdapter mLoopAdapter;
    private List<BannerBean> mBannerList = new ArrayList<>();
    private MarqueeView marqueeView;
    private List<FlashBean> news = new ArrayList<>();
    //    private GridView horizontalGridView;

    private AutoRelativeLayout rlRecFollow;
    //推荐关注
    private RecommendFollowAdapter mAdapter;
    private ArrayList<RecommendFollowBean> magazineLists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    //我的关注
    private List<HotNewsBean> mMyData = new ArrayList<>();
    private MyRecFollowAdapter followAdapter;
    private ShowAllListView lvRecMyFollow;
    private PtrClassicFrameLayout ptrLayout;
    private TextView tvType;
    private TextView tvMyFollowMore;
    private static long hotspotTimestamp;
    private int itemHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        lvHot.addHeaderView(headView);
        //--------我的关注
        //设置水平横向滑动的参数
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecommendFollowAdapter(getContext(), magazineLists);
        mRecyclerView.setAdapter(mAdapter);
        followAdapter = new MyRecFollowAdapter(getContext(), mMyData);
        lvRecMyFollow.setAdapter(followAdapter);
        adapter = new HotNewsAdapter(getContext(), mData);
        lvHot.setAdapter(adapter);
        initBanner();
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpGetNews();
                toHttpGetBanner();
                toHttpMyFollowList();
                toHttpGetFlash();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.autoRefresh();
        lvHot.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttpGetNews();
            }
        });
        lvRecMyFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (mMyData.get(position).getType()) {
                    case 1:
                        intent = new Intent(getContext(), ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mMyData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mMyData.get(position).getName());
                        audio.setArticleid(mMyData.get(position).getId());
                        audio.setLastupdatetime(mMyData.get(position).getCreatetime());
                        audio.setArticletype(2);
                        audio.setMediaurl(mMyData.get(position).getMediaurl());
                        audio.setCoverimgurl(mMyData.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mMyData.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(getContext(), AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", 0);
                        intent.putExtra("single", true);
                        intent.putExtra("id", mMyData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 3:
                        if (followAdapter.getItemViewType(position) == HotNewsAdapter.TYPE_VIDEO_B)
                            return;
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mMyData.get(position).getName());
                        video.setArticleid(mMyData.get(position).getId());
                        video.setLastupdatetime(mMyData.get(position).getCreatetime());
                        video.setArticletype(2);
                        video.setMediaurl(mMyData.get(position).getMediaurl());
                        video.setCoverimgurl(mMyData.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mMyData.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(getContext(), VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mMyData.get(0).getId());
                        getContext().startActivity(intent);
                        break;
                }
            }
        });
        lvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                position--;
                if (position < 0)
                    return;
                switch (mData.get(position).getType()) {
                    case 1:
                        intent = new Intent(getContext(), ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 2:
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mData.get(position).getName());
                        audio.setArticleid(mData.get(position).getId());
                        audio.setLastupdatetime(mData.get(position).getCreatetime());
                        audio.setArticletype(2);
                        audio.setMediaurl(mData.get(position).getMediaurl());
                        audio.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(getContext(), AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", 0);
                        intent.putExtra("single", true);
                        intent.putExtra("id", mData.get(position).getId());
                        getContext().startActivity(intent);
                        break;
                    case 3:
                        if (adapter.getItemViewType(position) == HotNewsAdapter.TYPE_VIDEO_B)
                            return;
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mData.get(position).getName());
                        video.setArticleid(mData.get(position).getId());
                        video.setLastupdatetime(mData.get(position).getCreatetime());
                        video.setArticletype(2);
                        video.setMediaurl(mData.get(position).getMediaurl());
                        video.setCoverimgurl(mData.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mData.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(getContext(), VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mData.get(0).getId());
                        getContext().startActivity(intent);
                        break;
                }

            }
        });
//        mAdapter.setOnItemClickLitener(new RecommendFollowAdapter.OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (!Util.hintLogin((BaseActivity) getActivity())) {
//                    return;
//                }
//                toHttpFocus(magazineLists.get(position).getId());
//            }
//        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent intent = new Intent(getContext(), NewsFlashCardActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("pages", 2);
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) news);
                getContext().startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

//    private void toHttpFocus(final String id) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("targetid", id);
//        map.put("type", 1);
//        ApiServerManager.getInstance().getApiServer().focusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
//                if (response.body().getCode() == 200) {
//
////                    Iterator<RecommendFollowBean> iterator = magazineLists.iterator();
////                    while (iterator.hasNext()) {
////                        RecommendFollowBean bean = iterator.next();
////                        if (TextUtils.equals(bean.getId(), id)) {
////                            iterator.remove();
////                            break;
////                        }
////                    }
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
//
//            }
//        });
//    }


    /**
     * 获取推荐关注列表
     */
    private void toHttpGetMagazineList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 10);
        ApiServerManager.getInstance().getApiServer().getRecommendFollow(map).enqueue(new RetrofitCallBack<List<RecommendFollowBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<RecommendFollowBean>>> call, Response<RetrofitResult<List<RecommendFollowBean>>> response) {
                if (response.body().getCode() == 200) {
                    magazineLists.removeAll(magazineLists);
                    magazineLists.addAll(response.body().getData());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<RecommendFollowBean>>> call, Throwable t) {
            }
        });
    }


    /**
     * 获取我的关注列表
     */
    private void toHttpMyFollowList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 10);
        ApiServerManager.getInstance().getApiServer().getMyRecFollow(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    mMyData.removeAll(mMyData);
                    if (response.body().getData().size() > 0) {
                        mMyData.addAll(response.body().getData());
                        followAdapter.notifyDataSetChanged();
                    }

                    if (mMyData.size() > 0) {
                        tvType.setText("我的关注");
                        lvRecMyFollow.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        tvMyFollowMore.setVisibility(View.VISIBLE);
                        lvRecMyFollow.post(new Runnable() {
                            @Override
                            public void run() {
                                //我也不知道为什么，不这么做就会无限的requestLayout()，甚是诡异
                                lvRecMyFollow.setVisibility(View.GONE);
                                lvRecMyFollow.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        tvType.setText("推荐关注");
                        lvRecMyFollow.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        tvMyFollowMore.setVisibility(View.GONE);
                        toHttpGetMagazineList();
                    }

                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                toHttpGetMagazineList();
            }
        });
    }

    private void initBanner() {
        mLoopAdapter = new BannerAdapter(rollPager, getContext(), mBannerList);
        rollPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Util.bannerJump(mBannerList.get(position), getContext(),position);
            }
        });
        mLoopAdapter.setOnCustomPageSelect(new LoopPagerAdapter.OnCustomPageSelect() {
            @Override
            public void onPageSelect(ViewGroup container, int position) {
                ImageView view = new ImageView(container.getContext());
                Glide.with(getContext()).load(SystemConstant.ALI_CLOUD + mBannerList.get(position).getImgurl())
                        .error(R.drawable.icon_banner_load)
                        .placeholder(R.drawable.icon_banner_load).into(view);
            }
        });
        rollPager.setAdapter(mLoopAdapter);
    }

    private void toHttpGetFlash() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getNews(map).enqueue(new RetrofitCallBack<List<FlashBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FlashBean>>> call, Response<RetrofitResult<List<FlashBean>>> response) {
                if (response.body().getCode() == 200) {
                    news.removeAll(news);
                    news.addAll(response.body().getData());
                    List<String> list = new ArrayList<String>();
                    for (FlashBean flashBean : news) {
                        list.add(flashBean.getContent());
                    }
                    marqueeView.startWithList(list);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FlashBean>>> call, Throwable t) {

            }
        });
    }

    private void initView(View view) {
        lvHot = (ListViewFinalLoadMore) view.findViewById(R.id.lv_hot);
        headView = LayoutInflater.from(getContext()).inflate(R.layout.head_knowledge, null, false);
        rollPager = (RollPagerView) headView.findViewById(R.id.roll_pager);
        marqueeView = (MarqueeView) headView.findViewById(R.id.marquee_view);
        lvRecMyFollow = (ShowAllListView) headView.findViewById(R.id.lv_rec_my_follow);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
//        horizontalGridView = (GridView) headView.findViewById(R.id.horizontal_gridview);
        mRecyclerView = (RecyclerView) headView.findViewById(R.id.id_recyclerview_horizontal);
        rlRecFollow = (AutoRelativeLayout) headView.findViewById(R.id.rl_rec_follow);
        tvType = (TextView) headView.findViewById(R.id.tv_type);
        tvMyFollowMore = (TextView) headView.findViewById(R.id.tv_my_follow_more);
        headView.findViewById(R.id.tv_news).setOnClickListener(this);
//        rlRecFollow.setOnClickListener(this);
        tvMyFollowMore.setOnClickListener(this);
    }

    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 12);
        map.put("pages", 1);
        map.put("pagesize", 6);
        ApiServerManager.getInstance().getApiServer().getBanner(map).enqueue(new RetrofitCallBack<List<BannerBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<BannerBean>>> call, Response<RetrofitResult<List<BannerBean>>> response) {
                if (response.body().getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(response.body().getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
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

    private void toHttpGetNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        if (pages == 1) {
            hotspotTimestamp = System.currentTimeMillis();
            map.put("periodstart", hotspotTimestamp);
        } else {
            map.put("periodstart", hotspotTimestamp);
        }
//        map.put("orientation", 1);
        ApiServerManager.getInstance().getApiServer().getHotRecommend(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                ptrLayout.onRefreshComplete();
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mData.removeAll(mData);
                    if (response.body().getData().size() > 0) {
                        lvHot.setHasLoadMore(true);
                        mData.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvHot.setHasLoadMore(false);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    lvHot.showFailUI();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvHot.showFailUI();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_news:
                startActivity(new Intent(getContext(), NewsFlashActivity.class));
                break;
//            case R.id.rl_rec_follow:
//                if (TextUtils.equals("我的关注", tvType.getText())) {
//                    tvType.setText("推荐关注");
//                    lvRecMyFollow.setVisibility(View.GONE);
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    tvMyFollowMore.setVisibility(View.GONE);
//                } else {
//                    tvType.setText("我的关注");
//                    lvRecMyFollow.setVisibility(View.VISIBLE);
//                    mRecyclerView.setVisibility(View.GONE);
//                    tvMyFollowMore.setVisibility(View.VISIBLE);
//                }
//                break;
            case R.id.tv_my_follow_more:
                startActivity(new Intent(getContext(), MyFocusActivity.class));
                break;
        }
    }

    //
//    private ViewPager viewPager;// 页卡内容
//    private List<Fragment> fragments;// Tab页面列表
//    private InfomationVPAdapter adapter;
//    private TabLayout tab_FindFragment_title; //定义TabLayout
//    private List<String> list_title; //tab名称列表
//
//
//    public KnowledgeFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
//        View view = inflater.inflate(R.layout.fragment_knowledge, container, false);
//        initView(view);
//        //初始化各fragment
//        InitViewPager();
//        return view;
//    }
//
//    private void initView(View view) {
//        tab_FindFragment_title = (TabLayout) view.findViewById(R.id.tab_FindFragment_title);
//        viewPager = (ViewPager) view.findViewById(R.id.vPager);
//        ((TextView) view.findViewById(R.id.maintitle_txt)).setText("知识");
//    }
//
//
//    /**
//     * 初始化Viewpager页
//     */
//    private void InitViewPager() {
//        fragments = new ArrayList<>();
//        fragments.add(new HotNewsFragment());
//        fragments.add(new VideoFragment());
//        fragments.add(new AllAlbumFragment());
//
//        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
//        list_title = new ArrayList<>();
//        list_title.add("图文");
//        list_title.add("视频");
//        list_title.add("音频");
//
//        //设置TabLayout的模式
//        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
//        //为TabLayout添加tab名称
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));
////        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(4)));
//        adapter = new InfomationVPAdapter(getActivity().getSupportFragmentManager(), fragments, list_title);
//
//        //viewpager加载adapter
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                JCVideoPlayer.releaseAllVideos();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        tab_FindFragment_title.setupWithViewPager(viewPager);
//    }
//
//
//    @Subscribe
//    public void onEvent(EventMsg msg) {
//        switch (msg.getWhat()) {
//            case SystemConstant.MOZ_REGISTER_HOT:
//                if (msg.getArg1() == 0) {
//                    viewPager.setCurrentItem(0);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
