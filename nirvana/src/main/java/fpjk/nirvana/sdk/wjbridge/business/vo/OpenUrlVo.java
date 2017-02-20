package fpjk.nirvana.sdk.wjbridge.business.vo;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-20 16:26
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */
public class OpenUrlVo {
    private String title = "";
    private boolean isShownBackButton;

    public String getTitle() {
        return title;
    }

    public OpenUrlVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isShownBackButton() {
        return isShownBackButton;
    }

    public OpenUrlVo setShownBackButton(boolean shownBackButton) {
        isShownBackButton = shownBackButton;
        return this;
    }

    @Override
    public String toString() {
        return "OpenUrlVo{" +
                ", title='" + title + '\'' +
                ", isShownBackButton=" + isShownBackButton +
                '}';
    }
}
