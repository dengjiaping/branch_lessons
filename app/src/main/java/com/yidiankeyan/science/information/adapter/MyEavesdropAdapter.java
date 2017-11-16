package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.yidiankeyan.science.information.acitivity.QADetailsActivity;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
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
 * Created by nby on 2017/2/25.
 * 作用：
 */

public class MyEavesdropAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewScienceHelp> mData;
    private String content;

    public MyEavesdropAdapter(Context context, List<NewScienceHelp> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_eavesdrop, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getSolver().getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);
        holder.tvTime.setText(TimeUtils.formatKedaTime(mData.get(position).getKederDB().getAnswertime()));
        holder.tvName.setText(mData.get(position).getSolver().getName());
        if (DemoApplication.answerAlbumMap.get(mData.get(position).getKederDB().getKedaalbumid()) != null) {
            holder.tvDomain.setVisibility(View.VISIBLE);
            holder.rlDomain.setVisibility(View.VISIBLE);
            holder.tvDomain.setText(DemoApplication.answerAlbumMap.get(mData.get(position).getKederDB().getKedaalbumid()).getKdname());
        } else {
            holder.tvDomain.setVisibility(View.GONE);
            holder.rlDomain.setVisibility(View.GONE);
            holder.tvContent.setText(mData.get(position).getKederDB().getQuestions());
        }
        holder.tvOccupationalName.setText(mData.get(position).getSolver().getProfession());
        holder.tvContent.setText(mData.get(position).getKederDB().getQuestions());
        holder.tvEavesdropCount.setText(mData.get(position).getKederDB().getEavesdropnum() + "人偷听");
        holder.tvClickCount.setText(mData.get(position).getKederDB().getPraisenum() + "");

        if (DB.getInstance(DemoApplication.applicationContext).isClick(mData.get(position).getKederDB().getId())) {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
        } else {
            holder.imgClick.setImageResource(R.drawable.icon_today_click_normal);
        }
        holder.llClick.setTag(holder);
        holder.llClick.setOnClickListener(this);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QADetailsActivity.class);
                intent.putExtra("id", mData.get(position).getKederDB().getId());
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
            case R.id.ll_click:
                holder = (ViewHolder) v.getTag();
                if (!DB.getInstance(DemoApplication.applicationContext).isClick(mData.get(holder.position).getKederDB().getId())) {
                    holder.imgClick.setImageResource(R.drawable.icon_today_click_pressed);
                    holder.tvClickCount.setText((Integer.parseInt(holder.tvClickCount.getText().toString()) + 1) + "");
                    EventMsg msg = EventMsg.obtain(SystemConstant.ADD_ZAN);
                    msg.setBody(mData.get(holder.position).getKederDB().getId());
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

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTime;
        private TextView tvName;
        private TextView tvDomain;
        private TextView tvOccupationalName;
        private TextView tvContent;
        private TextView tvEavesdropCount;
        private TextView tvClickCount;
        public int position;
        private AutoRelativeLayout rlDomain;
        private ImageView imgClick;
        private AutoLinearLayout llClick;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvDomain = (TextView) convertView.findViewById(R.id.tv_domain);
            tvOccupationalName = (TextView) convertView.findViewById(R.id.tv_occupational_name);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            tvEavesdropCount = (TextView) convertView.findViewById(R.id.tv_eavesdrop_count);
            tvClickCount = (TextView) convertView.findViewById(R.id.tv_click_count);
            rlDomain = (AutoRelativeLayout) convertView.findViewById(R.id.rl_domain);
            imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            llClick = (AutoLinearLayout) convertView.findViewById(R.id.ll_click);
        }
    }
}
