package xinguang.com.xgjsbridge.handler;

import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;
import xinguang.com.xgjsbridge.interfaces.IParamHandler;
import xinguang.com.xgjsbridge.utils.JsonUtil;

/**
 * JsParam注释对应的数据处理
 * Created by vitozhang on 2018/7/10.
 */

public class JsParamHandler implements IParamHandler {

    private int mIndex;

    public JsParamHandler(int index){
        mIndex = index;
    }

    @Override
    public void apply(IXGToJsHandler ixgToJsHandler, String name, Object[] params) {
        String paramsJson = JsonUtil.toJsonString(params[mIndex]);
        ixgToJsHandler.notify(name, paramsJson);
    }
}
