package app.jietuqi.cn.ui.activity

import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity

/**
 * 作者： liuyuanbo on 2018/11/22 12:37.
 * 时间： 2018/11/22 12:37
 * 邮箱： 972383753@qq.com
 * 用途： 用户协议
 */
class OverallProtocolActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_overall_protocol

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("用户协议")
    }

    override fun initViewsListener() {

    }
}
