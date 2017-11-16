package com.yidiankeyan.science.purchase.adapter;

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
import com.yidiankeyan.science.information.acitivity.ColumnDetailsActivity;
import com.yidiankeyan.science.information.acitivity.PreviewColumnContentActivity;
import com.yidiankeyan.science.purchase.entity.RecentColumn;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
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
 * Created by nby on 2016/11/30.
 * 作用：
 */

public class RecentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RecentColumn> mLists = new ArrayList<>();

    public RecentAdapter(Context context, List<RecentColumn> mLists) {
        this.mContext = context;
        this.mLists = mLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_purchase_recent, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.imgAvatar.getLayoutParams();
        int height = (int) (Util.getScreenWidth(mContext) * 0.89 * 0.5625);
        params.height = height;
        final RecentColumn column = mLists.get(position);
        holder.tvTitle.setText(column.getS2CIssueModels() == null ? "" : column.getS2CIssueModels().get(0).getName());
        holder.tvContent.setText(column.getS2CIssueModels() == null ? "" : column.getS2CIssueModels().get(0).getTitle());
        holder.tvTime.setText(TimeUtils.questionCreateDuration(column.getCreatetime()));
        holder.tvReadCount.setText("浏览量" + (column.getS2CIssueModels() == null ? "" : column.getS2CIssueModels().get(0).getReadnum()));
        holder.tvAuthorName.setText(column.getUsername());
        holder.tvVol.setText(column.getS2CIssueModels() == null ? "" : column.getS2CIssueModels().get(0).getVol() + "");
        if (column.getS2CIssueModels() == null) {
            holder.imgAvatar.setImageResource(R.drawable.icon_banner_load);
        } else {
            Glide.with(mContext).load(Util.getImgUrl(column.getS2CIssueModels().get(0).getCoverimg()))
                    .error(R.drawable.icon_banner_load)
                    .placeholder(R.drawable.icon_banner_load)
                    .into(holder.imgAvatar);
        }
        holder.rlIntoColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ColumnDetailsActivity.class);
                intent.putExtra("id", column.getId());
                intent.putExtra("userId", column.getUserid());
                intent.putExtra("name", column.getName());
                mContext.startActivity(intent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (column.getS2CIssueModels() == null) {
                    Intent intent = new Intent(mContext, ColumnDetailsActivity.class);
                    intent.putExtra("id", column.getId());
                    intent.putExtra("userId", column.getUserid());
                    intent.putExtra("name", column.getName());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PreviewColumnContentActivity.class);
                    intent.putExtra("id", column.getS2CIssueModels().get(0).getId());
                    mContext.startActivity(intent);
                }
            }
        });
        holder.tvUpdateNum.setText(column.getUpdates() + "更新");
        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private ImageView imgAvatar;
        private TextView tvTime;
        private TextView tvReadCount;
        private TextView tvAuthorName;
        private TextView tvVol;
        private View rlIntoColumn;
        private TextView tvUpdateNum;

        public ViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_recent_title);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvVol = (TextView) convertView.findViewById(R.id.tv_vol);
            rlIntoColumn = convertView.findViewById(R.id.rl_into_column);
            tvUpdateNum = (TextView) convertView.findViewById(R.id.tv_update_num);
        }
    }

}
