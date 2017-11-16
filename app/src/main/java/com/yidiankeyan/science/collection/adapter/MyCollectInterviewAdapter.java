package com.yidiankeyan.science.collection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.my.entity.MyCollectInterviewBean;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoRelativeLayout;

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
 * Created by nby on 2016/10/20.
 * 作用：
 */
public class MyCollectInterviewAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private List<MyCollectInterviewBean> mDatas;
    private LayoutInflater mInflater;

    public MyCollectInterviewAdapter(Context context, List<MyCollectInterviewBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public MyCollectInterviewBean getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_interview_my, parent, false);
            holder.cbDelete = (CheckBox) convertView.findViewById(R.id.cb_delete);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvReadNum = (TextView) convertView.findViewById(R.id.tv_read_num);
            holder.rlMyInterview= (AutoRelativeLayout) convertView.findViewById(R.id.rl_my_interview);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getInterviewImgUrl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(holder.imgAvatar);
        holder.tvName.setText(mDatas.get(position).getInterviewName());
        holder.tvReadNum.setText(mDatas.get(position).getCreateTime());

        holder.cbDelete.setTag(position);

        holder.rlMyInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MozInterviewDetailsActivity.class);
//                intent.putParcelableArrayListExtra("list", mDatas);
                intent.putExtra("id", mDatas.get(position).getId());
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        mDatas.get(position).setNeedDelete(isChecked);
    }

    class ViewHolder {
        private CheckBox cbDelete;
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvReadNum;
        private AutoRelativeLayout rlMyInterview;
    }
}
