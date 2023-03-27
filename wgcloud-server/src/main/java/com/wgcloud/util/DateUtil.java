package com.wgcloud.util;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
   private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
   private static final String DATE_PATTERN = "yyyy-MM-dd";
   private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
   private static final String DATETIME_PATTERN_ZH = "yyyy年MM月dd日HH:mm:ss";

   public static Timestamp getNowTime() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Timestamp nowTime = Timestamp.valueOf(dateFormat.format(new Date()));
      return nowTime;
   }

   public static String getCurrentDateTime() {
      return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
   }

   public static String getCurrentDate() {
      return getCurrentDateTime("yyyy-MM-dd");
   }

   public static String getCurrentDateTime(String pattern) {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      return sdf.format(cal.getTime());
   }

   public static Date getDate(String dateStr) throws ParseException {
      return getDate(dateStr, "yyyy-MM-dd HH:mm:ss");
   }

   public static Date getDate(String dateStr, String pattern) throws ParseException {
      Date date = null;
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      date = dateFormat.parse(dateStr);
      return date;
   }

   public static String getDateString(Date date) {
      return getString(date, "yyyy-MM-dd");
   }

   public static String getDateTimeString(Date date) {
      return getString(date, "yyyy-MM-dd HH:mm:ss");
   }

   public static String getDateTimeZhString(Date date) {
      return getString(date, "yyyy年MM月dd日HH:mm:ss");
   }

   public static String getString(Date date, String pattern) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      return dateFormat.format(date);
   }

   public static int getHour(Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      int hour = calendar.get(11);
      return hour;
   }

   public static boolean isClearTime() {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      int hour = calendar.get(11);
      int mins = calendar.get(12);
      return 8 == hour && mins >= 10 && mins <= 25;
   }

   public static boolean isClearTimeForHost() {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      int hour = calendar.get(11);
      int mins = calendar.get(12);
      return 8 == hour && mins >= 10 && mins <= 27;
   }

   public static boolean isWarnTime(String cronStr) {
      boolean sign = true;
      if (StringUtils.isEmpty(cronStr)) {
         return sign;
      } else {
         try {
            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
            CronParser parser = new CronParser(cronDefinition);
            ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cronStr.trim()));
            Date date = new Date();
            int year = date.getYear() + 1900;
            int month = date.getMonth() + 1;
            int day = date.getDate();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            int seconds = date.getSeconds();
            LocalDateTime ldt = LocalDateTime.of(year, month, day, hours, minutes, seconds);
            ZonedDateTime zbj = ldt.atZone(ZoneId.systemDefault());
            sign = executionTime.isMatch(zbj);
         } catch (Exception var14) {
            logger.error("解析告警时间段表达式错误", var14);
         }

         return sign;
      }
   }

   public static String getDateBefore(int day) {
      Calendar now = Calendar.getInstance();
      now.setTime(new Date());
      now.set(5, now.get(5) - day);
      return getString(now.getTime(), "yyyy-MM-dd HH:mm:ss");
   }

   public static void setBeginEndTime(Map<String, Object> params, int day) {
      String beginTime = getDateBefore(day);
      params.put("startTime", beginTime);
      String endTime = getDateTimeString(new Date());
      params.put("endTime", endTime);
   }

   public static String setLastMonBeginEndTime(Map<String, Object> params) {
      Calendar calendar = Calendar.getInstance();
      int year = calendar.get(1);
      int month = calendar.get(2);
      String lastMonStr = month + "";
      if (month < 10) {
         lastMonStr = "0" + month;
      }

      String beginTime = year + "-" + lastMonStr + "-01 00:00:00";
      String nowMonStr = month + 1 + "";
      if (month + 1 < 10) {
         nowMonStr = "0" + (month + 1);
      }

      String endTime = year + "-" + nowMonStr + "-01 00:00:00";
      params.put("startTime", beginTime);
      params.put("endTime", endTime);
      return year + "-" + lastMonStr;
   }

   public static String getLastWeekMonday(Date date) {
      SimpleDateFormat foramt = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      if (1 == cal.get(7)) {
         cal.add(5, -1);
      }

      cal.add(5, -7);
      cal.set(7, 2);
      return foramt.format(cal.getTime()) + " 00:00:00";
   }

   public static String getLastWeekSunday(Date date) {
      SimpleDateFormat foramt = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      if (1 == cal.get(7)) {
         cal.add(5, -1);
      }

      cal.set(7, 1);
      return foramt.format(cal.getTime()) + " 23:59:59";
   }

   public static String setLastWeekBeginEndTime(Map<String, Object> params) {
      Date date = new Date();
      String beginTime = getLastWeekMonday(date);
      String endTime = getLastWeekSunday(date);
      params.put("startTime", beginTime);
      params.put("endTime", endTime);
      String beginDate = beginTime.substring(0, 10);
      String endDate = endTime.substring(0, 10);
      return beginDate + " 至 " + endDate;
   }

   public static String secondToDate(Long second, String patten) {
      if (second == null) {
         return "";
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(second * 1000L);
         Date date = calendar.getTime();
         SimpleDateFormat format = new SimpleDateFormat(patten);
         String dateString = format.format(date);
         return dateString;
      }
   }

   public static String millisToDate(String millisStr, String patten) {
      if (StringUtils.isEmpty(millisStr)) {
         return "";
      } else {
         try {
            Long millis = Long.valueOf(millisStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(patten);
            String dateString = format.format(date);
            return dateString;
         } catch (Exception var7) {
            logger.error("millisToDate错误", var7);
            return "";
         }
      }
   }

   public static String beforeHourToNowDate(Integer hours) {
      if (null == hours || 0 == hours) {
         hours = 1;
      }

      Calendar c = Calendar.getInstance();
      long diffTimes = 3600000L;
      long nowTimes = c.getTimeInMillis();
      c.setTimeInMillis(nowTimes - (long)hours * diffTimes);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.format(c.getTime());
   }

   public static String beforeMinutesToNowDate(Integer minutes) {
      if (null == minutes || 0 == minutes) {
         minutes = 1;
      }

      Calendar c = Calendar.getInstance();
      long diffTimes = 60000L;
      long nowTimes = c.getTimeInMillis();
      c.setTimeInMillis(nowTimes - (long)minutes * diffTimes);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.format(c.getTime());
   }

   public static String getCurrentDateTimeNoChar() {
      String currentTime = getCurrentDateTime();
      if (!StringUtils.isEmpty(currentTime)) {
         currentTime = currentTime.replace("-", "").replace(" ", "").replace(":", "");
      }

      return currentTime;
   }
}
