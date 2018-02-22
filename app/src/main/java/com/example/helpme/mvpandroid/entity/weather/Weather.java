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
public class Weather implements Parcelable {

    private String code;
    private String message;
    private String redirect;
    private List<Value> value;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setRedirect(String redirect) {
         this.redirect = redirect;
     }
     public String getRedirect() {
         return redirect;
     }

    public void setValue(List<Value> value) {
         this.value = value;
     }
     public List<Value> getValue() {
         return value;
     }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.message);
        dest.writeString(this.redirect);
        dest.writeTypedList(this.value);
    }
    
    public Weather() {
    }
    
    protected Weather(Parcel in) {
        this.code = in.readString();
        this.message = in.readString();
        this.redirect = in.readString();
        this.value = in.createTypedArrayList(Value.CREATOR);
    }
    
    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }
        
        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}