package com.yidiankeyan.science.information.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.functionkey.activity.SearchActivity;
import com.yidiankeyan.science.information.acitivity.AudioActivity;
import com.yidiankeyan.science.information.acitivity.ColumnAllActivity;
import com.yidiankeyan.science.information.acitivity.InformationActivity;
import com.yidiankeyan.science.information.acitivity.MonthlySeriesActivity;
import com.yidiankeyan.science.information.acitivity.MozForumDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozInformationAllActivity;
import com.yidiankeyan.science.information.acitivity.MozInterviewActivity;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.information.acitivity.NewsFlashActivity;
import com.yidiankeyan.science.information.acitivity.RedCardActivity;
import com.yidiankeyan.science.information.acitivity.ScienceHelpActivity;
import com.yidiankeyan.science.information.acitivity.TheNewTodayAudioActivity;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.adapter.GridViewFMAdapter;
import com.yidiankeyan.science.information.adapter.MyHorizontalListAdapter;
import com.yidiankeyan.science.information.adapter.RecColumnAdapter;
import com.yidiankeyan.science.information.adapter.RecReadAdapter;
import com.yidiankeyan.science.information.adapter.RecommendTodayListAdapter;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.yidiankeyan.science.information.entity.MozActivityBean;
import com.yidiankeyan.science.information.entity.MozForumBean;
import com.yidiankeyan.science.information.entity.MozReadBean;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.entity.RecMagazineList;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.knowledge.activity.NewsFlashCardActivity;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.CustomRotateAnim;
import com.yidiankeyan.science.view.MarqueeView;
import com.yidiankeyan.science.view.NoScrollGridView;
import com.yidiankeyan.science.view.ObservableScrollView;
import com.yidiankeyan.science.view.ShowAllListView;
import com.yidiankeyan.science.view.rollviewpager.OnItemClickListener;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 资讯-推荐
 */
public class RecommendFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener ,ObservableScrollView.ScrollViewListener{

    private PtrClassicFrameLayout ptrLayout;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private Timer mTimer;
    private TimerTask mTimerTask;
    //点击进入推荐固定板块页面
    private AutoLinearLayout todayAchievement, llReadBook, llScienceHelp, llAudio, llVideo;
    //点击进入推荐定制板块页面
//    private AutoLinearLayout llBusiness,llRepair,ll_whiz, llKeYan,llXiaoShuo, llQimeng;

    private List<BannerBean> mBannerList = new ArrayList<>();
    private List<BannerBean> mpopList = new ArrayList<>();
    private TextView tvTitle;
    private List<String> titles = new ArrayList<>();

    /**
     * 活动快报
     */
    private List<MozActivityBean.ListDataBean> activityDataList = new ArrayList<>();
    private View activityLine;
    private AutoRelativeLayout rlColumn;
    private View vColumn, vColumnList;

    private IntentFilter intentFilter;

    //快讯
    private MarqueeView marqueeView;
    private List<FlashBean> news = new ArrayList<>();
    private AutoRelativeLayout rlNewsTitle;
    private View vNews;
    private View vNewsLine;
    private AutoRelativeLayout rlNewsAll;


    //日课
    private ListView lvToday;
    private AutoLinearLayout llSeeSign;
    //    private Map<String, List<OneDayArticles>> dataMap = new HashMap<>();
    private TextView tvDataState;
    private RecommendTodayListAdapter listAdapter;
    private AutoRelativeLayout rlTodayAll;
    private Animation operatingAnim;
    private AutoRelativeLayout rlToday;
    private ImageView imgTodayPlay;
    private AutoLinearLayout llRecPlayAudio;
//    private ArrayList<OneDayArticles> playList;
//    private int playPosition;

    //精品专栏
    private ShowAllListView lvListviewColumn;
    private ArrayList<SubscribeColumnBean.ListBean> mColumnList = new ArrayList<>();
    private RecColumnAdapter recColumnAdapter;
    private AutoRelativeLayout rlColumnAll;
    private AutoRelativeLayout rlColumnRandom;
    private int pages = 1;
    private SubscribeColumnBean columnBean;


    //墨子专访
    private ImageView imgInterviewBg;
    private AutoRelativeLayout rlInterviewAll;
    private AutoRelativeLayout rlInterview;
    private ArrayList<InterviewSoonBean> interviewList = new ArrayList<>();
    private View vInterviewLine;

    //墨子论坛
    private ImageView imgForumBg;
    private AutoRelativeLayout rlForum;
    private ArrayList<MozForumBean> forumList = new ArrayList<>();
    private View vForum;

    //推荐专辑
//    private NoScrollGridView gvAlbum;
//    private GridViewAdapter gridViewAdapter;
//    private List<NewRecommendBean.SimpleAlbumModlesBean> simpleAlbumModles = new ArrayList<>();
//    private TextView tvRecommend;
//    private AutoRelativeLayout rlRecommendAll;
//    private View lineRecommend;

    //推荐读书
    private MozReadBean mozReadBean;
    private ShowAllListView lvRead;
    private AutoRelativeLayout rlRead;
    //    private AutoRelativeLayout rlReadAll;
    private AutoRelativeLayout rlReadRandom;
    //    private RecReadAdapter recReadAdapter;
    private List<MozReadBean.ListBean> mReadList = new ArrayList<>();

    //墨子FM
    private NoScrollGridView gvFm;
    private ArrayList<EditorAlbum> mBusinessDatas = new ArrayList<>();
    private GridViewFMAdapter gridViewFmAdapter;
    private AutoRelativeLayout rlFmMore;
    private AutoRelativeLayout rlFmMoz;
    private View fmLine;


    //墨子杂志
    private RecyclerView horizontalGridView;
    private MyHorizontalListAdapter mAdapter;
    private ArrayList<RecMagazineList> magazineLists = new ArrayList<>();
    private AutoRelativeLayout rlMagazine;
    private View vMagazine;

    //知识红包
    private PopupWindow mPopupWindow;
    private RelativeLayout rlContent;
    private ImageView dragImg;
    private ImageView imgHongBao;
    private AutoLinearLayout relativeLayout;
    private ImageView tvTest;
    private CustomRotateAnim rotateAnim;
    private LinearLayout llRes;
    private View viewPacketClose;
    private AutoRelativeLayout layoutTitle;
    //    private SimpleDateFormat sdf;
    private AutoRelativeLayout rlKnowledgeAll;

    private PopupWindow reportPopupwindow;
    private View mClose_btn;
    private ImageView imgStart;
    private ObservableScrollView scrollView;
    private int Urlposition;

    private AnimationDrawable animationDrawable;
    private ImageView imgStaticPlay;
    private ImageView imgPlay;
    private AutoRelativeLayout llShare;
    private TextView tvSign;

    public SimpleDateFormat sdf;
    public Map<String, List<OneDayArticles>> dataMap = new HashMap<>();

    private static RecommendFragment instance;
    private RecReadAdapter recReadAdapter;
    private LayoutInflater inflater;
    private View loadmoreView;
    private int mlastIndex;
    private int mtotalIndex;
    private ObservableScrollView mscrollView;

    public static RecommendFragment getInstance() {
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.read.audio");
        getContext().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        //初始化控件
        initView(view);
        initAction(view);
        return view;
    }

    private void initView(View view) {   //
        ((TextView) view.findViewById(R.id.maintitle_txt)).setText("资讯");
        view.findViewById(R.id.title_btn).setVisibility(View.VISIBLE);
        imgPlay = (ImageView) view.findViewById(R.id.img_play);
        imgStaticPlay = (ImageView) view.findViewById(R.id.img_static_play);
        llShare = (AutoRelativeLayout) view.findViewById(R.id.ll_share);
        animationDrawable = (AnimationDrawable) imgPlay.getDrawable();
        tvSign = (TextView) view.findViewById(R.id.tv_sign);
        view.findViewById(R.id.title_btn).setOnClickListener(this);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        llScienceHelp = (AutoLinearLayout) view.findViewById(R.id.ll_science_help);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        todayAchievement = (AutoLinearLayout) view.findViewById(R.id.ll_today_achievement);
        llReadBook = (AutoLinearLayout) view.findViewById(R.id.ll_read_book);
        llVideo = (AutoLinearLayout) view.findViewById(R.id.ll_video);
        activityLine = view.findViewById(R.id.activity_line);
        llAudio = (AutoLinearLayout) view.findViewById(R.id.ll_audio);
        lvRead = (ShowAllListView) view.findViewById(R.id.lv_listview_read);
        rlRead = (AutoRelativeLayout) view.findViewById(R.id.rl_read);
//        rlReadAll = (AutoRelativeLayout) view.findViewById(R.id.rl_read_all);
        rlColumn = (AutoRelativeLayout) view.findViewById(R.id.rl_column);
        rlColumnRandom = (AutoRelativeLayout) view.findViewById(R.id.rl_column_random);
        vColumn = view.findViewById(R.id.v_column);
        vColumnList = view.findViewById(R.id.v_column_list);
        gvFm = (NoScrollGridView) view.findViewById(R.id.gv_fm);
        llRecPlayAudio = (AutoLinearLayout) view.findViewById(R.id.ll_rec_play_audio);
        imgTodayPlay = (ImageView) view.findViewById(R.id.img_today_play);
        // 用HorizontalScrollView设置横向GridView
        horizontalGridView = (RecyclerView) view.findViewById(R.id.horizontal_gridview);
        dragImg = (ImageView) view.findViewById(R.id.dragImg);
        tvTest = (ImageView) view.findViewById(R.id.tv_test);
        layoutTitle = (AutoRelativeLayout) view.findViewById(R.id.layout_title);
        relativeLayout = (AutoLinearLayout) view.findViewById(R.id.ll_recommend);
        llRes = (LinearLayout) view.findViewById(R.id.ll_res);
        rlKnowledgeAll = (AutoRelativeLayout) view.findViewById(R.id.rl_knowledge_all);
        rlFmMore = (AutoRelativeLayout) view.findViewById(R.id.rl_fm_more);
        lvToday = (ListView) view.findViewById(R.id.lv_history);
        tvDataState = (TextView) view.findViewById(R.id.tv_data_state);
        rlTodayAll = (AutoRelativeLayout) view.findViewById(R.id.rl_today_all);
        rlToday = (AutoRelativeLayout) view.findViewById(R.id.rl_today);
        rlFmMoz = (AutoRelativeLayout) view.findViewById(R.id.rl_moz_fm);
        fmLine = view.findViewById(R.id.fm_line);
        imgInterviewBg = (ImageView) view.findViewById(R.id.img_interview_bg);
        rlMagazine = (AutoRelativeLayout) view.findViewById(R.id.rl_magazine);
        vMagazine = view.findViewById(R.id.v_magazine);
        rlInterviewAll = (AutoRelativeLayout) view.findViewById(R.id.rl_interview_all);
        rlInterview = (AutoRelativeLayout) view.findViewById(R.id.rl_interview);
        imgForumBg = (ImageView) view.findViewById(R.id.img_forum_bg);
        rlForum = (AutoRelativeLayout) view.findViewById(R.id.rl_forum);
        vForum = view.findViewById(R.id.v_forum);
        rlReadRandom = (AutoRelativeLayout) view.findViewById(R.id.rl_read_random);
        llSeeSign = (AutoLinearLayout) view.findViewById(R.id.ll_see_sign);
        lvListviewColumn = (ShowAllListView) view.findViewById(R.id.lv_listview_column);
        rlColumnAll = (AutoRelativeLayout) view.findViewById(R.id.rl_column_all);
        marqueeView = (MarqueeView) view.findViewById(R.id.marquee_view);
        rlNewsTitle = (AutoRelativeLayout) view.findViewById(R.id.rl_news_title);
        vNews = view.findViewById(R.id.v_news);
        vNewsLine = view.findViewById(R.id.v_news_line);
        vInterviewLine = view.findViewById(R.id.v_interview_line);
        rlNewsAll = (AutoRelativeLayout) view.findViewById(R.id.rl_news_all);
        mscrollView = (ObservableScrollView) view.findViewById(R.id.scroll_view);
        mscrollView.setScrollViewListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAction(View view) {

        tvDataState.setVisibility(View.GONE);
        RecommendFragment.getInstance().sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "signtime")) ||
                !TextUtils.equals(SpUtils.getStringSp(getContext(), "signtime"),
                        RecommendFragment.getInstance().sdf.format(new Date()))) {
            toHttpGetPopBanner();
        }

        //给listview 设置数据
        recColumnAdapter = new RecColumnAdapter(mColumnList, getContext());
        recReadAdapter = new RecReadAdapter(mReadList, getContext());
//        gridViewAdapter = new GridViewAdapter(getContext(), simpleAlbumModles);
        gridViewFmAdapter = new GridViewFMAdapter(getContext(), mBusinessDatas);
        listAdapter = new RecommendTodayListAdapter(getActivity());
        if (mBannerList.size() == 0) {
            toHttpGetBanner();
        }
//        if (activityDataList.size() == 0) {
//            toHttpGetActivityInfo();
//        }
        mRollViewPager = (RollPagerView) view.findViewById(R.id.vp_school_course);
        mLoopAdapter = new BannerAdapter(mRollViewPager, getContext(), mBannerList);
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Urlposition = position;
                Util.bannerJump(mBannerList.get(position), getContext(), position);
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
        mRollViewPager.setAdapter(mLoopAdapter);

        //--------杂志
        //设置水平横向滑动的参数

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalGridView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyHorizontalListAdapter(getContext(), magazineLists);
        horizontalGridView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyHorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), MonthlySeriesActivity.class);
                intent.putExtra("id", magazineLists.get(position).getId());
                intent.putExtra("name", magazineLists.get(position).getName());
                startActivity(intent);
            }
        });

        initActivity();
        //设置精品专栏adapter
        lvListviewColumn.setAdapter(recColumnAdapter);
        //设置推荐读书adapter
        lvRead.setAdapter(recReadAdapter);
//        gvAlbum.setAdapter(gridViewAdapter);
        //设置热门音频adapter
        gvFm.setAdapter(gridViewFmAdapter);
        listAdapter.setDatas(RecommendFragment.getInstance().dataMap.get(RecommendFragment.getInstance().sdf.format(new Date())));
        lvToday.setAdapter(listAdapter);
        todayAchievement.setOnClickListener(this);
        llReadBook.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llScienceHelp.setOnClickListener(this);
        llAudio.setOnClickListener(this);
//        rlReadAll.setOnClickListener(this);
        llRecPlayAudio.setOnClickListener(this);
//        rlRecommendAll.setOnClickListener(this);
        rlKnowledgeAll.setOnClickListener(this);
        rlFmMore.setOnClickListener(this);
        rlTodayAll.setOnClickListener(this);
        llShare.setOnClickListener(this);
        imgInterviewBg.setOnClickListener(this);
        rlInterviewAll.setOnClickListener(this);
        imgForumBg.setOnClickListener(this);
        rlReadRandom.setOnClickListener(this);
        llSeeSign.setOnClickListener(this);
        rlColumnAll.setOnClickListener(this);
        rlColumnRandom.setOnClickListener(this);
        rlNewsAll.setOnClickListener(this);
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
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttpInterviewSoon();
                toHttpGetFlash();
                toHttpGetMagazineList();
                toHttpForum();
                toHttpGetSubColumn();
                toHttpGetMozRead();
//                toHttpGetRecommend();
//                toHttpGetMozFm();
                if (RecommendFragment.getInstance().sdf == null)
                    RecommendFragment.getInstance().sdf = new SimpleDateFormat("yyyy-MM-dd");
                toHttpGetToday(RecommendFragment.getInstance().sdf.format(new Date()));
            }
        });
        if (mColumnList.size() == 0 && mReadList.size() == 0 && mBusinessDatas.size() == 0) {
            ptrLayout.autoRefresh();
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        relativeLayout.setFocusable(false);
        // 获取自定义动画实例
        //rotateAnim = new CustomRotateAnim();
        //CustomRotateAnim.getCustomRotateAnim();
        rotateAnim = new CustomRotateAnim();
        // 一次动画执行1秒
        rotateAnim.setDuration(2000);
        // 设置为循环播放
        rotateAnim.setRepeatCount(-1);
        // 设置为匀速
        rotateAnim.setInterpolator(new LinearInterpolator());
        // 开始播放动画
        dragImg.startAnimation(rotateAnim);
        inflater = LayoutInflater.from(getContext());
        loadmoreView = inflater.inflate(R.layout.load_more, null);//获得刷新视图
        lvRead.setOnScrollListener(this);
        lvRead.addFooterView(loadmoreView,null,false);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            JCVideoPlayer.releaseAllVideos();
        }
    }

    /**
     * 快讯
     */
    private void toHttpGetFlash() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        ApiServerManager.getInstance().getApiServer().getNews(map).enqueue(new RetrofitCallBack<List<FlashBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<FlashBean>>> call, Response<RetrofitResult<List<FlashBean>>> response) {
                if (response.body().getCode() == 200) {
                    news.removeAll(news);
                    if (response.body().getData().size() > 0) {
                        news.addAll(response.body().getData());
                        List<String> list = new ArrayList<String>();
                        String mTitle = "";
                        for (FlashBean flashBean : news) {
                            if (!TextUtils.isEmpty(flashBean.getTitle()) && !TextUtils.equals("null", flashBean.getTitle())) {
                                mTitle = "【" + flashBean.getTitle() + "】";
                            } else {
                                mTitle = "";
                            }
                            list.add(flashBean.getCreatetimestr() + mTitle + flashBean.getContent());
                        }
                        marqueeView.startWithList(list);
                    } else {
                        marqueeView.setVisibility(View.GONE);
                        rlNewsTitle.setVisibility(View.GONE);
                        vNews.setVisibility(View.GONE);
                        vNewsLine.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<FlashBean>>> call, Throwable t) {
                marqueeView.setVisibility(View.GONE);
                rlNewsTitle.setVisibility(View.GONE);
                vNews.setVisibility(View.GONE);
                vNewsLine.setVisibility(View.GONE);
            }
        });
    }


    //日课
    private void toHttpGetToday(final String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("datetime", date);
        tvDataState.setVisibility(View.VISIBLE);
        tvDataState.setText("正在加载");
        HttpUtil.post(getActivity(), SystemConstant.URL + SystemConstant.GET_ONE_DAY_ARTICLES, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<OneDayArticles> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), OneDayArticles.class);
//                    if (list != null && list.size() != 0) {
//                        for (int i = 0; i < list.size(); i++) {
//                            if (i < 3) {
//                                list.get(i).setTitle(i + "");
//                                list.get(i).setAudiourl(audios[i]);
//                            }
//                        }
//                    }

                    RecommendFragment.getInstance().dataMap.put(date, list);
                    if (date.equals(RecommendFragment.getInstance().sdf.format(new Date())))
                        listAdapter.setDatas(RecommendFragment.getInstance().dataMap.get(date));
                    listAdapter.notifyDataSetChanged();
                    tvDataState.setVisibility(View.GONE);
                }
                if (RecommendFragment.getInstance().dataMap.get(date) == null || RecommendFragment.getInstance().dataMap.get(date).size() == 0) {
                    tvDataState.setVisibility(View.VISIBLE);
                    listAdapter.setDatas(null);
                    listAdapter.notifyDataSetChanged();
                    tvDataState.setText("敬请期待");
                    llRecPlayAudio.setEnabled(false);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                tvDataState.setVisibility(View.VISIBLE);
                tvDataState.setText("敬请期待");
            }
        });
    }

    private void showPayWindow() {
        if (mPopupWindow == null) {
            View view = View.inflate(getActivity(), R.layout.popupwindow_red_packet, null);
            imgHongBao = (ImageView) view.findViewById(R.id.img_red_packet);
            viewPacketClose = view.findViewById(R.id.view_packet_close);
            imgHongBao.setOnClickListener(this);
            viewPacketClose.setOnClickListener(this);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    finishPop(getActivity(), mPopupWindow);
                    llRes.setVisibility(View.VISIBLE);
                    dragImg.setVisibility(View.VISIBLE);
                    tvTest.setVisibility(View.VISIBLE);
                    // 开始播放动画
                    dragImg.startAnimation(rotateAnim);
                }
            });
            mPopupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.KNOWLEDGE_CARD_CLOSE:
                finishPop(getActivity(), mPopupWindow);
                rlKnowledgeAll.setVisibility(View.VISIBLE);
                llRes.setVisibility(View.GONE);
                dragImg.setVisibility(View.GONE);
                tvTest.setVisibility(View.GONE);
                // 开始播放动画
//                dragImg.startAnimation(rotateAnim);
                break;
//            case SystemConstant.INFO_AUDIO_PLAYER:
//                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP
//                        || AudioPlayManager.getInstance().CURRENT_STATE == 0) {
//                    RecommendFragment.getInstance().playList = (ArrayList<OneDayArticles>) RecommendFragment.getInstance().dataMap.get(RecommendFragment.getInstance().sdf.format(new Date()));
//                }
//                if (RecommendFragment.getInstance().playList == null || RecommendFragment.getInstance().playList.size() == 0) {
//                    Toast.makeText(getActivity(), "这天没有可播放的内容", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent(getContext(), AudioControlActivity.class);
//                intent.putParcelableArrayListExtra("playList", RecommendFragment.getInstance().playList);
//                intent.putExtra("playPosition", RecommendFragment.getInstance().playPosition);
//                startActivity(intent);
//                break;
            case SystemConstant.ON_PLAYING:
                listAdapter.notifyDataSetChanged();
                if (!animationDrawable.isRunning()) {
                    imgStaticPlay.setVisibility(View.GONE);
                    imgPlay.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                }
                imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_stops);
                break;
            case SystemConstant.ON_PAUSE:
                listAdapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_STOP:
                listAdapter.notifyDataSetChanged();
                if (animationDrawable.isRunning()) {
                    imgStaticPlay.setVisibility(View.VISIBLE);
                    imgPlay.setVisibility(View.GONE);
                    animationDrawable.stop();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_plays);
                    }
                });
                break;
            case SystemConstant.ON_PREPARE:
                imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_plays);
                break;
            case SystemConstant.MAINACTIVITY_AUDIO_REFRESH:
                if (AudioPlayManager.getInstance().getType() == AudioPlayManager.PlayType.OneDayArticles) {
                    if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP) {
                        imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_plays);
                    } else {
                        imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_stops);
                    }
                } else {
                    imgTodayPlay.setImageResource(R.drawable.icon_rec_recomm_plays);
                }
                break;
        }
    }

    private void toHttpIsSigned() {
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            ApiServerManager.getInstance().getApiServer().isSigned().enqueue(new RetrofitCallBack<Boolean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Boolean>> call, Response<RetrofitResult<Boolean>> response) {
                    if (response.body().getCode() == 200) {
                        if (response.body().getData()) {
                            tvSign.setText("查看签到");
                        } else {
                            tvSign.setText("打卡签到");
                        }
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<Boolean>> call, Throwable t) {

                }
            });
        } else {
            tvSign.setText("打卡签到");
        }
    }


    /**
     * 弹出签到框
     */
    private void showReportPop() {
        if (reportPopupwindow == null) {
            View view = View.inflate(getActivity(), R.layout.activity_start_dialog, null);
            mClose_btn = view.findViewById(R.id.btn_closes);
            imgStart = (ImageView) view.findViewById(R.id.start_img);
            for (int i = 0; i < mpopList.size(); i++) {
                Glide.with(this).load(Util.getImgUrl(mpopList.get(i).getImgurl()))
                        .placeholder(R.drawable.icon_banner_placeholder)
                        .error(R.drawable.icon_banner_error).into(imgStart);
            }
            mClose_btn.setOnClickListener(this);
            imgStart.setOnClickListener(this);
            reportPopupwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            reportPopupwindow.setContentView(view);
            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            reportPopupwindow.setFocusable(true);
            reportPopupwindow.setOutsideTouchable(true);
            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), reportPopupwindow);
                }
            });
            reportPopupwindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
//            reportPopupwindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
        }
    }


    /**
     * 获取墨子读书列表
     */
    private void toHttpGetMozRead() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
//        map.put("checkstatus", 2);
        ApiServerManager.getInstance().getApiServer().getMozReadList(map).enqueue(new RetrofitCallBack<MozReadBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<MozReadBean>> call, Response<RetrofitResult<MozReadBean>> response) {
                if(pages == 1){
                    mReadList.removeAll(mReadList);
                }
                if (response.body().getCode() == 200) {
//                    mReadList.removeAll(mReadList);
                    mozReadBean = response.body().getData();
                    if(mozReadBean.getList().size() == 0){
                        loadmoreView.setVisibility(View.GONE);
                    }else
                    mReadList.addAll(mozReadBean.getList());
                }
                if (mReadList.size() == 0) {
                    lvRead.setVisibility(View.GONE);
                    rlRead.setVisibility(View.GONE);
//                    rlReadAll.setVisibility(View.GONE);
                    rlReadRandom.setVisibility(View.GONE);
                    loadmoreView.setVisibility(View.GONE);
                } else {
                    lvRead.setVisibility(View.VISIBLE);
                    rlRead.setVisibility(View.VISIBLE);
//                    rlReadAll.setVisibility(View.VISIBLE);
                    rlReadRandom.setVisibility(View.VISIBLE);
                }
                recReadAdapter.notifyDataSetChanged();
                ptrLayout.onRefreshComplete();
                isLoad = false;
            }

            @Override
            public void onFailure(Call<RetrofitResult<MozReadBean>> call, Throwable t) {
                if (mReadList.size() > 0) {
                    lvRead.setVisibility(View.VISIBLE);
                    rlRead.setVisibility(View.VISIBLE);
//                    rlReadAll.setVisibility(View.VISIBLE);
                    rlReadRandom.setVisibility(View.VISIBLE);
                } else {
                    lvRead.setVisibility(View.GONE);
                    rlRead.setVisibility(View.GONE);
//                    rlReadAll.setVisibility(View.GONE);
                    rlReadRandom.setVisibility(View.GONE);
                }
                ptrLayout.onRefreshComplete();
                isLoad = false;
            }
        });
    }

    /**
     * 获取墨子读书
     * 换一批
     */
    private void toHttpBookPickothers() {
        ApiServerManager.getInstance().getApiServer().getBookPickothers(3).enqueue(new RetrofitCallBack<List<MozReadBean.ListBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<MozReadBean.ListBean>>> call, Response<RetrofitResult<List<MozReadBean.ListBean>>> response) {
                if (response.body().getCode() == 200) {
                    mReadList.removeAll(mReadList);
                    mReadList.addAll(response.body().getData());
//                    recReadAdapter.notifyDataSetChanged();
                }
                if (mReadList.size() == 0) {
                    lvRead.setVisibility(View.GONE);
                    rlRead.setVisibility(View.GONE);
//                    rlReadAll.setVisibility(View.GONE);
                    rlReadRandom.setVisibility(View.GONE);
                } else {
                    lvRead.setVisibility(View.VISIBLE);
                    rlRead.setVisibility(View.VISIBLE);
//                    rlReadAll.setVisibility(View.VISIBLE);
                    rlReadRandom.setVisibility(View.VISIBLE);
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<MozReadBean.ListBean>>> call, Throwable t) {
                if (mReadList.size() > 0) {
                    lvRead.setVisibility(View.VISIBLE);
                    rlRead.setVisibility(View.VISIBLE);
//                    rlReadAll.setVisibility(View.VISIBLE);
                    rlReadRandom.setVisibility(View.VISIBLE);
                } else {
                    lvRead.setVisibility(View.GONE);
                    rlRead.setVisibility(View.GONE);
//                    rlReadAll.setVisibility(View.GONE);
                    rlReadRandom.setVisibility(View.GONE);
                }
                ptrLayout.onRefreshComplete();
            }
        });
    }

    /**
     * 获取推荐专辑
     */
//    private void toHttpGetRecommend() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", 1);
//        map.put("pagesize", 6);
//        ApiServerManager.getInstance().getApiServer().getRecommendAlbum(map).enqueue(new RetrofitCallBack<NewRecommendBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<NewRecommendBean>> call, Response<RetrofitResult<NewRecommendBean>> response) {
//                if (response.body().getCode() == 200) {
//                    simpleAlbumModles.removeAll(simpleAlbumModles);
//                    simpleAlbumModles.addAll(response.body().getData().getSimpleAlbumModles());
//                    gridViewAdapter.notifyDataSetChanged();
//                    if (simpleAlbumModles.size() > 0) {
//                        tvRecommend.setVisibility(View.VISIBLE);
//                        gvAlbum.setVisibility(View.VISIBLE);
//                        rlRecommendAll.setVisibility(View.VISIBLE);
//                        lineRecommend.setVisibility(View.VISIBLE);
//                    } else {
//                        tvRecommend.setVisibility(View.GONE);
//                        gvAlbum.setVisibility(View.GONE);
//                        rlRecommendAll.setVisibility(View.GONE);
//                        lineRecommend.setVisibility(View.GONE);
//                    }
//                } else {
//                    tvRecommend.setVisibility(View.GONE);
//                    gvAlbum.setVisibility(View.GONE);
//                    rlRecommendAll.setVisibility(View.GONE);
//                    lineRecommend.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<NewRecommendBean>> call, Throwable t) {
//                tvRecommend.setVisibility(View.GONE);
//                gvAlbum.setVisibility(View.GONE);
//                rlRecommendAll.setVisibility(View.GONE);
//                lineRecommend.setVisibility(View.GONE);
//            }
//        });
//    }


    /**
     * 获取墨子FM
     */
//    private void toHttpGetMozFm() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("pages", 1);
//        map.put("pagesize", 4);
//        Map<String, Object> entity = new HashMap<>();
//        entity.put("belongtype", 2);
//        entity.put("belongid", 1004);
//        map.put("entity", entity);
//        ApiServerManager.getInstance().getApiServer().getRecommendFm(map).enqueue(new RetrofitCallBack<List<RecentContentBean>>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<List<RecentContentBean>>> call, Response<RetrofitResult<List<RecentContentBean>>> response) {
//                if (response.body().getCode() == 200) {
//                    mBusinessDatas.removeAll(mBusinessDatas);
//                    mBusinessDatas.addAll(response.body().getData());
//                    gridViewFmAdapter.notifyDataSetChanged();
//                    if (mBusinessDatas.size() > 0) {
//                        rlFmMoz.setVisibility(View.VISIBLE);
//                        fmLine.setVisibility(View.VISIBLE);
//                        gvFm.setVisibility(View.VISIBLE);
//                    } else {
//                        rlFmMoz.setVisibility(View.GONE);
//                        fmLine.setVisibility(View.GONE);
//                        gvFm.setVisibility(View.GONE);
//                    }
//                } else {
//                    rlFmMoz.setVisibility(View.GONE);
//                    fmLine.setVisibility(View.GONE);
//                    gvFm.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<List<RecentContentBean>>> call, Throwable t) {
//                rlFmMoz.setVisibility(View.GONE);
//                fmLine.setVisibility(View.GONE);
//                gvFm.setVisibility(View.GONE);
//            }
//        });
//    }

    //热门音频
    private void toHttpGetMozFm() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 4);
        Map<String, Object> entity = new HashMap<>();
        entity.put("albumtype", 2);
        entity.put("belongtype", 1);
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getEditorAlbum(map).enqueue(new RetrofitCallBack<ArrayList<EditorAlbum>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<EditorAlbum>>> call, Response<RetrofitResult<ArrayList<EditorAlbum>>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getCode() == 200) {
                        mBusinessDatas.removeAll(mBusinessDatas);
                        mBusinessDatas.addAll(response.body().getData());
                        gridViewFmAdapter.notifyDataSetChanged();
                        if (mBusinessDatas.size() > 0) {
                            rlFmMoz.setVisibility(View.VISIBLE);
                            fmLine.setVisibility(View.VISIBLE);
                            gvFm.setVisibility(View.VISIBLE);
                        } else {
                            rlFmMoz.setVisibility(View.GONE);
                            fmLine.setVisibility(View.GONE);
                            gvFm.setVisibility(View.GONE);
                        }
                    } else {
                        rlFmMoz.setVisibility(View.GONE);
                        fmLine.setVisibility(View.GONE);
                        gvFm.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<EditorAlbum>>> call, Throwable t) {
                rlFmMoz.setVisibility(View.GONE);
                fmLine.setVisibility(View.GONE);
                gvFm.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 初始化活动
     */
    private void initActivity() {
        if (activityDataList != null && activityDataList.size() != 0) {
            activityLine.setVisibility(View.VISIBLE);
            tvTitle.setText(titles.get(0));
        } else {
            activityLine.setVisibility(View.GONE);
        }
    }


    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 1);
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

    private void toHttpGetPopBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 11);
        map.put("pages", 1);
        map.put("pagesize", 6);
        ApiServerManager.getInstance().getApiServer().getBanner(map).enqueue(new RetrofitCallBack<List<BannerBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<BannerBean>>> call, Response<RetrofitResult<List<BannerBean>>> response) {
                if (response.body().getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(response.body().getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
                    mpopList.removeAll(mpopList);
                    mpopList.addAll(data);
                    if (mpopList.size() > 0) {
                        showReportPop();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<BannerBean>>> call, Throwable t) {

            }
        });
    }


    /**
     * 获取精品专栏列表
     */
    private void toHttpGetSubColumn() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageSize", 3);
        ApiServerManager.getInstance().getApiServer().getRandomColumn(map).enqueue(new RetrofitCallBack<SubscribeColumnBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<SubscribeColumnBean>> call, Response<RetrofitResult<SubscribeColumnBean>> response) {
                if (response.body().getCode() == 200) {
                    mColumnList.removeAll(mColumnList);
                    columnBean = response.body().getData();
                    mColumnList.addAll(columnBean.getList());
                    recColumnAdapter.notifyDataSetChanged();
                }
                if (mColumnList.size() == 0) {
                    lvListviewColumn.setVisibility(View.GONE);
                    rlColumn.setVisibility(View.GONE);
                    rlColumnRandom.setVisibility(View.GONE);
                    vColumn.setVisibility(View.GONE);
                    vColumnList.setVisibility(View.GONE);
                } else {
                    lvListviewColumn.setVisibility(View.VISIBLE);
                    rlColumn.setVisibility(View.VISIBLE);
                    rlColumnRandom.setVisibility(View.VISIBLE);
                    vColumn.setVisibility(View.VISIBLE);
                    vColumnList.setVisibility(View.VISIBLE);
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<SubscribeColumnBean>> call, Throwable t) {
                if (mColumnList.size() == 0) {
                    lvListviewColumn.setVisibility(View.GONE);
                    rlColumn.setVisibility(View.GONE);
                    rlColumnRandom.setVisibility(View.GONE);
                    vColumn.setVisibility(View.GONE);
                    vColumnList.setVisibility(View.GONE);
                } else {
                    lvListviewColumn.setVisibility(View.VISIBLE);
                    rlColumn.setVisibility(View.VISIBLE);
                    rlColumnRandom.setVisibility(View.VISIBLE);
                    vColumn.setVisibility(View.VISIBLE);
                    vColumnList.setVisibility(View.VISIBLE);
                }
                ptrLayout.onRefreshComplete();
            }
        });
    }


    /**
     * 获取杂志列表
     */
    private void toHttpGetMagazineList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().GetRecMagazineList(map).enqueue(new RetrofitCallBack<List<RecMagazineList>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<RecMagazineList>>> call, Response<RetrofitResult<List<RecMagazineList>>> response) {
                if (response.body().getCode() == 200) {
                    List<RecMagazineList> mData = response.body().getData();
                    magazineLists.removeAll(magazineLists);
                    magazineLists.addAll(mData);
                    mAdapter.notifyDataSetChanged();
                }

                if (magazineLists.size() == 0) {
                    horizontalGridView.setVisibility(View.GONE);
                    rlMagazine.setVisibility(View.GONE);
                    vMagazine.setVisibility(View.GONE);
                } else {
                    horizontalGridView.setVisibility(View.VISIBLE);
                    rlMagazine.setVisibility(View.VISIBLE);
                    vMagazine.setVisibility(View.VISIBLE);
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<RecMagazineList>>> call, Throwable t) {
                if (magazineLists.size() == 0) {
                    horizontalGridView.setVisibility(View.GONE);
                    rlMagazine.setVisibility(View.GONE);
                    vMagazine.setVisibility(View.GONE);
                } else {
                    horizontalGridView.setVisibility(View.VISIBLE);
                    rlMagazine.setVisibility(View.VISIBLE);
                    vMagazine.setVisibility(View.VISIBLE);
                }
                ptrLayout.onRefreshComplete();
            }
        });
    }


    //专访
    private void toHttpInterviewSoon() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 1);
        map.put("onlineStatus", "1");
        map.put("top", "1");

        ApiServerManager.getInstance().getApiServer().getInterViewSoonList(map).enqueue(new RetrofitCallBack<ArrayList<InterviewSoonBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<InterviewSoonBean>>> call, Response<RetrofitResult<ArrayList<InterviewSoonBean>>> response) {
                if (response.body().getCode() == 200) {
                    interviewList = response.body().getData();
                    Glide.with(getContext()).load(Util.getImgUrl(interviewList.get(0).getInterviewImgUrl()))
                            .placeholder(R.drawable.icon_banner_load)
                            .error(R.drawable.icon_banner_load).into(imgInterviewBg);
                } else {
                    rlInterview.setVisibility(View.GONE);
                    imgInterviewBg.setVisibility(View.GONE);
                    vInterviewLine.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<InterviewSoonBean>>> call, Throwable t) {
                rlInterview.setVisibility(View.GONE);
                imgInterviewBg.setVisibility(View.GONE);
                vInterviewLine.setVisibility(View.GONE);
            }
        });
    }


    //墨子论坛
    private void toHttpForum() {
        Map<String, Object> map = new HashMap<>();

        ApiServerManager.getInstance().getApiServer().getForumList(map).enqueue(new RetrofitCallBack<ArrayList<MozForumBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<MozForumBean>>> call, Response<RetrofitResult<ArrayList<MozForumBean>>> response) {
                if (response.body().getCode() == 200) {
                    forumList = response.body().getData();
                    Glide.with(getContext()).load(Util.getImgUrl(forumList.get(0).getForumImgUrl()))
                            .placeholder(R.drawable.icon_banner_load)
                            .error(R.drawable.icon_banner_load).into(imgForumBg);
                } else {
                    rlForum.setVisibility(View.GONE);
                    imgForumBg.setVisibility(View.GONE);
                    vForum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<MozForumBean>>> call, Throwable t) {
                rlForum.setVisibility(View.GONE);
                imgForumBg.setVisibility(View.GONE);
                vForum.setVisibility(View.GONE);
            }
        });
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.read.audio")) {
                ptrLayout.autoRefresh();
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_btn:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
//            case R.id.ll_share:
////                EventMsg msg = EventMsg.obtain(SystemConstant.INFO_AUDIO_PLAYER);
////                EventBus.getDefault().post(msg);
//                JCVideoPlayer.releaseAllVideos();
//                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP
//                        || AudioPlayManager.getInstance().CURRENT_STATE == 0) {
//                    RecommendFragment.getInstance().playList = (ArrayList<OneDayArticles>) dataMap.get(sdf.format(new Date()));
//                }
//                if (RecommendFragment.getInstance().playList == null || RecommendFragment.getInstance().playList.size() == 0) {
//                    ToastMaker.showShortToast("今天没有可播放的内容");
//                    return;
//                }
//                intent = new Intent(getContext(), AudioControlActivity.class);
//                LogUtils.e(RecommendFragment.getInstance().playList.toString());
//                intent.putParcelableArrayListExtra("playList", RecommendFragment.getInstance().playList);
//                for (int i = 0; i < RecommendFragment.getInstance().playList.size(); i++) {
//                    if (TextUtils.equals(RecommendFragment.getInstance().playList.get(i).getId(), AudioPlayManager.getInstance().getmMediaPlayId())) {
//                        playPosition = i;
//                        break;
//                    }
//                }
//                intent.putExtra("playPosition", playPosition);
//                startActivity(intent);
//                break;
            //播放控制器
            case R.id.ll_rec_play_audio:
                if (listAdapter.getDatas().size() > 0) {
                    AudioPlayManager.getInstance().init(listAdapter.getDatas(), 0, AudioPlayManager.PlayModel.ORDER);
                    AudioPlayManager.getInstance().ijkStart();
                    DemoApplication.isPlay = true;
                    listAdapter.notifyDataSetChanged();
                }
                break;
//            //日课
//            case R.id.ll_today_achievement:
////                startActivity(new Intent(getContext(), TodayAchievementActivity.class));
//                intent = new Intent(getContext(), TheNewTodayAudioActivity.class);
//                startActivity(intent);
//                break;
            case R.id.ll_today_achievement:
                //专栏
                startActivity(new Intent(getContext(), ColumnAllActivity.class));
                break;
            //科答
            case R.id.ll_science_help:
                startActivity(new Intent(getContext(), ScienceHelpActivity.class));
                break;
//            //墨子读书 -全部内容
//            case R.id.rl_read_all:
//                startActivity(new Intent(getContext(), MozInformationAllActivity.class));
//                break;
            //推荐专辑
//            case R.id.rl_recommend_all:
//                startActivity(new Intent(getContext(), EditorActivity.class));
//                break;
//            //墨子FM
//            case R.id.ll_today_sciencefm:
//                startActivity(new Intent(getContext(), ScienceFMActivity.class));
//                break;
            //进入定制专题
//            case R.id.ll_custom:
//                if (TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
//                    startActivity(new Intent(getContext(), LoginActivity.class));
//                    return;
//                }
//                startActivity(new Intent(getContext(), CustomProjectActivity.class));
//                break;
            //日课列表 -全部内容
            case R.id.rl_today_all:
                intent = new Intent(getContext(), TheNewTodayAudioActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_see_sign:
                //查看签到
                intent = new Intent(getContext(), TheNewTodayAudioActivity.class);
                intent.putExtra("expand", true);
                startActivity(intent);
                break;
            case R.id.rl_column_random:
                //专栏 -换一批
                toHttpGetSubColumn();
                break;
            //墨子读书 -全部内容
            case R.id.ll_read_book:
                startActivity(new Intent(getContext(), MozInformationAllActivity.class));
                break;
            case R.id.rl_read_random:
//                toHttpBookPickothers();
                break;
            //视频列表 -全部内容
            case R.id.ll_video:
//                startActivity(new Intent(getContext(), VideoActivity.class));
                startActivity(new Intent(getContext(), InformationActivity.class));
//                startActivity(new Intent(getContext(), TheNewTodayAudioActivity.class));
                break;
            //音频列表 -全部内容
            case R.id.ll_audio:
//                startActivity(new Intent(getContext(), AudioActivity.class));
                startActivity(new Intent(getContext(), NewsFlashActivity.class));
                break;
            //知识红包 -全部内容
            case R.id.rl_knowledge_all:
                intent = new Intent(getContext(), RedCardActivity.class);
                startActivity(intent);
                break;
            case R.id.img_red_packet:
                SpUtils.putStringSp(getActivity(), "data", RecommendFragment.getInstance().sdf.format(new Date()));
                finishPop(getActivity(), mPopupWindow);
                rlKnowledgeAll.setVisibility(View.VISIBLE);
                llRes.setVisibility(View.GONE);
                dragImg.setVisibility(View.GONE);
                tvTest.setVisibility(View.GONE);
//                showCardWindow();
                intent = new Intent(getContext(), RedCardActivity.class);
                startActivity(intent);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.view_packet_close:
                finishPop(getActivity(), mPopupWindow);
                llRes.setVisibility(View.VISIBLE);
                dragImg.setVisibility(View.VISIBLE);
                tvTest.setVisibility(View.VISIBLE);
                // 开始播放动画
                dragImg.startAnimation(rotateAnim);
                break;
            case R.id.btn_closes:
                SpUtils.putStringSp(getActivity(), "signtime", RecommendFragment.getInstance().sdf.format(new Date()));
                finishPop(getActivity(), reportPopupwindow);
                break;
            case R.id.start_img:
                SpUtils.putStringSp(getActivity(), "signtime", RecommendFragment.getInstance().sdf.format(new Date()));
                finishPop(getActivity(), reportPopupwindow);
                for (int i = 0; i < mpopList.size(); i++) {
                    Util.bannerJump(mpopList.get(i), getContext(), i);
                }
//                startActivity(new Intent(getContext(), TodayAchievementActivity.class));
                break;
            case R.id.rl_fm_more:
                //音频列表
//                startActivity(new Intent(getContext(), ScienceFMActivity.class));
                startActivity(new Intent(getContext(), AudioActivity.class));
                break;
            case R.id.rl_interview_all:
                startActivity(new Intent(getActivity(), MozInterviewActivity.class));
                break;
            case R.id.img_interview_bg:
                //专访
                intent = new Intent(getContext(), MozInterviewDetailsActivity.class);
                intent.putExtra("id", interviewList.get(0).getId());
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.img_forum_bg:
                //论坛
                intent = new Intent(getContext(), MozForumDetailsActivity.class);
                intent.putExtra("id", forumList.get(0).getId());
                intent.putExtra("imgurl", forumList.get(0).getForumImgUrl());
                intent.putExtra("videourl", forumList.get(0).getForumvideoUrl());
                intent.putExtra("name", forumList.get(0).getForumName());
                intent.putExtra("length", forumList.get(0).getVideoLength());
                intent.putExtra("intro", forumList.get(0).getForumIntro());
                intent.putExtra("likenum", forumList.get(0).getLikeNum());
                intent.putExtra("browsenum", forumList.get(0).getCommentnum());
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.rl_column_all:
                //专栏全部列表
                startActivity(new Intent(getContext(), ColumnAllActivity.class));
                break;
            case R.id.rl_news_all:
                startActivity(new Intent(getContext(), NewsFlashActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        toHttpGetBanner();
        listAdapter.notifyDataSetChanged();
        recColumnAdapter.notifyDataSetChanged();
        toHttpIsSigned();
//        if (AudioPlayManager.getInstance().isPlaying()) {
//            imgTodayPlay.setImageResource(R.drawable.icon_rec_stop_today);
//        } else {
//            imgTodayPlay.setImageResource(R.drawable.icon_rem_play_today);
//        }
    }

    public static void finishPop(Activity activity, PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        } catch (Exception e) {

        }
        if (reportPopupwindow != null) {
            reportPopupwindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
        getContext().unregisterReceiver(mRefreshBroadcastReceiver);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mlastIndex = firstVisibleItem + visibleItemCount;
        mtotalIndex = totalItemCount;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private boolean isLoad = false;
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        loadmoreView.getLocationInWindow(location);
        if(mlastIndex == mtotalIndex && loadmoreView.getLocalVisibleRect(rect) &&!isLoad){
            pages++;
            isLoad = true;
            toHttpGetMozRead();
        }

    }
}
