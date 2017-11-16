package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.subscribe.entity.GratuityPersonInfo;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by nby on 2016/8/9.
 */
public class GratuityPeopleAdapter extends RecyclerView.Adapter<GratuityPeopleAdapter.MyViewHolder> {

    private List<GratuityPersonInfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public GratuityPeopleAdapter(Context context, List<GratuityPersonInfo> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gratuity_people, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Glide.with(mContext).load(mDatas.get(position)).into(holder.imgAvatar);
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getImgurl())).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        }
    }
}
