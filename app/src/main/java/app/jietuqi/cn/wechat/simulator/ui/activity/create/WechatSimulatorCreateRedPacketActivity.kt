package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorCreateActivity
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_simloator_wechat_create_redpacket.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorCreateRedPacketActivity : BaseWechatSimulatorCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_simloator_wechat_create_redpacket

    override fun needLoadingView(): Boolean {
        return false
    }
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 3
        setBlackTitle("红包", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mSimulatorWechatCreatesRedPacketSendTv.setOnClickListener(this)
        mSimulatorWechatCreatesRedPacketReceiveTv.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mMsgEntity.msgType == 3){//发红包
            OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesRedPacketSendTv, mSimulatorWechatCreatesRedPacketReceiveTv)
            mSimulatorWechatCreatesRedPacketMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
        }else{//收红包
            OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesRedPacketReceiveTv, mSimulatorWechatCreatesRedPacketSendTv)
            mSimulatorWechatCreatesRedPacketMoneyEt.visibility = View.GONE
        }
        mSimulatorWechatCreatesRedPacketMsgEt.setText(if (!TextUtils.isEmpty(mMsgEntity.msg)) mMsgEntity.msg else "")
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mSimulatorWechatCreatesRedPacketReceiveTv ->{
                mMsgEntity.msgType = 4
                OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesRedPacketReceiveTv, mSimulatorWechatCreatesRedPacketSendTv)
                mMsgEntity.receive = true
                mSimulatorWechatCreatesRedPacketMoneyEt.visibility = View.GONE
            }
            R.id.mSimulatorWechatCreatesRedPacketSendTv ->{
                mMsgEntity.msgType = 3
                OtherUtil.changeWechatTwoBtnBg(this, mSimulatorWechatCreatesRedPacketSendTv, mSimulatorWechatCreatesRedPacketReceiveTv)
                mMsgEntity.receive = false
                mSimulatorWechatCreatesRedPacketMoneyEt.visibility = View.VISIBLE
            }
            R.id.overallAllRightWithBgTv ->{
                var money = mSimulatorWechatCreatesRedPacketMoneyEt.text.toString()
                if (mMsgEntity.msgType == 3){
                    if (TextUtils.isEmpty(money)){
                        showToast("请输入红包金额")
                        return
                    }else{
                        mMsgEntity.money = mSimulatorWechatCreatesRedPacketMoneyEt.text.toString()
                    }
                }else{
                    mMsgEntity.money = mSimulatorWechatCreatesRedPacketMoneyEt.text.toString()
                }
                mMsgEntity.msg = if (mSimulatorWechatCreatesRedPacketMsgEt.text.toString().trim().isNotEmpty()) mSimulatorWechatCreatesRedPacketMsgEt.text.toString() else "恭喜发财，大吉大利"
            }
        }
        super.onClick(v)
    }
}
