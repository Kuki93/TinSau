package com.example.helpme.mvpandroid.entity.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Created by helpme on 2018/1/27.
 * @Description
 */
public class CityInfo implements Parcelable {
    
    public final static int TYPE_LOCATION_ON = 0;
    public final static int TYPE_LOCATION_OFF = 1;
    public final static int TYPE_NORMAL = 2;
    
    private int type;
    private String city;
    private long cityId;
    private String weather;
    private String temperature;
    
    public CityInfo(String city, int type) {
        this.city = city;
        this.type = type;
    }
    
    public CityInfo(String city, long cityId) {
        this.type = TYPE_NORMAL;
        this.city = city;
        this.cityId = cityId;
    }
    
    public CityInfo(int type, String city, long cityId, String weather, String temperature) {
        this.type = type;
        this.city = city;
        this.cityId = cityId;
        this.weather = weather;
        this.temperature = temperature;
    }
    
    public CityInfo(String city, long cityId, String weather, String temperature) {
        this.type = TYPE_NORMAL;
        this.city = city;
        this.cityId = cityId;
        this.weather = weather;
        this.temperature = temperature;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public long getCityId() {
        return cityId;
    }
    
    public void setCityId(long cityId) {
        this.cityId = cityId;
    }
    
    public String getWeather() {
        return weather;
    }
    
    public void setWeather(String weather) {
        this.weather = weather;
    }
    
    public String getTemperature() {
        return temperature;
    }
    
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.city);
        dest.writeLong(this.cityId);
        dest.writeString(this.weather);
        dest.writeString(this.temperature);
    }
    
    protected CityInfo(Parcel in) {
        this.type = in.readInt();
        this.city = in.readString();
        this.cityId = in.readLong();
        this.weather = in.readString();
        this.temperature = in.readString();
    }
    
    public static final Creator<CityInfo> CREATOR = new Creator<CityInfo>() {
        @Override
        public CityInfo createFromParcel(Parcel source) {
            return new CityInfo(source);
        }
        
        @Override
        public CityInfo[] newArray(int size) {
            return new CityInfo[size];
        }
    };
}
