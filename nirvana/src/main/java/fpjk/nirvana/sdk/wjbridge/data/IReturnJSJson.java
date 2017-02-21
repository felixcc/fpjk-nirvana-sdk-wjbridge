package fpjk.nirvana.sdk.wjbridge.data;

import org.apache.commons.lang.StringEscapeUtils;

import fpjk.nirvana.sdk.wjbridge.business.entity.CommonEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.ErrorCodeEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.OuterPackageEntity;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-21 15:36
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public abstract class IReturnJSJson {
    public abstract String imei();

    /**
     * 封装共有 JSON-RESPONSE
     */
    public String buildReturnCorrectJSJson(Object o) {
        OuterPackageEntity outerPackageEntity = new OuterPackageEntity();
        CommonEntity commonEntity = new CommonEntity();
        commonEntity.setPid(imei());
        commonEntity.setVersion(BuildPackageConfigs.VERSION_NAME);

        outerPackageEntity.setCommon(commonEntity);
        outerPackageEntity.setBody(o);

        return GsonMgr.get().toJSONString(outerPackageEntity);
    }

    /**
     * 获取短信、通讯录、联系人、定位错误的返回Body
     */
    protected void buildErrorJSJson(int errorCode, WJCallbacks wjCallbacks) {
        OuterPackageEntity outerPackageEntity = new OuterPackageEntity();
        CommonEntity commonEntity = new CommonEntity();
        commonEntity.setPid(imei());
        commonEntity.setVersion(BuildPackageConfigs.VERSION_NAME);

        ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        errorCodeEntity.setErrorCode(errorCode);

        outerPackageEntity.setCommon(commonEntity);
        outerPackageEntity.setBody(errorCodeEntity);

        String callBackJson = GsonMgr.get().toJSONString(outerPackageEntity);
        wjCallbacks.onCallback(callBackJson);
    }

    /**
     * 对存入数据库的字段进行特殊字符处理
     *
     * @param sqlcolumn 需要存入数据的字符串
     * @return 格式化的字符串
     */
    public String escapeSql(String sqlcolumn) {
        return StringEscapeUtils.escapeSql(sqlcolumn);
    }
}
