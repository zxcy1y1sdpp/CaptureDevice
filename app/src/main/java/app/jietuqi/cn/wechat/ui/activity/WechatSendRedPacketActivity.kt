package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_send_redpacket.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @作者：liuyuanbo
 * @时间：2018/9/30
 * @邮箱：972383753@qq.com
 * @用途：发红包页面
 */

class WechatSendRedPacketActivity : BaseWechatActivity() {
    /**
     * 是不是修改发送人的名称和头像
     * true -- 发送人的昵称和头像
     * false -- 领取人的昵称和头像
     */
    private var mSendUser = true
    /**
     * 发送人的信息
     */
    private var mSendEntity: WechatUserTable = WechatUserTable()
    /**
     * 领取人的信息
     */
    private var mReceiveEntity: WechatUserTable = WechatUserTable()
    override fun setLayoutResourceId() = R.layout.activity_wechat_send_redpacket

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("微信红包")
        needPreviewBtn(this)
        registerEventBus()
        mReceivePacketLayout.visibility = View.GONE
    }
    override fun initViewsListener() {
        mRecivePacketTv.setOnClickListener(this)
        mSendPacketTv.setOnClickListener(this)
        mSendUserLayout.setOnClickListener(this)
        mReceiveUserLayout.setOnClickListener(this)
        mSelectTimeLayout.setOnClickListener(this)
        onlyOneEditTextNeedTextWatcher(mInputPacketEt, this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mRecivePacketTv ->{
                mReceivePacketLayout.visibility = View.GONE
                OtherUtil.changeWechatTwoBtnBg(this, mRecivePacketTv, mSendPacketTv)
            }
            R.id.mSendPacketTv ->{
                mReceivePacketLayout.visibility = View.VISIBLE
                OtherUtil.changeWechatTwoBtnBg(this, mSendPacketTv, mRecivePacketTv)
            }
            R.id.mSendUserLayout ->{
                mSendUser = true
                LaunchUtil.launch(this, WechatRoleActivity::class.java, RequestCode.CHANGE_ROLE)
            }
            R.id.mReceiveUserLayout ->{
                mSendUser = false
                LaunchUtil.launch(this, WechatRoleActivity::class.java, RequestCode.CHANGE_ROLE)
            }
            R.id.previewBtn ->{
                mSendEntity.msg = mLeaveMsgEt.text.toString()
                if (mReceivePacketLayout.visibility == View.VISIBLE){//发红包
                    LaunchUtil.startWechatRedPacketPreviewActivity(this, mSendEntity, mReceiveEntity, mInputPacketEt.text.toString())
                }else{//收红包
                    LaunchUtil.startWechatRedPacketPreviewActivity(this, mSendEntity, mInputPacketEt.text.toString())
                }
            }
            R.id.mSelectTimeLayout ->{
                initTimePickerView()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CHANGE_ROLE ->{
                    if (mSendUser){
                        mSendEntity = data?.getSerializableExtra(IntentKey.ENTITY) as WechatUserTable
                        mSenderNameTv.text = mSendEntity.wechatUserNickName
                        GlideUtil.display(this, mSendEntity.avatar, mSendHeadIv)
                    }else{
                        mReceiveEntity = data?.getSerializableExtra(IntentKey.ENTITY) as WechatUserTable
                        mReceiveNameTv.text = mReceiveEntity.wechatUserNickName
                        GlideUtil.display(this, mReceiveEntity.avatar, mReceiveHeadIv)
                    }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelecTimeEvent(timeEntity: EventBusTimeEntity) {
        mReceivePacketTimeTv.text = timeEntity.timeWithoutS
        mReceiveEntity.lastTime = timeEntity.timeLong
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
