package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_alipay_create_picture.*
import kotlinx.android.synthetic.main.include_choice_role.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class AlipayCreatePictureActivity : BaseCreateActivity() {
    private lateinit var mHelper: AlipayScreenShotHelper
    private var mMsgEntity: AlipayScreenShotEntity = AlipayScreenShotEntity()
    /**
     * 0 -- 发布新的图片
     * 1 -- 编辑图片
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_picture

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("图片", 1)
        mHelper = AlipayScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mWechatCreateChoiceMySideLayout.setOnClickListener(this)
        mWechatCreateChoiceOtherSideLayout.setOnClickListener(this)
        mAlipayCreatePictureLayout.setOnClickListener(this)
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mMsgEntity.msgType = 1
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatCreateChoiceOtherSideAvatarIv)
        mWechatCreateChoiceOtherSideNickNameTv.text = mOtherSideEntity.wechatUserNickName
        mMySideEntity = UserOperateUtil.getMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatCreateChoiceMySideAvatarIv)
        mWechatCreateChoiceMySideNickNameTv.text = mMySideEntity.wechatUserNickName
        setMsg(mMySideEntity)
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayScreenShotEntity
            if (mMsgEntity.wechatUserId == mMySideEntity.wechatUserId){
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
            }else{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
            }
            GlideUtil.display(this, mMsgEntity.filePath, mAlipayCreatePictureIv)
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayCreatePictureLayout ->{
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
                    mHelper.update(mMsgEntity)
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
        mMsgEntity.avatarInt = entity.resAvatar
        mMsgEntity.avatarStr = entity.wechatUserAvatar
        mMsgEntity.wechatUserId = entity.wechatUserId
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.CROP_IMAGE ->{
                GlideUtil.displayAll(this, mFinalCropFile, mAlipayCreatePictureIv)
                mMsgEntity.filePath = mFinalCropFile?.absolutePath
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
