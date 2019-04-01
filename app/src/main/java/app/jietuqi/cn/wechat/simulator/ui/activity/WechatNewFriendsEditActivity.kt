package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
import kotlinx.android.synthetic.main.activity_wechat_new_friends_edit.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2019/3/27 14:38.
 * 时间： 2019/3/27 14:38
 * 邮箱： 972383753@qq.com
 * 用途： 微信添加新朋友 -- 修改信息页面
 */
@RuntimePermissions
class WechatNewFriendsEditActivity : BaseWechatActivity() {
    private var mUserEntity: WechatUserEntity? = null
    private var mPosition = 0
    private var mIsChecked = false
    private var mHelper: RoleLibraryHelper? = null
    override fun setLayoutResourceId() = R.layout.activity_wechat_new_friends_edit

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("修改信息")
        mHelper = RoleLibraryHelper(this)
    }

    override fun initViewsListener() {
        mNickNameRefreshIv.setOnClickListener(this)
        mMsgRefreshIv.setOnClickListener(this)
        mConfirmEditBtn.setOnClickListener(this)
        mCleanItemBtn.setOnClickListener(this)
        mSelectAvatarLayout.setOnClickListener(this)
        mAlreadyFriendIv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mUserEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity?
        if (null == mUserEntity){
            mUserEntity = WechatUserEntity()
            mUserEntity?.eventBusTag = "新朋友 -- 添加"
        }
        mUserEntity?.isChecked?.let { mIsChecked = it }
        mPosition = intent.getIntExtra(IntentKey.POSITION, 0)
        if (null != mUserEntity){//修改信息
            GlideUtil.displayHead(this, mUserEntity?.getAvatarFile(), mUserAvatarIv)
            mNickNameEt.setText(mUserEntity?.wechatUserNickName)
            mMsgEt.setText(mUserEntity?.msg)
            if (mIsChecked){
                OtherUtil.onOrOff(mIsChecked ,mAlreadyFriendIv)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlreadyFriendIv ->{
                mIsChecked = !mIsChecked
                OtherUtil.onOrOff(mIsChecked ,mAlreadyFriendIv)
            }
            R.id.mSelectAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mNickNameRefreshIv ->{
                mNickNameEt.setText(RandomUtil.getRandomNewFriendNickName())
            }
            R.id.mMsgRefreshIv ->{
                mMsgEt.setText(RandomUtil.getRandomNewFriendMsg())
            }
            R.id.mConfirmEditBtn ->{
                if (TextUtils.isEmpty(mUserEntity?.resourceName) && null == mUserEntity?.avatarFile){
                    showQQTipDialog("请选择头像", 1)
                    return
                }
                val nickName = OtherUtil.getContent(mNickNameEt)
                if (TextUtils.isEmpty(nickName)){
                    showQQTipDialog("请输入昵称", 1)
                    return
                }
                val msg = OtherUtil.getContent(mMsgEt)
                if (TextUtils.isEmpty(msg)){
                    showQQTipDialog("请输入验证信息", 1)
                    return
                }
                mUserEntity?.setNickName(nickName)
                mUserEntity?.msg = msg
                mUserEntity?.isChecked = mIsChecked
                if (TextUtils.isEmpty(mUserEntity?.eventBusTag)){
                    mUserEntity?.eventBusTag = "新朋友 -- 修改"
                }
                val intent = Intent()
                intent.putExtra(IntentKey.ENTITY, mUserEntity)
                intent.putExtra(IntentKey.POSITION, mPosition)
                setResult(RequestCode.CHANGE_ROLE, intent)
                if (mUserEntity?.isChecked == false){
                    mHelper?.delete(mUserEntity)
                }
                finish()
            }
            R.id.mCleanItemBtn ->{
                EnsureDialog(this).builder()
                        .setGravity(Gravity.CENTER)//默认居中，可以不设置
                        .setTitle("提示！")//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("确定删除该记录吗？")
                        .setNegativeButton("取消") {}
                        .setPositiveButton("删除") {
                            mUserEntity?.eventBusTag = "新朋友 -- 删除"
                            val intent = Intent()
                            intent.putExtra(IntentKey.ENTITY, mUserEntity)
                            intent.putExtra(IntentKey.POSITION, mPosition)
                            setResult(RequestCode.CHANGE_ROLE, intent)
                            finish()
                        }.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mUserEntity?.avatarFile = mFinalCropFile
                    mUserEntity?.wechatUserAvatar = mFinalCropFile?.absolutePath
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
