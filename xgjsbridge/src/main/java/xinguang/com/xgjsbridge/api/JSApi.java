package xinguang.com.xgjsbridge.api;

/**
 * Created by vitozhang on 2018/7/2.
 */

import xinguang.com.xgjsbridge.XGNBridge;
import xinguang.com.xgjsbridge.interfaces.IXGJavaCallHandler;
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

    private IXGJavaCallHandler mJavaCallHandler;

    public JSApi(IXGJavaCallHandler javaCallHandler){
        mJavaCallHandler = javaCallHandler;
    }

    public void notifyTokenChange(){
        String jsCommand = String.format(NOTIFY_SUBSCRIBE_HANDLE, "userInfoChange", JsonUtil.toJsonString("{ 'name':'Vito'}"));
        mJavaCallHandler.send(jsCommand);
    }

}
