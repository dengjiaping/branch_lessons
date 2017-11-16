package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.yidiankeyan.science.my.adapter.MyFansFollowAdapter;
import com.yidiankeyan.science.my.entity.FansFollow;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/5/27 0027.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤   被装饰类要和装饰类继承自同一父类
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InterviewMyAdapter extends BaseAdapter {

    private ArrayList<InterviewSoonBean> mDatas;
    private Context mContext;

    public InterviewMyAdapter(Context context, ArrayList<InterviewSoonBean> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_interview_list, parent, false);
            viewHolder.mContentName = (TextView) convertView.findViewById(R.id.tv_content_name);
            viewHolder.mVideoLength = (TextView) convertView.findViewById(R.id.tv_video_length);
            viewHolder.imgInterviewBg = (ImageView) convertView.findViewById(R.id.img_interview_bg);
            viewHolder.mEstablishTime = (TextView) convertView.findViewById(R.id.tv_establish_time);
            viewHolder.mCommentNumber = (TextView) convertView.findViewById(R.id.tv_comment_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mContentName.setText(mDatas.get(position).getInterviewName());
        if (!TextUtils.equals("null", mDatas.get(position).getInterviewVideoUrl()) && !TextUtils.isEmpty(mDatas.get(position).getInterviewVideoUrl())) {
            viewHolder.mVideoLength.setVisibility(View.VISIBLE);
            viewHolder.mVideoLength.setText(TimeUtils.getInterviewTime(Integer.parseInt(mDatas.get(position).getVideoUrlLength())));
        } else {
            viewHolder.mVideoLength.setVisibility(View.GONE);
            viewHolder.mVideoLength.setText("");
        }

        if (!TextUtils.equals("null", mDatas.get(position).getCreateTime()) && !TextUtils.isEmpty(mDatas.get(position).getCreateTime())) {
            viewHolder.mEstablishTime.setText("上线：" + mDatas.get(position).getCreateTime().replace(".0", ""));
        } else {
            viewHolder.mEstablishTime.setText("");
        }
        if (!TextUtils.equals("null", mDatas.get(position).getCommentnum()) && !TextUtils.isEmpty(mDatas.get(position).getCommentnum())) {
            viewHolder.mCommentNumber.setText("评论：" + mDatas.get(position).getCommentnum());
        } else {
            viewHolder.mCommentNumber.setText("");
        }

        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getInterviewImgUrl()))
                .placeholder(R.drawable.icon_banner_load)
                .error(R.drawable.icon_banner_load)
                .into(viewHolder.imgInterviewBg);
        return convertView;
    }

    class ViewHolder {
        TextView mContentName;
        TextView mVideoLength;
        ImageView imgInterviewBg;
        TextView mEstablishTime;
        TextView mCommentNumber;
    }

}
