package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 科答
 * -问答专辑Adapter
 */
public class AnswerAlbumAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<AnswerAlbumBean> listHelp = new ArrayList<>();
    private Integer[] mAnswers =
            {
                    R.drawable.kd_academic_research, R.drawable.kd_preaching_explain, R.drawable.kd_teaching_education
                    , R.drawable.kd_science_industrialization, R.drawable.kd_hot_topic, R.drawable.kd_questions_and_answers
                    , R.drawable.kd_life_stories, R.drawable.kd_popular_science, R.drawable.kd_industry_dynamics
                    , R.drawable.kd_total_innovation, R.drawable.kd_widely_business, R.drawable.kd_business_management
                    , R.drawable.kd_career_advancement, R.drawable.kd_study_notes, R.drawable.kd_scientific_method
            };

    public AnswerAlbumAdapter(Context mContext, List<AnswerAlbumBean> listHelp) {
        this.mContext = mContext;
        this.listHelp = listHelp;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listHelp.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_answer_album, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_zavatars);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_ztitles);
            holder.tvUserNum = (TextView) convertView.findViewById(R.id.tv_user_num);
            holder.tvAnswerNum = (TextView) convertView.findViewById(R.id.tv_answer_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        AnswerAlbumBean albumBean = listHelp.get(position);
        holder.tvTitle.setText(albumBean.getKdname());
        holder.imgAvatar.setImageResource(mAnswers[position]);
        holder.tvUserNum.setText(albumBean.getKedausernum() + "位答人");
        holder.tvAnswerNum.setText(albumBean.getKedernum() + "条回答");
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvUserNum;
        private TextView tvAnswerNum;
//        private TextView tvTou;
    }
}
