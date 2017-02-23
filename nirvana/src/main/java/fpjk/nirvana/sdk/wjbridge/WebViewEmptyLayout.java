package fpjk.nirvana.sdk.wjbridge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fpjk.nirvana.sdk.wjbridge.logger.L;

public class WebViewEmptyLayout extends LinearLayout implements View.OnClickListener {
    private boolean clickEnable = true;
    private final Context context;
    private OnClickListener listener;

    private ImageView mIvLackOfLogoF;
    private TextView mTvWireSetting;
    private TextView mTvErrorMsg;

    public WebViewEmptyLayout(Context context) {
        super(context);
        this.context = context;
        initialization();
    }

    public WebViewEmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialization();
    }

    private void initialization() {
        View view = LayoutInflater.from(context).inflate(R.layout.fpjk_layout_lack_of, this);
        mIvLackOfLogoF = (ImageView) view.findViewById(R.id.ivLackOfLogoF);
        mTvWireSetting = (TextView) view.findViewById(R.id.tvWireSetting);
        mTvErrorMsg = (TextView) view.findViewById(R.id.tvErrorMsg);

        mTvWireSetting.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvWireSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    L.e("", e);
                }
            }
        });

        setOnClickListener(this);
    }

    private long clickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - clickTime < 200) {
            return;
        }
        clickTime = currentTime;
        if (clickEnable) {
            if (listener != null)
                listener.onClick(v);
        }
    }

    public void setOnRefreshClick(OnClickListener o) {
        this.listener = o;
    }

    public void dismiss() {
        setVisibility(View.GONE);
    }

    public void display() {
        setVisibility(View.VISIBLE);
    }
}
