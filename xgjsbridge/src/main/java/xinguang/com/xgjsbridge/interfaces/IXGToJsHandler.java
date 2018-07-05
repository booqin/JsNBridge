package xinguang.com.xgjsbridge.interfaces;

/**
 * 发送数据到js
 * Created by vitozhang on 2018/7/2.
 */

public interface IXGToJsHandler {
    /**
     * 调用Js端函数
     * @param command Js命令
     */
    void send(String command);

    /**
     * 向Js端发送通知
     * @param event 事件名，对应Js端的subscribe事件
     * @param jsonParams 参数，json字符串格式
     */
    void notify(String event, String jsonParams);

    /**
     * 执行回调
     * @param callBackId 回调的id
     * @param jsonParams 参数，json字符串格式
     */
    void callBack(String callBackId, String jsonParams);
}
