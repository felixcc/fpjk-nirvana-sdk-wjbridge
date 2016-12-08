package fpjk.nirvana.sdk.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

public class OpenUrlDialog extends Dialog {
    private Context mContext;

    public OpenUrlDialog(@NonNull Context context) {
        this(context, R.style.popupDialog);
    }

    public OpenUrlDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.popupDialog);
        mContext = context;
        buildConfigs();
    }

    private void buildConfigs() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_open_url);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        WindowManager.LayoutParams lay = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();//decorView是window中的最顶层view，可以从window中获取到decorView
        view.getWindowVisibleDisplayFrame(rect);
//        lay.height = dm.heightPixels - rect.top;
//        lay.width = dm.widthPixels;
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }
}
