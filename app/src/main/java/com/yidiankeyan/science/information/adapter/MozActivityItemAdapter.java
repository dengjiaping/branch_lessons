package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.LotteryActivity;
import com.yidiankeyan.science.information.acitivity.VoteActivity;
import com.yidiankeyan.science.information.entity.MozClassActivityBean;
import com.yidiankeyan.science.utils.Util;

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
 * Created by nby on 2016/11/9.
 * 作用：
 */
public class MozActivityItemAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private MozClassActivityBean mozClassActivityBean;
    public int TYPE_ACTIVITY = 0;
    public int TYPE_VOTE = 1;
    public int TYPE_LOTTERY = 2;

    public MozActivityItemAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mozClassActivityBean == null ?
                0 : (mozClassActivityBean.getActivityList() == null ?
                0 : mozClassActivityBean.getActivityList().size()) + (mozClassActivityBean.getLotteryList() == null ?
                0 : mozClassActivityBean.getLotteryList().size()) + (mozClassActivityBean.getVoteList() == null ?
                0 : mozClassActivityBean.getVoteList().size());
    }

    @Override
    public Object getItem(int position) {
        if (position < (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size())) {
            return mozClassActivityBean.getActivityList().get(position);
        } else if (position >= (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size()) &&
                position < (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size()) +
                        (mozClassActivityBean.getVoteList() == null ? 0 : mozClassActivityBean.getVoteList().size())) {
            return mozClassActivityBean.getVoteList().get(position - (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size()));
        } else
            return mozClassActivityBean.getLotteryList().get(position - mozClassActivityBean.getActivityList().size() - mozClassActivityBean.getVoteList().size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size())) {
            //该类是活动
            return TYPE_ACTIVITY;
        } else if (position >= (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size()) &&
                position < (mozClassActivityBean.getActivityList() == null ? 0 : mozClassActivityBean.getActivityList().size()) +
                        (mozClassActivityBean.getVoteList() == null ? 0 : mozClassActivityBean.getVoteList().size())) {
            //该类是投票
            return TYPE_VOTE;
        } else {
            return TYPE_LOTTERY;
        }
    }

    public MozClassActivityBean getMozClassActivityBean() {
        return mozClassActivityBean;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_moz_activity, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (getItemViewType(position)) {
            case 0:
                //活动
                Glide.with(mContext).load(Util.getImgUrl(((MozClassActivityBean.ActivityListBean) getItem(position)).getCoverimgurl())).into(holder.imgAvatar);
                holder.tvTitle.setText(((MozClassActivityBean.ActivityListBean) getItem(position)).getName());
                break;
            case 1:
                //投票
                Glide.with(mContext).load(Util.getImgUrl(((MozClassActivityBean.VoteListBean) getItem(position)).getCoverimgurl())).into(holder.imgAvatar);
                holder.tvTitle.setText(((MozClassActivityBean.VoteListBean) getItem(position)).getTitle());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VoteActivity.class);
                        intent.putExtra("id", ((MozClassActivityBean.VoteListBean) getItem(position)).getId());
                        intent.putExtra("startTime", ((MozClassActivityBean.VoteListBean) getItem(position)).getStarttime());
                        intent.putExtra("endTime", ((MozClassActivityBean.VoteListBean) getItem(position)).getEndtime());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 2:
                //抽奖
                Glide.with(mContext).load(Util.getImgUrl(((MozClassActivityBean.LotteryListBean) getItem(position)).getImgurl())).into(holder.imgAvatar);
                holder.tvTitle.setText(((MozClassActivityBean.LotteryListBean) getItem(position)).getName());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, LotteryActivity.class);
                        intent.putExtra("id", ((MozClassActivityBean.LotteryListBean) getItem(position)).getId());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        return convertView;
    }

    public void setMozClassActivityBean(MozClassActivityBean mozClassActivityBean) {
        this.mozClassActivityBean = mozClassActivityBean;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
    }
}
