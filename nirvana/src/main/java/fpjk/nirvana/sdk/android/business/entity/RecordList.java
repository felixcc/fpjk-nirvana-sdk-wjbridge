package fpjk.nirvana.sdk.android.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 13/12/2016
 * Time: 20:00
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class RecordList {
    @JSONField(name = "phoneNum")
    private String phoneNum;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "date")
    private Integer date;

    @JSONField(name = "duration")
    private Integer duration;

    @JSONField(name = "type")
    private Integer type;

    /**
     * @return The phoneNum
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phoneNum The phoneNum
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The date
     */
    public Integer getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(Integer date) {
        this.date = date;
    }

    /**
     * @return The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @return The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecordList{" +
                "phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", type=" + type +
                '}';
    }
}
