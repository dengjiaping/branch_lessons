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
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.RecommendWebActivity;
import com.yidiankeyan.science.information.entity.MagazineBean;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by zn on 2016/8/3 0003.
 */
public class ScienceRecommendAdapter extends BaseAdapter {

    private List<MagazineBean> mDatas;
    private Context context;

    private boolean showUnderscores;

    public void setShowUnderscores(boolean showUnderscores) {
        this.showUnderscores = showUnderscores;
    }

    public ScienceRecommendAdapter(Context context, List<MagazineBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_item_child_three, parent, false);
            viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MagazineBean allBean = mDatas.get(position);
        viewHolder.tvName.setText(allBean.getName());
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimg())).into(viewHolder.imgAvatar);
        viewHolder.tvDes.setText(allBean.getIntroduction());
        if (showUnderscores) {
            viewHolder.tvDes.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            viewHolder.tvDes.getPaint().setAntiAlias(true);//抗锯齿
            viewHolder.tvDes.setTag(position);
            viewHolder.tvDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, RecommendWebActivity.class).putExtra("url", mDatas.get(position).getWebsiteurl()));
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvDes;
    }

}
