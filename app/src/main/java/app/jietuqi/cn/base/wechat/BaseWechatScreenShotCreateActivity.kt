package app.jietuqi.cn.base.wechat

import android.content.Intent
import android.view.View
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.include_choice_role.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:17.
 * 时间： 2018/12/5 17:17
 * 邮箱： 972383753@qq.com
 * 用途：
 */
abstract class BaseWechatScreenShotCreateActivity : BaseWechatActivity(){
    lateinit var mHelper: WechatScreenShotHelper
    var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    /**
     * 0 -- 发布新的文本
     * 1 -- 编辑修改文本
     */
    var mType = 0
    /**
     * 是不是自己
     */
    var mMe = true

    override fun initAllViews() {
        mHelper = WechatScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mChangeRoleLayout.setOnClickListener(this)
        mAvatarIv.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mMySideEntity = UserOperateUtil.getMySelf()
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
            R.id.mAvatarIv ->{
                mMe = !mMe
                changeRole()
            }
            R.id.overallAllRightWithBgTv ->{
                if(mType == 0){
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity)
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
    private fun setChoice(choiceIv: ImageView, unChoiceIv: ImageView){
        choiceIv.setImageResource(R.drawable.choice)
        unChoiceIv.setImageResource(R.drawable.un_choice)
    }
}
