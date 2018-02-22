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
public class Pm25 implements Parcelable {

    private String advice;
    private String aqi;
    private int citycount;
    private int cityrank;
    private String co;
    private String color;
    private String level;
    private String no2;
    private String o3;
    private String pm10;
    private String pm25;
    private String quality;
    private String so2;
    private String timestamp;
    private String upDateTime;
    public void setAdvice(String advice) {
         this.advice = advice;
     }
     public String getAdvice() {
         return advice;
     }

    public void setAqi(String aqi) {
         this.aqi = aqi;
     }
     public String getAqi() {
         return aqi;
     }

    public void setCitycount(int citycount) {
         this.citycount = citycount;
     }
     public int getCitycount() {
         return citycount;
     }

    public void setCityrank(int cityrank) {
         this.cityrank = cityrank;
     }
     public int getCityrank() {
         return cityrank;
     }

    public void setCo(String co) {
         this.co = co;
     }
     public String getCo() {
         return co;
     }

    public void setColor(String color) {
         this.color = color;
     }
     public String getColor() {
         return color;
     }

    public void setLevel(String level) {
         this.level = level;
     }
     public String getLevel() {
         return level;
     }

    public void setNo2(String no2) {
         this.no2 = no2;
     }
     public String getNo2() {
         return no2;
     }

    public void setO3(String o3) {
         this.o3 = o3;
     }
     public String getO3() {
         return o3;
     }

    public void setPm10(String pm10) {
         this.pm10 = pm10;
     }
     public String getPm10() {
         return pm10;
     }

    public void setPm25(String pm25) {
         this.pm25 = pm25;
     }
     public String getPm25() {
         return pm25;
     }

    public void setQuality(String quality) {
         this.quality = quality;
     }
     public String getQuality() {
         return quality;
     }

    public void setSo2(String so2) {
         this.so2 = so2;
     }
     public String getSo2() {
         return so2;
     }

    public void setTimestamp(String timestamp) {
         this.timestamp = timestamp;
     }
     public String getTimestamp() {
         return timestamp;
     }

    public void setUpDateTime(String upDateTime) {
         this.upDateTime = upDateTime;
     }
     public String getUpDateTime() {
         return upDateTime;
     }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advice);
        dest.writeString(this.aqi);
        dest.writeInt(this.citycount);
        dest.writeInt(this.cityrank);
        dest.writeString(this.co);
        dest.writeString(this.color);
        dest.writeString(this.level);
        dest.writeString(this.no2);
        dest.writeString(this.o3);
        dest.writeString(this.pm10);
        dest.writeString(this.pm25);
        dest.writeString(this.quality);
        dest.writeString(this.so2);
        dest.writeString(this.timestamp);
        dest.writeString(this.upDateTime);
    }
    
    public Pm25() {
    }
    
    protected Pm25(Parcel in) {
        this.advice = in.readString();
        this.aqi = in.readString();
        this.citycount = in.readInt();
        this.cityrank = in.readInt();
        this.co = in.readString();
        this.color = in.readString();
        this.level = in.readString();
        this.no2 = in.readString();
        this.o3 = in.readString();
        this.pm10 = in.readString();
        this.pm25 = in.readString();
        this.quality = in.readString();
        this.so2 = in.readString();
        this.timestamp = in.readString();
        this.upDateTime = in.readString();
    }
    
    public static final Creator<Pm25> CREATOR = new Creator<Pm25>() {
        @Override
        public Pm25 createFromParcel(Parcel source) {
            return new Pm25(source);
        }
        
        @Override
        public Pm25[] newArray(int size) {
            return new Pm25[size];
        }
    };
}