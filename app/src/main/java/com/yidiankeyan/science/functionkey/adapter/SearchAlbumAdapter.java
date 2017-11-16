package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/1 0001.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class SearchAlbumAdapter extends BaseAdapter {
    private List<BusinessAllBean> list;
    private Context context;

    public SearchAlbumAdapter(Context context, List<BusinessAllBean> list) {
        this.context = context;
        this.list = list;

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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wholealbum, parent, false);
            viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.whole_title);
            viewHolder.txtContentId = (TextView) convertView.findViewById(R.id.whoid);
            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_whocontent);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_whoname);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txt_number);
            viewHolder.txtTou = (TextView) convertView.findViewById(R.id.txt_whotou);
            viewHolder.txtUpTime = (TextView) convertView.findViewById(R.id.txt_whoupdate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BusinessAllBean allBean = list.get(position);

        if (!TextUtils.isEmpty(allBean.getAlbumname()) && !TextUtils.equals("null", allBean.getAlbumname())) {
            viewHolder.txtTitle.setVisibility(View.VISIBLE);
            viewHolder.txtTitle.setText(allBean.getAlbumname());
        } else {
            viewHolder.txtTitle.setVisibility(View.GONE);
        }
        viewHolder.txtTitle.setText(allBean.getAlbumname());
        viewHolder.txtContent.setText(allBean.getRecenttitle());
        viewHolder.txtName.setText(allBean.getAlbumauthor());
        viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
        viewHolder.txtTou.setText("浏览量" + allBean.getReadnum());
        viewHolder.txtUpTime.setText(TimeUtils.questionCreateDuration(allBean.getRecentupdatetime()));
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);

        return convertView;
    }

    class ViewHolder {
        ImageView imgAdd;
        TextView txtTitle;
        TextView txtContentId;
        TextView txtContent;
        TextView txtName;
        TextView txtNumber;
        TextView txtTou;
        TextView txtUpTime;
    }

}
