package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.knowledge.activity.TagDetailActivity;
import com.yidiankeyan.science.my.entity.FocusBean;
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
 * Created by nby on 2017/7/17.
 * 作用：
 */

public class FocusTagAdapter extends BaseAdapter {

    private List<FocusBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public FocusTagAdapter(List<FocusBean> data, Context context) {
        this.mData = data;
        this.mContext = context;
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
            convertView = mInflater.inflate(R.layout.item_focus, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mData.get(position).getType() == 1) {
            holder.tvTitle.setText(mData.get(position).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MozDetailActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
                }
            });
            Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl())).error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(holder.imgAvatar);
        } else {
            holder.tvTitle.setText("#" + mData.get(position).getName() + "#");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TagDetailActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    intent.putExtra("type", 2);
                    mContext.startActivity(intent);
                }
            });
            holder.imgAvatar.setImageResource(R.drawable.icon_focus_tag);
        }
        holder.tvContent.setText(mData.get(position).getLastupdate());
        if (mData.get(position).getUpdates() > 0) {
            holder.tvUpdateNum.setText("[" + mData.get(position).getUpdates() + "条]");
        } else {
            holder.tvUpdateNum.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvUpdateNum;
        private TextView tvContent;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvUpdateNum = (TextView) convertView.findViewById(R.id.tv_update_num);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
        }
    }
}
