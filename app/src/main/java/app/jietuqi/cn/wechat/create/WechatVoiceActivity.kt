package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.WechatVoiceAndVideoEntity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_voice.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2019/1/3 16:33.
 * 时间： 2019/1/3 16:33
 * 邮箱： 972383753@qq.com
 * 用途： 语音通话
 */
class WechatVoiceActivity : BaseWechatActivity() {
    private var mEntity: WechatVoiceAndVideoEntity = WechatVoiceAndVideoEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_voice

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("微信语音聊天", 0)
        registerEventBus()
        val userEntity = RoleLibraryHelper(this).queryRandom1Item()
        mEntity.wechatUserNickName = userEntity.wechatUserNickName
        mEntity.resAvatar = userEntity.resAvatar
        mEntity.resourceName = userEntity.resourceName
        mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
        mEntity.avatarFile = userEntity.avatarFile
        GlideUtil.displayHead(this, mEntity.getAvatarFile(), mVoiceAvatarIv)
        mVoiceNickNameTv.text = mEntity.wechatUserNickName
        OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
    }

    override fun initViewsListener() {
        mVoiceWaitingForAnswerTv.setOnClickListener(this)
        mVoiceBusyNowTv.setOnClickListener(this)
        mVoiceTimeLayout.setOnClickListener(this)
        mTalkingRoleLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mVoiceWaitingForAnswerTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mVoiceWaitingForAnswerTv, mVoiceBusyNowTv)
                mVoiceTimeLayout.visibility = View.GONE
                mEntity.type = 0
            }
            R.id.mVoiceBusyNowTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mVoiceBusyNowTv, mVoiceWaitingForAnswerTv)
                mVoiceTimeLayout.visibility = View.VISIBLE
                mEntity.type = 1
            }
            R.id.mVoiceTimeLayout ->{
                initTimePickerView("", 2)
            }
            R.id.mTalkingRoleLayout ->{
                operateRole(mEntity, 1)
            }
            R.id.previewBtn ->{
                mEntity.time = mVoiceTimeTv.text.toString()
                LaunchUtil.startWechatVoiceAndVideoPreviewActivity(this, mEntity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.OTHER_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    val userEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    mEntity.avatarFile = userEntity.avatarFile
                    mEntity.resAvatar = userEntity.resAvatar
                    mEntity.resourceName = userEntity.resourceName
                    mEntity.wechatUserNickName = userEntity.wechatUserNickName
                    mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
                    mVoiceNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mEntity.getAvatarFile(), mVoiceAvatarIv)
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mVoiceTimeTv.text = timeEntity.timeOnlyMS
    }
}
