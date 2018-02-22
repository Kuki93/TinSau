package com.example.helpme.mvpandroid.entity.image;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class PhotoGroup implements Parcelable {
    
    private String name;
    private String iconUrl;
    private String title;
    private String content;
    private String homeUrl;
    private List<Long> img_ids;
    private List<String> urls;
    private int width;
    private int height;
    private int index;
    
    public PhotoGroup(String name, String iconUrl, String homeUrl, String title, String content,int width,int height) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.title = title;
        this.content = content;
        this.homeUrl = homeUrl;
        this.width = width;
        this.height = height;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getHomeUrl() {
        return homeUrl;
    }
    
    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }
    
    public List<Long> getImg_ids() {
        return img_ids;
    }
    
    public void setImg_ids(List<Long> img_ids) {
        this.img_ids = img_ids;
    }
    
    public List<String> getUrls() {
        return urls;
    }
    
    public void setUrls(List<String> urls) {
        this.urls = urls;
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
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.iconUrl);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.homeUrl);
        dest.writeList(this.img_ids);
        dest.writeStringList(this.urls);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.index);
    }
    
    public PhotoGroup() {
    }
    
    protected PhotoGroup(Parcel in) {
        this.name = in.readString();
        this.iconUrl = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.homeUrl = in.readString();
        this.img_ids = new ArrayList<Long>();
        in.readList(this.img_ids, Long.class.getClassLoader());
        this.urls = in.createStringArrayList();
        this.width = in.readInt();
        this.height = in.readInt();
        this.index = in.readInt();
    }
    
    public static final Creator<PhotoGroup> CREATOR = new Creator<PhotoGroup>() {
        @Override
        public PhotoGroup createFromParcel(Parcel source) {
            return new PhotoGroup(source);
        }
        
        @Override
        public PhotoGroup[] newArray(int size) {
            return new PhotoGroup[size];
        }
    };
}
