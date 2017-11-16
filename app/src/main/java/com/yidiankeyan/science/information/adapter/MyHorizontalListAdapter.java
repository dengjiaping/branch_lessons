package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.RecMagazineList;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/3/22 0022.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤        推荐
 * //       █▓▓▓▓██◤                杂志列表
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MyHorizontalListAdapter extends RecyclerView.Adapter<MyHorizontalListAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;
    private LayoutInflater mInflater;
    private ArrayList<RecMagazineList> mList = new ArrayList<>();
    private Context mContext;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyHorizontalListAdapter(Context context, ArrayList<RecMagazineList> mList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gv_ranking,
                parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imgCoverUrl = (ImageView) view.findViewById(R.id.img_author);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        viewHolder.tvUpdateNum = (TextView) view.findViewById(R.id.tv_update_num);
        if (onItemClickListener != null) {
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    onItemClickListener.onItemClick(viewHolder.itemView, position);
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (mList.get(position).getShortimg() != null && mList.get(position).getShortimg().startsWith("/"))
            Glide.with(mContext).load(Util.getImgUrl(mList.get(position).getShortimg())).placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);
        else
            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + "/" + mList.get(position).getShortimg()).placeholder(R.drawable.icon_readload_failed)
                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);

        holder.tvName.setText(mList.get(position).getName());
        holder.tvDesc.setText(mList.get(position).getDesc());
        if (!TextUtils.equals("null", mList.get(position).getUpdatemonthname()) &&
                !TextUtils.isEmpty(mList.get(position).getUpdatemonthname())) {
            holder.tvUpdateNum.setText("更新：" + mList.get(position).getUpdatemonthname());
        } else {
            holder.tvUpdateNum.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView imgCoverUrl;
        TextView tvName;
        TextView tvDesc;
        TextView tvUpdateNum;

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
