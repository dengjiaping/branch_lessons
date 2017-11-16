package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/3/22 0022.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤        知识
 * //       █▓▓▓▓██◤                推荐关注
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecommendFollowAdapter extends RecyclerView.Adapter<RecommendFollowAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RecommendFollowBean> mData;
    private LayoutInflater mInflater;
    private boolean isFollow;
//    /**
//     * ItemClick的回调接口
//     *
//     * @author zhy
//     */
//    public interface OnItemClickLitener {
//        void onItemClick(View view, int position);
//    }
//
//    private OnItemClickLitener mOnItemClickLitener;
//
//    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
//        this.mOnItemClickLitener = mOnItemClickLitener;
//    }


    public RecommendFollowAdapter(Context context, ArrayList<RecommendFollowBean> mData) {
        this.context = context;
        this.mData = mData;
        mInflater = LayoutInflater.from(context);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        private TextView tvFollow;
        private TextView tvName;

        ImageView imgCoverUrl;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.item_rec_follow,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.imgCoverUrl = (ImageView) view
                .findViewById(R.id.img_author);
        viewHolder.tvFollow = (TextView) view
                .findViewById(R.id.tv_follow);
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
        if (mData.get(position).getIsFocus() == 1) {
            holder.tvFollow.setBackgroundResource(R.drawable.shape_rec_follow);
            holder.tvFollow.setText("已关注");
            holder.tvFollow.setTextColor(Color.parseColor("#333333"));
            holder.tvFollow.setEnabled(false);
            isFollow = true;
        } else {
            holder.tvFollow.setBackgroundResource(R.drawable.shape_download_state_black);
            holder.tvFollow.setText("关注+");
            holder.tvFollow.setTextColor(Color.parseColor("#ffffff"));
            holder.tvFollow.setEnabled(true);
            isFollow = false;
        }
        holder.tvName.setText(mData.get(position).getName());
//        //如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null) {
//            holder.tvFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickLitener.onItemClick(holder.itemView, position);
//                }
//            });
//
//        }
        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (!Util.hintLogin((BaseActivity) context)) {
                                                       return;
                                                   }
                                                   if (isFollow) {
                                                       holder.tvFollow.setEnabled(true);
                                                       isFollow = false;
                                                   } else {
                                                       Map<String, Object> map = new HashMap<>();
                                                       map.put("targetid", mData.get(position).getId());
                                                       map.put("type", 1);
                                                       ApiServerManager.getInstance().getApiServer().focusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
                                                           @Override
                                                           public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                                               if (response.body().getCode() == 200) {
                                                                   holder.tvFollow.setBackgroundResource(R.drawable.shape_rec_follow);
                                                                   holder.tvFollow.setText("已关注");
                                                                   holder.tvFollow.setTextColor(Color.parseColor("#333333"));
                                                                   holder.tvFollow.setEnabled(false);
                                                                   isFollow = true;
                                                               }
                                                           }

                                                           @Override
                                                           public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                                                           }
                                                       });
                                                   }
                                               }
                                           }

        );
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
