package app.jietuqi.cn.wechat.screenshot

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.WechatTimeUtil
import kotlinx.android.synthetic.main.activity_wechat_preview_red_packet.*

/**
 * 作者： liuyuanbo on 2018/10/2 20:40.
 * 时间： 2018/10/2 20:40
 * 邮箱： 972383753@qq.com
 * 用途： 微信红包预览功能
 */

class WechatScreenShotRedPacketPreviewActivity : BaseWechatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.wechatPreviewTitleLayout)
    }
    override fun setLayoutResourceId() = R.layout.activity_wechat_screenshot_preview_red_packet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }


    override fun initViewsListener() {
        mWechatPreviewRedPacketFinishIv.setOnClickListener{
            finish()
        }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val sender: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY_SENDER) as WechatUserEntity//发送人的信息
        if (TextUtils.isEmpty(sender.msg)){
            mPreviewDescriptionTv.text = "恭喜发财，大吉大利"
        }else{
            mPreviewDescriptionTv.text = sender.msg//留言
        }
        GlideUtil.displayHead(this, sender.getAvatarFile(), mPreviewHeadIv)//发送人头像
        mPreviewPacketNameTv.text = sender.wechatUserNickName//发送人昵称

        val type = intent.getIntExtra(IntentKey.TYPE, 0)//0 -- 收红包，1 -- 发红包
        if (type == 0){//收红包的详情
            mMoneyLayout.visibility = View.VISIBLE
            mLeaveMessageTv.visibility = View.VISIBLE
            mMoneyTv.text = StringUtils.keep2Point(sender.money)
        }else{//发红包的详情
            mMoneyLayout.visibility = View.GONE
            mLeaveMessageTv.visibility = View.GONE
            mReceiverLayout.visibility = View.VISIBLE
            val receiver: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY_RECEIVER) as WechatUserEntity//领取人的信息
            GlideUtil.displayHead(this, receiver.getAvatarFile(), mReceiveNameIv)//设置领取人的头像
            mReceiveNameTv.text = receiver.wechatUserNickName//设置领取人的昵称
            mReceiveTimeTv.text = WechatTimeUtil.getNewChatTimeHongbaoPreview(receiver.lastTime)//设置领取时间
            mReceiveMoneyTv.text = StringUtils.insertBack(StringUtils.keep2Point(sender.money), "元")//设置金额
            mPreviewMoneyTv.text = StringUtils.insertFrontAndBack(StringUtils.keep2Point(sender.money), "1个红包共", "元")
        }
    }
}
