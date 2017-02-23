package fpjk.nirvana.sdk.wjbridge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Summary: Created by FelixChen Created 2017-02-23 17:08 Mail:lovejiuwei@gmail.com QQ:74104
 */
public class Titlebar extends RelativeLayout {
    private Context mContext;

    private ImageView mIvTitleBarBack;

    private TextView mIvTitleBarTitle;

    public Titlebar(Context context) {
        this(context, null);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        buildConfigs();
    }

    private void buildConfigs() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fpjk_titlebar, this);
        mIvTitleBarBack = (ImageView) v.findViewById(R.id.ivTitleBarBack);
        mIvTitleBarTitle = (TextView) v.findViewById(R.id.ivTitleBarTitle);
    }

    public void onBack(View.OnClickListener o) {
        mIvTitleBarBack.setOnClickListener(o);
    }

    public void setTitle(String title) {
        mIvTitleBarTitle.setText(title);
    }

    public String getTitle() {
        return mIvTitleBarTitle.getText().toString();
    }

    public void showBackButton() {
        mIvTitleBarBack.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        mIvTitleBarBack.setVisibility(View.GONE);
    }
}
