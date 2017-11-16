package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
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
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.AnswerTopDetailActivity;
import com.yidiankeyan.science.information.acitivity.QADetailsActivity;
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
 * Created by nby on 2017/2/15.
 * 作用：
 */

public class EavedropTopAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewScienceHelp> list = new ArrayList<>();
    private OnEavesdropClick onEavesdropClick;

    public EavedropTopAdapter(Context mContext, List<NewScienceHelp> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnEavesdropClick(OnEavesdropClick onEavesdropClick) {
        this.onEavesdropClick = onEavesdropClick;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_keda_eavedrop_top, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        final NewScienceHelp scienceHelp = list.get(position);
        if (scienceHelp.getSolver() == null)
            scienceHelp.setSolver(new NewScienceHelp.SolverBean());
        if (DemoApplication.answerAlbumMap.get(scienceHelp.getKederDB().getKedaalbumid()) != null) {
            holder.tvDomain.setVisibility(View.VISIBLE);
            holder.rlDomain.setVisibility(View.VISIBLE);
            holder.tvDomain.setText(DemoApplication.answerAlbumMap.get(scienceHelp.getKederDB().getKedaalbumid()).getKdname());
        } else {
            holder.tvDomain.setVisibility(View.GONE);
            holder.rlDomain.setVisibility(View.GONE);
        }
        holder.tvContent.setText(scienceHelp.getKederDB().getQuestions());
        holder.tvName.setText(scienceHelp.getSolver().getName());
//        holder.imgAvatar.setImageResource(scienceHelp.getImgUrl());
//        if ("xx".equals(scienceHelp.getSolverimgurl()))
//            Glide.with(mContext).load(url).into(holder.imgAvatar);
//        else
        Glide.with(mContext).load(Util.getImgUrl(scienceHelp.getSolver().getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext))
                .placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar).into(holder.imgAvatar);

        holder.tvEavesdropCount.setText(scienceHelp.getKederDB().getEavesdropnum() + "人偷听");
        holder.tvClickCount.setText(scienceHelp.getKederDB().getPraisenum() + "");
        holder.tvTime.setText(TimeUtils.formatKedaTime(scienceHelp.getKederDB().getAnswertime()));
        holder.tvLength.setText(TimeUtils.getAnswerLength(scienceHelp.getKederDB().getTaketime()));
        if(TextUtils.isEmpty(scienceHelp.getMaker().getProfession())&&TextUtils.equals("null",scienceHelp.getMaker().getProfession())){
            holder.tvOccupationalName.setText("");
        }else{
            holder.tvOccupationalName.setText(scienceHelp.getMaker().getProfession()+"");
        }
        holder.imgEavedrop.setTag(position);
        holder.imgEavedrop.setOnClickListener(this);
        if (DB.getInstance(DemoApplication.applicationContext).isClick(scienceHelp.getKederDB().getId())) {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
        } else {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
        }
        holder.rlLoveClick.setTag(holder);
        holder.rlLoveClick.setOnClickListener(this);
//        holder.imgAvatar.setTag(position);
        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnswerTopDetailActivity.class);
                intent.putExtra("name", list.get(position).getSolver().getName());
                intent.putExtra("id", list.get(position).getSolver().getId());
                mContext.startActivity(intent);
            }
        });
        AnimationDrawable animationDrawable = (AnimationDrawable) holder.imgPlay.getDrawable();
        boolean isPlaying = AudioPlayManager.getInstance().isPlaying();
        int state = AudioPlayManager.getInstance().CURRENT_STATE;
        String mediaId = AudioPlayManager.getInstance().getmMediaPlayId();
        if ((isPlaying || state == SystemConstant.ON_PREPARE) && mediaId.equals(list.get(position).getKederDB().getId())) {
//            if (!animationDrawable.isRunning())
            animationDrawable.start();
            holder.tvOneEavesdropping.setText("点击暂停");
        } else {
//            if (animationDrawable.isRunning())
            animationDrawable.stop();
            if (scienceHelp.getKederInfo().getPermission() == 1) {
                long surplusTime = (scienceHelp.getKederDB().getAnswertime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
                if(surplusTime >= 0){
                    if (surplusTime == 0)
                        surplusTime++;
                    holder.tvOneEavesdropping.setText("限时免费");
                    holder.tvSurplusTime.setText("还剩" + surplusTime + "分钟");
                    holder.tvSurplusTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.GONE);
                }else{
                    holder.tvOneEavesdropping.setText("点击播放");
                    holder.tvSurplusTime.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                }
            } else {
                long surplusTime = (scienceHelp.getKederDB().getTaketime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
                if (surplusTime >= 0) {
                    if (surplusTime == 0)
                        surplusTime++;
                    holder.tvOneEavesdropping.setText("限时免费");
                    holder.tvSurplusTime.setText("还剩" + surplusTime + "分钟");
                    holder.tvSurplusTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.GONE);
                } else {
                    holder.tvOneEavesdropping.setText("一墨子币偷听");
                    holder.tvSurplusTime.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                }
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QADetailsActivity.class);
                intent.putExtra("id", list.get(position).getKederDB().getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder;
        int position;
        switch (v.getId()) {
            case R.id.img_eavedrop:
                if (onEavesdropClick != null)
                    onEavesdropClick.onClick((int) v.getTag());
                break;
            case R.id.rl_love_click:
                if (!Util.hintLogin((BaseActivity) mContext)) return;
                holder = (ViewHolder) v.getTag();
                if (!DB.getInstance(DemoApplication.applicationContext).isClick(list.get(holder.position).getKederDB().getId())) {
                    toHttpLoveClick(list.get(holder.position).getKederDB().getId(), holder);
                    EventMsg msg = EventMsg.obtain(SystemConstant.ADD_ZAN);
                    msg.setBody(list.get(holder.position).getKederDB().getId());
                    EventBus.getDefault().post(msg);
                } else {
//                    holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
//                    holder.tvClickCount.setText((Integer.parseInt(holder.tvClickCount.getText().toString()) - 1) + "");
//                    EventMsg msg = EventMsg.obtain(SystemConstant.REMOVE_ZAN);
//                    msg.setBody(list.get(holder.position).getKederid());
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

    public void updatePermission(String id) {
        for (NewScienceHelp scienceHelp : list) {
            if (TextUtils.equals(id, scienceHelp.getKederDB().getId())) {
                scienceHelp.getKederInfo().setPermission(1);
            }
        }
    }

    public interface OnEavesdropClick {
        void onClick(int position);
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvSurplusTime;
        private TextView tvTime;
        private TextView tvName;
        private TextView tvOccupationalName;
        private TextView tvContent;
        private AutoRelativeLayout imgEavedrop;
        private TextView tvOneEavesdropping;
        private ImageView imgPlay;
        private ImageView imgClick;
        private TextView tvLength;
        private AutoLinearLayout llContainer;
        private TextView tvEavesdropCount;
        private TextView tvClickCount;
        public int position;
        private TextView tvDomain;
        private AutoRelativeLayout rlDomain;
        private AutoRelativeLayout rlLoveClick;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvSurplusTime = (TextView) convertView.findViewById(R.id.tv_surplus_time);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvOccupationalName = (TextView) convertView.findViewById(R.id.tv_occupational_name);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            imgEavedrop = (AutoRelativeLayout) convertView.findViewById(R.id.img_eavedrop);
            tvOneEavesdropping = (TextView) convertView.findViewById(R.id.tv_one_eavesdropping);
            imgPlay = (ImageView) convertView.findViewById(R.id.img_play);
            tvLength = (TextView) convertView.findViewById(R.id.tv_length);
            llContainer = (AutoLinearLayout) convertView.findViewById(R.id.ll_container);
            tvEavesdropCount = (TextView) convertView.findViewById(R.id.tv_eavesdrop_count);
            tvDomain = (TextView) convertView.findViewById(R.id.tv_domain);
            tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
            rlDomain = (AutoRelativeLayout) convertView.findViewById(R.id.rl_domain);
            imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            rlLoveClick= (AutoRelativeLayout) convertView.findViewById(R.id.rl_love_click);
        }
    }
}
