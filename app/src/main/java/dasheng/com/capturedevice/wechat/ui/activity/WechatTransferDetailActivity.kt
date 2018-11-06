package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.view.View
import android.widget.Toast
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.entity.WechatTransferEntity
import dasheng.com.capturedevice.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_transfer_detail.*

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
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatTransferEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatTransferEntity
        mTransferMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        if (entity.type == 0){//收钱
            if (entity.transferType == "已收钱"){
                mTransferIv.setImageResource(R.drawable.wechat_transfer_success)
                mGrayTv.visibility = View.GONE
                mBlueTv.visibility = View.VISIBLE
                Toast.makeText(this, "成功存入零钱", Toast.LENGTH_SHORT).show()
                mTransferNickNameTv.text = "已收钱"
                mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "收钱时间：")
            }else if (entity.transferType == "待收款"){
                mTransferIv.setImageResource(R.drawable.wechat_transfer_receiving)
                mTransferNickNameTv.text = "待确认收款"
                mGrayTv.visibility = View.VISIBLE
                mBlueTv.visibility = View.VISIBLE
                mReceiveTimeTv.visibility = View.GONE
                mReceiveMoneyTv.visibility = View.VISIBLE
                mGrayTv.text = "一天内未确认，将退还给对方。"
                mBlueTv.text = "立即退还"
                mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
            }else if (entity.transferType == "已退款"){
                mTransferIv.setImageResource(R.drawable.wechat_transfer_return)
                mTransferNickNameTv.text = "已退还"
                mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
                mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "退还时间：")
            }
        }else{//转出
          if (entity.transferType == "已收钱"){
                mTransferIv.setImageResource(R.drawable.wechat_transfer_success)
              mTransferNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "已收钱")
              mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
              mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "退还时间：")
            }else if (entity.transferType == "待收款"){
              mTransferIv.setImageResource(R.drawable.wechat_transfer_receiving)
              mTransferNickNameTv.text = StringUtils.insertFrontAndBack(entity.wechatUserNickName, "待", "确认收款")
              mBlueTv.visibility = View.VISIBLE
              mReceiveTimeTv.visibility = View.GONE
              mGrayTv.text = "一天内朋友未确认，将退还给你。"
              mBlueTv.text = "重发转账消息"
              mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
            }else if (entity.transferType == "已退款"){
              mTransferIv.setImageResource(R.drawable.wechat_transfer_return)
              mTransferNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "已退还")
              mBlueTv.visibility = View.VISIBLE
              mGrayTv.text = "已退款到零钱，"
              mBlueTv.text = "查看零钱"
              mTransferTimeTv.text = StringUtils.insertFront(entity.outTime, "转账时间：")
              mReceiveTimeTv.text = StringUtils.insertFront(entity.receiveTime, "退还时间：")
            }
        }
    }
}
