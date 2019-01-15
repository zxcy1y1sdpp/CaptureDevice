package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.entity.WechatTransferEntity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_transfer_detail.*
import kotlinx.android.synthetic.main.base_wechat_preview_transfer.*
import kotlinx.android.synthetic.main.base_wechat_preview_transfer_time.*

/**
 * 作者： liuyuanbo on 2018/10/20 15:09.
 * 时间： 2018/10/20 15:09
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 转账详情
 */

class WechatTransferDetailActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_transfer_detail

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("交易详情", 0)
    }

    override fun initViewsListener() {
        setLightStatusBarForM(this, true)
        setStatusBarColor(ColorFinal.WHITE)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatTransferEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatTransferEntity
        mTransferMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        if (entity.type == 0){//收钱
            when(entity.transferType) {
                "已收钱" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_success)
                    mTransferNickNameTv.text = "已收钱"
                    mCheckChargeTv.visibility = View.VISIBLE
                    mCheckChargeTv.setTextColor(Color.parseColor("#576b95"))
                    mWechatPreviewTransferLayout.visibility = View.INVISIBLE
                    mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                    mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "收钱时间：")
                    Toast.makeText(this, "成功存入零钱", Toast.LENGTH_SHORT).show()
                }
                "待收款" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_receiving)
                    mTransferNickNameTv.text = "待确认收款"
                    mWechatPreviewTransferLayout.visibility = View.VISIBLE
                    mReceiveTimeTv.visibility = View.INVISIBLE
                    mReceiveMoneyTv.visibility = View.VISIBLE
                    val spannableString = SpannableString("1天内未确认，将退还给对方。立即退还")
                    spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), 14, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mGrayTv.text = spannableString
                    mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                }
                "已退款" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_return)
                    mTransferNickNameTv.text = "已退还"
                    mWechatPreviewTransferLayout.visibility = View.INVISIBLE
                    mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                    mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "退还时间：")
                }
            }
        }else{//转出
            when(entity.transferType) {
                "已收钱" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_success)
                    mTransferNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "已收钱")
                    mCheckChargeTv.text = "已存入对方零钱中"
                    mCheckChargeTv.visibility = View.VISIBLE
                    mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                    mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "收钱时间：")
                }
                "待收款" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_receiving)
                    mTransferNickNameTv.text = StringUtils.insertFrontAndBack(entity.wechatUserNickName, "待", "确认收款")
                    mTransferTimeTv.visibility = View.INVISIBLE
                    val spannableString = SpannableString("1天内朋友未确认，将退还给你。重发转账消息")
                    spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), 15, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mCheckChargeTv.text = spannableString
                    mCheckChargeTv.visibility = View.VISIBLE
                    mReceiveTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                }
                "已退款" -> {
                    mTransferIv.setImageResource(R.drawable.wechat_transfer_return)
                    mTransferNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "已退还")
                    val spannableString = SpannableString("已退款到零钱，查看零钱")
                    spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), 7, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mCheckChargeTv.text = spannableString
                    mCheckChargeTv.visibility = View.VISIBLE
                    mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                    mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "退还时间：")
                }
            }
        }
    }
}
