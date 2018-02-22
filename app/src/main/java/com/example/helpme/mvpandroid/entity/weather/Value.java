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
public class Value implements Parcelable {

    private List<Alarms> alarms;
    private String city;
    private long cityid;
    private List<Indexes> indexes;
    private Pm25 pm25;
    private String provinceName;
    private Realtime realtime;
    private WeatherDetailsInfo weatherDetailsInfo;
    private List<Weathers> weathers;
    
    public Value(String city, long cityid) {
        this.city = city;
        this.cityid = cityid;
    }
    
    public Value() {
    }
    
    public void setAlarms(List<Alarms> alarms) {
         this.alarms = alarms;
     }
     public List<Alarms> getAlarms() {
         return alarms;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setCityid(long cityid) {
         this.cityid = cityid;
     }
     public long getCityid() {
         return cityid;
     }

    public void setIndexes(List<Indexes> indexes) {
         this.indexes = indexes;
     }
     public List<Indexes> getIndexes() {
         return indexes;
     }

    public void setPm25(Pm25 pm25) {
         this.pm25 = pm25;
     }
     public Pm25 getPm25() {
         return pm25;
     }

    public void setProvinceName(String provinceName) {
         this.provinceName = provinceName;
     }
     public String getProvinceName() {
         return provinceName;
     }

    public void setRealtime(Realtime realtime) {
         this.realtime = realtime;
     }
     public Realtime getRealtime() {
         return realtime;
     }

    public void setWeatherDetailsInfo(WeatherDetailsInfo weatherDetailsInfo) {
         this.weatherDetailsInfo = weatherDetailsInfo;
     }
     public WeatherDetailsInfo getWeatherDetailsInfo() {
         return weatherDetailsInfo;
     }

    public void setWeathers(List<Weathers> weathers) {
         this.weathers = weathers;
     }
     public List<Weathers> getWeathers() {
         return weathers;
     }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.alarms);
        dest.writeString(this.city);
        dest.writeLong(this.cityid);
        dest.writeTypedList(this.indexes);
        dest.writeParcelable(this.pm25, flags);
        dest.writeString(this.provinceName);
        dest.writeParcelable(this.realtime, flags);
        dest.writeParcelable(this.weatherDetailsInfo, flags);
        dest.writeTypedList(this.weathers);
    }
    
    protected Value(Parcel in) {
        this.alarms = in.createTypedArrayList(Alarms.CREATOR);
        this.city = in.readString();
        this.cityid = in.readLong();
        this.indexes = in.createTypedArrayList(Indexes.CREATOR);
        this.pm25 = in.readParcelable(Pm25.class.getClassLoader());
        this.provinceName = in.readString();
        this.realtime = in.readParcelable(Realtime.class.getClassLoader());
        this.weatherDetailsInfo = in.readParcelable(WeatherDetailsInfo.class.getClassLoader());
        this.weathers = in.createTypedArrayList(Weathers.CREATOR);
    }
    
    public static final Creator<Value> CREATOR = new Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel source) {
            return new Value(source);
        }
        
        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };
}