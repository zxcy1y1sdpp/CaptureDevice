package app.jietuqi.cn.pay.alipay

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import app.jietuqi.cn.constant.AppPayConfig
import com.alipay.sdk.app.PayTask
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EventBusUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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
    /**
     * 0 -- 购买会员卡
     * 1 -- 微币充值
     * 2 -- 购买清粉激活码
     * 3 -- 开通代理
     */
    private var mType = 0
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
                        when (mType) {
                            0 -> {
                                EventBusUtil.postSticky("购买会员卡成功")
                                val dialog = QMUITipDialog.Builder(mActivity)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("支付成功")
                                        .create()
                                dialog.show()
                                GlobalScope.launch { //
                                    delay(1500L) // 非阻塞的延迟一秒（默认单位是毫秒）
                                    dialog.dismiss()
                                }
                            }
                            1 -> {
                                EventBusUtil.postSticky("微币充值成功")
                                val dialog = QMUITipDialog.Builder(mActivity)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("充值成功")
                                        .create()
                                dialog.show()
                                GlobalScope.launch { //
                                    delay(1500L) // 非阻塞的延迟一秒（默认单位是毫秒）
                                    dialog.dismiss()
                                }
                            }
                            2 -> {
                                EventBusUtil.postSticky("激活码购买成功")
                                val dialog = QMUITipDialog.Builder(mActivity)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("激活码购买成功")
                                        .create()
                                dialog.show()
                                GlobalScope.launch { //
                                    delay(1500L) // 非阻塞的延迟一秒（默认单位是毫秒）
                                    dialog.dismiss()
                                }
                            }
                            3 -> {
                                EventBusUtil.postSticky("代理开通成功")
                            }
                        }

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        EventBusUtil.postSticky("付款失败")
                        ToastUtils.showShort(mActivity, "支付失败")
                    }
                }
                else -> {
                }
            }
        }
    }

    fun init(context: Activity, info: String, type: Int = 1) {
        mActivity = context
        this.info = info
        mType = type
        payV2()
    }

    /**
     * 支付宝支付业务示例
     */
    private fun payV2() {
        if (TextUtils.isEmpty(AppPayConfig.APPID) || TextUtils.isEmpty(AppPayConfig.RSA2_PRIVATE) && TextUtils.isEmpty(AppPayConfig.RSA2_PRIVATE)) {
            ToastUtils.showShort(mActivity, "支付遇到问题，请联系客服！")
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
