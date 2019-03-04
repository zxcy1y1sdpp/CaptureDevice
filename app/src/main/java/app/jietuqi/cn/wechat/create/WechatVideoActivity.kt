package app.jietuqi.cn.wechat.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.activity_wechat_video.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2019/1/3 16:33.
 * 时间： 2019/1/3 16:33
 * 邮箱： 972383753@qq.com
 * 用途： 视频通话
 */
@RuntimePermissions
class WechatVideoActivity : BaseWechatActivity() {
    /**
     * 0 -- 对方视频画面
     * 1 -- 我的视频画面
     */
    private var mType = 0
    private var mEntity: WechatVoiceAndVideoEntity = WechatVoiceAndVideoEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_video

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("微信视频聊天", 0)
        registerEventBus()
        mEntity.chatType = 1
        mEntity.type = 0
        val userEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mEntity.wechatUserNickName = userEntity.wechatUserNickName
        mEntity.avatarInt = userEntity.avatarInt
        mEntity.resourceName = userEntity.resourceName
        mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
        mEntity.avatarFile = userEntity.avatarFile
        GlideUtil.displayHead(this, mEntity.getAvatarFile(), mVideoWaitingTalkAvatarIv)
        mVideoWaitingTalkNickNameTv.text = mEntity.wechatUserNickName
    }

    override fun initViewsListener() {
        mVideoWaitingForAnswerTv.setOnClickListener(this)
        mVideoBusyNowTv.setOnClickListener(this)
        mVideoWaitingTalkRoleLayout.setOnClickListener(this)//待接听 -- 通话对象
        mVideoWaitingBgLayout.setOnClickListener(this)//待接听 -- 视频画面
        mVideoTimeLayout.setOnClickListener(this)//通话中 -- 通话时间
        mVideoBusyNowOtherSideBgLayout.setOnClickListener(this)//通话中 -- 对方视频画面
        mVideoBusyNowMySideBgLayout.setOnClickListener(this)//通话中 -- 我的视频画面
        previewBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mVideoWaitingForAnswerTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mVideoWaitingForAnswerTv, mVideoBusyNowTv)
                mVideoBusyNowLayout.visibility = View.GONE
                mVideoVideoWaitingLayout.visibility = View.VISIBLE
                mEntity.type = 0
            }
            R.id.mVideoBusyNowTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mVideoBusyNowTv, mVideoWaitingForAnswerTv)
                mVideoBusyNowLayout.visibility = View.VISIBLE
                mVideoVideoWaitingLayout.visibility = View.GONE
                mEntity.type = 1
            }
            R.id.mVideoTimeLayout ->{
                initTimePickerView("", 2)
            }
            R.id.mVideoWaitingTalkRoleLayout ->{
                operateRole(mEntity, 1)
            }
            R.id.mVideoWaitingBgLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mVideoBusyNowOtherSideBgLayout ->{
                mType = 0
                openAlbumWithPermissionCheck()
            }
            R.id.mVideoBusyNowMySideBgLayout ->{
                mType = 1
                openAlbumWithPermissionCheck()
            }
            R.id.previewBtn ->{
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
                    mEntity.avatarInt = userEntity.avatarInt
                    mEntity.resourceName = userEntity.resourceName
                    mEntity.wechatUserNickName = userEntity.wechatUserNickName
                    mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
                    mVideoWaitingTalkNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mEntity.getAvatarFile(), mVideoWaitingTalkAvatarIv)
                }
            }
            RequestCode.IMAGE_SELECT ->{//通话中
                if (mEntity.type == 0){//待接听
                    GlideUtil.displayAll(this@WechatVideoActivity, mFiles[0], mVideoWaitingBgIv)
                    mEntity.wechatBg = mFiles[0]
                    if (null != mEntity.wechatBg && !TextUtils.isEmpty(mEntity.wechatUserNickName) && null != mEntity.getAvatarFile()){
                        OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                    }
                }else{//通话中
                    if (mType == 0){//对方视频画面
                        mEntity.otherFileBg = mFiles[0]
                        GlideUtil.displayAll(this@WechatVideoActivity, mFiles[0], mVideoBusyNowOtherSideBgIv)
                    }else{//我的视频画面
                        mEntity.myFileBg = mFiles[0]
                        GlideUtil.displayAll(this@WechatVideoActivity, mFiles[0], mVideoBusyNowMySideBgIv)
                    }
                    if (null != mEntity.otherFileBg && mEntity.myFileBg != null){
                        OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                    }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mVideoTimeTv.text = timeEntity.timeOnlyMS
        mEntity.time = timeEntity.timeOnlyMS
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 存储 ] 访问权限")
    }
}
