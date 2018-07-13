package xinguang.com.xgjsbridge.api;

import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IXGToJavaHandler;

import static xinguang.com.xgjsbridge.interfaces.IJavascriptInterface.CALL_BACK_FROM_JS;

/**
 * 用于调用Js后的回调
 * Created by vitozhang on 2018/7/11.
 */

public class DefaultJavaApi {


    public static IXGToJavaHandler CallBack = new IXGToJavaHandler() {
        @Override
        public String getMethodName() {
            return CALL_BACK_FROM_JS;
        }

        @Override
        public void handler(String data, CallBackFunction function) {
            //直接将参数返回给回调接口
            function.onCallBack(data);
        }
    };

}
