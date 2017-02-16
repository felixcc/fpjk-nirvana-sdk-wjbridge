package fpjk.nirvana.sdk.wjbridge.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 16:08
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class CoordinateInfo {

    @JSONField(name = "longitude")
    private Double longitude;

    @JSONField(name = "latitude")
    private Double latitude;

    /**
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "CoordinateInfo{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}