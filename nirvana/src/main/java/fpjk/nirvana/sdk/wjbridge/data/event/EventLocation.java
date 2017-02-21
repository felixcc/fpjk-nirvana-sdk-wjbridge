package fpjk.nirvana.sdk.wjbridge.data.event;

import fpjk.nirvana.sdk.wjbridge.business.entity.LocationEntity;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;

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
    private LocationEntity locationEntity;
    private WJCallbacks wjCallbacks;

    public EventLocation() {
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public EventLocation setLocationEntity(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
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
                "locationEntity=" + locationEntity +
                ", wjCallbacks=" + wjCallbacks +
                '}';
    }
}
