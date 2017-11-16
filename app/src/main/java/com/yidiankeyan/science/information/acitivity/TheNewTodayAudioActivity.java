package com.yidiankeyan.science.information.acitivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.TodayHistoryListAdapter;
import com.yidiankeyan.science.information.adapter.WeekAdapter;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.fragment.TheNewTodayAudioFragment;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.ExpandableLinearLayout;
import com.yidiankeyan.science.view.ShowAllListView;
import com.yidiankeyan.science.view.calendar.MyViewPager;
import com.yidiankeyan.science.view.calendar.adapter.CalendarAdapter;
import com.yidiankeyan.science.view.calendar.data.DateInfo;
import com.yidiankeyan.science.view.calendar.utils.DataUtils;
import com.yidiankeyan.science.view.calendar.utils.TimeUtils;
import com.yidiankeyan.science.view.pickview.popwindow.DatePickerPopWin;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

import static com.yidiankeyan.science.R.id.app_bar;
import static com.yidiankeyan.science.information.acitivity.TodayAchievementActivity.getLastDayOfMonth;
import static com.yidiankeyan.science.utils.SystemConstant.ON_USER_INFORMATION;


/**
 * 新日课
 */
public class TheNewTodayAudioActivity extends BaseActivity {

    private Toolbar mToolbar;
    private MagicIndicator magicIndicator;
    private List<OneDayArticles.TagListBean> tags = new ArrayList<>();
    private CollapsingToolbarLayoutState state;
    private CoordinatorLayout coordinator;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvTitle;
    private TextView tvSign;
    private ImageView imgReturn;

    private ShowAllListView lvHistory;
    private TodayHistoryListAdapter listAdapter;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private ImageView imgExpand;
    private ExpandableLinearLayout expandTags;
    private View viewAlpha;
    private ImageView imgCollapse;
    private GridView gvTags;
    private TagGridAdapter gridAdapter;

    private int currIndex;

    private boolean isSigned;


    /************************************
     * 日历相关
     *****************************************************/

    private ExpandableLinearLayout mainFrame;
    private Map<String, Integer> dateMap = new HashMap<>();
    private GridView gridView = null;
    public CalendarAdapter adapter = null;
    private GridView currentView = null;
    public List<DateInfo> currList = null;
    public List<DateInfo> list = null;
    public int lastSelected = 0;
    public MyViewPager viewPager = null;
    public MyPagerAdapter pagerAdapter = null;
    private int currPager = 500;
    private int viewPagerHeight;
    private int viewPagerWidth;
    private Map<String, List<OneDayArticles>> dataMap = new HashMap<>();
    private SimpleDateFormat sdf;
    /**
     * 向后翻一个月
     */
    private LinearLayout llBackwards;
    /**
     * 向前翻一个月
     */
    private LinearLayout llForward;
    /**
     * 显示年月
     */
    public TextView showYearMonth = null;

    /**
     * 第一个页面的年月
     */
    private int currentYear;
    private int currentMonth;

    private int selectedYear;
    private int selectedMonth;
    private String currentDate;
    private String todayDate;
    private boolean isFirst = true;
    public List<Long> signList = new ArrayList<>();
    private GridView gvWeek;
    private List<Long> weekData = new ArrayList<>();
    private WeekAdapter weekAdapter;

    private String see;

    /************************************
     * 日历相关
     *****************************************************/

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_the_new_today_audio;
    }

    @Override
    protected void initView() {

        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSign = (TextView) findViewById(R.id.tv_sign);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        lvHistory = (ShowAllListView) findViewById(R.id.lv_history);
        imgExpand = (ImageView) findViewById(R.id.img_expand);
        expandTags = (ExpandableLinearLayout) findViewById(R.id.expand_tags);
        viewAlpha = findViewById(R.id.view_alpha);
        imgCollapse = (ImageView) findViewById(R.id.img_collapse);
        gvTags = (GridView) findViewById(R.id.gv_tags);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);

        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        llBackwards = (LinearLayout) findViewById(R.id.ll_backwards);
        llForward = (LinearLayout) findViewById(R.id.ll_forward);
        showYearMonth = (TextView) findViewById(R.id.main_year_month);
        mainFrame = (ExpandableLinearLayout) findViewById(R.id.main_frame);
        gvWeek = (GridView) findViewById(R.id.gv_week);
    }

    @Override
    protected void initAction() {
        initStatusBar();
        lvHistory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
//                            mCollapsingToolbarLayout.dispatchTouchEvent(event);
//                        return true;
                    break;
                    default:
                        break;
                }
                return true;
            }
        });
        mainFrame.post(new Runnable() {
            @Override
            public void run() {
                viewPagerHeight = viewPager.getHeight();
                viewPagerWidth = viewPager.getWidth();
                ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
                params.height = viewPagerHeight;
                mainFrame.setLayoutParams(params);
                //如果日历只有5行则显示5行
                if (TextUtils.isEmpty(currList.get(35).getNongliDate())) {
                    ViewGroup.LayoutParams params2 = mainFrame.getLayoutParams();
                    params2.height = viewPagerHeight - viewPagerHeight / 6;
                    mainFrame.setLayoutParams(params2);
                }
                if (getIntent().getBooleanExtra("expand", false)) {
                    llForward.setVisibility(View.VISIBLE);
                    llBackwards.setVisibility(View.VISIBLE);
                    gvWeek.setVisibility(View.GONE);
                    tvSign.setTag("1");
                } else {
                    mainFrame.setVisibility(View.GONE);
                    tvSign.setTag("0");
                }
            }
        });

        toHttpIsSigned();

        tvSign.setCompoundDrawablePadding(Util.dip2px(this, 5));

        initToolBar();

        initMagicIndicator();

        listAdapter = new TodayHistoryListAdapter(this);
        lvHistory.setAdapter(listAdapter);
        initCalendar();

        setClickListener();

        getWeek();
        viewAlpha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewAlpha.getVisibility() == View.VISIBLE) {
                    viewAlpha.setVisibility(View.GONE);
                    expandTags.collapsed();
                    return true;
                }
                return false;
            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
                    params.height = params.height + DisplayUtil.getStatueBarHeight(TheNewTodayAudioActivity.this);
                    mToolbar.setLayoutParams(params);
                }
            });
        }
    }

    private void toHttpIsSigned() {
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            ApiServerManager.getInstance().getApiServer().isSigned().enqueue(new RetrofitCallBack<Boolean>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Boolean>> call, Response<RetrofitResult<Boolean>> response) {
                    if (response.body().getCode() == 200) {
                        if (response.body().getData()) {
                            if (TextUtils.equals("1", (String) tvSign.getTag())) {
                                tvSign.setText("收起签到");
                            } else {
                                tvSign.setText("查看签到");
                            }
                        } else {
                            tvSign.setText("签到");
                        }
                        isSigned = response.body().getData();
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

    private void initData(final String date) {
//        AudioPlayManager.getInstance(getApplicationContext()).setDataList(mDatas);
//        AudioPlayManager.getInstance(getApplicationContext()).setPlayPosition(0);
        Map<String, Object> map = new HashMap<>();
        map.put("datetime", date);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ONE_DAY_ARTICLES, map, new HttpUtil.HttpCallBack() {
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
//                    if (TextUtils.equals(currentDate, sdf.format(new Date()))) {
//                        listAudio = new ArrayList<OneDayArticles>();
//                        listAudio.remove(listAudio);
//                        listAudio.addAll(list);
//                        RecommendFragment.getInstance().playList = listAudio;
//                    }


                    dataMap.put(date, list);
                    if (date.equals(currentDate))
                        listAdapter.setDatas(dataMap.get(date));
                    listAdapter.notifyDataSetChanged();
                }
//                if (dataMap.get(date) == null || dataMap.get(date).size() == 0) {
//                    tvDataState.setVisibility(View.VISIBLE);
//                    tvDataState.setText("敬请期待");
//                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
//                tvDataState.setVisibility(View.VISIBLE);
//                tvDataState.setText("敬请期待");
            }
        });
    }

    private void getWeek() {

        Calendar cal = Calendar.getInstance();
        long current = System.currentTimeMillis();
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        cal.setTime(new Date(zero));
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -5;
        } else {
            d = 1 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        //所在周开始日期
        weekData.add(cal.getTime().getTime());
        weekData.add(cal.getTime().getTime() + 24 * 3600 * 1000);
        weekData.add(cal.getTime().getTime() + 2 * 24 * 3600 * 1000);
        weekData.add(cal.getTime().getTime() + 3 * 24 * 3600 * 1000);
        weekData.add(cal.getTime().getTime() + 4 * 24 * 3600 * 1000);
        weekData.add(cal.getTime().getTime() + 5 * 24 * 3600 * 1000);
        weekData.add(cal.getTime().getTime() + 6 * 24 * 3600 * 1000);

        weekAdapter = new WeekAdapter(weekData, this);
        gvWeek.setAdapter(weekAdapter);

        gvWeek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weekAdapter.setSelectPosition(position);
                weekAdapter.notifyDataSetChanged();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                currentDate = sdf.format(new Date(weekData.get(position)));
                if (dataMap.get(sdf.format(new Date(weekData.get(position)))) == null) {
                    initData(sdf.format(new Date(weekData.get(position))));
                } else if (dataMap.get(sdf.format(new Date(weekData.get(position)))).size() > 0) {
//                    tvDataState.setVisibility(View.GONE);
                    listAdapter.setDatas(dataMap.get(sdf.format(new Date(weekData.get(position)))));
                    listAdapter.notifyDataSetChanged();
                } else if (dataMap.get(sdf.format(new Date(weekData.get(position)))).size() == 0) {
//                    tvDataState.setText("敬请期待");
//                    tvDataState.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setClickListener() {
        tvSign.setOnClickListener(this);
        imgReturn.setOnClickListener(this);
        llBackwards.setOnClickListener(this);
        llForward.setOnClickListener(this);
        showYearMonth.setOnClickListener(this);
        imgExpand.setOnClickListener(this);
        imgCollapse.setOnClickListener(this);
        findViewById(R.id.ll_pop_tags).setOnClickListener(this);
    }

    private void initCalendar() {
        currentYear = TimeUtils.getCurrentYear();
        currentMonth = TimeUtils.getCurrentMonth();
        lastSelected = TimeUtils.getCurrentDay();
        selectedYear = currentYear;
        selectedMonth = currentMonth;
        currentDate = String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected);
        todayDate = String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected);
        initData(currentDate);
        String formatDate = TimeUtils.getFormatDate(currentYear, currentMonth);
        try {
            list = TimeUtils.initCalendar(formatDate, currentMonth);
            currList = list;
        } catch (Exception e) {
            finish();
        }
        pagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(500);
        viewPager.setPageMargin(0);
        showYearMonth.getPaint().setFakeBoldText(true);
        showYearMonth.setText(String.format("%04d年%2d月", currentYear, currentMonth));
        toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), getLastDayOfMonth(currentYear, currentMonth - 1));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressWarnings("ResourceType")
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 1) {
                }
                if (arg0 == 0) {
                    currentView = (GridView) viewPager.findViewById(currPager);
                    if (currentView != null) {
                        adapter = (CalendarAdapter) currentView.getAdapter();
                        currList = adapter.getList();
                        ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
                        if (TextUtils.isEmpty(currList.get(35).getNongliDate())) {
                            params.height = viewPagerHeight - viewPagerHeight / 6;
                            mainFrame.setLayoutParams(params);
                        } else {
                            params.height = viewPagerHeight;
                            mainFrame.setLayoutParams(params);
                        }
                        final int pos = DataUtils.getDayFlag(currList, lastSelected);
                        adapter.setSelectedPosition(pos);
                        adapter.notifyDataSetInvalidated();
                    }
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int position) {
                int year = TimeUtils.getTimeByPosition(position, currentYear, currentMonth, "year");
                int month = TimeUtils.getTimeByPosition(position, currentYear, currentMonth, "month");
                showYearMonth.setText(String.format("%04d年%2d月", year, month));
                toHttpGetSignList(String.format("%04d-%02d-%02d", year, month, 0), getLastDayOfMonth(year, month - 1));
                currPager = position;
                if (TimeUtils.getCurrentYear() == year && TimeUtils.getCurrentMonth() == month) {
                    adapter.setCurrentMonth(true);
                } else {
                    adapter.setCurrentMonth(false);
                }

                if (selectedMonth == month && selectedYear == year)
                    //最后选择上的年月与当前显示的年月一致时，将选择的日期高亮显示出来
                    adapter.setShowSelectedColor(true);
                else
                    adapter.setShowSelectedColor(false);
//                }
                adapter.setMonth(month);
                adapter.setYear(year);
                adapter.notifyDataSetInvalidated();
            }
        });
    }

    private void toHttpGetSignList(String beginTime, String endTime) {
        if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("begintime", beginTime);
        map.put("endtime", endTime);
        ApiServerManager.getInstance().getApiServer().getDailySignList(map).enqueue(new RetrofitCallBack<List<Long>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<Long>>> call, Response<RetrofitResult<List<Long>>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().size() > 0) {
                        for (Long data : response.body().getData()) {
                            if (!signList.contains(data)) {
                                signList.add(data);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        weekAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<Long>>> call, Throwable t) {
//                isSign = false;
//                tvRecordStudy.setText("打卡学习");
//                tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
//                tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(" ");
//        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.TOP);////设置展开后标题的位置
        mCollapsingToolbarLayout.setExpandedTitleMarginTop(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setExpandedTitleMarginStart(Util.dip2px(this, 48));
        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER | Gravity.TOP);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
        AppBarLayout appBar = (AppBarLayout) findViewById(app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                Log.e("verticalOffset", verticalOffset + "");
//                Log.e("getTotalScrollRange", appBarLayout.getTotalScrollRange() + "");
//                Log.e("dip", Util.dip2px(TheNewTodayAudioActivity.this, 206) + "");
                if (Math.abs(verticalOffset) >= Util.dip2px(TheNewTodayAudioActivity.this, 206)) {
                    tvTitle.setBackgroundColor(Color.BLACK);
                    tvTitle.setText("墨子日课");
                    viewStatusBar.setBackgroundColor(Color.BLACK);
                } else {
                    tvTitle.setBackgroundColor(Color.TRANSPARENT);
                    tvTitle.setText("");
                    viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
                }
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    imgExpand.setVisibility(View.VISIBLE);
                } else {
                    imgExpand.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initMagicIndicator() {
        ApiServerManager.getInstance().getApiServer().getTodayTags().enqueue(new RetrofitCallBack<List<OneDayArticles.TagListBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<OneDayArticles.TagListBean>>> call, Response<RetrofitResult<List<OneDayArticles.TagListBean>>> response) {
                if (response.body().getCode() == 200) {
                    tags.add(new OneDayArticles.TagListBean(0, "全部"));
                    tags.addAll(response.body().getData());
                    for (int i = 0; i < tags.size(); i++) {
                        TheNewTodayAudioFragment fragment = new TheNewTodayAudioFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", tags.get(i).getId());
                        fragment.setArguments(bundle);
                        mFragments.add(fragment);
                    }
                    gvTags.setAdapter(new TagGridAdapter());
                    magicIndicator.setBackgroundColor(Color.WHITE);
                    CommonNavigator commonNavigator = new CommonNavigator(TheNewTodayAudioActivity.this);
                    commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                        @Override
                        public int getCount() {
                            return tags.size();
                        }

                        @Override
                        public IPagerTitleView getTitleView(Context context, final int index) {
                            Log.e("getTitleView", "getTitleView");
                            SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                            simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                            simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                            simplePagerTitleView.setText(tags.get(index).getName());
                            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currIndex = index;
                                    mFragmentContainerHelper.handlePageSelected(index);
                                    switchPages(index);
                                }
                            });
                            return simplePagerTitleView;
                        }

                        @Override
                        public IPagerIndicator getIndicator(Context context) {
                            LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                            linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                            linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2));
                            linePagerIndicator.setColors(Color.parseColor("#f1312e"));
                            return linePagerIndicator;
                        }
                    });
                    magicIndicator.setNavigator(commonNavigator);
                    mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
                    mFragmentContainerHelper.handlePageSelected(0, false);
                    switchPages(0);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<OneDayArticles.TagListBean>>> call, Throwable t) {

            }
        });
    }

    private void switchPages(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
//            viewPagerHeight = viewPager.getHeight();
//            viewPagerWidth = viewPager.getWidth();
//            ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
//            params.height = viewPagerHeight;
//            mainFrame.setLayoutParams(params);
//            //如果日历只有5行则显示5行
//            if (TextUtils.isEmpty(currList.get(35).getNongliDate())) {
//                ViewGroup.LayoutParams params2 = mainFrame.getLayoutParams();
//                params2.height = viewPagerHeight - viewPagerHeight / 6;
//                mainFrame.setLayoutParams(params2);
//            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long j = System.currentTimeMillis();
                    //将年月在vp代表的页码放到map
                    for (int i = 0; i < 1000; i++) {
                        final int year = TimeUtils.getTimeByPosition(i, currentYear, currentMonth, "year");
                        final int month = TimeUtils.getTimeByPosition(i, currentYear, currentMonth, "month");
                        String formatDate2 = TimeUtils.getFormatDate(year, month);
                        dateMap.put(formatDate2.substring(0, 7), i);
                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            listAdapter.notifyDataSetChanged();
//                        }
//                    });
                }
            }).start();
        }
        isFirst = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_backwards:
                currPager++;
                viewPager.setCurrentItem(currPager);
                break;
            case R.id.ll_forward:
                currPager--;
                viewPager.setCurrentItem(currPager);
                break;
            case R.id.main_year_month:
                if (llForward.getVisibility() == View.VISIBLE) {
                    String date = showYearMonth.getText().toString();
                    String year = date.substring(0, date.indexOf("年"));
                    String month = date.substring(date.indexOf("年") + 1, date.indexOf("月"));
                    DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(this, new DatePickerPopWin.OnDatePickedListener() {
                        @Override
                        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                            int position = dateMap.get(dateDesc.substring(0, 7));
                            currPager = position;
                            viewPager.setCurrentItem(currPager);
                        }
                    }).textConfirm("确认")
                            .textCancel("取消")
                            .btnTextSize(16)
                            .viewTextSize(25)
                            .colorCancel(Color.parseColor("#999999"))
                            .colorConfirm(Color.parseColor("#009900"))
                            .minYear(TimeUtils.getTimeByPosition(100, currentYear, currentMonth, "year"))
                            .maxYear(TimeUtils.getTimeByPosition(900, currentYear, currentMonth, "year"))
                            .dateChose(year + "-" + month + "-01")
                            .build();
                    pickerPopWin.showPopWin(this);
                }
                break;
            case R.id.tv_sign:
                if (!Util.hintLogin(this)) {
                    return;
                }
                if (isSigned) {
                    if (TextUtils.equals("1", (String) v.getTag())) {
                        //关闭
                        v.setTag("0");
                        tvSign.setText("查看签到");
                        mainFrame.collapsed();
                        gvWeek.setVisibility(View.VISIBLE);
                        llForward.setVisibility(View.GONE);
                        llBackwards.setVisibility(View.GONE);
                    } else {
                        //打开
                        v.setTag("1");
                        tvSign.setText("收起签到");
                        if (mainFrame.getVisibility() == View.GONE) {
                            mainFrame.setVisibility(View.VISIBLE);
                        } else {
                            mainFrame.expand();
                        }
                        llForward.setVisibility(View.VISIBLE);
                        llBackwards.setVisibility(View.VISIBLE);
                        gvWeek.setVisibility(View.GONE);
                    }
                } else {
                    toHttpSign();
                }
                break;
            case R.id.img_expand:
                viewAlpha.setVisibility(View.VISIBLE);
                expandTags.expand();
                break;
            case R.id.img_collapse:
                viewAlpha.setVisibility(View.GONE);
                expandTags.collapsed();
                break;
            case R.id.img_return:
                finish();
                break;
        }
    }

    /**
     * 签到
     */
    private void toHttpSign() {
        ApiServerManager.getInstance().getApiServer().todaySign().enqueue(new RetrofitCallBack<Integer>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Integer>> call, Response<RetrofitResult<Integer>> response) {
                if (response.body().getCode() == 200) {
                    toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), getLastDayOfMonth(currentYear, currentMonth - 1));
                    tvSign.setText("查看签到");
                    isSigned = true;
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Integer>> call, Throwable t) {

            }
        });
    }

    public enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    /**
     * viewpager的适配器，从第500页开始，最多支持0-1000页
     */
    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            currentView = (GridView) object;
            adapter = (CalendarAdapter) currentView.getAdapter();
        }

        @Override
        public int getCount() {
            return 1000;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gv = initCalendarView(position);
            gv.setId(position);
            container.addView(gv);
            return gv;
        }

    }

    /**
     * 初始化日历的gridview
     */
    private GridView initCalendarView(int position) {
        final int year = TimeUtils.getTimeByPosition(position, currentYear, currentMonth, "year");
        final int month = TimeUtils.getTimeByPosition(position, currentYear, currentMonth, "month");
        String formatDate = TimeUtils.getFormatDate(year, month);
        try {
            list = TimeUtils.initCalendar(formatDate, month);
        } catch (Exception e) {
            finish();
        }
        gridView = (GridView) LayoutInflater.from(this).inflate(R.layout.calendar_gridview, null, false);
//        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        adapter = new CalendarAdapter(this, list);
        if (position == 500) {
            currList = list;
            int pos = DataUtils.getDayFlag(adapter.getList(), TimeUtils.getCurrentDay());
            adapter.setTodayPosition(pos);
            adapter.setSelectedPosition(pos);
            adapter.setMonth(TimeUtils.getCurrentMonth());
            adapter.setYear(TimeUtils.getCurrentYear());
            adapter.setFirst(true);
        }
        gridView.setAdapter(adapter);
//        gridView.setNumColumns(7);
//        gridView.setHorizontalSpacing(1);
//        gridView.setVerticalSpacing(1);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        gridView.setGravity(Gravity.CENTER);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setFirst(false);
                if (currList.get(position).isThisMonth() == false) {
                    return;
                } else if (adapter.getMonth() == TimeUtils.getCurrentMonth() && adapter.getYear() == TimeUtils.getCurrentYear() && adapter.getTodayPosition() < position) {
                    return;
                } else if (adapter.getYear() == TimeUtils.getCurrentYear() && adapter.getMonth() > TimeUtils.getCurrentMonth()) {
                    return;
                } else if (adapter.getYear() > TimeUtils.getCurrentYear()) {
                    return;
                }
                selectedYear = year;
                selectedMonth = month;
                adapter.setShowSelectedColor(true);
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetInvalidated();
                lastSelected = currList.get(position).getDate();
//                for (int i = 0; i < mDatas.size(); i++) {
//                    if (String.format("%04d-%02d-%02d", year, month, lastSelected).equals(mDatas.get(i).getDate())) {
//                        lvHistory.setSelection(i);
//                        break;
//                    }
//                }
//                listAdapter.setDatas(dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)));
                currentDate = String.format("%04d-%02d-%02d", year, month, lastSelected);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(currentDate).getTime() - sdf.parse(todayDate).getTime() > 0) {
//                        tvDataState.setText("敬请期待");
//                        tvDataState.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)) == null) {
                    initData(String.format("%04d-%02d-%02d", year, month, lastSelected));
                } else if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)).size() > 0) {
//                    tvDataState.setVisibility(View.GONE);
                    listAdapter.setDatas(dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)));
                    listAdapter.notifyDataSetChanged();
                } else if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)).size() == 0) {
//                    tvDataState.setText("敬请期待");
//                    tvDataState.setVisibility(View.VISIBLE);
                }
            }
        });
        return gridView;
    }


    class TagGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tags.size();
        }

        @Override
        public Object getItem(int position) {
            return tags.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(TheNewTodayAudioActivity.this).inflate(R.layout.item_today_tag, parent, false);
            }
            ((TextView) convertView).setText(tags.get(position).getName());
            if (position == currIndex) {
                ((TextView) convertView).setTextColor(Color.WHITE);
                ((TextView) convertView).setBackgroundResource(R.drawable.shape_radius_30_111111);
            } else {
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));
                ((TextView) convertView).setBackgroundResource(R.drawable.shape_radius_30_e1e1e1);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currIndex = position;
                    mFragmentContainerHelper.handlePageSelected(position);
                    switchPages(position);
                    viewAlpha.setVisibility(View.GONE);
                    expandTags.collapsed();
                }
            });
            return convertView;
        }

    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()){
            case ON_USER_INFORMATION:
                toHttpIsSigned();
                toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), getLastDayOfMonth(currentYear, currentMonth - 1));
                break;
            case SystemConstant.ON_STOP:
                listAdapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_PLAYING:
                listAdapter.notifyDataSetChanged();
                break;
            case SystemConstant.ON_PAUSE:
                listAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
