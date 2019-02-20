package app.jietuqi.cn.wechat.ui.activity

import android.Manifest
import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import kotlinx.android.synthetic.main.activity_wechat_add_talk_object.*
import permissions.dispatcher.*


/**
 * 作者： liuyuanbo on 2018/10/13 21:43.
 * 时间： 2018/10/13 21:43
 * 邮箱： 972383753@qq.com
 * 用途： 添加微信聊天对象的页面
 */
@RuntimePermissions
class WechatAddTalkObjectActivity : BaseWechatActivity(){
    /**
     * 最后需要保存的对象
     */
    private var mUserEntity: WechatUserEntity = WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_add_talk_object

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("添加对话")
    }

    override fun initViewsListener() {
        mSelectAvatarLayout.setOnClickListener(this)
        mWechatAddObject.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mSelectAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mWechatAddObject ->{
                mUserEntity.wechatUserNickName = mNickNameEt.text.toString()
                mUserEntity.pinyinNickName = OtherUtil.transformPinYin(mUserEntity.wechatUserNickName)

                var helper = RoleLibraryHelper(this)
                var userId = helper.save(mUserEntity)
                mUserEntity.wechatUserId = userId.toString()
                mUserEntity.lastTime = TimeUtil.getCurrentTimeEndMs()

                var listHelper = WechatSimulatorListHelper(this)
                listHelper.save(mUserEntity)
                LaunchUtil.startWechatSimulatorPreviewActivity(this, mUserEntity)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mUserEntity.avatarInt = -1
                    mUserEntity.wechatUserAvatar = mFinalPicUri.toString()
                    GlideUtil.display(this, mFinalCropFile, mUserAvatarIv)
                }
            }
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(needCrop = true)
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
