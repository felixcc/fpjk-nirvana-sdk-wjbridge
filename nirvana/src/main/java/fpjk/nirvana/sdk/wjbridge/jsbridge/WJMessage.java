package fpjk.nirvana.sdk.wjbridge.jsbridge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fpjk.nirvana.sdk.wjbridge.logger.L;

public class WJMessage {

    private static final String KEY_CALLBACK_ID = "callbackId";
    private static final String KEY_HANDLER_NAME = "handlerName";
    private static final String KEY_RESPONSE_ID = "responseId";
    private static final String KEY_RESPONSE_DATA = "responseData";
    private static final String KEY_DATA = "data";

    String callbackId;
    String handlerName;
    String responseId;
    String responseData;
    String data;

    @Override
    public String toString() {
        return "WJMessage{" +
                "callbackId='" + callbackId + '\'' +
                ", handlerName='" + handlerName + '\'' +
                ", responseId='" + responseId + '\'' +
                ", responseData='" + responseData + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WJMessage.KEY_CALLBACK_ID, callbackId);
            jsonObject.put(WJMessage.KEY_DATA, data);
            jsonObject.put(WJMessage.KEY_HANDLER_NAME, handlerName);
            jsonObject.put(WJMessage.KEY_RESPONSE_DATA, responseData);
            jsonObject.put(WJMessage.KEY_RESPONSE_ID, responseId);
            return jsonObject.toString();
        } catch (Exception e) {
            L.e("WJMessage to json error![%s]", new Object[]{toString()}, e);
        }
        return null;
    }

    private static void jsonObj2Msg(WJMessage message, JSONObject jsonObject) {
        if (message == null || jsonObject == null) {
            return;
        }
        message.callbackId = jsonObject.optString(WJMessage.KEY_CALLBACK_ID, null);
        message.data = jsonObject.optString(WJMessage.KEY_DATA, null);
        message.handlerName = jsonObject.optString(WJMessage.KEY_HANDLER_NAME, null);
        message.responseData = jsonObject.optString(WJMessage.KEY_RESPONSE_DATA, null);
        message.responseId = jsonObject.optString(WJMessage.KEY_RESPONSE_ID, null);
    }

    public static WJMessage ofJson(String json) {
        WJMessage message = new WJMessage();
        try {
            JSONObject jsonObject = new JSONObject(json);
            WJMessage.jsonObj2Msg(message, jsonObject);
        } catch (Exception e) {
            L.e("WJMessage of json error![json=%s]", new Object[]{json}, e);
        }
        return message;
    }

    static List<WJMessage> ofJsonArray(String json) {
        List<WJMessage> messages = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0, length = jsonArray.length(); i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WJMessage message = new WJMessage();
                WJMessage.jsonObj2Msg(message, jsonObject);
                messages.add(message);
            }
        } catch (Exception e) {
            L.e("WJMessage of jsonArray error![json=%s]", new Object[]{json}, e);
        }
        return messages;
    }
}
