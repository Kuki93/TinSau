/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.image;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auto-generated: 2018-02-17 23:35:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Categories implements Parcelable {

    private String tag_name;
    private int tag_id;
    public void setTag_name(String tag_name) {
         this.tag_name = tag_name;
     }
     public String getTag_name() {
         return tag_name;
     }

    public void setTag_id(int tag_id) {
         this.tag_id = tag_id;
     }
     public int getTag_id() {
         return tag_id;
     }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag_name);
        dest.writeInt(this.tag_id);
    }
    
    public Categories() {
    }
    
    protected Categories(Parcel in) {
        this.tag_name = in.readString();
        this.tag_id = in.readInt();
    }
    
    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel source) {
            return new Categories(source);
        }
        
        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}