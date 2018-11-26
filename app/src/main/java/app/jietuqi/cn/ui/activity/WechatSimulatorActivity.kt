package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.create.AlipayCreateBalanceActivity
import app.jietuqi.cn.alipay.create.AlipayCreateMyActivity
import app.jietuqi.cn.alipay.create.AlipayCreateRedPacketActivity
import app.jietuqi.cn.alipay.create.AlipayCreateWithdrawDepositBillActivity
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.ui.activity.*
import kotlinx.android.synthetic.main.activity_wechatsimulator.*

/**
 * 作者： liuyuanbo on 2018/10/3 21:37.
 * 时间： 2018/10/3 21:37
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器
 */

class WechatSimulatorActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechatsimulator

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTitle("截图")
    }

    override fun initViewsListener() {
        mWechatFun1.setOnClickListener(this)
        mWechatFun2.setOnClickListener(this)
        mWechatFun3.setOnClickListener(this)
        mWechatFun4.setOnClickListener(this)
        mWechatFun5.setOnClickListener(this)
        mWechatFun6.setOnClickListener(this)
        mWechatFun7.setOnClickListener(this)
        mWechatFun8.setOnClickListener(this)
        mWechatFun9.setOnClickListener(this)


        mWechatFun10.setOnClickListener(this)
        mWechatFun11.setOnClickListener(this)
        mWechatFun12.setOnClickListener(this)
        mWechatFun13.setOnClickListener(this)
        mWechatFun14.setOnClickListener(this)
        mWechatFun15.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatFun1 ->{
                LaunchUtil.launch(this, WechatSendRedPacketActivity::class.java)
            }
            R.id.mWechatFun2 ->{
                LaunchUtil.launch(this, WechatTransferActivity::class.java)
            }
            R.id.mWechatFun3 ->{
                LaunchUtil.startWechatLooseChangeActivity(this, 1)
            }
            R.id.mWechatFun4 ->{
                LaunchUtil.startWechatLooseChangeActivity(this, 0)
            }
            R.id.mWechatFun5 ->{
                LaunchUtil.launch(this, WechatChangeWithdrawDepositActivity::class.java)
            }
            R.id.mWechatFun6 ->{
                LaunchUtil.startWechatVoiceAndVideoActivity(this, 0)
            }
            R.id.mWechatFun7 ->{
                LaunchUtil.launch(this, WechatChargeDetailActivity::class.java)
            }
            R.id.mWechatFun8 ->{
                LaunchUtil.startWechatVoiceAndVideoActivity(this, 1)
            }
            R.id.mWechatFun9 ->{
                LaunchUtil.launch(this, WechatChatListActivity::class.java)
            }


            R.id.mWechatFun10 ->{
                LaunchUtil.launch(this, AlipayCreateRedPacketActivity::class.java)
            }
            R.id.mWechatFun12 ->{
                LaunchUtil.launch(this, AlipayCreateWithdrawDepositBillActivity::class.java)
            }
            R.id.mWechatFun11 ->{
                LaunchUtil.launch(this, AlipayCreateBalanceActivity::class.java)
            }
            R.id.mWechatFun13 ->{
                LaunchUtil.startAlipayCreateTransferBillActivity(this, 1)
            }
            R.id.mWechatFun14 ->{
                LaunchUtil.startAlipayCreateTransferBillActivity(this, 0)
            }

            R.id.mWechatFun15 ->{
                LaunchUtil.launch(this, AlipayCreateMyActivity::class.java)
            }
        }
    }
}
