package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.entity.BirthData;

import java.util.List;


/**
 * Created by zn on 2016/8/30.
 * 编辑资料弹窗列表
 */
public class PersonalDataListAdapter extends BaseAdapter {

    private List<BirthData> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public PersonalDataListAdapter(List<BirthData> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
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
            convertView = mInflater.inflate(R.layout.item_personal_data, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDatas.get(position).isTag() == true) {
            holder.tvName.setTextColor(Color.parseColor("#F1312E"));
        } else {
            holder.tvName.setTextColor(Color.parseColor("#000000"));
        }
        holder.tvName.setText(mDatas.get(position).getYear() + "");

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }

}
