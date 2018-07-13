package xinguang.com.xgjsbridge;

import android.util.Log;

import com.google.gson.TypeAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import xinguang.com.xgjsbridge.annotations.JsParam;
import xinguang.com.xgjsbridge.handler.JsParamHandler;
import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.ICallAdapter;
import xinguang.com.xgjsbridge.interfaces.IParamHandler;
import xinguang.com.xgjsbridge.interfaces.IXGToJsHandler;

/**
 * 以方法为处理单位
 * 构建需要的参数
 * Created by vitozhang on 2018/7/11.
 */

public class ServiceMethod{

    private IParamHandler mParamHandler;

    private Map<String, ? super CallBackFunction> mCallBackMap;

    private JsCallBack mJsCallBack;

    private IXGToJsHandler mIxgToJsHandler;
    private String mName;
    private Object[] mArgs;

    private String mCallBackId;

    private Type mResultType;

    private Type mResponseType;



    public ServiceMethod(Method method, Map<String, CallBackFunction> map,IXGToJsHandler ixgToJsHandler, String name, Object[] args){
        //XGNBridge.this, name, args
        mCallBackMap = map;
        mIxgToJsHandler = ixgToJsHandler;
        mName = name;
        mArgs = args;

        mResultType = method.getGenericReturnType();

        if (mResultType instanceof ParameterizedType) {
            initResponseType((ParameterizedType) mResultType);
        }

        mCallBackId = getCallBackId();

        mParamHandler = getHandler(method, mCallBackId);

    }

    public Type getResultType() {
        return mResultType;
    }

    public <T> T adapt(ICallAdapter<T> t){
        return t.adapt(this);
    }

    /**
     * 发送请求
     */
    public void apply(){
        if (mParamHandler!=null) {
            mParamHandler.apply(mIxgToJsHandler, mName, mArgs);
        }
    }

    public void setJsCallBack(JsCallBack jsCallBack){
        mJsCallBack = jsCallBack;
    }

    public void destroy(){
        mCallBackMap.remove(mCallBackId);
    }


    private void initResponseType(ParameterizedType resultType){
        if (resultType.getActualTypeArguments()!=null && resultType.getActualTypeArguments().length>0) {
            mResponseType = resultType.getActualTypeArguments()[0];
        }else {
            throw new IllegalArgumentException("type arguments can not be null");
        }
    }

    /**
     * 获取回调Id，可以使用其它更好的算法作为唯一key
     */
    private String getCallBackId(){
        return Long.toString(System.currentTimeMillis());
    }

    /**
     * 获取参数handler，解析注解
     * @param method 接口方法
     * @param callBackId 回调的Id
     * @return
     */
    private IParamHandler getHandler(Method method, final String callBackId) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        int counts = paramAnnotations.length;
        for (int count = 0; count < counts; count++) {
            for (Annotation annotation : paramAnnotations[count]) {
                if (annotation instanceof JsParam) {
                    Log.d("BQ", "callBack:"+callBackId);
                    Log.d("BQ", "callBack:"+ mResultType);
                    if (mResultType != void.class) {
                        Log.d("BQ", "mResponseType:"+ mResponseType);
                        mCallBackMap.put(callBackId, new CallBackFunction() {
                            @Override
                            public void onCallBack(String jsonData) {
                                if (mJsCallBack!=null) {

                                    mJsCallBack.onCallBack(jsonData, mResponseType);
                                }
                                mCallBackMap.remove(callBackId);
                            }
                        });
                    }
                    return new JsParamHandler(count, callBackId);
                }
            }
        }

        return null;
    }


    public interface JsCallBack{

        void onCallBack(String jsonData, Type type);

    }

}
