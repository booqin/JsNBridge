package xinguang.com.xgjsbridge;


import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IXGJSCallHandler;

/**
 * Created by vitozhang on 2018/7/2.
 */

public class UseInfoHandler implements IXGJSCallHandler {
    @Override
    public String getMethodName() {
        return "getUserInfo";
    }

    @Override
    public void handler(String data, CallBackFunction function) {
        function.onCallBack("{ 'name':'BoQin'}");
    }
//
//    @Override
//    public void handler(String data, CallBackFunction function) {
//        function.onCallBack("{ 'name':'BoQin'}");
//    }
}
