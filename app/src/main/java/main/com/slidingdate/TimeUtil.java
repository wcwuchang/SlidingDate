package main.com.slidingdate;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * 获取绝对时间(系统当前时间),时间格式为"yyMMddHHmmss"
     *
     * @return
     */
    public static String getAbsoluteTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 获取相对时间(将给点的时间变换成相对于系统当前时间的差值)，格式为“XX分钟前”
     *
     * @return
     */
    public static String getRelativeTime(String date) {
        Log.i("TimeUtil", "date=" + date);
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            Date dt1 = sdf.parse(date);

            Calendar cl = Calendar.getInstance();
            int year2 = cl.get(Calendar.YEAR);
            int month2 = cl.get(Calendar.MONTH);
            int day2 = cl.get(Calendar.DAY_OF_MONTH);
            int hour2 = cl.get(Calendar.HOUR_OF_DAY);
            int minute2 = cl.get(Calendar.MINUTE);
            int second2 = cl.get(Calendar.SECOND);

            cl.setTime(dt1);
            int year1 = cl.get(Calendar.YEAR);
            int month1 = cl.get(Calendar.MONTH);
            int day1 = cl.get(Calendar.DAY_OF_MONTH);
            int hour1 = cl.get(Calendar.HOUR_OF_DAY);
            int minute1 = cl.get(Calendar.MINUTE);
            int second1 = cl.get(Calendar.SECOND);

            if (year1 == year2) {
                if (month1 == month2) {
                    if (day1 == day2) {
                        if (hour1 == hour2) {
                            if (minute1 == minute2) {
                                time = "刚才";
                            } else {
                                time = (minute2 - minute1) + "分钟前";
                            }
                        } else if (hour2 - hour1 > 3) {
                            time = formatTime(hour1, minute1);
                        } else if (hour2 - hour1 == 1) {
                            if (minute2 - minute1 > 0) {
                                time = "1小时前";
                            } else {
                                time = (60 + minute2 - minute1) + "分钟前";
                            }
                        } else {
                            time = (hour2 - hour1) + "小时前";
                        }
                    } else if (day2 - day1 == 1) {  //昨天
                        if (hour1 > 12) {
                            time = (month1 + 1) + "月" + day1 + "日  下午";
                        } else {
                            time = (month1 + 1) + "月" + day1 + "日  上午";
                        }
                    } else {
                        time = (month1 + 1) + "月" + day1 + "日";
                    }
                } else {
                    time = (month1 + 1) + "月" + day1 + "日";
                }
            } else {
                time = year1 + "年" + month1 + "月" + day1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getFormatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy年MM月dd日   HH:mm:ss");
        return sdf2.format(date);
    }

    private static String formatTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time += "0" + hour + ":";
        } else {
            time += hour + ":";
        }

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }
        System.out.println("format(hour, minute)=" + time);
        return time;
    }


    /**
     * 获取对应年月的月天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year,int month){
        boolean isLeap=isLeap(year);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if(isLeap) return 29;
                return 28;
            default:
                return 31;
        }
    }

    /**
     * 是否为闰年
     * @param year
     * @return
     */
    public static boolean isLeap(int year){
        if(year%400==0){
            return true;
        }else if(year%100!=0&&year%4==0){
            return true;
        }
        return false;
    }

    /**
     * 根据position判断是周几
     *
     * @param position
     * @return
     */
    public static String getFewOfWeek(int position) {
        switch (position) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "一";
        }
    }
}
