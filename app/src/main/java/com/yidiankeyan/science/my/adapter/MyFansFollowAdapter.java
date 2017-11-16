package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.entity.FansFollow;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/14 0014.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤    主页  -粉丝关注Adapter
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MyFansFollowAdapter extends BaseAdapter {

    private List<FansFollow> mDatas;
    private Context context;

    public MyFansFollowAdapter(Context context, List<FansFollow> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_fans_follow, parent, false);
            viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.img_reward);
//            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_reward_title);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_reward_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final FansFollow fansFollow = mDatas.get(position);
        Glide.with(context).load(Util.getImgUrl(fansFollow.getImgurl()))
                .placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar)
                .into(viewHolder.imgAdd);
//        viewHolder.txtContent.setText(fansFollow.getMysign());
        viewHolder.txtName.setText(fansFollow.getUsername());
        return convertView;
    }

    class ViewHolder {
        ImageView imgAdd;
        TextView txtContent;
        TextView txtName;
    }
}
