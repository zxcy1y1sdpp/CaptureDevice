package app.jietuqi.cn.pay.alipay

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import app.jietuqi.cn.constant.AppPayConfig
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.alipay.sdk.app.PayTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/12/19 15:14.
 * 时间： 2018/12/19 15:14
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class AlipayUtil {
    private val SDK_PAY_FLAG = 1
    private var mActivity: Activity? = null
    private var info: String? = null
    private lateinit var mDialog: SweetAlertDialog
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        SweetAlertDialog(mActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("支付成功")
                                .setContentText("恭喜您成为尊贵的Vip用户")
                                .setConfirmText("朕知道了")
                                .setConfirmClickListener {
                                    it.dismissWithAnimation()
                                }.show()
                        EventBusUtil.postSticky("购买会员卡成功")
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        EventBusUtil.postSticky("购买会员卡失败")
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                }
            }
        }
    }

    fun init(context: Activity, info: String) {
        mActivity = context
        this.info = info
        payV2()
    }

    /**
     * 支付宝支付业务示例
     */
    private fun payV2() {
        if (TextUtils.isEmpty(AppPayConfig.APPID) || TextUtils.isEmpty(AppPayConfig.RSA2_PRIVATE) && TextUtils.isEmpty(AppPayConfig.RSA2_PRIVATE)) {
            Toast.makeText(mActivity, "支付遇到问题，请联系客服！", Toast.LENGTH_SHORT).show()
            return
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val alipay = PayTask(mActivity)
            val result = alipay.payV2(info, true)
            Log.i("msp", result.toString())

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
    }
}