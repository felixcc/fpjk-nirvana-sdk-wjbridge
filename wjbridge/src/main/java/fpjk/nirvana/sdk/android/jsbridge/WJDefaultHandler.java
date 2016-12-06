package fpjk.nirvana.sdk.android.jsbridge;

class WJDefaultHandler implements WJBridgeHandler {

    @Override
    public void handler(String data, WJCallbacks callbacks) {
        if (callbacks != null) {
            callbacks.onCallback("WJBridge default handler response data!");
        }
    }
}
