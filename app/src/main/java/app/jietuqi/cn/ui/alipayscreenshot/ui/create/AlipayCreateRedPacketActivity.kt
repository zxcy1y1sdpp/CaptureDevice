package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.alipay.BaseAlipayScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_alipay_create_redpacket.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class AlipayCreateRedPacketActivity : BaseAlipayScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_redpacket

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
        mAlipayCreatesRedPacketSendTv.setOnClickListener(this)
        mAlipayCreatesRedPacketReceiveTv.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mAlipayCreatesRedPacketMsgEt.setText(if (!TextUtils.isEmpty(mMsgEntity.msg)) mMsgEntity.msg else "")
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mAlipayCreatesRedPacketReceiveTv ->{
                mMsgEntity.msgType = 4
                OtherUtil.changeWechatTwoBtnBg(this, mAlipayCreatesRedPacketReceiveTv, mAlipayCreatesRedPacketSendTv)
                mMsgEntity.receive = true
            }
            R.id.mAlipayCreatesRedPacketSendTv ->{
                mMsgEntity.msgType = 3
                OtherUtil.changeWechatTwoBtnBg(this, mAlipayCreatesRedPacketSendTv, mAlipayCreatesRedPacketReceiveTv)
                mMsgEntity.receive = false
            }
            R.id.overallAllRightWithBgTv ->{
                mMsgEntity.msg = if (mAlipayCreatesRedPacketMsgEt.text.toString().trim().isNotEmpty()) mAlipayCreatesRedPacketMsgEt.text.toString() else "恭喜发财，万事如意！"
            }
        }
        super.onClick(v)
    }
}
