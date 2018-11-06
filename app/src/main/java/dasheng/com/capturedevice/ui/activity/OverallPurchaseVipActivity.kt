package dasheng.com.capturedevice.ui.activity

import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.readystatesoftware.systembartint.SystemBarTintManager
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseOverallActivity
import dasheng.com.capturedevice.constant.ColorFinal
import kotlinx.android.synthetic.main.activity_overall_purchase_vip.*

/**
 * 作者： liuyuanbo on 2018/11/6 14:49.
 * 时间： 2018/11/6 14:49
 * 邮箱： 972383753@qq.com
 * 用途： 开通vip页面
 */

class OverallPurchaseVipActivity : BaseOverallActivity() {
    var statusBarView: View? = null
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.alipayBalanceRed)
        var tintManager: SystemBarTintManager = SystemBarTintManager(this)
        // 启用状态栏渐变
        tintManager.isStatusBarTintEnabled = true
        //设置状态栏颜色与ActionBar颜色相连
        tintManager.setStatusBarTintResource(R.color.gray2)
        Looper.myQueue().addIdleHandler {
            if (true) {
                initStatusBar()
                window.decorView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom -> initStatusBar() }
            }
            false
        }
        initStatusBar()
    }
    override fun setLayoutResourceId() = R.layout.activity_overall_purchase_vip

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setFlickerAnimation()
        val info = arrayListOf<String>()
        info.add("刚刚139****0206用户办理了三个月VIP")
        info.add("刚刚159****2456用户办理了三个月VIP")
        info.add("刚刚139****1234用户办理了三个月VIP")
        info.add("刚刚139****4321用户办理了三个月VIP")
        info.add("刚刚139****5645用户办理了三个月VIP")
        info.add("刚刚139****5676用户办理了三个月VIP")
        mOverallPurchaseVipMarqueeTv.startWithList(info)
    }

    override fun initViewsListener() {
        mOverallPurchaseVipJDIv.setOnClickListener(this)
        mOverallPurchaseVipNDIv.setOnClickListener(this)
        mOverallPurchaseVipYJIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPurchaseVipJDIv ->{
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_jdhy_mark)
            }
            R.id.mOverallPurchaseVipNDIv ->{
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_ndhy_mark)
            }
            R.id.mOverallPurchaseVipYJIv ->{
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_yjhy_mark)
            }
        }
    }
    private fun initStatusBar() {
        if (statusBarView == null) {
            //android系统级的资源id得这么拿,不然拿不到
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById<View>(identifier)
        }
        if (statusBarView != null) {
            statusBarView?.setBackgroundResource(R.drawable.overall_purchase_bg)
        }
    }

    private fun setFlickerAnimation() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 750//闪烁时间间隔
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        mOverallPurchaseVipMarkIv.animation = animation
    }

}
