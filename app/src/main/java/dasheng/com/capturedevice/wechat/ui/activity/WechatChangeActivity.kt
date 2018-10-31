package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseActivity
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.util.StringUtils
import dasheng.com.capturedevice.util.statusbar.StatusBarUtil
import kotlinx.android.synthetic.main.activity_wechat_change.*
import java.lang.reflect.Method

/**
 * 作者： liuyuanbo on 2018/10/29 16:14.
 * 时间： 2018/10/29 16:14
 * 邮箱： 972383753@qq.com
 * 用途： 微信零钱页面
 */

class WechatChangeActivity : BaseWechatActivity() {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.WHITE)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }
    override fun setLayoutResourceId() = R.layout.activity_wechat_change

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }

    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val money = intent.getStringExtra(IntentKey.MONEY)
        mMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(money), "¥")
    }
    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }
}
