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
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.Util;

import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2017/7/19.
 * 作用：
 */

public class RikeCollectionAdapter extends BaseAdapter {

    private Context mContext;
    private List<OneDayArticles> mData;
    private LayoutInflater mInflater;

    public RikeCollectionAdapter(Context context, List<OneDayArticles> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_hot_news_type_audio_a, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgHotNewsAudio.setImageResource(R.drawable.jc_play_normal);
        holder.tvAuthorName.setText(mData.get(position).getContentauthor());
        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl()))
                .placeholder(R.drawable.icon_hotload_failed)
                .error(R.drawable.icon_hotload_failed).into(holder.imgAvatar);
        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvTime;
        private TextView tvCommentCount;
        private ImageView imgAvatar;
        private ImageView imgHotNewsAudio;
        private TextView tvAlbumName;

        public ViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            imgHotNewsAudio = (ImageView) convertView.findViewById(R.id.img_hot_news_audio);
            tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
        }
    }
}
