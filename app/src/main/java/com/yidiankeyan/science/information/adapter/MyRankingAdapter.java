package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.RankingCharts;

import java.util.List;


/**
 * 数据适配
 */
public class MyRankingAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<RankingCharts> mData;
    private Context mContext;
    private MyOnItemClickListener onItemClickListener;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;

    public MyRankingAdapter(List<RankingCharts> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(mData.get(position).getClassName()))
            return TYPE_CONTENT;
        else
            return TYPE_TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public MyOnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TitleViewHolder titleHolder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    convertView = inflater.inflate(R.layout.item_rank_title, parent, false);
                    titleHolder = new TitleViewHolder();
                    titleHolder.txtRantitle = (TextView) convertView.findViewById(R.id.txt_rantitle);
                    convertView.setTag(titleHolder);
                    break;
                case TYPE_CONTENT:
                    convertView = inflater.inflate(R.layout.ranking_items, parent, false);
                    holder = new ViewHolder();
                    holder.imgRanUrl= (ImageView) convertView.findViewById(R.id.img_ranking);
                    holder.txtRanone = (TextView) convertView.findViewById(R.id.txt_ranone);
                    holder.txtRantwo = (TextView) convertView.findViewById(R.id.txt_rantwo);
                    holder.txtRanthree = (TextView) convertView.findViewById(R.id.txt_ranthree);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    titleHolder = (TitleViewHolder) convertView.getTag();
                    break;
                case TYPE_CONTENT:
                    holder = (ViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                try {
                    titleHolder.txtRantitle.setText(mData.get(position).getClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_CONTENT:
                holder.txtRanone.setText(mData.get(position).getTitle());
                holder.imgRanUrl.setImageResource(mData.get(position).getImgUrl());
                if (!TextUtils.isEmpty(mData.get(position).getTitles()+"")&&!TextUtils.equals("null",mData.get(position).getTitles()+"")) {
                    if (mData.get(position).getTitles().size() == 1) {
                        holder.txtRantwo.setText("1. " + mData.get(position).getTitles().get(0));
                    } else if (mData.get(position).getTitles().size() == 2) {
                        holder.txtRantwo.setText("1. " + mData.get(position).getTitles().get(0));
                        holder.txtRanthree.setText("2. " + mData.get(position).getTitles().get(1));
                    }
                } else {
                    holder.txtRantwo.setText("");
                    holder.txtRanthree.setText("");
//                    holder.txtRantwo.setVisibility(View.GONE);
//                    holder.txtRanthree.setVisibility(View.GONE);
                }
//                final ViewHolder finalHolder = holder;
//                holder.llContent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onItemClickListener != null)
//                            onItemClickListener.onItemClick(finalHolder.llContent, position);
//                    }
//                });

                break;
        }

        return convertView;
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder {
        private TextView txtRanone;
        private TextView txtRantwo;
        private TextView txtRanthree;
        private ImageView imgRanUrl;
    }

    class TitleViewHolder {
        private TextView txtRantitle;
    }
}
