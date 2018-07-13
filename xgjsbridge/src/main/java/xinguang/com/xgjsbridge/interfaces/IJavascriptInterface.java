package xinguang.com.xgjsbridge.interfaces;

/**
 * 提供Js直接调用的接口
 * Created by vitozhang on 2018/7/3.
 */

public interface IJavascriptInterface {

    String CALL_BACK_FROM_JS = "callBackFromJs";

    /**
     * 暴露给Js端直接调用的接口
     * @param event 事件名
     * @param paramsString 参数
     * @param callbackId 回调Id
     */
    void invokeHandler(String event, String paramsString, final String callbackId);
}
