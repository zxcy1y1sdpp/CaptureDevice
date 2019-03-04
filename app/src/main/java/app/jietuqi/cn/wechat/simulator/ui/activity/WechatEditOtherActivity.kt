package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.ChoiceWechatBackgroundDialog
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_edit_other.*
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2019/1/27 18:07.
 * 时间： 2019/1/27 18:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
@RuntimePermissions
class WechatEditOtherActivity : BaseOverallActivity(), ChoiceWechatBackgroundDialog.OnChoiceSingleTalkBgListener, EditDialogChoiceListener {
    /**
     * 0 -- 选择头像
     * 1 -- 选择背景
     */
    private var mType = 0

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_wechat_simulator_edit_other
    }

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }

    override fun initViewsListener() {
        mOtherAvatarLayout.setOnClickListener(this)
        mOtherBgLayout.setOnClickListener(this)
        mNickNameLayout.setOnClickListener(this)
        mOtherCleanBtn.setOnClickListener(this)
        setTopTitle("编辑", rightTitle = "完成")
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mOtherAvatarIv)

        mOtherNickNameTv.text = mOtherSideEntity.wechatUserNickName
        if (!TextUtils.isEmpty(mOtherSideEntity.chatBg)){//没有聊天背景
            GlideUtil.displayAll(this, File(mOtherSideEntity.chatBg), mOtherBgIv)
        }
    }
    override fun onChoice(need: Boolean) {
        if (!need){
            mOtherSideEntity.chatBg = ""
        }else{
            openAlbumWithPermissionCheck(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                if (mType == 1){
                    mOtherSideEntity.chatBg = mFiles[0].absolutePath
                    GlideUtil.displayAll(this, mFiles[0], mOtherBgIv)
                }
            }
            RequestCode.CROP_IMAGE ->{
                if (mType == 0){
                    GlideUtil.display(this, mFinalCropFile, mOtherAvatarIv)
                    mOtherSideEntity.avatarFile = mFinalCropFile
                    mOtherSideEntity.wechatUserAvatar = mFinalCropFile?.absolutePath
                }
            }
        }
    }
    override fun onChoice(entity: EditDialogEntity?) {
        mOtherNickNameTv.text = entity?.content
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOtherAvatarLayout ->{
                mType = 0
                openAlbumWithPermissionCheck(true)
            }
            R.id.mOtherBgLayout ->{
                mType = 1
                val dialog = ChoiceWechatBackgroundDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "choiceBg")
            }
            R.id.mNickNameLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "", "昵称"))
                dialog.show(supportFragmentManager, "payment")
            }
            R.id.mOtherCleanBtn ->{
                EventBusUtil.post("清除")
                finish()
            }
            R.id.overAllRightTitleTv ->{
                mOtherSideEntity.wechatUserNickName = OtherUtil.getContent(mOtherNickNameTv)
                mOtherSideEntity.pinyinNickName = OtherUtil.transformPinYin(mOtherSideEntity.wechatUserNickName)
                RoleLibraryHelper(this).update(this, mOtherSideEntity)
                EventBusUtil.post(mOtherSideEntity)
                finish()
            }
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum(needCrop: Boolean = false) {
        callAlbum(needCrop = needCrop)
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
