package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.RankingAlbumBean;

import java.util.List;


/**
 * 推荐
 * -墨子读书
 *      -墨子读书列表
 */
public class MozReadItemAdapter extends BaseAdapter {
    private List<RankingAlbumBean> list;
    private Context context;

    public MozReadItemAdapter(List<RankingAlbumBean> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_subscribe_column, parent, false);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AlbumDetailsActivity.class);
//                intent.putExtra("albumId", list.get(position).getAlbumid());
//                intent.putExtra("albumName", list.get(position).getAlbumname());
//                intent.putExtra("albumAvatar", list.get(position).getCoverimgurl());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        private TextView txtNumber;
        private ImageView imgAlbcharts;
        private TextView subTitle;
        private TextView tvRecentContent;
        private TextView tvBelongSub;
        private TextView tvContentNum;
        private TextView tvReadNum;
        private TextView tvTime;
    }
}
