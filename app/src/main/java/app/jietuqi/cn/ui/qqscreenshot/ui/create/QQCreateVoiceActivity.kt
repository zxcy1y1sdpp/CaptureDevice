package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.qq.BaseQQScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_qq_create_voice.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/9 12:54.
 * 时间： 2018/12/9 12:54
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 语音
 */
class QQCreateVoiceActivity : BaseQQScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_qq_create_voice

    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 7
        setBlackTitle("语音", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mQQCreateVoiceSecondsAlreadyReadIv.setOnClickListener(this)
        mQQCreateVoiceSecondsTransferIv.setOnClickListener(this)

        overallAllRightWithBgTv.setOnClickListener(this)
        mQQCreateVoiceSecondsSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                mQQCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(progress), "秒")
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {}
        }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mQQCreateVoiceSecondsSeekBar.setProgress(mMsgEntity.voiceLength.toFloat())
            mQQCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(mMsgEntity.voiceLength), "秒")
            OtherUtil.onOrOff(mMsgEntity.alreadyRead, mQQCreateVoiceSecondsAlreadyReadIv)
            val toText = mMsgEntity.voiceToText
            mQQCreateVoiceSecondsTransferIv.tag = !TextUtils.isEmpty(toText)
            OtherUtil.onOrOff(!TextUtils.isEmpty(toText), mQQCreateVoiceSecondsTransferIv)
            if (!TextUtils.isEmpty(toText)){
                mQQCreateVoiceSecondsTransferTextEv.visibility = View.VISIBLE
                mQQCreateVoiceSecondsTransferTextEv.setText(toText)
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mQQCreateVoiceSecondsAlreadyReadIv ->{
                var hasTransferText = mQQCreateVoiceSecondsTransferIv.tag.toString().toBoolean()
                if (hasTransferText){
                    showToast("请关闭语音转换选项")
                    return
                }
                var show = !mQQCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                mQQCreateVoiceSecondsAlreadyReadIv.tag = show
                OtherUtil.onOrOff(show, mQQCreateVoiceSecondsAlreadyReadIv)
            }
            R.id.mQQCreateVoiceSecondsTransferIv ->{
                var show = !mQQCreateVoiceSecondsTransferIv.tag.toString().toBoolean()
                mQQCreateVoiceSecondsTransferIv.tag = show
                if(show){
                    mQQCreateVoiceSecondsTransferTextEv.visibility = View.VISIBLE
                    mQQCreateVoiceSecondsAlreadyReadIv.tag = true
                    OtherUtil.onOrOff(true, mQQCreateVoiceSecondsAlreadyReadIv)
                }else{
                    mQQCreateVoiceSecondsTransferTextEv.visibility = View.GONE
                    mQQCreateVoiceSecondsTransferTextEv.setText("")
                }
                OtherUtil.onOrOff(show, mQQCreateVoiceSecondsTransferIv)
            }
            R.id.overallAllRightWithBgTv ->{
                var voiceLength = mQQCreateVoiceSecondsSeekBar.progress
                mMsgEntity.alreadyRead = mQQCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                mMsgEntity.voiceLength = voiceLength
                mMsgEntity.voiceToText = mQQCreateVoiceSecondsTransferTextEv.text.toString()
            }
        }
        super.onClick(v)
    }
}
