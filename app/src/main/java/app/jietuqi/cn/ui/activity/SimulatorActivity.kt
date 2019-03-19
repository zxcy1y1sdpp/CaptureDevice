package app.jietuqi.cn.ui.activity

import android.util.Log
import android.view.View
import app.jietuqi.cn.BuildConfig
import app.jietuqi.cn.R
import app.jietuqi.cn.RoleOfLibraryActivity
import app.jietuqi.cn.alipay.create.AlipayCreateBalanceActivity
import app.jietuqi.cn.alipay.create.AlipayCreateMyActivity
import app.jietuqi.cn.alipay.create.AlipayCreateRedPacketActivity
import app.jietuqi.cn.alipay.create.AlipayCreateWithdrawDepositBillActivity
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayScreenShotActivity
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQScreenShotActivity
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatScreenShotActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.WechatConstant
import app.jietuqi.cn.wechat.create.*
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatChatListActivity
import com.coorchice.library.SuperTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.zhouyou.http.utils.HttpLog
import kotlinx.android.synthetic.main.activity_wechatsimulator.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/10/3 21:37.
 * 时间： 2018/10/3 21:37
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器
 */

class SimulatorActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechatsimulator

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        if (!BuildConfig.DEBUG){
            if (UserOperateUtil.needColseByChannel()){
                HttpLog.d("channer---需要隐藏")
                mCloseLayout.visibility = View.GONE
                mWechatFun4.visibility = View.GONE
                mCloseLayout1.visibility = View.GONE
                mOverallSimulatorScreenShotLayout.visibility = View.GONE

                mWechatFun6.setOnClickListener(this)
                mWechatFun8.setOnClickListener(this)

                mAlipayTitle.visibility = View.GONE
                mCloseLayout2.visibility = View.GONE
                mCloseLayout3.visibility = View.GONE
                mWechatWalletLayout.visibility = View.GONE
            }
        }
        setTopTitle("微商截图", 0, rightIv = R.mipmap.jietu_lianxiren)
    }

    override fun initViewsListener() {
        mWechatScreenShotLayout.setOnClickListener(this)
        mAlipayScreenShotLayout.setOnClickListener(this)
        mQQScreenShotLayout.setOnClickListener(this)
        mWechatFun1.setOnClickListener(this)
        mWechatFun2.setOnClickListener(this)
        mWechatFun3.setOnClickListener(this)
        mWechatFun4.setOnClickListener(this)
        mWechatFun5.setOnClickListener(this)
        mWechatFun6.setOnClickListener(this)
        mWechatFun7.setOnClickListener(this)
        mWechatFun8.setOnClickListener(this)
        mWechatFun9.setOnClickListener(this)
//        mWechatFun16.setOnClickListener(this)

        mWechatFun10.setOnClickListener(this)
        mWechatFun11.setOnClickListener(this)
        mWechatFun12.setOnClickListener(this)
        mWechatFun13.setOnClickListener(this)
        mWechatFun14.setOnClickListener(this)
        mWechatFun15.setOnClickListener(this)

        mWechatSimulatorLayout.setOnClickListener(this)
        mAlipaySimulatorLayout.setOnClickListener(this)
        mQQSimulatorLayout.setOnClickListener(this)
        mTeachIv.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (UserOperateUtil.isVip()){
            mVipFun4.visibility = View.GONE
            mVipFun5.visibility = View.GONE
            mVipFun7.visibility = View.GONE
            mVipFun14.visibility = View.GONE
            mVipFun15.visibility = View.GONE
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            var list = WechatSimulatorListHelper(this@SimulatorActivity).queryAll()
            if (null != list){
                WechatConstant.WECHAT_CACHE_LIST.clear()
                WechatConstant.WECHAT_CACHE_LIST.addAll(list)
            }
        }
    }
    private fun showTip(tv: SuperTextView){
        var dialog = QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord("努力开发中").create()
        dialog.show()
        tv.postDelayed({ dialog.dismiss() }, 1500)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id != R.id.overAllBackLayout){
            if (!UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                return
            }
        }
        when(v.id){
            R.id.mAlipaySimulatorLayout ->{
                showTip(mAlipaySimulatorLayout)
            }
            R.id.mQQSimulatorLayout ->{
                showTip(mQQSimulatorLayout)
            }
            R.id.overAllRightIv ->{
                LaunchUtil.launch(this, RoleOfLibraryActivity::class.java)
            }
            R.id.mWechatScreenShotLayout ->{
                LaunchUtil.launch(this, WechatScreenShotActivity::class.java)
            }
            R.id.mAlipayScreenShotLayout ->{
                LaunchUtil.launch(this, AlipayScreenShotActivity::class.java)
            }
            R.id.mQQScreenShotLayout ->{
                LaunchUtil.launch(this, QQScreenShotActivity::class.java)
            }
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
                LaunchUtil.launch(this, WechatVoiceActivity::class.java)
            }
            R.id.mWechatFun7 ->{
                LaunchUtil.launch(this, WechatChargeDetailActivity::class.java)
            }
            R.id.mWechatFun8 ->{
                LaunchUtil.launch(this, WechatVideoActivity::class.java)
            }
            R.id.mWechatFun9, R.id.mWechatSimulatorLayout ->{
                Log.e("loadFromDb-开始跳转", TimeUtil.getNowAllTime())
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
            R.id.mTeachIv ->{
                LaunchUtil.startOverallWebViewActivity(this, "http://www.jietuqi.cn/index/help?os=android", "使用教程")
            }
//            R.id.mWechatFun16 ->{
//                LaunchUtil.launch(this, WechatCreateBillsListActivity::class.java)
//            }
        }
    }
}
