package app.jietuqi.cn.wechat.simulator.ui.activity

import android.graphics.Color
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_wallet.*



/**
 * 作者： liuyuanbo on 2019/1/24 23:38.
 * 时间： 2019/1/24 23:38
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器中的我的钱包
 */
class WechatSimulatorWalletActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_wallet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setStatusBarColor(Color.parseColor("#F7F7F7"))
        setLightStatusBarForM(this, true)
    }

    override fun initViewsListener() {
        mWechatMyWalletLayout.setOnClickListener(this)
        mWechatSimulatorPreviewBackITv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatMyWalletLayout ->{
                LaunchUtil.startWechatChangeActivity(this, UserOperateUtil.getWechatSimulatorMySelf().cash.toString(), true)
            }
            R.id.mWechatSimulatorPreviewBackITv ->{
                finish()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        needVipForCover()
        var cash = UserOperateUtil.getWechatSimulatorMySelf().cash
        mWechatMyWalletChargeTv.text = StringUtils.insertFront(StringUtils.keep2Point(cash), "¥")
    }
}
