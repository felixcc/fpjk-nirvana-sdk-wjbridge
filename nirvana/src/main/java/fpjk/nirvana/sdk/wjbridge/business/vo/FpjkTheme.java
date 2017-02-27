package fpjk.nirvana.sdk.wjbridge.business.vo;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Summary: Created by FelixChen Created 2017-02-24 17:32 Mail:lovejiuwei@gmail.com QQ:74104
 */
public class FpjkTheme {
    private int titleBarContentColor = 0;//标题颜色
    private int titleBarBackgroundColorResId = 0;//标题栏背景
    private int titleBarBackBtnResId = 0;//标题栏返回按钮

    public int getTitleBarContentColor() {
        return titleBarContentColor;
    }

    public FpjkTheme setTitleBarContentColor(int titleBarContentColor) {
        this.titleBarContentColor = titleBarContentColor;
        return this;
    }

    public int getTitleBarBackgroundColorResId() {
        return titleBarBackgroundColorResId;
    }

    public FpjkTheme setTitleBarBackgroundColorResId(@ColorRes int titleBarBackgroundColorResId) {
        this.titleBarBackgroundColorResId = titleBarBackgroundColorResId;
        return this;
    }

    public int getTitleBarBackBtnResId() {
        return titleBarBackBtnResId;
    }

    public FpjkTheme setTitleBarBackBtnResId(@DrawableRes int titleBarBackBtnResId) {
        this.titleBarBackBtnResId = titleBarBackBtnResId;
        return this;
    }

    @Override
    public String toString() {
        return "FpjkTheme{" +
                "titleBarContentColor=" + titleBarContentColor +
                ", titleBarBackgroundColorResId=" + titleBarBackgroundColorResId +
                ", titleBarBackBtnResId=" + titleBarBackBtnResId +
                '}';
    }
}
