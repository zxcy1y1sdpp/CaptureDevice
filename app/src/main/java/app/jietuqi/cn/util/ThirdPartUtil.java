package app.jietuqi.cn.util;

import app.jietuqi.cn.callback.MobSmsCodeListener;

/**
 * 作者： liuyuanbo on 2018/11/15 15:59.
 * 时间： 2018/11/15 15:59
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class ThirdPartUtil {
    private static ThirdPartUtil mInstant;
    // 单例模式
    public static ThirdPartUtil getInstance() {
        if (null == mInstant) {
            mInstant = new ThirdPartUtil();
        }
        return mInstant;
    }
    public ThirdPartUtil(){}
    /*************************************************************  Mob 验证码相关 *************************************************************/
    private static MobSmsCodeListener mMobSmsCodeListener;
    /**
     * 发送验证码
     */
    public void sendSmsCode(String phoneNum, MobSmsCodeListener listener){

    }
    public void verifySmsCode(String phoneNum, String smsCode, MobSmsCodeListener listener){
    }

    /**
     * 取消监听
     */
    public void unregisterEventHandler(){
    }
}
