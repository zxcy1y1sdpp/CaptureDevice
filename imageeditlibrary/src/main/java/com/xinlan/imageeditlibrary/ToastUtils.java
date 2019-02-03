package com.xinlan.imageeditlibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者： liuyuanbo on 2019/1/21 11:57.
 * 时间： 2019/1/21 11:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class ToastUtils {

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (toast == null){
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (toast == null){
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (toast == null){
            toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (toast == null){
            toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (toast == null){
            toast = Toast.makeText(context, null,duration);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (toast == null){
            toast = Toast.makeText(context, null,duration);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

    public static void makeText(Context context, String message, int lengthShort) {
        if (toast == null){
            toast = Toast.makeText(context, null,lengthShort);
            toast.setText(message);
        }else{
            toast.setText(message);
        }
        toast.show();
    }
}