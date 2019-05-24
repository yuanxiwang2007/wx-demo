package com.estatesoft.ris.wx.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ProjectName: qiedoctor-worker
 * Description: 日期工具类 推荐使用java8日期API
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/2/7
 * Time: 上午10:36
 */
public class DateUtils {
    //日期格式
    public static final String DATE_SIMPLE_FORMAT_STRING = "yyyyMMdd";
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";

    //时间格式
    public static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_SIMPLE_FORMAT_STRING = "yyyyMMddHHmmss";

    public static final String DATEHOUR_FORMAT_STRING = "yyyyMMddHH";
    public static final String DATETIME_FORMAT_STRING_CN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_FORMAT_STRING_CN = "yyyy年MM月dd日";

    private static Calendar calendar = Calendar.getInstance();

    /**
     * 格式日期yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String parseDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
        return df.format(date);
    }

    /**
     * 格式日期yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String parseDateCN(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT_STRING_CN);
        return df.format(date);
    }

    /**
     * 格式化简单日期yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String parseSimleDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_SIMPLE_FORMAT_STRING);
        return df.format(date);
    }

    /**
     * 格式化时间yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String parseDateTime(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT_STRING);
        return df.format(date);
    }

    /**
     * 格式化时间yyyyMMddHHMMss
     *
     * @param date
     * @return
     */
    public static String parseSimpleDateTime(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_SIMPLE_FORMAT_STRING);
        return df.format(date);
    }

    /**
     * 将yyyy-MM-dd字符串格式化成时间
     *
     * @param dt
     * @return
     */
    public static Date toDate(String dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
        try {
            return format.parse(dt);
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 将yyyyMMddHHmmss字符串格式化成时间
     *
     * @param dt
     * @return
     */
    public static Date toSimpleDate(String dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_STRING);
        try {
            return format.parse(dt);
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss 字符串格式化成时间
     *
     * @param dt
     * @return
     */
    public static Date toDateTime(String dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_STRING);
        try {
            return format.parse(dt);
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * @param @param  timestamp
     * @param @return
     * @return Date
     * @throws
     * @Title: timestampToDate
     * @Description: 时间戳转Date(毫秒时间戳)
     */
    public static Date timestampToDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_STRING);
        try {
            return format.parse(format.format(timestamp));
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 时间戳到年-月-日
     *
     * @param timestamp
     * @param formatTemplate
     * @return
     */
    public static Date timestampToDate(Long timestamp, String formatTemplate) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatTemplate);
        try {
            return format.parse(format.format(timestamp));
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 将yyyyMMddHHmmss字符串格式化成时间
     *
     * @param dt
     * @return
     */
    public static Date toSimpleDateTime(String dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_SIMPLE_FORMAT_STRING);
        try {
            return format.parse(dt);
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 获取当天的00:00:00
     *
     * @return
     */
    public static Date getTodayStart() {
        return getDateStart(new Date());
    }

    /**
     * 获取当天的00:00:00的时间戳
     *
     * @return
     */
    public static long getTodayStartTime() {
        return getTodayStart().getTime();
    }


    /**
     * 获取当天的23:59:59
     *
     * @return
     */
    public static Date getTodayEnd() {
        return getDateEnd(new Date());
    }


    /**
     * 获取当天的23:59:59的时间戳
     *
     * @return
     */
    public static long getTodayEndTime() {
        return getTodayEnd().getTime();
    }

    /**
     * 获取指定日期的00:00:00
     *
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * @param format
     * @return
     */
    public static String timeStamp2Date(Long time, String format) {
        if (time == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 获取指定日期的23:59:59
     *
     * @return
     */
    public static Date getDateEnd(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);

        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return (Date) currentDate.getTime().clone();

    }

    /**
     * 日期计算增加天数
     *
     * @param beginDate
     * @param days
     * @return
     */
    public static Date addDay(Date beginDate, int days) {
        Date date = beginDate;
        if (days != 0) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, days);
                date = calendar.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 日期计算增加月数
     *
     * @param beginDate
     * @param months
     * @return
     */
    public static Date addMonth(Date beginDate, int months) {
        Date date = beginDate;
        if (months != 0) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, months);
                date = calendar.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 日期计算增加秒数
     *
     * @param beginDate
     * @param seconds
     * @return
     */
    public static Date addSecond(Date beginDate, int seconds) {
        Date date = beginDate;
        if (seconds != 0) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.SECOND, seconds);
                date = calendar.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date getPreHour(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return calendar.getTime();
    }

    public static Date getNowHour(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 当前2017090912
     *
     * @param prehour
     * @return
     */
    public static Long getDayHour(Date prehour) {

        SimpleDateFormat format = new SimpleDateFormat(DATEHOUR_FORMAT_STRING);
        String hour = format.format(prehour);
        return Long.valueOf(hour);
    }

    /**
     * 修改小时
     *
     * @param prehour
     * @return
     */
    public static Long getDayHour(Date prehour, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prehour);
        calendar.add(Calendar.DAY_OF_MONTH, day);

        return getDayHour(calendar.getTime());
    }

    /**
     * 当前时间的小时
     *
     * @param prehour
     * @return
     */
    public static Integer getTimeHour(Date prehour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prehour);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前日期的毫秒值
     *
     * @return
     */
    public static Long getNowDateLong() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();

    }

    public static Date toUTCDateTime(String dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(dt);
        } catch (Exception e) {
            throw new RuntimeException("格式化时间错误");
        }
    }

    /**
     * 获取日期天integer
     *
     * @param createdTime
     * @return
     */
    public static Integer parseIntegerTime(Long createdTime) {
        String date = parseSimleDate(new Date(createdTime));

        return Integer.valueOf(date);
    }

    /**
     * 获取年
     *
     * @param createdTime
     * @return
     */
    public static Integer getYear(Long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @param createdTime
     * @return
     */
    public static Integer getMonth(Long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @param createdTime
     * @return
     */
    public static Integer getDay(Long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取周 获取周几
     *
     * @param date
     * @return
     */
    public static Integer getWeek(Date date) {
        //Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     * 获取两个间隔时间之间的日期列表
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }


    /**
     * 获取本月第一天的时间戳
     *
     * @throws ParseException
     */
    public static long getFirstDayOfCurrentMonth() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = new SimpleDateFormat("yyyy/MM/dd 00:00:00").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date firstDate = null;
        try {
            firstDate = sdf.parse(firstDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long firstDateTime = firstDate.getTime();
        return firstDateTime;

    }

    /**
     * 获取指定月份的第一天的时间戳
     *
     * @throws ParseException
     */
    public static long getFirstDayOfSpecifiedDate(Long timestamp) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = new SimpleDateFormat("yyyy/MM/dd 00:00:00").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date firstDate = null;
        try {
            firstDate = sdf.parse(firstDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long firstDateTime = firstDate.getTime();
        return firstDateTime;

    }

    /**
     * 获取本月第一天的时间
     *
     * @throws ParseException
     */
    public static Date getFirstDateOfCurrentMonth() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date firstDate = null;
        try {
            firstDate = sdf.parse(firstDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDate;

    }

    /**
     * 获取本月最后一天的时间戳
     *
     * @return
     * @throws ParseException
     */
    public static long getLastDayOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        String lastDay = new SimpleDateFormat("yyyy/MM/dd 23:59:59").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date lastDate = null;
        try {
            lastDate = sdf.parse(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long lastDateTime = lastDate.getTime();

        return lastDateTime;

    }

    /**
     * 获取指定时间的最后一天的时间戳
     *
     * @return
     * @throws ParseException
     */
    public static long getLastDayOfSpecifiedDate(Long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        String lastDay = new SimpleDateFormat("yyyy/MM/dd 23:59:59").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date lastDate = null;
        try {
            lastDate = sdf.parse(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long lastDateTime = lastDate.getTime();

        return lastDateTime;

    }

    /**
     * 获取本月最后一天的时间
     *
     * @return
     * @throws ParseException
     */
    public static Date getLastDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        String lastDay = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastDate = null;
        try {
            lastDate = sdf.parse(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastDate;
    }


    /**
     * 获取系统的年份
     *
     * @return
     *//**/
    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }


    /**
     * 根据日期取得星期几
     *
     * @param date
     * @return
     */
    public static String getWeekStr(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 根据日期取得星期几-推荐
     * 注：格式化字符串存在区分大小写
     * 对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
     * yyyy代表年份，如“2010”；dd代表天，如“25”
     */
    public static String getWe2k(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }

    /*public static void main(String[] args) {
        Date todayStart = getTodayStart();
        System.out.println(todayStart.getTime());
        String s = DateUtils.parseDate(todayStart);
        System.out.println(s);



        Date date = DateUtils.timestampToDate(1524758400000L, DATE_FORMAT_STRING);
        String sss = DateUtils.parseDate(date);
        int i = todayStart.compareTo(date);
        System.out.println(sss);
        System.out.println(i);

        System.out.println(getDateStart2(new Date()).getTime());

        System.out.println(getTodayEndTime());

    }*/

    public static Date getDateStart2(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
     /*   currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);*/
        return currentDate.getTime();
    }

    public static Integer getAge(Date birthDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        Integer yearBirth = calendar.get(Calendar.YEAR);
        Integer yearEnd = calendarEnd.get(Calendar.YEAR);

        Integer age = yearEnd - yearBirth;
        Integer monthBirth = calendar.get(Calendar.MONTH);
        Integer monthEnd = calendarEnd.get(Calendar.MONTH);
        Integer dayBirth = calendar.get(Calendar.DAY_OF_MONTH);
        Integer dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
        if (monthEnd < monthBirth) {
            age = age - 1;
        } else if (monthEnd == monthBirth && dayEnd < dayBirth) {
            age = age - 1;
        }
        return age;
    }

    public static Integer getAge(Date birthDate) {
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        Integer yearBirth = calendar.get(Calendar.YEAR);
        Integer yearEnd = calendarEnd.get(Calendar.YEAR);

        Integer age = yearEnd - yearBirth;
        Integer monthBirth = calendar.get(Calendar.MONTH);
        Integer monthEnd = calendarEnd.get(Calendar.MONTH);
        Integer dayBirth = calendar.get(Calendar.DAY_OF_MONTH);
        Integer dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
        if (monthEnd < monthBirth) {
            age = age - 1;
        } else if (monthEnd == monthBirth && dayEnd < dayBirth) {
            age = age - 1;
        }
        return age;
    }
}
