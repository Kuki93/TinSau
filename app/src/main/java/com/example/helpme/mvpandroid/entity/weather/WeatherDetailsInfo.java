/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.weather;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Auto-generated: 2018-01-29 20:21:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class WeatherDetailsInfo implements Parcelable {

    private String publishTime;
    private List<Weather3HoursDetailsInfos> weather3HoursDetailsInfos;
    public void setPublishTime(String publishTime) {
         this.publishTime = publishTime;
     }
     public String getPublishTime() {
         return publishTime;
     }

    public void setWeather3HoursDetailsInfos(List<Weather3HoursDetailsInfos> weather3HoursDetailsInfos) {
         this.weather3HoursDetailsInfos = weather3HoursDetailsInfos;
     }
     public List<Weather3HoursDetailsInfos> getWeather3HoursDetailsInfos() {
         return weather3HoursDetailsInfos;
     }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publishTime);
        dest.writeTypedList(this.weather3HoursDetailsInfos);
    }
    
    public WeatherDetailsInfo() {
    }
    
    protected WeatherDetailsInfo(Parcel in) {
        this.publishTime = in.readString();
        this.weather3HoursDetailsInfos = in.createTypedArrayList(Weather3HoursDetailsInfos.CREATOR);
    }
    
    public static final Creator<WeatherDetailsInfo> CREATOR = new Creator<WeatherDetailsInfo>() {
        @Override
        public WeatherDetailsInfo createFromParcel(Parcel source) {
            return new WeatherDetailsInfo(source);
        }
        
        @Override
        public WeatherDetailsInfo[] newArray(int size) {
            return new WeatherDetailsInfo[size];
        }
    };
}