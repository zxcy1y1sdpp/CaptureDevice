package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import kotlinx.android.synthetic.main.activity_wechat_create_transfer.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/6 17:29.
 * 时间： 2018/12/6 17:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信转账截图
 */
class WechatCreateTransferActivity : BaseWechatScreenShotCreateActivity() {
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
        mWechatCreatesTransferSendTv.setOnClickListener(this)
        mWechatCreatesTransferReceiveTv.setOnClickListener(this)
        mWechatCreatesTransferSendTimeLayout.setOnClickListener(this)
        mWechatCreatesTransferReceiveLayout.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mMsgEntity.msgType == 5){//转账
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferSendTv, mWechatCreatesTransferReceiveTv)
        }else{//收钱
            if (TextUtils.isEmpty(mMsgEntity.msg) || mMsgEntity.msg.startsWith("转账给")){
                mMsgEntity.msg = ""
            }
            mWechatCreatesTransferMsgEt.visibility = View.GONE
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv)
        }
        mWechatCreatesTransferMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")

        mWechatCreatesTransferSendTimeTv.text = if (mMsgEntity.transferOutTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferOutTime) else ""
        mWechatCreatesTransferReceiveTimeTv.text = if (mMsgEntity.transferReceiveTime > 0) TimeUtil.stampToDateWithYMDHM(mMsgEntity.transferReceiveTime) else ""
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreatesTransferReceiveTv ->{
                mMsgEntity.msgType = 6
                mMsgEntity.receive = true
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferReceiveTv, mWechatCreatesTransferSendTv)
            }
            R.id.mWechatCreatesTransferSendTv ->{
                mMsgEntity.msgType = 5
                mMsgEntity.receive = false
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesTransferSendTv, mWechatCreatesTransferReceiveTv)
            }
            R.id.mWechatCreatesTransferSendTimeLayout ->{
                initTimePickerView("转账时间", 1)
            }
            R.id.mWechatCreatesTransferReceiveLayout ->{
                initTimePickerView("收钱时间", 1)
            }
            R.id.overallAllRightWithBgTv ->{
                var money = mWechatCreatesTransferMoneyEt.text.toString()
                if (money.isNotBlank()){
                    mMsgEntity.money = mWechatCreatesTransferMoneyEt.text.toString()
                }
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
//123                        EventBusUtil.post(receiveEntity)
                    }
                }
            }
        }
        super.onClick(v)
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