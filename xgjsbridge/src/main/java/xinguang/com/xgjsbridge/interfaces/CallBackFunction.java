package xinguang.com.xgjsbridge.interfaces;

/**
 * 回调函数
 * Created by vitozhang on 2018/7/2.
 */
public interface CallBackFunction {
    /**
     * 回调方法
     * @param jsonData 传递到Js端的数据 json字符串
     */
    void onCallBack(String jsonData);
}
