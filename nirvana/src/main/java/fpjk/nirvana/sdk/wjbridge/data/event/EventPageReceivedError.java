package fpjk.nirvana.sdk.wjbridge.data.event;

/**
 * Summary: Created by FelixChen Created 2017-02-22 16:00 Mail:lovejiuwei@gmail.com QQ:74104
 */
public class EventPageReceivedError {
    private boolean pageReceivedError;
    private String failingUrl;

    public boolean isPageReceivedError() {
        return pageReceivedError;
    }

    public EventPageReceivedError setPageReceivedError(boolean pageReceivedError) {
        this.pageReceivedError = pageReceivedError;
        return this;
    }

    public String getFailingUrl() {
        return failingUrl;
    }

    public EventPageReceivedError setFailingUrl(String failingUrl) {
        this.failingUrl = failingUrl;
        return this;
    }
}
