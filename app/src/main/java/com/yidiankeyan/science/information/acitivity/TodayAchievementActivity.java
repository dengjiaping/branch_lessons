package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.TodayHistoryListAdapter;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.ShowAllListView;
import com.yidiankeyan.science.view.calendar.MyViewPager;
import com.yidiankeyan.science.view.calendar.Panel;
import com.yidiankeyan.science.view.calendar.adapter.CalendarAdapter;
import com.yidiankeyan.science.view.calendar.data.DateInfo;
import com.yidiankeyan.science.view.calendar.utils.DataUtils;
import com.yidiankeyan.science.view.calendar.utils.TimeUtils;
import com.yidiankeyan.science.view.pickview.popwindow.DatePickerPopWin;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 日课
 */
public class TodayAchievementActivity extends BaseActivity {

    private boolean isFirst = true;

    private AutoRelativeLayout llReturn;
    private TextView txtTitle;

    /**
     * 和viewpager相关变量
     */
    public MyViewPager viewPager = null;
    public MyPagerAdapter pagerAdapter = null;
    private int currPager = 500;
    private TextView shader;
    private int viewPagerHeight;
    private int viewPagerWidth;
    private TextView tvCurrentDate;
    /**
     * 和日历gridview相关变量
     */
    private GridView gridView = null;
    public CalendarAdapter adapter = null;
    private GridView currentView = null;
    public List<DateInfo> currList = null;
    public List<DateInfo> list = null;
    public int lastSelected = 0;
    /**
     * 向后翻一个月
     */
    private LinearLayout llBackwards;
    /**
     * 向前翻一个月
     */
    private LinearLayout llForward;
    private ShowAllListView lvHistory;
    private TodayHistoryListAdapter listAdapter;

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

    /**
     * 收缩展开的面板
     */
    private Panel panel;
    private RelativeLayout panelContent;
    private RelativeLayout mainFrame;
    private Map<String, Integer> dateMap = new HashMap<>();
    //        private Timer mTimer;
//    private TimerTask mTimerTask;
    private long currentTime;
    private AutoRelativeLayout llPlay;
    private ImageView imgPlay;
    private AnimationDrawable animationDrawable;
    private AutoLinearLayout llAll;
    private String todayDate;

    private ImageView imgAvatar;
    private TextView tvUserName, tvNumbers;
    private TextView tvHideCalendar, tvRecordStudy;
    private boolean isHide;

    public List<Long> signList = new ArrayList<>();

    /**
     * 键是日期，值是这一天的数据
     */
    private Map<String, List<OneDayArticles>> dataMap = new HashMap<>();
    private String currentDate;
    private static final int INTO_CONTROL = 101;

    /**
     * 将显示敬请期待以及正在加载
     */
    private TextView tvDataState;

//    private String[] audios = {"http://mp3.ldstars.com/down/4946.mp3", "http://www.itinge.com/music/3/9158.mp3", "http://mp3.haoduoge.com/s/2016-09-02/1472779589.mp3"};

    private int playPosition;
    private AutoRelativeLayout layoutTitle;
    private ImageView titleReturn;
    private SimpleDateFormat sdf;
    private ArrayList<OneDayArticles> listAudio;

    private PopupWindow reportPopupwindow;
    private Button mClose_btn;
    private ImageView imgStart;
    private TextView tvNumberMoz;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private IntentFilter intentFilter;
    private boolean isSign;
    private int number;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        currentTime = System.currentTimeMillis();
        return R.layout.activity_today_achievement;
    }

    @Override
    protected void initView() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("action.read.audio");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvUserName = (TextView) findViewById(R.id.tv_today_name);
        tvNumbers = (TextView) findViewById(R.id.tv_today_moz);
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        shader = (TextView) findViewById(R.id.main_frame_shader);
        panelContent = (RelativeLayout) findViewById(R.id.panelContent);
        tvDataState = (TextView) findViewById(R.id.tv_data_state);
        tvRecordStudy = (TextView) findViewById(R.id.tv_record_study);
        mainFrame = (RelativeLayout) findViewById(R.id.main_frame);
        panel = (Panel) findViewById(R.id.panel);
        llBackwards = (LinearLayout) findViewById(R.id.ll_backwards);
        llForward = (LinearLayout) findViewById(R.id.ll_forward);
        llReturn = (AutoRelativeLayout) findViewById(R.id.ll_return);
        lvHistory = (ShowAllListView) findViewById(R.id.lv_history);
        llPlay = (AutoRelativeLayout) findViewById(R.id.ll_play);
        tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
        imgPlay = (ImageView) findViewById(R.id.img_play);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        tvHideCalendar = (TextView) findViewById(R.id.tv_hide_calendar);
        animationDrawable = (AnimationDrawable) imgPlay.getDrawable();
    }

    @Override
    protected void initAction() {
        txtTitle.setText("日课");
        txtTitle.setTextSize(18);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String userAvatar = SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl");
        if (userAvatar != null && userAvatar.startsWith("http")) {
            Glide.with(this).load(userAvatar)
                    .error(R.drawable.icon_default_avatar)
                    .placeholder(R.drawable.icon_default_avatar)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(this)).into(imgAvatar);
        } else {
            Glide.with(this).load(Util.getImgUrl(userAvatar))
                    .error(R.drawable.icon_default_avatar)
                    .placeholder(R.drawable.icon_default_avatar)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(this)).into(imgAvatar);
        }
        tvUserName.setText(SpUtils.getStringSp(DemoApplication.applicationContext, "userName"));
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum")) && !TextUtils.equals("null", SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"))) {
            tvNumbers.setText("墨子号：" + SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"));
        } else {
            tvNumbers.setText("");
        }
//        if (TextUtils.isEmpty(SpUtils.getStringSp(TodayAchievementActivity.this, "todaytime")) && !TextUtils.equals(SpUtils.getStringSp(TodayAchievementActivity.this, "todaytime"), sdf.format(new Date()))) {
//            tvRecordStudy.setText("打卡学习");
//            tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
//            tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
//        } else {
//            tvRecordStudy.setText("已打卡");
//            tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
//            tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
//        }
        tvHideCalendar.setBackgroundResource(R.drawable.today_item_frames);
        tvHideCalendar.setTextColor(Color.parseColor("#f1312e"));
        tvHideCalendar.setText("显示日历");
        TextPaint tp = txtTitle.getPaint();
        tp.setFakeBoldText(true);
        llPlay.setOnClickListener(this);
        currentYear = TimeUtils.getCurrentYear();
        currentMonth = TimeUtils.getCurrentMonth();
        lastSelected = TimeUtils.getCurrentDay();
        listAdapter = new TodayHistoryListAdapter(this);
        listAdapter.setLlAll((LinearLayout) findViewById(R.id.ll_all));
        lvHistory.setAdapter(listAdapter);
        llBackwards.setOnClickListener(this);
        llForward.setOnClickListener(this);
        llReturn.setOnClickListener(this);
        tvHideCalendar.setOnClickListener(this);
        tvRecordStudy.setOnClickListener(this);
        selectedYear = currentYear;
        selectedMonth = currentMonth;
        currentDate = String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected);
        todayDate = String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected);
        initData(currentDate);
        listAdapter.setListView(lvHistory);
//        mTimer = new Timer();
//        //每隔0.1秒更新seekbar与剩余时间
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                listAdapter.updataProgress();
//            }
//        };
//        mTimer.scheduleAtFixedRate(mTimerTask, 0, 100);
        String formatDate = TimeUtils.getFormatDate(currentYear, currentMonth);
        try {
            list = TimeUtils.initCalendar(formatDate, currentMonth);
        } catch (Exception e) {
            finish();
        }
        panel.setOpen(true, false);
        shader.setVisibility(View.GONE);
        pagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(500);
        viewPager.setPageMargin(0);
        showYearMonth = (TextView) findViewById(R.id.main_year_month);
        showYearMonth.getPaint().setFakeBoldText(true);
        showYearMonth.setText(String.format("%04d年%2d月", currentYear, currentMonth));
        showYearMonth.setOnClickListener(this);
        tvCurrentDate.setText(String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected));
        toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), getLastDayOfMonth(currentYear, currentMonth - 1));
//        tvSelectionDate.setText(String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressWarnings("ResourceType")
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 1) {
                    shader.setText("");
                    shader.setVisibility(View.VISIBLE);
                }
                if (arg0 == 0) {
                    currentView = (GridView) viewPager.findViewById(currPager);
                    if (currentView != null) {
                        adapter = (CalendarAdapter) currentView.getAdapter();
                        currList = adapter.getList();
                        if (TextUtils.isEmpty(currList.get(35).getNongliDate())) {
                            ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
                            params.height = viewPagerHeight - viewPagerHeight / 6;
                            mainFrame.setLayoutParams(params);
                        } else {
                            ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
                            params.height = viewPagerHeight;
                            mainFrame.setLayoutParams(params);
                        }
                        final int pos = DataUtils.getDayFlag(currList, lastSelected);
                        adapter.setSelectedPosition(pos);
                        adapter.notifyDataSetInvalidated();
                    }
                    shader.setVisibility(View.GONE);
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
                shader.setText(showYearMonth.getText());
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
//        for (int i = 0; i < 1000; i++) {
//            final int year = TimeUtils.getTimeByPosition(i, currentYear, currentMonth, "year");
//            final int month = TimeUtils.getTimeByPosition(i, currentYear, currentMonth, "month");
//            String formatDate2 = TimeUtils.getFormatDate(year, month);
//            dateMap.put(formatDate2.substring(0, 7), i);
//        }
    }

    //需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是4月份的最后一天，千万不要搞错了哦
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
        if (AudioPlayManager.getInstance().isPlaying()) {
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else {
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
        }
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.read.audio")) {
                if (SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl") != null && SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl").startsWith("http")) {
                    Glide.with(TodayAchievementActivity.this).load(SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl"))
                            .error(R.drawable.icon_default_avatar)
                            .placeholder(R.drawable.icon_default_avatar)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(TodayAchievementActivity.this)).into(imgAvatar);
                } else {
                    Glide.with(TodayAchievementActivity.this).load(Util.getImgUrl(SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl")))
                            .error(R.drawable.icon_default_avatar)
                            .placeholder(R.drawable.icon_default_avatar)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(TodayAchievementActivity.this)).into(imgAvatar);
                }
                tvUserName.setText(SpUtils.getStringSp(DemoApplication.applicationContext, "userName"));
                if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum")) && !TextUtils.equals("null", SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"))) {
                    tvNumbers.setText("墨子号：" + SpUtils.getStringSp(DemoApplication.applicationContext, "scienceNum"));
                } else {
                    tvNumbers.setText("");
                }
//                if (isSign) {
//                    tvRecordStudy.setText("打卡学习");
//                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
//                    tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
//                } else {
//                    tvRecordStudy.setText("已打卡");
//                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
//                    tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
//                }
                toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), getLastDayOfMonth(currentYear, currentMonth - 1));
                tvHideCalendar.setBackgroundResource(R.drawable.today_item_frames);
                tvHideCalendar.setTextColor(Color.parseColor("#f1312e"));
                tvHideCalendar.setText("显示日历");
                isHide = true;
            }
        }
    };

    private void initData(final String date) {
//        AudioPlayManager.getInstance(getApplicationContext()).setDataList(mDatas);
//        AudioPlayManager.getInstance(getApplicationContext()).setPlayPosition(0);
        Map<String, Object> map = new HashMap<>();
        map.put("datetime", date);
        tvDataState.setVisibility(View.VISIBLE);
        tvDataState.setText("正在加载");
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
                    if (TextUtils.equals(currentDate, sdf.format(new Date()))) {
                        listAudio = new ArrayList<OneDayArticles>();
                        listAudio.remove(listAudio);
                        listAudio.addAll(list);
                    }


                    dataMap.put(date, list);
                    if (date.equals(currentDate))
                        listAdapter.setDatas(dataMap.get(date));
                    listAdapter.notifyDataSetChanged();
                    tvDataState.setVisibility(View.GONE);
                }
                if (dataMap.get(date) == null || dataMap.get(date).size() == 0) {
                    tvDataState.setVisibility(View.VISIBLE);
                    tvDataState.setText("敬请期待");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                tvDataState.setVisibility(View.VISIBLE);
                tvDataState.setText("敬请期待");
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
        isFirst = false;
        panel.setVisibility(View.GONE);
        tvHideCalendar.setBackgroundResource(R.drawable.today_item_frames);
        tvHideCalendar.setTextColor(Color.parseColor("#f1312e"));
        tvHideCalendar.setText("显示日历");
        isHide = true;
    }

    private void toHttpGetSignList(String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("begintime", beginTime);
        map.put("endtime", endTime);
        ApiServerManager.getInstance().getApiServer().getDailySignList(map).enqueue(new RetrofitCallBack<List<Long>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<Long>>> call, Response<RetrofitResult<List<Long>>> response) {
                isSign = false;
                tvRecordStudy.setText("打卡学习");
                tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
                tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
                if (response.body().getCode() == 200) {
                    if (response.body().getData().size() > 0) {
                        for (Long data : response.body().getData()) {
                            if (!signList.contains(data)) {
                                signList.add(data);
                            }
                            if (TextUtils.equals(todayDate, sdf.format(data))) {
                                isSign = true;
                                tvRecordStudy.setText("已打卡");
                                tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
                                tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<Long>>> call, Throwable t) {
                isSign = false;
                tvRecordStudy.setText("打卡学习");
                tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
                tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
            }
        });
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
            case R.id.ll_return:
                finish();
                break;
            case R.id.main_year_month:
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
                break;
            case R.id.ll_play:
//                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_STOP
//                        || AudioPlayManager.getInstance().CURRENT_STATE == 0) {
//                    RecommendFragment.getInstance().playList = (ArrayList<OneDayArticles>) dataMap.get(currentDate);
//                }
//                if (RecommendFragment.getInstance().playList == null || RecommendFragment.getInstance().playList.size() == 0) {
//                    Toast.makeText(TodayAchievementActivity.this, "这天没有可播放的内容", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent(this, AudioControlActivity.class);
//                intent.putParcelableArrayListExtra("playList", RecommendFragment.getInstance().playList);
//                for (int i = 0; i < RecommendFragment.getInstance().playList.size(); i++) {
//                    if (TextUtils.equals(RecommendFragment.getInstance().playList.get(i).getId(), AudioPlayManager.getInstance().getmMediaPlayId())) {
//                        playPosition = i;
//                        break;
//                    }
//                }
//                intent.putExtra("playPosition", playPosition);
//                startActivityForResult(intent, INTO_CONTROL);
                break;
            case R.id.tv_hide_calendar:
                //隐藏日历
                if (isHide) {
//                    panel.setOpen(true, false);
                    panel.setVisibility(View.VISIBLE);
                    isHide = false;
                    tvHideCalendar.setBackgroundResource(R.drawable.today_item_frame);
                    tvHideCalendar.setTextColor(Color.parseColor("#ffffff"));
                    tvHideCalendar.setText("隐藏日历");
                } else {
//                    panel.setOpen(false, true);
                    panel.setVisibility(View.GONE);
                    tvHideCalendar.setBackgroundResource(R.drawable.today_item_frames);
                    tvHideCalendar.setTextColor(Color.parseColor("#f1312e"));
                    tvHideCalendar.setText("显示日历");
                    isHide = true;
                }
                break;
            case R.id.tv_record_study:
//                if (!Util.hintLogin(this)) return;
//                //打卡签到
////                if (isSign || !TextUtils.isEmpty(SpUtils.getStringSp(TodayAchievementActivity.this, "todaytime")) && TextUtils.equals(SpUtils.getStringSp(TodayAchievementActivity.this, "todaytime"), sdf.format(new Date()))) {
//                if (isSign || !TextUtils.equals(currentDate, todayDate)) {
////                    tvRecordStudy.setText("已打卡");
////                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
////                    tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
//                    ToastMaker.showShortToast("昨日成风，抓住当下，好好学习");
////                    SpUtils.putStringSp(TodayAchievementActivity.this, "todaytime", sdf.format(new Date()));
//                } else {
//                    toDailySignon();
//                }
//                AudioPlayManager.getInstance().release();
//                if (RecommendFragment.getInstance().playList == null || RecommendFragment.getInstance().playList.size() == 0) {
//                    ToastMaker.showShortToast("今天没有可播放的内容");
//                    return;
//                } else {
//                    AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getId());
//                    if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
//                        //当前的音频已经下载到了本地，设置本地播放路径
//                        AudioPlayManager.getInstance().setUp(albumContent.getFilePath(), RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getId());
//                    } else {
//                        if (RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getAudiourl().startsWith("/"))
//                            AudioPlayManager.getInstance().setUp(SystemConstant.ACCESS_IMG_HOST + RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getAudiourl(), RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getId());
//                        else
//                            AudioPlayManager.getInstance().setUp(SystemConstant.ACCESS_IMG_HOST + "/" + RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getAudiourl(), RecommendFragment.getInstance().playList.get(RecommendFragment.getInstance().playPosition).getId());
//                    }
//                    AudioPlayManager.getInstance().start();
//                }
//                listAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_close:
                finishPop(this, reportPopupwindow);
                break;
            case R.id.start_img:
                finishPop(this, reportPopupwindow);
                showSharePop();
                break;
        }
    }

    private void toDailySignon() {

        ApiServerManager.getInstance().getApiServer().todaySign().enqueue(new RetrofitCallBack<Integer>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Integer>> call, Response<RetrofitResult<Integer>> response) {
                if (response.body().getCode() == 200) {
                    try {
                        signList.add(sdf.parse(todayDate).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ToastMaker.showShortToast("签到成功");
                    isSign = true;
                    tvRecordStudy.setText("已打卡");
                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
                    tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
//                    SpUtils.putStringSp(TodayAchievementActivity.this, "todaytime", sdf.format(new Date()));
                    adapter.notifyDataSetChanged();
//                    toHttpGetSignList(String.format("%04d-%02d-%02d", currentYear, currentMonth, 0), String.format("%04d-%02d-%02d", currentYear, currentMonth, lastSelected));

                    if (response.body().getData() == null) {
                        number = 0;
                    } else {
                        number = response.body().getData();
                    }
                    if (number > 0) {
                        showTodayMoz(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Integer>> call, Throwable t) {

            }
        });
    }


    /**
     * 弹出签到框
     *
     * @param data
     */
    private void showTodayMoz(Integer data) {
        if (reportPopupwindow == null) {
            View view = View.inflate(this, R.layout.dialog_today_search, null);
            mClose_btn = (Button) view.findViewById(R.id.btn_close);
            imgStart = (ImageView) view.findViewById(R.id.start_img);
            tvNumberMoz = (TextView) view.findViewById(R.id.tv_number_moz);
            tvNumberMoz.setText("恭喜您获得" + data + "枚墨子币");
            mClose_btn.setOnClickListener(this);
            imgStart.setOnClickListener(this);
            reportPopupwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            reportPopupwindow.setContentView(view);
            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            reportPopupwindow.setFocusable(true);
            reportPopupwindow.setOutsideTouchable(true);
            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(TodayAchievementActivity.this, reportPopupwindow);
                }
            });
            reportPopupwindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            reportPopupwindow.showAtLocation(llAll, Gravity.CENTER, 0, 0);
        }
    }


    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgReport = (TextView) view.findViewById(R.id.btn_cancel);
//            llSearchSina = (AutoLinearLayout) view.findViewById(R.id.ll_search_sina);
//            imgClick = (CheckBox) view.findViewById(R.id.img_click);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(TodayAchievementActivity.this, sharePopupWindow);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(TodayAchievementActivity.this, sharePopupWindow);
                }
            });
            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "data:image/jpeg;base64,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");
//                    String url = "http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                            .withText("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();

                    shareWeb(
                            SHARE_MEDIA.WEIXIN,
                            "听日课，赢金币",
                            "墨子",
                            null,
                            SystemConstant.MYURL + "/view/fore/rikeActivity/signShare.jsp"
                            , null
                    );
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWeb(
                            SHARE_MEDIA.WEIXIN_CIRCLE,
                            "听日课，赢金币",
                            "墨子",
                            null,
                            SystemConstant.MYURL + "/view/fore/rikeActivity/signShare.jsp"
                            , null
                    );
                }
            });
            llSearchQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                    String url = "http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                            .withTitle("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    shareWeb(
                            SHARE_MEDIA.QQ,
                            "听日课，赢金币",
                            "墨子",
                            null,
                            SystemConstant.MYURL + "/view/fore/rikeActivity/signShare.jsp"
                            , null
                    );
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTO_CONTROL:
//                    for (OneDayArticles article : RecommendFragment.getInstance().playList) {
//                        if (article.getId().equals(AudioPlayManager.getInstance().getmMediaPlayId())) {
//                            int clickCount = article.getPraisenum();
//                            clickCount++;
//                            article.setPraisenum(clickCount);
//                            listAdapter.notifyDataSetChanged();
//                        }
//                    }
                    break;
            }
        }
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
                listAdapter.setDatas(dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)));
                currentDate = String.format("%04d-%02d-%02d", year, month, lastSelected);
                tvCurrentDate.setText(currentDate);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(currentDate).getTime() - sdf.parse(todayDate).getTime() > 0) {
                        tvDataState.setText("敬请期待");
                        tvDataState.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)) == null) {
                    initData(String.format("%04d-%02d-%02d", year, month, lastSelected));
                } else if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)).size() > 0) {
                    tvDataState.setVisibility(View.GONE);
                } else if (dataMap.get(String.format("%04d-%02d-%02d", year, month, lastSelected)).size() == 0) {
                    tvDataState.setText("敬请期待");
                    tvDataState.setVisibility(View.VISIBLE);
                }
            }
        });
        return gridView;
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof TodayAchievementActivity))
            return;
        switch (msg.getWhat()) {
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
            case SystemConstant.GET_CUTTENT_PLAY_LIST:
//                RecommendFragment.getInstance().playList = (ArrayList<OneDayArticles>) msg.getBody();
//                playPosition = msg.getArg1();
                break;
            case SystemConstant.ON_TODAY_STATE:
                if (msg.getArg1() == 1) {
                    tvRecordStudy.setText("打卡学习");
                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frame);
                    tvRecordStudy.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    tvRecordStudy.setText("已打卡");
                    tvRecordStudy.setBackgroundResource(R.drawable.today_item_frames);
                    tvRecordStudy.setTextColor(Color.parseColor("#f1312e"));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.empty_view);
        Intent i = new Intent();
        setResult(1, i);
//        mTimer.cancel();
//        mTimerTask.cancel();
//        mTimer = null;
//        mTimerTask = null;
//        AudioPlayManager.getInstance().release();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mRefreshBroadcastReceiver);
        System.gc();
    }
}
