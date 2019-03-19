package app.jietuqi.cn.base.wechat

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatMsgEditEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.include_choice_role.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:17.
 * 时间： 2018/12/5 17:17
 * 邮箱： 972383753@qq.com
 * 用途：
 */
abstract class BaseWechatSimulatorCreateActivity : BaseWechatActivity(){
    lateinit var mHelper: WechatSimulatorHelper
    var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    /**
     * 0 -- 单聊
     * 1 -- 群聊
     */
    var mChatType = 0
    /**
     * 0 -- 发布新的文本
     * 1 -- 编辑修改文本
     */
    var mType = 0
    /**
     * 是不是自己
     */
    var mMe = true

    override fun initAllViews() {}

    override fun initViewsListener() {
        mChangeRoleLayout.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mChatType = intent.getIntExtra(IntentKey.CHAT_TYPE, 0)
        mHelper = if (TextUtils.isEmpty(mOtherSideEntity.groupTableName)){
            WechatSimulatorHelper(this, mOtherSideEntity)
        }else{
            WechatSimulatorHelper(this, mOtherSideEntity.groupTableName)
        }
        mMySideEntity = UserOperateUtil.getWechatSimulatorMySelf()

        changeRole()
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            if (mMsgEntity.wechatUserId == mMySideEntity.wechatUserId){
                mMe = true
                changeRole()
            }else{
                mMe = false
                changeRole()
            }
        }
    }
    private fun changeRole(){
        if (mMe){
            mSenderTitleTv.text = "发送人 -- 自己"
            mSenderNickNameTv.text = mMySideEntity.wechatUserNickName
            GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mAvatarIv)
            setMsg(mMySideEntity)
            mMsgEntity.isComMsg = false
        }else{
            mSenderTitleTv.text = "发送人 -- 对方"
            mSenderNickNameTv.text = mOtherSideEntity.wechatUserNickName
            GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mAvatarIv)
            setMsg(mOtherSideEntity)
            mMsgEntity.isComMsg = true
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mChangeRoleLayout ->{
                mMe = !mMe
                changeRole()
            }
            R.id.overallAllRightWithBgTv ->{
                if(mType == 0){
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
    }
    private fun setMsg(entity: WechatUserEntity){
        mMsgEntity.avatarInt = entity.avatarInt
        mMsgEntity.resourceName = entity.resourceName
        mMsgEntity.avatarStr = entity.wechatUserAvatar
        mMsgEntity.wechatUserId = entity.wechatUserId
    }
}
