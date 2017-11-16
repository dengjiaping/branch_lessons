package com.yidiankeyan.science.functionkey.adapter;

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
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessContentBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nby on 2016/8/4.
 */
public class SearchContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<BusinessContentBean> list;
    private LayoutInflater mInflater;

    public SearchContentAdapter(Context mContext, List<BusinessContentBean> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_search_content, parent, false);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatars);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatars);
            holder.tvClickCount = (TextView) convertView.findViewById(R.id.search_number);
            holder.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_number);
            holder.tvUpTime = (TextView) convertView.findViewById(R.id.tv_data_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BusinessContentBean allBean = list.get(position);
        holder.tvName.setText(allBean.getName());
        holder.tvReadCount.setText(allBean.getCommentnum() + "");
        holder.tvClickCount.setText(allBean.getReadnum() + "");
        holder.tvUpTime.setText(TimeUtils.questionCreateDuration(allBean.getCreatetime()));

        Glide.with(mContext).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(holder.imgAvatar);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (allBean.getType() == 1) {
                    intent = new Intent(mContext, ImgTxtAlbumActivity.class);
                    intent.putExtra("id", allBean.getId());
                    mContext.startActivity(intent);
                } else if (allBean.getType() == 2) {
                    ArrayList<AlbumContent> listItem = new ArrayList<>();
                    AlbumContent albumContent = new AlbumContent(allBean.getName());
                    albumContent.setArticleid(allBean.getId());
                    albumContent.setMediaurl(allBean.getMediaurl());
                    listItem.add(albumContent);
                    intent = new Intent(mContext, AudioAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItem);
                    intent.putExtra("id", listItem.get(0).getArticleid());
                    intent.putExtra("position", 0);
                    intent.putExtra("single", true);
                    mContext.startActivity(intent);
                } else if (allBean.getType() == 3) {
                    ArrayList<AlbumContent> listItem = new ArrayList<>();
                    AlbumContent albumContent = new AlbumContent(allBean.getName());
                    albumContent.setArticleid(allBean.getId());
                    albumContent.setMediaurl(allBean.getMediaurl());
                    listItem.add(albumContent);
                    intent = new Intent(mContext, VideoAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItem);
                    intent.putExtra("id", listItem.get(0).getArticleid());
                    intent.putExtra("position", 0);
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvReadCount;
        private TextView tvClickCount;
        private TextView tvUpTime;
    }
}
