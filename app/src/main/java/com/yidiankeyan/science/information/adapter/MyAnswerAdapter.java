package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.MyAnswerDetailActivity;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 我答适配器
 */
public class MyAnswerAdapter extends BaseAdapter  {

    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * true:追问
     * false:问题
     */
    private boolean flag;
    /**
     * 问题
     */
    private final int TYPE_1 = 0;
    /**
     * 追问
     */
    private final int TYPE_2 = 1;
    /**
     * 已回答
     */
    private List<NewScienceHelp> mAnsweredDatas = new ArrayList<>();

    /**
     * 未回答
     */
    private List<NewScienceHelp> mWaitAnswerDatas = new ArrayList<>();

    private OnRecordTouchListener onRecordTouchListener;
    private OnEavesdropClick onEavesdropClick;

    public MyAnswerAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (flag)
            return TYPE_2;
        else
            return TYPE_1;
    }

    @Override
    public int getCount() {
        if (flag)
            return mAnsweredDatas == null ? 0 : mAnsweredDatas.size();
        else
            return mWaitAnswerDatas == null ? 0 : mWaitAnswerDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 判断当前显示已回答还是待回答
     *
     * @return true 已回答， false 待回答
     */
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setOnEavesdropClick(OnEavesdropClick onEavesdropClick) {
        this.onEavesdropClick = onEavesdropClick;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AnswerViewHolder answerViewHolder = null;
        AnsweredViewHolder answeredViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = mInflater.inflate(R.layout.item_my_answer_answer, parent, false);
                    answerViewHolder = new AnswerViewHolder();
                    answerViewHolder.imgRequestAvatar = (ImageView) convertView.findViewById(R.id.img_request_avatar);
                    answerViewHolder.tvRequestContent = (TextView) convertView.findViewById(R.id.tv_request_content);
                    answerViewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                    answerViewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    answerViewHolder.tvQuestionUserName = (TextView) convertView.findViewById(R.id.tv_question_user_name);
                    answerViewHolder.tvAnswerState = (TextView) convertView.findViewById(R.id.tv_answer_state);
                    answerViewHolder.tvProfession = (TextView) convertView.findViewById(R.id.tv_profession);
                    answerViewHolder.tvDomain = (TextView) convertView.findViewById(R.id.tv_domain);
                    answerViewHolder.tvEavesdropNum = (TextView) convertView.findViewById(R.id.tv_eavesdrop_num);
                    answerViewHolder.tvCheckState = (TextView) convertView.findViewById(R.id.tv_check_state);
                    answerViewHolder.rlDomain = (AutoRelativeLayout) convertView.findViewById(R.id.rl_domain);
                    convertView.setTag(answerViewHolder);
                    break;
                case TYPE_2:
                    convertView = mInflater.inflate(R.layout.item_my_answer_answer, parent, false);
                    answeredViewHolder = new AnsweredViewHolder();
                    answeredViewHolder.imgRequestAvatar = (ImageView) convertView.findViewById(R.id.img_request_avatar);
                    answeredViewHolder.tvRequestContent = (TextView) convertView.findViewById(R.id.tv_request_content);
                    answeredViewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                    answeredViewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    answeredViewHolder.tvQuestionUserName = (TextView) convertView.findViewById(R.id.tv_question_user_name);
                    answeredViewHolder.tvAnswerState = (TextView) convertView.findViewById(R.id.tv_answer_state);
                    answeredViewHolder.tvProfession = (TextView) convertView.findViewById(R.id.tv_profession);
                    answeredViewHolder.tvDomain = (TextView) convertView.findViewById(R.id.tv_domain);
                    answeredViewHolder.tvEavesdropNum = (TextView) convertView.findViewById(R.id.tv_eavesdrop_num);
                    answeredViewHolder.rlDomain = (AutoRelativeLayout) convertView.findViewById(R.id.rl_domain);
                    convertView.setTag(answeredViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    answerViewHolder = (AnswerViewHolder) convertView.getTag();
                    break;
                case TYPE_2:
                    answeredViewHolder = (AnsweredViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
                try {
                    Glide.with(mContext).load(Util.getImgUrl(mWaitAnswerDatas.get(position).getMaker().getCoverimg())).error(R.drawable.icon_default_avatar)
                            .placeholder(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(answerViewHolder.imgRequestAvatar);
                    answerViewHolder.tvQuestionUserName.setText(mWaitAnswerDatas.get(position).getMaker().getName());
                    answerViewHolder.tvProfession.setText(mWaitAnswerDatas.get(position).getMaker().getProfession());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                answerViewHolder.tvPrice.setText("¥" + mWaitAnswerDatas.get(position).getKedaQuestionsDB().getAmount());
                if (DemoApplication.answerAlbumMap.get(mWaitAnswerDatas.get(position).getKedaQuestionsDB().getKedaalbumid()) != null) {
                    answerViewHolder.tvRequestContent.setText(mWaitAnswerDatas.get(position).getKedaQuestionsDB().getQuestions());
                    answerViewHolder.tvDomain.setVisibility(View.VISIBLE);
                    answerViewHolder.rlDomain.setVisibility(View.VISIBLE);
                    answerViewHolder.tvDomain.setText(DemoApplication.answerAlbumMap.get(mWaitAnswerDatas.get(position).getKedaQuestionsDB().getKedaalbumid()).getKdname());
                } else {
                    answerViewHolder.tvRequestContent.setText(mWaitAnswerDatas.get(position).getKedaQuestionsDB().getQuestions());
                    answerViewHolder.tvDomain.setVisibility(View.GONE);
                    answerViewHolder.rlDomain.setVisibility(View.GONE);
                }

                final AnswerViewHolder finalAnswerViewHolder = answerViewHolder;
//                answerViewHolder.llAnswer.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            finalAnswerViewHolder.llAnswer.setBackgroundResource(R.drawable.shape_login_pressed);
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            finalAnswerViewHolder.llAnswer.setBackgroundResource(R.drawable.shape_orange_pressed);
//                        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                            finalAnswerViewHolder.llAnswer.setBackgroundResource(R.drawable.shape_orange_pressed);
//                        }
//                        if (onRecordTouchListener != null)
//                            onRecordTouchListener.onTouch(v, event, mWaitAnswerDatas.get(position).getKedaQuestionsDB().getId());
//                        return true;
//                    }
//                });
//                //到期时间
//                long surplusTime = mWaitAnswerDatas.get(position).getKedaQuestionsDB().getCreatetime() + 48 * 60 * 60 * 1000 - System.currentTimeMillis();
//                //剩余小时
//                long hour = surplusTime / (60 * 60 * 1000);
//                //剩余分钟
//                long minute = (surplusTime - (hour * (60 * 60 * 1000))) / (60 * 1000);
//                answerViewHolder.tvTime.setText(hour + "小时" + minute + "分钟");
                answerViewHolder.tvTime.setText(TimeUtils.formatKedaTime(mWaitAnswerDatas.get(position).getKedaQuestionsDB().getCreatetime()));
                if (mWaitAnswerDatas.get(position).getKederDB() != null) {
                    answerViewHolder.tvAnswerState.setText("已回答");
                    answerViewHolder.tvAnswerState.setTextColor(Color.parseColor("#525252"));
                    answerViewHolder.tvEavesdropNum.setVisibility(View.VISIBLE);
                    answerViewHolder.tvEavesdropNum.setText(mWaitAnswerDatas.get(position).getKederDB().getEavesdropnum() + "人偷听");
                    if (mWaitAnswerDatas.get(position).getKederDB().getIscheck() == 1) {
                        answerViewHolder.tvCheckState.setVisibility(View.VISIBLE);
                    } else {
                        answerViewHolder.tvCheckState.setVisibility(View.GONE);
                    }
//                    answerViewHolder.llAnswer.setVisibility(View.GONE);
                } else {
                    answerViewHolder.tvAnswerState.setText("待回答");
                    answerViewHolder.tvAnswerState.setTextColor(Color.parseColor("#FF0000"));
                    answerViewHolder.tvEavesdropNum.setVisibility(View.GONE);
                    answerViewHolder.tvCheckState.setVisibility(View.GONE);
//                    answerViewHolder.llAnswer.setVisibility(View.VISIBLE);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyAnswerDetailActivity.class);
                        intent.putExtra("id", mWaitAnswerDatas.get(position).getKedaQuestionsDB().getId());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case TYPE_2:
                Glide.with(mContext).load(Util.getImgUrl(mAnsweredDatas.get(position).getMaker().getCoverimg())).error(R.drawable.icon_default_avatar)
                        .placeholder(R.drawable.icon_default_avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(answeredViewHolder.imgRequestAvatar);
                answeredViewHolder.tvQuestionUserName.setText(mAnsweredDatas.get(position).getMaker().getName());
                answeredViewHolder.tvProfession.setText(mAnsweredDatas.get(position).getMaker().getProfession());
                if (DemoApplication.answerAlbumMap.get(mAnsweredDatas.get(position).getKederDB().getKedaalbumid()) != null) {
                    answeredViewHolder.tvRequestContent.setText(mAnsweredDatas.get(position).getKederDB().getQuestions());
                    answeredViewHolder.tvDomain.setVisibility(View.VISIBLE);
                    answeredViewHolder.rlDomain.setVisibility(View.VISIBLE);
                    answeredViewHolder.tvDomain.setText(DemoApplication.answerAlbumMap.get(mAnsweredDatas.get(position).getKederDB().getKedaalbumid()).getKdname());
                } else {
                    answeredViewHolder.tvRequestContent.setText(mAnsweredDatas.get(position).getKederDB().getQuestions());
                    answeredViewHolder.tvDomain.setVisibility(View.GONE);
                    answeredViewHolder.rlDomain.setVisibility(View.GONE);
                }
                answeredViewHolder.tvPrice.setText("¥" + mAnsweredDatas.get(position).getKederDB().getPayoff());
                answeredViewHolder.tvTime.setText(TimeUtils.formatKedaTime(mAnsweredDatas.get(position).getKederDB().getAnswertime()));
                if (!TextUtils.isEmpty(mAnsweredDatas.get(position).getKedaReQuestionDB().getAnswerurl())) {
                    answeredViewHolder.tvAnswerState.setText("已回答");
//                    answerViewHolder.llAnswer.setVisibility(View.GONE);
                    answeredViewHolder.tvEavesdropNum.setVisibility(View.VISIBLE);
                } else {
                    answeredViewHolder.tvAnswerState.setText("待回答");
//                    answerViewHolder.llAnswer.setVisibility(View.VISIBLE);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyAnswerDetailActivity.class);
                        intent.putExtra("id", mAnsweredDatas.get(position).getKederDB().getId());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        return convertView;
    }

    public void setOnRecordTouchListener(OnRecordTouchListener onRecordTouchListener) {
        this.onRecordTouchListener = onRecordTouchListener;
    }

    public List<NewScienceHelp> getAnsweredDatas() {
        return mAnsweredDatas;
    }

    public List<NewScienceHelp> getWaitAnswerDatas() {
        return mWaitAnswerDatas;
    }


    class AnswerViewHolder {
        private ImageView imgRequestAvatar;
        private TextView tvRequestContent;
        private TextView tvPrice;
        private TextView tvTime;
        private TextView tvQuestionUserName;
        private TextView tvAnswerState;
        private TextView tvProfession;
        private TextView tvDomain;
        private TextView tvEavesdropNum;
        private TextView tvCheckState;
        private AutoRelativeLayout rlDomain;
    }

    class AnsweredViewHolder {
        private ImageView imgRequestAvatar;
        private TextView tvRequestContent;
        private TextView tvPrice;
        private TextView tvTime;
        private TextView tvQuestionUserName;
        private TextView tvAnswerState;
        private TextView tvProfession;
        private TextView tvDomain;
        private TextView tvEavesdropNum;
        private AutoRelativeLayout rlDomain;
    }

    public interface OnRecordTouchListener {
        void onTouch(View v, MotionEvent event, String id);
    }

    public interface OnEavesdropClick {
        void onClick(NewScienceHelp keda);
    }
}
