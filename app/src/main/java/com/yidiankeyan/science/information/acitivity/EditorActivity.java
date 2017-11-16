package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.EditorRecommentAdapter;
import com.yidiankeyan.science.information.entity.EditorRecommentBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 推荐
 * -推荐专辑
 */
public class EditorActivity extends BaseActivity {
    //    private ViewPager vPager;
//    private AlbumPagerAdapter adapter;
    private ImageButton imgTitle;
    private AutoLinearLayout llReturn;
    private TextView txtTitle;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvEdupdate;
    private List<EditorRecommentBean> mDatas = new ArrayList<>();
    private EditorRecommentAdapter adapter;
    private int pages = 1;

//    private TextView tvUp;
//    private TextView tvHeat;
////    private TextView tvCapa;
//    private List<Fragment> fragmentList = new ArrayList<>();
//    private int currIndex = 0;// 当前页卡编号

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        imgTitle = (ImageButton) findViewById(R.id.title_btn);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvEdupdate = (ListViewFinalLoadMore) findViewById(R.id.lv_edupdate);
//        tvUp = (TextView) findViewById(R.id.tv_editup);
//        tvHeat = (TextView) findViewById(R.id.tv_editheat);
////        tvCapa = (TextView) findViewById(R.id.tv_editcapa);
//        vPager = (ViewPager) findViewById(R.id.view_edit);
    }

    @Override
    protected void initAction() {
        imgTitle.setVisibility(View.GONE);
        txtTitle.setText("推荐专辑");
        findViewById(R.id.ll_return).setOnClickListener(this);
//        fragmentList.add(new EditorUpdateFragment());
//        fragmentList.add(new EditorHeatFragment());
//        fragmentList.add(new EditorCapacityFragment());
//        adapter = new AlbumPagerAdapter(getSupportFragmentManager(), fragmentList);
//        vPager.setAdapter(adapter);
//        vPager.setCurrentItem(0);
//        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
//        llReturn.setOnClickListener(this);
//        tvUp.setOnClickListener(this);
//        tvHeat.setOnClickListener(this);
////        tvCapa.setOnClickListener(this);
//        tvUp.setTextColor(Color.parseColor("#F1312E"));
        //填充数据
        if (mDatas.size() == 0)
            ptrLayout.autoRefresh();
        else
            lvEdupdate.setHasLoadMore(true);
//        addData();
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                toHttp();
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        lvEdupdate.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                toHttp();
            }
        });
        adapter = new EditorRecommentAdapter(this, mDatas);
        lvEdupdate.setAdapter(adapter);
        lvEdupdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EditorActivity.this, AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getShortimgurl());
                startActivity(intent);
//            }
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_editor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
//            case R.id.tv_editup:
//                currIndex = 0;
//                vPager.setCurrentItem(currIndex);
//                break;
//            case R.id.tv_editheat:
//                currIndex = 1;
//                vPager.setCurrentItem(currIndex);
//                break;
////            case R.id.tv_editcapa:
////                currIndex = 2;
////                vPager.setCurrentItem(currIndex);
////                break;
        }
    }

    private void toHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        map.put("ordertype", 1);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_RECOMMEND_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<EditorRecommentBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), EditorRecommentBean.class);
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (datas.size() > 0) {
                        lvEdupdate.setHasLoadMore(true);
                        mDatas.addAll(datas);
                        pages++;
                    } else {
                        lvEdupdate.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvEdupdate.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                ptrLayout.onRefreshComplete();
                lvEdupdate.showFailUI();
            }
        });
    }

//    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            currIndex = position;
//            setNormal();
//            switch (currIndex) {
//                case 0:
//                    tvUp.setTextColor(Color.parseColor("#F1312E"));
//                    break;
//                case 1:
//                    tvHeat.setTextColor(Color.parseColor("#F1312E"));
//                    break;
////                case 2:
////                    tvCapa.setTextColor(Color.parseColor("#F1312E"));
////                    break;
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
//
//    private void setNormal() {
//        tvUp.setTextColor(Color.parseColor("#999999"));
//        tvHeat.setTextColor(Color.parseColor("#999999"));
////        tvCapa.setTextColor(Color.parseColor("#999999"));
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
