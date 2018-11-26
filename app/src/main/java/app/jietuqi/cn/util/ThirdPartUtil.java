package app.jietuqi.cn.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import app.jietuqi.cn.callback.MobSmsCodeListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
        SMSSDK.getVerificationCode("86",phoneNum);
        mMobSmsCodeListener = listener;
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(mEventHandler);
    }
    public void verifySmsCode(String phoneNum, String smsCode, MobSmsCodeListener listener){
        SMSSDK.submitVerificationCode("86", phoneNum, smsCode);
        mMobSmsCodeListener = listener;
    }
    private EventHandler mEventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            mMobSmsCodeListener.sendCodeSuccess();
                        } else {
                            // TODO 处理错误的结果
                            mMobSmsCodeListener.sendCodeFail();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果
                            mMobSmsCodeListener.verifyCodeSuccess();
                        } else {
                            // TODO 处理错误的结果
                            mMobSmsCodeListener.verifyCodeFail();
                        }
                    }
                    // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false;
                }
            }).sendMessage(msg);
        }
    };

    /**
     * 取消监听
     */
    public void unregisterEventHandler(){
        if (null != mEventHandler){
            SMSSDK.unregisterEventHandler(mEventHandler);
        }
    }
}
