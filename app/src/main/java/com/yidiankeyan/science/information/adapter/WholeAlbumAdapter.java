package com.yidiankeyan.science.information.adapter;

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
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.List;


/**
 * -最新专辑
 */
public class WholeAlbumAdapter extends BaseAdapter {

    private List<BusinessAllBean> list;
    private Context context;

    public WholeAlbumAdapter(Context context, List<BusinessAllBean> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
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

        final BusinessAllBean allBean = list.get(position);


        if (TextUtils.isEmpty(allBean.getAlbumname())) {
            viewHolder.txtTitle.setText(allBean.getTitle());
            viewHolder.imgAdd.setImageResource(allBean.getImgUrl());
            viewHolder.txtContent.setText(allBean.getContentId() + allBean.getContent());
            if (!TextUtils.isEmpty(allBean.getName()) & !TextUtils.equals("null", allBean.getName())) {
                viewHolder.txtName.setVisibility(View.VISIBLE);
                viewHolder.txtName.setText(allBean.getName());
            } else {
                viewHolder.txtName.setVisibility(View.GONE);
            }

            viewHolder.txtNumber.setText("内容量" + allBean.getContentNumber() + "篇");
            viewHolder.txtTou.setText("浏览量" + allBean.getToPeekNumber());
            viewHolder.txtUpTime.setText(allBean.getUpdateTime());
        } else {
            viewHolder.txtTitle.setText(allBean.getAlbumname());
            viewHolder.txtContent.setText(allBean.getRecenttitle());
            if (!TextUtils.isEmpty(allBean.getAlbumauthor()) & !TextUtils.equals("null", allBean.getAlbumauthor())) {
                viewHolder.txtName.setVisibility(View.VISIBLE);
                viewHolder.txtName.setText(allBean.getAlbumauthor());
            } else {
                viewHolder.txtName.setVisibility(View.GONE);
            }
            viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
            viewHolder.txtTou.setText("浏览量" + allBean.getReadnum());
            viewHolder.txtUpTime.setText(TimeUtils.formatDate(allBean.getRecentupdatetime()));
            Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(viewHolder.imgAdd);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("albumId", list.get(position).getAlbumid());
                intent.putExtra("albumName", list.get(position).getAlbumname());
                intent.putExtra("albumAvatar", list.get(position).getCoverimgurl());
                context.startActivity(intent);
            }
        });
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
