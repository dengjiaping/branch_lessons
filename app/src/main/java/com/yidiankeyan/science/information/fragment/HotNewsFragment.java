package com.yidiankeyan.science.information.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.HotNewsAdapter;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
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
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/11/24.
 * 作用：热点新闻
 */
public class HotNewsFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvRecommend;
    private List<HotNewsBean> mDataNews = new ArrayList<>();
    private List<HotNewsBean> mDataHot = new ArrayList<>();
    private List<HotNewsBean> mData = new ArrayList<>();
    private HotNewsAdapter adapter;
    private View headView;
    private TextView tvCustom;
    private AutoRelativeLayout rlHotNewsClick;
    private boolean isFo;
    private RadioGroup radioGroup;
    private RadioButton rbNewest;
    private RadioButton rbHot;
    private int newsPage = 1;
    private int hotPage = 1;
    private static long hotspotTimestamp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_answer, container, false);
        initView(view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_newest) {
                    //最新
                    isFo = false;
                    if (mDataNews.size() == 0) {
                        ptrLayout.autoRefresh();
                    } else {
                        mData.removeAll(mData);
                        mData.addAll(mDataNews);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    //热门
                    isFo = true;
                    if (mDataHot.size() == 0) {
                        ptrLayout.autoRefresh();
                    } else {
                        mData.removeAll(mData);
                        mData.addAll(mDataHot);
                        adapter.notifyDataSetChanged();
                    }
                }
                SpUtils.putBooleanSp(getContext(), "hotNews", isFo);
            }
        });
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                toHttpGetNews();
                if (rbHot.isChecked()) {
                    hotPage = 1;
                    isFo = true;
//                    tvCustom.setTextColor(Color.parseColor("#333333"));
//                    tvCustom.setBackgroundResource(R.drawable.select_hot_news);
                    toHttpGetNews();
                } else {
                    newsPage = 1;
                    isFo = false;
//                    tvCustom.setTextColor(Color.parseColor("#FFFFFF"));
//                    tvCustom.setBackgroundResource(R.drawable.select_register_phones);
                    toHttpGetNewsTime();
                }
            }
        });
        if (SpUtils.getBooleanSp(getContext(), "hotNews") == false) {
            rbNewest.setChecked(true);
        } else {
            rbHot.setChecked(true);
        }
        if (mDataNews.size() == 0 && mDataHot.size() == 0) {
            newsPage = 1;
            hotPage = 1;
        } else {
            lvRecommend.setHasLoadMore(true);
        }
        lvRecommend.addHeaderView(headView);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        adapter = new HotNewsAdapter(getContext(), mData);
        lvRecommend.setAdapter(adapter);
        lvRecommend.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                if (rbHot.isChecked()) {
//                    tvCustom.setTextColor(Color.parseColor("#333333"));
//                    tvCustom.setBackgroundResource(R.drawable.select_hot_news);
                    toHttpGetNews();
                } else {
//                    tvCustom.setTextColor(Color.parseColor("#FFFFFF"));
//                    tvCustom.setBackgroundResource(R.drawable.select_register_phones);
                    toHttpGetNewsTime();
                }

            }
        });
        lvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        return view;
    }

    private void toHttpGetNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", hotPage);
        map.put("pagesize", 20);
        map.put("orientation", 1);
        ApiServerManager.getInstance().getApiServer().getHotNews(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (hotPage == 1) {
                        mDataHot.removeAll(mDataHot);
                    }
                    if (response.body().getData().size() > 0) {
                        lvRecommend.setHasLoadMore(true);
                        mDataHot.addAll(response.body().getData());
                        hotPage++;
                    } else {
                        lvRecommend.setHasLoadMore(false);
                    }
                    mData.removeAll(mData);
                    mData.addAll(mDataHot);
                    adapter.notifyDataSetChanged();
                } else {
                    lvRecommend.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvRecommend.showFailUI();
            }
        });
    }

    private void toHttpGetNewsTime() {
        //最新
        Map<String, Object> map = new HashMap<>();
        map.put("pages", newsPage);
        if (newsPage == 1) {
            hotspotTimestamp = System.currentTimeMillis();
            map.put("periodstart", hotspotTimestamp);
        } else {
            map.put("periodstart", hotspotTimestamp);
        }
        map.put("pagesize", 20);
        map.put("orientation", 1);

        ApiServerManager.getInstance().getApiServer().getHotNewsTime(map).enqueue(new RetrofitCallBack<List<HotNewsBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<HotNewsBean>>> call, Response<RetrofitResult<List<HotNewsBean>>> response) {
                if (response.body().getCode() == 200) {
                    LogUtils.e(response.body().getData().toString());
                    if (newsPage == 1) {
                        mDataNews.removeAll(mDataNews);
                    }
                    if (response.body().getData().size() > 0) {
                        lvRecommend.setHasLoadMore(true);
                        mDataNews.addAll(response.body().getData());
                        newsPage++;
                    } else {
                        lvRecommend.setHasLoadMore(false);
                    }
                    mData.removeAll(mData);
                    mData.addAll(mDataNews);
                    adapter.notifyDataSetChanged();
                } else {
                    lvRecommend.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<HotNewsBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvRecommend.showFailUI();
            }
        });
    }

    private void initView(View view) {
        lvRecommend = (ListViewFinalLoadMore) view.findViewById(R.id.lv_recent_answer);
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_recommend_layout);
        headView = LayoutInflater.from(getContext()).inflate(R.layout.head_hot_news, null);
        tvCustom = (TextView) headView.findViewById(R.id.tv_custom);
        radioGroup = (RadioGroup) headView.findViewById(R.id.radio_group_news);
        rbNewest = (RadioButton) headView.findViewById(R.id.rb_newest);
        rbHot = (RadioButton) headView.findViewById(R.id.rb_hot);
        rlHotNewsClick = (AutoRelativeLayout) headView.findViewById(R.id.rl_hot_news_click);
//        rlHotNewsClick.setOnClickListener(this);
    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.rl_hot_news_click:
//////                if (TextUtils.isEmpty(SpUtils.getStringSp(getContext(), "userId"))) {
//////                    startActivity(new Intent(getContext(), MainLoginActivity.class));
//////                    return;
//////                }
//////                startActivity(new Intent(getContext(), CustomProjectActivity.class));
////                if (isFo) {
////                    isFo = false;
////                    tvCustom.setTextColor(Color.parseColor("#333333"));
////                    tvCustom.setBackgroundResource(R.drawable.select_hot_news);
////                    ptrLayout.autoRefresh();
////                } else {
////                    isFo = true;
////                    tvCustom.setTextColor(Color.parseColor("#FFFFFF"));
////                    tvCustom.setBackgroundResource(R.drawable.select_register_phones);
////                    ptrLayout.autoRefresh();
////                }
////                SpUtils.putBooleanSp(getContext(), "hotNews", isFo);
////                break;
//        }
//    }
}
