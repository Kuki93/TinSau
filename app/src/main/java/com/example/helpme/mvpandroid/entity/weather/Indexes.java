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
public class Indexes implements Parcelable {

    private String abbreviation;
    private String alias;
    private String content;
    private String level;
    private String name;
    public void setAbbreviation(String abbreviation) {
         this.abbreviation = abbreviation;
     }
     public String getAbbreviation() {
         return abbreviation;
     }

    public void setAlias(String alias) {
         this.alias = alias;
     }
     public String getAlias() {
         return alias;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setLevel(String level) {
         this.level = level;
     }
     public String getLevel() {
         return level;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.abbreviation);
        dest.writeString(this.alias);
        dest.writeString(this.content);
        dest.writeString(this.level);
        dest.writeString(this.name);
    }
    
    public Indexes() {
    }
    
    protected Indexes(Parcel in) {
        this.abbreviation = in.readString();
        this.alias = in.readString();
        this.content = in.readString();
        this.level = in.readString();
        this.name = in.readString();
    }
    
    public static final Parcelable.Creator<Indexes> CREATOR = new Parcelable.Creator<Indexes>() {
        @Override
        public Indexes createFromParcel(Parcel source) {
            return new Indexes(source);
        }
        
        @Override
        public Indexes[] newArray(int size) {
            return new Indexes[size];
        }
    };
}