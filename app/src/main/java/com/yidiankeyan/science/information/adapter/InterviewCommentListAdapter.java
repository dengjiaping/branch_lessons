package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.entity.InterviewCommentListBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/6/19 0014.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤    专访  -专访评论详情Adapter
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InterviewCommentListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<InterviewCommentListBean> mDatas;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;

    public InterviewCommentListAdapter(Context context, List<InterviewCommentListBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    @Override
    public int getCount() {
        return mDatas.size();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_list_interview, parent, false);
            viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.tv_father);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
//            viewHolder.tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            viewHolder.tvDeleteComment = (TextView) convertView.findViewById(R.id.tv_delete_comment);
//            viewHolder.llClick = (AutoLinearLayout) convertView.findViewById(R.id.ll_click);
//            viewHolder.tvClickCount= (TextView) convertView.findViewById(R.id.tv_click_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
//        viewHolder.llClick.setTag(viewHolder);
        viewHolder.tvDeleteComment.setTag(position);
//        viewHolder.llClick.setOnClickListener(this);
        viewHolder.tvDeleteComment.setOnClickListener(this);
        Glide.with(context).load(Util.getImgUrl(mDatas.get(position).getImgurl()))
                .placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar)
                .into(viewHolder.imgAvatar);
        viewHolder.tvDate.setText(TimeUtils.questionCreateDuration(mDatas.get(position).getCreatetime()));
//        viewHolder.tvClickCount.setText(mDatas.get(position).getUps() + "");
        viewHolder.txtContent.setText(mDatas.get(position).getContent());
        viewHolder.txtName.setText(mDatas.get(position).getUsername());
//        viewHolder.tvCommentCount.setText(mDatas.get(position).getCommentnum() + "");

        if (TextUtils.equals(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"), mDatas.get(position).getUserid())) {
            viewHolder.tvDeleteComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvDeleteComment.setVisibility(View.GONE);
        }

        viewHolder.tvDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null)
                    onDeleteClickListener.onDeleteClick(position);
            }
        });
        return convertView;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ll_click:
//                ViewHolder holder= (ViewHolder) v.getTag();
//                EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_CHANGE);
//                msg.setBody(mDatas.get(holder.position).getCommentid());
//                if (DB.getInstance(DemoApplication.applicationContext).isClick(mDatas.get(holder.position).getCommentid())) {
//                    Toast.makeText(context, "你已经点过了", Toast.LENGTH_SHORT).show();
//                } else {
//                    mDatas.get(holder.position).setUps(mDatas.get(holder.position).getUps() + 1);
//                    msg.setArg1(1);
//                    EventBus.getDefault().post(msg);
//                }
//                notifyDataSetChanged();
//                holder.tvCommentCount.setText(mDatas.get(holder.position).getUps() + "");
//                break;
//        }
    }


    class ViewHolder {
        int position;
        ImageView imgAvatar;
        TextView txtContent;
        TextView txtName;
        TextView tvDate;
        TextView tvCommentCount;
        TextView tvDeleteComment;
        AutoLinearLayout llClick;
        private TextView tvClickCount;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
