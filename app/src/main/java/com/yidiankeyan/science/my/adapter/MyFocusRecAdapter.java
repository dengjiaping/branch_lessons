package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

//推荐关注列表Adapter

public class MyFocusRecAdapter extends BaseAdapter {

    private List<RecommendFollowBean> magazineLists;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isFollow;

    public MyFocusRecAdapter(List<RecommendFollowBean> magazineLists, Context context) {
        this.magazineLists = magazineLists;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return magazineLists == null ? 0 : magazineLists.size();
    }

    @Override
    public Object getItem(int position) {
        return magazineLists == null ? null : magazineLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_rec_focus, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + "/" + magazineLists.get(position).getCoverimgurl()).placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.imgAvatar);
        holder.tvTitle.setText(magazineLists.get(position).getName());
        if (magazineLists.get(position).getIsFocus() == 1) {
            holder.tvFollow.setBackgroundResource(R.drawable.shape_download_state_white);
            holder.tvFollow.setText("查看");
            isFollow = true;
        } else {
            holder.tvFollow.setBackgroundResource(R.drawable.shape_download_state_black);
            holder.tvFollow.setText("关注+");
            isFollow = false;
        }
        if (TextUtils.equals("null", magazineLists.get(position).getDesc()) && TextUtils.isEmpty(magazineLists.get(position).getDesc())) {
            holder.tvContent.setText(magazineLists.get(position).getDesc());
        } else {
            holder.tvContent.setText("");
        }

        holder.llFocusAll.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (!Util.hintLogin((BaseActivity) mContext)) {
                                                       return;
                                                   }
                                                   if (magazineLists.get(position).getIsFocus() == 1) {
                                                       Intent intent = new Intent(mContext, MozDetailActivity.class);
                                                       intent.putExtra("id", magazineLists.get(position).getId());
                                                       intent.putExtra("type", 1);
                                                       mContext.startActivity(intent);
                                                   } else {
                                                       if (isFollow) {
                                                           holder.tvFollow.setEnabled(true);
                                                           Intent intent = new Intent(mContext, MozDetailActivity.class);
                                                           intent.putExtra("id", magazineLists.get(position).getId());
                                                           intent.putExtra("type", 1);
                                                           mContext.startActivity(intent);
                                                           isFollow = false;
                                                       } else {
                                                           Map<String, Object> map = new HashMap<>();
                                                           map.put("targetid", magazineLists.get(position).getId());
                                                           map.put("type", 1);
                                                           ApiServerManager.getInstance().getApiServer().focusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
                                                               @Override
                                                               public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                                                   if (response.body().getCode() == 200) {
                                                                       holder.tvFollow.setBackgroundResource(R.drawable.shape_radius_30_cacaca);
                                                                       holder.tvFollow.setText("查看");
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
                                           }

        );
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvFollow;
        private AutoLinearLayout llFocusAll;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            tvFollow = (TextView) convertView.findViewById(R.id.tv_follow);
            llFocusAll= (AutoLinearLayout) convertView.findViewById(R.id.ll_focus_all);
        }
    }
}
