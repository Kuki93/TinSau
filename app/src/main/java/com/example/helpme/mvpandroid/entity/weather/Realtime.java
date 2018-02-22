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
public class Realtime implements Parcelable {

    private String img;
    private String sD;
    private String sendibleTemp;
    private String temp;
    private String time;
    private String wD;
    private String wS;
    private String weather;
    private String ziwaixian;
    
    public String getImg() {
        return img;
    }
    
    public void setImg(String img) {
        this.img = img;
    }
    
    public String getsD() {
        return sD;
    }
    
    public void setsD(String sD) {
        this.sD = sD;
    }
    
    public String getSendibleTemp() {
        return sendibleTemp;
    }
    
    public void setSendibleTemp(String sendibleTemp) {
        this.sendibleTemp = sendibleTemp;
    }
    
    public String getTemp() {
        return temp;
    }
    
    public void setTemp(String temp) {
        this.temp = temp;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getwD() {
        return wD;
    }
    
    public void setwD(String wD) {
        this.wD = wD;
    }
    
    public String getwS() {
        return wS;
    }
    
    public void setwS(String wS) {
        this.wS = wS;
    }
    
    public String getWeather() {
        return weather;
    }
    
    public void setWeather(String weather) {
        this.weather = weather;
    }
    
    public String getZiwaixian() {
        return ziwaixian;
    }
    
    public void setZiwaixian(String ziwaixian) {
        this.ziwaixian = ziwaixian;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img);
        dest.writeString(this.sD);
        dest.writeString(this.sendibleTemp);
        dest.writeString(this.temp);
        dest.writeString(this.time);
        dest.writeString(this.wD);
        dest.writeString(this.wS);
        dest.writeString(this.weather);
        dest.writeString(this.ziwaixian);
    }
    
    public Realtime() {
    }
    
    protected Realtime(Parcel in) {
        this.img = in.readString();
        this.sD = in.readString();
        this.sendibleTemp = in.readString();
        this.temp = in.readString();
        this.time = in.readString();
        this.wD = in.readString();
        this.wS = in.readString();
        this.weather = in.readString();
        this.ziwaixian = in.readString();
    }
    
    public static final Creator<Realtime> CREATOR = new Creator<Realtime>() {
        @Override
        public Realtime createFromParcel(Parcel source) {
            return new Realtime(source);
        }
        
        @Override
        public Realtime[] newArray(int size) {
            return new Realtime[size];
        }
    };
}