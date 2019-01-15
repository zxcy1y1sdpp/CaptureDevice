package app.jietuqi.cn.wechat.ui.activity

import android.Manifest
import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_role.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/10/18 14:24.
 * 时间： 2018/10/18 14:24
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 修改角色
 */
@RuntimePermissions
class WechatRoleActivity : BaseWechatActivity(), EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        mSenderNameTv.text = entity?.content
        mEntity.wechatUserNickName = entity?.content
    }

    private var mEntity: WechatUserEntity = WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_role

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("修改角色", 1)
    }

    override fun initViewsListener() {
        mAvatarLayout.setOnClickListener(this)
        mNickNameLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAvatarLayout ->{
                openAlbumWithPermissionCheck()
//                callAlbum(1, true)
            }
            R.id.mNickNameLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(-1, getString(R.string.app_name), "角色昵称"))
                dialog.show(supportFragmentManager, "changeRole")
            }
            R.id.sureTv ->{
                var intent = Intent()
                intent.putExtra(IntentKey.ENTITY, mEntity)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mEntity.avatarFile = mFinalCropFile
                    GlideUtil.display(this, mFinalCropFile, mAvatarIv)
                }
            }
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(1, true)
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
