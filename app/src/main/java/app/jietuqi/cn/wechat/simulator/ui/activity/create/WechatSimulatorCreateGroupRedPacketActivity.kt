package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_simloator_wechat_create_group_redpacket.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途： 创建群红包
 */
class WechatSimulatorCreateGroupRedPacketActivity : BaseCreateActivity() {
    private var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    private var mSenderEntity = WechatUserEntity()
    /**
     * 群人数
     */
    private var mCount = 0
    override fun setLayoutResourceId() = R.layout.activity_simloator_wechat_create_group_redpacket


    override fun needLoadingView(): Boolean {
        return false
    }
    override fun initAllViews() {
        mMsgEntity.msgType = 3
        mMsgEntity.receive = false
        setBlackTitle("添加群红包", 1)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mSenderEntity = intent.getSerializableExtra(IntentKey.ENTITY_SENDER) as WechatUserEntity
        val isComMsg = intent.getBooleanExtra(IntentKey.COMMSG, false)
        mMsgEntity.isComMsg = isComMsg
        mCount = intent.getIntExtra(IntentKey.COUNT, 0) + 1
        mMsgEntity.avatarInt = mSenderEntity.avatarInt
        mMsgEntity.resourceName = mSenderEntity.resourceName
        mMsgEntity.avatarStr = mSenderEntity.wechatUserAvatar
        mMsgEntity.wechatUserId = mSenderEntity.wechatUserId
        mMsgEntity.wechatUserNickName = mSenderEntity.wechatUserNickName
        mMsgEntity.redPacketSenderNickName = mSenderEntity.wechatUserNickName
        mSimulatorWechatCreateGroupRedPacketCountEt.hint = StringUtils.insertFrontAndBack(mCount, "红包个数（本群共", "）人")

    }
    override fun initViewsListener() {
        overallAllRightWithBgTv.setOnClickListener(this)
        mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                var money = mSimulatorWechatCreateGroupRedPacketMoneyEt.text.toString()
                if (TextUtils.isEmpty(OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketCountEt))){
                    showToast("请输入红包个数")
                    return
                }
                mCount = OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketCountEt).toInt()
                when {
                    TextUtils.isEmpty(money) -> {
                        showToast("请输入红包金额")
                        return
                    }
                    money.toFloat() > mCount * 200 -> {
                        showToast(StringUtils.insertFrontAndBack(mCount * 200, "最大金额不可超过", "元"))
                        return
                    }
                    else -> mMsgEntity.money = money
                }
                if (OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketCountEt).toInt() <= 0){
                    showToast("红包个数不能小于或等于0")
                    return
                }
                if (OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketCountEt).toInt() > mCount){
                    showToast("红包最大个数不得超过参与领红包的人数")
                    return
                }
                if (TextUtils.isEmpty(OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketReceiveTimeEt))){
                    showToast("请设置红包领取完成时间")
                    return
                }
                mMsgEntity.receiveCompleteTime = OtherUtil.getContent(mSimulatorWechatCreateGroupRedPacketReceiveTimeEt)
                mMsgEntity.msg = if (mSimulatorWechatCreateGroupRedPacketMsgEt.text.toString().trim().isNotEmpty()) mSimulatorWechatCreateGroupRedPacketMsgEt.text.toString() else "恭喜发财，大吉大利"
                mMsgEntity.redPacketCount = mSimulatorWechatCreateGroupRedPacketCountEt.text.toString().toInt()
                EventBusUtil.post(mMsgEntity)
                finish()
            }
            R.id.mSimulatorWechatCreateGroupRedPacketJoinReceiveIv ->{
                mMsgEntity.joinReceiveRedPacket = !mMsgEntity.joinReceiveRedPacket
                if (!mMsgEntity.joinReceiveRedPacket){
                    showToast(StringUtils.insertFrontAndBack(mCount - 1, "红包个数最多是", "个"))
                    mCount -= 1
                }else{
                    mCount += 1
                }
                OtherUtil.onOrOff(mMsgEntity.joinReceiveRedPacket, mSimulatorWechatCreateGroupRedPacketJoinReceiveIv)
            }
        }
    }
}
