package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.SingleTalkEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorPreviewAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.widget.WechatSimulatorBottomFunsDialog
import kotlinx.android.synthetic.main.activity_wechat_simulator_preview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/4 22:46.
 * 时间： 2018/12/4 22:46
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图预览页面
 */
@RuntimePermissions
class WechatSimulatorPreviewActivity : BaseWechatActivity(), WechatSimulatorBottomFunsDialog.FunsChoiceListener {
    override fun choice(title: String, funType: Int) {
        mFunType = funType
        when(title){
            "时间" ->{
                initTimePickerView(tag = "创建")
            }
            "图片" ->{
                openAlbumWithPermissionCheck()
            }
            "红包" ->{
                LaunchUtil.startWechatSimulatorCreateRedPacketActivity(this, mOtherSideEntity, null, 0)
            }
            "转账" ->{
                LaunchUtil.startWechatSimulatorCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            "语音" ->{
                LaunchUtil.startWechatSimulatorCreateVoiceActivity(this, mOtherSideEntity)
            }
            "系统提示" ->{
                LaunchUtil.startWechatSimulatorCreateSystemMessageActivity(this, mOtherSideEntity, null, 0, "微信")
            }
        }
    }

    private lateinit var mHelper: WechatSimulatorHelper
    private val mList: MutableList<SingleTalkEntity> = mutableListOf()
    private lateinit var mAdapter: WechatSimulatorPreviewAdapter
    /**
     * 0 -- 文本
     * 1-- 图片
     * 2 -- 时间
     * 3 -- 红包
     * 4 -- 转账
     * 5 -- 语音
     * 6 -- 系统提示
     */
    private var mFunType = 0
    /**
     * 是否是自己说话
     */
    private var mComMsg = false
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_preview

    override fun needLoadingView() = false

    override fun initAllViews() {
        mOtherSideEntity = WechatUserEntity()
        registerEventBus()
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR)
        setLightStatusBarForM(this, true)
        mMySideEntity = UserOperateUtil.getMySelf()
        mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "0"

    }

    override fun initViewsListener() {
        mWechatSimulatorPreviewNickNameTv.setOnClickListener(this)
        mWechatSimulatorPreviewBackTv.setOnClickListener(this)
        mWechatSimulatorPreviewVoiceAndKeyBoardIv.setOnClickListener(this)
        mWechatSimulatorPreviewSendTv.setOnClickListener(this)
        mWechatSimulatorPreviewMoreIv.setOnClickListener(this)
        mWechatSimulatorPreviewContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()){
                    mWechatSimulatorPreviewMoreIv.visibility = View.GONE
                    mWechatSimulatorPreviewSendTv.visibility = View.VISIBLE
                }else{
                    mWechatSimulatorPreviewMoreIv.visibility = View.VISIBLE
                    mWechatSimulatorPreviewSendTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity//获取对方信息
        mHelper = WechatSimulatorHelper(this, mOtherSideEntity.wechatUserId)
        mWechatSimulatorPreviewNickNameTv.text = mOtherSideEntity.wechatUserNickName
        val list = mHelper.queryAll()//获取和对方聊天的记录
        if (null != list){
            mList.addAll(list)
        }
        val entity = UserOperateUtil.getSingleTalkBg()
        if (entity.needBg){
            GlideUtil.displayAll(this, entity.bg, mWechatSimulatorPreviewRecyclerViewBgIv)
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.VISIBLE
        }else{
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.GONE
        }
        mAdapter = WechatSimulatorPreviewAdapter(mList, entity.needBg, mOtherSideEntity, mHelper)
        mWechatSimulatorPreviewRecyclerView.adapter = mAdapter
        mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorPreviewBackTv ->{
                finish()
            }
            R.id.mWechatSimulatorPreviewMoreIv ->{
                val dialog = WechatSimulatorBottomFunsDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "funType")
            }
            R.id.mWechatSimulatorPreviewNickNameTv ->{//切换聊天对象
                mComMsg = !mComMsg
                if (mComMsg){//对方说话
                    showToast("切换为对方说话")
                }else{
                    showToast("切换为自己说话")
                }
            }
            R.id.mWechatSimulatorPreviewVoiceAndKeyBoardIv ->{
                if (mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag.toString() == "0"){
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "1"
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_keyboard)
                    mWechatSimulatorPreviewHoldDownTheTalkTv.visibility = View.VISIBLE
                }else{
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "0"
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_voice)
                    mWechatSimulatorPreviewHoldDownTheTalkTv.visibility = View.GONE
                }
            }
            R.id.mWechatSimulatorPreviewSendTv ->{
                mFunType = 0
                saveMsg()
                mWechatSimulatorPreviewContentEt.setText("")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.CROP_IMAGE ->{
                saveMsg()
            }
        }
    }
    private fun saveMsg(entity: WechatScreenShotEntity? = null){
        var msgEntity = WechatScreenShotEntity()
        msgEntity.msgType = mFunType//默认为文本聊天
        msgEntity.isComMsg = mComMsg//对方说话
        //如果是自己说话
        when (mFunType) {
            0 -> {
                var msg = mWechatSimulatorPreviewContentEt.text.toString()
                if (TextUtils.isEmpty(msg)){
                    showToast("请输入聊天内容")
                    return
                }
                msgEntity.msg = msg
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            1 -> {
                msgEntity.filePath = mFinalCropFile?.absolutePath
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            2 -> {
                msgEntity.time = mWechatSimulatorPreviewMoreIv.tag.toString().toLong()
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            3 -> {//发红包
                if (entity?.receive == true && entity?.msgType == 3){
                    msgEntity.msgType = 4
                    msgEntity.receive = true
                    entity?.isComMsg?.let { msgEntity.isComMsg = !it }
                    entity?.isComMsg?.let { mComMsg = !it }
                    entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                    if (entity?.msgType == 3){//领红包
                        msgEntity.msgType = 4
                    }
                }else{
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }
                }
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            4 -> {//领取红包
                msgEntity.receive = true
                entity?.isComMsg?.let { msgEntity.isComMsg = !it }
                entity?.isComMsg?.let { mComMsg = !it }
                entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                if (!mComMsg){//自己的红包被领取
                    msgEntity.avatarInt = mOtherSideEntity.resAvatar
                    msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
                }else{//领取对方的红包
                    msgEntity.avatarInt = mMySideEntity.resAvatar
                    msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mMySideEntity.wechatUserId
                }
            }
            5 -> {//转账
                if (entity?.receive == true && entity?.msgType == 5){
                    msgEntity.msgType = 6
                    msgEntity.receive = true
                    entity?.isComMsg?.let { msgEntity.isComMsg = !it }
                    entity?.isComMsg?.let { mComMsg = !it }
                    entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                    if (entity?.msgType == 5){//收钱
                        msgEntity.msgType = 6
                        msgEntity.transferReceiveTime = entity.transferReceiveTime
                        msgEntity.transferOutTime = entity.transferOutTime
                    }
                }else{
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }
                }
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            6 -> {//转账
                msgEntity.receive = true
                entity?.isComMsg?.let { msgEntity.isComMsg = it }
                entity?.isComMsg?.let { mComMsg = it }
                entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
            }
            7 -> {
                entity?.voiceLength?.let { msgEntity.voiceLength = it }
                entity?.voiceToText?.let { msgEntity.voiceToText = it }
                entity?.isComMsg?.let { mComMsg = it }
                entity?.isComMsg?.let { msgEntity.isComMsg = it }
                entity?.alreadyRead?.let { msgEntity.alreadyRead = it }
                setRole(msgEntity)
            }
            8 -> {
                msgEntity.msg = entity?.msg
                entity?.msgType?.let { msgEntity.msgType = it }
            }
        }
        val position = mList.size
        mHelper.save(msgEntity)
        mList.add(msgEntity)
        mAdapter.notifyItemInserted(position)
        mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
    }

    private fun setRole(msgEntity: WechatScreenShotEntity){
        if (!mComMsg){//如果是自己说话
            if (mFunType == 4){
                msgEntity.avatarInt = mOtherSideEntity.resAvatar
                msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
            }else{
                msgEntity.avatarInt = mMySideEntity.resAvatar
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
            }
        }else{//对方说话
            if (mFunType == 4){
                msgEntity.avatarInt = mMySideEntity.resAvatar
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
            }else{
                msgEntity.avatarInt = mOtherSideEntity.resAvatar
                msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatScreenShotEntity) {
        mFunType = entity.msgType
        saveMsg(entity)
        EventBusUtil.unRegister(this)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(needCrop = true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 存储 ] 访问权限")
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "创建"){
            mFunType = 2
            mWechatSimulatorPreviewMoreIv.tag = timeEntity.timeLong
            saveMsg()
        }
    }
    override fun finish() {
        EventBusUtil.unRegister(this)
        val lastEntity = mHelper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = mOtherSideEntity.id
            userEntity.wechatUserId = mOtherSideEntity.wechatUserId
            userEntity.resAvatar = mOtherSideEntity.resAvatar
            userEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
            userEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
            userEntity.avatarFile = mOtherSideEntity.avatarFile
            userEntity.pinyinNickName = mOtherSideEntity.pinyinNickName
            userEntity.firstChar = mOtherSideEntity.firstChar
            userEntity.isFirst = mOtherSideEntity.isFirst
            userEntity.isLast = mOtherSideEntity.isLast
            userEntity.meSelf = mOtherSideEntity.meSelf

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead
            EventBusUtil.post(userEntity)
        }

        super.finish()
    }
}
