package app.jietuqi.cn.ui.wechatscreenshot.ui.preview

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatScreenShotPreviewAdapter
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
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
class WechatScreenShotPreviewActivity : BaseWechatActivity() {
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
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR)
        setLightStatusBarForM(this, true)
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        mMySideEntity = UserOperateUtil.getMySelf()
        mWechatScreenShotPreviewVoiceAndKeyBoardIv.tag = "0"
        val entity = UserOperateUtil.getSingleTalkBg()
        if (entity.needBg){
            GlideUtil.displayAll(this, entity.bg, mWechatScreenShotPreviewRecyclerViewBgIv)
            mWechatScreenShotPreviewRecyclerViewBgIv.visibility = View.VISIBLE
        }else{
            mWechatScreenShotPreviewRecyclerViewBgIv.visibility = View.GONE
        }
        mAdapter = WechatScreenShotPreviewAdapter(mList, entity.needBg)
        mWechatScreenShotPreviewRecyclerView.adapter = mAdapter
        val otherEntity = UserOperateUtil.getOtherSide()
        mWechatScreenShotPreviewNickNameTv.text = otherEntity.wechatUserNickName
        registerEventBus()
    }

    override fun initViewsListener() {
        mWechatScreenShotPreviewBackTv.setOnClickListener(this)
        mWechatScreenShotPreviewVoiceAndKeyBoardIv.setOnClickListener(this)
        mWechatScreenShotPreviewSendTv.setOnClickListener(this)
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
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val list = intent.getSerializableExtra(IntentKey.LIST) as MutableList<WechatScreenShotEntity>
        mList.addAll(list)
        mAdapter.notifyDataSetChanged()

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatScreenShotPreviewBackTv ->{
                finish()
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
                    msgEntity.avatarInt = mMySideEntity.resAvatar
                    msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mMySideEntity.wechatUserId
                }else{//对方说话
                    msgEntity.avatarInt = mOtherSideEntity.resAvatar
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
        var receiveEntity = WechatScreenShotEntity()
        if (entity.wechatUserId == mOtherSideEntity.wechatUserId){
            receiveEntity.wechatUserId = mMySideEntity.wechatUserId
            receiveEntity.avatarStr = mMySideEntity.wechatUserAvatar
            receiveEntity.avatarInt = mMySideEntity.resAvatar

        }else{
            receiveEntity.wechatUserId = mOtherSideEntity.wechatUserId
            receiveEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
            receiveEntity.avatarInt = mOtherSideEntity.resAvatar
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
//123        EventBusUtil.post(receiveEntity)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
}
