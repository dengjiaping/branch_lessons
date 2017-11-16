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
import com.yidiankeyan.science.information.acitivity.MagazineDetailsActivity;
import com.yidiankeyan.science.information.entity.MozMagazineAllBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;

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
 * //       █▓▓▓▓██◤                        -所有杂志Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MagazineAllAdapter extends BaseAdapter {

    private Context mContext;
    private List<MozMagazineAllBean> mList;

    public MagazineAllAdapter(List<MozMagazineAllBean> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_magazine_all, null, false);
            holder.imgCoverUrl = (ImageView) convertView.findViewById(R.id.img_author);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.interPreter = (TextView) convertView.findViewById(R.id.tv_interpreter);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DecimalFormat df = new DecimalFormat("0.00");
        if (mList.get(position).getMonthlyDB().getCoverimg() != null && mList.get(position).getMonthlyDB().getCoverimg().startsWith("/"))
            Glide.with(mContext).load(Util.getImgUrl(mList.get(position).getMonthlyDB().getCoverimg())).placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);
        else
            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + "/" + mList.get(position).getMonthlyDB().getCoverimg()).placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);

        holder.tvName.setText(mList.get(position).getMonthlyDB().getName());
        holder.interPreter.setText(mList.get(position).getMonthlyDB().getAuthor());
        holder.tvDesc.setText(mList.get(position).getMonthlyDB().getDesc());
        if (mList.get(position).getPermission() == 1) {
            holder.tvPrice.setText("已购");
            holder.tvPrice.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.tvPrice.setText("¥" + df.format(mList.get(position).getMonthlyDB().getPrice()) + "/本");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MagazineDetailsActivity.class);
                intent.putExtra("id", mList.get(position).getMonthlyDB().getId());
                intent.putExtra("name", mList.get(position).getMonthlyDB().getName());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        ImageView imgCoverUrl;
        TextView tvName;
        TextView interPreter;
        TextView tvPrice;
        TextView tvDesc;
    }

}
