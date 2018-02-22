package com.example.helpme.mvpandroid.entity.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Created by helpme on 2018/1/30.
 * @Description
 */
public class AllCityCode implements Parcelable {
    
    /**
     * areaid : 101010100
     * countyname : 北京
     */
    private long areaid;
    private String countyname;
    private String fisrtPy;
    private String pinYin;
    private boolean isCheck;
    
    public AllCityCode(long areaid, String countyname) {
        this.areaid = areaid;
        this.countyname = countyname;
        isCheck = false;
    }
    
    public long getAreaid() {
        return areaid;
    }
    
    public void setAreaid(long areaid) {
        this.areaid = areaid;
    }
    
    public String getCountyname() {
        return countyname;
    }
    
    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }
    
    public String getFisrtPy() {
        return fisrtPy;
    }
    
    public void setFisrtPy(String fisrtPy) {
        this.fisrtPy = fisrtPy;
    }
    
    public String getPinYin() {
        return pinYin;
    }
    
    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }
    
    public boolean isCheck() {
        return isCheck;
    }
    
    public void setCheck(boolean check) {
        isCheck = check;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.areaid);
        dest.writeString(this.countyname);
        dest.writeString(this.fisrtPy);
        dest.writeString(this.pinYin);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }
    
    private AllCityCode(Parcel in) {
        this.areaid = in.readLong();
        this.countyname = in.readString();
        this.fisrtPy = in.readString();
        this.pinYin = in.readString();
        this.isCheck = in.readByte() != 0;
    }
    
    public static final Creator<AllCityCode> CREATOR = new Creator<AllCityCode>() {
        @Override
        public AllCityCode createFromParcel(Parcel source) {
            return new AllCityCode(source);
        }
        
        @Override
        public AllCityCode[] newArray(int size) {
            return new AllCityCode[size];
        }
    };
}
