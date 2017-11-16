package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.service.MyLocalService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

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
 * Created by nby on 2017/7/6.
 * 作用：
 */

public class TheNewTodayAllAdapter extends RecyclerAdapter<OneDayArticles> {
    private Context mContext;
    public TheNewTodayAllAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<OneDayArticles> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<OneDayArticles> {
        private ImageView imgTodayCor;
        private TextView tvTitle;
        private TextView tvReadCount;
        private TextView tvPraiseNum;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_branch_history_list);
        }

        @Override
        public void setData(final OneDayArticles object) {
            super.setData(object);
            tvTitle.setText(object.getTitle());
            Glide.with(getContext()).load(Util.getImgUrl(object.getCoverimgurl())).placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed).into(imgTodayCor);
            tvPraiseNum.setText(TimeUtils.getMagazineLength((int) object.getTimelength()));
            tvReadCount.setText(object.getContentauthor());

            if ((object.getId()).equals(AudioPlayManager.getInstance().getCurrId()) && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                    || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
                //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
                tvTitle.setTextColor(getContext().getResources().getColor(R.color.defaultcolor));
            } else {
                tvTitle.setTextColor(getContext().getResources().getColor(R.color.black_33));
            }
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgTodayCor = findViewById(R.id.img_today_cor);
            tvTitle = findViewById(R.id.tv_title);
            tvReadCount = findViewById(R.id.tv_read_count);
            tvPraiseNum = findViewById(R.id.tv_praise_num);

        }

        @Override
        public void onItemViewClick(OneDayArticles object) {
            super.onItemViewClick(object);
            //点击事件
            List<OneDayArticles> list = new ArrayList<>();
            list.add(object);
            if (!AudioPlayManager.getInstance().isInited() || !(object.getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
//                AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
//                AudioPlayManager.getInstance().ijkStart();
                Intent intent = new Intent(mContext, MyLocalService.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                bundle.putParcelableArrayList("mData", null);
                bundle.putParcelableArrayList("mDatas", (ArrayList<OneDayArticles>) list);
                intent.putExtras(bundle);
                mContext.startService(intent);
                DemoApplication.isPlay = true;
            } else {
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PREPARE:
                        //停止
                        AudioPlayManager.getInstance().release();
                        break;
                    case SystemConstant.ON_PAUSE:
                        //恢复播放
                        AudioPlayManager.getInstance().resume();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        //暂停
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                }
            }

            notifyDataSetChanged();
        }
    }
}


