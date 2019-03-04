package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.dialog.ChoiceRoleDialog
import app.jietuqi.cn.widget.dialog.ChoiceWechatBackgroundDialog
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_create_card.*
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/5 13:59.
 * 时间： 2018/12/5 13:59
 * 邮箱： 972383753@qq.com
 * 用途： 创建图片和视频的页面
 */
@RuntimePermissions
class WechatCreateCardActivity : BaseWechatScreenShotCreateActivity(), EditDialogChoiceListener, ChoiceWechatBackgroundDialog.OnChoiceSingleTalkBgListener {
    private var mCardEntity: WechatUserEntity? = null
    override fun onChoice(entity: EditDialogEntity?) {
        mWechatCreateCardWxNumberTv.text = entity?.content
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_create_card

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 12
        setBlackTitle("个人名片", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatCreateCardLayout.setOnClickListener(this)
        mWechatCreateCardWxNumberLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mCardEntity =  mMsgEntity.card
            GlideUtil.displayHead(this, mCardEntity?.getAvatarFile(), mWechatCreateCardIv)
            mWechatCreateCardNickNameTv.text = mCardEntity?.wechatUserNickName
            mWechatCreateCardWxNumberTv.text = mCardEntity?.wxNumber
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatCreateCardLayout ->{
                if (null == mCardEntity){
                    mCardEntity = WechatUserEntity()
                }
                val dialog = ChoiceRoleDialog()
                mCardEntity?.let {  dialog.setRequestCode(100, it, 0, false) }

                dialog.show(supportFragmentManager, "choiceRole")
            }
            R.id.mWechatCreateCardWxNumberLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "", "微信号"))
                dialog.show(supportFragmentManager, "payment")
            }
            R.id.overallAllRightWithBgTv ->{
                val wxNumber = mWechatCreateCardWxNumberTv.text.toString()
                if (TextUtils.isEmpty(wxNumber)){
                    showToast("请输入微信号")
                    return
                }
                mCardEntity?.wxNumber = wxNumber
                mMsgEntity.card = mCardEntity
            }
        }
        super.onClick(v)
    }
    override fun onChoice(need: Boolean) {
        if(need){
            openAlbumWithPermissionCheck()
        }else{
            mMsgEntity.filePath = ""
            mWechatCreateCardIv.setImageResource(R.drawable.wechat_share_default)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                GlideUtil.displayAll(this, mFiles[0], mWechatCreateCardIv)
                mMsgEntity.filePath = mFiles[0].absolutePath
            }
            100 ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    val entity = data.getSerializableExtra(IntentKey.ENTITY)
                    mCardEntity =  entity as WechatUserEntity
                    GlideUtil.displayHead(this, mCardEntity?.getAvatarFile(), mWechatCreateCardIv)
                    mWechatCreateCardNickNameTv.text = mCardEntity?.wechatUserNickName
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
