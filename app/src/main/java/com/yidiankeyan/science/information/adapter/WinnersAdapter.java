package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.WinnerBean;

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
 * Created by nby on 2016/11/9.
 * 作用：
 */
public class WinnersAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<WinnerBean> winnerNames;

    public WinnersAdapter(List<WinnerBean> winnerNames, Context context) {
        this.winnerNames = winnerNames;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (winnerNames == null || winnerNames.size() == 0)
            return 0;
        return 65535;
    }

    @Override
    public Object getItem(int position) {
        return winnerNames.get(position % winnerNames.size());
    }

    @Override
    public long getItemId(int position) {
        return position % winnerNames.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_winner_info, parent, false);
            holder = new ViewHolder();
            holder.tvWinnerName = (TextView) convertView.findViewById(R.id.tv_winner_name);
            holder.tvWinnerPhone = (TextView) convertView.findViewById(R.id.tv_winner_phone);
            holder.tvPrize = (TextView) convertView.findViewById(R.id.tv_prize);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvWinnerName.setText(winnerNames.get(position % winnerNames.size()).getName());
        holder.tvWinnerPhone.setText(winnerNames.get(position % winnerNames.size()).getMobile());
        holder.tvPrize.setText(winnerNames.get(position % winnerNames.size()).getPrizename());
        return convertView;
    }

    class ViewHolder {
        private TextView tvWinnerName;
        private TextView tvWinnerPhone;
        private TextView tvPrize;
    }

}
