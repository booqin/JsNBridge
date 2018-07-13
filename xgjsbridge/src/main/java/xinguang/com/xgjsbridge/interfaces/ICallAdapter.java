package xinguang.com.xgjsbridge.interfaces;

import xinguang.com.xgjsbridge.ServiceMethod;

/**
 * Created by vitozhang on 2018/7/11.
 */

public interface ICallAdapter<T> {

    T adapt(ServiceMethod serviceMethod);

}
