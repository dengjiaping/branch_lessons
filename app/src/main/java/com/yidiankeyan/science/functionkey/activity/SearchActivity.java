package com.yidiankeyan.science.functionkey.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.functionkey.adapter.SearchHistoryAdapter;
import com.yidiankeyan.science.functionkey.adapter.SearchHotGridAdapter;
import com.yidiankeyan.science.functionkey.fragment.SearchAblumFragment;
import com.yidiankeyan.science.functionkey.fragment.SearchAnswerFragment;
import com.yidiankeyan.science.functionkey.fragment.SearchContentFragment;
import com.yidiankeyan.science.functionkey.fragment.SearchEditorFragment;
import com.yidiankeyan.science.functionkey.fragment.SearchWholeFragment;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.ExpandableLinearLayout;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/***
 * 搜索页
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    /**
     * 搜索前
     */
    private ListView lvHotItem;
    private TextView textfinsh;
    private SearchHotGridAdapter hotGridAdapter;
    /**
     * 热搜数据
     */
    private List<String> mDatas = new ArrayList<>();
    private AutoLinearLayout llCancel;
    private EditText etSearchContent;

    /**
     * 搜索历史
     */
    private List<String> mContactDatas;
    private ImageView tvClear;
    private TextView tvTip;
    private AutoLinearLayout llHis;
    private ScrollView svNoSearch;
    private SearchAblumFragment ablumFragment = null;
    private SearchWholeFragment wholeFragment = null;
    private SearchContentFragment contentFragment = null;
    private SearchEditorFragment editorFragment = null;
    private SearchAnswerFragment answerFragment = null;
    private List<String> datas;
    private ShowAllListView gvTagHistory;
    private SearchHistoryAdapter historyAdapter;
    private ExpandableLinearLayout expandLayout;

    /**
     * 搜索后
     */
    private ViewPager vpSearch;// 页卡内容
    private TextView txtAll, txtAlbum, txtContent, txtEditor, txtAnswer;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private MyPagerAdapter adapter;
    private AutoLinearLayout atFragment;

//
//    //设置搜索内容的触摸事件，（必须至少一个处于选中状态，如果只有一个处于选中状态，那么这个CheckBox不可置为未选中）
//    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                CheckBox cb = (CheckBox) v;
//                if (cb.isChecked()) {
//                    int count = 0;
//                    for (CheckBox checkBox : checkBoxList) {
//                        if (checkBox.isChecked()) {
//                            count++;
//                        }
//                    }
//                    if (count > 1) {
//                        return false;
//                    } else {
//                        Toast.makeText(mContext, "请至少选择一个", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        }
//    };

    @Override
    protected int setContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        textfinsh = (TextView) findViewById(R.id.tv_cancel);
        lvHotItem = (ListView) findViewById(R.id.lv_hot_item);
        llCancel = (AutoLinearLayout) findViewById(R.id.ll_cancel);
        etSearchContent = (EditText) findViewById(R.id.et_search_content);
        tvClear = (ImageView) findViewById(R.id.tv_clear);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        llHis = (AutoLinearLayout) findViewById(R.id.ll_his);
        gvTagHistory = (ShowAllListView) findViewById(R.id.gv_tag_history);
        svNoSearch = (ScrollView) findViewById(R.id.sv_no_search);
        expandLayout = (ExpandableLinearLayout) findViewById(R.id.expand_layout);

        txtAll = (TextView) findViewById(R.id.search_all);
        txtAlbum = (TextView) findViewById(R.id.search_ablum);
        txtContent = (TextView) findViewById(R.id.search_content);
        txtEditor = (TextView) findViewById(R.id.search_editor);
        txtAnswer = (TextView) findViewById(R.id.search_answer);
        vpSearch = (ViewPager) findViewById(R.id.vp_search);
        atFragment = (AutoLinearLayout) findViewById(R.id.search_fragment);

//        mFlowLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                llHis.setVisibility(View.VISIBLE);
//                mContactDatas = new DB(SearchActivity.this).querySearch();
//                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lp.leftMargin = 10;
//                lp.rightMargin = 5;
//                lp.topMargin = 10;
//                lp.bottomMargin = 10;
//                for (int i = 0; i < mContactDatas.size(); i++) {
//                    TextView view = new TextView(SearchActivity.this);
//                    view.setText(mContactDatas.get(i));
//                    view.setTextColor(Color.BLACK);
//                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
//                    mFlowLayout.addView(view, lp);
//                }
//            }
//        });
        wholeFragment = new SearchWholeFragment();
        ablumFragment = new SearchAblumFragment();
        contentFragment = new SearchContentFragment();
        editorFragment = new SearchEditorFragment();
        answerFragment = new SearchAnswerFragment();
    }

    @Override
    protected void initAction() {
        expandLayout.setCollapsed(true);
        if (DB.getInstance(DemoApplication.applicationContext).querySearch().size() != 0) {
            llHis.setVisibility(View.VISIBLE);
        }
        etSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    wholeFragment.clean();
                    ablumFragment.clean();
                    contentFragment.clean();
                    editorFragment.clean();
                    answerFragment.clean();
                    ((InputMethodManager) etSearchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!TextUtils.isEmpty(etSearchContent.getText().toString()) && !mContactDatas.contains(etSearchContent.getText().toString())) {
                        new DB(SearchActivity.this).insertSearch(etSearchContent.getText().toString().trim());
                    }

                    svNoSearch.setVisibility(View.GONE);
                    atFragment.setVisibility(View.VISIBLE);
                    llHis.setVisibility(View.VISIBLE);
                    mContactDatas = new DB(SearchActivity.this).querySearch();
                    historyAdapter = new SearchHistoryAdapter(SearchActivity.this, mContactDatas);
                    View item = historyAdapter.getView(0, null, lvHotItem);
                    item.measure(0, 0);
                    expandLayout.setExpandHeight(item.getMeasuredHeight() * historyAdapter.getCount());
                    historyAdapter.setOnExpandCollapsedClickListener(new SearchHistoryAdapter.OnExpandCollapsedClickListener() {
                        @Override
                        public void onClick() {
                            View item = historyAdapter.getView(0, null, lvHotItem);
                            item.measure(0, 0);
                            if (expandLayout.isCollapsed()) {
                                expandLayout.setExpandHeight(item.getMeasuredHeight() * historyAdapter.getCount());
                                expandLayout.expand();
                            } else {
                                expandLayout.setCollapsedHeight(item.getMeasuredHeight() * 2);
                                expandLayout.collapsed();
                            }
                        }
                    });
                    gvTagHistory.setAdapter(historyAdapter);
                    historyAdapter.notifyDataSetChanged();
                    wholeFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                    ablumFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                    contentFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                    editorFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                    answerFragment.setSearchReturn(etSearchContent.getText().toString().trim());


                    int index = vpSearch.getCurrentItem();
                    switch (index) {
                        case 0:
                            wholeFragment.loadData(SearchActivity.this);
                            ablumFragment.loadData(SearchActivity.this);
                            break;
                        case 1:
                            wholeFragment.loadData(SearchActivity.this);
                            ablumFragment.loadData(SearchActivity.this);
                            contentFragment.loadData(SearchActivity.this);
                            break;
                        case 2:
                            ablumFragment.loadData(SearchActivity.this);
                            contentFragment.loadData(SearchActivity.this);
                            editorFragment.loadData(SearchActivity.this);
                            break;
                        case 3:
                            contentFragment.loadData(SearchActivity.this);
                            editorFragment.loadData(SearchActivity.this);
                            answerFragment.loadData(SearchActivity.this);
                            break;
                        case 4:
                            editorFragment.loadData(SearchActivity.this);
                            answerFragment.loadData(SearchActivity.this);
                            break;
                    }

                }
                return false;
            }
        });

        gvTagHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                svNoSearch.setVisibility(View.GONE);
                atFragment.setVisibility(View.VISIBLE);
                llHis.setVisibility(View.VISIBLE);
//                mContactDatas.removeAll(mContactDatas);
//                mContactDatas.addAll(new DB(SearchActivity.this).querySearch());
//                tagHistory.setTags(mContactDatas);
                etSearchContent.setText(mContactDatas.get(position).toString().trim());
                etSearchContent.setSelection(mContactDatas.get(position).toString().length());
                wholeFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                ablumFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                contentFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                editorFragment.setSearchReturn(etSearchContent.getText().toString().trim());
                answerFragment.setSearchReturn(etSearchContent.getText().toString().trim());

                int index = vpSearch.getCurrentItem();
                switch (index) {
                    case 0:
                        wholeFragment.loadData(SearchActivity.this);
                        ablumFragment.loadData(SearchActivity.this);
                        break;
                    case 1:
                        wholeFragment.loadData(SearchActivity.this);
                        ablumFragment.loadData(SearchActivity.this);
                        contentFragment.loadData(SearchActivity.this);
                        break;
                    case 2:
                        ablumFragment.loadData(SearchActivity.this);
                        contentFragment.loadData(SearchActivity.this);
                        editorFragment.loadData(SearchActivity.this);
                        break;
                    case 3:
                        contentFragment.loadData(SearchActivity.this);
                        editorFragment.loadData(SearchActivity.this);
                        answerFragment.loadData(SearchActivity.this);
                        break;
                    case 4:
                        editorFragment.loadData(SearchActivity.this);
                        answerFragment.loadData(SearchActivity.this);
                        break;
                }
            }
        });


        etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    svNoSearch.setVisibility(View.VISIBLE);
                    atFragment.setVisibility(View.GONE);
                    tvTip.setText("搜索历史");
                } else {
                    tvTip.setText("搜索结果");
                }

            }
        });
        textfinsh.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        //热门搜索
        addData();
        fragments = new ArrayList<>();
        fragments.add(wholeFragment);
        fragments.add(ablumFragment);
        fragments.add(contentFragment);
        fragments.add(editorFragment);
        fragments.add(answerFragment);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        vpSearch.setAdapter(adapter);
        txtAll.setOnClickListener(this);
        txtAlbum.setOnClickListener(this);
        txtContent.setOnClickListener(this);
        txtEditor.setOnClickListener(this);
        txtAnswer.setOnClickListener(this);
        lvHotItem.setOnItemClickListener(this);
        vpSearch.setCurrentItem(0);
        vpSearch.setOnPageChangeListener(new MyOnPageChangeListener());
        txtAll.setTextColor(Color.parseColor("#F1312E"));
        mContactDatas = new DB(SearchActivity.this).querySearch();
        historyAdapter = new SearchHistoryAdapter(this, mContactDatas);
        historyAdapter.setOnExpandCollapsedClickListener(new SearchHistoryAdapter.OnExpandCollapsedClickListener() {
            @Override
            public void onClick() {
                View item = historyAdapter.getView(0, null, lvHotItem);
                item.measure(0, 0);
                if (expandLayout.isCollapsed()) {
                    expandLayout.setExpandHeight(item.getMeasuredHeight() * historyAdapter.getCount());
                    expandLayout.expand();
                } else {
                    expandLayout.setCollapsedHeight(item.getMeasuredHeight() * 2);
                    expandLayout.collapsed();
                }
            }
        });
        gvTagHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        if (mContactDatas.size() > 2) {
            expandLayout.post(new Runnable() {
                @Override
                public void run() {
                    View item = historyAdapter.getView(0, null, lvHotItem);
                    item.measure(0, 0);
                    expandLayout.getLayoutParams().height = item.getMeasuredHeight() * 2;
                    expandLayout.requestLayout();
                }
            });
        }
    }

    private void addData() {
        HttpUtil.post(this, SystemConstant.URL + "size/search/hotwords", null, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), String.class);
                    for (String s : datas) {
                        mDatas.add(s.toString());
                        adapter.notifyDataSetChanged();
                    }
                    hotGridAdapter = new SearchHotGridAdapter(mContext, datas);
                    lvHotItem.setAdapter(hotGridAdapter);
                    setListViewHeightBasedOnChildren(lvHotItem);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        SearchHotGridAdapter listAdapter = (SearchHotGridAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.ll_cancel:
                etSearchContent.setText("");
                atFragment.setVisibility(View.GONE);
                wholeFragment.clean();
                ablumFragment.clean();
                contentFragment.clean();
                editorFragment.clean();
                answerFragment.clean();
                vpSearch.setCurrentItem(0);
                txtAll.setTextColor(Color.parseColor("#F1312E"));
                if (!expandLayout.isCollapsed()) {
                    expandLayout.expand();
                }
                break;
            case R.id.tv_clear:
                //清空搜索历史
                new DB(SearchActivity.this).deleteAllSearch();
                llHis.setVisibility(View.GONE);
                mContactDatas.removeAll(mContactDatas);
                break;
            case R.id.search_all:
                currIndex = 0;
                vpSearch.setCurrentItem(currIndex);
                break;
            case R.id.search_ablum:
                currIndex = 1;
                vpSearch.setCurrentItem(currIndex);
                break;
            case R.id.search_content:
                currIndex = 2;
                vpSearch.setCurrentItem(currIndex);
                break;
            case R.id.search_editor:
                currIndex = 3;
                vpSearch.setCurrentItem(currIndex);
                break;
            case R.id.search_answer:
                currIndex = 4;
                vpSearch.setCurrentItem(currIndex);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mContactDatas.contains(mDatas.get(position).toString().trim())) {
            new DB(SearchActivity.this).insertSearch(mDatas.get(position));
        }
        ((InputMethodManager) etSearchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (!TextUtils.isEmpty(etSearchContent.getText().toString()) && !mContactDatas.contains(etSearchContent.getText().toString())) {
            new DB(SearchActivity.this).insertSearch(etSearchContent.getText().toString().trim());
        }
        svNoSearch.setVisibility(View.GONE);
        atFragment.setVisibility(View.VISIBLE);
        llHis.setVisibility(View.VISIBLE);
        mContactDatas.removeAll(mContactDatas);
        mContactDatas.addAll(new DB(SearchActivity.this).querySearch());
        gvTagHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        etSearchContent.setText(mDatas.get(position).toString().trim());
        etSearchContent.setSelection(mDatas.get(position).toString().length());
        wholeFragment.setSearchReturn(etSearchContent.getText().toString().trim());
        ablumFragment.setSearchReturn(etSearchContent.getText().toString().trim());
        contentFragment.setSearchReturn(etSearchContent.getText().toString().trim());
        editorFragment.setSearchReturn(etSearchContent.getText().toString().trim());
        answerFragment.setSearchReturn(etSearchContent.getText().toString().trim());
        int index = vpSearch.getCurrentItem();
        switch (index) {
            case 0:
                wholeFragment.loadData(this);
                ablumFragment.loadData(this);
                break;
            case 1:
                wholeFragment.loadData(this);
                ablumFragment.loadData(this);
                contentFragment.loadData(this);
                break;
            case 2:
                ablumFragment.loadData(this);
                contentFragment.loadData(this);
                editorFragment.loadData(this);
                break;
            case 3:
                contentFragment.loadData(this);
                editorFragment.loadData(this);
                answerFragment.loadData(this);
                break;
            case 4:
                editorFragment.loadData(this);
                answerFragment.loadData(this);
                break;
        }
    }


    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
                    txtAll.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
                    txtAlbum.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 2:
                    txtContent.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 3:
                    txtEditor.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 4:
                    txtAnswer.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        txtAll.setTextColor(Color.parseColor("#999999"));
        txtAlbum.setTextColor(Color.parseColor("#999999"));
        txtContent.setTextColor(Color.parseColor("#999999"));
        txtEditor.setTextColor(Color.parseColor("#999999"));
        txtAnswer.setTextColor(Color.parseColor("#999999"));
    }
}
