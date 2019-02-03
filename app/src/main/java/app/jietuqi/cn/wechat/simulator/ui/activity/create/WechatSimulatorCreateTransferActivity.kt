package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import kotlinx.android.synthetic.main.activity_simulator_wechat_create_transfer.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/6 17:29.
 * 时间： 2018/12/6 17:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信转账截图
 */
class WechatSimulatorCreateTransferActivity : BaseWechatSimulatorCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_simulator_wechat_create_transfer

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 5
        setBlackTitle("转账", 1)
        val nowTime = TimeUtil.getCurrentTimeEndMs()
        mMsgEntity.transferOutTime = nowTime
        mMsgEntity.transferReceiveTime = nowTime
        mSimulatorWechatCreatesTransferSendTimeTv.text = TimeUtil.stampToDateWithYMDHM(nowTime)
        mSimulatorWechatCreatesTransferReceiveTimeTv.text = TimeUtil.stampToDateWithYMDHM(nowTime)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mSimulatorWechatCreatesTransferSendTv.setOnClickListener(this)
        mSimulatorWechatCreatesTransferReceiveTv.setOnClickListener(this)
        mSimulatorWechatCreatesTransferSendTimeLayout.setOnClickListener(this)
        mSimulatorWechatCreatesTransferReceiveLayout.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mMsgEntity.receive = false
        if (mMsgEntity.msgType == 5){//转账
            OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesTransferSendTv, mSimulatorWechatCreatesTransferReceiveTv)
        }else{//收钱
            if (TextUtils.isEmpty(mMsgEntity.msg) || mMsgEntity.msg.startsWith("转账给")){
                mMsgEntity.msg = ""
            }
            mSimulatorWechatCreatesTransferMsgEt.visibility = View.GONE
            OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesTransferReceiveTv, mSimulatorWechatCreatesTransferSendTv)
        }
        mSimulatorWechatCreatesTransferMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
        mSimulatorWechatCreatesTransferSendTimeTv.text = if (mMsgEntity.transferOutTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferOutTime) else ""
        mSimulatorWechatCreatesTransferReceiveTimeTv.text = if (mMsgEntity.transferReceiveTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferReceiveTime) else ""
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mSimulatorWechatCreatesTransferReceiveTv ->{
                mMsgEntity.msgType = 6
                mMsgEntity.receive = true
                OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesTransferReceiveTv, mSimulatorWechatCreatesTransferSendTv)
            }
            R.id.mSimulatorWechatCreatesTransferSendTv ->{
                mMsgEntity.msgType = 5
                mMsgEntity.receive = false
                OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesTransferSendTv, mSimulatorWechatCreatesTransferReceiveTv)
            }
            R.id.mSimulatorWechatCreatesTransferSendTimeLayout ->{
                initTimePickerView("转账时间", 1)
            }
            R.id.mSimulatorWechatCreatesTransferReceiveLayout ->{
                initTimePickerView("收钱时间", 1)
            }
            R.id.overallAllRightWithBgTv ->{
                var money = mSimulatorWechatCreatesTransferMoneyEt.text.toString()
                if (TextUtils.isEmpty(money)){
                    showToast("请输入转账金额")
                    return
                }else{
                    mMsgEntity.money = mSimulatorWechatCreatesTransferMoneyEt.text.toString()
                }
                val msg = mSimulatorWechatCreatesTransferMsgEt.text.toString().trim()
                if (msg.isNotBlank()){
                    mMsgEntity.msg = msg
                }else{
                    if (mMsgEntity.wechatUserId == mOtherSideEntity.wechatUserId){
                        mMsgEntity.msg = "转账给你"
                    }else{
                        mMsgEntity.msg = StringUtils.insertFront(mOtherSideEntity.wechatUserNickName, "转账给")
                    }
                }
            }
        }
        super.onClick(v)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "转账时间"){
            mSimulatorWechatCreatesTransferSendTimeTv.text = timeEntity.timeWithoutS
            mMsgEntity.transferOutTime = timeEntity.timeLong
        }else{
            mSimulatorWechatCreatesTransferReceiveTimeTv.text = timeEntity.timeWithoutS
            mMsgEntity.transferReceiveTime = timeEntity.timeLong
        }
    }
}