package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.alipay.BaseAlipayScreenShotCreateActivity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_alipay_create_voice.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/9 12:54.
 * 时间： 2018/12/9 12:54
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 语音
 */
class AlipayCreateVoiceActivity : BaseAlipayScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_voice

    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 7
        setBlackTitle("语音", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mAlipayCreateVoiceSecondsAlreadyReadIv.setOnClickListener(this)

        overallAllRightWithBgTv.setOnClickListener(this)
        mAlipayCreateVoiceSecondsSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                mAlipayCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(progress), "秒")
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {}
        }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mAlipayCreateVoiceSecondsSeekBar.setProgress(mMsgEntity.voiceLength.toFloat())
            mAlipayCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(mMsgEntity.voiceLength), "秒")
            OtherUtil.onOrOff(mMsgEntity.alreadyRead, mAlipayCreateVoiceSecondsAlreadyReadIv)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mAlipayCreateVoiceSecondsAlreadyReadIv ->{
                var show = !mAlipayCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                mAlipayCreateVoiceSecondsAlreadyReadIv.tag = show
                OtherUtil.onOrOff(show, mAlipayCreateVoiceSecondsAlreadyReadIv)
            }
            R.id.overallAllRightWithBgTv ->{
                var voiceLength = mAlipayCreateVoiceSecondsSeekBar.progress
                mMsgEntity.alreadyRead = mAlipayCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                mMsgEntity.voiceLength = voiceLength
            }
        }
        super.onClick(v)
    }
}
