package fpjk.nirvana.sdk.wjbridge.business;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;
import fpjk.nirvana.sdk.wjbridge.R;

/**
 * Summary:
 * Created by Felix
 * Date: 13/12/2016
 * Time: 17:08
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class OpenUrlView extends RelativeLayout {
    private WJBridgeWebView openUrlWJBridgeWebView;
    private TextView txtOpenUrlTitle;
    private Button btnOpenUrlBack;

    public OpenUrlView(Context context) {
        super(context);
        build(context);
    }

    public OpenUrlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(context);
    }

    public OpenUrlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build(context);
    }

    private void build(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_open_url, null);

        openUrlWJBridgeWebView = (WJBridgeWebView) v.findViewById(R.id.openUrlWJBridgeWebView);
        txtOpenUrlTitle = (TextView) v.findViewById(R.id.txtOpenUrlTitle);
        btnOpenUrlBack = (Button) v.findViewById(R.id.btnOpenUrlBack);

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1, -1);
        addView(v, rl);
    }

    public void setTitle(String title) {
        if (!StringUtils.isEmpty(title)) {
            txtOpenUrlTitle.setText(title);
        }
    }

    public void onBack(View.OnClickListener o) {
        btnOpenUrlBack.setOnClickListener(o);
    }

    public void loadUrl(String url) {
        openUrlWJBridgeWebView.loadUrl(url);
    }

    public void recycle() {
        openUrlWJBridgeWebView.loadUrl("");
        openUrlWJBridgeWebView.reload();
    }
}
