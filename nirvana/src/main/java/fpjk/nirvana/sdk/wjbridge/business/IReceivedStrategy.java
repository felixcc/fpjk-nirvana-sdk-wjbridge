package fpjk.nirvana.sdk.wjbridge.business;

/**
 * Summary: Created by FelixChen Created 2017-02-22 15:07 Mail:lovejiuwei@gmail.com QQ:74104
 */
public interface IReceivedStrategy {
    /**
     * 注销回调
     */
    void onReceivedLogout();

    /**
     * 页面加载失败
     */
    void onReceivedOnPageError();
}
