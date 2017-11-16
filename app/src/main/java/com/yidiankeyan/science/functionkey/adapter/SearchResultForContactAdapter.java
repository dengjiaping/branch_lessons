package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.yidiankeyan.science.R;

import java.util.List;

/**
 * Created by nby on 2016/7/5.
 */
public class SearchResultForContactAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;

    public SearchResultForContactAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_search_result_for_contact, parent, false);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_contact_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_contact_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mDatas.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
    }
}
