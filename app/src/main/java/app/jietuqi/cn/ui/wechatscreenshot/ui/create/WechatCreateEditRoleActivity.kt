package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_edit_role.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/3 16:18.
 * 时间： 2018/12/3 16:18
 * 邮箱： 972383753@qq.com
 * 用途： 修改角色
 */
@RuntimePermissions
class WechatCreateEditRoleActivity : BaseWechatActivity(), EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        mWechatEditRoleNickNameTv.text = entity?.content
        mUserEntity?.wechatUserNickName = entity?.content
        mUserEntity?.pinyinNickName = OtherUtil.transformPinYin(entity?.content)
        mUserEntity?.firstChar = OtherUtil.getFirstLetter(mUserEntity?.pinyinNickName)
    }
    /**
     * 0 -- 微信
     * 1 -- 支付宝
     * 2 -- QQ
     */
    private var mType = 0
    private var mUserEntity: WechatUserEntity? = null
    private var mRequestCode = -1
    /**
     * 是否需要操作数据库
     */
    private var mNeedOperateDB = false
    override fun setLayoutResourceId() = R.layout.activity_wechat_edit_role

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("修改角色", 1)
    }

    override fun initViewsListener() {
        mWechatEditRoleAvatarLayout.setOnClickListener(this)
        mWechatEditRoleNickNameLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mUserEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity?
        mRequestCode = intent.getIntExtra(IntentKey.REQUEST_CODE, -1)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mNeedOperateDB = intent.getBooleanExtra(IntentKey.NEED_OPERATE_DB, false)
        mWechatEditRoleNickNameTv.text = mUserEntity?.wechatUserNickName
        mUserEntity?.getAvatarFile()?.let { GlideUtil.displayHead(this, it, mWechatEditRoleAvatarIv) }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                finish()
            }
            R.id.mWechatEditRoleAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mWechatEditRoleNickNameLayout ->{
                val dialog = EditDialog()
                if (TextUtils.isEmpty(mUserEntity?.wechatUserNickName)){
                    dialog.setData(this, EditDialogEntity(0, "", "填写角色昵称"))
                }else{
                    mUserEntity?.wechatUserNickName?.let { dialog.setData(this, EditDialogEntity(0, it, "填写角色昵称")) }
                }
                dialog.show(supportFragmentManager, "payment")
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    GlideUtil.display(this, mFinalCropFile, mWechatEditRoleAvatarIv)
                    mUserEntity?.avatarFile = mFinalCropFile
                    mUserEntity?.wechatUserAvatar = mFinalCropFile?.absolutePath
                }
            }
        }
    }
    override fun finish() {

        if (mNeedOperateDB){
            when (mRequestCode) {
                RequestCode.MY_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.MY_SELF)
                RequestCode.OTHER_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.OTHER_SIDE)
                RequestCode.QQ_MY_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.QQ_ME_SELF)
                RequestCode.QQ_OTHER_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.QQ_OTHER_SIDE)
                RequestCode.ALIPAY_MY_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.ALIPAY_ME_SELF)
                RequestCode.ALIPAY_OTHER_SIDE -> SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.ALIPAY_OTHER_SIDE)
            }
            RoleLibraryHelper(this).update(this, mUserEntity)
        }
        intent.putExtra(IntentKey.ENTITY, mUserEntity)
        setResult(mRequestCode, intent)
        super.finish()
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
