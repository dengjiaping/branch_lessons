package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.subscribe.entity.CommentBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;


import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nby on 2016/7/30.
 * 内容下面评论的子评论列表适配器
 */
public class AlbumCommentListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<CommentBean> list;
    private Context context;
    private final SparseBooleanArray mCollapsedStatus;
    private Intent intent;
    private MyOnItemClickListener myOnItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    public AlbumCommentListAdapter(Context context, List<CommentBean> list) {
        this.context = context;
        this.list = list;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_album_comment, parent, false);
            viewHolder.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            viewHolder.imgComment = (ImageButton) convertView.findViewById(R.id.img_comment);
            viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
            viewHolder.tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            viewHolder.llClick = (AutoLinearLayout) convertView.findViewById(R.id.ll_click);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.expandable_text);
            viewHolder.llComment = (AutoLinearLayout) convertView.findViewById(R.id.ll_comment);
            viewHolder.tvDeleteComment = (TextView) convertView.findViewById(R.id.tv_delete_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置文本
//        ForegroundColorSpan blueSpan1 = new ForegroundColorSpan(
//                context.getResources().getColor(
//                        R.color.red));
//        ForegroundColorSpan blueSpan2 = new ForegroundColorSpan(
//                context.getResources().getColor(
//                        R.color.black));
//        String content = list.get(position).getContent();
//        SpannableStringBuilder builder = null;
//        builder = new SpannableStringBuilder(content);
//        builder.setSpan(blueSpan1, 0, 5,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//
//        builder.setSpan(blueSpan2, 5,
//                content.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        viewHolder.expandableTextView.setText(builder, mCollapsedStatus, position);
//                viewHolder.expandableTextView.judgmentTvExpandShow();
        viewHolder.position = position;
        Glide.with(context).load(Util.getImgUrl(list.get(position).getImgurl())).error(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(context)).into(viewHolder.imgAvatar);
        viewHolder.tvDate.setText(TimeUtils.formatDate2(list.get(position).getCreatetime()));
        if (list.get(position).getIsreply() == 1) {
            viewHolder.tvName.setText(list.get(position).getUsername() + "回复了" + list.get(position).getReplyname());
        } else {
            viewHolder.tvName.setText(list.get(position).getUsername());
        }
        viewHolder.llClick.setVisibility(View.VISIBLE);
        viewHolder.llComment.setVisibility(View.VISIBLE);
        viewHolder.tvClickCount.setText(list.get(position).getUps() + "");
        viewHolder.tvCommentCount.setText(list.get(position).getCommentnum() + "");
        viewHolder.tvContent.setText(list.get(position).getContent());
        viewHolder.imgClick.setTag(viewHolder);
        viewHolder.imgClick.setOnClickListener(this);
//        if (list.get(position).isClick()) {
//            //当前的状态为已点赞，所以图标变成已点赞
//            viewHolder.imgToday.setImageResource(R.drawable.icon_today_click_pressed);
//        } else {
//            viewHolder.imgToday.setImageResource(R.drawable.icon_today_click_normal);
//        }

        viewHolder.imgComment.setTag(position);
        viewHolder.imgComment.setOnClickListener(this);
        if (DB.getInstance(DemoApplication.applicationContext).isClick(list.get(position).getCommentid())) {
            viewHolder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
        } else {
            viewHolder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myOnItemClickListener != null)
//                    myOnItemClickListener.onItemClick(position);
//            }
//        });
        if (TextUtils.equals(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"), list.get(position).getUserid())) {
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
        int position;
        switch (v.getId()) {
            case R.id.img_click:
                ViewHolder holder = (ViewHolder) v.getTag();
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_CHANGE);
                msg.setBody(list.get(holder.position).getCommentid());
                if (DB.getInstance(DemoApplication.applicationContext).isClick(list.get(holder.position).getCommentid())) {
//                    ((ImageView) v).setImageResource(R.drawable.icon_today_click_normal);
//                    list.get(holder.position).setUps(list.get(holder.position).getUps() - 1);
//                    msg.setArg1(0);
//                    EventBus.getDefault().post(msg);
                    Toast.makeText(context, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    ((ImageView) v).setImageResource(R.drawable.icon_today_click_pressed);
                    list.get(holder.position).setUps(list.get(holder.position).getUps() + 1);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                }
                notifyDataSetChanged();
                holder.tvCommentCount.setText(list.get(holder.position).getUps() + "");
                break;
            case R.id.img_comment:
//                position = (int) v.getTag();
//                intent = new Intent(context, CommentDetailsActivity.class);
//                intent.putExtra("bean", list.get(position));
//                context.startActivity(intent);
                break;
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder {
        public int position;
        private ImageView imgClick;
        private ImageButton imgComment;
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvClickCount;
        private TextView tvCommentCount;
        private AutoLinearLayout llClick;
        private AutoLinearLayout llComment;
        private TextView tvContent;
        private TextView tvDeleteComment;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
