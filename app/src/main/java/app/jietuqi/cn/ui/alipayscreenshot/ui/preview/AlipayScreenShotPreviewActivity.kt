package app.jietuqi.cn.ui.alipayscreenshot.ui.preview

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.alipayscreenshot.adapter.AlipayScreenShotPreviewAdapter
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_alipay_screenshot_preview.*
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
class AlipayScreenShotPreviewActivity : BaseWechatActivity() {
    private var mHelper: AlipayScreenShotHelper = AlipayScreenShotHelper(this)
    private val mList: MutableList<AlipayScreenShotEntity> = mutableListOf()
    private lateinit var mAdapter: AlipayScreenShotPreviewAdapter

    override fun setLayoutResourceId() = R.layout.activity_alipay_screenshot_preview

    override fun needLoadingView() = false

    override fun initAllViews() {
        mOtherSideEntity = UserOperateUtil.getAlipayOtherSide()
        mMySideEntity = UserOperateUtil.getAlipayMySelf()
        setStatusBarColor(ColorFinal.alipayBalanceRed)
        mAdapter = AlipayScreenShotPreviewAdapter(mList)
        mAlipayScreenShotPreviewRecyclerView.adapter = mAdapter
        val otherEntity = UserOperateUtil.getAlipayOtherSide()
        mAlipayScreenShotPreviewNickNameTv.text = otherEntity.wechatUserNickName
        val entity = UserOperateUtil.getAlipayChatBg()
        if (entity.needBg){
            GlideUtil.displayAll(this, entity.bg, mAlipayScreenShotPreviewRecyclerViewBgIv)
            mAlipayScreenShotPreviewRecyclerViewBgIv.visibility = View.VISIBLE
        }else{
            mAlipayScreenShotPreviewRecyclerViewBgIv.visibility = View.GONE
        }
        registerEventBus()
    }

    override fun initViewsListener() {
        mAlipayScreenShotPreviewBackIv.setOnClickListener(this)
        mAlipayScreenShotPreviewContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()){
                    mAlipayScreenShotPreviewMoreIv.visibility = View.GONE
                    mAlipayScreenShotPreviewSendTv.visibility = View.VISIBLE
                }else{
                    mAlipayScreenShotPreviewMoreIv.visibility = View.VISIBLE
                    mAlipayScreenShotPreviewSendTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val list = intent.getSerializableExtra(IntentKey.LIST) as MutableList<AlipayScreenShotEntity>
        mList.addAll(list)
        mAdapter.notifyDataSetChanged()
        mAlipayScreenShotPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayScreenShotPreviewBackIv ->{
                finish()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: AlipayScreenShotEntity) {
        var receiveEntity = AlipayScreenShotEntity()
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
        mAlipayScreenShotPreviewRecyclerView.scrollToPosition(mAdapter.itemCount -1)
//        mAlipayScreenShotPreviewRecyclerView.scrollToPosition(receiveEntity.position)
        EventBusUtil.unRegister(this)
        mHelper.save(receiveEntity)
//123        EventBusUtil.post(receiveEntity)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
}
