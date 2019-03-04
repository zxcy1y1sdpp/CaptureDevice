package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.*
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.AppManager
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_register.*

/**
 * 作者： liuyuanbo on 2018/11/12 17:45.
 * 时间： 2018/11/12 17:45
 * 邮箱： 972383753@qq.com
 * 用途： 注册页面
 */
class OverallRegisterActivity : BaseOverallInternetActivity()/*, MobSmsCodeListener*/ {

    /**
     * 定时器
     */
    private var mTimer: CountDownTimer? = null
    /**
     * 内网ip
     */
    private var IIP = ""
    /**
     * 外网ip
     */
    private var IP = ""
    /**
     * 0 -- 注册
     * 1 -- 绑定手机
     * 2 -- 修改密码
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_overall_register

    override fun needLoadingView() = false

    override fun initAllViews() {

        IIP = IPUtil.getLocalIpV4Address()
        Thread(Runnable {
            // 获取外网ip
            IP = IPUtil.getNetIp2()
            Log.e("ip:外网地址",  IP)
        }).start()
        Log.e("ip:内网地址",  IIP)
    }

    override fun initViewsListener() {
        mOverallRegisterConfirmBtn.setOnClickListener(this)
        mOverallRegisterGetVerificationCodeView.setOnClickListener(this)
        mOverallRegisterUserAgreementTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        when (mType) {
            0 -> setTopTitle("注册账号")
            1 -> {
                setTopTitle("绑定手机")
                mOverallRegisterConfirmBtn.text = "绑定"
                mOverallRegisterUserAgreementTv.visibility = View.GONE
            }
            else -> {
                setTopTitle("修改密码")
                mOverallRegisterConfirmBtn.text = "修改密码"
            }
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallRegisterGetVerificationCodeView ->{
                showLoadingDialog("获取中")
                sendSmsCode()
            }
            R.id.mOverallRegisterConfirmBtn ->{
                if (0 >= OtherUtil.getContentLength(mOverallRegisterVerificationCodeView)){
                    ToastUtils.showShort(this, "验证码输入")
                    return
                }
                showLoadingDialog("请稍后...")
                if (canRegister()){
                    if (mType == 0){
                        registerAndReset()
                    }else if (mType == 2){
                        registerAndReset()
                    }
                    if (mType == 1){
                        binding()
                    }
                }else{
                    dismissLoadingDialog()
                }
            }
            R.id.mOverallRegisterUserAgreementTv ->{
                LaunchUtil.launch(this, OverallProtocolActivity::class.java)
            }
        }
    }
    private fun canRegister(): Boolean{
        if(OtherUtil.getContentLength(mOverallRegisterPhoneNumberEt) != 11){
            ToastUtils.showShort(this, "手机号长度不正确")
            return false
        }
        if (6 > OtherUtil.getContentLength(mOverallRegisterPasswordViewEt)){
            ToastUtils.showShort(this, "密码长度必须大于6位")
            return false
        }
        if (0 >= OtherUtil.getContentLength(mOverallRegisterVerificationCodeView)){
            ToastUtils.showShort(this, "验证码输入有误")
            return false
        }
        return true
    }

    /**
     * 验证码发送倒计时
     */
    private fun downTime() {
        mTimer = object : CountDownTimer((60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mOverallRegisterGetVerificationCodeView.setTextColor(ContextCompat.getColor(this@OverallRegisterActivity, R.color.overallGray3))
                //每隔countDownInterval秒会回调一次onTick()方法
                mOverallRegisterGetVerificationCodeView.text = "重发(" + millisUntilFinished / 1000 + "s)"
                mOverallRegisterGetVerificationCodeView.isClickable = false
            }
            override fun onFinish() {
                mOverallRegisterGetVerificationCodeView.setTextColor(ContextCompat.getColor(this@OverallRegisterActivity, R.color.black))
                mOverallRegisterGetVerificationCodeView.text = "重新发送"
                mOverallRegisterGetVerificationCodeView.isEnabled = true
                mOverallRegisterGetVerificationCodeView.isClickable = true
                mTimer?.cancel()
            }
        }
        mTimer?.start()// 开始计时
    }
    override fun onDestroy() {
        super.onDestroy()
        ThirdPartUtil.getInstance().unregisterEventHandler()
        if (null != mTimer){
            mTimer?.cancel()
            mTimer = null
        }
    }
    private fun registerAndReset(){
        EasyHttp.post(HttpConfig.REGISTER_AND_LOGIN, false)
                .params("mobile", OtherUtil.getContent(mOverallRegisterPhoneNumberEt))
                .params("password", OtherUtil.getContent(mOverallRegisterPasswordViewEt))
                .params("way", "mobile")
                .params("os", "android")
                .params("ip", IP)
                .params("iip", IIP)
                .params("code", OtherUtil.getContent(mOverallRegisterVerificationCodeView))
                .execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
                    override fun onStart() {
                        super.onStart()
                        showLoadingDialog()
                    }
                    override fun onError(e: ApiException) {
                        dismissLoadingDialog()
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: OverallUserInfoEntity?) {
                        dismissLoadingDialog()
                        SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.USER_INFO)
                        SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, true)
                        if(mType == 2){
                            showToast("密码重置成功")
                        }else{
                            showToast("注册成功")
                            AppManager.getInstance().killActivity(OverallLoginActivity::class.java)
                        }
                        finish()
                    }

                }) {})
    }
    private fun binding(){
        val mobile = mOverallRegisterPhoneNumberEt.text.toString()
        EasyHttp.post(HttpConfig.USERS, false)
                .params("way", "edit")
                .params("id", UserOperateUtil.getUserId())
                .params("mobile", mobile)
                .params("password", mOverallRegisterPasswordViewEt.text.toString())
                .params("code", OtherUtil.getContent(mOverallRegisterVerificationCodeView))
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                    override fun onSuccess(t: String) {
                        showToast("手机绑定成功")
                        SharedPreferencesUtils.putData(SharedPreferenceKey.USER_PHONE_NUMBER, mobile)
                        var entity = UserOperateUtil.getUserInfo()
                        entity.mobile = mobile
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.USER_INFO)
                        finish()
                    }
                })
    }
    private fun sendSmsCode(){
        val postRequest = EasyHttp.post(HttpConfig.USERS, false).params("mobile", OtherUtil.getContent(mOverallRegisterPhoneNumberEt)).params("way", "sms")
        when (mType) {
            0 -> //注册
                postRequest.params("classify", "register")
            1 -> //绑定
                postRequest.params("classify", "bind")
            else -> //忘记密码
                postRequest.params("classify", "reset")
        }
        postRequest.execute(object : SimpleCallBack<String>() {
            override fun onError(e: ApiException) {
                dismissLoadingDialog()
                e.message?.let { showToast(it) }
            }
            override fun onSuccess(t: String) {
                downTime()
                showToast("验证码已发送到您的手机，请注意查收！")
            }
        })
    }
}
