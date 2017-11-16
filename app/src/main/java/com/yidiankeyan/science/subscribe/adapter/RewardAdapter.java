package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.subscribe.entity.GratuityPersonInfo;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

public class RewardAdapter extends BaseAdapter {

    private List<GratuityPersonInfo> list;
    private Context context;

    public RewardAdapter(List<GratuityPersonInfo> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reward_record, parent, false);
            viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.txt_reward_money);
            viewHolder.imgUrl = (ImageView) convertView.findViewById(R.id.img_reward);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_reward_time);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.txt_reward_title);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_reward_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GratuityPersonInfo recordBean = list.get(position);
        Glide.with(context).load(Util.getImgUrl(list.get(position).getImgurl())).into(viewHolder.imgUrl);
        viewHolder.txtName.setText(recordBean.getUsername());
//        viewHolder.tvCount.setText(recordBean.getTitle());
        viewHolder.txtTime.setText(TimeUtils.questionCreateDuration(recordBean.getCreatetime()));
        viewHolder.tvMoney.setText(recordBean.getTip_price() + "");
        return convertView;
    }

    class ViewHolder {
        private ImageView imgUrl;
        private TextView tvMoney;
        private TextView txtTime;
        private TextView tvCount;
        private TextView txtName;
    }
}
