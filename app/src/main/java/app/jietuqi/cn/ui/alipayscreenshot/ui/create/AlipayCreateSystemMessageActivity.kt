package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_create_system_message.*

/**
 * 作者： liuyuanbo on 2018/12/22 16:24.
 * 时间： 2018/12/22 16:24
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝系统提示
 */
class AlipayCreateSystemMessageActivity : BaseCreateActivity() {
    private lateinit var mHelper: AlipayScreenShotHelper
    private var mType = 0
    private var mMsgEntity: AlipayScreenShotEntity = AlipayScreenShotEntity()
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_system_message

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("系统提示", 1)
        mHelper = AlipayScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mAlipayCreateSystemMessageRecallTv.setOnClickListener(this)
        mAlipayCreateSystemMessageNewFriendTv.setOnClickListener(this)
        mAlipayCreateSystemMessageHiTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mMsgEntity.msgType = 8
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayScreenShotEntity
            mAlipayCreateSystemMessageEt.setText(mMsgEntity.msg)
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayCreateSystemMessageRecallTv ->{
                    mAlipayCreateSystemMessageEt.setText("你撤回了一条消息")
            }
            R.id.mAlipayCreateSystemMessageNewFriendTv ->{
                val nickName = mOtherSideEntity.wechatUserNickName
                mAlipayCreateSystemMessageEt.setText(StringUtils.insertFrontAndBack(nickName, "你已添加了", "现在可以开始聊天了。"))
            }
            R.id.mAlipayCreateSystemMessageHiTv ->{
                mAlipayCreateSystemMessageEt.setText("以上是打招呼的内容")
            }
            R.id.overallAllRightWithBgTv ->{
                val msg = mAlipayCreateSystemMessageEt.text.toString().trim()
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
