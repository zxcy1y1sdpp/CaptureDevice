package dasheng.com.capturedevice.alipay.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.entity.AlipayPreviewBalanceEntity
import dasheng.com.capturedevice.base.alipay.BaseAlipayActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.util.OtherUtil.formatPrice
import dasheng.com.capturedevice.util.StringUtils
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
