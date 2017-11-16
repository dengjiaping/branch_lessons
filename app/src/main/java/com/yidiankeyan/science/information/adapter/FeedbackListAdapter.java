package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.FeedbackListBean;
import com.yidiankeyan.science.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/10/25.
 */

public class FeedbackListAdapter extends RecyclerAdapter<FeedbackListBean> {

    private Context mContext;
    private FeedbackListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(FeedbackListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FeedbackListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<FeedbackListBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<FeedbackListBean> {

        private ImageView mivHeadImage;
        private TextView mtvTitle;
        private TextView mtvTime;
        private TextView mtvMessage;
        private TextView mtvMessageMe;
        private ImageView mivHeadImageMe;
        private TextView mtvTimeMe;
        private TextView mtvTitleMe;
        private ImageView mimgAvatar;
        private ImageView mllReturn;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.activity_feedback_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mivHeadImage = (ImageView) findViewById(R.id.view1).findViewById(R.id.iv_head_image);
            mtvTitle = (TextView) findViewById(R.id.view1).findViewById(R.id.tv_title);
            mtvTime = (TextView) findViewById(R.id.view1).findViewById(R.id.tv_time);
            mtvMessage = (TextView) findViewById(R.id.view1).findViewById(R.id.tv_message);
            mimgAvatar = (ImageView) findViewById(R.id.img_avatar);
            mivHeadImageMe = (ImageView) findViewById(R.id.view2).findViewById(R.id.iv_head_image);
            mtvTitleMe = (TextView) findViewById(R.id.view2).findViewById(R.id.tv_title);
            mtvTimeMe = (TextView) findViewById(R.id.view2).findViewById(R.id.tv_time);
            mtvMessageMe = (TextView) findViewById(R.id.view2).findViewById(R.id.tv_message);
        }

        @Override
        public void onItemViewClick(FeedbackListBean data) {
            super.onItemViewClick(data);
//            if (FeedbackListAdapter.this.onItemClickListener != null) {
//                FeedbackListAdapter.this.onItemClickListener.onItemClick(getLayoutPosition(), isPrice);
//            }
        }

        @Override
        public void setData(final FeedbackListBean data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getUserImg())).placeholder(R.drawable.icon_default_avatar).
                    bitmapTransform(new CropCircleTransformation(getContext())).
                    error(R.drawable.icon_default_avatar).into(mivHeadImage);
            if (!StringUtils.isEmpty(data.getUserName())) {
                mtvTitle.setText(data.getUserName());
            }
            if (!StringUtils.isEmpty(data.getCreatetime() + "")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                String createTime = simpleDateFormat.format(new Date(data.getCreatetime()));
                mtvTime.setText(createTime);
            }
            if (!StringUtils.isEmpty(data.getContent())) {
                mtvMessage.setText(data.getContent());
            }
            if(!StringUtils.isEmpty(data.getImgurl())){
                mimgAvatar.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(Util.getImgUrl(data.getImgurl()))
                        .placeholder(R.drawable.icon_hotload_failed)
                        .error(R.drawable.icon_hotload_failed).into(mimgAvatar);
            }else {
                mimgAvatar.setVisibility(View.GONE);
            }
            mimgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FeedbackListAdapter.this.onItemClickListener != null) {
                        FeedbackListAdapter.this.onItemClickListener.onItemClick(getLayoutPosition(),
                                Util.getImgUrl(data.getAdminImg()));
                    }
                }
            });
            if (!StringUtils.isEmpty(data.getReply())) {
                mtvTitleMe.setVisibility(View.VISIBLE);
                mtvTimeMe.setVisibility(View.VISIBLE);
                mtvMessageMe.setVisibility(View.VISIBLE);
                mivHeadImageMe.setVisibility(View.VISIBLE);
                mtvMessageMe.setText(data.getReply());
                Glide.with(getContext()).load(Util.getImgUrl(data.getAdminImg()))
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .placeholder(R.drawable.icon_default_avatar)
                        .error(R.drawable.icon_default_avatar).into(mivHeadImageMe);
                mtvTitleMe.setText(data.getAdminName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                String createTime = simpleDateFormat.format(new Date(data.getReplyTime()));
                mtvTimeMe.setText(createTime);
            } else{
                mtvMessageMe.setVisibility(View.GONE);
                mtvTimeMe.setVisibility(View.GONE);
                mtvTitleMe.setVisibility(View.GONE);
                mivHeadImageMe.setVisibility(View.GONE);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, String urlId);
    }
}
