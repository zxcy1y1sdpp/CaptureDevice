package app.jietuqi.cn.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者： liuyuanbo on 2018/10/17 14:19.
 * 时间： 2018/10/17 14:19
 * 邮箱： 972383753@qq.com
 * 用途： 时间工具
 */

public class TimeUtil {
    /**
     * 获取当前时间以毫秒为单位
     *
     * @return
     */
    public static long getCurrentTimeEndMs(){
        return System.currentTimeMillis();
    }
    /**
     * 获取当前时间以秒为单位
     * @return
     */
    public static long getCurrentTimeEndSec(){
        return System.currentTimeMillis() / 1000;
    }
    public static Date getNowDate(){
        Date curDate = new Date(System.currentTimeMillis());
        return curDate;
    }
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime2(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    public static String getNowTimeWithYMD(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    public static String getNowAllTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(Object time){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy—MM—dd");
        Date date = new Date(Long.parseLong(time.toString()) * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * 将时间戳转换为时间
     */
    public static String stampToDateYMDHM(Object time){
        Long timeStamp = Long.parseLong(time.toString());
        if (String.valueOf(time).length() <= 10){//秒
            timeStamp = timeStamp * 1000;
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        Date date = new Date(timeStamp);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * 将时间戳转换为时间
     */
    public static String stampToDateMDHM(Object time){
        Long timeStamp = Long.parseLong(time.toString());
        if (String.valueOf(time).length() <= 10){//秒
            timeStamp = timeStamp * 1000;
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd HH时mm分");
        Date date = new Date(timeStamp);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * 将时间戳转换为时间
     * 时分秒
     */
    public static String getNowTimeOnlyHMS(){
//        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date(Long.parseLong(time.toString()) * 1000);
//        res = simpleDateFormat.format(date);
//        return res;

        String time = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//中间的横线（-）比较短
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy—MM—dd HH:mm");//中间的横线（-）比较长
        Date date;
        if (time.length() < 10){//秒
            date = new Date(Long.parseLong(time) * 1000);
        }else {
            date = new Date(Long.parseLong(time));
        }
        return simpleDateFormat.format(date);
    }
    /**
     * 将时间戳转换为时间
     */
    public static String stampToDateWithYMDHM(Object object){
        String time = object.toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//中间的横线（-）比较短
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy—MM—dd HH:mm");//中间的横线（-）比较长
        Date date;
        if (time.length() < 10){//秒
            date = new Date(Long.parseLong(time) * 1000);
        }else {
            date = new Date(Long.parseLong(time));
        }
        return simpleDateFormat.format(date);
    }

    /**
     * 判断是否是同一天
     * @param date
     * @param sameDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameDay(Date date, Date sameDate) {
        if (null == date || null == sameDate) {
            return false;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(sameDate);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
                && nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
                && nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {
            return true;
        }
        // if (date.getYear() == sameDate.getYear() && date.getMonth() == sameDate.getMonth()
        // && date.getDate() == sameDate.getDate()) {
        // return true;
        // }
        return false;

    }
}
