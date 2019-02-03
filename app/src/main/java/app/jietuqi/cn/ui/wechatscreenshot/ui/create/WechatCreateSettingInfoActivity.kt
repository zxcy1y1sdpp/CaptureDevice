package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.ChangeSingleTaklBgEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.ChoiceRoleDialog
import app.jietuqi.cn.widget.dialog.ChoiceWechatBackgroundDialog
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_setting_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/3 11:51.
 * 时间： 2018/12/3 11:51
 * 邮箱： 972383753@qq.com
 * 用途： 设置资料
 */
@RuntimePermissions
class WechatCreateSettingInfoActivity : BaseCreateActivity(), ChoiceWechatBackgroundDialog.OnChoiceSingleTalkBgListener, EditDialogChoiceListener {
    /**
     * 0 -- 微信
     * 1 -- 支付宝
     * 2 -- QQ
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_wechat_setting_info

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("设置资料")
    }

    override fun initViewsListener() {
        mWechatSettingInfoMyLayout.setOnClickListener(this)
        mWechatSettingInfoOtherLayout.setOnClickListener(this)
        mWechatSettingInfoBgLayout.setOnClickListener(this)
        mQQOtherStatusLayout.setOnClickListener(this)
        mQQUnReadNumberLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType == 0){

        }else if (mType == 1){

        }else if (mType == 2){
            mQQOtherStatusLayout.visibility = View.VISIBLE//对方状态
            mQQUnReadNumberLayout.visibility = View.VISIBLE//消息未读数量
            mWechatSettingInfoQQLayout.visibility = View.VISIBLE
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                val status = UserOperateUtil.getQQOtherStatus()
                val number = UserOperateUtil.getQQUnReadNumber()
                runOnUiThread {
                    mQQUnReadNumberTv.text = if (number.toInt() >= 100) "99+" else number
                    mQQOtherStatusTv.text = if (TextUtils.isEmpty(status)) "手机在线 - WIFI" else status
                }
            }
        }
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mMySideEntity = intent.getSerializableExtra(IntentKey.MY_SIDE) as WechatUserEntity
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatSettingInfoMyAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatSettingInfoOtherAvatarIv)
        mWechatSettingInfoMyNickNameTv.text = mMySideEntity.wechatUserNickName
        mWechatSettingInfoOtherNickNameTv.text = mOtherSideEntity.wechatUserNickName
        var chatBg: ChangeSingleTaklBgEntity? = null
        when(mType) {
            0 -> {
                chatBg = UserOperateUtil.getSingleTalkBg()
            }
            1 -> {
                chatBg = UserOperateUtil.getAlipayChatBg()
            }
            2 -> {
                chatBg = UserOperateUtil.getQQSingleTalkBg()
            }
        }
        if (chatBg?.needBg == true){
            GlideUtil.displayAll(this, chatBg?.bg, mWechatSettingInfoBgIv)
        }else{
            GlideUtil.displayAll(this, R.drawable.default_bg, mWechatSettingInfoBgIv)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mQQOtherStatusLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, mQQOtherStatusTv.text.toString(), "在线状态"))
                dialog.show(supportFragmentManager, "edit")
            }
            R.id.mQQUnReadNumberLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(1, mQQUnReadNumberTv.text.toString(), "未读消息数量"))
                dialog.show(supportFragmentManager, "edit")
            }
            R.id.mWechatSettingInfoMyLayout ->{
                val dialog = ChoiceRoleDialog()
                when(mType) {
                    0 -> {
                        dialog.setRequestCode(RequestCode.MY_SIDE, mMySideEntity, mType, true)
                    }
                    1 -> {
                        dialog.setRequestCode(RequestCode.ALIPAY_MY_SIDE, mMySideEntity, mType, true)
                    }
                    2 -> {
                        dialog.setRequestCode(RequestCode.QQ_MY_SIDE, mMySideEntity, mType, true)
                    }
                }
                dialog.show(supportFragmentManager, "choiceRole")
            }
            R.id.mWechatSettingInfoOtherLayout ->{
                val dialog = ChoiceRoleDialog()
                when(mType) {
                    0 -> {
                        dialog.setRequestCode(RequestCode.OTHER_SIDE, mOtherSideEntity, mType, true)
                    }
                    1 -> {
                        dialog.setRequestCode(RequestCode.ALIPAY_OTHER_SIDE, mOtherSideEntity, mType, true)
                    }
                    2 -> {
                        dialog.setRequestCode(RequestCode.QQ_OTHER_SIDE, mOtherSideEntity, mType, true)
                    }
                }
                dialog.show(supportFragmentManager, "choiceRole")
            }
            R.id.mWechatSettingInfoBgLayout ->{
                val dialog = ChoiceWechatBackgroundDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "choiceBg")
            }
        }
    }
    override fun onChoice(entity: EditDialogEntity?) {
        when(entity?.position){
            0 ->{//在线状态
                mQQOtherStatusTv.text = entity.content
            }
            1 ->{//未读消息数量
                mQQUnReadNumberTv.text = entity.content
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.MY_SIDE, RequestCode.QQ_MY_SIDE, RequestCode.ALIPAY_MY_SIDE-> {
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mMySideEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatSettingInfoMyAvatarIv)
                    mWechatSettingInfoMyNickNameTv.text = mMySideEntity.wechatUserNickName
                }
            }
            RequestCode.OTHER_SIDE, RequestCode.QQ_OTHER_SIDE, RequestCode.ALIPAY_OTHER_SIDE -> {
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mOtherSideEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatSettingInfoOtherAvatarIv)
                    mWechatSettingInfoOtherNickNameTv.text = mOtherSideEntity.wechatUserNickName
                }
            }
            RequestCode.IMAGE_SELECT ->{
                GlideUtil.displayAll(this, mFiles[0], mWechatSettingInfoBgIv)
                when(mType){
                    0 ->{
                        SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(true, mFiles[0].absolutePath), SharedPreferenceKey.SINGLE_TALK_BG)
                    }
                    1 ->{
                        SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(true, mFiles[0].absolutePath), SharedPreferenceKey.ALIPAY_CHAT_BG)
                    }
                    2 ->{
                        SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(true, mFiles[0].absolutePath), SharedPreferenceKey.QQ_CHAT_BG)
                    }
                }
            }
        }
    }
    override fun onChoice(need: Boolean) {
        if(need){
            openAlbumWithPermissionCheck()
        }else{
            GlideUtil.displayAll(this, R.drawable.default_bg, mWechatSettingInfoBgIv)
            when(mType){
                0 ->{
                    SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(false, ""), SharedPreferenceKey.SINGLE_TALK_BG)
                }
                1 ->{
                    SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(false, ""), SharedPreferenceKey.ALIPAY_CHAT_BG)
                }
                2 ->{
                    SharedPreferencesUtils.saveBean2Sp(ChangeSingleTaklBgEntity(false, ""), SharedPreferenceKey.QQ_CHAT_BG)
                }
            }

        }
    }
    override fun finish() {
        mMySideEntity.meSelf = true
        mOtherSideEntity.meSelf = false
        when(mType){
            0 ->{
                SharedPreferencesUtils.saveBean2Sp(mMySideEntity, SharedPreferenceKey.MY_SELF)
                SharedPreferencesUtils.saveBean2Sp(mOtherSideEntity, SharedPreferenceKey.OTHER_SIDE)
            }
            1 ->{
                SharedPreferencesUtils.saveBean2Sp(mMySideEntity, SharedPreferenceKey.ALIPAY_ME_SELF)
                SharedPreferencesUtils.saveBean2Sp(mOtherSideEntity, SharedPreferenceKey.ALIPAY_OTHER_SIDE)
            }
            2 ->{
                SharedPreferencesUtils.saveBean2Sp(mMySideEntity, SharedPreferenceKey.QQ_ME_SELF)
                SharedPreferencesUtils.saveBean2Sp(mOtherSideEntity, SharedPreferenceKey.QQ_OTHER_SIDE)
                SharedPreferencesUtils.putData(SharedPreferenceKey.QQ_UN_READ_NUMBER, mQQUnReadNumberTv.text.toString())
                SharedPreferencesUtils.putData(SharedPreferenceKey.QQ_OTHER_STATUS, mQQOtherStatusTv.text.toString())
            }
        }

        EventBusUtil.post(mMySideEntity)
        EventBusUtil.post(mOtherSideEntity)
        super.finish()
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
