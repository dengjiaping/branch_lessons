package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nby on 2016/7/12.
 */
public class AnswerTopDetailAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewScienceHelp> mDatas;
    private OnEavesdropClick onEavesdropClick;

    private ArrayList<AnswerAlbumBean> answerAlbumList;
    private List<AnswerAlbumBean> domanList = new ArrayList<>();

    public AnswerTopDetailAdapter(Context mContext, List<NewScienceHelp> datas) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mDatas = datas;
    }

    public void setOnEavesdropClick(OnEavesdropClick onEavesdropClick) {
        this.onEavesdropClick = onEavesdropClick;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public NewScienceHelp getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Log.e("position", "===" + position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_answer_top_detail, parent, false);
            holder = new ViewHolder();
            holder.imgRequestAvatar = (ImageView) convertView.findViewById(R.id.img_request_avatar);
            holder.imgAnswerAvatar = (ImageView) convertView.findViewById(R.id.img_answer_avatar);
            holder.tvRequestContent = (TextView) convertView.findViewById(R.id.tv_request_content);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvAudioLength = (TextView) convertView.findViewById(R.id.tv_audio_length);
            holder.tvEavesdropCount = (TextView) convertView.findViewById(R.id.tv_eavesdrop_count);
            holder.tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
            holder.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvQuestionUserName = (TextView) convertView.findViewById(R.id.tv_question_user_name);
            holder.tvEavedrop = (TextView) convertView.findViewById(R.id.tv_eavedrop);
            holder.rlEavedrop = (AutoRelativeLayout) convertView.findViewById(R.id.rl_eavedrop);
            holder.imgPlay = (ImageView) convertView.findViewById(R.id.img_play);
            holder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tvSurplusTime = (TextView) convertView.findViewById(R.id.tv_surplus_time);
            holder.llReturn = (AutoLinearLayout) convertView.findViewById(R.id.ll_return);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        try {
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getMaker().getCoverimg()))
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .error(R.drawable.icon_default_avatar)
                    .placeholder(R.drawable.icon_default_avatar).into(holder.imgRequestAvatar);
        } catch (NullPointerException e) {

        }
        try {
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getSolver().getCoverimg()))
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .error(R.drawable.icon_default_avatar)
                    .placeholder(R.drawable.icon_default_avatar).into(holder.imgAnswerAvatar);
        } catch (NullPointerException e) {

        }

        holder.tvRequestContent.setText(mDatas.get(position).getKederDB().getQuestions());
        holder.tvPrice.setText("" + mDatas.get(position).getKederDB().getPayoff());
        holder.tvAudioLength.setText(TimeUtils.getAnswerLength(mDatas.get(position).getKederDB().getTaketime()));
        holder.tvEavesdropCount.setText(mDatas.get(position).getKederDB().getEavesdropnum() + "人偷听");
        holder.tvClickCount.setText(mDatas.get(position).getKederDB().getPraisenum() + "");
        holder.tvTime.setText(TimeUtils.formatKedaTime(mDatas.get(position).getKederDB().getAnswertime()));
        if (mDatas.get(position).getMaker() == null) {
            holder.tvQuestionUserName.setText("");
        } else {
            if (TextUtils.isEmpty(mDatas.get(position).getMaker().getName()))
                holder.tvQuestionUserName.setText("匿名");
            else
                holder.tvQuestionUserName.setText(mDatas.get(position).getMaker().getName());
        }
        if (DB.getInstance(DemoApplication.applicationContext).isClick(mDatas.get(position).getKederDB().getId())) {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
        } else {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
        }
        holder.llReturn.setTag(holder);
        holder.llReturn.setOnClickListener(this);
        holder.rlEavedrop.setTag(position);
        holder.rlEavedrop.setOnClickListener(this);
        AnimationDrawable animationDrawable = (AnimationDrawable) holder.imgPlay.getDrawable();
        boolean isPlaying = AudioPlayManager.getInstance().isPlaying();
        int state = AudioPlayManager.getInstance().CURRENT_STATE;
        String mediaId = AudioPlayManager.getInstance().getmMediaPlayId();
        if ((isPlaying || state == SystemConstant.ON_PREPARE) && mediaId.equals(mDatas.get(position).getKederDB().getId())) {
//            if (!animationDrawable.isRunning())
            animationDrawable.start();
            holder.tvEavedrop.setText("点击暂停");
        } else {
//            if (animationDrawable.isRunning())
            animationDrawable.stop();
            if (mDatas.get(position).getKederInfo().getPermission() == 1) {
                long surplusTime = (mDatas.get(position).getKederDB().getAnswertime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
                if(surplusTime >= 0){
                    if (surplusTime == 0)
                        surplusTime++;
                    holder.tvEavedrop.setText("限时免费");
                    holder.tvSurplusTime.setText("还剩" + surplusTime + "分钟");
                    holder.tvSurplusTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.GONE);
                }else{
                    holder.tvEavedrop.setText("点击播放");
                    holder.tvSurplusTime.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                }
            } else {
                long surplusTime = (mDatas.get(position).getKederDB().getAnswertime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
                if (surplusTime >= 0) {
                    if (surplusTime == 0)
                        surplusTime++;
                    holder.tvEavedrop.setText("限时免费");
                    holder.tvSurplusTime.setText("还剩" + surplusTime + "分钟");
                    holder.tvSurplusTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.GONE);
                } else {
                    holder.tvEavedrop.setText("一墨子币偷听");
                    holder.tvSurplusTime.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                }
            }
        }
        if (mDatas.get(position).getMaker() == null) {
            holder.tvPosition.setText("");
        } else {
            if (!TextUtils.isEmpty(mDatas.get(position).getMaker().getProfession()) && !TextUtils.equals("null", mDatas.get(position).getMaker().getProfession())) {
                holder.tvPosition.setText(mDatas.get(position).getMaker().getProfession());
            }
        }
//        holder.tvPosition.setText(mDatas.get(position).getProfession() + "/" + mDatas.get(position).getPosition());
        return convertView;
    }

    public void updatePermission(String id) {
        for (NewScienceHelp scienceHelp : mDatas) {
            if (TextUtils.equals(id, scienceHelp.getKederDB().getId())) {
                scienceHelp.getKederInfo().setPermission(1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder;
        int position;
        switch (v.getId()) {
            case R.id.rl_eavedrop:
                if (onEavesdropClick != null)
                    onEavesdropClick.onClick(mDatas.get((int) v.getTag()));
                break;
            case R.id.ll_return:
                holder = (ViewHolder) v.getTag();
                if (!DB.getInstance(DemoApplication.applicationContext).isClick(mDatas.get(holder.position).getKederDB().getId())) {
                    toHttpLoveClick(mDatas.get(holder.position).getKederDB().getId(), holder);
                    EventMsg msg = EventMsg.obtain(SystemConstant.ADD_ZAN);
                    msg.setBody(mDatas.get(holder.position).getKederDB().getId());
                    EventBus.getDefault().post(msg);
                } else {
//                    holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
//                    holder.tvClickCount.setText((Integer.parseInt(holder.tvClickCount.getText().toString()) - 1) + "");
//                    EventMsg msg = EventMsg.obtain(SystemConstant.REMOVE_ZAN);
//                    msg.setBody(mDatas.get(holder.position).getKederDB().getId());
//                    EventBus.getDefault().post(msg);
                    Toast.makeText(mContext, "你已经赞过了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void toHttpLoveClick(String id, final ViewHolder holder) {
        Map<String, Object> map = new HashMap<>();
        map.put("uptype", "KEDER");
        map.put("targetid", id);
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    holder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
                    holder.tvClickCount.setText((Integer.parseInt(holder.tvClickCount.getText().toString()) + 1) + "");
                    ToastMaker.showShortToast("点赞成功");
                } else if (result.getCode() == 306) {
                    ToastMaker.showShortToast("您已经点过赞了");
                } else {
                    ToastMaker.showShortToast("点赞失败");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    class ViewHolder {
        private int position;
        private ImageView imgRequestAvatar;
        private TextView tvRequestContent;
        private TextView tvPrice;
        private TextView tvAudioLength;
        private TextView tvEavesdropCount;
        private TextView tvClickCount;
        private TextView tvPosition;
        private TextView tvTime;
        private ImageView imgClick;
        private TextView tvQuestionUserName;
        private TextView tvEavedrop;
        private AutoRelativeLayout rlEavedrop;
        private ImageView imgPlay;
        private TextView tvSurplusTime;
        private AutoLinearLayout llReturn;
        private ImageView imgAnswerAvatar;

    }

    public interface OnEavesdropClick {
        void onClick(NewScienceHelp keda);
    }
}
