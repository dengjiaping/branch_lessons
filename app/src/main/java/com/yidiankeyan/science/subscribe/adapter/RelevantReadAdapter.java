package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.subscribe.entity.RelatedArticleBean;
import com.yidiankeyan.science.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/19 0019.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆         文章详情
 * //     █▓▓▓██◆           -相关阅读adapter
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RelevantReadAdapter extends BaseAdapter {

    private List<RelatedArticleBean> articleBeen;
    private Context context;

    public RelevantReadAdapter(List<RelatedArticleBean> articleBeen, Context context) {
        this.context = context;
        this.articleBeen = articleBeen;
    }

    @Override
    public int getCount() {
        return articleBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return articleBeen.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_relevamt_text, parent, false);
            viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvReadTime = (TextView) convertView.findViewById(R.id.tv_read_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Glide.with(context).load(Util.getImgUrl(articleBeen.get(position).getCoverimgurl()))
                .error(R.drawable.icon_readload_failed)
                .placeholder(R.drawable.icon_readload_failed)
                .into(viewHolder.imgAvatar);
        viewHolder.tvAuthorName.setText(articleBeen.get(position).getMozname());
        viewHolder.tvTitle.setText(articleBeen.get(position).getName());
        viewHolder.tvReadTime.setText(getTime(articleBeen.get(position).getCreatetime()));
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvAuthorName;
        private TextView tvTitle;
        private TextView tvReadTime;
    }

    private String getTime(long millisecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millisecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0) {
            return new SimpleDateFormat("MM-dd").format(new Date(millisecond));
        }
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 0)
            return minute + "分钟前";
        long second = duration / 1000;
        if (second == 0)
            return "1秒前";
        else
            return second + "秒前";
    }
}
