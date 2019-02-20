package app.jietuqi.cn.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.Constant
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity
import app.jietuqi.cn.ui.fragment.HomeFragment
import app.jietuqi.cn.ui.fragment.MyFragment
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.widget.MainNavigateTabBar
import app.jietuqi.cn.widget.ProgressButton
import app.jietuqi.cn.widget.dialog.UpdateView
import cn.jzvd.Jzvd
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.utils.HttpLog
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/23 17:14.
 * 时间： 2018/10/23 17:14
 * 邮箱： 972383753@qq.com
 * 用途： App首页
 */

open class HomeActivity : BaseOverallActivity(), UpdateView.UpdateListener {
    /**
     * 是否正在更新
     */
    private var mDownloading = false
    /**
     * 是否不更新
     */
    private var mDisposable: Disposable? = null
    override fun update(update: Boolean, url: String, btn: ProgressButton) {
        if (update){//去更新
            if (!mDownloading){
                updataApk(url, btn)
            }else{
                showToast("正在更新，请稍后...")
            }
        }else{
            EasyHttp.cancelSubscription(mDisposable)
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_home
    override fun needLoadingView(): Boolean {
        return false
    }
    override fun initAllViews() {
        Log.e("分辨率", resources.displayMetrics.toString())
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            var bankList = UserOperateUtil.getWechatSimulatorBank()
            if (null ==  bankList || bankList.size == 0){
                var list = arrayListOf<WechatBankEntity>()
                list.add(WechatBankEntity("R.drawable.wechat_zhongguoyinhang", "中国银行储蓄卡", "中国银行", 20000, "5685", "当天24点前到账"))
                SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_SIMULATOR_BANK_LIST, list)
            }
        }
        getProjectClassify()
        getIndustryData()
        getGroupIndustryData()
        registerEventBus()
        mMainTabBar.addTab(this, HomeFragment::class.java, MainNavigateTabBar.TabParam(R.drawable.overall_home_unselect, R.drawable.overall_home_select, "首页"))
        mMainTabBar.addTab(this, MyFragment::class.java, MainNavigateTabBar.TabParam(R.drawable.overall_my_unselect, R.drawable.overall_my_select, "我的"))
    }

    override fun initViewsListener() {}

    private var mExitTime: Long = 0
    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showShort(this, "再按一次退出程序")
            mExitTime = System.currentTimeMillis()
        } else {
            Jzvd.releaseAllVideos()
            AppManager.getInstance().AppExit(this)
        }
    }

    /**
     * 事件订阅者自定义的接收方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(entity: ProductFlavorsEntity) {
        if (!TextUtils.isEmpty(entity.apkurl)){
            var msg = "忽略会导致您在使用的过程中出现问题，建议更新！"
            if (!TextUtils.isEmpty(entity.intro)){
                val builder = StringBuilder()
                val message = entity.intro.split(",")
                if (message != null && message.isNotEmpty()) {
                    var i = 0
                    val size = message.size
                    while (i < size) {
                        builder.append(message[i]).append("\n")
                        i++
                    }
                }
                msg = builder.toString()
            }
            var dialog = UpdateView()
            dialog.setData(msg, entity.apkurl, this)
            dialog.show(supportFragmentManager, "update")
        }
    }

    private fun updataApk(apkUrl: String, progressButton: ProgressButton) {//下载回调是在异步里处理的
        FileUtil.RecursionDeleteFile(Constant.APK_PATH)
        mDisposable = EasyHttp.downLoad(apkUrl).saveName("wxyxb.apk")//默认名字是时间戳生成的
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                        val progress = (bytesRead * 100 / contentLength).toInt()
                        progressButton.setProgress(progress)
                        HttpLog.e(progress.toString() + "% ")
                        if (done) {
                        }
                    }

                    override fun onStart() {
                        mDownloading = true
                        HttpLog.i("======" + Thread.currentThread().name)
                    }

                    override fun onComplete(path: String) {
                        var apkFile = File(path)
                        progressButton.reset()
                        installApk(apkFile)
                    }

                    override fun onError(e: ApiException) {
                        HttpLog.i("======" + Thread.currentThread().name)
                    }
                })
    }

    private fun installApk(apk: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive")
        } else {//Android7.0之后获取uri要用contentProvider
            val uri = FileProvider.getUriForFile(this, "app.jietuqi.cn.fileprovider", apk)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    /**
     * 获取群类别
     */
    private fun getGroupIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "heapsort")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.HEAPSORT, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }

    /**
     * 获取行业类别
     */
    private fun getIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "industry")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.INDUSTRY, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }
    /**
     * 获取行业类别
     */
    private fun getProjectClassify(){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "industry")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.PROJECT_CLASSIFY, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }
}
