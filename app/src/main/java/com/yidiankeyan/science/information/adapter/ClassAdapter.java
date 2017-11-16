package com.yidiankeyan.science.information.adapter;

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
import com.yidiankeyan.science.information.entity.ClassAllBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * 分类Adapter
 */
public class ClassAdapter extends BaseAdapter {

    private List<ClassAllBean> list;
    private Context context;

    public ClassAdapter(Context context, List<ClassAllBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classall, parent, false);
            viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.cls_title);
            viewHolder.txtContentId = (TextView) convertView.findViewById(R.id.cls_whoid);
            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.cls_whocontent);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.cls_whoname);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.cls_number);
            viewHolder.txtTou = (TextView) convertView.findViewById(R.id.cls_whotou);
            viewHolder.txtUpTime = (TextView) convertView.findViewById(R.id.cls_whoupdate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ClassAllBean allBean = list.get(position);
        viewHolder.txtContent.setText(allBean.getRecenttitle());
        viewHolder.txtTitle.setText(allBean.getAlbumname());
        if (!TextUtils.isEmpty(allBean.getAlbumauthor()) && !TextUtils.equals("null", allBean.getAlbumauthor())) {
            viewHolder.txtName.setText(allBean.getAlbumauthor());
        } else
            viewHolder.txtName.setVisibility(View.GONE);
        viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);
        viewHolder.txtTou.setText("浏览量" + allBean.getReadnum());
        viewHolder.txtUpTime.setText(TimeUtils.questionCreateDuration(allBean.getRecentupdatetime()));
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
