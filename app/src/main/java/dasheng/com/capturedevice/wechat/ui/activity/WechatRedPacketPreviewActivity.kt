package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.StringUtils
import dasheng.com.capturedevice.util.WechatTimeUtil
import kotlinx.android.synthetic.main.activity_wechat_preview_red_packet.*

/**
 * 作者： liuyuanbo on 2018/10/2 20:40.
 * 时间： 2018/10/2 20:40
 * 邮箱： 972383753@qq.com
 * 用途： 微信红包预览功能
 */

class WechatRedPacketPreviewActivity : BaseWechatActivity() {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.wechatPreviewTitleLayout)
    }
    override fun setLayoutResourceId() = R.layout.activity_wechat_preview_red_packet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }


    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val sender: WechatUserTable = intent.getSerializableExtra(IntentKey.ENTITY_SENDER) as WechatUserTable//发送人的信息
        val money = intent.getStringExtra(IntentKey.MONEY)//红包金额
        mPreviewDescriptionTv.text = sender.msg//留言
        GlideUtil.display(this, sender.avatar, mPreviewHeadIv)//发送人头像
        mPreviewPacketNameTv.text = sender.wechatUserNickName//发送人昵称

        val type = intent.getIntExtra(IntentKey.TYPE, 0)//0 -- 收红包，1 -- 发红包
        if (type == 0){//收红包的详情
            mMoneyLayout.visibility = View.VISIBLE
            mLeaveMessageTv.visibility = View.VISIBLE
            mMoneyTv.text = StringUtils.keep2Point(money)

        }else{//发红包的详情
            mMoneyLayout.visibility = View.GONE
            mLeaveMessageTv.visibility = View.GONE
            mReceiverLayout.visibility = View.VISIBLE
            val receiver: WechatUserTable = intent.getSerializableExtra(IntentKey.ENTITY_RECEIVER) as WechatUserTable//领取人的信息
            GlideUtil.display(this, receiver.avatar, mReceiveNameIv)//设置领取人的头像
            mReceiveNameTv.text = receiver.wechatUserNickName//设置领取人的昵称
            mReceiveTimeTv.text = WechatTimeUtil.getNewChatTime(receiver.lastTime)//设置领取时间
            mReceiveMoneyTv.text = StringUtils.insertBack(StringUtils.keep2Point(money), "元")//设置金额
            mPreviewMoneyTv.text = StringUtils.insertFrontAndBack(StringUtils.keep2Point(money), "一个红包共", "元")
        }
    }
}
