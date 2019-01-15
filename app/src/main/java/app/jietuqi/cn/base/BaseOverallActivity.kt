package app.jietuqi.cn.base

import android.os.Bundle
import app.jietuqi.cn.constant.ColorFinal


/**
 * 作者： liuyuanbo on 2018/11/6 10:23.
 * 时间： 2018/11/6 10:23
 * 邮箱： 972383753@qq.com
 * 用途： 自己页面的基类（非仿微信，支付宝，QQ页面的基类）
 */

abstract class BaseOverallActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBarForM(this, true)
        setStatusBarColor(ColorFinal.WHITE)
    }
}
