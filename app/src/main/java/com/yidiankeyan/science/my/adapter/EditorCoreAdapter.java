package com.yidiankeyan.science.my.adapter;

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
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.my.entity.EditorTypeBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.NoScrollGridView;

import java.util.List;

/**
 * Created by zn on 2016/9/24.
 */
public class EditorCoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<EditorTypeBean> mEditorList;
    private LayoutInflater mInflater;

    public EditorCoreAdapter(Context mContext, List<EditorTypeBean> mEditorList) {
        this.mContext = mContext;
        this.mEditorList = mEditorList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mEditorList.size();
    }

    @Override
    public EditorTypeBean getItem(int position) {
        return mEditorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_editor_core, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.imgClickType = (ImageView) convertView.findViewById(R.id.img_click_type);
            holder.listviewItemGridview = (NoScrollGridView) convertView.findViewById(R.id.listview_item_gridview);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvTitle.setText(mEditorList.get(position).getTitle());
        holder.listviewItemGridview.setAdapter(new GridViewAdapter(mEditorList.get(position).getmDatas()));
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mEditorList.get(position).getTitle().equals("音频专辑")) {
//                    mContext.startActivity(new Intent(mContext, ClassAudioActivity.class));
//                } else if (mEditorList.get(position).getTitle().equals("视频专辑")) {
//                    mContext.startActivity(new Intent(mContext, ClassVideoActivity.class));
//                } else {
//                    mContext.startActivity(new Intent(mContext, ClassImaTxtActivity.class));
//                }
//            }
//        });
        return convertView;
    }

    class GridViewAdapter extends BaseAdapter {

        private List<EditorAlbum> mDatas;

        public GridViewAdapter(List<EditorAlbum> mDatas) {
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
                convertView = mInflater.inflate(R.layout.gridview_item, null, false);
                holder.button = (ImageView) convertView.findViewById(R.id.gridview_item_button);
                holder.tvImgDes = (TextView) convertView.findViewById(R.id.tv_imgdes);
                holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
                holder.imgAuthor = (ImageView) convertView.findViewById(R.id.img_author);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl()))
                    .placeholder(R.drawable.icon_hotload_failed)
                    .error(R.drawable.icon_hotload_failed)
                    .into(holder.button);
            holder.tvImgDes.setText(mDatas.get(position).getRecenttitle());
            holder.tv_author.setText(mDatas.get(position).getAlbumname());
            final int type = mDatas.get(position).getAlbumtype();
            if (holder.button != null) {
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (type) {
                            case 1:
                            case 2:
//                            mContext.startActivity(new Intent(mContext, AlbumDetailsActivity.class));
//                            break;
                            case 3:
                                //跳转到专辑详情
                                Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                                intent.putExtra("albumName", mDatas.get(position).getAlbumname());
                                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                });
            }

            switch (type) {
                case 1:
                    holder.imgAuthor.setImageResource(R.drawable.icon_rec_tuwen);
                    break;
                case 2:
                    holder.imgAuthor.setImageResource(R.drawable.icon_rec_audio);
                    break;
                case 3:
                    holder.imgAuthor.setImageResource(R.drawable.icon_rec_video);
                    break;
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView button;
            TextView tvImgDes;
            private TextView tv_author;
            private ImageView imgAuthor;
        }
    }

    class ViewHolder {
        private TextView tvTitle;
        private ImageView imgClickType;
        private NoScrollGridView listviewItemGridview;
    }

}
