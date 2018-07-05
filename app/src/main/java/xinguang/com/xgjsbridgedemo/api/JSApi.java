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

    private IXGToJsHandler mJavaCallHandler;

    public JSApi(IXGToJsHandler javaCallHandler){
        mJavaCallHandler = javaCallHandler;
    }

    public void notifyTokenChange(){
        mJavaCallHandler.notify("userInfoChange", JsonUtil.toJsonString(getUserInfo()));
    }

    private Object getUserInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "error");
        return map;
    }

}
