(function() {

    if (window.XGJSBridge) return;

    var isIosWebView = false;
    var userAgent = window.navigator.userAgent;
    var isAndroidWebView = (userAgent.indexOf('Android') > -1) || (userAgent.indexOf('Adr') > -1);
    isIosWebView = !isAndroidWebView;

    var invokeCallbacks = {};
    var invokeCallbackId = 0;
    var subscribeCallbacks = {};

    /**
    * Web调用App功能
    * @params event [String] 功能名称
    * @params params [Object] 参数 maybe null
    * @callback [Function] maybe null or undefined
    */
    function invoke(event, params, callback) {
      var paramsString = isIosWebView ? (params || {}) : JSON.stringify(params || {});
      var callbackId = ++invokeCallbackId;
      if(callback && typeof callback == 'function'){
            invokeCallbacks[callbackId] = callback;
      }
      _invokeHandler(event, paramsString, callbackId);
    }


    /**
    * Web调用App
    * @params event [String] 事件名称
    * @params paramsString [Android-string,IOS-Object] 参数
    * @params callbackId [String] 回调id
    */
    function _invokeHandler(event, paramsString, callbackId) {
        if (isIosWebView) {
            window.webkit.messageHandlers.invokeHandler.postMessage({
                event: event,
                params: paramsString,
                callbackId: callbackId,
            });
        } else {
            XGJSCore.invokeHandler(event, paramsString, callbackId);
        }
    }

    /**
    * App回调Web
    * @params callbackId [String] 回调id
    * @params result [String] 回调结果，maybe null or undefined
    */
    function invokeCallbackHandler(callbackId, result) {
        if(!callbackId) return;
        var callback = invokeCallbacks[callbackId];
        if(!callback) return;
        if (callback) {
            if(typeof result === 'string'){
                callback(JSON.parse(result));
            } else {
                callback(result);
            }
        }
        delete invokeCallbacks[callbackId];
    }



    /**
    * 添加Web对App的监听
    * @params event [String] 监听事件名
    * @params callback [Function] 收到监听的回调函数
    */
    function subscribe(event, callback) {
        if((event && typeof event === 'string') && callback && (typeof callback === 'function')){
            subscribeCallbacks[event] = callback;
        }
    }


    /**
    * Web收到App的消息
    * @params event [String] 事件名
    * @params result [String] 参数
    */
    function subscribeHandler(event, result) {
        var callback = subscribeCallbacks[event];
        if (callback) {
            if(result && typeof result === 'string'){
                callback(JSON.parse(result));
            } else {
                callback(result);
            }
        }
    }

    window.XGJSBridge = {
        //Web调用App功能,Web使用
        invoke: invoke,
        //App回调Web,App使用
        invokeCallbackHandler: invokeCallbackHandler,
        //Web订阅App事件,Web使用
        subscribe: subscribe,
        //App发送事件给Web,App使用
        : subscribeHandler
    };
})();