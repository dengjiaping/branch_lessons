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
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 我问Adapter
 */
public class AnswerTopAdapter extends BaseAdapter {

    private Context mContext;
    private List<AnswerPeopleDetail> listHelp = new ArrayList<>();
    private LayoutInflater mInflater;

    public AnswerTopAdapter(Context mContext, List<AnswerPeopleDetail> listHelp) {
        this.mContext = mContext;
        this.listHelp = listHelp;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listHelp.size();
    }

    @Override
    public AnswerPeopleDetail getItem(int position) {
        return listHelp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_answer_top, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_datitle);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_dajob);
            holder.tvDa = (TextView) convertView.findViewById(R.id.txt_dawendaliang);
            holder.tvTou = (TextView) convertView.findViewById(R.id.txt_daguanzhuliang);
            holder.imgIsFocus = (ImageView) convertView.findViewById(R.id.img_is_focus);
            holder.txtDatoutingliang = (TextView) convertView.findViewById(R.id.txt_datoutingliang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        AnswerPeopleDetail albumBean = listHelp.get(position);
//        if (TextUtils.isEmpty(albumBean.getId())) {
//            holder.tvTitle.setText(albumBean.getTitle());
//            holder.tvContent.setText(albumBean.getContent());
//            holder.imgAvatar.setImageResource(albumBean.getImgUrl());
//            holder.tvDa.setText(albumBean.getLikeNumber() + "");
//            holder.tvWen.setText(albumBean.getProblemAmount() + "");
//            holder.tvTou.setText(albumBean.getHearNumber());
//        } else {
        holder.tvTitle.setText(albumBean.getName());
        if (!TextUtils.isEmpty(albumBean.getProfession()) && !TextUtils.equals("null", albumBean.getProfession())) {
            holder.tvContent.setText(albumBean.getProfession());
        }
        Glide.with(mContext).load(Util.getImgUrl(albumBean.getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);
        holder.tvDa.setText(albumBean.getAnswersnum() + "");
        holder.tvTou.setText(albumBean.getFollowers() + "人已关注");
        holder.txtDatoutingliang.setText(albumBean.getEavesdropnum() + "人偷听");
        if (albumBean.getAuthenticated() == 2)
            holder.imgIsFocus.setVisibility(View.VISIBLE);
        else
            holder.imgIsFocus.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDa;
        private TextView tvTou;
        private ImageView imgIsFocus;
        private TextView txtDatoutingliang;
    }
}
