package fpjk.nirvana.sdk.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import fpjk.nirvana.sdk.android.business.DataTransferEntity;
import fpjk.nirvana.sdk.android.presenter.WJBridgeWebView;
import fpjk.nirvana.sdk.wjbridge.R;

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
    private Context mContext;
    private DataTransferEntity mDataTransferEntity;
    private WJBridgeWebView mWJBridgeWebView;
    private TextView mTxtOpenUrlTitle;
    private Button mBtnOpenUrlBack;

    public OpenUrlDialog(@NonNull Context context, DataTransferEntity dataTransferEntity, int width, int height) {
        super(context, R.style.popupDialog);
        mDataTransferEntity = dataTransferEntity;
        buildConfigs(width, height);
    }

    private void buildConfigs(int width, int height) {
        if (getWindow() == null) {
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_open_url);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        WindowManager.LayoutParams lay = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = height;
        lay.width = width;

        mWJBridgeWebView = (WJBridgeWebView) findViewById(R.id.wjBridgeWebView);
        mTxtOpenUrlTitle = (TextView) findViewById(R.id.txtOpenUrlTitle);
        mBtnOpenUrlBack = (Button) findViewById(R.id.btnOpenUrlBack);
        mBtnOpenUrlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnOpenUrlBack) {
            dismiss();
        }
    }
}
