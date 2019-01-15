package app.jietuqi.cn.alipay.preview

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateTransferBillEntity
import app.jietuqi.cn.base.alipay.BaseAlipayActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_preview_transfer_bill.*

/**
 * 作者： liuyuanbo on 2018/11/2 12:04.
 * 时间： 2018/11/2 12:04
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 预览 -- 转账账单页面
 */

class AlipayPreviewTransferBillActivity : BaseAlipayActivity() {
    /**
     * 0 -- 转账账单
     * 1 -- 收款账单
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_alipay_preview_transfer_bill

    override fun needLoadingView() = false

    override fun initAllViews() {
        setAlipayPreviewTitle("账单详情", type = 0)
    }

    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)//0 -- 转账账单 1 -- 收款账单
        val entity: AlipayCreateTransferBillEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayCreateTransferBillEntity
        GlideUtil.displayHead(this, entity.getAvatarFile(), mAlipayPreviewTransferBillAvatarIv)
        mAlipayPreviewTransferBillNickNameTv.text = entity.wechatUserNickName
        var account = if (entity.account.contains("@")){//邮箱
            entity.account.replace("(\\w{3})(\\w+)(@\\w+\\.[a-z]+(\\.[a-z]+)?)".toRegex(), "$1***$3")
        }else{
            entity.account.replace("(\\d{3})\\d{6}(\\d{2})".toRegex(), "$1******$2")
        }
        mAlipayPreviewTransferBillAccountTv.text = StringUtils.insertFront(account, entity.wechatUserNickName + " ")
        if (mType == 1){//收款账单
            mAlipayPreviewTransferBillMsgTitleTv.text = "收款理由"
            if (entity.transferStatus == "等待对方付款"){
                mAlipayPreviewTransferBillTransferStatusTv.setTextColor(ContextCompat.getColor(this, R.color.alipayOrange))
            }
            if (entity.showContactRecord){
                mAlipayPreviewTransferBillContactRecordLayout.visibility = View.VISIBLE
            }else{
                mAlipayPreviewTransferBillContactRecordLayout.visibility = View.GONE
            }
        }

        mAlipayPreviewTransferBillTransferStatusTv.text = entity.transferStatus
        mAlipayPreviewTransferBillTransferTypeTv.text = entity.paymentMethods
        mAlipayPreviewTransferBillMsgTv.text = entity.msg
        mAlipayPreviewTransferBillClassifyTv.text = entity.billClassify
        mAlipayPreviewTransferBillCreateTimeTv.text = entity.createTime
        mAlipayPreviewTransferBillNumTv.text = entity.num

        if (entity.type == 1){//转出
            mAlipayPreviewTransferBillMoneyTv.text = StringUtils.insertFront(OtherUtil.formatPrice(entity.money), "-")
            mAlipayPreviewTransferBillTransferTypeTitleTv.text = "付款方式"
        }else{
            mAlipayPreviewTransferBillMoneyTv.text = StringUtils.insertFront(OtherUtil.formatPrice(entity.money), "+")
            mAlipayPreviewTransferBillTransferTypeTitleTv.text = "收款方式"
        }
    }

    override fun onResume() {
        super.onResume()
        if (mType == 0){
            needVip()
        }
    }
}
