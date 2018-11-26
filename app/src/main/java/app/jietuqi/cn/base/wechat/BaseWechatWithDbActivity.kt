package app.jietuqi.cn.base.wechat

import app.jietuqi.cn.base.BaseWechatActivity

/**
 * 作者： liuyuanbo on 2018/10/31 09:54.
 * 时间： 2018/10/31 09:54
 * 邮箱： 972383753@qq.com
 * 用途：
 */

abstract class BaseWechatWithDbActivity : BaseWechatActivity() {
    open fun loadFromDb(){}
    override fun onResume() {
        super.onResume()
        loadFromDb()
    }
}
