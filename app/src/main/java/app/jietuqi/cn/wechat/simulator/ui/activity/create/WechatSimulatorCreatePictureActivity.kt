package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorCreateActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_create_picture_and_video.*
import kotlinx.android.synthetic.main.include_choice_role.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class WechatSimulatorCreatePictureActivity : BaseWechatSimulatorCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_picture_and_video

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("图片", 1)
    }

    override fun initViewsListener() {
        mWechatCreatePictureAndVideoPictureTypeTv.setOnClickListener(this)
        mWechatCreatePictureAndVideoVideoTypeTv.setOnClickListener(this)
        mWechatCreateChoiceMySideLayout.setOnClickListener(this)
        mWechatCreateChoiceOtherSideLayout.setOnClickListener(this)
        mWechatCreatePictureAndVideoPictureLayout.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mMsgEntity.msgType = 1
        if (mType == 1){
            GlideUtil.display(this, mMsgEntity.filePath, mWechatCreatePictureAndVideoPictureIv)
        }
        /*mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatCreateChoiceOtherSideAvatarIv)
        mWechatCreateChoiceOtherSideNickNameTv.text = mOtherSideEntity.wechatUserNickName
        mMySideEntity = UserOperateUtil.getMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatCreateChoiceMySideAvatarIv)
        mWechatCreateChoiceMySideNickNameTv.text = mMySideEntity.wechatUserNickName
        setMsg(mMySideEntity)
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            if (mMsgEntity.wechatUserId == mMySideEntity.wechatUserId){
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
            }else{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
            }
            GlideUtil.display(this, mMsgEntity.filePath, mWechatCreatePictureAndVideoPictureIv)
        }*/
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatCreatePictureAndVideoPictureTypeTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatePictureAndVideoPictureTypeTv, mWechatCreatePictureAndVideoVideoTypeTv)
                mWechatSettingInfoVideoAndTimeLayout.visibility = View.GONE
                mWechatCreatePictureAndVideoPictureLayout.visibility = View.VISIBLE
            }
            R.id.mWechatCreatePictureAndVideoVideoTypeTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreatePictureAndVideoVideoTypeTv, mWechatCreatePictureAndVideoPictureTypeTv)
                mWechatSettingInfoVideoAndTimeLayout.visibility = View.VISIBLE
                mWechatCreatePictureAndVideoPictureLayout.visibility = View.GONE
            }
            R.id.mWechatCreatePictureAndVideoPictureLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mWechatCreateChoiceMySideLayout ->{
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
                mMsgEntity.isComMsg = false
            }
            R.id.mWechatCreateChoiceOtherSideLayout ->{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
                mMsgEntity.isComMsg = true
            }
            R.id.overallAllRightWithBgTv ->{
                if(mType == 0){
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity, true)
                }
                finish()
            }
        }
    }
    private fun setChoice(choiceIv: ImageView, unChoiceIv: ImageView){
        choiceIv.setImageResource(R.drawable.choice)
        unChoiceIv.setImageResource(R.drawable.un_choice)
    }
    private fun setMsg(entity: WechatUserEntity){
        mMsgEntity.avatarInt = entity.avatarInt
        mMsgEntity.resourceName = entity.resourceName
        mMsgEntity.avatarStr = entity.wechatUserAvatar
        mMsgEntity.wechatUserId = entity.wechatUserId
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                GlideUtil.displayAll(this, mFiles[0], mWechatCreatePictureAndVideoPictureIv)
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
