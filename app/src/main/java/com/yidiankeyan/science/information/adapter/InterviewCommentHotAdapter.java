package com.yidiankeyan.science.information.adapter;

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
import com.yidiankeyan.science.information.acitivity.InterviewCommentListActivity;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.subscribe.entity.InterviewCommentBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;


import java.util.ArrayList;
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
public class InterviewCommentHotAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<InterviewCommentBean> commentList;
    private Context mContext;
    private LayoutInflater mInflater;
    private Intent intent;
    private String content;
    private OnDeleteClickListener onDeleteClickListener;
    private OnSonDeleteClickListener onSonDeleteClickListener;
    public InterviewCommentBean commentBean;
    private int isSize;

    public static final int VALUE_TIME_TITLE = 0;
    public static final int VALUE_LEFT_TEXT = 1;

    public InterviewCommentHotAdapter(ArrayList<InterviewCommentBean> commentList, Context context) {
        this.commentList = commentList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentList == null ? 0 : commentList.size();
    }

    @Override
    public InterviewCommentBean getItem(int position) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        FatherViewHolder holder = null;
        if (convertView == null) {
            switch (type) {
                case VALUE_TIME_TITLE:
                    convertView = mInflater.inflate(R.layout.item_interview_title,
                            null);
                    viewHolder = new ViewHolder();
                    viewHolder.tvTitle = (TextView) convertView
                            .findViewById(R.id.tv_interview_title);
                    convertView.setTag(viewHolder);
                    break;
                case VALUE_LEFT_TEXT:
                    convertView = mInflater.inflate(R.layout.item_interview_comment, parent, false);
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
                    holder.llLookMore = (AutoLinearLayout) convertView.findViewById(R.id.ll_look_more);
                    convertView.setTag(holder);
                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case VALUE_TIME_TITLE:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case VALUE_LEFT_TEXT:
                    holder = (FatherViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (type) {
            case VALUE_TIME_TITLE:
                viewHolder.tvTitle.setText(commentBean.text);
                break;
            case VALUE_LEFT_TEXT:
                holder.position = position;
                ContentCommentBean comment = commentList.get(position).contentCommentBean;
                Glide.with(mContext).load(Util.getImgUrl(comment.getFather().getImgurl())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);
                holder.tvDate.setText(TimeUtils.questionCreateDuration(comment.getFather().getCreatetime()));
                if (!TextUtils.isEmpty(comment.getFather().getUsername()) && !TextUtils.equals("null", comment.getFather().getUsername())) {
                    holder.tvName.setText(comment.getFather().getUsername());
                } else {
                    holder.tvName.setText("匿名用户");
                }

                holder.tvClickCount.setText(comment.getFather().getUps() + "");
                holder.tvCommentCount.setText(comment.getFather().getCommentnum() + "");
                holder.tvMoreComment.setText(comment.getFather().getCommentnum() + "");
                holder.llClick.setTag(holder);
                holder.llClick.setOnClickListener(this);
                holder.imgComment.setTag(position);
                holder.lvSons.setTag(position);
                holder.tvDeleteComment.setTag(position);
                holder.imgComment.setOnClickListener(this);
                holder.tvDeleteComment.setOnClickListener(this);
                holder.llLookMore.setTag(position);
                holder.llLookMore.setOnClickListener(this);

                if (!TextUtils.equals(comment.getFather().getUserid(), (SpUtils.getStringSp(mContext, "userId")))) {
                    holder.tvDeleteComment.setVisibility(View.GONE);
                } else {
                    holder.tvDeleteComment.setVisibility(View.VISIBLE);
                }

                holder.tvFather.setText(comment.getFather().getContent());

                if (DB.getInstance(DemoApplication.applicationContext).isClick(comment.getFather().getCommentid()) || comment.getFather().getLiked() == 1) {
                    holder.imgClick.setImageResource(R.drawable.icon_click_love_selected);
                } else {
                    holder.imgClick.setImageResource(R.drawable.icon_click_love_interview);
                }
                if (comment.getSons() == null || comment.getSons().size() == 0) {
                    holder.rlSonContainer.setVisibility(View.GONE);
                } else {
                    holder.rlSonContainer.setVisibility(View.VISIBLE);
                    SonCommentAdapter adapter = new SonCommentAdapter(comment.getSons(), position);
                    holder.lvSons.setAdapter(adapter);
                    holder.lvSons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int outerPosition = (int) parent.getTag();
                            intent = new Intent(mContext, InterviewCommentListActivity.class);
                            intent.putExtra("id", commentList.get(outerPosition).getContentCommentBean().getFather().getCommentid());
                            intent.putExtra("userId", commentList.get(outerPosition).contentCommentBean.getFather().getUserid());
                            intent.putExtra("userImg", commentList.get(outerPosition).contentCommentBean.getFather().getImgurl());
                            intent.putExtra("userName", commentList.get(outerPosition).contentCommentBean.getFather().getUsername());
                            intent.putExtra("comNum", commentList.get(outerPosition).contentCommentBean.getFather().getCommentnum());
                            intent.putExtra("clickNum", commentList.get(outerPosition).contentCommentBean.getFather().getUps());
                            intent.putExtra("content", commentList.get(outerPosition).contentCommentBean.getFather().getContent());
                            intent.putExtra("data", commentList.get(outerPosition).contentCommentBean.getFather().getCreatetime());
                            intent.putExtra("position", outerPosition);
                            ((BaseActivity) mContext).startActivityForResult(intent, MozInterviewDetailsActivity.INTO_COMMENT_DETAIL);
                        }
                    });
                }
                break;
        }

//        convertView.setOnClickListener(this);
        return convertView;
    }

    public void setOnSonDeleteClickListener(OnSonDeleteClickListener onSonDeleteClickListener) {
        this.onSonDeleteClickListener = onSonDeleteClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_comment:
                int posi = (int) v.getTag();
                EventMsg msgComt = EventMsg.obtain(SystemConstant.ON_INTERVIEW_COMMENT);
                msgComt.setArg1(posi);
                EventBus.getDefault().post(msgComt);
                break;
            case R.id.ll_click:
                if (!Util.hintLogin((BaseActivity) mContext)) return;
                FatherViewHolder holder = (FatherViewHolder) v.getTag();
                EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_CHANGE);
                msg.setBody(commentList.get(holder.position).getContentCommentBean().getFather().getCommentid());
                if (DB.getInstance(DemoApplication.applicationContext).isClick(commentList.get(holder.position).getContentCommentBean().getFather().getCommentid()) || commentList.get(holder.position).getContentCommentBean().getFather().getLiked() == 1) {
                    Toast.makeText(mContext, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    commentList.get(holder.position).getContentCommentBean().getFather().setUps(commentList.get(holder.position).getContentCommentBean().getFather().getUps() + 1);
                    holder.imgClick.setImageResource(R.drawable.icon_click_love_selected);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                }
                notifyDataSetChanged();
                holder.tvCommentCount.setText(commentList.get(holder.position).getContentCommentBean().getFather().getUps() + "");
                break;
            case R.id.tv_delete_comment:
                if (onDeleteClickListener != null)
                    onDeleteClickListener.onDeleteClick((int) v.getTag());
                break;
            case R.id.ll_look_more:
                int position = (int) v.getTag();
                EventMsg msgComts = EventMsg.obtain(SystemConstant.ON_INTERVIEW_COMMENT_LIST);
                msgComts.setArg1(position);
                EventBus.getDefault().post(msgComts);
                break;
        }
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        commentBean = getItem(position);
        if (commentBean.type == commentBean.SECTION) {
            return VALUE_TIME_TITLE;
        } else {
            return VALUE_LEFT_TEXT;
        }

    }

    class SonCommentAdapter extends BaseAdapter {

        private List<InterviewCommentBean.SonsBean> sonCommentList;
        private int parentPosition;

        public SonCommentAdapter(List<InterviewCommentBean.SonsBean> sonCommentList, int parentPosition) {
            this.sonCommentList = sonCommentList;
            this.parentPosition = parentPosition;
        }

        @Override
        public int getCount() {
            if (sonCommentList.size() > 3) {
                isSize = 3;
            } else {
                isSize = sonCommentList.size();
            }
            return sonCommentList == null ? 0 : isSize;
        }

        @Override
        public InterviewCommentBean.SonsBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.item_son_interview, parent, false);
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
        private AutoLinearLayout llLookMore;
    }

    class ViewHolder {
        private TextView tvTitle;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnSonDeleteClickListener {
        void onDeleteClick(int parentPosition, int sonPosition);
    }
}
