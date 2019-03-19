package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import kotlinx.android.synthetic.main.activity_wechat_create_transfer.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import kotlinx.android.synthetic.main.include_choice_role.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/6 17:29.
 * 时间： 2018/12/6 17:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信转账截图
 */
class WechatCreateTransferActivity : BaseWechatScreenShotCreateActivity() {
    /**
     * 退款要发两条消息
     * 这是第二条消息
     */
    var mMsgEntity2: WechatScreenShotEntity = WechatScreenShotEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_transfer

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 5
        setBlackTitle("转账", 1)
        val nowTime = TimeUtil.getCurrentTimeEndMs()
        mMsgEntity.transferOutTime = nowTime
        mMsgEntity.transferReceiveTime = nowTime
        mWechatCreatesTransferSendTimeTv.text = TimeUtil.stampToDateWithYMDHM(nowTime)
        mWechatCreatesTransferReceiveTimeTv.text = TimeUtil.stampToDateWithYMDHM(nowTime)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mChangeRoleLayout.setOnClickListener(this)
        mWechatCreatesTransferSendTv.setOnClickListener(this)
        mWechatCreatesTransferReceiveTv.setOnClickListener(this)
        mWechatCreatesTransferBackTv.setOnClickListener(this)
        mWechatCreatesTransferSendTimeLayout.setOnClickListener(this)
        mWechatCreatesTransferReceiveLayout.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mMsgEntity.msgType == 5){//转账
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferSendTv, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferBackTv)
        }else if (mMsgEntity.msgType == 6){//收钱
            if (TextUtils.isEmpty(mMsgEntity.msg) || mMsgEntity.msg.startsWith("转账给")){
                mMsgEntity.msg = ""
            }
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv, mWechatCreatesTransferBackTv)
        }else{
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferBackTv, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv)
        }
        mWechatCreatesTransferMsgEt.setText(mMsgEntity.msg)
        mWechatCreatesTransferMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
        mWechatCreatesTransferSendTimeTv.text = if (mMsgEntity.transferOutTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferOutTime) else ""
        mWechatCreatesTransferReceiveTimeTv.text = if (mMsgEntity.transferReceiveTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferReceiveTime) else ""
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mChangeRoleLayout ->{
                mMe = !mMe
                changeRole()
            }
            R.id.mWechatCreatesTransferReceiveTv ->{
                mMsgEntity.msgType = 6
                mMsgEntity.receive = true
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv, mWechatCreatesTransferBackTv)
            }
            R.id.mWechatCreatesTransferSendTv ->{
                mMsgEntity.msgType = 5
                mMsgEntity.receive = false
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferSendTv, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferBackTv)
            }
            R.id.mWechatCreatesTransferBackTv ->{
                mMsgEntity.msgType = 15
                mMsgEntity.receive = true
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferBackTv, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv)
            }
            R.id.mWechatCreatesTransferSendTimeLayout ->{
                initTimePickerView("转账时间", 1)
            }
            R.id.mWechatCreatesTransferReceiveLayout ->{
                initTimePickerView("收钱时间", 1)
            }
            R.id.overallAllRightWithBgTv ->{
                var money = mWechatCreatesTransferMoneyEt.text.toString()
                if (TextUtils.isEmpty(money)){
                    showToast("请输入转账金额")
                    return
                }
                mMsgEntity.money = mWechatCreatesTransferMoneyEt.text.toString()
                val msg = mWechatCreatesTransferMsgEt.text.toString().trim()
                if (msg.isNotBlank()){
                    mMsgEntity.msg = msg
                }else{
                    if (mMsgEntity.wechatUserId == mOtherSideEntity.wechatUserId){
                        mMsgEntity.msg = "转账给你"
                    }else{
                        mMsgEntity.msg = StringUtils.insertFront(mOtherSideEntity.wechatUserNickName, "转账给")
                    }
                }

                if (mType == 1){//修改
                    val receiveEntity = mHelper.query(mMsgEntity.id)//与被修改的数据关联在一起的收钱的数据
                    if (null != receiveEntity){
                        receiveEntity.transferReceiveTime = mMsgEntity.transferReceiveTime
                        receiveEntity.transferOutTime = mMsgEntity.transferOutTime
                        receiveEntity.money = mMsgEntity.money
                        mHelper.update(receiveEntity)
                    }
                }
                if (mMsgEntity.isComMsg){
                    setFirstMsg(mMySideEntity)
                }else{
                    setFirstMsg(mOtherSideEntity)
                }
                if(mType == 0){
                    if (mMsgEntity.msgType == 15){
                        mMsgEntity2.msg = StringUtils.insertFront(msg, "已退还-")
                        mHelper.save(mMsgEntity2)
                        mMsgEntity.msg = "已退还"
                    }
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity)
                }
                finish()
            }
        }
//        super.onClick(v)
    }
    private fun setFirstMsg(entity: WechatUserEntity){
        mMsgEntity2.avatarInt = entity.avatarInt
        mMsgEntity2.resourceName = entity.resourceName
        mMsgEntity2.avatarStr = entity.wechatUserAvatar
        mMsgEntity2.wechatUserId = entity.wechatUserId
        mMsgEntity2.msgType = 15
        mMsgEntity2.receive = true
        mMsgEntity2.isComMsg = !mMsgEntity2.isComMsg
        mMsgEntity2.money = mMsgEntity.money
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "转账时间"){
            mWechatCreatesTransferSendTimeTv.text = timeEntity.timeWithoutS
            mMsgEntity.transferOutTime = timeEntity.timeLong
        }else{
            mWechatCreatesTransferReceiveTimeTv.text = timeEntity.timeWithoutS
            mMsgEntity.transferReceiveTime = timeEntity.timeLong
        }
    }
}