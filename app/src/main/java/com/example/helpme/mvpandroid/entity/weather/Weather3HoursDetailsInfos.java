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
public class Weather3HoursDetailsInfos implements Parcelable {

    private String endTime;
    private String highestTemperature;
    private String img;
    private String isRainFall;
    private String lowerestTemperature;
    private String precipitation;
    private String startTime;
    private String wd;
    private String weather;
    private String ws;
    
    public void setEndTime(String endTime) {
         this.endTime = endTime;
     }
     public String getEndTime() {
         return endTime;
     }

    public void setHighestTemperature(String highestTemperature) {
         this.highestTemperature = highestTemperature;
     }
     public String getHighestTemperature() {
         return highestTemperature;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setIsRainFall(String isRainFall) {
         this.isRainFall = isRainFall;
     }
     public String getIsRainFall() {
         return isRainFall;
     }

    public void setLowerestTemperature(String lowerestTemperature) {
         this.lowerestTemperature = lowerestTemperature;
     }
     public String getLowerestTemperature() {
         return lowerestTemperature;
     }

    public void setPrecipitation(String precipitation) {
         this.precipitation = precipitation;
     }
     public String getPrecipitation() {
         return precipitation;
     }

    public void setStartTime(String startTime) {
         this.startTime = startTime;
     }
     public String getStartTime() {
         return startTime;
     }

    public void setWd(String wd) {
         this.wd = wd;
     }
     public String getWd() {
         return wd;
     }

    public void setWeather(String weather) {
         this.weather = weather;
     }
     public String getWeather() {
         return weather;
     }

    public void setWs(String ws) {
         this.ws = ws;
     }
     public String getWs() {
         return ws;
     }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endTime);
        dest.writeString(this.highestTemperature);
        dest.writeString(this.img);
        dest.writeString(this.isRainFall);
        dest.writeString(this.lowerestTemperature);
        dest.writeString(this.precipitation);
        dest.writeString(this.startTime);
        dest.writeString(this.wd);
        dest.writeString(this.weather);
        dest.writeString(this.ws);
    }
    
    public Weather3HoursDetailsInfos() {
    }
    
    protected Weather3HoursDetailsInfos(Parcel in) {
        this.endTime = in.readString();
        this.highestTemperature = in.readString();
        this.img = in.readString();
        this.isRainFall = in.readString();
        this.lowerestTemperature = in.readString();
        this.precipitation = in.readString();
        this.startTime = in.readString();
        this.wd = in.readString();
        this.weather = in.readString();
        this.ws = in.readString();
    }
    
    public static final Creator<Weather3HoursDetailsInfos> CREATOR = new Creator<Weather3HoursDetailsInfos>() {
        @Override
        public Weather3HoursDetailsInfos createFromParcel(Parcel source) {
            return new Weather3HoursDetailsInfos(source);
        }
        
        @Override
        public Weather3HoursDetailsInfos[] newArray(int size) {
            return new Weather3HoursDetailsInfos[size];
        }
    };
}