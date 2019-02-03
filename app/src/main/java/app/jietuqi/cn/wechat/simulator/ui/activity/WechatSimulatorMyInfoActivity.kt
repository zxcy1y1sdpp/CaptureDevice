package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_simulator_myinfo.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2019/1/16 10:46.
 * 时间： 2019/1/16 10:46
 * 邮箱： 972383753@qq.com
 * 用途： 微信个人信息页面
 */
@RuntimePermissions
class WechatSimulatorMyInfoActivity : BaseCreateActivity(), EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity) {
        if (entity.position == 0){
            mWechatSimulatorMyInfoNickNameTv.text = entity.content
            mMySideEntity.wechatUserNickName = entity.content
        }else{
            mWechatSimulatorMyInfoWechatNumberTv.text = entity.content
            mMySideEntity.wechatNumber = entity.content
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_myinfo

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mMySideEntity = UserOperateUtil.getWechatSimulatorMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatSimulatorMyInfoAvatarIv)
        mWechatSimulatorMyInfoNickNameTv.text = mMySideEntity.wechatUserNickName
        mWechatSimulatorMyInfoWechatNumberTv.text = mMySideEntity.wechatNumber

    }
    override fun initViewsListener() {
        mWechatSimulatorPreviewBackITv.setOnClickListener(this)
        mWechatSimulatorMyInfoPreviewChangeAvatarLayout.setOnClickListener(this)
        mWechatSimulatorMyInfoPreviewChangeNickNameLayout.setOnClickListener(this)
        mWechatSimulatorMyInfoPreviewChangeWxNumberLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorPreviewBackITv ->{
                finish()
            }
            R.id.mWechatSimulatorMyInfoPreviewChangeAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mWechatSimulatorMyInfoPreviewChangeNickNameLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "", "修改昵称"))
                dialog.show(supportFragmentManager, "changeRole")
            }
            R.id.mWechatSimulatorMyInfoPreviewChangeWxNumberLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(1, "", "修改微信号"))
                dialog.show(supportFragmentManager, "changeRole")
            }
        }
    }
    override fun finish() {
        super.finish()
        mMySideEntity.wechatNumber = mWechatSimulatorMyInfoWechatNumberTv.text.toString()
        mMySideEntity?.pinyinNickName = OtherUtil.transformPinYin(mMySideEntity?.wechatUserNickName)
        mMySideEntity?.firstChar = OtherUtil.getFirstLetter(mMySideEntity?.pinyinNickName)
        SharedPreferencesUtils.saveBean2Sp(mMySideEntity, SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.CROP_IMAGE ->{
                GlideUtil.displayAll(this, mFinalCropFile, mWechatSimulatorMyInfoAvatarIv)
                mMySideEntity.avatarFile = mFinalCropFile
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
    override fun onResume() {
        super.onResume()
        needVipForCover()
    }
}
