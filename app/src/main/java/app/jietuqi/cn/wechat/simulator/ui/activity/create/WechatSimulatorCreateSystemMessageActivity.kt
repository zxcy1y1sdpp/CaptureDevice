package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.wechat.entity.WechatMsgEditEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import kotlinx.android.synthetic.main.activity_simulator_wechat_create_system_message.*

/**
 * 作者： liuyuanbo on 2018/12/22 16:24.
 * 时间： 2018/12/22 16:24
 * 邮箱： 972383753@qq.com
 * 用途： 系统提示
 */
class WechatSimulatorCreateSystemMessageActivity : BaseCreateActivity() {
//    private lateinit var mHelper: WechatScreenShotHelper
    private lateinit var mHelper: WechatSimulatorHelper
    private var mScreenType = "微信"
    private var mType = 0
    private var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    override fun setLayoutResourceId() = R.layout.activity_simulator_wechat_create_system_message

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("系统提示", 1)
    }

    override fun initViewsListener() {
        mSimulatorSystemMessageRecallTv.setOnClickListener(this)
        mSimulatorSystemMessageNewFriendTv.setOnClickListener(this)
        mSimulatorSystemMessageHiTv.setOnClickListener(this)
        mSimulatorSystemMessageStrangerTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mHelper = WechatSimulatorHelper(this, mOtherSideEntity.wechatUserId)
        mScreenType = intent.getStringExtra(IntentKey.SCREEN_TYPE)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mMsgEntity.msgType = 8
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            mSimulatorSystemMessageEt.setText(mMsgEntity.msg)
        }
        if (mScreenType == "支付宝"){
            mSimulatorSystemMessageStrangerTv.visibility = View.GONE
        }
    }
    override fun onClick(v: View) {

        when(v.id){
            R.id.mSimulatorSystemMessageRecallTv ->{
                if (mScreenType == "微信"){
                    mSimulatorSystemMessageEt.setText("你撤回了一条消息 重新编辑")
                }else{
                    mSimulatorSystemMessageEt.setText("你撤回了一条消息")
                }
            }
            R.id.mSimulatorSystemMessageNewFriendTv ->{
                val nickName = mOtherSideEntity.wechatUserNickName
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(nickName, "你已添加了", "现在可以开始聊天了。"))
            }
            R.id.mSimulatorSystemMessageHiTv ->{
                mSimulatorSystemMessageEt.setText("以上是打招呼的内容")
            }
            R.id.mSimulatorSystemMessageStrangerTv ->{
                mSimulatorSystemMessageEt.setText("如果陌生人主动添加你为朋友，请谨慎核实对方身份")
            }

            R.id.overallAllRightWithBgTv ->{
                val msg = mSimulatorSystemMessageEt.text.toString().trim()
                if(TextUtils.isEmpty(msg)){
                    showToast("请输入内容")
                    return
                }
                mMsgEntity.msg = msg
                if (mType == 0){
                    EventBusUtil.post(mMsgEntity)
                }else{
                    val entity = WechatMsgEditEntity()
                    entity.editEntity  = mMsgEntity
                    EventBusUtil.post(entity)
                    mHelper.update(mMsgEntity, false)
                }
                finish()
            }
        }
        super.onClick(v)
    }
}
