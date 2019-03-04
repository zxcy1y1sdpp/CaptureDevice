package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.qq.BaseQQScreenShotCreateActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.qqscreenshot.db.QQScreenShotHelper
import app.jietuqi.cn.util.GlideUtil
import kotlinx.android.synthetic.main.activity_qq_create_picture.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class QQCreatePictureActivity : BaseQQScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_qq_create_picture

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 1
        setBlackTitle("图片", 1)
        mHelper = QQScreenShotHelper(this)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mQQCreatePictureLayout.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 0){
            GlideUtil.display(this, mMsgEntity.filePath, mQQCreatePictureIv)
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mQQCreatePictureLayout ->{
                openAlbumWithPermissionCheck()
            }
            /*R.id.overallAllRightWithBgTv ->{
                if(mType == 0){
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity)
                }
                finish()
            }*/
        }
        super.onClick(v)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                GlideUtil.displayAll(this, mFiles[0], mQQCreatePictureIv)
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
}
