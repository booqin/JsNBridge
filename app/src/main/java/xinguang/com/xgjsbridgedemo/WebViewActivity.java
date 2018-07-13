package xinguang.com.xgjsbridgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import io.reactivex.functions.Consumer;
import xinguang.com.xgjsbridge.XGNBridge;
import xinguang.com.xgjsbridgedemo.api.JavaApi;
import xinguang.com.xgjsbridgedemo.api.JsApis;
import xinguang.com.xgjsbridgedemo.bean.RespBean;
import xinguang.com.xgjsbridgedemo.bean.UserInfoBean;

/**
 * Created by vitozhang on 2018/7/2.
 */

public class WebViewActivity extends Activity{

    private WebView mWebView;
    private XGNBridge mJsBridge;

    private JsApis jsApiDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web_view);
        mJsBridge = new XGNBridge.XGNBridgeBuilder().registerToJavaHandler(JavaApi.UserInfoHandler).build(mWebView);
        jsApiDemo = mJsBridge.create(JsApis.class);
        mWebView.loadUrl("file:///android_asset/XGJSBridgeDemo.html");
        findViewById(R.id.bt_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.name = "error";
                jsApiDemo.notifyUserInfoChange(userInfoBean).subscribe(new Consumer<RespBean>() {
                    @Override
                    public void accept(RespBean s) throws Exception {
                        Toast.makeText(WebViewActivity.this, s.getName(), Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
//                jsApiDemo.notifyUserInfoChange(userInfoBean);
            }
        });
    }
}
