package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorCreateActivity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_wechat_simulator_create_voice.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/9 12:54.
 * 时间： 2018/12/9 12:54
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 语音
 */
class WechatSimulatorCreateVoiceActivity : BaseWechatSimulatorCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_create_voice

    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 7
//        mMsgEntity.isComMsg = false
        setBlackTitle("语音", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatSimulatorCreateVoiceSecondsAlreadyReadIv.setOnClickListener(this)
        mWechatSimulatorCreateVoiceSecondsTransferIv.setOnClickListener(this)

        overallAllRightWithBgTv.setOnClickListener(this)
        mWechatSimulatorCreateVoiceSecondsSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                mWechatSimulatorCreateVoiceSecondsTv.text = StringUtils.insertBack(StringUtils.insertZeroFont(progress), "秒")
            }
            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateChoiceMySideLayout ->{
                OtherUtil.onOrOff(true, mWechatSimulatorCreateVoiceSecondsAlreadyReadIv)
                mWechatSimulatorCreateVoiceSecondsTransferIv.tag = false
            }
            R.id.mWechatSimulatorCreateVoiceSecondsAlreadyReadIv ->{
                if (!mMsgEntity.isComMsg){//自己发送的语音消息，不可以选择未读
                    showToast("自己发送的语音消息不可以选择未读")
                    return
                }else{
                    var show = !mWechatSimulatorCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                    mWechatSimulatorCreateVoiceSecondsAlreadyReadIv.tag = show
                    OtherUtil.onOrOff(show, mWechatSimulatorCreateVoiceSecondsAlreadyReadIv)
                }
            }
            R.id.mWechatSimulatorCreateVoiceSecondsTransferIv ->{
                var show = !mWechatSimulatorCreateVoiceSecondsTransferIv.tag.toString().toBoolean()
                mWechatSimulatorCreateVoiceSecondsTransferIv.tag = show
                if(show){
                    mWechatSimulatorCreateVoiceSecondsTransferTextEv.visibility = View.VISIBLE
                }else{
                    mWechatSimulatorCreateVoiceSecondsTransferTextEv.visibility = View.GONE
                }
                OtherUtil.onOrOff(show, mWechatSimulatorCreateVoiceSecondsTransferIv)
            }
            R.id.overallAllRightWithBgTv ->{
                var voiceLength = mWechatSimulatorCreateVoiceSecondsSeekBar.progress
                mMsgEntity.alreadyRead = mWechatSimulatorCreateVoiceSecondsAlreadyReadIv.tag.toString().toBoolean()
                if (mWechatSimulatorCreateVoiceSecondsTransferIv.tag.toString().toBoolean()){
                    mMsgEntity.voiceToText = mWechatSimulatorCreateVoiceSecondsTransferTextEv.text.toString()
                }
                mMsgEntity.voiceLength = voiceLength
            }
        }
        super.onClick(v)
    }
}
