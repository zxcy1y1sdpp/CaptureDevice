package app.jietuqi.cn.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.jietuqi.cn.R;
import app.jietuqi.cn.web.SonicRuntimeImpl;
import app.jietuqi.cn.web.SonicSessionClientImpl;

/**
 * 作者： liuyuanbo on 2019/2/20 13:37.
 * 时间： 2019/2/20 13:37
 * 邮箱： 972383753@qq.com
 * 用途： 使用说明
 */
public class HowToUseFragment extends Fragment {
    private SonicSession mSonicSession;
    private String mUrl = "http://jietuqi.cn/index/index/news/id/2";
    private WebView mWebView;
    private WebSettings mWebSettings;
    /**
     * 根view
     */
    private View mRootView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getActivity().getApplication()), new SonicConfig.Builder().build());
        }
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
                return new OfflinePkgSessionConnection(getActivity(), session, intent);
            }
        });
        mSonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
        if (null != mSonicSession) {
            mSonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            Toast.makeText(getActivity(), "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
        /**
         * 解决内存泄漏的重要方法，防止onCreateView方法多次执行
         */
        if (null != mRootView) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeView(mRootView);
        } else {
            mRootView = inflater.inflate(R.layout.fragment_how_to_use, container, false);
            //            onLazyLoad();
            //            onLoad();
        }
        mWebView = mRootView.findViewById(R.id.mHowToUseWv);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
//                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-courier').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
                // hide element by id
//                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-center').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
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

//        WebSettings webSettings = mWebView.getSettings();

        mWebSettings = mWebView.getSettings();
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        mWebSettings.setJavaScriptEnabled(true);
//        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");//防止JS注入

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
        return mRootView;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getActivity().getApplication()), new SonicConfig.Builder().build());
        }
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
                return new OfflinePkgSessionConnection(getActivity(), session, intent);
            }
        });
        mSonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
        if (null != mSonicSession) {
            mSonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            Toast.makeText(getActivity(), "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
        mWebView = findViewById(R.id.mHowToUseWv);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
//                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-courier').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
                // hide element by id
//                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-center').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
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

//        WebSettings webSettings = mWebView.getSettings();

        mWebSettings = mWebView.getSettings();
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        mWebSettings.setJavaScriptEnabled(true);
//        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");//防止JS注入

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
    }*/
//    @Override
//    public void onBackPressed() {
//        if (mWebView.canGoBack()){
//            mWebView.goBack();
//            return;
//        }
//        super.onBackPressed();
//    }


    @Override
    public void onDestroyView() {
        if (null != mSonicSession) {
            mSonicSession.destroy();
            mSonicSession = null;
        }
        super.onDestroyView();
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
}
