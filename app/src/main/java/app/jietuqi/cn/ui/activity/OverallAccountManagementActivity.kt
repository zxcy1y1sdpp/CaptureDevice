package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.EditDialog
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_account_management.*
import permissions.dispatcher.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

/**
 * 作者： liuyuanbo on 2018/11/6 10:22.
 * 时间： 2018/11/6 10:22
 * 邮箱： 972383753@qq.com
 * 用途： 账号管理
 */
@RuntimePermissions
class OverallAccountManagementActivity : BaseOverallInternetActivity(), PlatformActionListener, EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        entity?.content?.let { mOpenId = it }
        mOverallAccountManagementNickNameTv.text = entity?.content
        binding()
    }

    private var mUserEntity: OverallUserInfoEntity? = null
    /**
     * 0 -- 绑定手机
     * 1 -- 绑定微信
     * 2 -- 绑定QQ
     * 3 -- 昵称
     */
    private var mType: Int = -1
    private var mOpenId = ""
    override fun setLayoutResourceId() = R.layout.activity_overall_account_management

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("账号管理")
    }
    override fun initViewsListener() {
        mOverallAccountManagementAvatarLayout.setOnClickListener(this)
        mOverallAccountManagementNickNameLayout.setOnClickListener(this)
        mOverallAccountManagementBindingPhoneLayout.setOnClickListener(this)
        mOverallAccountManagementBindingWXLayout.setOnClickListener(this)
        mOverallAccountManagementBindingQQLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mUserEntity = UserOperateUtil.getUserInfo()
        showUserInfo()
        if (!TextUtils.isEmpty(mUserEntity?.wx_openid)){
            mOverallAccountManagementBindingWXTv.text = "已绑定"
        }
        if (!TextUtils.isEmpty(mUserEntity?.qq_openid)){
            mOverallAccountManagementBindingQQTv.text = "已绑定"
        }
        if (!TextUtils.isEmpty(mUserEntity?.mobile)){
            mOverallAccountManagementBindingPhoneTv.text = "已绑定"
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallAccountManagementAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mOverallAccountManagementNickNameLayout ->{
                mType = 3
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity( title = "请输入昵称"))
                dialog.show(supportFragmentManager, "classify")
            }
            R.id.mOverallAccountManagementBindingPhoneLayout ->{
                if (mOverallAccountManagementBindingPhoneTv.text.toString() == "未绑定"){
                    mType = 0
                    LaunchUtil.startOverallRegisterActivity(this, 1)
                }

            }
            R.id.mOverallAccountManagementBindingWXLayout ->{
                if (TextUtils.isEmpty(mUserEntity?.wx_openid) && mOverallAccountManagementBindingWXTv.text.toString() == "未绑定"){
                    authorize(ShareSDK.getPlatform(Wechat.NAME))
                    mType = 1
                }
            }
            R.id.mOverallAccountManagementBindingQQLayout ->{
                if (TextUtils.isEmpty(mUserEntity?.qq_openid) && mOverallAccountManagementBindingQQTv.text.toString() == "未绑定"){
                    authorize(ShareSDK.getPlatform(QQ.NAME))
                    mType = 2
                }
            }
        }
    }
    private fun authorize(plat: Platform) {
        showLoadingDialog("绑定中...")
        plat.platformActionListener = this
        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(false)
        //获取用户资料
        plat.showUser(null)
    }

    /**
     * 授权成功的回调
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
        mOpenId = platform.db.userId
        if (TextUtils.isEmpty(mUserEntity?.headimgurl)){
            mUserEntity?.headimgurl = platform.db.userIcon
        }
        if (TextUtils.isEmpty(mUserEntity?.nickname)){
            mUserEntity?.nickname = platform.db.userName
        }
        mUserEntity?.sex = if (platform.db.userGender == "m") 0 else 1
        binding()
    }
    /**
     * 授权取消的回调
     *
     * @param platform
     * @param i
     */
    override fun onCancel(platform: Platform?, i: Int) {
        showToast("已取消")
        dismissLoadingDialog()
    }
    /**
     * 授权错误的回调
     * @param platform
     * @param i
     * @param throwable
     */
    override fun onError(platform: Platform, i: Int, throwable: Throwable?) {
        showToast("失败")
        dismissLoadingDialog()
    }

    private fun binding(){
        var request: PostRequest = EasyHttp.post(HttpConfig.USERS, false).params("way", "edit").params("id", UserOperateUtil.getUserId())
        when (mType) {
            0 -> { //绑定手机号
                request.params("mobile", mOpenId)
            }
            1 -> {//绑定微信
                request.params("wx_openid", mOpenId)
            }
            2 -> {
                request.params("qq_openid", mOpenId)
            }
            3 -> {
                request.params("nickname", mOpenId)
            }
        }
        request.execute(object : SimpleCallBack<String>() {
            override fun onError(e: ApiException) {}
            override fun onSuccess(t: String) {
                if (mType == 0){
                    showToast("手机绑定成功")
                    mUserEntity?.mobile = mOpenId
                    mOverallAccountManagementBindingPhoneTv.text = "已绑定"
                }else if (mType == 1){
                    showToast("微信绑定成功")
                    mOverallAccountManagementBindingWXTv.text = "已绑定"
                    mUserEntity?.wx_openid = mOpenId
                    val wechat = ShareSDK.getPlatform(Wechat.NAME)
                    val isAuthValid = wechat.isAuthValid
                    if (isAuthValid) {
                        wechat.removeAccount(true)
                    }
                }else if (mType == 2){
                    showToast("QQ绑定成功")
                    mOverallAccountManagementBindingQQTv.text = "已绑定"
                    mUserEntity?.qq_openid = mOpenId
                    val wechat = ShareSDK.getPlatform(QQ.NAME)
                    val isAuthValid = wechat.isAuthValid
                    if (isAuthValid) {
                        wechat.removeAccount(true)
                    }
                }else if (mType == 3){
                    showToast("昵称修改成功")
                    mUserEntity?.nickname = mOpenId
                }
                showUserInfo()
                UserOperateUtil.saveUser(mUserEntity)
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mFinalCropFile?.let { luban(it) }
                }
            }
        }
    }
    private fun uploadPics(file: File){
        val mUIProgressResponseCallBack = object : UIProgressResponseCallBack() {
            override fun onUIResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean) {}
        }
        EasyHttp.post(HttpConfig.USERS, false)
                .params("way", "edit")
                .params("headimgurl", file, file.name, mUIProgressResponseCallBack)
                .params("id", UserOperateUtil.getUserId())
                .execute(object : CallBackProxy<OverallApiEntity<OverallPublishEntity>, OverallPublishEntity>(object : SimpleCallBack<OverallPublishEntity>() {
                    override fun onSuccess(t: OverallPublishEntity) {
                        dismissLoadingDialog()
                        showToast("上传成功")
                        mUserEntity?.localHead = mFinalCropFile
                        UserOperateUtil.saveUser(mUserEntity)
                        mUserEntity?.localHead?.let { GlideUtil.displayHead(this@OverallAccountManagementActivity, it, mOverallAccountManagementAvatarIv) }
                    }
                    override fun onStart() {
                        super.onStart()
                        showLoadingDialog("上传中...")
                    }
                    override fun onError(e: ApiException) {
                        dismissLoadingDialog()
                        showToast("上传失败" + e.message)
                    }
                }) {})
    }
    private fun luban(file: File){
        Log.e("luban --- ", FileUtil.getFileOrFilesSize(file.absolutePath, FileUtil.SIZETYPE_KB).toString())
        Luban.with(this)
                .load(file)//原图
                .ignoreBy(100)//多大不压缩
                .setTargetDir(cacheDir.path)//缓存压缩图片路径
//                .filter(CompressionPredicate { path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) })
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {}

                    override fun onSuccess(file: File) {
                        uploadPics(file)
                    }
                    override fun onError(e: Throwable) {
                    }
                }).launch()
    }

    override fun onResume() {
        super.onResume()
        mUserEntity = UserOperateUtil.getUserInfo()
        if (!TextUtils.isEmpty(mUserEntity?.mobile)){
            mOverallAccountManagementBindingPhoneTv.text = "已绑定"
        }
    }

    /**
     * 显示用户信息
     */
    fun showUserInfo(){
        GlideUtil.display(this, mUserEntity?.headimgurl, mOverallAccountManagementAvatarIv)
        mOverallAccountManagementNickNameTv.text = mUserEntity?.nickname
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
