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
 * 搜索历史GridView
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private OnExpandCollapsedClickListener onExpandCollapsedClickListener;

    public SearchHistoryAdapter(Context mContext, List<String> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_grid_history, parent, false);
            holder = new ViewHolder();
            holder.hotName = (TextView) convertView.findViewById(R.id.tv_hot_name);
            holder.imgExpandable = (ImageView) convertView.findViewById(R.id.img_expandable);
            holder.imgExpandable.setTag(0);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.hotName.setText(mDatas.get(position));
        if (position == 1 && getCount() > 2) {
            holder.imgExpandable.setVisibility(View.VISIBLE);
            holder.imgExpandable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onExpandCollapsedClickListener != null) {
                        if ((int) v.getTag() == 0) {
                            v.setTag(1);
                            ((ImageView) v).setImageResource(R.drawable.xiala);
                        } else {
                            v.setTag(0);
                            ((ImageView) v).setImageResource(R.drawable.icon_shang);
                        }
                        onExpandCollapsedClickListener.onClick();
                    }
                }
            });
        } else {
            holder.imgExpandable.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setOnExpandCollapsedClickListener(OnExpandCollapsedClickListener onExpandCollapsedClickListener) {
        this.onExpandCollapsedClickListener = onExpandCollapsedClickListener;
    }

    class ViewHolder {
        TextView hotName;
        private ImageView imgExpandable;
    }

    public interface OnExpandCollapsedClickListener {
        void onClick();
    }
}
