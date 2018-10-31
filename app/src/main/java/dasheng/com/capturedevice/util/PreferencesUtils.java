package dasheng.com.capturedevice.util;

/**
 * 作者： liuyuanbo on 2018/10/11 11:14.
 * 时间： 2018/10/11 11:14
 * 邮箱： 972383753@qq.com
 * 用途：
 */

import android.content.Context;
import android.content.SharedPreferences;

import dasheng.com.capturedevice.R;
import dasheng.com.capturedevice.entity.UserEntity;

/**
 * Used 临时存储数据操作类（全）
 */
public class PreferencesUtils {
    public static String PREFERENCE_NAME = "user";

    public static void putMyInfo(Context context){
        PreferencesUtils.putInt(context, "myId", 0);
        PreferencesUtils.putInt(context, "myAvatar", R.mipmap.icon0);
        PreferencesUtils.putString(context, "myNick", "远博");
    }

    /**
     * 获取本人的信息
     * @param context
     * @return
     */
    public static UserEntity getMyInfo(Context context){
        PreferencesUtils.getInt(context, "myAvatar", R.mipmap.icon0);
        PreferencesUtils.getString(context, "myNick", "远博");
        UserEntity entity = new UserEntity();
        entity.id = PreferencesUtils.getInt(context, "myId", 0);
        entity.avatar = PreferencesUtils.getInt(context, "myAvatar", R.mipmap.icon0);
        entity.nickName = PreferencesUtils.getString(context, "myNick");
        return entity;
    }
    /**存储字符串*/
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }
    /**读取字符串*/
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }
    /**读取字符串（带默认值的）*/
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return preferences.getString(key, defaultValue);
    }
    /**存储整型数字*/
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }
    /**读取整型数字*/
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }
    /**读取整型数字（带默认值的）*/
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return preferences.getInt(key, defaultValue);
    }
    /**存储长整型数字*/
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }
    /**读取长整型数字*/
    public static long getLong(Context context, String key) {
        return getLong(context, key, 0xffffffff);
    }
    /**读取长整型数字（带默认值的）*/
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return preferences.getLong(key, defaultValue);
    }
    /**存储Float数字*/
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }
    /**读取Float数字*/
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1.0f);
    }
    /**读取Float数字（带默认值的）*/
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return preferences.getFloat(key, defaultValue);
    }
    /**存储boolean类型数据*/
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }
    /**读取boolean类型数据*/
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }
    /**读取boolean类型数据（带默认值的）*/
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        return preferences.getBoolean(key, defaultValue);
    }
    /**清除数据*/
    public static boolean clearPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }
}
