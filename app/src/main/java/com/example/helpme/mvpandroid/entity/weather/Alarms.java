/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.weather;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auto-generated: 2018-01-29 20:21:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Alarms implements Parcelable {

    private String alarmContent;
    private String alarmDesc;
    private String alarmId;
    private String alarmLevelNo;
    private String alarmLevelNoDesc;
    private String alarmType;
    private String alarmTypeDesc;
    private String precaution;
    private String publishTime;
    
    public String getAlarmContent() {
        return alarmContent;
    }
    
    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }
    
    public String getAlarmDesc() {
        return alarmDesc;
    }
    
    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }
    
    public String getAlarmId() {
        return alarmId;
    }
    
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
    
    public String getAlarmLevelNo() {
        return alarmLevelNo;
    }
    
    public void setAlarmLevelNo(String alarmLevelNo) {
        this.alarmLevelNo = alarmLevelNo;
    }
    
    public String getAlarmLevelNoDesc() {
        return alarmLevelNoDesc;
    }
    
    public void setAlarmLevelNoDesc(String alarmLevelNoDesc) {
        this.alarmLevelNoDesc = alarmLevelNoDesc;
    }
    
    public String getAlarmType() {
        return alarmType;
    }
    
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
    
    public String getAlarmTypeDesc() {
        return alarmTypeDesc;
    }
    
    public void setAlarmTypeDesc(String alarmTypeDesc) {
        this.alarmTypeDesc = alarmTypeDesc;
    }
    
    public String getPrecaution() {
        return precaution;
    }
    
    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }
    
    public String getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alarmContent);
        dest.writeString(this.alarmDesc);
        dest.writeString(this.alarmId);
        dest.writeString(this.alarmLevelNo);
        dest.writeString(this.alarmLevelNoDesc);
        dest.writeString(this.alarmType);
        dest.writeString(this.alarmTypeDesc);
        dest.writeString(this.precaution);
        dest.writeString(this.publishTime);
    }
    
    public Alarms() {
    }
    
    protected Alarms(Parcel in) {
        this.alarmContent = in.readString();
        this.alarmDesc = in.readString();
        this.alarmId = in.readString();
        this.alarmLevelNo = in.readString();
        this.alarmLevelNoDesc = in.readString();
        this.alarmType = in.readString();
        this.alarmTypeDesc = in.readString();
        this.precaution = in.readString();
        this.publishTime = in.readString();
    }
    
    public static final Creator<Alarms> CREATOR = new Creator<Alarms>() {
        @Override
        public Alarms createFromParcel(Parcel source) {
            return new Alarms(source);
        }
        
        @Override
        public Alarms[] newArray(int size) {
            return new Alarms[size];
        }
    };
}