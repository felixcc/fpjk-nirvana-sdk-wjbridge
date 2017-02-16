package fpjk.nirvana.sdk.wjbridge.data.event;

/**
 * Summary:
 * Created by FelixChen
 * Created 2016-12-10 17:46
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class EventPageFinished {
    private String currentUrl = "";

    public String getCurrentUrl() {
        return currentUrl;
    }

    public EventPageFinished setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
        return this;
    }

    @Override
    public String toString() {
        return "EventPageFinished{" +
                "currentUrl='" + currentUrl + '\'' +
                '}';
    }
}
