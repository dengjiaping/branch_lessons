package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AnswerAlbumDetailUserBean;
import com.yidiankeyan.science.utils.Util;

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
 * Created by nby on 2017/2/14.
 * 作用：问答专辑里面的答人列表
 */

public class AnswerAlbumDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<AnswerAlbumDetailUserBean> mData;
    private LayoutInflater mInflater;

    public AnswerAlbumDetailAdapter(Context context, List<AnswerAlbumDetailUserBean> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_answer_top, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvDatitle.setText(mData.get(position).getName());
        if (!TextUtils.isEmpty(mData.get(position).getProfession()) && !TextUtils.equals("null", mData.get(position).getProfession())) {
            holder.tvDajob.setText(mData.get(position).getProfession());
        }
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);
        holder.txtDawendaliang.setText(mData.get(position).getAnswersnum() + "");
        holder.txtDaguanzhuliang.setText(mData.get(position).getFollowers() + "人已关注");
        holder.txtDatoutingliang.setText(mData.get(position).getEavesdropnum() + "人偷听");
        return convertView;
    }

    class ViewHolder {
        private ImageView imgAvatar;
        private TextView tvDatitle;
        private TextView tvDajob;
        private TextView txtDawendaliang;
        private TextView txtDaguanzhuliang;
        private TextView txtDatoutingliang;

        public ViewHolder(View convertView) {
            imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            tvDatitle = (TextView) convertView.findViewById(R.id.tv_datitle);
            tvDajob = (TextView) convertView.findViewById(R.id.tv_dajob);
            txtDawendaliang = (TextView) convertView.findViewById(R.id.txt_dawendaliang);
            txtDaguanzhuliang = (TextView) convertView.findViewById(R.id.txt_daguanzhuliang);
            txtDatoutingliang = (TextView) convertView.findViewById(R.id.txt_datoutingliang);
        }
    }
}
