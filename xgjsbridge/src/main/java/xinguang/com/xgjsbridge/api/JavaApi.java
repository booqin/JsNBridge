package xinguang.com.xgjsbridge.api;

import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IXGJSCallHandler;

/**
 * 提供给JS使用到java代码，根据前端要求添加java协议代码
 * Created by Boqin on 2017/7/12.
 * Modified by Boqin
 *
 * @Version
 */
public class JavaApi {

    public static IXGJSCallHandler UserInfoHandler = new IXGJSCallHandler() {
        @Override
        public String getMethodName() {
            return "getUserInfo";
        }

        @Override
        public void handler(String data, CallBackFunction function) {
            function.onCallBack("{ 'name':'BoQin'}");
        }
    };
}
