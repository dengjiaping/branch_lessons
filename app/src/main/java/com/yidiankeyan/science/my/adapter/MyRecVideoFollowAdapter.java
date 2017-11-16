package com.yidiankeyan.science.my.adapter;

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
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.knowledge.activity.TagDetailActivity;
import com.yidiankeyan.science.my.entity.VideoArticleFollowBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Created by zn on 2017/7/14
 * -我的关注   视频adapter
 */
public class MyRecVideoFollowAdapter extends BaseAdapter {

    private Context mContext;
    private List<VideoArticleFollowBean> mData;
    private LayoutInflater mInflater;


    public MyRecVideoFollowAdapter(Context context, List<VideoArticleFollowBean> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public VideoArticleFollowBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        videoBViewHolder videoBViewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_type_video, parent, false);
            videoBViewHolder = new videoBViewHolder(convertView);
            convertView.setTag(videoBViewHolder);
        } else {

            videoBViewHolder = (videoBViewHolder) convertView.getTag();
        }
        videoBViewHolder.tvTime.setText(getTime(mData.get(position).getCreatetime()));
        videoBViewHolder.tvAuthorName.setText(mData.get(position).getMozname());
        videoBViewHolder.tvTitle.setText(mData.get(position).getName());
        if (mData.get(position).getLength() == 0) {
            videoBViewHolder.tvVideoTime.setText("");
            videoBViewHolder.tvVideoTime.setVisibility(View.INVISIBLE);
        } else {
            videoBViewHolder.tvVideoTime.setVisibility(View.VISIBLE);
            videoBViewHolder.tvVideoTime.setText(TimeUtils.getInterviewTime(mData.get(position).getLength()));
        }
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                .placeholder(R.drawable.icon_hotload_failed)
                .error(R.drawable.icon_hotload_failed).into(videoBViewHolder.imgAvatar);
        videoBViewHolder.tvAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mData.get(position).getMozid();
                if (mData.get(position).getType() == 1) {
                    Intent intent = new Intent(mContext, MozDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", mData.get(position).getType());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, TagDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", mData.get(position).getType());
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class videoBViewHolder {
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private ImageView imgAvatar;
        private TextView tvVideoTime;

        public videoBViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvVideoTime = (TextView) convertView.findViewById(R.id.tv_video_time);
        }
    }

    private String getTime(long millisecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millisecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0) {
            return new SimpleDateFormat("MM-dd").format(new Date(millisecond));
        }
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 0)
            return minute + "分钟前";
        long second = duration / 1000;
        if (second == 0)
            return "1秒前";
        else
            return second + "秒前";
    }
}
