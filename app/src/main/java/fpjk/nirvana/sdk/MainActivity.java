package fpjk.nirvana.sdk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import fpjk.nirvana.sdk.android.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.android.business.FpjkView;

public class MainActivity extends Activity implements View.OnClickListener {

//    WJBridgeWebView mWJBridgeWebView;

    FpjkView mFpjkView;

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goBack).setOnClickListener(this);
        findViewById(R.id.forward).setOnClickListener(this);
        findViewById(R.id.reload).setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.address);
        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        final String url = mEditText.getText().toString();
        FpjkWJSDKMgr.initWebView(mFpjkView, this, url);

        getCallDetails(this);
    }

    private String getCallDetails(Context context) {
        StringBuffer sb = new StringBuffer();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor managedCursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        return sb.toString();
    }

    @Override
    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.goBack:
//                if (mWJBridgeWebView.canGoBack()) {
//                    mWJBridgeWebView.goBack();
//                }
//                break;
//            case R.id.forward:
//                if (mWJBridgeWebView.canGoForward()) {
//                    mWJBridgeWebView.goForward();
//                }
//                break;
//            case R.id.reload:
////                final String url = mEditText.getText().toString();
////                mWJBridgeWebView.loadUrl(url);
//                mWJBridgeWebView.reload();
//                break;
//            default:
//                break;
//        }
    }
}
