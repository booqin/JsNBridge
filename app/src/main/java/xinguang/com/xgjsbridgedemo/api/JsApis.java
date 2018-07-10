package xinguang.com.xgjsbridgedemo.api;

import xinguang.com.xgjsbridge.annotations.JsName;
import xinguang.com.xgjsbridge.annotations.JsParam;
import xinguang.com.xgjsbridgedemo.bean.UserInfoBean;

/**
 * Created by vitozhang on 2018/7/10.
 */

public interface JsApis {

    @JsName("userInfoChange")
    void notifyUserInfoChange(@JsParam UserInfoBean userInfoBean);

}
