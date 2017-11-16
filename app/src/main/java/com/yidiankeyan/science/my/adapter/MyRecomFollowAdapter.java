package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/3/22 0022.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤        我的关注
 * //       █▓▓▓▓██◤                推荐关注
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MyRecomFollowAdapter extends RecyclerView.Adapter<MyRecomFollowAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RecommendFollowBean> mData;
    private LayoutInflater mInflater;
    private boolean isFollow;


    public MyRecomFollowAdapter(Context context, ArrayList<RecommendFollowBean> mData) {
        this.context = context;
        this.mData = mData;
        mInflater = LayoutInflater.from(context);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

//        private TextView tvName;

        ImageView imgCoverUrl;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.item_my_rec_follow,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.imgCoverUrl = (ImageView) view
                .findViewById(R.id.img_author);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mData.get(position).getCoverimgurl() != null && mData.get(position).getCoverimgurl().startsWith("/"))
            Glide.with(context).load(Util.getImgUrl(mData.get(position).getCoverimgurl())).placeholder(R.drawable.icon_default_avatar)
                    .error(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(context)).into(holder.imgCoverUrl);
        else
            Glide.with(context).load(SystemConstant.ACCESS_IMG_HOST + "/" + mData.get(position).getCoverimgurl()).placeholder(R.drawable.icon_default_avatar)
                    .error(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(context)).into(holder.imgCoverUrl);
//        holder.tvName.setText(mData.get(position).getName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
