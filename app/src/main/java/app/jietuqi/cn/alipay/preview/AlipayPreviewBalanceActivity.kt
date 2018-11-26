package app.jietuqi.cn.alipay.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayPreviewBalanceEntity
import app.jietuqi.cn.base.alipay.BaseAlipayActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.OtherUtil.formatPrice
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_preview_balance.*

/**
 * 作者： liuyuanbo on 2018/11/1 10:47.
 * 时间： 2018/11/1 10:47
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 预览 -- 余额页面
 */

class AlipayPreviewBalanceActivity : BaseAlipayActivity() {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.alipayBalanceRed)
    }
    override fun setLayoutResourceId() = R.layout.activity_alipay_preview_balance

    override fun needLoadingView() = false

    override fun initAllViews() {

    }

    override fun initViewsListener() {
        mAlipayBalanceFinishIv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: AlipayPreviewBalanceEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayPreviewBalanceEntity
        mAlipayBalanceMoneyTv.text = formatPrice(entity.balanceMoney)
        if (entity.showYuEBao){
            mAlipayBalanceYuEBaoLayout.visibility = View.VISIBLE
            if (entity.dredgeYuEBao){
                mAlipayBalanceYuEBaoTv.text = StringUtils.insertFrontAndBack(formatPrice(entity.todayMoney), "今天转入", "元已自动转入余额宝")
                mAlipayBalanceCheckTv.text = "去查看"
            }else{
                mAlipayBalanceYuEBaoTv.text = "余额自动转入余额宝，收益天天算"
                mAlipayBalanceCheckTv.text = "去开启"
            }
        }else{
            mAlipayBalanceYuEBaoLayout.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        finish()
    }
}
