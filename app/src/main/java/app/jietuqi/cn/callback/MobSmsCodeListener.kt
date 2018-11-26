package app.jietuqi.cn.callback

/**
 * 作者： liuyuanbo on 2018/11/15 16:06.
 * 时间： 2018/11/15 16:06
 * 邮箱： 972383753@qq.com
 * 用途： Mob手机验证码相关的接口
 */
interface MobSmsCodeListener {
    /**
     * 验证码发送成功
     */
    fun sendCodeSuccess()
    /**
     * 验证码发送失败
     */
    fun sendCodeFail()
    /**
     * 验证码校验成功
     */
    fun verifyCodeSuccess()
    /**
     * 验证码校验成功
     */
    fun verifyCodeFail()
}
