package com.xiaojia.daniujia.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 * @author wangqi 2015-11-13 (add function)
 */
public class TimeUtils {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_TIME_24 = new SimpleDateFormat(
			"HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH = new SimpleDateFormat(
			"yyyy年-MM月", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH_ZERO = new SimpleDateFormat(
			"yyyy-MM", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_ORDER = new SimpleDateFormat(
			"yyyy/MM/dd", Locale.getDefault());
	public static final SimpleDateFormat DATE_FORMAT_CAREER = new SimpleDateFormat(
			"yyyy/MM", Locale.getDefault());
	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}


	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	public static int getHour(String time) {
		String hour_cal = time.subSequence(11, 13).toString();
		int hour;
		if (hour_cal.charAt(0) == '0')
			hour = Integer.parseInt(hour_cal.subSequence(1, 2).toString());
		else {
			hour = Integer.parseInt(hour_cal);
		}
		return hour;
	}

	public static int getMinute(String time) {
		String minute_cal = time.subSequence(14, 16).toString();
		int minute;
		if (minute_cal.charAt(0) == '0')
			minute = Integer.parseInt(minute_cal.subSequence(1, 2).toString());
		else {
			minute = Integer.parseInt(minute_cal);
		}
		return minute;
	}

	public static int getYear(String date) {
		String year_cal = date.subSequence(0, 4).toString();

		int year = Integer.parseInt(year_cal);
		return year;
	}

	public static int getMonth(String date) {
		String month_cal = date.subSequence(5, 7).toString();
		int month;
		if (month_cal.charAt(0) == '0')
			month = Integer.parseInt(month_cal.subSequence(1, 2).toString());
		else {
			month = Integer.parseInt(month_cal);
		}
		return month;
	}

	public static int getDay(String date) {
		String day_cal = date.subSequence(8, 10).toString();
		int day;
		if (day_cal.charAt(0) == '0')
			day = Integer.parseInt(day_cal.subSequence(1, 2).toString());
		else {
			day = Integer.parseInt(day_cal);
		}
		return day;
	}

	/**
	 * 获取当天0点的时间戳
	 * 
	 * @return
	 */
	public static long getTimesMorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static String getVoiceTime(long time){
		int min = (int) (time / 60);
		int second = (int) (time % 60);
		if (min == 1) {
			return "60'";
		}
		return (min > 0 ? min + "'" : "") + (second > 9 ? second + "" : "" + second) + "''";
	}

	public static String timeFormateShow(long dateTime, int type) {
		long iTime = getTimesMorning() / 1000 - dateTime / 1000;
		int iDay = (int) (iTime / 60 / 60 / 24);

		String ampm;
		String datetime = TimeUtils.getTime(dateTime);
		int hour = getHour(datetime);
		if ((hour >= 0) && (hour < 5)) {
			ampm = "凌晨";
		} else {
			if ((hour >= 5) && (hour < 12)) {
				ampm = "上午";
			} else {
				if ((hour > 12) && (hour < 19)) {
					hour %= 12;
					ampm = "下午";
				} else {
					if (hour == 12) {
						ampm = "中午";
					} else {
						hour %= 12;
						ampm = "晚上";
					}
				}
			}
		}
		String date;
		String time = hour + ":" + datetime.subSequence(14, 16).toString();
		switch (iDay) {
		case 0:
			date = ampm;
			return date + " " + time;
		case 1:
			date = "昨天";
			time = getTime(dateTime, DATE_FORMAT_TIME_24);
			break;
		case 2:
			date = "前天";
			time = getTime(dateTime, DATE_FORMAT_TIME_24);
			break;
		default:
			date = getYear(datetime) + "年" + getMonth(datetime) + "月"
					+ getDay(datetime) + "日";
			time = getTime(dateTime, DATE_FORMAT_TIME_24);
			break;
		}
		if (type == 0) {
			return date;
		}
		return date + " " + time;
	}

	public static String second2TimeStr(int second) {
		StringBuffer sb = new StringBuffer();
		if (second < 60) {
			sb.append("00:");
			if (second < 10) {
				sb.append("0").append(second);
			} else {
				sb.append(second);
			}
		} else if (second >= 60 && second < 3600) {
			int min = second / 60;
			int sec = second % 60;
			if (min < 10) {
				sb.append("0");
			}
			sb.append(min).append(":");
			if (sec < 10) {
				sb.append("0");
			}
			sb.append(sec);
		} else {
			int hour = second / 60 / 60;
			int min = second % 3600 / 60;
			int sec = second % 3600 % 60;
			if (hour < 10) {
				sb.append(0);
			}
			sb.append(hour).append(":");
			if (min < 10) {
				sb.append("0");
			}
			sb.append(min).append(":");
			if (sec < 10) {
				sb.append("0");
			}
			sb.append(sec);
		}
		return sb.toString();
	}

	public static String getCareerDuration(long begin, long end) {

		long space = end - begin;
		int year = new Long(space / (60 * 60 * 24 * 365)).intValue();
		int month = new Long(((space / (60 * 60 * 24)) % 365) / 30).intValue();
		if (year <= 0) {
			return month + "个月";
		} else {
			if (month == 0) {
				return year + "年";
			} else {
				return year + "年" + month + "个月";
			}
		}
	}

	public static String getJobTime(String time) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		String sd = sdf.format(new Date(Long.parseLong(time)));
		return sd;
	}

	public static String calculateWorkAge(long time_To,long time_From) {
		long mSecond = time_From - time_To;
		int TotalMonth = (int) (mSecond / 60 / 60 / 24 / 30);
		int year = TotalMonth / 12;
		int month = TotalMonth % 12;
		if (month < 6) {
			return year + "年工作经验";
		}else {
			return year + "年半工作经验";
		}
	}

	public static String calculateEduTime(long beginTime, long endTime) {
		String timeFrom = getTime(beginTime * 1000, DATE_FORMAT_YEAR_MONTH);
		String timeTo = getTime(endTime * 1000, DATE_FORMAT_YEAR_MONTH);
		return timeFrom + "一" + timeTo;
	}

	public static int date2TimeStamp(String date_str,SimpleDateFormat format){
		try {
			return (int)(format.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}