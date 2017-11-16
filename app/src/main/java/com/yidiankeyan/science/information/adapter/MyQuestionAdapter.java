package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.MyQuestionDetailActivity;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

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
 * Created by nby on 2017/1/12.
 * 作用：
 */

public class MyQuestionAdapter extends BaseAdapter {

    private List<NewScienceHelp> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private String content = null;

    public MyQuestionAdapter(List<NewScienceHelp> data, Context context) {
        this.mData = data;
        this.mContext = context;
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
            convertView = mInflater.inflate(R.layout.item_my_question, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getSolver().getCoverimg())).into(holder.imgAvatar);
        } catch (NullPointerException e) {

        }
        if (mData.get(position).getSolver() == null) {
            holder.tvName.setText("");
            holder.tvProfession.setText("");
        } else {
            holder.tvName.setText(mData.get(position).getSolver().getName());
            holder.tvProfession.setText(mData.get(position).getSolver().getProfession());
        }
        holder.tvTime.setText(TimeUtils.formatKedaTime(mData.get(position).getKedaQuestionsDB().getCreatetime()) + "");
        holder.tvPrice.setText("¥" + mData.get(position).getKedaQuestionsDB().getAmount());
//        holder.tvAnswerState.setText(mData.get(position).getKedaQuestionsDB().getIsanswer() == 0 ?
//                "待回答" : "已回答");
        if (mData.get(position).getKedaQuestionsDB().getIsanswer() == 0) {
            holder.tvAnswerState.setText("待回答");
            holder.tvAnswerState.setTextColor(Color.parseColor("#FF0000"));
            holder.tvEavesdropping.setVisibility(View.GONE);
            holder.tvEavesdropping.setText(mData.get(position).getMaker().getEavesdropnum()+"人偷听");
        } else {
            holder.tvAnswerState.setText("已回答");
            holder.tvAnswerState.setTextColor(Color.parseColor("#525252"));
            holder.tvEavesdropping.setVisibility(View.VISIBLE);
            holder.tvEavesdropping.setText(mData.get(position).getMaker().getEavesdropnum()+"人偷听");
        }
        if (DemoApplication.answerAlbumMap.get(mData.get(position).getKedaQuestionsDB().getKedaalbumid()) != null) {
//            holder.tvContent.setText("【" + DemoApplication.answerAlbumMap.get(mData.get(position).getKedaQuestionsDB().getKedaalbumid()).getKdname() + "】 " + mData.get(position).getKedaQuestionsDB().getQuestions());
            content = "【" + DemoApplication.answerAlbumMap.get(mData.get(position).getKedaQuestionsDB().getKedaalbumid()).getKdname() + "】" + mData.get(position).getKedaQuestionsDB().getQuestions();
            Integer bstart = content.indexOf( "【"+DemoApplication.answerAlbumMap.get(mData.get(position).getKedaQuestionsDB().getKedaalbumid()).getKdname());
            Integer bend = bstart + DemoApplication.answerAlbumMap.get(mData.get(position).getKedaQuestionsDB().getKedaalbumid()).getKdname().length()+2;
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            style.setSpan(new ForegroundColorSpan(Color.RED), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvContent.setText(style);
        } else {
            holder.tvContent.setText(mData.get(position).getKedaQuestionsDB().getQuestions());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyQuestionDetailActivity.class);
                intent.putExtra("id", mData.get(position).getKedaQuestionsDB().getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvProfession;
        private TextView tvAnswerState;
        private TextView tvPrice;
        private TextView tvContent;
        private TextView tvTime;
        private TextView tvEavesdropping;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvProfession = (TextView) convertView.findViewById(R.id.tv_profession);
            tvAnswerState = (TextView) convertView.findViewById(R.id.tv_answer_state);
            tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvEavesdropping= (TextView) convertView.findViewById(R.id.tv_eavesdropping);
        }
    }
}
