package xinguang.com.xgjsbridge.handler;

import android.util.Log;

import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;
import xinguang.com.xgjsbridge.interfaces.IParamHandler;
import xinguang.com.xgjsbridge.utils.JsonUtil;

/**
 * JsParam注释对应的数据处理
 * Created by vitozhang on 2018/7/10.
 */

public class JsParamHandler implements IParamHandler {

    private int mIndex;
    private String mCallBackId;

    public JsParamHandler(int index, String callBackId){
        mIndex = index;
        mCallBackId = callBackId;
    }

    @Override
    public void apply(IXGToJsHandler ixgToJsHandler, String name, Object[] params) {
        String paramsJson = JsonUtil.toJsonString(params[mIndex]);
        Log.d("BQ", "paramsJson:"+paramsJson);
        ixgToJsHandler.notify(name, paramsJson, mCallBackId);
    }
}
