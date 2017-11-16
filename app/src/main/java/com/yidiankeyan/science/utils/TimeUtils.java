package com.yidiankeyan.science.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Created by nby on 2017/1/4.
 * 作用：
 */

public class TimeUtils {
    /**
     * 毫秒转分秒
     *
     * @param time 毫秒
     * @return
     */
    public static String getTimeFromLong(long time) {
        if (time <= 0) {
            return "00:00";
        }
        long secondnd = (time / 1000) / 60;
        long million = (time / 1000) % 60;
//        String f = String.valueOf(secondnd);
        String f = secondnd >= 10 ? String.valueOf(secondnd) : "0" + String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
        return f + ":" + m;
    }

    /**
     * 毫秒转分秒
     *
     * @param time 毫秒
     * @return
     */
    public static String getTimeFromInt(int time) {
        if (time <= 0) {
            return "00:00";
        }
        int secondnd = (time / 1000) / 60;
        int million = (time / 1000) % 60;
//        String f = String.valueOf(secondnd);
        String f = secondnd >= 10 ? String.valueOf(secondnd) : "0" + String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
        return f + ":" + m;
    }

    /*
* 毫秒转化
*/
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        return strMinute + "分" + strSecond + "秒";
    }

    /**
     * 格式化科答的回答时长
     *
     * @param second 秒
     * @return 实例：02:56
     */
    public static String getAnswerLength(long second) {
        long minute = second / 60;
        String m = minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute);
        String s = (second % 60) >= 10 ? String.valueOf((second % 60)) : "0" + String.valueOf((second % 60));
        return m + ":" + s;
    }

    /**
     * 格式化科答的回答时长
     *
     * @param second 秒
     * @return 实例：02:56
     */
    public static String getAnswerLength(int second) {
        int minute = second / 60;
        String m = minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute);
        String s = (second % 60) >= 10 ? String.valueOf((second % 60)) : "0" + String.valueOf((second % 60));
        return m + ":" + s;
    }

    //专访时间
    public static String getInterviewTime(int second) {
        int minute = second / 60;
        String m = minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute);
        String s = (second % 60) >= 10 ? String.valueOf((second % 60)) : "0" + String.valueOf((second % 60));
        return m + "'" + s + "''";
    }

    //期刊
    public static String getMagazineLength(int second) {
        int minute = second / 60;
        String m = minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute);
        String s = (second % 60) >= 10 ? String.valueOf((second % 60)) : "0" + String.valueOf((second % 60));
        return m + "分" + s + "秒";
    }

    /**
     * 获取问题创建时间到现在时间过了多久
     *
     * @param millisecond 问题创建时间
     * @return
     */
    public static String questionCreateDuration(long millisecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millisecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0)
            return formatDate(millisecond);
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 0)
            return minute + "分钟前";
        long second = duration / 1000;
        if (second == 0)
            return "1秒前";
        else {
            if (second > 1)
                return second + "秒前";
            else
                return "刚刚";
        }
    }

    /**
     * @param millisecond
     * @return 距离活动开始时间
     */
    public static String activityStartDistance(long millisecond) {
        long currentTime = (long) System.currentTimeMillis();
        if (currentTime >= millisecond)
            return "进行中";
        long duration = millisecond - currentTime;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0)
            return "倒计时" + day + "天";
        long hour = duration / (60 * 60 * 1000);
        long minute = (duration - (hour * (60 * 60 * 1000))) / (60 * 1000);
        return "倒计时" + hour + "时" + minute + "分";
    }

    /**
     * 格式化时间
     *
     * @param millisecond
     * @return
     */
    public static String formatDate2(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return simpleDateFormat.format(new Date(millisecond));
    }

    /**
     * 格式化时间
     *
     * @param millisecond 时间戳
     * @return yyyy-MM-dd
     */
    public static String formatDate(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current = new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis()));
        String time = simpleDateFormat.format(new Date(millisecond));
        if (TextUtils.equals(current, time.substring(0, 4))) {
            return new SimpleDateFormat("MM-dd").format(new Date(millisecond));
        }
        return time;
    }

    /**
     * 格式化时间
     *
     * @param millisecond
     * @return yyyy-MM
     */
    public static String formatData3(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date(millisecond));
    }

    /**
     * 格式化时间
     *
     * @param millisecond 时间戳
     * @return 时和分
     */
    public static String formatDateTime(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(new Date(millisecond));
    }

    /**
     * 格式化时间,带星期
     *
     * @param millisecond 时间戳
     * @return yyyy-MM-dd-EEEE
     */
    public static String formatDateWeek(long millisecond) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(millisecond);
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        return sdfInput.format(date) + " " + dayNames[dayOfWeek];
    }

    /**
     * 格式化时间为星期
     *
     * @param millisecond 时间戳
     */
    public static String formatWeek(long millisecond) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(millisecond);
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    public static boolean judgeYesterday(Date paramDate) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(date);
        //得到今天零时零分零秒这一时刻
        Date today = null;
        try {
            today = format.parse(todayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //比较
        if ((today.getTime() - paramDate.getTime()) > 0 && (today.getTime() - paramDate.getTime()) < 86400000) {
            return true;
        }
        return false;
    }

    public static String formatConversationTime(long millsecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millsecond;
        long day = duration / (24 * 60 * 60 * 1000);
        SimpleDateFormat sdf;
        if (judgeYesterday(new Date(millsecond))) {
            return "昨天";
        } else if (day > 1) {
            sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(millsecond);
        } else {
            sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(millsecond);
        }
    }

    public static String formatReadTime(long millsecond) {
        SimpleDateFormat sdf;
        String time;
        sdf = new SimpleDateFormat("MM-dd HH:mm");
        time = sdf.format(millsecond);
        return time;
    }

    public static String formatSeriesTime(int time) {
        if (time <= 0) {
            return "00:00";
        }
        int secondnd = time / 60;
        int million = time % 60;
//        String f = String.valueOf(secondnd);
        String f = secondnd >= 10 ? String.valueOf(secondnd) : "0" + String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
        return f + "分" + m + "秒";
    }

    /**
     * 今天（刚刚、10分钟前、1小时前、23小时前），昨天（昨天 04:32），前天（前天  04：32），其他时间格式（12-9 04:32）
     *
     * @param millsecond
     * @return
     */
    public static String formatKedaTime(long millsecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millsecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0) {
            SimpleDateFormat sdf;
            String time;
//            switch ((int) day) {
//                case 1:
//                    sdf = new SimpleDateFormat("HH:mm");
//                    time = "昨天" + sdf.format(millsecond);
//                    break;
//                case 2:
//                    sdf = new SimpleDateFormat("HH:mm");
//                    time = "前天" + sdf.format(millsecond);
//                    break;
//                default:
//                    sdf = new SimpleDateFormat("MM-dd HH:mm");
//                    time = sdf.format(millsecond);
//                    break;
//            }
//            sdf = new SimpleDateFormat("MM月dd");
            sdf = new SimpleDateFormat("HH:mm");
            time = sdf.format(millsecond);
            return time;
        }
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 9)
            return minute + "分钟前";
//        long second = duration / 1000;
//        if (second == 0)
//            return "1秒前";
//        else
        return "刚刚";
    }


    public static String timeDifference(long millsecond) {
        long currentTime = System.currentTimeMillis();
        long duration = millsecond - currentTime;
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return "剩余" + hour + "小时";
        long minute = duration / (60 * 1000);
        return "剩余" + minute + "分钟";
    }


}
