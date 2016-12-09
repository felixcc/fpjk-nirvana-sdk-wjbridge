package fpjk.nirvana.sdk.android.business;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 16:09
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class LocationEntity {

    @JSONField(name = "mapType")
    private String mapType;

    @JSONField(name = "coordinateInfo")
    private CoordinateInfo coordinateInfo;

    @JSONField(name = "addressInfo")
    private AddressInfo addressInfo;

    @JSONField(name = "errorCode")
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public LocationEntity setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * @return The mapType
     */
    public String getMapType() {
        return mapType;
    }

    /**
     * @param mapType The mapType
     */
    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    /**
     * @return The coordinateInfo
     */
    public CoordinateInfo getCoordinateInfo() {
        return coordinateInfo;
    }

    /**
     * @param coordinateInfo The coordinateInfo
     */
    public void setCoordinateInfo(CoordinateInfo coordinateInfo) {
        this.coordinateInfo = coordinateInfo;
    }

    /**
     * @return The addressInfo
     */
    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    /**
     * @param addressInfo The addressInfo
     */
    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "mapType='" + mapType + '\'' +
                ", coordinateInfo=" + coordinateInfo +
                ", addressInfo=" + addressInfo +
                ", errorCode=" + errorCode +
                '}';
    }
}
