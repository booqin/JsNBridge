package xinguang.com.xgjsbridge;

import android.webkit.JavascriptInterface;


import java.util.HashMap;
import java.util.Map;

import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IXGJSCallHandler;
import xinguang.com.xgjsbridge.interfaces.IXGJavaCallHandler;
import xinguang.com.xgjsbridge.utils.JsonUtil;

/**
 * Created by vitozhang on 2018/6/29.
 */

public class BaseJavascriptInterface {

    private static final String INVOKE_CALLBACK_HANDLE = "javascript:XGJSBridge.invokeCallbackHandler('%s','%s')";

    private Map<String, IXGJSCallHandler> mNativeCallMap;

    private IXGJavaCallHandler mJavaCallHandler;

    public BaseJavascriptInterface(IXGJavaCallHandler javaCallHandler){
        mNativeCallMap = new HashMap<>();
        mJavaCallHandler = javaCallHandler;
    }

    /**
     * 设置暴露的方法
     */
    public void addCall(IXGJSCallHandler ixgBridgeHandler){
        if (ixgBridgeHandler == null) {
            return;
        }
        mNativeCallMap.put(ixgBridgeHandler.getMethodName(), ixgBridgeHandler);
    }

    @JavascriptInterface
    public String invokeHandler(String event, String paramsString, final String callbackId){
        if (mNativeCallMap.get(event)!=null) {
            mNativeCallMap.get(event).handler(paramsString, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    String jsCommand = String.format(INVOKE_CALLBACK_HANDLE, callbackId, JsonUtil.toJsonString(data));
                    mJavaCallHandler.send(jsCommand);
                }

            });
        }
        return "";
    }

}
