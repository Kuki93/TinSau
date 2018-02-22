package com.example.helpme.mvpandroid.entity.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Created by helpme on 2018/1/29.
 * @Description
 */
public class WeatherCityCache implements Parcelable {
    
    private boolean isOpenLocation;
    private Value extraValue;
    private List<Value> cityValues;
    
    public WeatherCityCache(boolean isOpenLocation) {
        this.isOpenLocation = isOpenLocation;
    }
    
    public boolean isOpenLocation() {
        return isOpenLocation;
    }
    
    public void setOpenLocation(boolean openLocation) {
        isOpenLocation = openLocation;
    }
    
    public Value getExtraValue() {
        return extraValue;
    }
    
    public void setExtraValue(Value extraValue) {
        this.extraValue = extraValue;
    }
    
    public List<Value> getCityValues() {
        return cityValues;
    }
    
    public void setCityValues(List<Value> cityValues) {
        this.cityValues = cityValues;
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isOpenLocation ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.extraValue, flags);
        dest.writeTypedList(this.cityValues);
    }
    
    protected WeatherCityCache(Parcel in) {
        this.isOpenLocation = in.readByte() != 0;
        this.extraValue = in.readParcelable(Value.class.getClassLoader());
        this.cityValues = in.createTypedArrayList(Value.CREATOR);
    }
    
    public static final Creator<WeatherCityCache> CREATOR = new Creator<WeatherCityCache>() {
        @Override
        public WeatherCityCache createFromParcel(Parcel source) {
            return new WeatherCityCache(source);
        }
        
        @Override
        public WeatherCityCache[] newArray(int size) {
            return new WeatherCityCache[size];
        }
    };
}
