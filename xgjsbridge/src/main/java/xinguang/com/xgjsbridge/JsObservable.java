package xinguang.com.xgjsbridge;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vitozhang on 2018/7/12.
 */

public class JsObservable<T> extends Observable<T> {

    private ServiceMethod mServiceMethod;

    public JsObservable(ServiceMethod serviceMethod){

        mServiceMethod = serviceMethod;

    }

    @Override
    protected void subscribeActual(Observer observer) {

        JsDisposable jsDisposable = new JsDisposable<T>(mServiceMethod, observer);

        observer.onSubscribe(jsDisposable);

        mServiceMethod.apply();

    }


    private static class JsDisposable<T> implements Disposable, ServiceMethod.JsCallBack{

        private ServiceMethod mServiceMethod;
        private Observer<T> mObserver;
        private boolean isDispose;

        public JsDisposable(ServiceMethod serviceMethod, Observer<T> observable){

            mServiceMethod = serviceMethod;
            mServiceMethod.setJsCallBack(this);
            mObserver = observable;
            isDispose = false;
        }

        @Override
        public void dispose() {
            isDispose = true;
            mObserver = null;
            mServiceMethod.destroy();
        }

        @Override
        public boolean isDisposed() {
            return isDispose;
        }


        @Override
        public void onCallBack(String jsonData, Type type) {
            if (!isDispose) {
                if (mObserver!=null) {
                    Gson gson = new Gson();
                    TypeAdapter<?> typeAdapter = gson.getAdapter(TypeToken.get(type));
                    GsonCovert<T> gsonCovert = new GsonCovert(typeAdapter);
                    try {
                        //发布数据
                        mObserver.onNext(gsonCovert.getAdapter().fromJson(jsonData));
                    } catch (Exception e) {
                        mObserver.onError(e);
                    }

                }
            }


        }

        private class GsonCovert<R>{

            private TypeAdapter<R> adapter;

            public GsonCovert(TypeAdapter<R> typeAdapter){
                adapter = typeAdapter;
            }

            public TypeAdapter<R> getAdapter(){
                return adapter;
            }
        }
    }



}
