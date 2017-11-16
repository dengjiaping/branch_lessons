package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.ReceivePrizeActivity;
import com.yidiankeyan.science.information.entity.MyPrizeBean;
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
 * Created by nby on 2016/11/15.
 * 作用：
 */
public class MyPrizeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyPrizeBean> mData;
    private LayoutInflater mInflater;

    public MyPrizeListAdapter(Context mContext, List<MyPrizeBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
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
            convertView = mInflater.inflate(R.layout.item_my_prize, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.tvPrizeName = (TextView) convertView.findViewById(R.id.tv_prize_name);
            holder.tvPrizeState = (TextView) convertView.findViewById(R.id.tv_prize_state);
            holder.tvReceivePrize = (TextView) convertView.findViewById(R.id.tv_receive_prize);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getImgurl())).into(holder.imgAvatar);
        if (mData.get(position).getIsreceived() == 1) {
            holder.tvReceivePrize.setText("已领取");
            holder.tvPrizeState.setVisibility(View.GONE);
            holder.tvReceivePrize.setOnClickListener(null);
        } else {
            holder.tvReceivePrize.setText("领取");
            holder.tvPrizeState.setVisibility(View.VISIBLE);
            holder.tvReceivePrize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReceivePrizeActivity.class);
                    intent.putExtra("prizeId", mData.get(position).getId());
                    ((BaseActivity) mContext).startActivityForResult(intent, 111);
                }
            });
        }
        holder.tvPrizeName.setText(mData.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvPrizeName;
        private TextView tvPrizeState;
        private TextView tvReceivePrize;
    }
}
