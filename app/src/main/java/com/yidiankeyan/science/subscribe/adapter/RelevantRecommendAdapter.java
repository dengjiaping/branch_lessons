package com.yidiankeyan.science.subscribe.adapter;

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
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.RelevantRecommend;
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
 * Created by nby on 2016/9/25.
 * 作用：
 */
public class RelevantRecommendAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RelevantRecommend> mDatas;

    public RelevantRecommendAdapter(Context mContext, List<RelevantRecommend> datas) {
        this.mContext = mContext;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.gridview_item, null, false);
            holder.button = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            holder.tvImgDes = (TextView) convertView.findViewById(R.id.tv_imgdes);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            holder.imgAuthor = (ImageView) convertView.findViewById(R.id.img_author);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(holder.button);
        holder.tvImgDes.setText(mDatas.get(position).getLastupdatetitle());
        holder.tv_author.setText(mDatas.get(position).getAlbumname());
        final String type = mDatas.get(position).getAlbumtype();
        if (holder.button != null) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type) {
                        case "1":
                        case "2":
//                            mContext.startActivity(new Intent(mContext, AlbumDetailsActivity.class));
//                            break;
                        case "3":
                            //跳转到专辑详情
                            Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                            intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                            intent.putExtra("albumName", mDatas.get(position).getAlbumname());
                            intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                            mContext.startActivity(intent);
                            break;
                    }
                }
            });
        }

        switch (type) {
            case "1":
                holder.imgAuthor.setImageResource(R.drawable.icon_rec_tuwen);
                break;
            case "2":
                holder.imgAuthor.setImageResource(R.drawable.icon_rec_audio);
                break;
            case "3":
                holder.imgAuthor.setImageResource(R.drawable.icon_rec_video);
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView button;
        TextView tvImgDes;
        private TextView tv_author;
        private ImageView imgAuthor;
    }
}
