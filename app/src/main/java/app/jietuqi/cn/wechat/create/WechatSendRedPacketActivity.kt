package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.TimeUtil
import com.zhouyou.http.EventBusUtil
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
    private var mSendEntity: WechatUserEntity = WechatUserEntity()
    /**
     * 领取人的信息
     */
    private var mReceiveEntity: WechatUserEntity = WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_send_redpacket

    override fun needLoadingView() = false

    override fun initAllViews() {
        mSendEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mReceiveEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mSenderNameTv.text = mSendEntity.wechatUserNickName
        GlideUtil.displayHead(this, mSendEntity.getAvatarFile(), mSendHeadIv)
        mReceiveNameTv.text = mReceiveEntity.wechatUserNickName
        GlideUtil.displayHead(this, mReceiveEntity.getAvatarFile(), mReceiveHeadIv)
        setWechatViewTitle("微信红包")
        needPreviewBtn(this)
        registerEventBus()
        mReceivePacketLayout.visibility = View.GONE
        mReceivePacketTimeTv.text = TimeUtil.getNowTimeOnlyHMS()
        mReceiveEntity.lastTime = TimeUtil.getCurrentTimeEndSec()
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
                operateRole(mSendEntity)
            }
            R.id.mReceiveUserLayout ->{
                mSendUser = false
                operateRole(mReceiveEntity, 1)
            }
            R.id.previewBtn ->{
                mSendEntity.msg = mLeaveMsgEt.text.toString()
                if (mReceivePacketLayout.visibility == View.VISIBLE){//发红包
                    mSendEntity.money = mInputPacketEt.text.toString()
                    LaunchUtil.startWechatScreenShotSendRedPacketActivity(this, mSendEntity, mReceiveEntity)
                }else{//收红包
                    mSendEntity.money = mInputPacketEt.text.toString()
                    LaunchUtil.startWechatScreenShotReceiveRedPacketActivity(this, mSendEntity)
                }
            }
            R.id.mSelectTimeLayout ->{
                initTimePickerView()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.MY_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mSendEntity = mMySideEntity
                    mSenderNameTv.text = mSendEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mSendEntity.getAvatarFile(), mSendHeadIv)
                }

            }
            RequestCode.OTHER_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mReceiveEntity = mOtherSideEntity
                    mReceiveNameTv.text = mOtherSideEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mReceiveHeadIv)
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mReceivePacketTimeTv.text = timeEntity.timeWithoutS
        mReceiveEntity.lastTime = timeEntity.timeLong
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
