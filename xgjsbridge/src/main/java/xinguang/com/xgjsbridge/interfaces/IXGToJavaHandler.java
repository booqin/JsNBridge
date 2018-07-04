package xinguang.com.xgjsbridge.interfaces;

/**
 * Bridge处理接口，实现Js对Java的调用接口
 * Created by Boqin on 2017/7/11.
 * Modified by Boqin
 *
 * @Version
 */
public interface IXGToJavaHandler {
    /**
     * 接口名，提供给Js端调用
     * @return 对应的接口名
     */
    String getMethodName();

    /**
     * 处理Js的调用，并可执行回调
     * @param data Js传递过来的数据
     * @param function 回调函数
     */
    void handler(String data, CallBackFunction function);

}
