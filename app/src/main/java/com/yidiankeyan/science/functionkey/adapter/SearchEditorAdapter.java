package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.functionkey.entity.SearchEditorBean;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

/**
 * Created by nby on 2016/8/4.
 * 主编搜索结果
 */
public class SearchEditorAdapter extends BaseAdapter {

    private List<SearchEditorBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public SearchEditorAdapter(List<SearchEditorBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_search_editor, parent, false);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.llInto = (AutoLinearLayout) convertView.findViewById(R.id.ll_into);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
            holder.tvFansCount = (TextView) convertView.findViewById(R.id.tv_fans_count);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SearchEditorBean albumBean = mData.get(position);
        holder.tvName.setText(albumBean.getName());
        holder.tvDes.setText(albumBean.getNick());
        Glide.with(mContext).load(Util.getImgUrl(albumBean.getImgurl())).into(holder.imgAvatar);
        holder.tvFansCount.setText(albumBean.getFansnum() + "");
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvDes;
        private TextView tvFansCount;
        private AutoLinearLayout llInto;
    }
}
