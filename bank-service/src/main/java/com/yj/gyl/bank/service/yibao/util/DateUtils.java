package com.yj.gyl.bank.service.yibao.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_PATTERN_DETAIL = "yyyyMMdd";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATEDETAIL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DEFAULTDATEDETAILPATTERN = "yyyyMMddHHmmss";
    public static final String DEFAULTDATEDETAILPATTERN_SSSS = "yyyyMMddHHmmssSSSS";
    public static final String DEFAULT_MATH_PATTERN = "MM-dd";
    public static final String DEFAULT_DATE_DAY = "d";
    public static final String DEFAULT_DATE_HOUR = "h";
    public static final String DEFAULT_DATE_MINUTE = "m";
    public static final String DEFAULT_DATE_SECOUND = "s";
    public static final long MILLIS_A_DAY = 86400000L;
    private static Map<String, Object> parsers = new HashMap();

    public DateUtils() {
    }

    private static SimpleDateFormat getDateParser(String pattern) {
        Object parser = parsers.get(pattern);
        if(parser == null) {
            parser = new SimpleDateFormat(pattern);
            parsers.put(pattern, parser);
        }

        return (SimpleDateFormat)parser;
    }

    public static Date currentDate() {
        return new Date();
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String nowDate(String strFormat) {
        Date date = new Date();
        return getDateParser(strFormat).format(date);
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp((new Date()).getTime());
    }

    public static Date toDate(String dateString, String pattern) throws Exception {
        return getDateParser(pattern).parse(dateString);
    }

    public static Date toDate(String dateString) throws Exception {
        return getDateParser("yyyy-MM-dd").parse(dateString);
    }

    public static Date toDateTime(String dateString) throws Exception {
        return getDateParser("yyyy-MM-dd HH:mm:ss").parse(dateString);
    }

    public static String toDateString(Date date, String pattern) {
        return getDateParser(pattern).format(date);
    }

    public static String toDateString(Date date) {
        return getDateParser("yyyy-MM-dd").format(date);
    }

    public static String toDateTimeString(Date date) {
        return getDateParser("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date diffDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) - (long)day * 86400000L);
        return c.getTime();
    }

    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + (long)day * 86400000L);
        return c.getTime();
    }

    public static List<String> getBetweenDate(String d1, String d2) throws Exception {
        ArrayList list = new ArrayList();
        ArrayList dayList = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();
        gc1.setTime(sdf.parse(d1));
        gc2.setTime(sdf.parse(d2));

        do {
            GregorianCalendar gc3 = (GregorianCalendar)gc1.clone();
            list.add(gc3.getTime());
            gc1.add(5, 1);
        } while(!gc1.after(gc2));

        Iterator gc31 = list.iterator();

        while(gc31.hasNext()) {
            Date date = (Date)gc31.next();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayList.add(parseDateToStr(c.getTime(), "yyyy-MM-dd"));
        }

        return dayList;
    }

    public static String parseDateToStr(Date date, String pattern) {
        if(date == null) {
            return "";
        } else {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
    }

    public static Date parseStrToDate(String stringDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(stringDate);
        return date;
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar startdate = Calendar.getInstance();
        startdate.setTime(date);
        return startdate;
    }

    public static Date CalendarToDate(Calendar calendar) {
        Date date = calendar.getTime();
        return date;
    }

    public static Map<Integer, Integer> dateCompare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return calendarCompare(c1, c2);
    }

    public static Map<Integer, Integer> calendarCompare(Calendar c1, Calendar c2) {
        HashMap ret = new HashMap();
        int year = c1.get(1) - c2.get(1);
        int month = c1.get(2) - c2.get(2);
        int day = c1.get(5) - c2.get(5);
        int hour = c1.get(11) - c2.get(11);
        int min = c1.get(12) - c2.get(12);
        int second = c1.get(13) - c2.get(13);
        if(month < 0 && year > 0) {
            --year;
            month += 12;
        }

        if(day < 0 && month > 0) {
            --month;
            day = c2.getActualMaximum(5) - c2.get(5) + c1.get(5);
        }

        if(hour < 0 && day > 0) {
            --day;
            hour += 24;
        }

        if(min < 0 && hour > 0) {
            --hour;
            min += 60;
        }

        if(second < 0 && min > 0) {
            --min;
            second += 60;
        }

        ret.put(Integer.valueOf(1), Integer.valueOf(year));
        ret.put(Integer.valueOf(2), Integer.valueOf(month));
        ret.put(Integer.valueOf(5), Integer.valueOf(day));
        ret.put(Integer.valueOf(10), Integer.valueOf(hour));
        ret.put(Integer.valueOf(12), Integer.valueOf(min));
        ret.put(Integer.valueOf(13), Integer.valueOf(second));
        return ret;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static boolean compareToDays(Date firstDay, Date secoundDay) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strFirst = sdf.format(firstDay);
        String strSec = sdf.format(secoundDay);
        if(strFirst.equals(strSec)) {
            flag = true;
        }

        return flag;
    }

    public static Boolean checkHour(Date date, Integer hour) {
        Boolean flag = Boolean.valueOf(false);
        Calendar calendar = DateToCalendar(date);
        if(calendar.get(11) > hour.intValue()) {
            flag = Boolean.valueOf(true);
        }

        return flag;
    }

    public static Boolean checkHourFalse(Date date, Integer hour) {
        Boolean flag = Boolean.valueOf(true);
        Calendar calendar = DateToCalendar(date);
        if(calendar.get(11) < hour.intValue()) {
            flag = Boolean.valueOf(false);
        }

        return flag;
    }

    public static Date getBeforeDay(int beforeDay) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(5, -beforeDay);
        return calender.getTime();
    }

    public static Date getBeforeDayByDay(Date date, int beforeDay) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(5, -beforeDay);
        return calender.getTime();
    }

    public static Date getDayAfterByDay(Date date, int afterDay) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(5, afterDay);
        return calender.getTime();
    }

    public static Date getDayAfterByDayState(Date date, int afterDay) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(5, afterDay);
        calender.set(11, 23);
        calender.set(12, 59);
        calender.set(13, 59);
        calender.set(14, 1);
        return calender.getTime();
    }

    public static Date getDayAfterByDayEnd(Date date, int afterDay) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(5, afterDay);
        calender.set(11, 18);
        calender.set(12, 0);
        calender.set(13, 0);
        calender.set(14, 1);
        return calender.getTime();
    }

    public static Date getBeforeWeek(int beforeWeek) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(8, -beforeWeek);
        return calender.getTime();
    }

    public static Map<String, Date> getNowDays(Date day) {
        HashMap days = new HashMap();

        try {
            String str = parseDateToStr(day, "yyyy-MM-dd");
            String start = str + " 00:00:00";
            String end = str + " 23:59:59";
            Date startDate = toDateTime(start);
            Date endDate = toDateTime(end);
            days.put("startTime", startDate);
            days.put("endTime", endDate);
        } catch (Exception var7) {
            ;
        }

        return days;
    }

    public static Map<String, Date> getBetweenDays(Date day, int beforeDay) {
        HashMap days = new HashMap();

        try {
            String str = parseDateToStr(day, "yyyy-MM-dd");
            String end = str + " 23:59:59";
            Date weekDay = getBeforeDay(beforeDay);
            String weekStr = parseDateToStr(weekDay, "yyyy-MM-dd");
            String start = weekStr + " 00:00:00";
            Date startDate = toDateTime(start);
            Date endDate = toDateTime(end);
            days.put("startTime", startDate);
            days.put("endTime", endDate);
        } catch (Exception var10) {
            ;
        }

        return days;
    }

    public static long getDateDiffer(Date smaller, Date bigger, String parameter) {
        long time = 0L;

        try {
            long nd = 86400000L;
            long nh = 3600000L;
            long nm = 60000L;
            long ns = 1000L;
            long count = bigger.getTime() - smaller.getTime();
            if(count > 0L) {
                if(parameter.equals("d")) {
                    time = count / nd;
                } else if(parameter.equals("h")) {
                    time = count / nh;
                } else if(parameter.equals("m")) {
                    time = count / nm;
                } else {
                    time = count / ns;
                }
            }
        } catch (Exception var15) {
            ;
        }

        return time;
    }

    public boolean isWeekend(Date date) {
        boolean weekend = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(7) == 7 || cal.get(7) == 1) {
            weekend = true;
        }

        return weekend;
    }

    public static List<String> getDays(Date date, int before, String type) {
        ArrayList days = new ArrayList();
        String format = "MM-dd";
        if(type != null && !"".equals(type)) {
            format = type;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        for(int i = 0; i < before; ++i) {
            new Date();
            Date day = getBeforeDayByDay(date, i);
            days.add(sdf.format(day));
        }

        Collections.sort(days);
        return days;
    }

    public static List<String> getBetweenDayList(String startDay, String endDay) {
        ArrayList result = new ArrayList();

        try {
            Date smdate = parseStrToDate(startDay);
            Date bdate = parseStrToDate(endDay);
            int num = daysBetween(smdate, bdate);

            for(int i = 0; i <= num; ++i) {
                Calendar calender = Calendar.getInstance();
                calender.setTime(bdate);
                calender.add(5, -i);
                Date date = calender.getTime();
                String str = parseDateToStr(date, "MM-dd");
                result.add(str);
            }
        } catch (Exception var10) {
            ;
        }

        return result;
    }

    public static List<String> getMonthList(int before, Date date) {
        ArrayList list = new ArrayList();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        int month = calender.get(2) + 1;

        for(int i = before; i >= 0; --i) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(2, -i);
            int mon = c.get(2) + 1;
            int year = c.get(1);
            String str = "";
            if(mon < 10) {
                str = year + "-0" + mon;
            } else {
                str = year + "-" + mon;
            }

            list.add(str);
        }

        return list;
    }

    public static Map<String, Date> getConfirmDays(Date day, int before, String startTime, String endTime) {
        HashMap days = new HashMap();

        try {
            String str = parseDateToStr(day, "yyyy-MM-dd");
            String end = str + " " + endTime;
            Calendar calender = Calendar.getInstance();
            calender.setTime(day);
            calender.add(5, -before);
            Date date = calender.getTime();
            String str2 = parseDateToStr(date, "yyyy-MM-dd");
            String start = str2 + " " + startTime;
            Date startDate = toDateTime(start);
            Date endDate = toDateTime(end);
            days.put("startTime", startDate);
            days.put("endTime", endDate);
        } catch (Exception var13) {
            ;
        }

        return days;
    }

    public static String getDayOfWeek(Date day) {
        String week = "";
        Calendar calender = Calendar.getInstance();
        calender.setTime(day);
        int weeks = calender.get(7);
        if(weeks == 2) {
            week = "星期一";
        } else if(weeks == 3) {
            week = "星期二";
        } else if(weeks == 4) {
            week = "星期三";
        } else if(weeks == 5) {
            week = "星期四";
        } else if(weeks == 6) {
            week = "星期五";
        } else if(weeks == 7) {
            week = "星期六";
        } else if(weeks == 1) {
            week = "星期日";
        }

        return week;
    }

    public static int dayForWeek(Date day) throws Exception {
        new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        boolean dayForWeek = false;
        int dayForWeek1;
        if(c.get(7) == 1) {
            dayForWeek1 = 7;
        } else {
            dayForWeek1 = c.get(7) - 1;
        }

        return dayForWeek1;
    }

    public static boolean isBetweenTime(Date time, String startTime, String endTime) {
        boolean flag = false;

        try {
            String str = parseDateToStr(time, "yyyy-MM-dd");
            startTime = str + " " + startTime;
            endTime = str + " " + endTime;
            Date startDay = toDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date endDay = toDate(endTime, "yyyy-MM-dd HH:mm:ss");
            long lessTime = 0L;
            long moreTime = 0L;
            lessTime = time.getTime() - startDay.getTime();
            moreTime = endDay.getTime() - time.getTime();
            if(0L <= lessTime && moreTime >= 0L) {
                flag = true;
            }
        } catch (Exception var11) {
            ;
        }

        return flag;
    }

    public static boolean isBetweenTimeFlag(Date time, String startTime, String endTime) {
        boolean flag = false;

        try {
            String e = parseDateToStr(time, "yyyy-MM-dd");
            Date startDay = toDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date endDay = toDate(endTime, "yyyy-MM-dd HH:mm:ss");
            long lessTime = 0L;
            long moreTime = 0L;
            lessTime = time.getTime() - startDay.getTime();
            moreTime = endDay.getTime() - time.getTime();
            if(0L <= lessTime && moreTime >= 0L) {
                flag = true;
            }
        } catch (Exception var11) {
            System.out.println(var11);
        }

        return flag;
    }

    public Date getLastMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.add(2, -month);
        return c.getTime();
    }

    public static String getFlowTime() {
        return parseDateToStr(new Date(), "yyyyMMdd");
    }

    public static String getFlowTime(Date date) {
        return parseDateToStr(date, "yyyyMMdd");
    }

    public static String getFlowTimeDate() {
        return parseDateToStr(new Date(), "yyyyMMdd");
    }

    public static String getFlowTime_18() {
        return parseDateToStr(new Date(), "yyyyMMddHHmmssSSSS");
    }

    public static List<Date> getDate() {
        LinkedList list = new LinkedList();

        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.add(2, 0);
            c.set(5, 1);
            String first = e.format(c.getTime());
            first = first + " 00:00:00";
            Calendar ca = Calendar.getInstance();
            ca.set(5, ca.getActualMaximum(5));
            String last = e.format(ca.getTime());
            last = last + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date firstDay = sdf.parse(first);
            Date lastDay = sdf.parse(last);
            Calendar calender = Calendar.getInstance();
            calender.setTime(firstDay);
            calender.add(2, 2);
            calender.set(5, calender.getActualMaximum(5));
            String st = e.format(calender.getTime());
            st = st + " 23:59:59";
            Date validtime = sdf.parse(st);
            list.add(0, firstDay);
            list.add(1, lastDay);
            list.add(2, validtime);
        } catch (ParseException var12) {
            var12.printStackTrace();
        }

        return list;
    }

    public static Date getCouponValidTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(2, 0);
        c.set(5, 1);
        String first = format.format(c.getTime());
        first = first + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date firstDay = sdf.parse(first);
        byte effective = 2;
        Calendar calender = Calendar.getInstance();
        calender.setTime(firstDay);
        calender.add(2, effective);
        calender.set(5, calender.getActualMaximum(5));
        String st = format.format(calender.getTime());
        st = st + " 23:59:59";
        return sdf.parse(st);
    }

    public static void main(String[] arg) throws ParseException {
        System.out.println(toDateTimeString(getCouponValidTime()));
    }
}
