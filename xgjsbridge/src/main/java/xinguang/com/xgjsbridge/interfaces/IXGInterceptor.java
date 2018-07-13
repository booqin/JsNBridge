package xinguang.com.xgjsbridge.interfaces;

/**
 * Created by vitozhang on 2018/7/3.
 */

public interface IXGInterceptor {

    boolean intercept(String event, String callbackId, String params, IXGToJsHandler javaCallHandler);

}
