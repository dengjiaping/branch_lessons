package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.ColumnDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.entity.PayTopBean;
import com.yidiankeyan.science.utils.SystemConstant;
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
 * Created by nby on 2016/12/14.
 * 作用：
 */

public class PayTopAdapter extends BaseAdapter {

    private List<PayTopBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public PayTopAdapter(List<PayTopBean> data, Context context) {
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pay_top, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (mData.get(position).getEntitytype() == 1) {
            //专栏
            Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimg())).into(holder.imgAuthor);
            holder.tvTitle.setText(mData.get(position).getTitle());
            holder.tvAuthorName.setText(mData.get(position).getSubtitle());
            holder.tvPrice.setText("¥" + String.format("%.2f", mData.get(position).getPrice()) + "/年");
            holder.tvDesc.setText(mData.get(position).getLatesttitle());
        } else {
            //读书
            Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimg())).into(holder.imgAuthor);
            holder.tvTitle.setText(mData.get(position).getTitle());
            holder.tvAuthorName.setText(mData.get(position).getSubtitle());
            holder.tvPrice.setText("");
            if (mData.get(position).getIspaid() == 1) {
                holder.tvDesc.setText("已购");
                holder.tvDesc.setTextColor(Color.parseColor("#FF0000"));
            } else {
                if (mData.get(position).getPrice() != 0) {
                    holder.tvDesc.setText("¥" + String.format("%.2f", mData.get(position).getPrice()) + "/本");
                    holder.isPrice = 2;
                    holder.tvDesc.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    holder.tvDesc.setText("免费");
                    holder.isPrice = 1;
                    holder.tvDesc.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        }

        double width = (168.0 / 750.0) * SystemConstant.ScreenWidth;
        double height = (228.0 / 168.0) * width;
        ViewGroup.LayoutParams lp = holder.imgAuthor.getLayoutParams();
        lp.width = (int) width;
        lp.height = (int) height;
        holder.imgAuthor.setLayoutParams(lp);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (mData.get(position).getEntitytype() == 1) {
                    intent = new Intent(mContext, ColumnDetailsActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(mContext, MozReadDetailsActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    intent.putExtra("isPrice", holder.isPrice + "");
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAuthor;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvAuthorName;
        private TextView tvPrice;
        private TextView tvDesc;
        private int isPrice;

        public ViewHolder(View convertView) {
            imgAuthor = (ImageView) convertView.findViewById(R.id.img_author);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
        }
    }
}
