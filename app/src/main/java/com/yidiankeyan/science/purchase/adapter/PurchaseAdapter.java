package com.yidiankeyan.science.purchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.purchase.entity.ColumnPurchaseBean;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
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
 * Created by nby on 2016/11/30.
 * 作用：
 */

public class PurchaseAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ColumnPurchaseBean> mLists = new ArrayList<>();

    public PurchaseAdapter(Context context, List<ColumnPurchaseBean> mLists) {
        this.mContext = context;
        this.mLists = mLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_purchase, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (holder == null)
            return convertView;

        Glide.with(mContext).load(Util.getImgUrl(mLists.get(position).getCoverimgurl()))
                .placeholder(R.drawable.icon_readload_failed)
                .error(R.drawable.icon_readload_failed).into(holder.imgAvatar);
        holder.tvName.setText(mLists.get(position).getName());

        return convertView;
    }

}

class ViewHolder {
    ImageView imgAvatar;
    TextView tvName;

    public ViewHolder(View convertView) {
        imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
    }
}

