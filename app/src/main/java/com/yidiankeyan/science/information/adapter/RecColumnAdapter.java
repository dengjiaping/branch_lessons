package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.ColumnDetailActivity;
import com.yidiankeyan.science.information.acitivity.ColumnIntroductionActivity;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.GlideRoundTransform;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/28 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                推荐
 * //       █▓▓▓▓██◤                        -精品专栏Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecColumnAdapter extends BaseAdapter {

    private Context mContext;
    private Intent intent;
    private List<SubscribeColumnBean.ListBean> mColumnList;

    public RecColumnAdapter(List<SubscribeColumnBean.ListBean> mColumnList, Context mContext) {
        super();
        this.mContext = mContext;
        this.mColumnList = mColumnList;
    }

    @Override
    public int getCount() {
        if (mColumnList != null) {
            return mColumnList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mColumnList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_subscribe_column, null, false);
            holder.imgCoverUrl = (ImageView) convertView.findViewById(R.id.img_author);
            holder.imgReadHead = (ImageView) convertView.findViewById(R.id.img_read_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.txt_albumname);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.txt_editor);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvReadTime = (TextView) convertView.findViewById(R.id.tv_read_time);
            holder.tvColumnTitle = (TextView) convertView.findViewById(R.id.tv_column_title);
            holder.tvActivePrice = (TextView) convertView.findViewById(R.id.tv_active_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        Glide.with(mContext).load(Util.getImgUrl(mColumnList.get(position).
                getColumnPicture())).placeholder(R.drawable.icon_readload_failed)
                .transform(new GlideRoundTransform(mContext))
                .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);

        if (DemoApplication.mList.contains(mColumnList.get(position).getId())) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.menu));//
            holder.tvColumnTitle.setTextColor(mContext.getResources().getColor(R.color.menu));
        } else {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.black_33));
            holder.tvColumnTitle.setTextColor(mContext.getResources().getColor(R.color.black_33));
        }
        holder.tvName.setText(mColumnList.get(position).getColumnName());
        if (!StringUtils.isEmpty(mColumnList.get(position).getHaveYouPurchased()) &&
                !StringUtils.isEmpty(String.valueOf(mColumnList.get(position).getIshasactivityprice()))) {
            if ("1".equals(mColumnList.get(position).getHaveYouPurchased())) {
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvPrice.setVisibility(View.INVISIBLE);
                holder.tvActivePrice.setText("已购    ");
            } else if ("0".equals(mColumnList.get(position).getHaveYouPurchased()) &&
                    0 == mColumnList.get(position).getIshasactivityprice()) {   //未购买 没有活动价
                holder.tvPrice.setVisibility(View.INVISIBLE);
                holder.tvActivePrice.setVisibility(View.VISIBLE);
                holder.tvActivePrice.setText(mColumnList.get(position).getColumnPrice().replace(".00", "") +
                        mContext.getResources().getString(R.string.moz_money));
            } else if ("0".equals(mColumnList.get(position).getHaveYouPurchased()) &&
                    1 == mColumnList.get(position).getIshasactivityprice()) {   //未购买 有活动价
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvActivePrice.setVisibility(View.VISIBLE);
                holder.tvPrice.setText(mColumnList.get(position).getColumnPrice().replace(".00", "") + " 墨子币");
                holder.tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvActivePrice.setText(mColumnList.get(position).getColumnActivityPrice().replace(".00", "") +
                        mContext.getResources().getString(R.string.moz_money));
            }
        }
        holder.tvDesc.setText(mColumnList.get(position).getColumnWriterIntro());
        holder.tvReadTime.setText(mColumnList.get(position).getCreateTime() +
                mContext.getResources().getString(R.string.update));
        holder.tvColumnTitle.setText(mColumnList.get(position).getRankUpdate());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoApplication.mList.add(mColumnList.get(position).getId());
                if (!StringUtils.isEmpty(mColumnList.get(position).getHaveYouPurchased()) &&
                        "0".equals(mColumnList.get(position).getHaveYouPurchased())) {//getIntent().getStringExtra("name")
                    intent = new Intent(mContext, ColumnIntroductionActivity.class);
                    intent.putExtra("id", mColumnList.get(position).getId());
                    intent.putExtra("name", mColumnList.get(position).getColumnName());
                    intent.putExtra("columnWriterIntro", mColumnList.get(position).getColumnWriterIntro());
                    intent.putExtra("price", mColumnList.get(position).getColumnPrice().replace(".00", ""));
                    intent.putExtra("ishasactivityprice", mColumnList.get(position).getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", mColumnList.get(position).getColumnActivityPrice());
                    mContext.startActivity(intent);
                } else if (!StringUtils.isEmpty(mColumnList.get(position).getHaveYouPurchased()) &&
                        "1".equals(mColumnList.get(position).getHaveYouPurchased())) {
                    intent = new Intent(mContext, ColumnDetailActivity.class);
                    intent.putExtra("id", mColumnList.get(position).getId());
                    intent.putExtra("name", mColumnList.get(position).getColumnName());
                    intent.putExtra("columnWriterIntro", mColumnList.get(position).getColumnWriterIntro());
                    intent.putExtra("price", mColumnList.get(position).getColumnPrice().replace(".00", ""));
                    intent.putExtra("isBuys", mColumnList.get(position).getHaveYouPurchased());
                    intent.putExtra("ishasactivityprice", mColumnList.get(position).getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", mColumnList.get(position).getColumnActivityPrice());
                    mContext.startActivity(intent);
                }
            }
        });


        return convertView;
    }

    private class ViewHolder {
        ImageView imgCoverUrl;
        ImageView imgReadHead;
        TextView tvReadTime;
        TextView tvName;
        TextView tvPrice;
        TextView tvDesc;
        int position;
        TextView tvColumnTitle;
        TextView tvActivePrice;
    }

}
