package xinguang.com.xgjsbridgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import xinguang.com.xgjsbridge.XGNBridge;
import xinguang.com.xgjsbridgedemo.api.JSApi;
import xinguang.com.xgjsbridgedemo.api.JavaApi;

/**
 * Created by vitozhang on 2018/7/2.
 */

public class WebViewActivity extends Activity{

    private WebView mWebView;
    private XGNBridge mJsBridge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web_view);
        mJsBridge = new XGNBridge.XGNBridgeBuilder().registerToJavaHandler(JavaApi.UserInfoHandler).build(mWebView);
        mWebView.loadUrl("file:///android_asset/XGJSBridgeDemo.html");
        findViewById(R.id.bt_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSApi(mJsBridge).notifyTokenChange();
            }
        });
    }
}
