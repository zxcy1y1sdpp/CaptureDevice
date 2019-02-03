package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorRechargeActivity
import app.jietuqi.cn.widget.TopSpan
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_wechat_change.*

/**
 * 作者： liuyuanbo on 2018/10/29 16:14.
 * 时间： 2018/10/29 16:14
 * 邮箱： 972383753@qq.com
 * 用途： 微信零钱页面
 */

class WechatChangeActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_change

    override fun needLoadingView() = false

    override fun initAllViews() {
        StatusBarUtil.setColor(this, ColorFinal.WHITE, 0)
        StatusBarUtil.setLightMode(this)
        setWechatViewTitle("微信零钱", 3)
    }

    override fun initViewsListener() {
        mChargeTv.setOnClickListener(this)
        mWithdrawDepositTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val money = intent.getStringExtra(IntentKey.MONEY)
        val sp = SpannableString(StringUtils.insertFront(StringUtils.keep2Point(money), "¥" + ""))
        sp.setSpan(TopSpan(124f, mMoneyTv.currentTextColor), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mMoneyTv.text = sp
        var cash = UserOperateUtil.getWechatSimulatorMySelf().cash
        mMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(cash), "¥")
    }
    override fun onResume() {
        super.onResume()
        needVipForCover()
        var cash = UserOperateUtil.getWechatSimulatorMySelf().cash
        mMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(cash), "¥")
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mChargeTv ->{
                val intent = Intent(this, WechatSimulatorRechargeActivity::class.java)
                intent.putExtra(IntentKey.TYPE, 0)//充值
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.mWithdrawDepositTv ->{
                val intent = Intent(this, WechatSimulatorRechargeActivity::class.java)
                intent.putExtra(IntentKey.TYPE, 1)//提现
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }
}
