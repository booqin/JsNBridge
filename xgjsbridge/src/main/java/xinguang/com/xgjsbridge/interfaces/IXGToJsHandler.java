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
}
