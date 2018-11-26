package app.jietuqi.cn.ui.activity

import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.MobSmsCodeListener
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallThridLoginEntity
import app.jietuqi.cn.util.*
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.mob.tools.utils.UIHandler
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
class OverallLoginActivity : BaseOverallInternetActivity(), MobSmsCodeListener, PlatformActionListener, Handler.Callback{
    /**
     * 定时器
     */
    private var mTimer: CountDownTimer? = null

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

    override fun setLayoutResourceId() = R.layout.activity_overall_login

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }

    override fun initViewsListener() {
        mOverallLoginBtn.setOnClickListener(this)
        mOverallLoginSignInTv.setOnClickListener(this)
        mOverallLoginAccountSmsCodeTv.setOnClickListener(this)
        mOverallLoginWechatIv.setOnClickListener(this)
        mOverallLoginQQIv.setOnClickListener(this)
        mOverallLoginGetVerificationCodeTv.setOnClickListener(this)
        mOverallLoginUserAgreementTv.setOnClickListener(this)
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
                        1 ->{
                            ThirdPartUtil.getInstance().verifySmsCode(mOverallLoginAccountEt.text.toString(), mOverallLoginPasswordEt.text.toString(), this)
                        }
                        2 ->{}
                        3 ->{}
                    }
                }
            }
            R.id.mOverallLoginSignInTv ->{
                LaunchUtil.startOverallRegisterActivity(this, 0)
//                LaunchUtil.launch(this, OverallRegisterActivity::class.java)
            }
            R.id.mOverallLoginAccountSmsCodeTv ->{
                if (mLoginType == 0){
                    mLoginType = 1//验证码登录
                    mOverallLoginAccountSmsCodeTv.text = "验证码登录"
                    mOverallLoginPasswordEt.hint = "请输入验证码"
                    mOverallLoginGetVerificationCodeTv.visibility = View.VISIBLE
                }else{
                    mLoginType = 0//账号密码登录
                    mOverallLoginAccountSmsCodeTv.text = "账号密码登录"
                    mOverallLoginGetVerificationCodeTv.visibility = View.GONE
                    mOverallLoginPasswordEt.hint = "请输入密码"
                }
            }
            R.id.mOverallLoginWechatIv ->{//微信登录
                authorize(ShareSDK.getPlatform(Wechat.NAME))
            }
            R.id.mOverallLoginQQIv ->{//QQ登录
                authorize(ShareSDK.getPlatform(QQ.NAME))
            }
            R.id.mOverallLoginGetVerificationCodeTv ->{//发送验证码
                if(TextUtils.isEmpty(OtherUtil.getContent(mOverallLoginAccountEt)) || !AccountValidatorUtil.isMobile(OtherUtil.getContent(mOverallLoginAccountEt))){
                    Toast.makeText(this, "账号填写不正确", Toast.LENGTH_SHORT).show()
                    return
                }
                ThirdPartUtil.getInstance().sendSmsCode(mOverallLoginAccountEt.text.toString(), this)
            }
            R.id.mOverallLoginUserAgreementTv ->{//用户协议
                LaunchUtil.launch(this, OverallProtocolActivity::class.java)
            }
        }
    }

    private fun canLogin(): Boolean{
        if(TextUtils.isEmpty(OtherUtil.getContent(mOverallLoginAccountEt)) || !AccountValidatorUtil.isMobile(OtherUtil.getContent(mOverallLoginAccountEt))){
            Toast.makeText(this, "账号填写不正确", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mLoginType == 0){
            if (6 > OtherUtil.getContentLength(mOverallLoginPasswordEt)){
                Toast.makeText(this, "密码长度必须大于6位", Toast.LENGTH_SHORT).show()
                return false
            }
        }else if (mLoginType == 1){
            if (6 > OtherUtil.getContentLength(mOverallLoginPasswordEt)){
                Toast.makeText(this, "验证码输入不正确", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }
    private fun login(){
        var request: PostRequest = EasyHttp.post(HttpConfig.REGISTER_AND_LOGIN)
//                .params("way", "mobile")

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
        }
        request.execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
            override fun onStart() {
                super.onStart()
                ProgressUtils.showProgressDialog("登录中...", this@OverallLoginActivity)
            }
            override fun onError(e: ApiException) {
                dismissLoadingDialog()
                e.message?.let { showToast(it) }
            }
            override fun onSuccess(t: OverallUserInfoEntity) {
                dismissLoadingDialog()
                Toast.makeText(this@OverallLoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.USER_INFO)
                SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, true)
                SharedPreferencesUtils.putData(SharedPreferenceKey.USER_ID, t.id.toString())
                SharedPreferencesUtils.putData(SharedPreferenceKey.USER_AVATAR, t.headimgurl)
                SharedPreferencesUtils.putData(SharedPreferenceKey.USER_NICKNAME, t.nickname)
                SharedPreferencesUtils.putData(SharedPreferenceKey.USER_GENDER, t.sex)
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
    /**
     * 验证码发送倒计时
     */
    private fun downTime() {
        mTimer = object : CountDownTimer((60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //每隔countDownInterval秒会回调一次onTick()方法
                mOverallLoginGetVerificationCodeTv.text = "重发(" + millisUntilFinished / 1000 + "s)"
                mOverallLoginGetVerificationCodeTv.setTextColor(ContextCompat.getColor(this@OverallLoginActivity, R.color.overallGray))
                mOverallLoginGetVerificationCodeTv.isClickable = false
                mOverallLoginGetVerificationCodeTv.isEnabled = false
            }
            override fun onFinish() {
                mOverallLoginGetVerificationCodeTv.text = "重新发送"
                mOverallLoginGetVerificationCodeTv.isEnabled = true
                mOverallLoginGetVerificationCodeTv.isClickable = true
                mOverallLoginGetVerificationCodeTv.setTextColor(ContextCompat.getColor(this@OverallLoginActivity, R.color.white))
                mTimer?.cancel()
            }
        }
        mTimer?.start()// 开始计时
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
                Toast.makeText(this@OverallLoginActivity, "取消了", Toast.LENGTH_SHORT).show()
                ProgressUtils.cancleProgressDialog()
            }
            MSG_AUTH_ERROR -> {
                ProgressUtils.cancleProgressDialog()
                Toast.makeText(this@OverallLoginActivity, "授权错误", Toast.LENGTH_SHORT).show()
            }
            MSG_AUTH_COMPLETE -> {
                Toast.makeText(this@OverallLoginActivity, "R.string.auth_complete", Toast.LENGTH_SHORT).show()
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
    override fun sendCodeSuccess() {
        downTime()
        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
        Toast.makeText(this@OverallLoginActivity, "验证码已发送到您的手机，请注意查收", Toast.LENGTH_SHORT).show()
    }

    override fun sendCodeFail() {
        Toast.makeText(this@OverallLoginActivity, "验证码发送失败，请联系客服", Toast.LENGTH_SHORT).show()
    }

    override fun verifyCodeSuccess() {
        login()
    }

    override fun verifyCodeFail() {
        Toast.makeText(this@OverallLoginActivity, "验证码校验失败，请联系客服", Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        super.onDestroy()
        ThirdPartUtil.getInstance().unregisterEventHandler()
        if (null != mTimer){
            mTimer?.cancel()
            mTimer = null
        }
    }
}