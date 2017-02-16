package com.xiaojia.daniujia.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.domain.resp.GraphicConsultHistoryVo.GraphicConsultHistItem;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    public static final int TODAY = 0;// 今天
    public static final int YESTERDAY = -1;// 昨天
    public static final int BEFORE_Y = -2;// 前天

    public static String formatWorkExpDate(long seconds) {
        if (seconds == 0) {
            return SysUtil.getString(R.string.so_far);// 至今
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM");
        return df.format(new Date(seconds * 1000));
    }

    public static String formatQuestionDate(long seconds) {
        DateFormat df = new SimpleDateFormat("MM-dd hh:mm");
        return df.format(new Date(seconds * 1000));
    }

    public static String formatQuestionDetailDate(long seconds) {
        DateFormat df = new SimpleDateFormat("yy/MM/dd hh:mm");
        return df.format(new Date(seconds * 1000));
    }

    public static String formatOrderDate(long seconds) {
        DateFormat df = new SimpleDateFormat("MM-dd hh:mm");
        return df.format(new Date(seconds * 1000));
    }

    public static String formatCouponDate(long seconds) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(seconds * 1000));
    }

    public static long formatDay(long dayTimeInMillis) {
        Date now = new Date();
        Date minToday = new Date(now.getYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        Date maxToday = new Date(now.getYear(), now.getMonth(), now.getDate(), 23, 59, 59);
        long minTodayInMillis = minToday.getTime();
        long maxTodayInMillis = maxToday.getTime();
        long minYesterdayInMillis = minTodayInMillis - 24 * 60 * 60 * 1000;
        long maxYesterdayInMillis = maxTodayInMillis - 24 * 60 * 60 * 1000;
        long minBeforeInmillis = minYesterdayInMillis - 24 * 60 * 60 * 1000;
        long maxBeforeInmillis = maxYesterdayInMillis - 24 * 60 * 60 * 1000;
        if (dayTimeInMillis >= minTodayInMillis && dayTimeInMillis <= maxTodayInMillis) {
            return TODAY;
        } else if (dayTimeInMillis >= minYesterdayInMillis && dayTimeInMillis <= maxYesterdayInMillis) {
            return YESTERDAY;
        } else if (dayTimeInMillis >= minBeforeInmillis && dayTimeInMillis <= maxBeforeInmillis) {
            return BEFORE_Y;
        } else {
            return dayTimeInMillis;// 更早
        }
    }

    public static String getTimeStr(long timeInSeconds) {
        String timeStr = null;
        DateFormat df = new SimpleDateFormat("HH:mm");
        long formattedDay = DateUtil.formatDay(timeInSeconds * 1000);
        Date date = new Date(timeInSeconds * 1000);
        if (formattedDay == TODAY) {
            timeStr = App.get().getString(R.string.today) + df.format(date);
        } else if (formattedDay == YESTERDAY) {
            timeStr = App.get().getString(R.string.yesterday) + df.format(date);
        } else if (formattedDay == BEFORE_Y) {
            timeStr = App.get().getString(R.string.the_day_before_yesterday) + df.format(date);
        } else {
            timeStr = formatOrderDate(timeInSeconds);
        }
        return timeStr;
    }

    private static final long TIME_TAG_INTERVAL = 1 * 60 * 1000;

    public static void setChatHistTime(TextView timeTv, List<GraphicConsultHistItem> chatContents, int position) {
        GraphicConsultHistItem chatContent = chatContents.get(position);
        long time = chatContent.sendtime;
        String timeStr = getTimeStr(time);
        if (position == 0) {// 最顶部的一条
            timeTv.setText(timeStr);
        } else {
            GraphicConsultHistItem preChatContent = chatContents.get(position - 1);// 上一条记录
            long diff = Math.abs(time - preChatContent.sendtime);
            if (diff >= TIME_TAG_INTERVAL) {
                timeTv.setText(timeStr);
            } else {
                timeTv.setText("");
            }
        }
    }

    public static String formatServiceTime(int timeInMinutes) {
        float timeInHours = timeInMinutes / 60f;
        String result = String.format("%.1f", timeInHours);
        if (result.contains(".0")) {
            return result.substring(0, result.indexOf("."));
        }
        return result;
    }

    public static String formatServiceTime2(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        if (hours == 0) {
            return timeInMinutes + "分钟";
        } else if (minutes == 0) {
            return hours + "小时";
        } else {
            return hours + "小时" + minutes + "分钟";
        }
    }

    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        String flag;
        if (time1 < time2) {
            diff = time2 - time1;
            flag = "前";
        } else {
            diff = time1 - time2;
            flag = "后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天" + flag;
        if (hour != 0) return hour + "小时" + flag;
        if (min != 0) return min + "分钟" + flag;
        return "刚刚";
    }

    public static int getDistanceDayCounts(long time1, long time2) {
        int day;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            return 0;
        }
        day = (int)(diff / (24 * 60 * 60 * 1000));
        if (day != 0)
            return day;
        return 0;
    }

}
