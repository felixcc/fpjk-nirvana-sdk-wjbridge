package fpjk.nirvana.sdk.wjbridge.jsbridge;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-16 12:04
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */
public class WJBridgeConfigs {
    public static final String a = ";(function() {\n" +
            "\tif (window.WebViewJavascriptBridge) {\n" +
            "            return;\n" +
            "\t}\n" +
            "\n" +
            "\tvar messagingIframe;\n" +
            "\tvar sendMessageQueue = [];\n" +
            "\tvar receiveMessageQueue = [];\n" +
            "\tvar messageHandlers = {};\n" +
            "\n" +
            "\tvar CUSTOM_PROTOCOL_SCHEME = 'wvjbscheme';\n" +
            "\tvar QUEUE_HAS_MESSAGE = '__QUEUE_MESSAGE__/';\n" +
            "\n" +
            "\tvar responseCallbacks = {};\n" +
            "\tvar uniqueId = 1;\n" +
            "\n" +
            "\tfunction _createQueueReadyIframe(doc) {\n" +
            "\t\tmessagingIframe = doc.createElement('iframe');\n" +
            "\t\tmessagingIframe.style.display = 'none';\n" +
            "\t\tdoc.documentElement.appendChild(messagingIframe);\n" +
            "\t}\n" +
            "\n" +
            "\tfunction _defaultHandler(data, responseCallback) {\n" +
            "\t\talert(\"defaultHandler:\" + data);\n" +
            "\t\tresponseCallback(JSON.parse(\"defaultHandler : callback\"));\n" +
            "\t}\n" +
            "\t//set default messageHandler\n" +
            "\tfunction init() {\n" +
            "\t\tWebViewJavascriptBridge._messageHandler = _defaultHandler;\n" +
            "\t\tvar receivedMessages = receiveMessageQueue;\n" +
            "\t\treceiveMessageQueue = null;\n" +
            "\t\tfor (var i = 0; i < receivedMessages.length; i++) {\n" +
            "\t\t\t_dispatchMessageFromNative(receivedMessages[i]);\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\tfunction send(data, responseCallback) {\n" +
            "\t\t_doSend({\n" +
            "\t\t\tdata: data\n" +
            "\t\t}, responseCallback);\n" +
            "\t}\n" +
            "\n" +
            "\tfunction registerHandler(handlerName, handler) {\n" +
            "\t\tmessageHandlers[handlerName] = handler;\n" +
            "\t}\n" +
            "\n" +
            "\tfunction callHandler(handlerName, data, responseCallback) {\n" +
            "\t\t_doSend({\n" +
            "\t\t\thandlerName: handlerName,\n" +
            "\t\t\tdata: data\n" +
            "\t\t}, responseCallback);\n" +
            "\t}\n" +
            "\n" +
            "\t//sendMessage add message, 触发native处理 sendMessage\n" +
            "\tfunction _doSend(message, responseCallback) {\n" +
            "\t\tif (responseCallback) {\n" +
            "\t\t\tvar callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();\n" +
            "\t\t\tresponseCallbacks[callbackId] = responseCallback;\n" +
            "\t\t\tmessage.callbackId = callbackId;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\tsendMessageQueue.push(message);\n" +
            "\t\tmessagingIframe.src = CUSTOM_PROTOCOL_SCHEME + '://' + QUEUE_HAS_MESSAGE;\n" +
            "\t}\n" +
            "\n" +
            "\t// 提供给native调用,该函数作用:获取sendMessageQueue返回给native,由于android不能直接获取返回的内容,所以使用url shouldOverrideUrlLoading 的方式返回内容\n" +
            "\tfunction _fetchQueue() {\n" +
            "\t\tvar messageQueueString = JSON.stringify(sendMessageQueue);\n" +
            "\t\tsendMessageQueue = [];\n" +
            "\t\t//android can't read directly the return data, so we can reload iframe src to communicate with java\n" +
            "\t\tmessagingIframe.src = CUSTOM_PROTOCOL_SCHEME + '://return/_fetchQueue/' + encodeURIComponent(messageQueueString);\n" +
            "\t}\n" +
            "\n" +
            "\t//提供给native使用,\n" +
            "\tfunction _dispatchMessageFromNative(messageJSON) {\n" +
            "\t\tsetTimeout(function() {\n" +
            "\t\t\tvar message = JSON.parse(messageJSON);\n" +
            "//\t\t\tconsole.log(\"嗷嗷嗷啊：\" + message.responseData + \"---\" +JSON.parse(message.responseData));\n" +
            "\t\t\tvar responseCallback;\n" +
            "\t\t\t//java call finished, now need to call js callback function\n" +
            "\t\t\tif (message.responseId) {\n" +
            "\t\t\t\tresponseCallback = responseCallbacks[message.responseId];\n" +
            "\t\t\t\tif (!responseCallback) {\n" +
            "\t\t\t\t\treturn;\n" +
            "\t\t\t\t}\n" +
            "\t\t\t\tresponseCallback(JSON.parse(message.responseData));\n" +
            "\t\t\t\tdelete responseCallbacks[message.responseId];\n" +
            "\t\t\t} else {\n" +
            "\t\t\t\t//直接发送\n" +
            "\t\t\t\tif (message.callbackId) {\n" +
            "\t\t\t\t\tvar callbackResponseId = message.callbackId;\n" +
            "\t\t\t\t\tresponseCallback = function(responseData) {\n" +
            "\t\t\t\t\t\t_doSend({\n" +
            "\t\t\t\t\t\t\tresponseId: callbackResponseId,\n" +
            "\t\t\t\t\t\t\tresponseData: JSON.parse(responseData)\n" +
            "\t\t\t\t\t\t});\n" +
            "\t\t\t\t\t};\n" +
            "\t\t\t\t}\n" +
            "\n" +
            "\t\t\t\tvar handler = WebViewJavascriptBridge._messageHandler;\n" +
            "\t\t\t\tif (message.handlerName) {\n" +
            "\t\t\t\t\thandler = messageHandlers[message.handlerName];\n" +
            "\t\t\t\t}\n" +
            "\t\t\t\t//查找指定handler\n" +
            "\t\t\t\ttry {\n" +
            "\t\t\t\t\thandler(message.data, responseCallback);\n" +
            "\t\t\t\t} catch (exception) {\n" +
            "\t\t\t\t\tif (typeof console != 'undefined') {\n" +
            "\t\t\t\t\t\tconsole.log(\"WebViewJavascriptBridge: WARNING: javascript handler threw.\", message, exception);\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t});\n" +
            "\t}\n" +
            "\n" +
            "\t//提供给native调用,receiveMessageQueue 在会在页面加载完后赋值为null,所以\n" +
            "\tfunction _handleMessageFromNative(messageJSON) {\n" +
            "//\t\tconsole.log(\"提供给native调用,receiveMessageQueue 在会在页面加载完后赋值为null,所以========\"+messageJSON);\n" +
            "\t\tif (receiveMessageQueue && receiveMessageQueue.length > 0) {\n" +
            "\t\t\treceiveMessageQueue.push(messageJSON);\n" +
            "\t\t} else {\n" +
            "\t\t\t_dispatchMessageFromNative(messageJSON);\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\tfunction setDefaultHandler(messageHandler){\n" +
            "\t\tWebViewJavascriptBridge._messageHandler = messageHandler;\n" +
            "\t}\n" +
            "\n" +
            "\tvar WebViewJavascriptBridge = window.WebViewJavascriptBridge = {\n" +
            "\t\tinit: init,\n" +
            "\t\tsend: send,\n" +
            "\t\tsetDefaultHandler: setDefaultHandler,\n" +
            "\t\tregisterHandler: registerHandler,\n" +
            "\t\tcallHandler: callHandler,\n" +
            "\t\t_fetchQueue: _fetchQueue,\n" +
            "\t\t_handleMessageFromNative: _handleMessageFromNative\n" +
            "\t};\n" +
            "\n" +
            "\tvar doc = document;\n" +
            "\t_createQueueReadyIframe(doc);\n" +
            "\tvar readyEvent = doc.createEvent('Events');\n" +
            "\treadyEvent.initEvent('WebViewJavascriptBridgeReady');\n" +
            "\treadyEvent.bridge = WebViewJavascriptBridge;\n" +
            "\tdoc.dispatchEvent(readyEvent);\n" +
            "\n" +
            "\tsetTimeout(_callWVJBCallbacks, 0);\n" +
            "\tfunction _callWVJBCallbacks() {\n" +
            "\t\tWebViewJavascriptBridge.init();\n" +
            "\t\tvar callbacks = window.WVJBCallbacks;\n" +
            "\t\tdelete window.WVJBCallbacks;\n" +
            "\t\tfor (var i=0; i<callbacks.length; i++) {\n" +
            "\t\t\tcallbacks[i](WebViewJavascriptBridge);\n" +
            "\t\t}\n" +
            "\t}\n" +
            "})();";
}
