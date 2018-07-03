package xinguang.com.xgjsbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import xinguang.com.xgjsbridge.api.JSApi;
import xinguang.com.xgjsbridge.api.JavaApi;
import xinguang.com.xgjsbridge.interfaces.IXGJSCallHandler;
import xinguang.com.xgjsbridge.interfaces.IXGJavaCallHandler;
import xinguang.com.xgjsbridge.utils.BridgeUtil;

/**
 * Created by vitozhang on 2018/6/29.
 */

public class XGNBridge implements IXGJavaCallHandler{

    private static final String XGJS = "XGJSCore";
    private static final String toLoadJs = "XGJSBridge.js";

    private WebView mWebView;

    private BaseJavascriptInterface mJavascriptInterface;

    private JSApi mJSApi;

    private WebViewClient mWebViewClient;

    private WebChromeClient mWebChromeClient;

    public XGNBridge(WebView webView){
        init(webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(WebView webView){
        //app提供给js的接口
        mWebView = webView;
        mJavascriptInterface = new BaseJavascriptInterface(this);
        if (mWebView == null) {
            return;
        }

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebView.setWebViewClient(generateBridgeWebViewClient());
        mWebView.addJavascriptInterface(mJavascriptInterface, XGJS);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);

        registerJsCallHandler(JavaApi.UserInfoHandler);

        mJSApi = new JSApi(this);

    }

    public void registerJsCallHandler(IXGJSCallHandler ixgBridgeHandler){
        mJavascriptInterface.addCall(ixgBridgeHandler);
    }

    /**
     * 根据协议处理接受到的数据，将参数发送到JS
     */
    protected WebViewClient generateBridgeWebViewClient() {
        return new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mWebViewClient!=null) {
                    return mWebViewClient.shouldOverrideUrlLoading(view, url);
                }else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (mWebViewClient!=null) {
                    mWebViewClient.onPageStarted(view, url, favicon);
                }else {
                    super.onPageStarted(view, url, favicon);
                }
                BridgeUtil.webViewLoadLocalJs(view, toLoadJs);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mWebViewClient!=null) {
                    mWebViewClient.onPageFinished(view, url);
                }else {
                    super.onPageFinished(view, url);
                }
            }


        };
    }

    @Override
    public void send(final String jsCommand) {
        //优化，在kitkat以上使用evaluate

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            loadUrl(jsCommand);
        } else {
            ((Activity) mWebView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadUrl(jsCommand);
                }
            });
        }

    }

    public JSApi getJSApi() {
        return mJSApi;
    }

    public void setJSApi(JSApi jsApi) {
        this.mJSApi = jsApi;
    }

    public void onDestroy(){
        mWebView = null;
        mJavascriptInterface.onDestroy();
    }

    public void setWebViewClient(WebViewClient webViewClient){
        mWebViewClient = webViewClient;
    }

    public void setWebChromeClient(WebChromeClient webChromeClient){
        mWebChromeClient = webChromeClient;
        mWebView.setWebChromeClient(mWebChromeClient);
    }


    private void loadUrl(String jsCommand){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(jsCommand, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        }else {
            mWebView.loadUrl(jsCommand);
        }
    }
}
