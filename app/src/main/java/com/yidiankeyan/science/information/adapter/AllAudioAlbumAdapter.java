package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.AudioAlbumActivity;
import com.yidiankeyan.science.information.entity.AllAudioListBean;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/3/13 0013.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤         所有音频专辑
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class AllAudioAlbumAdapter extends BaseAdapter {

    private List<AllAudioListBean> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    private Intent intent;

    public AllAudioAlbumAdapter(Context mContext, List<AllAudioListBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public AllAudioListBean getItem(int position) {
        return mDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.item_article_core, null, false);
            holder.listviewItemGridview = (NoScrollGridView) convertView.findViewById(R.id.listview_item_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.listviewItemGridview.setAdapter(new GridViewAdapter(mDatas.get(position).getEditorAlba()));

        return convertView;
    }

    class GridViewAdapter extends BaseAdapter {

        private ArrayList<EditorAlbum> mDatas;

        public GridViewAdapter(ArrayList<EditorAlbum> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public EditorAlbum getItem(int position) {
            return mDatas.get(position);
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
                convertView = mInflater.inflate(R.layout.audio_gridview_item, null, false);
                holder.button = (ImageView) convertView.findViewById(R.id.gridview_item_button);
                holder.tvImgDes = (TextView) convertView.findViewById(R.id.tv_imgdes);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).placeholder(R.drawable.icon_hotload_failed)
                    .error(R.drawable.icon_hotload_failed).into(holder.button);
            holder.tvImgDes.setText(mDatas.get(position).getRecenttitle());
            final String type = mDatas.get(position).getAlbumtype() + "";
            if (holder.button != null) {
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AudioAlbumActivity.class);
                        intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                        mContext.startActivity(intent);
                    }
                });
            }
            return convertView;
        }


        class ViewHolder {
            ImageView button;
            TextView tvImgDes;
            private TextView tv_author;
            private ImageView imgAuthor;
        }
    }

    class ViewHolder {
        private NoScrollGridView listviewItemGridview;
    }
}
