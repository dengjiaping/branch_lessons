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
import com.yidiankeyan.science.information.entity.NewRecommendBean;
import com.yidiankeyan.science.subscribe.activity.TransitionActivity;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1 0001.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewRecommendBean.SimpleAlbumModlesBean> mDatas;

    public GridViewAdapter(Context mContext, List<NewRecommendBean.SimpleAlbumModlesBean> imgList) {
        super();
        this.mContext = mContext;
        mDatas = imgList;
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
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.gridview_main_item, null, false);
            holder.button = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            holder.tvImgDes = (TextView) convertView.findViewById(R.id.tv_imgdes);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            holder.imgAuthor = (ImageView) convertView.findViewById(R.id.img_author);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.button.setImageResource(mDatas.get(position).getImgRes());
//        if (TextUtils.isEmpty(mDatas.get(position).getCoverimgurl()) || "null".equals(mDatas.get(position).getCoverimgurl())) {
//            Glide.with(mContext).load(mDatas.get(position).getAvatar()).into(holder.button);
//        } else {
//            Glide.with(mContext).load(mDatas.get(position).getCoverimgurl()).into(holder.button);
//        }
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).placeholder(R.drawable.icon_hotload_failed)
                .error(R.drawable.icon_hotload_failed).into(holder.button);
        holder.tv_author.setText(mDatas.get(position).getName());
        holder.tvImgDes.setText(mDatas.get(position).getLastupdatetitle());
        final String type = mDatas.get(position).getAlbumtype();
        if (holder.button != null) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type) {
                        case "1":
                        case "2":
//                            mContext.startActivity(new Intent(mContext, AlbumDetailsActivity.class));
//                            break;
                        default:
                            //跳转到专辑详情
//                            Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                            Intent intent = new Intent(mContext, TransitionActivity.class);
                            intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                            intent.putExtra("albumName", mDatas.get(position).getName());
                            intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            mContext.startActivity(intent);
                            break;
                    }
                }
            });
        }

        switch (type) {
            case "1":
                holder.imgAuthor.setImageResource(0);
                break;
            case "2":
                holder.imgAuthor.setImageResource(R.drawable.animation_column_three);
                break;
            case "3":
                holder.imgAuthor.setImageResource(R.drawable.jc_play_normal);
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
