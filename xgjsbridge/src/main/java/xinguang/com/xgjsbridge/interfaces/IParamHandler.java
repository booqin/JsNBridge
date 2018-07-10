package xinguang.com.xgjsbridge.interfaces;

/**
 * 参数处理接口
 * Created by vitozhang on 2018/7/10.
 */

public interface IParamHandler {

    void apply(IXGToJsHandler ixgToJsHandler, String name, Object[] params);
}
