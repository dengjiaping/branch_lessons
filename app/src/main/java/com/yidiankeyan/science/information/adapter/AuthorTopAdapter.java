package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AuthorTop;
import com.yidiankeyan.science.my.activity.HisDataActivity;
import com.yidiankeyan.science.my.activity.PersonalDataActivity;
import com.yidiankeyan.science.utils.SpUtils;
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
 * Created by nby on 2016/10/14.
 * 作用：排行--主编排行
 */
public class AuthorTopAdapter extends BaseAdapter {

    private List<AuthorTop> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public AuthorTopAdapter(List<AuthorTop> mDatas, Context context) {
        this.mDatas = mDatas;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public AuthorTop getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_author_top, parent, false);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_number);
            holder.imgAuthorAvatar = (ImageView) convertView.findViewById(R.id.img_author_avatar);
            holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvFansCount = (TextView) convertView.findViewById(R.id.tv_fans_count);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getImgurl())).error(R.drawable.icon_default_avatar).into(holder.imgAuthorAvatar);
        holder.tvAuthorName.setText(mDatas.get(position).getName());
        holder.tvTitle.setText(mDatas.get(position).getAlbumname());
        holder.tvFansCount.setText("粉丝量：" + mDatas.get(position).getFansnum());
        switch (position) {
            case 0:
                holder.txtNumber.setText(1 + "");
                holder.txtNumber.setBackgroundColor(Color.parseColor("#F1312E"));
                break;
            case 1:
                holder.txtNumber.setText(2 + "");
                holder.txtNumber.setBackgroundColor(Color.parseColor("#F18E2E"));
                break;
            case 2:
                holder.txtNumber.setText(3 + "");
                holder.txtNumber.setBackgroundColor(Color.parseColor("#FCD000"));
                break;
            default:
                holder.txtNumber.setText(position + 1 + "");
                holder.txtNumber.setBackgroundColor(Color.parseColor("#A0A0A0"));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(mDatas.get(position).getAuthorid(), SpUtils.getStringSp(mContext, "userId"))) {
                    Intent intent = new Intent(mContext, HisDataActivity.class);
                    intent.putExtra("id", mDatas.get(position).getAuthorid());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PersonalDataActivity.class);
//                    intent.putExtra("id",mContentList.get(getPlayPosition()).getArticleid());
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView txtNumber;
        private ImageView imgAuthorAvatar;
        private TextView tvAuthorName;
        private TextView tvTitle;
        private TextView tvFansCount;
    }
}
