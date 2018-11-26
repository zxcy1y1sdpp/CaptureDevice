package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.MobSmsCodeListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.*
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.widget.ProgressUtils
import kotlinx.android.synthetic.main.activity_overall_register.*


/**
 * 作者： liuyuanbo on 2018/11/12 17:45.
 * 时间： 2018/11/12 17:45
 * 邮箱： 972383753@qq.com
 * 用途： 注册页面
 */
class OverallRegisterActivity : BaseOverallInternetActivity(), MobSmsCodeListener {

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
        if (mType == 0){
            setTitle("注册账号")
        }else{
            setTitle("绑定手机")
            mOverallRegisterConfirmBtn.text = "绑定"
            mOverallRegisterUserAgreementTv.visibility = View.GONE
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallRegisterGetVerificationCodeView ->{
                showLoadingDialog("获取中")
                ThirdPartUtil.getInstance().sendSmsCode(mOverallRegisterPhoneNumberEt.text.toString(), this)
            }
            R.id.mOverallRegisterConfirmBtn ->{
                if (6 != OtherUtil.getContentLength(mOverallRegisterVerificationCodeView)){
                    Toast.makeText(this, "验证码输入有误", Toast.LENGTH_SHORT).show()
                    return
                }
                showLoadingDialog("请稍后...")
                ThirdPartUtil.getInstance().verifySmsCode(mOverallRegisterPhoneNumberEt.text.toString(), mOverallRegisterVerificationCodeView.text.toString(), this)
            }
            R.id.mOverallRegisterUserAgreementTv ->{
                LaunchUtil.launch(this, OverallProtocolActivity::class.java)
            }
        }
    }
    private fun canRigister(): Boolean{
        if(TextUtils.isEmpty(OtherUtil.getContent(mOverallRegisterPhoneNumberEt))){
            Toast.makeText(this, "请填写账号", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!AccountValidatorUtil.isMobile(OtherUtil.getContent(mOverallRegisterPhoneNumberEt))){
            Toast.makeText(this, "请正确填写手机号", Toast.LENGTH_SHORT).show()
            return false
        }
        if (6 > OtherUtil.getContentLength(mOverallRegisterPasswordViewEt)){
            Toast.makeText(this, "密码长度必须大于6位", Toast.LENGTH_SHORT).show()
            return false
        }
        if (6 != OtherUtil.getContentLength(mOverallRegisterVerificationCodeView)){
            Toast.makeText(this, "验证码输入有误", Toast.LENGTH_SHORT).show()
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
                mOverallRegisterGetVerificationCodeView.setBgColor(ContextCompat.getColor(this@OverallRegisterActivity, R.color.overallGray3))
                //每隔countDownInterval秒会回调一次onTick()方法
                mOverallRegisterGetVerificationCodeView.text = "重发(" + millisUntilFinished / 1000 + "s)"
                mOverallRegisterGetVerificationCodeView.isClickable = false
            }
            override fun onFinish() {
                mOverallRegisterGetVerificationCodeView.setBgColor(ContextCompat.getColor(this@OverallRegisterActivity, R.color.overallBlue))
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
    private fun register(){
        EasyHttp.post(HttpConfig.REGISTER_AND_LOGIN)
                .params("mobile", OtherUtil.getContent(mOverallRegisterPhoneNumberEt))
                .params("password", OtherUtil.getContent(mOverallRegisterPasswordViewEt))
                .params("way", "mobile")
                .params("ip", IP)
                .params("iip", IIP)
                .execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
                    override fun onStart() {
                        super.onStart()
                        ProgressUtils.showProgressDialog("正在注册账号...", this@OverallRegisterActivity)
                    }
                    override fun onError(e: ApiException) {
                        dismissLoadingDialog()
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: OverallUserInfoEntity?) {
                        SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, true)
                        Toast.makeText(this@OverallRegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                        dismissLoadingDialog()
                        SharedPreferencesUtils.putData(SharedPreferenceKey.USER_INFO, t)
                        AppManager.getInstance().killActivity(OverallLoginActivity::class.java)
                        finish()
                    }

                }) {})
    }
    private fun binding(){
        val mobile = mOverallRegisterPhoneNumberEt.text.toString()
        EasyHttp.post(HttpConfig.USERS)
                .params("way", "edit")
                .params("id", UserOperateUtil.getUserId())
                .params("mobile", mobile)
                .params("password", mOverallRegisterPasswordViewEt.text.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {}
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
    override fun sendCodeSuccess() {
        dismissLoadingDialog()
        downTime()
        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
        Toast.makeText(this@OverallRegisterActivity, "验证码已发送到您的手机，请注意查收", Toast.LENGTH_SHORT).show()
    }

    override fun sendCodeFail() {
        dismissLoadingDialog()
        Toast.makeText(this@OverallRegisterActivity, "验证码发送失败，请稍后再试： ", Toast.LENGTH_SHORT).show()
    }

    override fun verifyCodeSuccess() {
        dismissLoadingDialog()
        if (canRigister()){
            if (mType == 0){
                register()

            }else{
                binding()
            }
        }
    }

    override fun verifyCodeFail() {
        dismissLoadingDialog()
        Toast.makeText(this@OverallRegisterActivity, "验证失败： 请联系客服" , Toast.LENGTH_SHORT).show()
    }
}
