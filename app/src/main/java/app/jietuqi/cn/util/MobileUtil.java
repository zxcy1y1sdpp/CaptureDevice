package app.jietuqi.cn.util;

import android.text.TextUtils;

/**
 * 作者： liuyuanbo on 2018/11/14 11:51.
 * 时间： 2018/11/14 11:51
 * 邮箱： 972383753@qq.com
 * 用途： 手机号相关的操作
 */
public class MobileUtil {
    /**
     * 验证是否是手机号
     * @param mobileNum
     * @return
     */
    public static boolean isMobileNO(String mobileNum) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNum))
            return false;
        else
            return mobileNum.matches(telRegex);

    }
}
