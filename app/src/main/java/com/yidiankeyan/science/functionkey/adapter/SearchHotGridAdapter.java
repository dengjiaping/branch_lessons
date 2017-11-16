package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yidiankeyan.science.R;

import java.util.List;

/**
 * Created by nby on 2016/7/5.
 * 搜索时热门搜索的GridView
 */
public class SearchHotGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public SearchHotGridAdapter(Context mContext, List<String> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_grid_hot, parent, false);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            holder.hotName = (TextView) convertView.findViewById(R.id.tv_hot_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNumber.setText(position + 1 + "  ");
        holder.hotName.setText(mDatas.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tvNumber;
        TextView hotName;
    }
}
