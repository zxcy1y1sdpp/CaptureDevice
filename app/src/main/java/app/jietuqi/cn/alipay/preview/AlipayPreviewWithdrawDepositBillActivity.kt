package app.jietuqi.cn.alipay.preview

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateWithdrawDepositBillEntity
import app.jietuqi.cn.base.alipay.BaseAlipayActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_preview_withdraw_deposit_bill.*

/**
 * 作者： liuyuanbo on 2018/11/4 14:33.
 * 时间： 2018/11/4 14:33
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 预览 -- 提现账单
 */

class AlipayPreviewWithdrawDepositBillActivity : BaseAlipayActivity() {
    override fun setLayoutResourceId() = R.layout.activity_alipay_preview_withdraw_deposit_bill

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setAlipayPreviewTitle("账单详情", type = 0)
    }

    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: AlipayCreateWithdrawDepositBillEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayCreateWithdrawDepositBillEntity
        GlideUtil.display(this, entity.bankPic, mAlipayPreviewWithdrawDepositBillBankPicIv)
        mAlipayPreviewWithdrawDepositBillBankNameTv.text = entity.bank
        mAlipayPreviewWithdrawDepositBillMoneyTv.text = OtherUtil.formatPrice(entity.money)
        if (entity.serviceCharge){
            mAlipayPreviewWithdrawDepositShowServiceChargeLayout.visibility = View.VISIBLE
            mAlipayPreviewWithdrawDepositBillServiceChargeTv.text = OtherUtil.getServiceCharge(entity.money)
        }else{
            mAlipayPreviewWithdrawDepositShowServiceChargeLayout.visibility = View.GONE
        }
        mAlipayPreviewWithdrawDepositBillPayTimeTv.text = entity.withdrawDepositTime
        mAlipayPreviewWithdrawDepositBillBeingProcessedTimeTv.text = entity.withdrawDepositTime
        mAlipayPreviewWithdrawDepositBillSuccessTimeTv.text = entity.successTime

        mAlipayPreviewWithdrawDepositBillBankInfoTv.text = StringUtils.insertFrontAndBack(entity.bankNum4, entity.bank, entity.name)
        mAlipayPreviewWithdrawDepositBillClassifyTv.text = entity.classify
        mAlipayPreviewWithdrawDepositBillCreateTimeTv.text = entity.createTime
        mAlipayPreviewWithdrawDepositBillNumTv.text = RandomUtil.getRandomNum(20)
    }
}
