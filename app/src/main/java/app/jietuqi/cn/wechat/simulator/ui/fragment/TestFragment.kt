package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.util.Log
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.wechat.simulator.widget.WechatSimulatorDialog


/**
 * 作者： liuyuanbo on 2018/10/9 17:41.
 * 时间： 2018/10/9 17:41
 * 邮箱： 972383753@qq.com
 * 用途： 微信聊天列表
 */

class TestFragment : BaseWechatFragment(), WechatSimulatorDialog.OperateListener {
    override fun operate(type: String, userEntity: WechatUserEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun needLoading() = false
    override fun setLayoutResouceId(): Int {
        Log.e("loadFromDb-onCreate", TimeUtil.getNowAllTime())
        return R.layout.fragment_wechat_list
    }

    override fun initAllViews() {
    }
    override fun initViewsListener() {
    }
}
