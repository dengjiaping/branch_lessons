package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AlbumContent;

import java.util.List;


/**
 * 多选列表adapter
 */
public class CheckAdapter extends BaseAdapter {

    private Context context = null;
    private List<AlbumContent> list = null;

    public CheckAdapter(Context context, List<AlbumContent> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            holder.tvFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.cb.setTag(position);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int i = (int) buttonView.getTag();
                list.get(i).setChecked(isChecked);
            }
        });
        holder.tv.setText(list.get(position).getArticlename());
        if (list.get(position).isChecked()) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }
        return convertView;
    }

    /**
     * 操作CheckBox多选框
     */
    public class ViewHolder {
        public CheckBox cb;
        public TextView tv;
        private TextView tvFileSize;
    }

}


