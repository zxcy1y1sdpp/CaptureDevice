package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.ChoiceWechatBackgroundDialog
import kotlinx.android.synthetic.main.activity_wechat_create_share.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class WechatCreateShareActivity : BaseWechatScreenShotCreateActivity(), ChoiceWechatBackgroundDialog.OnChoiceSingleTalkBgListener {

    override fun setLayoutResourceId() = R.layout.activity_wechat_create_share

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 11
        setBlackTitle("转发", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreateShareBgLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            if (TextUtils.isEmpty(mMsgEntity.filePath)){
                mWechatCreateShareBgIv.setImageResource(R.drawable.wechat_share_default)
            }else{
                GlideUtil.display(this, mMsgEntity.filePath, mWechatCreateShareBgIv)
            }
            mWechatCreateShareTitleEt.setText(mMsgEntity.msg)
            mWechatCreateShareContentEt.setText(mMsgEntity.content)
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateShareBgLayout ->{
                val dialog = ChoiceWechatBackgroundDialog()
                dialog.setListener(this)
                dialog.setTitle("自定义封面", "默认封面")
                dialog.show(supportFragmentManager, "choiceBg")
            }
            R.id.overallAllRightWithBgTv ->{
                val title = OtherUtil.getContent(mWechatCreateShareTitleEt)
                val content = OtherUtil.getContent(mWechatCreateShareContentEt)
                if(TextUtils.isEmpty(title)){
                    showToast("请输入文章标题")
                    return
                }
                if(TextUtils.isEmpty(content)){
                    showToast("请输入文字描述")
                    return
                }
                mMsgEntity.msg = title
                mMsgEntity.content = content
            }
        }
        super.onClick(v)
    }
    override fun onChoice(need: Boolean) {
        if(need){
            openAlbumWithPermissionCheck()
        }else{
            mMsgEntity.filePath = ""
            mWechatCreateShareBgIv.setImageResource(R.drawable.wechat_share_default)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                GlideUtil.displayAll(this, mFiles[0], mWechatCreateShareBgIv)
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
