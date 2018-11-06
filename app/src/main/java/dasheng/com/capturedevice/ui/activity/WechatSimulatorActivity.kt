package dasheng.com.capturedevice.ui.activity

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.create.AlipayCreateBalanceActivity
import dasheng.com.capturedevice.alipay.create.AlipayCreateMyActivity
import dasheng.com.capturedevice.alipay.create.AlipayCreateRedPacketActivity
import dasheng.com.capturedevice.alipay.create.AlipayCreateWithdrawDepositBillActivity
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.wechat.ui.activity.*
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

    }

    override fun initViewsListener() {
        mWechatRedPacketBtn.setOnClickListener(this)
        mWechatWalletBtn.setOnClickListener(this)
        mWechatTalkBtn.setOnClickListener(this)
        mWechatTransferBtn.setOnClickListener(this)
        mWechatVoiceBtn.setOnClickListener(this)
        mWechatVideoBtn.setOnClickListener(this)
        mWechatWithdrawDepositBtn.setOnClickListener(this)
        mChangeBtn.setOnClickListener(this)
        mWechatChargeDetailBtn.setOnClickListener(this)


        mAlipayRedPacketBtn.setOnClickListener(this)
        mAlipayBalanceBtn.setOnClickListener(this)
        mAlipayTransferBillBtn.setOnClickListener(this)
        mAlipayReceivingBtn.setOnClickListener(this)
        mAlipayWithdrawDepositBillBtn.setOnClickListener(this)
        mAlipayMyBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatRedPacketBtn ->{
                LaunchUtil.launch(this, WechatSendRedPacketActivity::class.java)
            }
            R.id.mWechatWalletBtn ->{
                LaunchUtil.startWechatLooseChangeActivity(this, 0)
            }
            R.id.mChangeBtn ->{
                LaunchUtil.startWechatLooseChangeActivity(this, 1)
            }
            R.id.mWechatTalkBtn ->{
                LaunchUtil.launch(this, WechatChatListActivity::class.java)
            }
            R.id.mWechatTransferBtn ->{
                LaunchUtil.launch(this, WechatTransferActivity::class.java)
            }
            R.id.mWechatVoiceBtn ->{
                LaunchUtil.startWechatVoiceAndVideoActivity(this, 0)
            }
            R.id.mWechatVideoBtn ->{
                LaunchUtil.startWechatVoiceAndVideoActivity(this, 1)
            }
            R.id.mWechatWithdrawDepositBtn ->{
                LaunchUtil.launch(this, WechatChangeWithdrawDepositActivity::class.java)
            }
            R.id.mWechatChargeDetailBtn ->{
                LaunchUtil.launch(this, WechatChargeDetailActivity::class.java)
            }
            R.id.mAlipayRedPacketBtn ->{
                LaunchUtil.launch(this, AlipayCreateRedPacketActivity::class.java)
            }
            R.id.mAlipayBalanceBtn ->{
                LaunchUtil.launch(this, AlipayCreateBalanceActivity::class.java)
            }
            R.id.mAlipayTransferBillBtn ->{
                LaunchUtil.startAlipayCreateTransferBillActivity(this, 0)
            }
            R.id.mAlipayReceivingBtn ->{
                LaunchUtil.startAlipayCreateTransferBillActivity(this, 1)
            }
            R.id.mAlipayWithdrawDepositBillBtn ->{
                LaunchUtil.launch(this, AlipayCreateWithdrawDepositBillActivity::class.java)
            }
            R.id.mAlipayMyBtn ->{
                LaunchUtil.launch(this, AlipayCreateMyActivity::class.java)
            }
        }
    }
}
