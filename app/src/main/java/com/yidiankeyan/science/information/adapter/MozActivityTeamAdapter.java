package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.VoteBean;
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
 * Created by nby on 2016/11/10.
 * 作用：moz投票队伍
 */
public class MozActivityTeamAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<VoteBean.PartsBean> mData;
    private LayoutInflater mInflater;
    private OnVoteListener onVoteListener;
    private OnToTeamDetailListener onToTeamDetailListener;

    public MozActivityTeamAdapter(Context context, List<VoteBean.PartsBean> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnToTeamDetailListener(OnToTeamDetailListener onToTeamDetailListener) {
        this.onToTeamDetailListener = onToTeamDetailListener;
    }

    public void setOnVoteListener(OnVoteListener onVoteListener) {
        this.onVoteListener = onVoteListener;
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
            convertView = mInflater.inflate(R.layout.item_moz_team, parent, false);
            holder = new ViewHolder();
            holder.tvTeamNum = (TextView) convertView.findViewById(R.id.tv_team_num);
            holder.tvTeamName = (TextView) convertView.findViewById(R.id.tv_team_name);
            holder.imgTeamAvatar = (ImageView) convertView.findViewById(R.id.img_team_avatar);
            holder.tvRanking = (TextView) convertView.findViewById(R.id.tv_ranking);
            holder.tvTicketNum = (TextView) convertView.findViewById(R.id.tv_ticket_num);
            holder.btnVote = (Button) convertView.findViewById(R.id.btn_vote);
            holder.rlContainer = (AutoRelativeLayout) convertView.findViewById(R.id.rl_container);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTeamNum.setText(mData.get(position).getSort() + "号队");
        holder.tvRanking.setText(mData.get(position).getRanknum() + "");
        holder.tvTeamName.setText(mData.get(position).getName());
        holder.tvTicketNum.setText(mData.get(position).getTotalnum() + "");
        Glide.with(mContext).load(Util.getImgUrl(mData.get(position).getCoverimgurl())).into(holder.imgTeamAvatar);
        holder.btnVote.setTag(position);
        holder.btnVote.setOnClickListener(this);
        holder.rlContainer.setTag(position);
        holder.rlContainer.setOnClickListener(this);
        if (position % 2 == 0) {
            int i = Util.getRelativeWidth(24);
            convertView.setPadding(22, 0, 0, 0);
        } else {
            convertView.setPadding(0, 0, 22, 0);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_vote:
                position = (int) v.getTag();
                if (onVoteListener != null)
                    onVoteListener.onVote(position);
                break;
            case R.id.rl_container:
                position = (int) v.getTag();
                if (onToTeamDetailListener != null) {
                    onToTeamDetailListener.onClick(position);
                }
                break;
        }
    }

    class ViewHolder {
        private TextView tvTeamNum;
        private TextView tvTeamName;
        private ImageView imgTeamAvatar;
        private TextView tvRanking;
        private TextView tvTicketNum;
        private Button btnVote;
        private AutoRelativeLayout rlContainer;
    }

    public interface OnVoteListener {
        void onVote(int position);
    }

    public interface OnToTeamDetailListener {
        void onClick(int position);
    }
}
