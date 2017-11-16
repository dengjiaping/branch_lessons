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
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;

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
 * Created by nby on 2016/12/9.
 * 作用：专辑下载adapter
 */

public class DownloadFileContentAdapter extends BaseAdapter {

    private ArrayList<AlbumContent> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public DownloadFileContentAdapter(ArrayList<AlbumContent> data, Context context) {
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
        return mData == null ? null : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_download_file, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl())).into(holder.imgAvatar);
        holder.tvTitle.setText(mData.get(position).getArticlename());
        if (mData.get(position).getArticletype() == 2) {
            //音频
            if (mData.get(position).getAlreadyWatch() == 0) {
                holder.tvAlreadyWatch.setText("未收听");
            } else {
                holder.tvAlreadyWatch.setText("已收听");
            }
        } else if (mData.get(position).getArticletype() == 3) {
            //视频
            if (mData.get(position).getAlreadyWatch() == 0) {
                holder.tvAlreadyWatch.setText("未观看");
            } else {
                holder.tvAlreadyWatch.setText("已观看");
            }
        }
        holder.tvFileSize.setText(mData.get(position).getFileSize());
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTitle;
        private TextView tvAlreadyWatch;
        private TextView tvFileSize;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAlreadyWatch = (TextView) convertView.findViewById(R.id.tv_already_watch);
            tvFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);
        }
    }
}
