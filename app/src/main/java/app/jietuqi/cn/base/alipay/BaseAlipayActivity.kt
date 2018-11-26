package app.jietuqi.cn.base.alipay

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseActivity
import app.jietuqi.cn.constant.ColorFinal
import com.jaeger.library.StatusBarUtil
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
        StatusBarUtil.setColor(this, color, 0)
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
