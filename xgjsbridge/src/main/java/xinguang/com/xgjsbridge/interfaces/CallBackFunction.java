package xinguang.com.xgjsbridge.interfaces;

/**
 * 回调函数
 * Created by vitozhang on 2018/7/2.
 */
public interface CallBackFunction {
    /**
     * 回调方法
     * @param data 传递到Js端的数据
     */
    void onCallBack(String data);
}
