package app.jietuqi.cn.constant;

import android.os.Environment;

/**
 * 作者： liuyuanbo on 2018/12/19 12:19.
 * 时间： 2018/12/19 12:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class Constant {
    /**
     * LOG的开关和类型
     */
    public static final boolean LOGOFF = false;
    public static final boolean LOGOFF_VERBOSE = false;
    public static final boolean LOGOFF_DEBUG = false;
    /**
     * apk的目录
     */
    public static final String APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wsyxb/apk";
}
