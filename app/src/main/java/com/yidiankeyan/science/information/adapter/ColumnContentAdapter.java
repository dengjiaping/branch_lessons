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
import com.yidiankeyan.science.information.entity.FreeIssues;
import com.yidiankeyan.science.utils.TimeUtils;
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
 * Created by nby on 2016/11/29.
 * 作用：
 */
public class ColumnContentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FreeIssues> mData;

    public ColumnContentAdapter(Context context, List<FreeIssues> data) {
        this.mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData == null ? null : mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_column_content, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.imgAvatar.getLayoutParams();
        int height = (int) (Util.getScreenWidth(mContext) * 0.89 * 0.5625);
        params.height = height;
        holder.imgAvatar.setLayoutParams(params);
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimg())).into(holder.imgAvatar);
        holder.tvTitle.setText(mData.get(position).getVol() + "期内容");
        holder.tvContent.setText(mData.get(position).getTitle());
        holder.tvTime.setText(TimeUtils.questionCreateDuration(mData.get(position).getCreatetime()));
        holder.tvReadCount.setText(mData.get(position).getReadnum() + "人已读");
        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private ImageView imgAvatar;
        private TextView tvTime;
        private TextView tvReadCount;

        public ViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
        }
    }
}
