package app.jietuqi.cn.wechat.simulator.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import kotlinx.android.synthetic.main.activity_wechat_simulator_recharge_success.*

/**
 * 作者： liuyuanbo on 2019/1/26 14:48.
 * 时间： 2019/1/26 14:48
 * 邮箱： 972383753@qq.com
 * 用途： 充值成功页面
 */
class WechatSimulatorRechargeSuccessActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_recharge_success

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
    }

    override fun initViewsListener() {
        mWechatSimulatorPreviewBackITv.setOnClickListener(this)
        mWechatSimulatorRechargeSuccessFinishTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatBankEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatBankEntity
        mWechatSimulatorRechargeSuccessBankInfoTv.text = StringUtils.insertFrontAndBack(" 尾号", entity.bankShortName, entity.bankTailNumber)
        val money = intent.getStringExtra(IntentKey.MONEY)
        mWechatSimulatorRechargeSuccessMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(money), "¥")
        var mySelf = UserOperateUtil.getWechatSimulatorMySelf()
        mySelf.cash += money.toDouble()
        SharedPreferencesUtils.saveBean2Sp(mySelf, SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorPreviewBackITv ->{
                finish()
            }
            R.id.mWechatSimulatorRechargeSuccessFinishTv ->{
                finish()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        needVipForCover()
    }
}
