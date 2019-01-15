package app.jietuqi.cn.ui.qqscreenshot.ui.preview

import android.content.Intent
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.qqscreenshot.adapter.QQScreenShotPreviewAdapter
import app.jietuqi.cn.ui.qqscreenshot.db.QQScreenShotHelper
import app.jietuqi.cn.ui.qqscreenshot.entity.QQScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_qq_screenshot_preview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/4 22:46.
 * 时间： 2018/12/4 22:46
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝截图预览页面
 */
class QQScreenShotPreviewActivity : BaseWechatActivity() {
    private var mHelper: QQScreenShotHelper = QQScreenShotHelper(this)
    private val mList: MutableList<QQScreenShotEntity> = mutableListOf()
    private lateinit var mAdapter: QQScreenShotPreviewAdapter
    private var statusBarView: View ?= null

    override fun setLayoutResourceId() = R.layout.activity_qq_screenshot_preview

    override fun needLoadingView() = false

    override fun initAllViews() {
        //延时加载数据.
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            //只走一次
            false
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val status = UserOperateUtil.getQQOtherStatus()
            val number = UserOperateUtil.getQQUnReadNumber()
            runOnUiThread {
                mQQScreenShotPreviewUnReadNumberTv.text = if (number.toInt() >= 100) "99+" else number
                mQQScreenShotPreviewStatusTv.text = if (TextUtils.isEmpty(status)) "手机在线 - WIFI" else status
            }
        }
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        mMySideEntity = UserOperateUtil.getMySelf()
        mAdapter = QQScreenShotPreviewAdapter(mList)
        mQQScreenShotPreviewRecyclerView.adapter = mAdapter
        val otherEntity = UserOperateUtil.getOtherSide()
        mQQScreenShotPreviewNickNameTv.text = otherEntity.wechatUserNickName
        val entity = UserOperateUtil.getSingleTalkBg()
        if (entity.needBg){
            GlideUtil.displayAll(this, entity.bg, mQQScreenShotPreviewRecyclerViewBgIv)
        }else{
            mQQScreenShotPreviewRecyclerViewBgIv.visibility = View.GONE
        }
        registerEventBus()
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            //android系统级的资源id得这么拿,不然拿不到
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        if (statusBarView != null) {
            statusBarView?.setBackgroundResource(R.drawable.qq_head_giadient)
        }
    }
    override fun initViewsListener() {
        mQQScreenShotPreviewBackIv.setOnClickListener(this)
        mQQScreenShotPreviewUnReadNumberTv.setOnClickListener(this)
        mQQScreenShotPreviewContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()){
                    mQQScreenShotPreviewSendTv.solid = ContextCompat.getColor(this@QQScreenShotPreviewActivity, R.color.qqCanSend)
                    mQQScreenShotPreviewEmojiIv.visibility = View.VISIBLE
                }else{
                    mQQScreenShotPreviewSendTv.solid = ContextCompat.getColor(this@QQScreenShotPreviewActivity, R.color.qqCanNotSend)
                    mQQScreenShotPreviewEmojiIv.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val list = intent.getSerializableExtra(IntentKey.LIST) as MutableList<QQScreenShotEntity>
        mList.addAll(list)
        mAdapter.notifyDataSetChanged()
        mQQScreenShotPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mQQScreenShotPreviewBackIv, R.id.mQQScreenShotPreviewUnReadNumberTv ->{
                finish()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: QQScreenShotEntity) {
        var receiveEntity = QQScreenShotEntity()
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
        mQQScreenShotPreviewRecyclerView.scrollToPosition(mAdapter.itemCount -1)
        EventBusUtil.unRegister(this)
        mHelper.save(receiveEntity)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
}
