package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.pay.alipay.AlipayUtil
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallVipCardEntity
import app.jietuqi.cn.ui.entity.OverallVipCardOrderEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_open_agency.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者： liuyuanbo on 2019/3/14 10:11.
 * 时间： 2019/3/14 10:11
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallOpenAgencyActivity : BaseOverallInternetActivity() {
    private lateinit var mEntity: OverallVipCardEntity
    override fun setLayoutResourceId() = R.layout.activity_open_agency

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        getMoney()
    }

    override fun initViewsListener() {
        mOpenAgencyBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.mOpenAgencyBtn -> {
                createOrder("支付宝")
            }
        }
    }

    private fun getMoney() {
        var request: PostRequest = EasyHttp.post(HttpConfig.PRICE, false).params("way", "agent")
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallVipCardEntity>>, ArrayList<OverallVipCardEntity>>(object : SimpleCallBack<ArrayList<OverallVipCardEntity>>() {
            override fun onSuccess(t: ArrayList<OverallVipCardEntity>) {
                mEntity = t[0]
            }

            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }
        }) {})
    }

    override fun onResume() {
        super.onResume()
        registerEventBus()
    }

    /**
     * 创建订单的接口
     */
    private fun createOrder(payChannel: String = "微信") {
        showQQWaitDialog("订单创建中")
        var request: PostRequest = EasyHttp.post(HttpConfig.ORDER, false)
                .params("way", "add")
                .params("pay", "appalipay")
                .params("money", mEntity.id)
                .params("uid", UserOperateUtil.getUserId())
                .params("pay_channel", payChannel)
                .params("os", "android")

        request.execute(object : CallBackProxy<OverallApiEntity<OverallVipCardOrderEntity>, OverallVipCardOrderEntity>(object : SimpleCallBack<OverallVipCardOrderEntity>() {
            override fun onSuccess(t: OverallVipCardOrderEntity?) {
                t?.info?.let { AlipayUtil().init(this@OverallOpenAgencyActivity, it, 3) }
                dismissQQDialog()
            }

            override fun onError(e: ApiException) {
                dismissQQDialog()
                e.message?.let { showToast(it) }
            }
        }) {})
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onPaySuccess(result: String) {
        EventBus.getDefault().removeAllStickyEvents()
        if (result == "代理开通成功") {
            showQQTipDialog("代理开通成功")
            GlobalScope.launch {
                delay(2000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                LaunchUtil.startOverallEditAgencyInfoActivity(this@OverallOpenAgencyActivity, null, 0)
            }
        } else if (result == "付款失败") {
            showQQTipDialog("付款失败", 1)
        }
    }
}
