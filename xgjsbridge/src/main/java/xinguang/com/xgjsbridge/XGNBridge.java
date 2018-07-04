package xinguang.com.xgjsbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import xinguang.com.xgjsbridge.WebClient.JSWebViewClient;
import xinguang.com.xgjsbridge.api.JSApi;
import xinguang.com.xgjsbridge.interfaces.IJavascriptInterface;
import xinguang.com.xgjsbridge.interfaces.IXGInterceptor;
import xinguang.com.xgjsbridge.interfaces.IXGToJavaHandler;
import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;


/**
 * Created by vitozhang on 2018/6/29.
 */

public class XGNBridge implements IXGToJsHandler {

    private static final String XGJS = "XGJSCore";

    private WebView mWebView;

    private JavascriptInterfaceImpl mJavascriptInterface;

    private JSApi mJSApi;

    public XGNBridge(WebView webView){
        init(webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(WebView webView){
        //app提供给js的接口
        mWebView = webView;
        mJavascriptInterface = new JavascriptInterfaceImpl(this);
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

    }

    public void registerJsCallHandler(IXGToJavaHandler ixgBridgeHandler){
        mJavascriptInterface.addCall(ixgBridgeHandler);
    }

    public void registerInterceptor(IXGInterceptor javaApiInterceptor){
        mJavascriptInterface.addInterceptor(javaApiInterceptor);
    }

    public void setDefaultHandler(IJavascriptInterface javascriptInterface){
        mJavascriptInterface.setDefaultCall(javascriptInterface);
    }

    /**
     * 根据协议处理接受到的数据，将参数发送到JS
     */
    protected WebViewClient generateBridgeWebViewClient() {
        return new JSWebViewClient();
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
        mWebView.setWebViewClient(null);
        mWebView.setWebChromeClient(null);
        mWebView.destroy();
        mWebView = null;
        mJavascriptInterface.onDestroy();
    }

    public void setWebViewClient(JSWebViewClient webViewClient){
        mWebView.setWebViewClient(webViewClient);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient){
        mWebView.setWebChromeClient(webChromeClient);
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
