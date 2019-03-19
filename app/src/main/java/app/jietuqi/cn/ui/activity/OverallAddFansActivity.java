package app.jietuqi.cn.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.request.PostRequest;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.jietuqi.cn.R;
import app.jietuqi.cn.base.BaseOverallInternetActivity;
import app.jietuqi.cn.http.HttpConfig;
import app.jietuqi.cn.ui.entity.OverallApiEntity;
import app.jietuqi.cn.ui.entity.OverallVipCardEntity;
import app.jietuqi.cn.util.LaunchUtil;
import app.jietuqi.cn.util.StringUtils;
import app.jietuqi.cn.util.UserOperateUtil;
import app.jietuqi.cn.web.SonicJavaScriptInterface;
import app.jietuqi.cn.web.SonicRuntimeImpl;
import app.jietuqi.cn.web.SonicSessionClientImpl;

/**
 * 作者： liuyuanbo on 2019/2/19 11:54.
 * 时间： 2019/2/19 11:54
 * 邮箱： 972383753@qq.com
 * 用途： 清理死粉
 */
public class OverallAddFansActivity extends BaseOverallInternetActivity {
    private SonicSession mSonicSession;
    private String mUrl = "http://jietuqi.cn/index/index/news/id/6";
    private WebView mWebView;
    private WebSettings mWebSettings;
    private ArrayList<OverallVipCardEntity> mPriceList = new ArrayList<>();
    private TextView mTitleTv = null;
    private TextView mNotVipPriceTv = null;
    private TextView mVipTv = null;
    private LinearLayout mBgLayout = null;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_overall_add_fans;
    }

    @Override
    protected boolean needLoadingView() {
        return false;
    }

    @Override
    protected void initAllViews() { }

    @Override
    protected void initViewsListener() { }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
    }

    @Override
    protected void getAttribute(@NotNull Intent intent) {
        super.getAttribute(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        SonicSessionClientImpl sonicSessionClient = null;
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
            @Override
            public String getCacheData(SonicSession session) {
                return null; // offline pkg does not need cache
            }
        });
        sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
            @Override
            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                return new OverallAddFansActivity.OfflinePkgSessionConnection(OverallAddFansActivity.this, session, intent);
            }
        });
        mSonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
        if (null != mSonicSession) {
            mSonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
        setTopTitle("微爆粉加粉", 0, "订单记录", R.color.overallTextColor, R.color.black, R.mipmap.back, R.color.white, R.color.black, -1);
        // init webview
        mWebView = findViewById(R.id.mWebView);
        mBgLayout = findViewById(R.id.mBgLayout);
        mBgLayout.getBackground().mutate().setAlpha(200);
        mTitleTv = findViewById(R.id.mTitleTv);
        mNotVipPriceTv = findViewById(R.id.notVipPriceTv);
        mVipTv = findViewById(R.id.vipPriceTv);
        findViewById(R.id.mOpenVipTv).setOnClickListener(v -> {
            LaunchUtil.launch(this, OverallPurchaseVipActivity.class);
        });
        findViewById(R.id.mBuyTv).setOnClickListener(v -> {
            if (UserOperateUtil.isCurrentLogin(this)){
                LaunchUtil.startOverallCleanFansConfirmOrderActivity(OverallAddFansActivity.this, mPriceList.get(0), mPriceList.get(1), 1);
            }
        });
        findViewById(R.id.overAllRightTitleTv).setOnClickListener(v -> {
            if (UserOperateUtil.isCurrentLogin(this)){
                LaunchUtil.startOverallCleanFansOrderActivity(this, 1);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mSonicSession != null) {
                    mSonicSession.getSessionClient().pageFinish(url);
                }
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (mSonicSession != null) {
                    return (WebResourceResponse) mSonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }
        });

        mWebSettings = mWebView.getSettings();
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        mWebSettings.setJavaScriptEnabled(true);
//        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");//防止JS注入
        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
        mWebView.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

        // init webview settings
        mWebSettings.setAllowContentAccess(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setSavePassword(false);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);


        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(mWebView);
            sonicSessionClient.clientReady();
        } else { // default_bg mode
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVipMoney();
    }

    private void getVipMoney(){
        PostRequest request  = EasyHttp.post(HttpConfig.PRICE, false).params("way", "vipqingfen2");
        request.execute(new CallBackProxy<OverallApiEntity<ArrayList<OverallVipCardEntity>>, ArrayList<OverallVipCardEntity>>(new SimpleCallBack<ArrayList<OverallVipCardEntity>> () {
            @Override
            public void onError(ApiException e) {}

            @Override
            public void onSuccess(ArrayList<OverallVipCardEntity> overallVipCardEntities) {
                mVipTv.setText(StringUtils.insertFront(overallVipCardEntities.get(0).price, "VIP价:"));
                mPriceList.add(overallVipCardEntities.get(0));
                getMoney();
            }
        }) {});
    }
    private void getMoney(){
        PostRequest request  = EasyHttp.post(HttpConfig.PRICE, false).params("way", "qingfen2");
        request.execute(new CallBackProxy<OverallApiEntity<ArrayList<OverallVipCardEntity>>, ArrayList<OverallVipCardEntity>>(new SimpleCallBack<ArrayList<OverallVipCardEntity>> () {
            @Override
            public void onError(ApiException e) {}

            @Override
            public void onSuccess(ArrayList<OverallVipCardEntity> overallVipCardEntities) {
                mPriceList.add(overallVipCardEntities.get(0));
                mTitleTv.setText(ToDBC("微爆粉加粉-周卡非VIP" + mPriceList.get(1).price + ",VIP" + mPriceList.get(0).price));
                mNotVipPriceTv.setText(StringUtils.insertFront(overallVipCardEntities.get(0).price, "非VIP价:"));

            }
        }) {});
    }
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (null != mSonicSession) {
            mSonicSession.destroy();
            mSonicSession = null;
        }
        super.onDestroy();
    }

    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }

    /*** 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /** * 去除特殊字符或将所有中文标号替换为英文标号
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
