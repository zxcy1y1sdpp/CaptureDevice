package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import kotlinx.android.synthetic.main.activity_wechat_create_emoji.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建表情页面
 */
class WechatCreateEmojiActivity : BaseWechatScreenShotCreateActivity(){
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_emoji

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        registerEventBus()
        mMsgEntity.msgType = 14
        setBlackTitle("大表情", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreateEmojiLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            GlideUtil.displayGif(this, ResourceHelper.getAppIconId(mMsgEntity.pic), mWechatCreateEmojiIv)
            mWechatCreateEmojiLayout.tag = mMsgEntity.pic
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateEmojiLayout ->{
                LaunchUtil.launch(this, WechatChoiceEmojiActivity::class.java)
            }
            R.id.overallAllRightWithBgTv ->{
                mMsgEntity.pic = mWechatCreateEmojiLayout.tag.toString()
            }
        }
        super.onClick(v)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChoiceEmoji(name: String) {
        mWechatCreateEmojiLayout.tag = name
        GlideUtil.displayGif(this, ResourceHelper.getAppIconId(name), mWechatCreateEmojiIv)
    }
}
