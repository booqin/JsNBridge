package xinguang.com.xgjsbridgedemo.api;


import java.util.HashMap;

import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.IXGToJavaHandler;
import xinguang.com.xgjsbridge.utils.JsonUtil;

/**
 * 提供给JS使用到java代码，根据前端要求添加java协议代码
 * Created by Boqin on 2017/7/12.
 * Modified by Boqin
 *
 * @Version
 */
public abstract class JavaApi {

    public static IXGToJavaHandler UserInfoHandler = new IXGToJavaHandler() {

        @Override
        public String getMethodName() {
            return "getUserInfo";
        }

        @Override
        public void handler(String data, CallBackFunction function) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", "BQ");
            function.onCallBack(JsonUtil.toJsonString(map));
        }
    };

}
