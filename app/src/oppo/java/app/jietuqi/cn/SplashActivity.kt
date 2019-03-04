package app.jietuqi.cn

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.WindowManager
import app.jietuqi.cn.base.BaseActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.HomeActivity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity
import com.zhouyou.http.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/10/15 10:43.
 * 时间： 2018/10/15 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 开机启动页面
 */
open class SplashActivity : BaseActivity() {
    override fun setLayoutResourceId() = 0

    override fun needLoadingView() = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAllViews() {
        requestPrivatePermission()
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
    override fun initViewsListener() {}

    private fun getData(){
        UserOperateUtil.getChannelParams(this).execute(object : CallBackProxy<OverallApiEntity<ProductFlavorsEntity>, ProductFlavorsEntity>(object : SimpleCallBack<ProductFlavorsEntity>() {
            override fun onSuccess(t: ProductFlavorsEntity) {
                var version = UserOperateUtil.getChannelVersion()
                var status = UserOperateUtil.getChannelStatus()
                if (version == "-1"){//-1代表没存过
                    SharedPreferencesUtils.putData(SharedPreferenceKey.CHANNEL_VERSION, t.appversion)
                }else{
                    if (version != t.appversion){//本地存储的版本和当前的版本不一样需要存
                        SharedPreferencesUtils.putData(SharedPreferenceKey.CHANNEL_VERSION, t.appversion)
                    }
                }
                if (status == -1){//-1代表没存过
                    SharedPreferencesUtils.putData(SharedPreferenceKey.CHANNEL_STATUS, t.status)
                }else{
                    if (status != t.status){//本地存储的开关和当前的版本的开关不一样就需要存
                        SharedPreferencesUtils.putData(SharedPreferenceKey.CHANNEL_STATUS, t.status)
                    }
                }
                EventBusUtil.postSticky(t)
            }
            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }

            }
        }) {})
    }
    private fun requestPrivatePermission() {
        getData()
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            LaunchUtil.launch(this@SplashActivity, HomeActivity::class.java)
            finish()
        }
    }
}