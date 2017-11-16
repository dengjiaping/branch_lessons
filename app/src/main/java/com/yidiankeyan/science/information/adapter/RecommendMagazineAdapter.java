package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.MagazineBean;
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
 * Created by nby on 2016/9/1.
 * 作用：推荐杂志adapter
 */
public class RecommendMagazineAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<MagazineBean> mDatas;

    public RecommendMagazineAdapter(Context context, List<MagazineBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public MagazineBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_recommend_magazine, parent, false);
            holder.imgMagazineAvatar = (ImageView) convertView.findViewById(R.id.img_magazine_avatar);
            holder.txtMagazineName = (TextView) convertView.findViewById(R.id.txt_magazine_name);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.txtMessageNumber = (TextView) convertView.findViewById(R.id.txt_message_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimg())).into(holder.imgMagazineAvatar);
        holder.txtMagazineName.setText(mDatas.get(position).getName());
        holder.tvDate.setText(TimeUtils.formatData3(mDatas.get(position).getPublishtime()) + "刊目录");
        holder.tvContent.setText(mDatas.get(position).getIntroduction());
        holder.txtMessageNumber.setText(mDatas.get(position).getReadnum() + "");
        return convertView;
    }

    class ViewHolder {
        private ImageView imgMagazineAvatar;
        private TextView txtMagazineName;
        private TextView tvDate;
        private TextView tvContent;
        private TextView txtMessageNumber;
    }
}
