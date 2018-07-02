package xinguang.com.xgjsbridge.interfaces;

/**
 * Bridge处理接口
 * Created by Boqin on 2017/7/11.
 * Modified by Boqin
 *
 * @Version
 */
public interface IXGJSCallHandler{

    String getMethodName();

    void handler(String data, CallBackFunction function);

}
