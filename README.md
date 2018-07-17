## XGJSBridge
实现了Native端和Web端的相互通信的桥接类

## 初始化配置

在app的build.gradle中添加如下依赖

```xml
	implementation 'com.xgn.public:xg-jsbridge:0.2.0-SNAPSHOT'
```

## 接口的定义

### Web调用Native端
两端相互通信的接口实现不一样，Js调用Native端时，相当于Native作为一个服务器的RPC通信，需要实现接口__IXGToJavaHandler__，比如一个java暴露的名为getUserInfo的方法，js调用后可以获取到包含用户信息的json字符串参数，实现如下：

```java
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
```

### Native调用Web端
在调用Web端的方法时，其实和网络请求类似，Retrofit的接口实现方式也适用在该应用场景，以下是对Js中userInfoChange方法的调用。

```java
public interface JsApis {

    @JsName("userInfoChange")
    Observable<RespBean> notifyUserInfoChange(@JsParam UserInfoBean userInfoBean);

}
```

XGJSBridge库中，定义了两个注解：

- @JsName 对应的JS中的方法名
- @JsParam 需要传递到对应方法的参数，会自动转换为json格式

如果js有返回结果，通过Observable来获取，使用Rxjava的响应式编程可以方便的获取回调结果。如果不存在回调可以直接使用void。

## 接口的使用

注册提供给Web使用的方法，在实例化过程中调用registerToJavaHandler方法：

```java
	mJsBridge.registerToJavaHandler(JavaApi.UserInfoHandler)
```

调用Js方法与Retrofit中调用接口相似，实例化代理接口，然后就可以执行对应方法了:

```java
	jsApiDemo = mJsBridge.create(JsApis.class);

	jsApiDemo.notifyUserInfoChange(userInfoBean).subscribe(new Consumer<RespBean>() {
                    @Override
                    public void accept(RespBean s) throws Exception {
                        Toast.makeText(WebViewActivity.this, s.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
```
以上实现了两端的相互通信的功能。

## 自定义Client

__XGJSBridge__ 中提供了setWebViewClient和setWebChromeClient来替代原WebView的对于方法，因为协议的实现依赖client，所以如果有功能需要自定义Client请使用__XGJSBridge__ 中对应方法替代。

## 其它

- registerInterceptor
提供拦截器功能，所有的interceptor都优先与IXGToJavaHandler处理，即在调用提供给Web的Java接口时都会先执行interceptor，如果返回turn者不在继续执行Handler。优先级最高。

- setDefaultHandler
默认的Handler，在没有对于IXGToJavaHandler的时候会执行，优先级最低。

## TODO

- 目前只支持targetSDK 22的版本，需要解决高版本兼容的问题

- 协议依赖@JavascriptInterface，所以在4.2以下存在风险

