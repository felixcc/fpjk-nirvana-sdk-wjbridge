;(function (callback) {
    if (window.WebViewJavascriptBridge) {
        return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
        return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function () {
        document.documentElement.removeChild(WVJBIframe);
    }, 0);
}(function (bridge) {
    fpjk_loanBridge.bridge = bridge;
    fpjk_loanBridge.registerJSFunc();
}));
var fpjk_loanBridge = {
    bridge: null,
    registerJSFunc: function () {
        fpjk_loanBridge.bridge.registerHandler('fpjkBridgeCallJavaScript', function (data, responseCallback) {
            fpjk_loanBridge.jsFunc[data.opt](data.data, responseCallback);
            console.log(data);
        });
    },
    callNativeFunc: function (name, data, func) {
        fpjk_loanBridge.bridge.callHandler('fpjkBridgeCallNative', {'opt': name, 'data': data}, func);
    },
    jsFunc: { // Add bridge call JS funtions here
        testBridgeCallJS: function (data, responseCallback) {
            log('ObjC called fpjkBridgeCallJavaScript with', data);
            var responseData = {'Javascript Says': 'Success!'};
            log('JS responding with', responseData);
            responseCallback(responseData);
        }
    }
};
