/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

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
import android.widget.Toast;

import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.jietuqi.cn.R;
import app.jietuqi.cn.base.BaseOverallInternetActivity;
import app.jietuqi.cn.constant.IntentKey;
import app.jietuqi.cn.web.SonicJavaScriptInterface;
import app.jietuqi.cn.web.SonicRuntimeImpl;
import app.jietuqi.cn.web.SonicSessionClientImpl;

/**
 *  A demo browser activity
 *  In this demo there are three modes,
 *  sonic mode: sonic mode means webview loads html by sonic,
 *  offline mode: offline mode means webview loads html from local offline packages,
 *  default_bg mode: default_bg mode means webview loads html in the normal way.
 *
 */

public class OverallWebViewActivity extends BaseOverallInternetActivity {

    private SonicSession mSonicSession;
    private String mUrl = "https://m.kuaidi100.com/app/?coname=dasheng";
    private WebView mWebView;
    private WebSettings mWebSettings;
    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_overall_web;
    }

    @Override
    protected boolean needLoadingView() {
        return false;
    }

    @Override
    protected void initAllViews() {


    }

    @Override
    protected void initViewsListener() {

    }

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
        mUrl = intent.getStringExtra(IntentKey.URL);
        sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
            @Override
            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                return new OfflinePkgSessionConnection(OverallWebViewActivity.this, session, intent);
            }
        });
        mSonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
        if (null != mSonicSession) {
            mSonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_overall_web);
        String title = intent.getStringExtra(IntentKey.TITLE);
        setTopTitle(title, 0, "", R.color.overallTextColor, R.color.black, R.mipmap.back, R.color.white, R.color.black, -1);
        // init webview
        mWebView = findViewById(R.id.mOverallWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-courier').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
                // hide element by id
//                mWebView.loadUrl("javascript:document.getElementById('t-center').style.display='none'");
                mWebView.loadUrl("javascript:(function() { " + "document.getElementById('t-center').style.display='none';document.querySelector('.menus').style.display='none';document.querySelector('.head a').style.display='none';document.querySelector('.title').innerHTML='';document.querySelector('.tag').innerHTML=''})()");
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
}
