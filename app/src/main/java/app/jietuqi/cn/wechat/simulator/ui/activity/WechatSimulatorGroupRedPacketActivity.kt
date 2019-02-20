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
        if(entity.wechatUserId == UserOperateUtil.getWechatSimulatorMySelf().wechatUserId){//自己发送的红包
            mAdapter = WechatSimulatorGroupRedPacketAdapter(entity, true)
        }else{
            mAdapter = WechatSimulatorGroupRedPacketAdapter(entity, false)
        }

        mGroupRedPacketRv.adapter = mAdapter
        /*val sender: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY_SENDER) as WechatUserEntity//发送人的信息
        val receiver: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY_RECEIVER) as WechatUserEntity//领取人的信息
        GlideUtil.displayHead(this, sender.getAvatarFile(), mWechatScreenShotSenderAvatarIv)//发送人头像
        mWechatScreenShotRedPacketNickNameTv.text = StringUtils.insertBack(sender.wechatUserNickName, "的红包")//发送人昵称
        if (TextUtils.isEmpty(sender.msg)){
            mWechatScreenShotRedPacketMsgTv.text = "恭喜发财，大吉大利"
        }else{
            mWechatScreenShotRedPacketMsgTv.text = sender.msg//留言
        }
        mWechatScreenShotSendRedPacketMoney2Tv.text = StringUtils.insertBack(StringUtils.keep2Point(sender.money), "元")//设置金额
        GlideUtil.displayHead(this, receiver.getAvatarFile(), mWechatScreenShotSendRedPacketReceiverAvatarIv)//设置领取人的头像
        mWechatScreenShotSendRedPacketReceiveTimeTv.text = WechatTimeUtil.getNewChatTimeHongbaoPreview(receiver.lastTime)//设置领取时间
        mWechatScreenShotSendRedPacketReceiverNickNameTv.text = receiver.wechatUserNickName
        mWechatScreenShotSendRedPacketMoneyTv.text = StringUtils.insertFrontAndBack(StringUtils.keep2Point(sender.money), "1个红包共", "元")*/
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
    }
}
