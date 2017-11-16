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
import com.yidiankeyan.science.information.entity.EditorRecommentBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * Created by nby on 2016/8/26.
 * 总编推荐适配器
 */
public class EditorRecommentAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<EditorRecommentBean> mDatas;

    public EditorRecommentAdapter(Context mContext, List<EditorRecommentBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public EditorRecommentBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_wholealbum, parent, false);
            holder = new ViewHolder();
            holder.imgAdd = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.whole_title);
            holder.txtContentId = (TextView) convertView.findViewById(R.id.whoid);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txt_whocontent);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_whoname);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_number);
            holder.txtTou = (TextView) convertView.findViewById(R.id.txt_whotou);
            holder.txtUpTime = (TextView) convertView.findViewById(R.id.txt_whoupdate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle.setText(mDatas.get(position).getName());
        holder.txtContent.setText(mDatas.get(position).getLastupdatetitle());
        holder.txtName.setText(mDatas.get(position).getUsername());
        holder.txtNumber.setText("内容量" + mDatas.get(position).getContentnum() + "篇");
        holder.txtTou.setText("浏览量" + mDatas.get(position).getReadnum());
        holder.txtUpTime.setText(TimeUtils.questionCreateDuration(mDatas.get(position).getLastupdatetime()));
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).into(holder.imgAdd);
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
