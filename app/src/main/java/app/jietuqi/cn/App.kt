package app.jietuqi.cn

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.text.TextUtils
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.database.DatabaseUtils
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.http.util.MD5
import app.jietuqi.cn.ui.alipayscreenshot.widget.EmojiAlipayManager
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.qqscreenshot.widget.EmojiQQManager
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.web.SonicRuntimeImpl
import app.jietuqi.cn.widget.ninegrid.NineGridView
import com.mob.MobSDK
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.cache.converter.SerializableDiskConverter
import com.zhouyou.http.model.HttpHeaders
import com.zhouyou.http.utils.HttpLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * 作者： liuyuanbo on 2018/10/9 10:43.
 * 时间： 2018/10/9 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 自定义的Application
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initBugly()
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this)
        globeHttpConfiguration()
        DatabaseUtils.initHelper(this)
        NineGridView.setImageLoader(GlideImageLoader())
        SharedPreferencesUtils.getInstance(this, SharedPreferencesUtils.SHARED_NAME)
        initTencentWeb()
        initRoleLibrary()
        EmojiWechatManager.init(this)
        EmojiAlipayManager.init(this)
        EmojiQQManager.init(this)
//        MagicScreenAdapter.initDesignWidthInDp(360)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    private fun initTencentWeb() {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(this), SonicConfig.Builder().build())
        }
    }
    private fun initRoleLibrary(){
        var helper = RoleLibraryHelper(this)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val list = helper.query()
            if (list.isNullOrEmpty() || list.size <= 0){
                var entity: WechatUserEntity
                var nickName: String
                for (i in RandomUtil.randomAvatar.indices){
                    nickName = RandomUtil.getOrderNickName(i)
                    entity = WechatUserEntity(RandomUtil.getOrderAvatar(i), "", nickName, OtherUtil.transformPinYin(nickName))
                    var userId = helper.save(entity)
                    entity.wechatUserId = userId.toString()
                    helper.update(entity)
                    if (i == 0){//第一条数据就是聊天页面的我的身份
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.MY_SELF)
                    }
                    if (i == 1){//第一条数据就是聊天页面的对方的身份
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.OTHER_SIDE)
                    }
                }
            }else{
                var mySelf: WechatUserEntity? = UserOperateUtil.getMySelf()
                if (null == mySelf){
                    SharedPreferencesUtils.saveBean2Sp(list[0], SharedPreferenceKey.MY_SELF)
                }
                var otherSide: WechatUserEntity? = UserOperateUtil.getOtherSide()
                if (null == otherSide){
                    SharedPreferencesUtils.saveBean2Sp(list[1], SharedPreferenceKey.OTHER_SIDE)
                }
            }
        }
    }

    /**
     * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
     * 输出详细的Bugly SDK的Log；
     * 每一条Crash都会被立即上报
     * 自定义日志将会在Logcat中输出
     * 建议在测试阶段建议设置成true，发布时设置为false。
     */
    private fun initBugly() {
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 建议在测试阶段建议设置成true，发布时设置为false。
        // 初始化Bugly
        CrashReport.initCrashReport(context, AppConfig.BUGLY_APP_ID, true, strategy)
        /**
         * bugly的标签
         * 区分是线上代码报错还是开发代码报错
         */
        CrashReport.setUserSceneTag(applicationContext, if(BuildConfig.DEBUG) 99740 else 99739)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
    private fun globeHttpConfiguration() {
        EasyHttp.init(this)
        //设置请求头
        val headers = HttpHeaders()
        //        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, AppConstant.APPID));
        //        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", HttpConfig.APPID + "|" + MD5.string2MD5(HttpConfig.APPID + HttpConfig.APPSECRET))
        headers.put("Accept-Encoding", "identity")
        //设置请求参数
        EasyHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut((60 * 1000).toLong())
                .setWriteTimeOut((60 * 1000).toLong())
                .setConnectTimeout((60 * 1000).toLong())
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setBaseUrl(HttpConfig.BASE_URL)
                .setCacheDiskConverter(SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize((50 * 1024 * 1024).toLong())//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setHostnameVerifier(UnSafeHostnameVerifier(HttpConfig.BASE_URL))//全局访问规则
                .setCertificates()//信任所有证书
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(headers)//设置全局公共头
        //                .addCommonParams(params)//设置全局公共参数
        //                .addInterceptor(new CustomSignInterceptor());//添加参数签名拦截器
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器
    }

    inner class UnSafeHostnameVerifier(private val host: String?) : HostnameVerifier {

        init {
            HttpLog.i("###############　UnSafeHostnameVerifier $host")
        }

        override fun verify(hostname: String, session: SSLSession): Boolean {
            HttpLog.i("############### verify " + hostname + " " + this.host)
            return !(this.host == null || "" == this.host || !this.host.contains(hostname))
        }
    }

    companion object {
        private var mInstant: App? = null

        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3)//全局设置主题颜色
                ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3)//全局设置主题颜色
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }

        // 单例模式中获取唯一的MyApplication实例
        val instance: App
            get() {
                if (null == mInstant) {
                    mInstant = App()
                }
                return mInstant as App
            }
    }
}
