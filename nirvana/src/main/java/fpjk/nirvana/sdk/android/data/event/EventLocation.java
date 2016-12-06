package fpjk.nirvana.sdk.android.data.event;

import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 16:50
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class EventLocation {
    private String locationInfo = "";
    private WJCallbacks wjCallbacks;

    public EventLocation() {
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public EventLocation setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
        return this;
    }

    public WJCallbacks getWjCallbacks() {
        return wjCallbacks;
    }

    public EventLocation setWjCallbacks(WJCallbacks wjCallbacks) {
        this.wjCallbacks = wjCallbacks;
        return this;
    }

    @Override
    public String toString() {
        return "EventLocation{" +
                "locationInfo='" + locationInfo + '\'' +
                ", wjCallbacks=" + wjCallbacks +
                '}';
    }
}
