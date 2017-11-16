package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/9/14 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                已购
 * //       █▓▓▓▓██◤                        -热门推荐Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class PurchaseHotRecAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SubscribeColumnBean.ListBean> mColumnList;

    public PurchaseHotRecAdapter(List<SubscribeColumnBean.ListBean> mColumnList, Context mContext) {
        super();
        this.mContext = mContext;
        this.mColumnList = mColumnList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mColumnList != null) {
            return mColumnList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mColumnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_puchase_hot_rec, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (holder == null)
            return convertView;

        Glide.with(mContext).load(Util.getImgUrl(mColumnList.get(position).getColumnPicture()))
                .placeholder(R.drawable.icon_readload_failed)
                .error(R.drawable.icon_readload_failed).into(holder.imgAvatar);
        holder.tvName.setText(mColumnList.get(position).getColumnName());

        return convertView;
    }

}


class ViewHolder {
    ImageView imgAvatar;
    TextView tvName;

    public ViewHolder(View convertView) {
        imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
    }
}
