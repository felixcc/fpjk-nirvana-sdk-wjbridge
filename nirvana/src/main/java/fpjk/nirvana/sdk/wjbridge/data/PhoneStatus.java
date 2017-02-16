package fpjk.nirvana.sdk.wjbridge.data;

import org.apache.commons.lang.StringEscapeUtils;

import fpjk.nirvana.sdk.wjbridge.business.entity.ErrorCodeEntity;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;

/**
 * Summary:
 * Created by Felix
 * Date: 14/12/2016
 * Time: 15:17
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public abstract class PhoneStatus {

    protected void buildReturnMsg(WJCallbacks wjCallbacks, int errorCode) {
        ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        errorCodeEntity.setErrorCode(errorCode);
        String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
        wjCallbacks.onCallback(callBack);
    }

    /**
     * 对存入数据库的字段进行特殊字符处理
     *
     * @param sqlcolumn 需要存入数据的字符串
     * @return 格式化的字符串
     */
    protected String escapeSql(String sqlcolumn) {
        return StringEscapeUtils.escapeSql(sqlcolumn);
    }
}
