package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.StringUtils
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_wechat_change.*

/**
 * 作者： liuyuanbo on 2018/10/29 16:14.
 * 时间： 2018/10/29 16:14
 * 邮箱： 972383753@qq.com
 * 用途： 微信零钱页面
 */

class WechatChangeActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_change

    override fun needLoadingView() = false

    override fun initAllViews() {
        StatusBarUtil.setColor(this@WechatChangeActivity, ColorFinal.WHITE, 0)
        StatusBarUtil.setLightMode(this@WechatChangeActivity)
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val money = intent.getStringExtra(IntentKey.MONEY)
        mMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(money), "¥")
    }
}
