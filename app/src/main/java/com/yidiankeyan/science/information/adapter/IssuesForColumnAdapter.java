package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.IssuesForColumnBean;

import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Created by nby on 2016/12/1.
 * 作用：
 */

public class IssuesForColumnAdapter extends BaseAdapter {

    private Context mContext;
    private List<IssuesForColumnBean.S2CIssueModelsBean> mData;
    private LayoutInflater mInflater;

    public IssuesForColumnAdapter(Context context, List<IssuesForColumnBean.S2CIssueModelsBean> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_issues_for_column, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvTitle.setText("第" + mData.get(position).getVol() + "期 " + mData.get(position).getName());
        holder.tvReadNum.setText(mData.get(position).getReadnum() + "人已读");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        holder.tvDate.setText(simpleDateFormat.format(new Date(mData.get(position).getCreatetime())));
        if (mData.get(position).getEverRead() == 1) {
            holder.imgReadState.setImageResource(R.drawable.icon_readed);
            holder.tvTitle.setTextColor(Color.parseColor("#999999"));
        } else {
            holder.tvTitle.setTextColor(Color.parseColor("#333333"));
            if (mData.get(position).getUpdateitem() == 1) {
                holder.imgReadState.setImageResource(R.drawable.icon_no_read);
            } else {
                holder.imgReadState.setImageResource(R.drawable.icon_readed);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView imgReadState;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvReadNum;

        public ViewHolder(View convertView) {
            imgReadState = (ImageView) convertView.findViewById(R.id.img_read_state);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            tvReadNum = (TextView) convertView.findViewById(R.id.tv_read_num);
        }
    }
}
