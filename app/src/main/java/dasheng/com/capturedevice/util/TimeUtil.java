package dasheng.com.capturedevice.util;

import java.text.SimpleDateFormat;
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
    public static String getNowAllTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
}
