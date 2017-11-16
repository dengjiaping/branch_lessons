package com.yidiankeyan.science.view.calendar.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.TheNewTodayAudioActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.view.calendar.data.DateInfo;
import com.yidiankeyan.science.view.calendar.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * 日历gridview适配器
 */
public class CalendarAdapter extends BaseAdapter {

    private List<DateInfo> list = null;
    private Context context = null;
    private int selectedPosition = -1;
    Activity activity;
    private boolean showSelectedColor = true;
    private int todayPosition = -1;
    private boolean isCurrentMonth = true;
    private boolean isFirst;
    private int month;
    private int year;

    public void setShowSelectedColor(boolean showSelectedColor) {
        this.showSelectedColor = showSelectedColor;
    }

    public CalendarAdapter(Activity activity, List<DateInfo> list) {
        this.context = activity;
        this.list = list;
        this.activity = activity;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public int getTodayPosition() {
        return todayPosition;
    }

    public void setTodayPosition(int todayPosition) {
        this.todayPosition = todayPosition;
    }

    public List<DateInfo> getList() {
        return list;
    }

    public int getCount() {
        if (TextUtils.isEmpty(list.get(35).getNongliDate())) {
            return 35;
        } else {
            return list.size();
        }
    }

    public DateInfo getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置选中位置
     */
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    /**
     * 产生一个view
     */
    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup group) {
        //通过viewholder做一些优化
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_calendar_grid, null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.item_date);
//            viewHolder.nongliDate = (TextView) convertView.findViewById(R.id.item_nongli_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.date.setText(list.get(position).getDate() + "");
        String currentDate = String.format("%04d-%02d-%02d", year, month, list.get(position).getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //根据数据源设置单元格的字体颜色、背景等
        if (showSelectedColor && selectedPosition == position) {
            if (isFirst) {

                try {
                    if (list.get(position).isThisMonth() && todayPosition == position && ((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                        viewHolder.date.setTextColor(Color.WHITE);
//                        viewHolder.date.setPadding(0, 10, 10, 0);
                        viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
                    } else {
                        viewHolder.date.setTextColor(Color.parseColor("#ffffff"));
//                    viewHolder.date.setText("");
                        viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //当天
//                viewHolder.date.setTextColor(Color.WHITE);
//                viewHolder.date.setText("");
//                viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.icon_today_selected));
//                viewHolder.nongliDate.setTextColor(Color.WHITE);
//                viewHolder.date.setBackgroundColor(Color.parseColor("#000000"));
//                if(todayPosition >= position){
//                viewHolder.date.setTextColor(Color.parseColor("#f1312e"));
//                    viewHolder.date.setText("");
//                viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
//                }else{
//                    viewHolder.date.setTextColor(Color.WHITE);
//                    viewHolder.date.setText("");
//                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.icon_today_selected));
//                }

            } else try {
                if (todayPosition > position && !((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                    viewHolder.date.setTextColor(Color.WHITE);
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                } else if (month < TimeUtils.getCurrentMonth() && year == TimeUtils.getCurrentYear() && todayPosition < position && !((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                    viewHolder.date.setTextColor(Color.WHITE);
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                } else if (month < TimeUtils.getCurrentMonth() && year == TimeUtils.getCurrentYear() && todayPosition < position && ((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(2);
                    EventBus.getDefault().post(msg);
                    viewHolder.date.setTextColor(Color.WHITE);
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
                } else if (list.get(position).isThisMonth() && todayPosition > position && ((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    //被选中
                    viewHolder.date.setTextColor(Color.WHITE);
                    //                viewHolder.nongliDate.setTextColor(Color.WHITE);
                    //                viewHolder.date.setBackgroundColor(Color.parseColor("#FF6600"));
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
                    //                }
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(2);
                    EventBus.getDefault().post(msg);
                } else if (list.get(position).isThisMonth() && todayPosition == position && ((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    //被选中
                    viewHolder.date.setTextColor(Color.WHITE);
                    //                viewHolder.nongliDate.setTextColor(Color.WHITE);
                    //                viewHolder.date.setBackgroundColor(Color.parseColor("#FF6600"));
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
                    //                }
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(2);
                    EventBus.getDefault().post(msg);
                } else if (list.get(position).isThisMonth() && todayPosition == position && !((TheNewTodayAudioActivity) context).signList.contains(sdf.parse(currentDate).getTime())) {
                    //被选中
                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_TODAY_STATE);
                    msg.setArg1(1);
                    EventBus.getDefault().post(msg);
                    viewHolder.date.setTextColor(Color.WHITE);
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
//            String currentDate = String.format("%04d-%02d-%02d", year, month, list.get(position).getDate());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Long data = sdf.parse(currentDate).getTime();
                if (list.get(position).isThisMonth() && list.get(position).isThisMonth() && ((TheNewTodayAudioActivity) context).signList.contains(data)) {
                    viewHolder.date.setTextColor(Color.WHITE);
//                    viewHolder.date.setPadding(0, 10, 10, 0);
//                    viewHolder.date.setText("");
//                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.icon_today_selected));
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_sign_bg));
                } else {
                    viewHolder.date.setBackgroundColor(context.getResources().getColor(R.color.white));
                    viewHolder.date.setTextColor(Color.BLACK);
                    if (list.get(position).isHoliday()) {
                    } else if (list.get(position).isThisMonth() == false) {
                        viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
                    }
                    if (isCurrentMonth && todayPosition == position) {
                        viewHolder.date.setTextColor(Color.WHITE);
//                        viewHolder.date.setText("");
                        viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                    }


                }
            } catch (ParseException e) {
                e.printStackTrace();
                viewHolder.date.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.date.setTextColor(Color.BLACK);
                if (list.get(position).isHoliday()) {
                } else if (list.get(position).isThisMonth() == false) {
                    viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
                }
                if (isCurrentMonth && todayPosition == position) {
                    viewHolder.date.setTextColor(Color.WHITE);
//                    viewHolder.date.setText("");
                    viewHolder.date.setBackground(context.getResources().getDrawable(R.drawable.shape_today_date_normal_bg));
                }
            }
        }
        if (month == TimeUtils.getCurrentMonth() && year == TimeUtils.getCurrentYear() && todayPosition < position) {
            viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
            convertView.setEnabled(false);
        } else if (year == TimeUtils.getCurrentYear() && month > TimeUtils.getCurrentMonth()) {
            viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
            convertView.setEnabled(false);
        } else if (year > TimeUtils.getCurrentYear()) {
            viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
            convertView.setEnabled(false);
        }
//        if (list.get(position).getNongliDate().length() > 3)
//            viewHolder.nongliDate.setText(viewHolder.nongliDate.getText().toString().substring(0, 3) + "...");
//        LogUtils.e(group.getWidth() + "");
        return convertView;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    private class ViewHolder {
        TextView date;
//        TextView nongliDate;
    }

}
