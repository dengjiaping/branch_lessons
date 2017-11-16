package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.CardMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zn on 2016-12-23.
 */
public class CardAdapter extends BaseAdapter {
    private Context mContext;
    private List<CardMode> mCardList = new ArrayList<>();

    public CardAdapter(Context mContext, List<CardMode> mCardList) {
        this.mContext = mContext;
        this.mCardList = mCardList;
    }

    @Override
    public int getCount() {
        return mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_card, parent, false);
            holder = new ViewHolder();
            holder.mCardImageView = (ImageView) convertView.findViewById(R.id.img_card_bg);
            holder.mCardContent = (TextView) convertView.findViewById(R.id.card_content);
            holder.mCardTitle = (TextView) convertView.findViewById(R.id.tv_card_title);
            holder.mCardYear = (TextView) convertView.findViewById(R.id.tv_card_year);
            holder.tvCardTime = (TextView) convertView.findViewById(R.id.tv_card_time);
            holder.imgCardRight = (ImageView) convertView.findViewById(R.id.img_card_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mCardList.size() == 0) {
            holder.imgCardRight.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(mCardList.get(position).getImages().get(0))
                .into(holder.mCardImageView);
        holder.mCardContent.setText(mCardList.get(position).getName());
//        if (mCardList.get(position).getImgurl() != null && mCardList.get(position).getImgurl().startsWith("/"))
//            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + mCardList.get(position).getImgurl()).placeholder(R.drawable.bg_navy)
//                    .error(R.drawable.bg_navy).into(holder.mCardImageView);
//        else
//            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + "/" + mCardList.get(position).getImgurl()).placeholder(R.drawable.bg_navy)
//                    .error(R.drawable.bg_navy).into(holder.mCardImageView);
//
//        holder.mCardTitle.setText(mCardList.get(position).getName());
//        holder.mCardContent.setText(mCardList.get(position).getContent());
//        holder.tvCardTime.setText(TimeUtils.formatWeek(mCardList.get(position).getPublishtime()));
//        holder.mCardYear.setText(TimeUtils.formatDate(mCardList.get(position).getPublishtime()));
        return convertView;
    }

    public class ViewHolder {
        public TextView mCardTitle;
        public TextView mCardContent;
        public TextView mCardYear;
        public TextView tvCardTime;
        public ImageView mCardImageView;
        public int position;
        public ImageView imgCardRight;
    }
}
