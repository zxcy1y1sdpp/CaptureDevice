package dasheng.com.capturedevice.base

import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.util.statusbar.StatusBarUtil

/**
 * 作者： liuyuanbo on 2018/11/6 10:23.
 * 时间： 2018/11/6 10:23
 * 邮箱： 972383753@qq.com
 * 用途： 自己页面的基类（非仿微信，支付宝，QQ页面的基类）
 */

abstract class BaseOverallActivity : BaseActivity(){
    /**
     * 设置状态栏的颜色
     */
    fun setStatusBarColor(color: Int = ColorFinal.WHITE){
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarColor(this, color)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }
}
