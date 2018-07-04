package xinguang.com.xgjsbridge.api;


import xinguang.com.xgjsbridge.interfaces.IJavascriptInterface;

/**
 * Created by vitozhang on 2018/7/3.
 */

public class DefaultJavaApi implements IJavascriptInterface {

    @Override
    public void invokeHandler(String event, String paramsString, String callbackId) {
        //do something when no Java event matched
    }
}
