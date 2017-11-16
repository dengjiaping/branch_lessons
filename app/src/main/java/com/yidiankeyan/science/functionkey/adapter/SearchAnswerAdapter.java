package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
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

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/7 0007.
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
public class SearchAnswerAdapter extends BaseAdapter {

    private Context mContext;
    private List<AnswerPeopleDetail> listHelp = new ArrayList<>();
    private LayoutInflater mInflater;

    public SearchAnswerAdapter(Context mContext, List<AnswerPeopleDetail> listHelp) {
        this.mContext = mContext;
        this.listHelp = listHelp;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listHelp == null ? 0 : listHelp.size();
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        AnswerPeopleDetail albumBean = listHelp.get(position);
        holder.tvTitle.setText(albumBean.getName());
        holder.tvContent.setText(albumBean.getProfession());
        Glide.with(mContext).load(Util.getImgUrl(albumBean.getCoverimg())).into(holder.imgAvatar);
        holder.tvDa.setText(albumBean.getAnswersnum() + "");
        if (albumBean.getIsFocus() == 0)
            holder.imgIsFocus.setVisibility(View.GONE);
        else
            holder.imgIsFocus.setVisibility(View.VISIBLE);
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDa;
        private TextView tvTou;
        private ImageView imgIsFocus;
    }

}
