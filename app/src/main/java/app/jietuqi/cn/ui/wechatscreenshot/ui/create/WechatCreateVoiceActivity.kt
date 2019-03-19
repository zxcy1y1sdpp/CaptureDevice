package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_wechat_create_voice.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/9 12:54.
 * 时间： 2018/12/9 12:54
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 语音
 */
class WechatCreateVoiceActivity : BaseWechatScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_voice

    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 7
        setBlackTitle("语音", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreateVoiceSecondsAlreadyReadIv.setOnClickListener(this)
        mWechatCreateVoiceSecondsTransferIv.setOnClickListener(this)

        overallAllRightWithBgTv.setOnClickListener(this)
        mWechatCreateVoiceSecondsSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                mWechatCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(progress), "秒")
            }
            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
            }
        }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mWechatCreateVoiceSecondsSeekBar.setProgress(mMsgEntity.voiceLength.toFloat())
            mWechatCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(mMsgEntity.voiceLength), "秒")
            OtherUtil.onOrOff(mMsgEntity.alreadyRead, mWechatCreateVoiceSecondsAlreadyReadIv)
            val toText = mMsgEntity.voiceToText
            OtherUtil.onOrOff(!TextUtils.isEmpty(toText), mWechatCreateVoiceSecondsTransferIv)
            if (!TextUtils.isEmpty(toText)){
                mWechatCreateVoiceSecondsTransferTextEv.visibility = View.VISIBLE
                mWechatCreateVoiceSecondsTransferTextEv.setText(toText)
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateVoiceSecondsAlreadyReadIv ->{
                if (!mMsgEntity.isComMsg){//自己发送的语音消息，不可以选择未读
                    showToast("自己发送的语音消息不可以选择未读")
                    return
                }else{
                    var show = !mWechatCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                    mWechatCreateVoiceSecondsAlreadyReadIv.tag = show
                    OtherUtil.onOrOff(show, mWechatCreateVoiceSecondsAlreadyReadIv)
                }
            }
            R.id.mWechatCreateVoiceSecondsTransferIv ->{
                var show = !mWechatCreateVoiceSecondsTransferIv.tag.toString().toBoolean()
                mWechatCreateVoiceSecondsTransferIv.tag = show
                if(show){
                    mWechatCreateVoiceSecondsTransferTextEv.visibility = View.VISIBLE
                }else{
                    mWechatCreateVoiceSecondsTransferTextEv.visibility = View.GONE
                    mMsgEntity.voiceToText = ""
                }
                OtherUtil.onOrOff(show, mWechatCreateVoiceSecondsTransferIv)
            }
            R.id.overallAllRightWithBgTv ->{
                var voiceLength = mWechatCreateVoiceSecondsSeekBar.progress
                mMsgEntity.alreadyRead = mWechatCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                if (mWechatCreateVoiceSecondsTransferIv.tag.toString().toBoolean()){
                    mMsgEntity.voiceToText = mWechatCreateVoiceSecondsTransferTextEv.text.toString()
                }
                mMsgEntity.voiceLength = voiceLength
            }
        }
        super.onClick(v)
    }
}
