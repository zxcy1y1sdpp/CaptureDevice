package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_create_picture.*
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
class WechatCreatePictureAndVideoActivity : BaseWechatScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_picture

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        registerEventBus()
        setBlackTitle("图片/表情", 1)
        mMsgEntity.msgType = 1
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mPictureTypeTv.setOnClickListener(this)
        mEmojiTypeTv.setOnClickListener(this)
        mWechatCreatePictureAndVideoPictureLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            if (mMsgEntity.msgType == 1){
                mTitleTv.text = "选择图片"
                GlideUtil.display(this, mMsgEntity.filePath, mPictureIv)
                OtherUtil.changeWechatTwoBtnBg(this, mPictureTypeTv, mEmojiTypeTv)
            }else{
                mTitleTv.text = "选择表情"
                GlideUtil.displayGif(this, ResourceHelper.getAppIconId(mMsgEntity.pic), mPictureIv)
                mWechatCreatePictureAndVideoPictureLayout.tag = mMsgEntity.pic
                OtherUtil.changeWechatTwoBtnBg(this, mEmojiTypeTv, mPictureTypeTv)
            }
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mPictureTypeTv ->{
                mMsgEntity.msgType = 1
                OtherUtil.changeWechatTwoBtnBg(this, mPictureTypeTv, mEmojiTypeTv)
                mTitleTv.text = "选择图片"
                GlideUtil.displayAll(this, mMsgEntity.filePath, mPictureIv)
            }
            R.id.mEmojiTypeTv ->{
                mMsgEntity.msgType = 14
                OtherUtil.changeWechatTwoBtnBg(this, mEmojiTypeTv, mPictureTypeTv)
                mTitleTv.text = "选择表情"
                GlideUtil.displayGif(this, ResourceHelper.getAppIconId(mWechatCreatePictureAndVideoPictureLayout.tag.toString()), mPictureIv)
            }
            R.id.mWechatCreatePictureAndVideoPictureLayout ->{
                if (mMsgEntity.msgType == 1){
                    openAlbumWithPermissionCheck()
                }else{
                    LaunchUtil.launch(this, WechatChoiceEmojiActivity::class.java)
                }
            }
            R.id.overallAllRightWithBgTv ->{
                if (mMsgEntity.msgType == 14){
                    val emoji = mWechatCreatePictureAndVideoPictureLayout.tag.toString()
                    if (TextUtils.isEmpty(emoji)){
                        showToast("请选择表情")
                        return
                    }
                    mMsgEntity.pic = emoji
                }else{
                    if (TextUtils.isEmpty(mMsgEntity.filePath)){
                        showToast("请选择图片")
                        return
                    }
                }
            }
        }
        super.onClick(v)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChoiceEmoji(name: String) {
        mWechatCreatePictureAndVideoPictureLayout.tag = name
        GlideUtil.displayGif(this, ResourceHelper.getAppIconId(name), mPictureIv)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                if (null != data){
                    GlideUtil.displayAll(this, mFiles[0], mPictureIv)
                    mMsgEntity.filePath = mFiles[0].absolutePath
                    mFiles.clear()
                }
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
