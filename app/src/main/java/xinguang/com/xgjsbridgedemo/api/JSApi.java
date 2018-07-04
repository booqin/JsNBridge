package xinguang.com.xgjsbridgedemo.api;

import java.util.HashMap;

import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;
import xinguang.com.xgjsbridge.utils.JsonUtil;

/**
 * 用于Native调用的JS方法。
 * Created by Boqin on 2017/7/12.
 * Modified by Boqin
 *
 * @Version
 */
public class JSApi {

    private static final String NOTIFY_SUBSCRIBE_HANDLE = "javascript:XGJSBridge.subscribeHandler('%s','%s')";

    private IXGToJsHandler mJavaCallHandler;

    public JSApi(IXGToJsHandler javaCallHandler){
        mJavaCallHandler = javaCallHandler;
    }

    public void notifyTokenChange(){
        String jsCommand = String.format(NOTIFY_SUBSCRIBE_HANDLE, "userInfoChange", JsonUtil.toJsonString(getUserInfo()));
        mJavaCallHandler.send(jsCommand);
    }

    private Object getUserInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "error");
        return map;
    }

}
