package fpjk.nirvana.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import fpjk.nirvana.sdk.android.business.DataTransferEntity;
import fpjk.nirvana.sdk.android.business.OpenUrlResponse;
import fpjk.nirvana.sdk.android.data.GsonManager;
import fpjk.nirvana.sdk.android.presenter.WJBridgeWebView;
import fpjk.nirvana.sdk.wjbridge.R;

/**
 * Summary:
 * Created by Felix
 * Date: 06/12/2016
 * Time: 19:59
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class OpenUrlActivity extends Activity {
    public static final String EXTRA_SHOW_ME = "EXTRA_SHOW_ME";
    private DataTransferEntity mDataTransferEntity;
    private WJBridgeWebView mWJBridgeWebView;
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_url);
        mDataTransferEntity = getIntent().getParcelableExtra(EXTRA_SHOW_ME);

        if (null == mDataTransferEntity) {
            finish();
        }

        mTextView = (TextView) findViewById(R.id.txtOpenUrlTitle);
        mWJBridgeWebView = (WJBridgeWebView) findViewById(R.id.wjBridgeWebView);

        mWJBridgeWebView.loadUrl(mDataTransferEntity.getUrl());
        mTextView.setText(mDataTransferEntity.getTitle());
    }

    public void finishMySelf(View view) {
        OpenUrlResponse openUrlResponse = new OpenUrlResponse();
        openUrlResponse.switchManualProcessingMode();
        String reponses = GsonManager.newInstance().toJSONString(openUrlResponse);
//        mWJCallbacks.onCallback(reponses);
        this.finish();
    }
}
