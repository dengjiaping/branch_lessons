package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;
import com.yidiankeyan.science.view.rollviewpager.hintview.ColorPointHintView;

import java.util.List;

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
 * Created by nby on 2016/11/7.
 * 作用：
 */
public class BannerAdapter extends LoopPagerAdapter {

    private Context mContext;
    private List<BannerBean> mBannerList;
    private RollPagerView rollPagerView;

    public BannerAdapter(RollPagerView viewPager, Context mContext, List<BannerBean> mBannerList) {
        super(viewPager);
        rollPagerView = viewPager;
        this.mContext = mContext;
        this.mBannerList = mBannerList;
        if (mBannerList.size() < 2) {
            rollPagerView.setHintView(null);
        } else {
            rollPagerView.initHint(new ColorPointHintView(mContext, Color.parseColor("#E3AC42"), Color.parseColor("#88ffffff")));
        }
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        Glide.with(mContext).load(Util.getImgUrl(mBannerList.get(position).getImgurl())).error(R.drawable.icon_banner_load)
                .placeholder(R.drawable.icon_banner_load).into(view);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        if (mBannerList.size() < 2) {
            rollPagerView.setHintView(null);
        } else {
            rollPagerView.initHint(new ColorPointHintView(mContext, Color.parseColor("#E3AC42"), Color.parseColor("#88ffffff")));
        }
        super.notifyDataSetChanged();
    }

    @Override
    public int getRealCount() {
        return mBannerList.size();
    }
}
