package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_create_system_message.*

/**
 * 作者： liuyuanbo on 2018/12/22 16:24.
 * 时间： 2018/12/22 16:24
 * 邮箱： 972383753@qq.com
 * 用途： 系统提示
 */
class WechatCreateSystemMessageActivity : BaseCreateActivity() {
    private lateinit var mHelper: WechatScreenShotHelper
    private var mScreenType = "微信"
    private var mType = 0
    private var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_system_message

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("系统提示", 1)
        mHelper = WechatScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mCreateSystemMessageRecallTv.setOnClickListener(this)
        mCreateSystemMessageNewFriendTv.setOnClickListener(this)
        mCreateSystemMessageHiTv.setOnClickListener(this)
        mCreateSystemMessageStrangerTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mScreenType = intent.getStringExtra(IntentKey.SCREEN_TYPE)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mMsgEntity.msgType = 8
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            mCreateSystemMessageEt.setText(mMsgEntity.msg)
        }
        if (mScreenType == "支付宝"){
            mCreateSystemMessageStrangerTv.visibility = View.GONE
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mCreateSystemMessageRecallTv ->{
                if (mScreenType == "微信"){
                    mCreateSystemMessageEt.setText("你撤回了一条消息 重新编辑")
                }else{
                    mCreateSystemMessageEt.setText("你撤回了一条消息")
                }
            }
            R.id.mCreateSystemMessageNewFriendTv ->{
                val nickName = mOtherSideEntity.wechatUserNickName
                mCreateSystemMessageEt.setText(StringUtils.insertFrontAndBack(nickName, "你已添加了", "现在可以开始聊天了。"))
            }
            R.id.mCreateSystemMessageHiTv ->{
                mCreateSystemMessageEt.setText("以上是打招呼的内容")
            }
            R.id.mCreateSystemMessageStrangerTv ->{
                mCreateSystemMessageEt.setText("如果陌生人主动添加你为朋友，请谨慎核实对方身份")
            }

            R.id.overallAllRightWithBgTv ->{
                val msg = mCreateSystemMessageEt.text.toString().trim()
                if(TextUtils.isEmpty(msg)){
                    showToast("请输入内容")
                    return
                }
                mMsgEntity.msg = msg
                if(mType == 0){
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity)
                }
                finish()
            }
        }
    }
}
