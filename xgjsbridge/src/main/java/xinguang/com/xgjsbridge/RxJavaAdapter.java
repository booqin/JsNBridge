package xinguang.com.xgjsbridge;

import java.util.Map;

import xinguang.com.xgjsbridge.interfaces.CallBackFunction;
import xinguang.com.xgjsbridge.interfaces.ICallAdapter;

/**
 * Created by vitozhang on 2018/7/11.
 */

public class RxJavaAdapter implements ICallAdapter {

    public Map<?, ? extends CallBackFunction> map;

    public RxJavaAdapter(Map<?, ? extends CallBackFunction> callbacks){
        map = callbacks;
    }

    @Override
    public Object adapt(final ServiceMethod serviceMethod) {
        //创建Observable
        if (serviceMethod.getResultType() == void.class) {
            serviceMethod.apply();
            return void.class;
        }else {
            return new JsObservable(serviceMethod);
        }

    }
}
