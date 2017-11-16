package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.adapter.BannerAdapter;
import com.yidiankeyan.science.information.adapter.NoticeListAdapter;
import com.yidiankeyan.science.information.entity.AboutUs;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.MagazineBean;
import com.yidiankeyan.science.information.entity.NoticeBean;
import com.yidiankeyan.science.information.entity.NotifyActivityBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * 资讯-公告
 */
public class NoticeFragment extends Fragment {

    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinal lvNotice;
    private View viewtop;
    private RollPagerView mRollViewPager;
    private BannerAdapter mLoopAdapter;
    private List<BannerBean> mBannerList = new ArrayList<>();
    private List<NoticeBean> mNoticeList = new ArrayList<>();
    private NoticeListAdapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (NoticeBean notice : mNoticeList) {
                if (!notice.isLoadFinish())
                    return;
            }
            adapter.notifyDataSetChanged();
            ptrLayout.onRefreshComplete();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
        lvNotice = (ListViewFinal) view.findViewById(R.id.lv_notice);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        lvNotice.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mLoopAdapter = new BannerAdapter(mRollViewPager, getContext(), mBannerList);
        mRollViewPager.setAdapter(mLoopAdapter);
        mRollViewPager.setHintAlpha(0);
//        mRollViewPager.setHintView(null);
        if (mBannerList.size() == 0)
            toHttpGetBanner();
        //因为轮播图的bug导致图片有几率没加载出来，所以重新加载一次
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
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
        adapter = new NoticeListAdapter(getContext(), mNoticeList);
        lvNotice.setAdapter(adapter);
        if (mNoticeList.size() == 0) {
            ptrLayout.autoRefresh();
        }
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                toHttpGetMagazine();
                toHttpGetWechat();
//                toHttpGetAboutUs();
                toHttpGetWebsite();
                toHttpGetNotifyActivity();
                toHttpGetNotifyTechnologyActivity();
            }
        });
    }

    /**
     * 科技活动信息
     */
    private void toHttpGetNotifyTechnologyActivity() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        map.put("type", 2);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_NOTIFY_ACTIVITY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<NotifyActivityBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NotifyActivityBean.class);
                    mNoticeList.get(1).removeAll();
                    for (NotifyActivityBean aboutUs : list) {
                        mNoticeList.get(1).createChild(SystemConstant.ACCESS_IMG_HOST + aboutUs.getCoverimgurl(), aboutUs.getTitle(), aboutUs.getHoster(), aboutUs.getStarttime(), aboutUs.getId());
                    }
                }
                mNoticeList.get(1).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(1).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 讲座信息
     */
    private void toHttpGetNotifyActivity() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        map.put("type", 1);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_NOTIFY_ACTIVITY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<NotifyActivityBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NotifyActivityBean.class);
                    mNoticeList.get(0).removeAll();
                    for (NotifyActivityBean aboutUs : list) {
                        mNoticeList.get(0).createChild(SystemConstant.ACCESS_IMG_HOST + aboutUs.getCoverimgurl(), aboutUs.getTitle(), aboutUs.getHoster(), aboutUs.getStarttime(), aboutUs.getId());
                    }
                }
                mNoticeList.get(0).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(0).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 关于我们
     */
    private void toHttpGetAboutUs() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<>();
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_ABOUT_US, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<AboutUs> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AboutUs.class);
                    mNoticeList.get(0).removeAll();
                    for (AboutUs aboutUs : list) {
                        mNoticeList.get(0).createChild(SystemConstant.ACCESS_IMG_HOST + aboutUs.getCoverimgurl(), aboutUs.getName());
                    }
                }
                mNoticeList.get(0).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(0).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 网站推荐
     */
    private void toHttpGetWebsite() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_WEBSITE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<MagazineBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), MagazineBean.class);
                    mNoticeList.get(4).removeAll();
                    for (MagazineBean magazine : list) {
                        mNoticeList.get(4).createChild(SystemConstant.ACCESS_IMG_HOST + magazine.getCoverimg(), magazine.getName(), magazine.getIntroduction(), magazine.getWebsiteurl());
                    }
                }
                mNoticeList.get(4).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(4).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 公众号推荐
     */
    private void toHttpGetWechat() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_WECHAT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<MagazineBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), MagazineBean.class);
                    mNoticeList.get(3).removeAll();
                    for (MagazineBean magazine : list) {
                        mNoticeList.get(3).createChild(SystemConstant.ACCESS_IMG_HOST + magazine.getCoverimg(), magazine.getName(), magazine.getIntroduction());
                    }
                }
                mNoticeList.get(3).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(3).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 科技杂志推荐
     */
    private void toHttpGetMagazine() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.GET_MAGAZINE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                if (result.getCode() == 200) {
                    List<MagazineBean> list = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), MagazineBean.class);
                    mNoticeList.get(2).removeAll();
                    for (MagazineBean magazine : list) {
                        mNoticeList.get(2).createChild(magazine.getCoverimg(), magazine.getName());
                    }
                }
                mNoticeList.get(2).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mNoticeList.size() == 0) {
                    initData();
                }
                mNoticeList.get(2).setLoadFinish(true);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private void initData() {
//        NoticeBean bean1 = new NoticeBean("关于我们", 0);
        NoticeBean bean2 = new NoticeBean("讲座信息", 1);
        NoticeBean bean3 = new NoticeBean("科技活动信息", 1);
        NoticeBean bean4 = new NoticeBean("科技杂志推荐", 3);
        NoticeBean bean5 = new NoticeBean("科技公众号推荐", 2);
        NoticeBean bean6 = new NoticeBean("科技网站推荐", 2);
//        mNoticeList.add(bean1);
        mNoticeList.add(bean2);
        mNoticeList.add(bean3);
        mNoticeList.add(bean4);
        mNoticeList.add(bean5);
        mNoticeList.add(bean6);
    }

    /**
     * 获取轮播图
     */
    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 1);
        map.put("pages",1);
        map.put("pagesize",6);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<BannerBean> data = GsonUtils.json2List(jsonData, BannerBean.class);
                    mBannerList.removeAll(mBannerList);
                    mBannerList.addAll(data);
                    mLoopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }


}
