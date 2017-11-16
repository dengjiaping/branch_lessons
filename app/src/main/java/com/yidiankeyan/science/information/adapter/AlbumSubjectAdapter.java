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
import com.yidiankeyan.science.information.entity.RankingAlbumBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * 排行
 * -主体号Adapter
 */
public class AlbumSubjectAdapter extends BaseAdapter {
    private List<RankingAlbumBean> list;
    private Context context;

    public AlbumSubjectAdapter(List<RankingAlbumBean> list, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sybhectcharts, parent, false);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txt_number);
            viewHolder.imgAlbcharts = (ImageView) convertView.findViewById(R.id.img_albcharts);
            viewHolder.tuwenbiao = (ImageView) convertView.findViewById(R.id.tuwenbiao);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.sub_title);
            viewHolder.tvRecentContent = (TextView) convertView.findViewById(R.id.tv_recent_content);
            viewHolder.tvEnterpriseName = (TextView) convertView.findViewById(R.id.tv_enterprise_name);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.tv_albnumber.setText(++position + "");
        viewHolder.txtNumber.setText((position + 1) + "");
        Glide.with(context).load(Util.getImgUrl(list.get(position).getCoverimgurl())).into(viewHolder.imgAlbcharts);
        switch (list.get(position).getAlbumtype()) {
            case 1:
                viewHolder.tuwenbiao.setImageResource(R.drawable.icon_rec_tuwen);
                break;
            case 2:
                viewHolder.tuwenbiao.setImageResource(R.drawable.icon_rec_audio);
                break;
            case 3:
                viewHolder.tuwenbiao.setImageResource(R.drawable.icon_rec_video);
                break;
        }
        viewHolder.subTitle.setText(list.get(position).getAlbumname());
        viewHolder.tvRecentContent.setText(list.get(position).getRecenttitle());
        if (list.get(position).getAlbumauthor() == null) {
            viewHolder.tvEnterpriseName.setText("");
        } else {
            viewHolder.tvEnterpriseName.setText(list.get(position).getAlbumauthor());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("albumId", list.get(position).getAlbumid());
                intent.putExtra("albumName", list.get(position).getAlbumname());
                intent.putExtra("albumAvatar", list.get(position).getCoverimgurl());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView txtNumber;
        private ImageView imgAlbcharts;
        private ImageView tuwenbiao;
        private TextView subTitle;
        private TextView tvRecentContent;
        private TextView tvTime;
        private TextView tvEnterpriseName;
    }
}
