package app.jietuqi.cn.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author: lyb
 * create at: 2016/12/9 11:07
 * use for:
 */

public class StringUtils {
    /**
     * String 字符串判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * String转换List
     * @param string :需要转换的文本
     * @param charTag :需要以此来进行分割
     * @return ArrayList<?>
     */
    public static ArrayList<String> toArrayList(String string, String charTag) {
        String[] arr = string.split(charTag);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0, size = arr.length; i < size; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    /**
     * 在String字符串前边添加字符串
     * @param obj
     * @param front:需要在前面添加的符号
     * @return
     */
    public static String insertFront(Object obj, String front){
        if (null == obj){
            return "";
        }
        if (TextUtils.isEmpty(obj.toString())){
            return front;
        }
        StringBuilder builder = new StringBuilder(obj.toString());
        builder.insert(0, front);
        return builder.toString();
    }
    /**
     * 在String字符串前后添加字符串
     * @param front:需要在前面添加的符号
     * @param back:需要在后面添加的符号
     * @return
     */
    public static String insertFrontAndBack(Object obj, String front, String back){
        if (null == obj){
            return "";
        }
        StringBuilder builder = new StringBuilder(String.valueOf(obj));
        builder.insert(0, front).append(back);
        return builder.toString();
    }
    /**
     * 在String字符串前后添加字符串
     * @param back:需要在后面添加的符号
     * @return
     */
    public static String insertBack(Object obj, String back){
        if (null == obj){
            return "";
        }
        StringBuilder builder = new StringBuilder(obj.toString());
        builder.append(back);
        return builder.toString();
    }

    public static String stringToInt(String price){
        return String.valueOf((int) Float.parseFloat(price));
    }
    /**
     * 保留两位小数
     * @return
     */
    public static String keep2Point(Object obj){
        if (obj == null){
            return "0.00";
        }
        if (TextUtils.isEmpty(obj.toString())){
            return "0.00";
        }
        BigDecimal b = new BigDecimal(obj.toString());
        String dd;
        dd = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        System.out.println(dd);
        return dd;
    }
    /**
     * 保留两位小数
     * @return
     */
    public static String insertZeroFont(Object obj){
        if (obj == null){
            return "01";
        }
        if (TextUtils.isEmpty(obj.toString())){
            return "01";
        }
        if (Integer.parseInt(obj.toString()) < 10 ){
            return insertFront(obj, "0");
        }else {
            return obj.toString();
        }
    }
}

