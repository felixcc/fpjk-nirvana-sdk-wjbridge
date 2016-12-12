package fpjk.nirvana.sdk.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import fpjk.nirvana.sdk.android.business.DataTransferEntity;
import fpjk.nirvana.sdk.android.business.OpenUrlResponse;
import fpjk.nirvana.sdk.android.data.GsonMgr;
import fpjk.nirvana.sdk.android.data.RxBus;
import fpjk.nirvana.sdk.android.data.event.EventPageFinished;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.logger.L;
import fpjk.nirvana.sdk.android.presenter.WJBridgeWebView;
import fpjk.nirvana.sdk.wjbridge.R;
import rx.functions.Action1;

/**
 * Summary:
 * Created by Felix
 * Date: 07/12/2016
 * Time: 16:12
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class OpenUrlDialog extends Dialog implements View.OnClickListener {
    private DataTransferEntity mDataTransferEntity;
    private WJBridgeWebView mWJBridgeWebView;
    private WJCallbacks mWjCallbacks;
    private TextView mTxtOpenUrlTitle;
    private Button mBtnOpenUrlBack;

    public OpenUrlDialog(@NonNull Context context,
                         DataTransferEntity dataTransferEntity,
                         WJCallbacks wjCallbacks,
                         int width,
                         int height) {
        super(context, R.style.popupDialog);
        setContentView(R.layout.view_open_url);

        buildConfigs(context, width, height);

        mWjCallbacks = wjCallbacks;
        mDataTransferEntity = dataTransferEntity;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_open_url);
//
//        mWJBridgeWebView = (WJBridgeWebView) findViewById(R.id.wjBridgeWebView);
//        mTxtOpenUrlTitle = (TextView) findViewById(R.id.txtOpenUrlTitle);
//        mBtnOpenUrlBack = (Button) findViewById(R.id.btnOpenUrlBack);
//        mBtnOpenUrlBack.setOnClickListener(this);
//
//        mTxtOpenUrlTitle.setText(mDataTransferEntity.getTitle());
//        mWJBridgeWebView.loadUrl(mDataTransferEntity.getUrl());
//
//        processingEvent();
//    }

    private void buildConfigs(Context context, int width, int height) {
        if (getWindow() == null) {
            return;
        }

        setCanceledOnTouchOutside(false);
        setCancelable(false);
        WindowManager.LayoutParams lay = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = height;
        lay.width = width;
    }

    private void processingEvent() {
        RxBus.get().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof EventPageFinished) {
                }
                L.d("processingEvent[%s]", o);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnOpenUrlBack) {
            OpenUrlResponse openUrlResponse = new OpenUrlResponse();
            openUrlResponse.switchManualProcessingMode();
            mWjCallbacks.onCallback(GsonMgr.get().toJSONString(openUrlResponse));
            dismiss();
        }
    }
}
