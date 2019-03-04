package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_create_redpacket.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatCreateRedPacketActivity : BaseWechatScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_redpacket
    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 3
        setBlackTitle("红包", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreatesRedPacketSendTv.setOnClickListener(this)
        mWechatCreatesRedPacketReceiveTv.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mMsgEntity.msgType == 3){//发红包
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesRedPacketSendTv, mWechatCreatesRedPacketReceiveTv)
            mWechatCreatesRedPacketMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
        }else{//收红包
            OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesRedPacketReceiveTv, mWechatCreatesRedPacketSendTv)
            mWechatCreatesRedPacketMoneyEt.visibility = View.GONE
        }
        mWechatCreatesRedPacketMsgEt.setText(if (!TextUtils.isEmpty(mMsgEntity.msg)) mMsgEntity.msg else "")
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreatesRedPacketReceiveTv ->{
                mMsgEntity.msgType = 4
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesRedPacketReceiveTv, mWechatCreatesRedPacketSendTv)
                mMsgEntity.receive = true
                mWechatCreatesRedPacketMoneyEt.visibility = View.GONE
            }
            R.id.mWechatCreatesRedPacketSendTv ->{
                mMsgEntity.msgType = 3
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatesRedPacketSendTv, mWechatCreatesRedPacketReceiveTv)
                mMsgEntity.receive = false
                mWechatCreatesRedPacketMoneyEt.visibility = View.VISIBLE
            }
            R.id.overallAllRightWithBgTv ->{
                var money = mWechatCreatesRedPacketMoneyEt.text.toString()
                if (money.isNotEmpty()){
                    mMsgEntity.money = mWechatCreatesRedPacketMoneyEt.text.toString()
                }
                mMsgEntity.msg = if (mWechatCreatesRedPacketMsgEt.text.toString().trim().isNotEmpty()) mWechatCreatesRedPacketMsgEt.text.toString() else "恭喜发财，大吉大利"
            }
        }
        super.onClick(v)
    }
}
