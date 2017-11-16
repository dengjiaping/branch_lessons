package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.entity.MozReadBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.GlideRoundTransform;

import java.text.DecimalFormat;
import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/28 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                推荐
 * //       █▓▓▓▓██◤                        -墨子读书Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecReadAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<MozReadBean.ListBean> mList;
    private Intent intent;

    public RecReadAdapter(List<MozReadBean.ListBean> mList, Context mContext) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_moz_read, null, false);
            holder.imgCoverUrl = (ImageView) convertView.findViewById(R.id.img_author);
            holder.imgReadHead = (ImageView) convertView.findViewById(R.id.img_read_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.txt_albumname);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.txt_editor);
            holder.tvActiviePrice = (TextView) convertView.findViewById(R.id.tv_active_price);
            holder.tvTimeLength = (TextView) convertView.findViewById(R.id.tv_time_length);
            holder.tvRowPrice = (TextView) convertView.findViewById(R.id.tv_row_price);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
//            holder.tvUnusablePrice = (TextView) convertView.findViewById(R.id.tv_unusable_price);
            holder.viewLine = (View) convertView.findViewById(R.id.view_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        Glide.with(mContext).load(Util.getImgUrl(mList.get(position).getCoverimgurl()))
                .transform(new GlideRoundTransform(mContext))
                .placeholder(R.drawable.icon_readload_failed)
                .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);
        holder.tvName.setText(mList.get(position).getName());
        holder.tvDesc.setText(mList.get(position).getDesc());
        holder.viewLine.setVisibility(View.GONE);
        if (!StringUtils.isEmpty(String.valueOf(mList.get(position).getMedialength()))) {
            holder.tvTimeLength.setText(TimeUtils.getMagazineLength(mList.get(position).getMedialength()));
        } else holder.tvTimeLength.setVisibility(View.INVISIBLE);
        DecimalFormat df = new DecimalFormat("0.00");
        if (mList.get(position).getIsbuy() == 1) {   //已购买
            holder.tvRowPrice.setVisibility(View.INVISIBLE);
            holder.tvActiviePrice.setText("已购");
        } else {   //未购买
            if (1 == mList.get(position).getIsActPriceShow()) {  //不是免费  有活动价
                holder.tvRowPrice.setVisibility(View.VISIBLE);
                holder.tvRowPrice.setText(df.format(mList.get(position).getPrice()) + " 墨子币");
                holder.tvRowPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvActiviePrice.setText(mList.get(position).getActivityprice() + " 墨子币");
            } else if ( 0 == mList.get(position).getIsActPriceShow()) {  //不是免费  无活动价
                holder.tvRowPrice.setVisibility(View.INVISIBLE);
                holder.tvActiviePrice.setText(mList.get(position).getPrice() + " 墨子币");
            } else if(mList.get(position).getPrice() == 0){ //免费
                holder.tvRowPrice.setVisibility(View.INVISIBLE);
                holder.tvActiviePrice.setVisibility(View.VISIBLE);
                holder.tvActiviePrice.setText("免费");
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, MozReadDetailsActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                intent.putExtra("name", mList.get(position).getName());
                intent.putExtra("isbuy", mList.get(position).getIsbuy() + "");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
    }

    private class ViewHolder {
        ImageView imgCoverUrl;
        ImageView imgReadHead;
        TextView tvName;
        TextView tvPrice;
        TextView tvActiviePrice;

        TextView tvTimeLength;
        TextView tvRowPrice;
        TextView tvDesc;
        int position;
        private TextView tvUnusablePrice;
        private View viewLine;
    }

}
