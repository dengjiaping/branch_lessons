package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
 * -Adapter
 */
public class AlbumChartsAdapter extends BaseAdapter {
    private List<RankingAlbumBean> list;
    private Context context;

    public AlbumChartsAdapter(List<RankingAlbumBean> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_albcharts, parent, false);
            viewHolder.imgAlbcharts = (ImageView) convertView.findViewById(R.id.img_albcharts);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.sub_title);
            viewHolder.tvRecentContent = (TextView) convertView.findViewById(R.id.tv_recent_content);
            viewHolder.tvReadNum = (TextView) convertView.findViewById(R.id.tv_read_num);
            viewHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(Util.getImgUrl(list.get(position).getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAlbcharts);
        switch (position) {
            case 0:
                viewHolder.tvNum.setText(1 + "");
                viewHolder.tvNum.setBackgroundColor(Color.parseColor("#F1312E"));
                break;
            case 1:
                viewHolder.tvNum.setText(2 + "");
                viewHolder.tvNum.setBackgroundColor(Color.parseColor("#F18E2E"));
                break;
            case 2:
                viewHolder.tvNum.setText(3 + "");
                viewHolder.tvNum.setBackgroundColor(Color.parseColor("#FCD000"));
                break;
            default:
                viewHolder.tvNum.setText(position + 1 + "");
                viewHolder.tvNum.setBackgroundColor(Color.parseColor("#A0A0A0"));
        }
        viewHolder.subTitle.setText(list.get(position).getAlbumname());
        viewHolder.tvRecentContent.setText(list.get(position).getRecenttitle());
        viewHolder.tvReadNum.setText("浏览量: " + list.get(position).getReadnum());
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
        private ImageView imgAlbcharts;
        private TextView subTitle;
        private TextView tvRecentContent;
        private TextView tvReadNum;
        private TextView tvNum;
    }
}
