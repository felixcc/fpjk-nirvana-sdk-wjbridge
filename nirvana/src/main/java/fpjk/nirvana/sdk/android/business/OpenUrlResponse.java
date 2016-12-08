package fpjk.nirvana.sdk.android.business;

/**
 * Summary:
 * Created by Felix
 * Date: 07/12/2016
 * Time: 11:30
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class OpenUrlResponse {
    //0=手动，1是自动
    private int success = 0;

    public void switchManualProcessingMode() {
        success = 0;
    }

    public void switchAutoProcessingMode() {
        success = 1;
    }

    @Override
    public String toString() {
        return "OpenUrlResponse{" +
                "success=" + success +
                '}';
    }
}
