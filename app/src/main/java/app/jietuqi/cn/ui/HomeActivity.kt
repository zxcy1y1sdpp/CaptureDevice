package app.jietuqi.cn.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.widget.Toast
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.Constant
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity
import app.jietuqi.cn.ui.fragment.HomeFragment
import app.jietuqi.cn.ui.fragment.MyFragment
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.widget.MainNavigateTabBar
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import cn.jzvd.Jzvd
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.utils.HttpLog
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/23 17:14.
 * 时间： 2018/10/23 17:14
 * 邮箱： 972383753@qq.com
 * 用途： App首页
 */

open class HomeActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_home
    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
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
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
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
                        builder.append(message[i])/*.append("\n")*/
                        i++
                    }
                }
                msg = builder.toString()
            }
            SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setCanTouchOutSideCancle(false)
                    .setTitleText("发现新版本")
                    ?.setContentText(msg)
                    ?.setContentText(msg)
                    ?.setConfirmText("去更新")
                    ?.setCancelText("暂不更新")
                    ?.setConfirmClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                        updataApk(entity.apkurl)
                    }?.setCancelClickListener {
                        it.dismissWithAnimation()
                    }?.show()
        }
    }

    private fun updataApk(apkUrl: String) {//下载回调是在异步里处理的
        FileUtil.RecursionDeleteFile(Constant.APK_PATH)
        EasyHttp.downLoad(apkUrl)
                //EasyHttp.downLoad("http://crfiles2.he1ju.com/0/925096f8-f720-4aa5-86ae-ef30548d2fdc.txt")
                .savePath(Constant.APK_PATH)//默认在：/storage/emulated/0/Android/data/包名/files/1494647767055
                .saveName("wxyxb.apk")//默认名字是时间戳生成的
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                        val progress = (bytesRead * 100 / contentLength).toInt()
                        HttpLog.e(progress.toString() + "% ")
                        if (done) {
                        }
                    }

                    override fun onStart() {
                        HttpLog.i("======" + Thread.currentThread().name)
                    }

                    override fun onComplete(path: String) {
                        var apkFile = File(path)
                        installApk(apkFile)
                    }

                    override fun onError(e: ApiException) {
                        HttpLog.i("======" + Thread.currentThread().name)
                        Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
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
}
