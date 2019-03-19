package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.AgencyInfoEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_edit_agency_info.*

/**
 * 作者： liuyuanbo on 2019/3/14 13:57.
 * 时间： 2019/3/14 13:57
 * 邮箱： 972383753@qq.com
 * 用途： 创建和编辑代理支付宝信息
 */
class OverallEditAgencyInfoActivity : BaseOverallInternetActivity() {
    /**
     * 0 -- 填写
     * 1 -- 修改
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_overall_edit_agency_info

    override fun needLoadingView() = false

    override fun initAllViews() {
        window.decorView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                initStatusBar(R.drawable.agency_status_bar)
                window.decorView.removeOnLayoutChangeListener(this)
            }
        })
    }

    override fun initViewsListener() {
        mEditAgencyInfoPublishTv.setOnClickListener(this)
        mAgencyServerIv.setOnClickListener(this)
        mBackIv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType != 0){
            val entity: AgencyInfoEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AgencyInfoEntity
            mEditAgencyInfoAccountEt.setText(entity.alipay_name)
            mEditAgencyInfoAlipayEt.setText(entity.alipay_account)
            mEditAgencyInfoWechatEt.setText(entity.wx_account)
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mEditAgencyInfoPublishTv ->{
                if (canPublish()){
                    uploadInfo()
                }
            }
            R.id.mBackIv ->{
                finish()
            }
            R.id.mAgencyServerIv ->{
                LaunchUtil.launch(this, OverallServerHelpActivity::class.java)
            }
        }
    }
    private fun canPublish(): Boolean{
        if (TextUtils.isEmpty(OtherUtil.getContent(mEditAgencyInfoAccountEt))){
            showQQTipDialog("请输入真是姓名", 1)
            return false
        }
        if (TextUtils.isEmpty(OtherUtil.getContent(mEditAgencyInfoAlipayEt))){
            showQQTipDialog("请输入支付宝账号", 1)
            return false
        }
        if (TextUtils.isEmpty(OtherUtil.getContent(mEditAgencyInfoWechatEt))){
            showQQTipDialog("请输入微信账号", 1)
            return false
        }
        return true
    }

    private fun uploadInfo() {
        if (UserOperateUtil.isCurrentLogin(this)){
            EasyHttp.post(HttpConfig.AGENT, false)
                    .params("way", "edit")
                    .params("users_id", UserOperateUtil.getUserId())
                    .params("alipay_name", OtherUtil.getContent(mEditAgencyInfoAccountEt))
                    .params("alipay_account", OtherUtil.getContent(mEditAgencyInfoAlipayEt))
                    .params("wx_account", OtherUtil.getContent(mEditAgencyInfoWechatEt))
                    .execute(object : CallBackProxy<OverallApiEntity<AgencyInfoEntity>, AgencyInfoEntity>(object : SimpleCallBack<AgencyInfoEntity>() {
                        override fun onStart() {
                            super.onStart()
                            showQQWaitDialog("请稍后...")
                        }
                        override fun onError(e: ApiException) {
                            dismissQQDialog("提交失败")
                        }
                        override fun onSuccess(t: AgencyInfoEntity) {
                            SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.AGENCY_INFO)
                            if (mType == 0){
                                LaunchUtil.launch(this@OverallEditAgencyInfoActivity, OverallAgencyActivity::class.java)
                                dismissQQDialog("提交成功", needFinish = true)
                            }else{
                                dismissQQDialog("编辑成功", needFinish = true)
                            }
                        }
                    }) {})
        }
    }
}
