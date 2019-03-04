package app.jietuqi.cn.wechat.simulator.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorGroupRedPacketAdapter
import kotlinx.android.synthetic.main.activity_wechat_simulator_group_red_packet.*

/**
 * 作者： liuyuanbo on 2019/2/1 16:54.
 * 时间： 2019/2/1 16:54
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图 -- 发红包
 */
class WechatSimulatorGroupRedPacketActivity : BaseCreateActivity() {
    private var mAdapter: WechatSimulatorGroupRedPacketAdapter? = null
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_group_red_packet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.wechatPreviewTitleLayout)
    }

    override fun initViewsListener() {
        mWechatPreviewRedPacketFinishIv.setOnClickListener{
            finish()
        }
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatScreenShotEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
        mAdapter = if(entity.wechatUserId == UserOperateUtil.getWechatSimulatorMySelf().wechatUserId){//自己发送的红包
            WechatSimulatorGroupRedPacketAdapter(entity, true)
        }else{
            WechatSimulatorGroupRedPacketAdapter(entity, false)
        }

        mGroupRedPacketRv.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
    }
}
