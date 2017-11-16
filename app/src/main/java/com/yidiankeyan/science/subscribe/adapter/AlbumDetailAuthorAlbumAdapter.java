package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.subscribe.entity.AlbumDetailAuthorAlbum;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by nby on 2016/8/7.
 * 专辑详情-作者其他专辑
 */
public class AlbumDetailAuthorAlbumAdapter extends BaseAdapter {

    private Context mContext;
    private List<AlbumDetailAuthorAlbum> mData;
    private LayoutInflater mInflater;

    public AlbumDetailAuthorAlbumAdapter(Context mContext, List<AlbumDetailAuthorAlbum> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public AlbumDetailAuthorAlbum getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_album_detail_author_album, parent, false);
            viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.imgAlbumType = (ImageView) convertView.findViewById(R.id.img_album_type);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvAlbumDes = (TextView) convertView.findViewById(R.id.tv_album_des);
            viewHolder.imgGo = (ImageView) convertView.findViewById(R.id.img_go);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if (TextUtils.isEmpty(mData.get(position).getAuthorid())) {
//            viewHolder.tvName.setText(mData.get(position).getAlbumname());
//        } else {
        viewHolder.tvName.setText(mData.get(position).getAlbumname());
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAvatar);
        switch (mData.get(position).getAlbumtype()) {
            case 1:
                viewHolder.imgAlbumType.setImageResource(R.drawable.icon_rec_tuwen);
                break;
            case 2:
                viewHolder.imgAlbumType.setImageResource(R.drawable.icon_rec_audio);
                break;
            case 3:
                viewHolder.imgAlbumType.setImageResource(R.drawable.icon_rec_video);
                break;
        }
        viewHolder.tvAlbumDes.setText(mData.get(position).getRecenttitle());
//        }
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private ImageView imgAlbumType;
        private TextView tvName;
        private TextView tvAlbumDes;
        private ImageView imgGo;
    }
}
