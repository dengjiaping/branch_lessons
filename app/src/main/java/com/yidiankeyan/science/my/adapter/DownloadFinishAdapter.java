package com.yidiankeyan.science.my.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.RecColumnAllAdapter;
import com.yidiankeyan.science.my.entity.DownloadFinishBean;
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
 * Created by nby on 2017/7/18.
 * 作用：
 */

public class DownloadFinishAdapter extends BaseAdapter {

    private Context mContext;
    private List<DownloadFinishBean> mData;
    private LayoutInflater mInflater;//iv_remove

    private DownloadFinishAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DownloadFinishAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public DownloadFinishAdapter(Context context, List<DownloadFinishBean> data) {
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
            convertView = mInflater.inflate(R.layout.item_download_finish, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCover()))
                .error(R.drawable.icon_readload_failed)
                .placeholder(R.drawable.icon_readload_failed)
                .into(holder.imgAvatar);
        holder.tvName.setText(mData.get(position).getName());
        if (mData.get(position).getList() == null || mData.get(position).getList().size() == 0) {
            holder.tvCount.setVisibility(View.GONE);
        } else {
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText("已下载" + mData.get(position).getList().size() + "段");
        }
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvCount;
        private ImageView ivRemove;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            ivRemove = (ImageView) convertView.findViewById(R.id.iv_remove);
        }
    }
}
