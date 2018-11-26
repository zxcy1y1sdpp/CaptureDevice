package app.jietuqi.cn;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;
import com.zhouyou.http.utils.HttpLog;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import app.jietuqi.cn.database.DatabaseUtils;
import app.jietuqi.cn.http.HttpConfig;
import app.jietuqi.cn.http.util.MD5;
import app.jietuqi.cn.util.SharedPreferencesUtils;
import app.jietuqi.cn.web.SonicRuntimeImpl;
import app.jietuqi.cn.widget.ninegrid.NineGridView;

/**
 * 作者： liuyuanbo on 2018/10/9 10:43.
 * 时间： 2018/10/9 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 自定义的Application
 */

public class App extends Application {
    private static App mInstant;
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3);//全局设置主题颜色
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this);
//        MobSDK.init(this, AppConfig.MOB_APP_KEY, AppConfig.MOB_App_Secret);
        globeHttpConfiguration();
        DatabaseUtils.initHelper(this);
        NineGridView.setImageLoader(new GlideImageLoader());
        SharedPreferencesUtils.getInstance(this, SharedPreferencesUtils.SHARED_NAME);
        initTencentWeb();
    }

    public void initTencentWeb(){
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(this), new SonicConfig.Builder().build());
        }
    }
    public void globeHttpConfiguration(){
        EasyHttp.init(this);
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
//        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, AppConstant.APPID));
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", HttpConfig.APPID+"|" + MD5.string2MD5(HttpConfig.APPID + HttpConfig.APPSECRET));
        //设置请求参数
        HttpParams params = new HttpParams();
        EasyHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setBaseUrl(HttpConfig.BASE_URL)
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setHostnameVerifier(new UnSafeHostnameVerifier(HttpConfig.BASE_URL))//全局访问规则
                .setCertificates()//信任所有证书
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(headers);//设置全局公共头
//                .addCommonParams(params)//设置全局公共参数
//                .addInterceptor(new CustomSignInterceptor());//添加参数签名拦截器
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器
    }

    public class UnSafeHostnameVerifier implements HostnameVerifier {
        private String host;

        public UnSafeHostnameVerifier(String host) {
            this.host = host;
            HttpLog.i("###############　UnSafeHostnameVerifier " + host);
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            HttpLog.i("############### verify " + hostname + " " + this.host);
            if (this.host == null || "".equals(this.host) || !this.host.contains(hostname))
                return false;
            return true;
        }
    }
    // 单例模式中获取唯一的MyApplication实例
    public static App getInstance() {
        if (null == mInstant) {
            mInstant = new App();
        }
        return mInstant;
    }
}
