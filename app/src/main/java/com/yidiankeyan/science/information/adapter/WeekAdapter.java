package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.TheNewTodayAudioActivity;

import java.util.Calendar;
import java.util.Date;
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
 * Created by nby on 2017/7/7.
 * 作用：
 */

public class WeekAdapter extends BaseAdapter {

    private List<Long> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    private int selectPosition = -1;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public WeekAdapter(List<Long> data, Context context) {
        this.mData = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_calendar_grid, null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.item_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(mData.get(position)));
        viewHolder.date.setText(cal.get(Calendar.DAY_OF_MONTH) + "");

        if (mData.get(position) > System.currentTimeMillis()) {
            //将来的日期, 不能操作
            viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
            convertView.setEnabled(false);
        } else {
            //可操作的日期
            if (((TheNewTodayAudioActivity) mContext).signList.contains(mData.get(position))) {
                //已签到
                viewHolder.date.setTextColor(Color.WHITE);
                viewHolder.date.setBackground(mContext.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
            } else {
                //未签到
                if ((selectPosition == -1) && System.currentTimeMillis() - mData.get(position) <= 24 * 60 * 60 * 1000) {
                    //初始状态
                    viewHolder.date.setTextColor(Color.parseColor("#ffffff"));
//                    viewHolder.date.setText("");
                    viewHolder.date.setBackground(mContext.getResources().getDrawable(R.drawable.shape_today_date));
                } else if (selectPosition == position) {
                    //选中状态
                    viewHolder.date.setTextColor(Color.WHITE);
                    viewHolder.date.setBackground(mContext.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                } else {
                    //默认状态
                    if (System.currentTimeMillis() - mData.get(position) <= 24 * 60 * 60 * 1000) {
                        viewHolder.date.setTextColor(Color.parseColor("#f1312e"));
                        viewHolder.date.setBackground(mContext.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                    } else {
                        viewHolder.date.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        viewHolder.date.setTextColor(Color.BLACK);
                    }
                }
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView date;
    }
}
