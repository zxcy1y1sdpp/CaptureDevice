package dasheng.com.capturedevice.base.alipay

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.util.statusbar.StatusBarUtil
import kotlinx.android.synthetic.main.include_base_alipay_title.*

/**
 * 作者： liuyuanbo on 2018/10/31 14:01.
 * 时间： 2018/10/31 14:01
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝基类
 */

abstract class BaseAlipayActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
    }

    /**
     * 设置状态栏的颜色
     */
    fun setStatusBarColor(color: Int = ColorFinal.wechatTitleBar){
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true)
        //设置状态栏透明
//        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarColor(this, color)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtil.setStatusBarColor(this, 0x55555555)
//        }
    }

    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 右侧的文字
     */
    protected fun setAlipayPreviewTitle(titleContext: String = "账单详情", titleColor: Int = R.color.black, bcColor: Int = R.color.white,
                                        finishDrawable: Int = R.drawable.alipay_title_finish_blue, showFinish: Boolean = true, showRight: Boolean = false,
                                        showBottomLine: Boolean = true, rightContext: String = "明细", type: Int = 0) {
        mAlipayPreviewBgLayout.setBackgroundColor(ContextCompat.getColor(this, bcColor))
        if (!showFinish){
            mAlipayPreviewFinishIv.visibility = View.GONE
        }
        if (showRight){
            mAlipayPreviewRightTv.visibility = View.VISIBLE
            mAlipayPreviewRightTv.text = rightContext
        }
        if (!showBottomLine){
            mAlipayPreviewBottomLineTv.visibility = View.GONE
        }
        mAlipayPreviewFinishIv.setImageDrawable(ContextCompat.getDrawable(this, finishDrawable))
        mAlipayPreviewTitleTv.setTextColor(ContextCompat.getColor(this, titleColor))
        mAlipayPreviewTitleTv.text = titleContext

        when(type){
            0 ->{
                mAlipayPreviewRightTv.visibility = View.GONE
            }
            1 ->{
                var sureTv = findViewById<TextView>(R.id.sureTv)
                sureTv.visibility = View.VISIBLE
                sureTv.setOnClickListener(this)
            }
            2 ->{
                var thirdPoint = findViewById<ImageView>(R.id.thirdPointIv)
                thirdPoint.visibility = View.VISIBLE
                thirdPoint.setOnClickListener(this)
            }
        }
        mAlipayPreviewFinishIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayPreviewFinishIv -> finish()
        }
    }
}
