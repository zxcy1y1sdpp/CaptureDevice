package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_create_invite_join_group.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class WechatCreateInviteJoinGroupActivity : BaseWechatScreenShotCreateActivity(){
    private var mGroupInfoEntity =  WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_invite_join_group

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        registerEventBus()
        mMsgEntity.msgType = 13
        setBlackTitle("邀请加群", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreateShareBgLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mGroupInfoEntity = mMsgEntity.groupInfo
            val bitmap = BitmapFactory.decodeByteArray(mGroupInfoEntity.groupHeaderByte, 0, mGroupInfoEntity.groupHeaderByte.size)
            mWechatCreateGroupIconIv.setImageBitmap(bitmap)
            mWechatCreateGroupNameEt.setText(mGroupInfoEntity.groupName)
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateShareBgLayout ->{
                LaunchUtil.launch(this, WechatChoiceGroupHeaderActivity::class.java)
            }
            R.id.overallAllRightWithBgTv ->{
                val groupName = OtherUtil.getContent(mWechatCreateGroupNameEt)
                if(TextUtils.isEmpty(groupName)){
                    showToast("请输入群名称")
                    return
                }
                if(null == mGroupInfoEntity.groupHeaderByte){
                    showToast("请选择群头像")
                    return
                }
                changeRole2()
                mGroupInfoEntity.groupName = groupName
                mMsgEntity.groupInfo = mGroupInfoEntity
            }
        }
        super.onClick(v)
    }
    private fun changeRole2(){
        if (mMe){
            mGroupInfoEntity.avatarInt = mMySideEntity.avatarInt
            mGroupInfoEntity.resourceName = mMySideEntity.resourceName
            mGroupInfoEntity.wechatUserAvatar = mMySideEntity.wechatUserAvatar
            mGroupInfoEntity.wechatUserId = mMySideEntity.wechatUserId
            mGroupInfoEntity.wechatUserNickName = mMySideEntity.wechatUserNickName
        }else{
            mGroupInfoEntity.avatarInt = mOtherSideEntity.avatarInt
            mGroupInfoEntity.resourceName = mOtherSideEntity.resourceName
            mGroupInfoEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
            mGroupInfoEntity.wechatUserId = mOtherSideEntity.wechatUserId
            mGroupInfoEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                mMsgEntity.filePath = mFiles[0].absolutePath
            }
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChoiceGroupHeader(byte: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
        mWechatCreateGroupIconIv.setImageBitmap(bitmap)
        mGroupInfoEntity.groupHeaderByte = byte
    }
}
