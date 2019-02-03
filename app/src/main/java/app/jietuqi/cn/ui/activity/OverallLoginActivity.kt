package app.jietuqi.cn.ui.activity

import android.os.Handler
import android.os.Message
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallThridLoginEntity
import app.jietuqi.cn.util.IPUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.mob.tools.utils.UIHandler
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import com.zhouyou.http.widget.ProgressUtils
import kotlinx.android.synthetic.main.activity_overall_login.*
import java.util.*
/**
 * 作者： liuyuanbo on 2018/11/12 15:36.
 * 时间： 2018/11/12 15:36
 * 邮箱： 972383753@qq.com
 * 用途： 登录页面
 */
class OverallLoginActivity : BaseOverallInternetActivity(), PlatformActionListener, Handler.Callback{

    /**
     * 0 -- 账号密码登录
     * 1 -- 账号和验证码登录
     * 2 -- 第三方
     */
    private var mLoginType = 0
    private val MSG_USERID_FOUND = 1
    private val MSG_LOGIN = 2
    private val MSG_AUTH_CANCEL = 3
    private val MSG_AUTH_ERROR = 4
    private val MSG_AUTH_COMPLETE = 5
    private var mThirdLoginEntity: OverallThridLoginEntity? = null
    /**
     * 内网ip
     */
    private var IIP = ""
    /**
     * 外网ip
     */
    private var IP = ""

    override fun setLayoutResourceId() = R.layout.activity_overall_login

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("用户登录")
        IIP = IPUtil.getLocalIpV4Address()
        Thread(Runnable {
            // 获取外网ip
            IP = IPUtil.getNetIp2()
            Log.e("ip:外网地址",  IP)
        }).start()
        Log.e("ip:内网地址",  IIP)
    }

    override fun initViewsListener() {
        mOverallLoginBtn.setOnClickListener(this)
        mOverallLoginSignInTv.setOnClickListener(this)
        mOverallLoginAccountSmsCodeTv.setOnClickListener(this)
        mOverallLoginWechatIv.setOnClickListener(this)
        mOverallLoginQQIv.setOnClickListener(this)
        mOverallLoginUserAgreementLayout.setOnClickListener(this)
        mOverallLoginForgetPasswordIv.setOnClickListener(this)
        mOverallLoginShowPasswordIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallLoginBtn ->{
                if (canLogin()){
                    when(mLoginType){
                        0 ->{
                            login()
                        }
                        2 ->{}
                        3 ->{}
                    }
                }
            }
            R.id.mOverallLoginSignInTv ->{
                LaunchUtil.startOverallRegisterActivity(this, 0)
            }
            R.id.mOverallLoginShowPasswordIv ->{
                var showPass = !mOverallLoginShowPasswordIv.tag.toString().toBoolean()
                if (showPass){
                    mOverallLoginPasswordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    mOverallLoginShowPasswordIv.setImageResource(R.drawable.password_show)
                }else{
                    mOverallLoginPasswordEt.transformationMethod = PasswordTransformationMethod.getInstance()
                    mOverallLoginShowPasswordIv.setImageResource(R.drawable.password_hide)
                }
                mOverallLoginShowPasswordIv.tag = showPass
            }
            R.id.mOverallLoginForgetPasswordIv ->{
                LaunchUtil.startOverallRegisterActivity(this, 2)
            }
            R.id.mOverallLoginAccountSmsCodeTv ->{
                if (mLoginType == 0){
                    mLoginType = 1//验证码登录
                    mOverallLoginAccountSmsCodeTv.text = "验证码登录"
                    mOverallLoginPasswordEt.hint = "请输入验证码"
                }else{
                    mLoginType = 0//账号密码登录
                    mOverallLoginAccountSmsCodeTv.text = "账号密码登录"
                    mOverallLoginPasswordEt.hint = "请输入密码"
                }
            }
            R.id.mOverallLoginWechatIv ->{//微信登录
                authorize(ShareSDK.getPlatform(Wechat.NAME))
            }
            R.id.mOverallLoginQQIv ->{//QQ登录
                authorize(ShareSDK.getPlatform(QQ.NAME))
            }
            R.id.mOverallLoginUserAgreementLayout ->{//用户协议
                LaunchUtil.launch(this, OverallProtocolActivity::class.java)
            }
        }
    }

    private fun canLogin(): Boolean{
        if(OtherUtil.getContentLength(mOverallLoginAccountEt) != 11){
            ToastUtils.showShort(this, "手机号长度不正确")
            return false
        }
        if (mLoginType == 0){
            if (6 > OtherUtil.getContentLength(mOverallLoginPasswordEt)){
                ToastUtils.showShort(this, "密码长度必须大于6位")
                return false
            }
        }else if (mLoginType == 1){
            if (6 > OtherUtil.getContentLength(mOverallLoginPasswordEt)){
                ToastUtils.showShort(this, "验证码输入不正确")
                return false
            }
        }

        return true
    }
    private fun login(){
        var request: PostRequest = EasyHttp.post(HttpConfig.REGISTER_AND_LOGIN)
        if (mLoginType == 0){//账号密码登录
            request.params("password", OtherUtil.getContent(mOverallLoginPasswordEt))
                    .params("mobile", OtherUtil.getContent(mOverallLoginAccountEt))
                    .params("way", "mobile")
        }
        if (mLoginType == 1){//验证码登录
            request.params("mobile", OtherUtil.getContent(mOverallLoginAccountEt))
                    .params("way", "mobile")

        }
        if (mLoginType == 2){//QQ登录
            request.params("openid", mThirdLoginEntity?.openId)
            request.params("nickname", mThirdLoginEntity?.nickName)
            request.params("headimgurl", mThirdLoginEntity?.avatar)
            request.params("sex", mThirdLoginEntity?.sex)
            request.params("province", mThirdLoginEntity?.province)
            request.params("district", mThirdLoginEntity?.district)
            request.params("way", mThirdLoginEntity?.loginType)
            request.params("ip", IP)
            request.params("iip", IIP)

        }
        request.execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
            override fun onStart() {
                super.onStart()
                showLoadingDialog()
            }
            override fun onError(e: ApiException) {
                dismissLoadingDialog()
                e.message?.let { showToast(it) }
            }
            override fun onSuccess(t: OverallUserInfoEntity) {
                dismissLoadingDialog()
                ToastUtils.showShort(this@OverallLoginActivity, "登录成功")
                SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.USER_INFO)
                SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, true)
                finish()
            }
        }) {})
    }

    private fun authorize(plat: Platform) {
        showLoadingDialog()
        plat.platformActionListener = this
        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(false)
        //获取用户资料
        plat.showUser(null)
    }
    private fun removeAccount(platform: Platform) {
        if (platform.isAuthValid) {
            platform.removeAccount(true)
        }
    }
    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            MSG_USERID_FOUND -> {
            }
            MSG_LOGIN -> {
                //登录类型 1微信 2 QQ 3新浪
                removeAccount(msg.obj as Platform)
                login()
//                WebServiceAPI.getInstance().tLogin(mThirdLoginType, mOpenId, mNickName, mGender, mUserAvatar, JPushInterface.getRegistrationID(this), this, this)
            }
            MSG_AUTH_CANCEL -> {
                ToastUtils.showShort(this@OverallLoginActivity, "取消了")
                ProgressUtils.cancleProgressDialog()
            }
            MSG_AUTH_ERROR -> {
                ProgressUtils.cancleProgressDialog()
                ToastUtils.showShort(this@OverallLoginActivity, "授权错误")
            }
            MSG_AUTH_COMPLETE -> {
                ToastUtils.showShort(this@OverallLoginActivity, "授权成功")
            }
        }
        return false
    }
    /**
     * 授权成功的回调
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
        dismissLoadingDialog()
        mLoginType = 2
        //输出所有授权信息
        Log.e("thirdLogin", platform.db.exportData())
        mThirdLoginEntity = OverallThridLoginEntity()
        mThirdLoginEntity?.openId = platform.db.userId
        mThirdLoginEntity?.avatar = platform.db.userIcon
        mThirdLoginEntity?.nickName = platform.db.userName
        mThirdLoginEntity?.sex = if (platform.db.userGender == "m") "0" else "1"
        mThirdLoginEntity?.loginType = if ("QQ" == platform.name) "qq" else "wx"
        //遍历Map
        val ite = hashMap.entries.iterator()
        while (ite.hasNext()) {
            val entry = ite.next() as Map.Entry<*, *>
            val key = entry.key
            val value = entry.value
            if (key == "province"){
                mThirdLoginEntity?.province = value?.toString()
            }
            if (key == "city"){
                mThirdLoginEntity?.district = value?.toString()
            }
        }
        val msg = Message()
        msg.what = MSG_LOGIN
        msg.obj = platform
        UIHandler.sendMessage(msg, this)
    }
    /**
     * 授权取消的回调
     *
     * @param platform
     * @param i
     */
    override fun onCancel(platform: Platform?, i: Int) {
        val msg = Message()
        msg.what = MSG_AUTH_CANCEL
        msg.obj = platform
        UIHandler.sendMessage(msg, this)
    }
    /**
     * 授权错误的回调
     * @param platform
     * @param i
     * @param throwable
     */
    override fun onError(platform: Platform, i: Int, throwable: Throwable?) {
        val msg = Message()
        msg.what = MSG_AUTH_ERROR
        msg.obj = platform
        UIHandler.sendMessage(msg, this)
        platform.removeAccount(true)
    }
}