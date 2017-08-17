package com.z7dream.android_filemanager.tool;

import android.os.Build;
import android.text.TextUtils;

import com.z7dream.android_filemanager_lib.tool.Utils;
import com.z7dream.android_filemanager.R;
import com.z7dream.android_filemanager.base.mvp.BaseAppli;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yangzonghui on 2015/9/24.
 */
public class DateUtils {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    public static Calendar calendar = Calendar.getInstance();

    public static Locale getLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = BaseAppli.getContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = BaseAppli.getContext().getResources().getConfiguration().locale;
        }
        return locale;
    }

    /**
     * 获取本周的起始时间
     *
     * @return
     */
    public static long getTimeOfWeekStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        return ca.getTimeInMillis();
    }

    /**
     * 获取本月起始时间
     *
     * @return
     */
    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    /**
     * 获取今年起始时间
     *
     * @return
     */
    public static long getTimeOfYearStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_YEAR, 1);
        return ca.getTimeInMillis();
    }

    /**
     * @return yyyy-mm-dd
     */
    public static String getDate() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * @return yyyy-mm-dd HH:MM:SS
     */
    public static String getDateHMS() {
        return getYear() + "-" + getMonth() + "-" + getDay() + " " + get24Hour() + ":" + getMinute() + ":" + getSecond();
    }

    /**
     * 11月15日 07:09 formate yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String formatDataChineseToStr(String datas) {
        if (!TextUtils.isEmpty(datas)) {
            datas = datas.replace("月", "-");
            datas = datas.replace("日", "");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            datas = year + "-" + datas;
            return datas;
        }
        return datas;
    }

    /**
     * @param format
     * @return yyyy年MM月dd HH:mm
     * MM-dd HH:mm 2015-9-24
     */
    public static String getDate(String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format, getLocale());
        return simple.format(calendar.getTime());
    }

    /**
     * @return yyyy-MM-dd HH:mm
     */
    public static String getDateAndMinute(long date) {
        Date date1 = new Date(date);
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm", getLocale());
        return simple.format(date1);
    }

    public static String and7Day() {
        Date date = new Date(System.currentTimeMillis() + 86400000 * 7);
        SimpleDateFormat simple = new SimpleDateFormat("MM月dd日", getLocale());
        return simple.format(date);
    }

    public static String and7DeadLine() {
        Date date = new Date(System.currentTimeMillis() + 86400000 * 7);
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd", getLocale());
        return simple.format(date);
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getDay1() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd", getLocale());
        return simple.format(date);
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getFullDate() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getLocale());
        return simple.format(calendar.getTime());
    }

    public static String formatPatternToMilles(String value) {
        try {
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm", getLocale());
            Date date = simple.parse(value);
            return String.valueOf(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatRongyunData(String value) {
        try {
            SimpleDateFormat simple = new SimpleDateFormat("MM-dd HH:mm", getLocale());
            Date date = new Date();
            date.setTime(Long.parseLong(value));
            return simple.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(long value, String format) {
        try {
            SimpleDateFormat simple = new SimpleDateFormat(format, getLocale());
            Date date = new Date();
            date.setTime(value);
            return simple.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long formatPatternToMilles(String value, String pattern) {
        try {
            SimpleDateFormat simple = new SimpleDateFormat(pattern, getLocale());
            Date date = simple.parse(value);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static boolean isCurrentToday(String todoData) {
        try {
            String[] va = todoData.split("-");
            if (va.length > 2) {
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                return month == Integer.valueOf(va[1]) && day == Integer.valueOf(va[2]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 距离今天多久
     *
     * @param date
     * @return
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "分钟前";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_DAY * 3)
            return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月" + day + "天前"
                    + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
                    + "日";
        }

    }

    /**
     * 距离今天多久
     *
     * @param date1
     * @return
     */
    public static String fromToday(long date1) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(date1);
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时前";
        else if (ago <= ONE_DAY * 2)
            return "昨天";
        else if (ago <= ONE_DAY * 3)
            return "前天";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            return month + "个月";
        } else {
            long year = ago / ONE_YEAR;
            return year + "年前";
        }

    }


    /**
     * 是否为今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if (year1 == year2 && month1 == month2 && day1 == day2) {
            return true;
        }
        return false;
    }

    public static String fromdayReplace(long date1) {
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(date1);
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        SimpleDateFormat format;
        if (ago <= ONE_YEAR) {
            format = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            return format.format(date);
        }
        format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return format.format(date);
    }

    public static String fromday(long date1) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(date1);
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;

        SimpleDateFormat format;
        if (true) {
            if (ago <= ONE_YEAR) {
                format = new SimpleDateFormat("MM月dd日", Locale.CHINA);
                return format.format(date);
            }
            format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            return format.format(date);
        }


        if (isToday(date)) {
            return "今天";
        } else if (ago <= ONE_DAY * 1)
            return "昨天";
        else if (ago <= ONE_DAY * 2)
            return "前天";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            return month + "个月";
        } else {
            long year = ago / ONE_YEAR;
            return year + "年前";
        }

    }

    /**
     * 根据时间格式转化为long值时间
     *
     * @param strTime
     * @param formatType
     * @return
     */
    public static long getTime(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType, Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param
     * @return
     */
    public static String fromDeadline(long data) {

        long deadline = data / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
//        Log.e("second", (remain % ONE_DAY % ONE_HOUR % ONE_MINUTE) + "  " + remain+ "  "+ deadline +"  "+now);
        if (remain <= ONE_MINUTE) {
            return 0 + "天" + 0 + "小时" + 0 + "分钟" + remain + "秒";
        } else if (remain <= ONE_HOUR)
            return 0 + "天" + 0 + "小时" + remain / ONE_MINUTE + "分钟" + (remain % ONE_MINUTE) + "秒";
        else if (remain <= ONE_DAY)
            return 0 + "天" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟" + (remain % ONE_HOUR % ONE_MINUTE) + "秒";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            long scend = remain % ONE_DAY % ONE_HOUR % ONE_MINUTE;
            return day + "天" + hour + "小时" + minute + "分钟" + scend + "秒";
        }

    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }

    public static String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }

    public static String getDay() {
        return calendar.get(Calendar.DATE) + "";
    }

    public static String getDayNumber() {
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return calendar.get(Calendar.MINUTE) + "";
    }

    public static String getSecond() {
        return calendar.get(Calendar.SECOND) + "";
    }

    /**
     * 判断是当前日期之前还是之后
     *
     * @return 1:在当前日期之后
     */
    public static int compareNow(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date dt1 = df.parse(date);
            long a = dt1.getTime();
            long b = System.currentTimeMillis();
            if (dt1.getTime() > System.currentTimeMillis()) {
                return 1;  //日期在当前日期之后
            } else if (dt1.getTime() < System.currentTimeMillis()) {
                return -1;  //日期在当前日期之前
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * format "2016-12-14 23:14" to 23:14
     *
     * @return
     */
    public static String formatCalendarToTime(String date) {
        if (!TextUtils.isEmpty(date) && date.contains(" ")) {
            return date.substring(date.indexOf(" ") + 1);
        }
        return date;
    }


    /**
     * 时间long转换为时分秒
     *
     * @param data
     * @return
     */
    public static String long2Data(long data) {
        int second = (int) (data / 1000);
        if (second < 60) {
            if (second < 10) {

                return "00:0" + second;
            } else {
                return "00:" + second;
            }
        } else {
            int minute = second / 60;
            second = second % 60;
            if (minute < 10 && second < 10) {
                return "0" + minute + ":0" + second;
            } else if (minute < 10) {
                return "0" + minute + ":" + second;
            } else if (second < 10) {
                return minute + ":0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    public static String long2TimeStr(long data) {
        long minute = data / (60 * 1000L);
        if (minute < 60) {
            return BaseAppli.getContext().getString(R.string.n_min, minute);
        } else if (minute == 60) {
            return BaseAppli.getContext().getString(R.string.n_hour, 1);
        } else {
            long hour = minute / 60;
            minute = minute % 60;
            if (minute > 0) {
                return BaseAppli.getContext().getString(R.string.n_hour_m_min, hour, minute);
            } else {
                return BaseAppli.getContext().getString(R.string.n_hour, hour);
            }
        }
    }


    public static String getChineseTime(long times) {
        Calendar local = Calendar.getInstance();
        Calendar server = Calendar.getInstance();
        server.setTimeInMillis(times);

        long interval = local.getTimeInMillis() - server.getTimeInMillis();

        Date resultDate = new Date(times);
        String yearFormat = "yyyy", previousYearFormat = "yyyy-MM-dd HH:mm", nowYearFormat = "MM-dd HH:mm";
        String result = new SimpleDateFormat(previousYearFormat, Locale.CHINA).format(resultDate);//默认为往年格式

        String sysYear = new SimpleDateFormat(yearFormat, Locale.CHINA).format(new Date(System.currentTimeMillis()));
        String resultYear = new SimpleDateFormat(yearFormat, Locale.CHINA).format(resultDate);
        boolean isThisYear = TextUtils.equals(sysYear, resultYear);
        long minute = 60 * 1000;
        long hour = 60 * minute;
        long day = 24 * hour;

        if (interval >= 0) {
            if (interval <= minute) {
                result = BaseAppli.getContext().getString(R.string.time_moment_str);
            } else if (interval > minute && interval <= hour) {
                result = (interval / minute) + BaseAppli.getContext().getString(R.string.time_min_ago_str);
            } else if (interval > hour && interval <= day) {
                result = (interval / hour) + BaseAppli.getContext().getString(R.string.time_hour_ago_str);
            }
//            else if (interval > day && interval <= 2 * day) {
//                data = "昨天 " + new SimpleDateFormat(timeFromat).format(resultDate);
//            }
            else if (isThisYear) {
                result = new SimpleDateFormat(nowYearFormat, Locale.CHINA).format(resultDate);
            } else {
                result = new SimpleDateFormat(previousYearFormat, Locale.CHINA).format(resultDate);
            }
        }
        return result;
    }


    /**
     * 生成消息的时间
     *
     * @param receiveTime
     * @return
     */
    public static String getMessageReceiveTime(long receiveTime) {
        Calendar local = Calendar.getInstance();
        local.setTimeInMillis(System.currentTimeMillis());

        Calendar receive = Calendar.getInstance();
        receive.setTimeInMillis(receiveTime);

        Date receiveDate = new Date(receiveTime);

        if (receive.get(Calendar.YEAR) == local.get(Calendar.YEAR)) {
            int diffDay = receive.get(Calendar.DAY_OF_YEAR) - local.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {//判断是否为今天
                return thisDayFormate.get().format(receiveDate);
            }/* else if (diffDay == -1) {//判断是否为昨天
                return Appli.getContext().getString(R.string.yestday_str);
            } else if (receive.get(Calendar.WEEK_OF_YEAR) == local.get(Calendar.WEEK_OF_YEAR)) {//判断是否为本周
                return thisWeekFormate.get().format(receiveDate);
            }*/ else {//判断为今年
                return yearFormate.get().format(receiveDate);
            }
        } else {
            return lastYearFormate.get().format(receiveDate);
        }
    }

//    public static String createDivierTime(long receiveTime) {
//        Calendar local = Calendar.getInstance();
//        local.setTimeInMillis(System.currentTimeMillis());
//
//        Calendar receive = Calendar.getInstance();
//        receive.setTimeInMillis(receiveTime);
//
//        Date receiveDate = new Date(receiveTime);
//
//        if (receive.get(Calendar.YEAR) == local.get(Calendar.YEAR)) {
//            int diffDay = receive.get(Calendar.DAY_OF_YEAR) - local.get(Calendar.DAY_OF_YEAR);
//
//            if (diffDay == 0) {//判断是否为今天
//                return thisDayFormate.get().format(receiveDate);
//            } else if (diffDay == -1) {//判断是否为昨天
//                return Appli.getContext().getString(R.string.yestday_2_str, thisDayFormate.get().format(receiveDate));
//            }
//        }
//        return yearFormateTime.get().format(receiveDate);
//    }

    //今天
    private final static ThreadLocal<SimpleDateFormat> thisDayFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
    };

    //本周
    private final static ThreadLocal<SimpleDateFormat> thisWeekFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("E", Locale.getDefault());
        }
    };

    //年
    private final static ThreadLocal<SimpleDateFormat> yearFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Utils.isSystemZh(BaseAppli.getContext()) ? "MM-dd HH:mm" : "MM/dd HH:mm", Locale.getDefault());
        }
    };

    //今年以前
    private final static ThreadLocal<SimpleDateFormat> lastYearFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Utils.isSystemZh(BaseAppli.getContext()) ? "yyyy-MM-dd HH:mm" : "MM/dd/yyyy HH:mm", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> yearFormateTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Utils.isSystemZh(BaseAppli.getContext()) ? "yyyy年MM月dd日 HH:mm" : "MM/dd/yyyy HH:mm", Locale.getDefault());
        }
    };
}
