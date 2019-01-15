package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.util.GlideCacheUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/10/25 15:10.
 * 时间： 2018/10/25 15:10
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class SettingActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_setting

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("设置")
        mOverallSettingCacheSizeTv.text = GlideCacheUtil.getInstance().getCacheSize(this)
    }

    override fun initViewsListener() {
        mClearCacheLayout.setOnClickListener(this)
        mWechatNumLayout.setOnClickListener(this)
        mAboutUsLayout.setOnClickListener(this)
        mGiveUsSupportLayout.setOnClickListener(this)
        mExitLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mClearCacheLayout ->{
                clean()
            }
            R.id.mWechatNumLayout ->{}
            R.id.mAboutUsLayout ->{
                LaunchUtil.launch(this, OverallAboutUsActivity::class.java)
            }
            R.id.mExitLayout ->{

//                EventBusUtil.post(OverallUserInfoEntity())
                showLoadingDialog("正在退出...")
                SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, false)
                SharedPreferencesUtils.saveBean2Sp(OverallUserInfoEntity(), SharedPreferenceKey.USER_INFO)
                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                    dismissLoadingDialog()
                    finish()
                }
            }
            R.id.mGiveUsSupportLayout ->{}
        }
    }
    private fun clean() {
        if (!this.isFinishing) {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCanTouchOutSideCancle(false)
                    .setTitleText("提示")
                    ?.setContentText("是否清理磁盘上的缓存文件")
                    ?.setConfirmText("清理")
                    ?.setCancelText("取消")
                    ?.setConfirmClickListener {
                        it.contentText = "清理完成"
                        it.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        it.confirmText = "完成"
                        it.showCancelButton(false)
                        it.setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        GlideCacheUtil.getInstance().clearImageAllCache(this)
                        mOverallSettingCacheSizeTv.text = GlideCacheUtil.getInstance().getCacheSize(this@SettingActivity)
                    }?.setCancelClickListener {
                        it.dismissWithAnimation()
                    }?.show()
        }
    }
}
