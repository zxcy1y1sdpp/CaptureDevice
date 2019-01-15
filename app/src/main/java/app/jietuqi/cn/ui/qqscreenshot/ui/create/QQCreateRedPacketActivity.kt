package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.qq.BaseQQScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_qq_create_redpacket.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class QQCreateRedPacketActivity : BaseQQScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_qq_create_redpacket

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
        mQQCreatesRedPacketSendTv.setOnClickListener(this)
        mQQCreatesRedPacketReceiveTv.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mQQCreatesRedPacketMsgEt.setText(if (!TextUtils.isEmpty(mMsgEntity.msg)) mMsgEntity.msg else "")
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mQQCreatesRedPacketReceiveTv ->{
                mMsgEntity.msgType = 4
                OtherUtil.changeWechatTwoBtnBg(this, mQQCreatesRedPacketReceiveTv, mQQCreatesRedPacketSendTv)
                mMsgEntity.receive = true
            }
            R.id.mQQCreatesRedPacketSendTv ->{
                mMsgEntity.msgType = 3
                OtherUtil.changeWechatTwoBtnBg(this, mQQCreatesRedPacketSendTv, mQQCreatesRedPacketReceiveTv)
                mMsgEntity.receive = false
            }
            R.id.overallAllRightWithBgTv ->{
                mMsgEntity.msg = if (mQQCreatesRedPacketMsgEt.text.toString().trim().isNotEmpty()) mQQCreatesRedPacketMsgEt.text.toString() else "恭喜发财"
                mMsgEntity.money = mQQCreatesRedPacketMoneyEt.text.toString()
            }
        }
        super.onClick(v)
    }
}
