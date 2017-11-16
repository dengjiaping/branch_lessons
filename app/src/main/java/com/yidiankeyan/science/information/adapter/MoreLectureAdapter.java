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
import com.yidiankeyan.science.information.acitivity.RecommendWebActivity;
import com.yidiankeyan.science.information.entity.NotifyActivityBean;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by zn on 2016/8/3 0003.
 */
public class MoreLectureAdapter extends BaseAdapter {

    private List<NotifyActivityBean> mDatas;
    private Context context;
    private String type;

    public MoreLectureAdapter(Context context, List<NotifyActivityBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    public void setType(String type) {
        this.type = type;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_more_lecture, parent, false);
            viewHolder.textNane = (TextView) convertView.findViewById(R.id.txt_ablum_name);
            viewHolder.imgPicture = (ImageView) convertView.findViewById(R.id.img_item_more);
            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_ablum_content);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_data_time);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txt_message_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NotifyActivityBean allBean = mDatas.get(position);
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl())).into(viewHolder.imgPicture);
        viewHolder.txtTime.setText("时间：" + allBean.getStarttime());
        if ("讲座信息".equals(type)) {
            viewHolder.textNane.setText("题目：" + allBean.getTitle());
            viewHolder.txtContent.setText("主讲人：" + allBean.getHoster());
        } else {
            viewHolder.textNane.setText("主题：" + allBean.getTitle());
            viewHolder.txtContent.setText("承办：" + allBean.getHoster());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, RecommendWebActivity.class).putExtra("url", "http://moz.poinetech.com/cmsweb/notice/info/" + mDatas.get(position).getId()));
            }
        });
//        viewHolder.txtMessage.setText(allBean.getContentId());
        return convertView;
    }

    class ViewHolder {
        ImageView imgPicture;
        TextView textNane;
        TextView txtContent;
        TextView txtTime;
        TextView txtMessage;
    }

}
