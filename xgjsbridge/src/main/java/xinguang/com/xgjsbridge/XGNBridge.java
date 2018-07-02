package xinguang.com.xgjsbridge;

import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.CallBackFunction;

import xinguang.com.xgjsbridge.interfaces.IXGBridgeHandler;

/**
 * Created by vitozhang on 2018/6/29.
 */

public class XGNBridge{

    public static final String XGJS = "XGJSCore";
    private WebView mWebView;

    public XGNBridge(WebView webView){
        mWebView = webView;
    }

    private void init(){
        //app提供给js的接口
        mWebView.addJavascriptInterface(new JsCallJava(), XGJS);

    }

    public void send(){
        //优化，在kitkat以上使用evaluate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript("", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        }else {
            mWebView.loadUrl("");
        }
    }


}
