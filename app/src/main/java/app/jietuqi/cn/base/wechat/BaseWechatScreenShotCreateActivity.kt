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

    override fun initAllViews() {
        mHelper = WechatScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mWechatCreateChoiceMySideLayout.setOnClickListener(this)
        mWechatCreateChoiceOtherSideLayout.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatCreateChoiceOtherSideAvatarIv)
        mWechatCreateChoiceOtherSideNickNameTv.text = mOtherSideEntity.wechatUserNickName
        mMySideEntity = UserOperateUtil.getMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatCreateChoiceMySideAvatarIv)
        mWechatCreateChoiceMySideNickNameTv.text = mMySideEntity.wechatUserNickName
        setMsg(mMySideEntity)
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            if (mMsgEntity.wechatUserId == mMySideEntity.wechatUserId){
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
            }else{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
            }
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatCreateChoiceMySideLayout ->{
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
                mMsgEntity.isComMsg = false
            }
            R.id.mWechatCreateChoiceOtherSideLayout ->{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
                mMsgEntity.isComMsg = true
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
        mMsgEntity.avatarInt = entity.resAvatar
        mMsgEntity.avatarStr = entity.wechatUserAvatar
        mMsgEntity.wechatUserId = entity.wechatUserId
    }
    private fun setChoice(choiceIv: ImageView, unChoiceIv: ImageView){
        choiceIv.setImageResource(R.drawable.choice)
        unChoiceIv.setImageResource(R.drawable.un_choice)
    }
}
