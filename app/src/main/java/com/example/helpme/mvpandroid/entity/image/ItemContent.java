package com.example.helpme.mvpandroid.entity.image;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Created by helpme on 2018/2/23.
 * @Description
 */
public class ItemContent implements Parcelable {
    
    private String photoUrl;
    private String content;
    private String desc;
    private int width;
    private int height;
    private int type;
    
    public ItemContent(int type) {
        this.type = type;
    }
    
    public ItemContent(String content, String desc, int type) {
        this.content = content;
        this.desc = desc;
        this.type = type;
    }
    
    public ItemContent(String photoUrl, int width, int height, int type) {
        this.photoUrl = photoUrl;
        this.width = width;
        this.height = height;
        this.type = type;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoUrl);
        dest.writeString(this.content);
        dest.writeString(this.desc);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.type);
    }
    
    protected ItemContent(Parcel in) {
        this.photoUrl = in.readString();
        this.content = in.readString();
        this.desc = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.type = in.readInt();
    }
    
    public static final Creator<ItemContent> CREATOR = new Creator<ItemContent>() {
        @Override
        public ItemContent createFromParcel(Parcel source) {
            return new ItemContent(source);
        }
        
        @Override
        public ItemContent[] newArray(int size) {
            return new ItemContent[size];
        }
    };
}
