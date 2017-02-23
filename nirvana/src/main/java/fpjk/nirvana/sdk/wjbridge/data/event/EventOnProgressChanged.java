package fpjk.nirvana.sdk.wjbridge.data.event;

/**
 * Summary: Created by FelixChen Created 2017-02-23 11:41 Mail:lovejiuwei@gmail.com QQ:74104
 */
public class EventOnProgressChanged {
    private int newProgress = 0;

    public int getNewProgress() {
        return newProgress;
    }

    public EventOnProgressChanged setNewProgress(int newProgress) {
        this.newProgress = newProgress;
        return this;
    }
}
