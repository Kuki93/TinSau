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
public class Weathers implements Parcelable {

    private String date;
    private String img;
    private String sun_down_time;
    private String sun_rise_time;
    private String temp_day_c;
    private String temp_day_f;
    private String temp_night_c;
    private String temp_night_f;
    private String wd;
    private String weather;
    private String week;
    private String ws;
    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setSun_down_time(String sun_down_time) {
         this.sun_down_time = sun_down_time;
     }
     public String getSun_down_time() {
         return sun_down_time;
     }

    public void setSun_rise_time(String sun_rise_time) {
         this.sun_rise_time = sun_rise_time;
     }
     public String getSun_rise_time() {
         return sun_rise_time;
     }

    public void setTemp_day_c(String temp_day_c) {
         this.temp_day_c = temp_day_c;
     }
     public String getTemp_day_c() {
         return temp_day_c;
     }

    public void setTemp_day_f(String temp_day_f) {
         this.temp_day_f = temp_day_f;
     }
     public String getTemp_day_f() {
         return temp_day_f;
     }

    public void setTemp_night_c(String temp_night_c) {
         this.temp_night_c = temp_night_c;
     }
     public String getTemp_night_c() {
         return temp_night_c;
     }

    public void setTemp_night_f(String temp_night_f) {
         this.temp_night_f = temp_night_f;
     }
     public String getTemp_night_f() {
         return temp_night_f;
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

    public void setWeek(String week) {
         this.week = week;
     }
     public String getWeek() {
         return week;
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
        dest.writeString(this.date);
        dest.writeString(this.img);
        dest.writeString(this.sun_down_time);
        dest.writeString(this.sun_rise_time);
        dest.writeString(this.temp_day_c);
        dest.writeString(this.temp_day_f);
        dest.writeString(this.temp_night_c);
        dest.writeString(this.temp_night_f);
        dest.writeString(this.wd);
        dest.writeString(this.weather);
        dest.writeString(this.week);
        dest.writeString(this.ws);
    }
    
    public Weathers() {
    }
    
    protected Weathers(Parcel in) {
        this.date = in.readString();
        this.img = in.readString();
        this.sun_down_time = in.readString();
        this.sun_rise_time = in.readString();
        this.temp_day_c = in.readString();
        this.temp_day_f = in.readString();
        this.temp_night_c = in.readString();
        this.temp_night_f = in.readString();
        this.wd = in.readString();
        this.weather = in.readString();
        this.week = in.readString();
        this.ws = in.readString();
    }
    
    public static final Creator<Weathers> CREATOR = new Creator<Weathers>() {
        @Override
        public Weathers createFromParcel(Parcel source) {
            return new Weathers(source);
        }
        
        @Override
        public Weathers[] newArray(int size) {
            return new Weathers[size];
        }
    };
}