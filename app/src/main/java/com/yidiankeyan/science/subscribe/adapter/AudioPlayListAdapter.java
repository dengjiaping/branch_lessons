package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.utils.AudioPlayManager;

import java.util.List;


/**
 * Created by nby on 2016/7/30.
 * 音频详情页播放列表
 */
public class AudioPlayListAdapter extends BaseAdapter {

    private List<AlbumContent> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public AudioPlayListAdapter(List<AlbumContent> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public AlbumContent getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_audio_play, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvName.setText(mDatas.get(position).getArticlename());
        if (mDatas.get(position).getArticleid().equals(AudioPlayManager.getInstance().getCurrId())) {
            holder.tvName.setTextColor(Color.parseColor("#F1312E"));
        } else {
            holder.tvName.setTextColor(Color.parseColor("#666666"));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
