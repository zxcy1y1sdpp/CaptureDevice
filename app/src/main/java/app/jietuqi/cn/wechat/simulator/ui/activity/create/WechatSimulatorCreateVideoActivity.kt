package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.widget.dialog.ChangeWechatTransferDialog
import kotlinx.android.synthetic.main.activity_wechat_simulator_create_video.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/9 12:54.
 * 时间： 2018/12/9 12:54
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 视频
 */
class WechatSimulatorCreateVideoActivity : BaseWechatSimulatorCreateActivity(), ChangeWechatTransferDialog.OnItemSelectListener {
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_create_video
    override fun needLoadingView() = false
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 9
        setBlackTitle("视频聊天", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mMsgEntity.msgType = 9
        mMsgEntity.alreadyRead = true
        mWechatCreateTimeLayout.setOnClickListener(this)
        mWechatCreateStatusLayout.setOnClickListener(this)
        mWechatCreateVideoTv.setOnClickListener(this)
        mWechatCreateVoiceTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            if (mMsgEntity.msgType == 9){//视频聊天
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateVideoTv, mWechatCreateVoiceTv)
            }else{//语音聊天
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateVoiceTv, mWechatCreateVideoTv)
            }

            if (mMsgEntity.msg.contains("已接通")){
                mWechatCreateStatusTv.text = "已接通"
                mWechatCreateTimeTv.text = mMsgEntity.msg.substring(mMsgEntity.msg.length - 5,mMsgEntity.msg.length)
            }
            if (mMsgEntity.msg.contains("已取消")){
                mWechatCreateStatusTv.text = "已取消"
            }
            if (mMsgEntity.msg.contains("已拒绝")){
                mWechatCreateStatusTv.text = "已拒绝"
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateVideoTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateVideoTv, mWechatCreateVoiceTv)
                mMsgEntity.msgType = 9
            }
            R.id.mWechatCreateVoiceTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateVoiceTv, mWechatCreateVideoTv)
                mMsgEntity.msgType = 10
            }
            R.id.mWechatCreateTimeLayout ->{
                initTimePickerView("", 2)
            }
            R.id.mWechatCreateStatusLayout ->{
                var dialog = ChangeWechatTransferDialog()
                dialog.setOnItemSelectListener(this)
                dialog.setType(1)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.overallAllRightWithBgTv ->{
                if (mMsgEntity.isComMsg){//对方
                    when(mWechatCreateStatusTv.text.toString()){
                        "已接通" ->{
                            mMsgEntity.msg = StringUtils.insertFront(mWechatCreateTimeTv.text.toString(), "聊天时长 ")
                        }
                        "已取消" ->{
                            mMsgEntity.msg = "对方已取消"
                            mMsgEntity.alreadyRead = false
                        }
                        "已拒绝" ->{
                            mMsgEntity.msg = "已拒绝"
                        }
                    }
                }else{//我
                    when(mWechatCreateStatusTv.text.toString()){
                        "已接通" ->{
                            mMsgEntity.msg = StringUtils.insertFront(mWechatCreateTimeTv.text.toString(), "聊天时长 ")
                        }
                        "已取消" ->{
                            mMsgEntity.msg = "已取消"
                        }
                        "已拒绝" ->{
                            mMsgEntity.msg = "已拒绝"
                        }
                    }
                }
            }
        }
        super.onClick(v)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mWechatCreateTimeTv.text = timeEntity.timeOnlyMS
        mMsgEntity.msg = StringUtils.insertFront(timeEntity.timeOnlyMS, "聊天时长")
    }
    override fun click(type: String) {
        mWechatCreateStatusTv.text = type
        if (mMsgEntity.isComMsg){
            if ("已取消" == type){
                mMsgEntity.alreadyRead = false
            }
        }
    }
}
