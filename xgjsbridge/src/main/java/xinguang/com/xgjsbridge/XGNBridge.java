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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import xinguang.com.xgjsbridge.WebClient.JSWebViewClient;
import xinguang.com.xgjsbridge.annotations.JsName;
import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IJavascriptInterface;
import xinguang.com.xgjsbridge.interfaces.IXGInterceptor;
import xinguang.com.xgjsbridge.interfaces.IXGToJavaHandler;
import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;


/**
 * Created by vitozhang on 2018/6/29.
 */

public class XGNBridge implements IXGToJsHandler {

    private static final String XGJS = "XGJSCore";
    private static final String NOTIFY_SUBSCRIBE_HANDLE = "javascript:XGJSBridge.subscribeHandler('%s','%s')";
    private static final String NOTIFY_SUBSCRIBE_WITH_CALLBACK_HANDLE = "javascript:XGJSBridge.subscribeHandler('%s','%s', '%s')";
    private static final String INVOKE_CALLBACK_HANDLE = "javascript:XGJSBridge.invokeCallbackHandler('%s','%s')";


    private WebView mWebView;

    private JavascriptInterfaceImpl mJavascriptInterface;

    private Map<String, CallBackFunction> mCallBacks;

    private XGNBridge(){
        mCallBacks = new HashMap<>();
        mJavascriptInterface = new JavascriptInterfaceImpl(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(WebView webView){
        //app提供给js的接口
        mWebView = webView;

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

        registerInterceptor(new CallBackInterceptor(mCallBacks));

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

    @Override
    public void notify(String event, String jsonParams) {
        String jsCommand = String.format(NOTIFY_SUBSCRIBE_HANDLE, event, jsonParams);
        send(jsCommand);
    }

    @Override
    public void notify(String event, String jsonParams, String callBackId) {
        String jsCommand = String.format(NOTIFY_SUBSCRIBE_WITH_CALLBACK_HANDLE, event, jsonParams, callBackId);
        send(jsCommand);
    }

    @Override
    public void callBack(String callBackId, String jsonParams) {
        String jsCommand = String.format(INVOKE_CALLBACK_HANDLE, callBackId, jsonParams);
        send(jsCommand);
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

    public <T> T create(final Class<T> api){

        if (!api.isInterface()) {
            throw new IllegalArgumentException("api must be a Interface");
        }
        //获取参数
        //1.获取注解

        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class<?>[]{api}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = "";
                //获取方法的注解
                JsName jsName = method.getAnnotation(JsName.class);
                if (jsName!=null) {
                    name = jsName.value();
                }
                ServiceMethod serviceMethod = new ServiceMethod(method, mCallBacks, XGNBridge.this, name, args);
                RxJavaAdapter adapter = new RxJavaAdapter(mCallBacks);
                return serviceMethod.adapt(adapter);
            }


        });
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

    public static class XGNBridgeBuilder{

        private XGNBridge xgnBridge;

        public XGNBridgeBuilder(){
            xgnBridge = new XGNBridge();
        }

        public XGNBridgeBuilder registerToJavaHandler(IXGToJavaHandler ixgBridgeHandler){
            xgnBridge.registerJsCallHandler(ixgBridgeHandler);
            return this;
        }

        public XGNBridgeBuilder registerInterceptor(IXGInterceptor javaApiInterceptor){
            xgnBridge.registerInterceptor(javaApiInterceptor);
            return this;
        }

        public XGNBridgeBuilder setDefaultHandler(IJavascriptInterface javascriptInterface){
            xgnBridge.setDefaultHandler(javascriptInterface);
            return this;
        }

        public XGNBridge build(WebView webView){
            xgnBridge.init(webView);
            return xgnBridge;
        }

    }
}
