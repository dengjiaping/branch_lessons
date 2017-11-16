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
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;

/**
 * Created by zn on 2017/3/22
 */
public class GridViewFMAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<EditorAlbum> mDatas;

    public GridViewFMAdapter(Context mContext, ArrayList<EditorAlbum> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.gridview_fm_item, null, false);
            holder.button = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            holder.tvImgDes = (TextView) convertView.findViewById(R.id.tv_imgdes);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_album_author);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).placeholder(R.drawable.icon_hotload_failed)
                .error(R.drawable.icon_hotload_failed).into(holder.button);
        holder.tvImgDes.setText(mDatas.get(position).getAlbumname());
//        final int type = mDatas.get(position).getAlbumtype();
//        if (holder.button != null) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AudioAlbumActivity.class);
//                ArrayList<AlbumContent> newList = new ArrayList<AlbumContent>();
//                for(RecentContentBean model:mDatas){
//                    AlbumContent content = new AlbumContent(model);
//                    newList.add(content);
//                }

                Intent intent = new Intent(mContext, AudioAlbumActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                mContext.startActivity(intent);

//                AlbumContent audio = new AlbumContent(null);
//                audio.setArticlename(mDatas.get(position).getAuthername());
//                audio.setArticleid(mDatas.get(position).getId());
//                audio.setLastupdatetime(mDatas.get(position).getCreatetime());
//                audio.setArticletype(2);
//                audio.setMediaurl(mDatas.get(position).getMediaurl());
//                audio.setCoverimgurl(mDatas.get(position).getCoverimgurl());
//                AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
//                if (content != null)
//                    audio.setFilePath(content.getFilePath());
//                ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
//                listItem.add(audio);
//                Intent intent = new Intent(mContext, AudioAlbumActivity.class);
//                intent.putParcelableArrayListExtra("list", listItem);
//                intent.putExtra("position", 0);
//                intent.putExtra("id", listItem.get(0).getArticleid());
//                intent.putExtra("single", true);
//                mContext.startActivity(intent);
            }
        });
//        }
        return convertView;
    }

    private class ViewHolder {
        ImageView button;
        TextView tvImgDes;
        private TextView tvAuthor;
    }
}
