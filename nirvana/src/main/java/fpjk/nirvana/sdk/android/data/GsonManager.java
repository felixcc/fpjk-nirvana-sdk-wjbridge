package fpjk.nirvana.sdk.android.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

import fpjk.nirvana.sdk.android.logger.L;

/**
 * Created with Android Studio.
 * User: Felix
 * Date: 10/23/15
 * Time: 11:47 AM
 * QQ:74104
 * Email:lovejiuwei@gmail.com
 */
public class GsonManager {

    public static GsonManager newInstance() {
        return new GsonManager();
    }

    private GsonManager() {
    }

    public final <T> T json2Object(String json, Class<T> clazz) {
        T ob = null;
        try {
            ob = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            L.e("GsonManager->[%s]", e);
        }
        return ob;
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     *
     * @param object java对象
     */
    public final String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     *
     * @param jsonData JSON数据
     */
    public <T> List<T> toArrayObject(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<List<T>>() {
        });
    }

}
