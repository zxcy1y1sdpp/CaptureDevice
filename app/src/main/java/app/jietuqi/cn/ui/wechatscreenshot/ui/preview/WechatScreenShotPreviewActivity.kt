package app.jietuqi.cn.ui.wechatscreenshot.ui.preview

import android.animation.ObjectAnimator
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import app.jietuqi.cn.R
import app.jietuqi.cn.animation.FrameAnimation
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.GlobalVariable
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatScreenShotPreviewAdapter
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.*
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_screenshot_preview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/4 22:46.
 * 时间： 2018/12/4 22:46
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图预览页面
 */
class WechatScreenShotPreviewActivity : BaseWechatActivity(), WechatScreenShotPreviewAdapter.WechatOperateListener {
    private var mHelper: WechatScreenShotHelper = WechatScreenShotHelper(this)
    private val mList: MutableList<WechatScreenShotEntity> = mutableListOf()
    private lateinit var mAdapter: WechatScreenShotPreviewAdapter
    /**
     * 是否是自己说话
     */
    private var mComMsg = false
    override fun setLayoutResourceId() = R.layout.activity_wechat_screenshot_preview

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        mMySideEntity = UserOperateUtil.getMySelf()
        mWechatScreenShotPreviewVoiceAndKeyBoardIv.tag = "0"
        val entity = UserOperateUtil.getSingleTalkBg()
        if (entity.messageFree){
            mWechatScreenShotPreviewMessageFreeIv.visibility = View.VISIBLE
        }
        if (entity.earMode){
            mWechatScreenShotPreviewEarModeIv.visibility = View.VISIBLE
        }
        if (entity.needBg){
            GlideUtil.displayAll(this, entity.bg, mWechatScreenShotPreviewRecyclerViewBgIv)
            mWechatScreenShotPreviewRecyclerViewBgIv.visibility = View.VISIBLE
        }else{
            mWechatScreenShotPreviewRecyclerViewBgIv.visibility = View.GONE
        }
        mAdapter = WechatScreenShotPreviewAdapter(mList, entity.needBg, this)
        mWechatScreenShotPreviewRecyclerView.adapter = mAdapter
        val otherEntity = UserOperateUtil.getOtherSide()
        mWechatScreenShotPreviewNickNameTv.text = otherEntity.wechatUserNickName
        registerEventBus()
        mWechatScreenShotPreviewRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                mWechatScreenShotPreviewRecyclerView.postDelayed({ mWechatScreenShotPreviewRecyclerView.scrollToPosition(mList.size - 1) }, 0)
            }
        }
    }

    override fun initViewsListener() {
        mWechatScreenShotPreviewBackTv.setOnClickListener(this)
        mWechatScreenShotPreviewVoiceAndKeyBoardIv.setOnClickListener(this)
        mWechatScreenShotPreviewSendTv.setOnClickListener(this)
        mOpenIv.setOnClickListener(this)
        mCloseHongbaoIv.setOnClickListener(this)
        mWechatScreenShotPreviewContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()){
                    mWechatScreenShotPreviewMoreIv.visibility = View.GONE
                    mWechatScreenShotPreviewSendTv.visibility = View.VISIBLE
                }else{
                    mWechatScreenShotPreviewMoreIv.visibility = View.VISIBLE
                    mWechatScreenShotPreviewSendTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mWechatScreenShotPreviewRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        GlobalScope.launch { // 在一个公共线程池中创建一个协程
                            runOnUiThread {
                                val layoutManager = mWechatScreenShotPreviewRecyclerView.layoutManager
//                                mWechatScreenShotPreviewRecyclerView.scrollToPosition(mList.size - 1)
                                layoutManager.scrollToPosition(mAdapter.itemCount - 1)
                            }
                        }
                        mWechatScreenShotPreviewRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mList.addAll(GlobalVariable.WECHAT_SCREEN_SHOT_LIST)
        mAdapter.notifyDataSetChanged()

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatScreenShotPreviewBackTv ->{
                finish()
            }
            R.id.mOpenIv ->{
                startAnim()
            }
            R.id.mCloseHongbaoIv ->{
                stopAnim()
            }
            R.id.mWechatScreenShotPreviewVoiceAndKeyBoardIv ->{
                if (mWechatScreenShotPreviewVoiceAndKeyBoardIv.tag.toString() == "0"){
                    mWechatScreenShotPreviewVoiceAndKeyBoardIv.tag = "1"
                    mWechatScreenShotPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_keyboard)
                    mWechatScreenShotPreviewHoldDownTheTalkTv.visibility = View.VISIBLE
                }else{
                    mWechatScreenShotPreviewVoiceAndKeyBoardIv.tag = "0"
                    mWechatScreenShotPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_voice)
                    mWechatScreenShotPreviewHoldDownTheTalkTv.visibility = View.GONE
                }
            }
            R.id.mWechatScreenShotPreviewSendTv ->{
                var msg = mWechatScreenShotPreviewSendTv.text.toString()
                if (TextUtils.isEmpty(msg)){
                    showToast("请输入聊天内容")
                    return
                }
                var msgEntity = WechatScreenShotEntity()
                msgEntity.msgType = 0//默认为文本聊天
                msgEntity.isComMsg = mComMsg//默认为自己说话
                msgEntity.msg = msg
                if (mComMsg){//如果是自己说话
                    msgEntity.resourceName = mMySideEntity.resourceName
                    msgEntity.avatarInt = mMySideEntity.avatarInt
                    msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mMySideEntity.wechatUserId
                }else{//对方说话
                    msgEntity.resourceName = mOtherSideEntity.resourceName
                    msgEntity.avatarInt = mOtherSideEntity.avatarInt
                    msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
                }
            }
            R.id.mBaseCreateTitleTv ->{
                mComMsg = !mComMsg
                if(mComMsg){
                    showToast("切换为对方说话")
                }else{
                    showToast("切换为自己说话")
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatScreenShotEntity) {
        if ("收钱" == entity.tagStr){
            mHelper.update(entity)
            GlobalScope.launch {
                var readyToDel: WechatScreenShotEntity
                for (i in mList.indices){
                    readyToDel = mList[i]
                    if (readyToDel.id == entity.id){
                        readyToDel.receive = true
                        readyToDel.needEventBus = false
                        readyToDel.receiveTime = TimeUtil.stampToDateWithS(TimeUtil.getCurrentTimeEndMs())
                        readyToDel.transferType = "已收钱"
                        readyToDel.receiveTime = TimeUtil.getCurrentTimeEndMs().toString()
                        runOnUiThread {
                            mAdapter.notifyItemChanged(i)
                        }
                    }
                }
            }
        }
        var receiveEntity = WechatScreenShotEntity()
        if (entity.wechatUserId == mOtherSideEntity.wechatUserId){
            receiveEntity.wechatUserId = mMySideEntity.wechatUserId
            receiveEntity.avatarStr = mMySideEntity.wechatUserAvatar
            receiveEntity.avatarInt = mMySideEntity.avatarInt
            receiveEntity.resourceName = mMySideEntity.resourceName
        }else{
            receiveEntity.wechatUserId = mOtherSideEntity.wechatUserId
            receiveEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
            receiveEntity.avatarInt = mOtherSideEntity.avatarInt
            receiveEntity.resourceName = mOtherSideEntity.resourceName
        }
        if (entity.msgType == 3){//领红包
            receiveEntity.msgType = 4
            receiveEntity.money = entity.money
            receiveEntity.receiveTransferId = entity.id//修改的时候同时修改
        }else if (entity.msgType == 5){//收钱
            receiveEntity.msgType = 6
            receiveEntity.transferReceiveTime = entity.transferReceiveTime
            receiveEntity.transferOutTime = entity.transferOutTime
            receiveEntity.money = entity.money
            receiveEntity.isComMsg = !entity.isComMsg
            receiveEntity.receiveTransferId = entity.id//修改的时候同时修改
        }
        receiveEntity.receive = true
        mList.add(receiveEntity)
        mAdapter.notifyDataSetChanged()
        mWechatScreenShotPreviewRecyclerView.scrollToPosition(receiveEntity.position)
        EventBusUtil.unRegister(this)
        mHelper.save(receiveEntity)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
    private fun showRedpacketLayout() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR2)
        setNavigationBarBg(ColorFinal.WHITE)
        showLoadingDialog("正在加载...")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(300L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread {
                dismissLoadingDialog()
                mHonebaoAnimationBgLayout.visibility = View.VISIBLE
            }
        }
        val animatorX = ObjectAnimator.ofFloat(mHonebaoAnimationLayout, "scaleX", 0.5f, 1.1F, 0.97F, 1.0F)
        animatorX.duration = 500
        animatorX.startDelay = 300
        animatorX.start()
        val animatorY = ObjectAnimator.ofFloat(mHonebaoAnimationLayout, "scaleY", 0.5f, 1.1F, 0.97F, 1.0F)
        animatorY.duration = 500
        animatorY.startDelay = 300
        animatorY.start()
    }

    private var mFrameAnimation: FrameAnimation? = null
    /**
     * 带有动画的红包的位置
     */
    private var mRedpacketPosition = -1
    private lateinit var mRedpacketEntity: WechatScreenShotEntity
    private val mImgResIds = intArrayOf(R.drawable.wechat_hongbao_animation_1,
            R.drawable.wechat_hongbao_animation_2,
            R.drawable.wechat_hongbao_animation_3,
            R.drawable.wechat_hongbao_animation_4,
            R.drawable.wechat_hongbao_animation_5)
    private fun startAnim() {
        if (mFrameAnimation != null) {
            //如果正在转动，则直接返回
            return
        }
        mFrameAnimation = FrameAnimation(mAnimationIv, mImgResIds, 125, true)
        mFrameAnimation?.setAnimationListener(object : FrameAnimation.AnimationListener {
            override fun onAnimationStart() {
                mOpenIv.visibility = View.GONE
                mAnimationIv.visibility = View.VISIBLE

            }

            override fun onAnimationEnd() {
                Log.e("Animation-", "end")
            }

            override fun onAnimationRepeat() {
                Log.e("Animation-", "repeat")
            }

            override fun onAnimationPause() {
                mAnimationIv.setBackgroundResource(R.drawable.wechat_hongbao_open)
            }
        })
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1600L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread {
                mRedpacketEntity.receive = true
                stopAnim()
                mHelper.update(mRedpacketEntity)
                mAdapter.notifyItemChanged(mRedpacketPosition)
                val senderEntity = WechatUserEntity()//发送人
                senderEntity.money = mRedpacketEntity.money
                senderEntity.wechatUserId = mRedpacketEntity.wechatUserId
                senderEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
                senderEntity.avatarFile = mOtherSideEntity.avatarFile
                senderEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
                senderEntity.resourceName = mOtherSideEntity.resourceName
                senderEntity.avatarInt = mOtherSideEntity.avatarInt
                senderEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                LaunchUtil.startWechatSimulatorReceiveRedPacketActivityWithAnim(this@WechatScreenShotPreviewActivity, mOtherSideEntity, mRedpacketEntity, false)
            }
        }
    }

    private fun stopAnim() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setNavigationBarBg(ColorFinal.NEW_WECHAT_TITLEBAR)
        mFrameAnimation?.release()
        mFrameAnimation = null
        mHonebaoAnimationBgLayout.visibility = View.GONE
    }
    override fun otherTakeMyRedPacket(entity: WechatScreenShotEntity, position: Int) {
        entity.receive = true
        mHelper.update(entity)
        mAdapter.notifyItemChanged(position)
    }

    override fun meTakeOtherRedPacket(entity: WechatScreenShotEntity, position: Int) {
        showRedpacketLayout()
        mRedpacketEntity = entity
        mRedpacketPosition = position
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mRedpacketAvatarIv)
        mRedpacketNickNameTv.text = StringUtils.insertBack(mOtherSideEntity.wechatUserNickName, "的红包")
        mRedpacketMsgTv.text = entity.msg
    }

    override fun checkOtherRedpacketDetail(entity: WechatScreenShotEntity, position: Int) {
        showLoadingDialog("正在加载...")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(200L) // 非阻塞的延迟一秒（默认单位是毫秒）
            dismissLoadingDialog()
            LaunchUtil.startWechatSimulatorReceiveRedPacketActivity(this@WechatScreenShotPreviewActivity, mOtherSideEntity, entity, false)
        }

    }

    override fun checkMyRedpacketDetail(entity: WechatScreenShotEntity, position: Int) {
        showLoadingDialog("正在加载...")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(200L) // 非阻塞的延迟一秒（默认单位是毫秒）
            dismissLoadingDialog()
            LaunchUtil.startWechatSimulatorSendRedPacketActivity(this@WechatScreenShotPreviewActivity, entity, mOtherSideEntity, mMySideEntity, false)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        GlobalVariable.WECHAT_SCREEN_SHOT_LIST.clear()
    }
}
