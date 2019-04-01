package app.jietuqi.cn.wechat.simulator.ui.activity

import android.content.Intent
import android.text.TextUtils
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_screenshot_receive_red_packet.*

/**
 * 作者： liuyuanbo on 2019/2/1 15:20.
 * 时间： 2019/2/1 15:20
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图 -- 收红包
 */
class WechatSimulatorReceiveRedPacketActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_receive_red_packet

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
        val sender: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY_SENDER) as WechatUserEntity//发送人的信息
        if (TextUtils.isEmpty(sender.msg)){
            mWechatScreenShotRedPacketPreviewMsgTv.text = "恭喜发财，大吉大利"
        }else{
            mWechatScreenShotRedPacketPreviewMsgTv.text = sender.msg//留言
        }
        GlideUtil.displayHead(this, sender.getAvatarFile(), mWechatScreenShotReceiveAvatarIv)//发送人头像
        mWechatScreenShotRedPacketNickNameTv.text = StringUtils.insertBack(sender.wechatUserNickName, "的红包")//发送人昵称
        mWechatScreenShotRedPacketMoneyTv.text = StringUtils.keep2Point(sender.money)
    }

    override fun onResume() {
        super.onResume()
        val isSimulator = intent.getBooleanExtra(IntentKey.IS_SIMULATOR, false)
        if (isSimulator){
            needVipForCover()
        }
    }
}
