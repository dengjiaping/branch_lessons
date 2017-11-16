package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.subscribe.activity.CommentDetailsActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;


import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
 * Created by nby on 2016/10/13.
 * 作用：内容评论
 */
public class ContentCommentAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ContentCommentBean> commentList;
    private Context mContext;
    private LayoutInflater mInflater;
    private Intent intent;
    private String content;
    private OnDeleteClickListener onDeleteClickListener;
    private OnSonDeleteClickListener onSonDeleteClickListener;

    public ContentCommentAdapter(List<ContentCommentBean> commentList, Context context) {
        this.commentList = commentList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentList == null ? 0 : commentList.size();
    }

    @Override
    public ContentCommentBean getItem(int position) {
        return commentList == null ? null : commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FatherViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_content_comment, parent, false);
            holder = new FatherViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.rlContainer = (AutoRelativeLayout) convertView.findViewById(R.id.rl_container);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.llClick = (AutoLinearLayout) convertView.findViewById(R.id.ll_click);
            holder.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            holder.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
            holder.llComment = (AutoLinearLayout) convertView.findViewById(R.id.ll_comment);
            holder.imgComment = (ImageButton) convertView.findViewById(R.id.img_comment);
            holder.tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            holder.tvFather = (TextView) convertView.findViewById(R.id.tv_father);
            holder.rlAll = (AutoRelativeLayout) convertView.findViewById(R.id.rl_all);
            holder.rlSonContainer = (AutoRelativeLayout) convertView.findViewById(R.id.rl_son_container);
            holder.lvSons = (ShowAllListView) convertView.findViewById(R.id.lv_sons);
            holder.tvMoreComment = (TextView) convertView.findViewById(R.id.tv_more_comment);
            holder.tvDeleteComment = (TextView) convertView.findViewById(R.id.tv_delete_comment);
            convertView.setTag(holder);
        } else
            holder = (FatherViewHolder) convertView.getTag();
        holder.position = position;
        Glide.with(mContext).load(Util.getImgUrl(commentList.get(position).getFather().getImgurl())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);
        holder.tvDate.setText(TimeUtils.questionCreateDuration(commentList.get(position).getFather().getCreatetime()));
        if (!TextUtils.isEmpty(commentList.get(position).getFather().getUsername()) && !TextUtils.equals("null", commentList.get(position).getFather().getUsername())) {
            holder.tvName.setText(commentList.get(position).getFather().getUsername());
        } else {
            holder.tvName.setText("匿名用户");
        }

        holder.tvClickCount.setText(commentList.get(position).getFather().getUps() + "");
        holder.tvCommentCount.setText(commentList.get(position).getFather().getCommentnum() + "");
        holder.llClick.setTag(holder);
        holder.llClick.setOnClickListener(this);
        holder.imgComment.setTag(position);
        holder.lvSons.setTag(position);
        holder.tvDeleteComment.setTag(position);
        holder.tvDeleteComment.setOnClickListener(this);
        holder.imgComment.setOnClickListener(this);
        if (!TextUtils.equals(commentList.get(position).getFather().getUserid(), (SpUtils.getStringSp(mContext, "userId")))) {
            holder.tvDeleteComment.setVisibility(View.GONE);
        } else {
            holder.tvDeleteComment.setVisibility(View.VISIBLE);
        }

        holder.tvFather.setText(commentList.get(position).getFather().getContent());

        if (DB.getInstance(DemoApplication.applicationContext).isClick(commentList.get(position).getFather().getCommentid())) {
            holder.imgClick.setImageResource(R.drawable.icon_click_love_selected);
        } else {
            holder.imgClick.setImageResource(R.drawable.icon_click_love_interview);
        }
        if (commentList.get(position).getSons() == null || commentList.get(position).getSons().size() == 0) {
            holder.rlSonContainer.setVisibility(View.GONE);
        } else {
            holder.rlSonContainer.setVisibility(View.VISIBLE);
            SonCommentAdapter adapter = new SonCommentAdapter(commentList.get(position).getSons(), position);
            holder.lvSons.setAdapter(adapter);
            holder.lvSons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int outerPosition = (int) parent.getTag();
                    intent = new Intent(mContext, CommentDetailsActivity.class);
                    intent.putExtra("bean", commentList.get(outerPosition));
                    intent.putExtra("position", outerPosition);
                    ((BaseActivity) mContext).startActivityForResult(intent, ImgTxtAlbumActivity.INTO_COMMENT_DETAIL);
                }
            });
        }
        convertView.setOnClickListener(this);
        return convertView;
    }

    public void setOnSonDeleteClickListener(OnSonDeleteClickListener onSonDeleteClickListener) {
        this.onSonDeleteClickListener = onSonDeleteClickListener;
    }

    @Override
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.ll_click:
                FatherViewHolder holder = (FatherViewHolder) v.getTag();
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_CHANGE);
                msg.setBody(commentList.get(holder.position).getFather().getCommentid());
                if (DB.getInstance(DemoApplication.applicationContext).isClick(commentList.get(holder.position).getFather().getCommentid())) {
//                    holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
//                    commentList.get(holder.position).getFather().setUps(commentList.get(holder.position).getFather().getUps() - 1);
//                    msg.setArg1(0);
//                    EventBus.getDefault().post(msg);
                    Toast.makeText(mContext, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    holder.imgClick.setImageResource(R.drawable.icon_click_love_selected);
                    commentList.get(holder.position).getFather().setUps(commentList.get(holder.position).getFather().getUps() + 1);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                }
                notifyDataSetChanged();
                holder.tvCommentCount.setText(commentList.get(holder.position).getFather().getUps() + "");
                break;
            case R.id.rl_all:
                intent = new Intent(mContext, CommentDetailsActivity.class);
                intent.putExtra("bean", commentList.get(((FatherViewHolder) v.getTag()).position));
                intent.putExtra("position", ((FatherViewHolder) v.getTag()).position);
                ((BaseActivity) mContext).startActivityForResult(intent, ImgTxtAlbumActivity.INTO_COMMENT_DETAIL);
                break;
            case R.id.img_comment:
                position = (int) v.getTag();
                intent = new Intent(mContext, CommentDetailsActivity.class);
                intent.putExtra("bean", commentList.get(position));
                intent.putExtra("position", position);
                ((BaseActivity) mContext).startActivityForResult(intent, ImgTxtAlbumActivity.INTO_COMMENT_DETAIL);
                break;
            case R.id.tv_delete_comment:
//                position = (int) v.getTag();
//                toHttpCommentDelete(position);
                if (onDeleteClickListener != null)
                    onDeleteClickListener.onDeleteClick((int) v.getTag());
                break;
        }
    }

    class SonCommentAdapter extends BaseAdapter {

        private List<ContentCommentBean.SonsBean> sonCommentList;
        private int parentPosition;

        public SonCommentAdapter(List<ContentCommentBean.SonsBean> sonCommentList, int parentPosition) {
            this.sonCommentList = sonCommentList;
            this.parentPosition = parentPosition;
        }

        @Override
        public int getCount() {
            return sonCommentList == null ? 0 : sonCommentList.size();
        }

        @Override
        public ContentCommentBean.SonsBean getItem(int position) {
            return sonCommentList == null ? null : sonCommentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            SonViewHolder holder;
            if (convertView == null) {
                holder = new SonViewHolder();
                convertView = mInflater.inflate(R.layout.item_son_comment, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvDeleteComment = (TextView) convertView.findViewById(R.id.tv_delete_comment);
                convertView.setTag(holder);
            } else
                holder = (SonViewHolder) convertView.getTag();
            if (!TextUtils.isEmpty(sonCommentList.get(position).getUsername()) && !TextUtils.equals("null", sonCommentList.get(position).getUsername())) {
                content = sonCommentList.get(position).getUsername() + "：" + sonCommentList.get(position).getContent();
            } else {
                content = "匿名用户吧：" + sonCommentList.get(position).getContent();
            }
            int length;
            if (!TextUtils.isEmpty(sonCommentList.get(position).getUsername())) {
                length = (sonCommentList.get(position).getUsername() + "：").length();
            } else {
                length = 0;
            }
            SpannableStringBuilder builder = null;
            builder = new SpannableStringBuilder(content);
            ForegroundColorSpan blueSpan1 = new ForegroundColorSpan(
                    mContext.getResources().getColor(
                            R.color.comment_highlight));
            ForegroundColorSpan blueSpan2 = new ForegroundColorSpan(
                    mContext.getResources().getColor(
                            R.color.comment_content));
            builder.setSpan(blueSpan1, 0, length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(blueSpan2, length,
                    content.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvName.setText(builder);
            if (TextUtils.equals(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"), sonCommentList.get(position).getUserid())) {
                holder.tvDeleteComment.setVisibility(View.VISIBLE);
            } else {
                holder.tvDeleteComment.setVisibility(View.GONE);
            }
            holder.tvDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSonDeleteClickListener != null) {
                        onSonDeleteClickListener.onDeleteClick(parentPosition, position);
                    }
                }
            });
            return convertView;
        }

        class SonViewHolder {
            private TextView tvName;
            private TextView tvDeleteComment;
        }

    }

    class FatherViewHolder {
        public int position;
        private ImageView imgAvatar;
        private AutoRelativeLayout rlContainer;
        private TextView tvName;
        private TextView tvDate;
        private AutoRelativeLayout rlAll;
        private AutoLinearLayout llClick;
        private ImageView imgClick;
        private TextView tvClickCount;
        private AutoLinearLayout llComment;
        private ImageButton imgComment;
        private TextView tvCommentCount;
        private TextView tvFather;
        private AutoRelativeLayout rlSonContainer;
        private ShowAllListView lvSons;
        private TextView tvMoreComment;
        private TextView tvDeleteComment;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnSonDeleteClickListener {
        void onDeleteClick(int parentPosition, int sonPosition);
    }
}
