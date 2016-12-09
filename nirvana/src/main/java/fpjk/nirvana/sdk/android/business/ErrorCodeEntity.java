package fpjk.nirvana.sdk.android.business;

/**
 * Summary:
 * Created by Felix
 * Date: 09/12/2016
 * Time: 16:35
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class ErrorCodeEntity {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public ErrorCodeEntity setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorCodeEntity{" +
                "errorCode=" + errorCode +
                '}';
    }
}
