package com.example.helpme.mvpandroid.entity.image;

import android.graphics.Rect;
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
    private String opusUrl;
    private String siteUrl;
    private String publishDate;
    private String type;
    private List<ImageDetails> images;
    private List<Rect> rects;
    private int favorite;
    private int typeInt = 0;
    private int puzzHeight;
    private int mode;
    private float[] scales;
    private Rect rect;
    
    public void setModeAndScales(int mode, float[] scales) {
        this.mode = mode;
        this.scales = scales;
    }
    
    public PhotoGroup(String name, String iconUrl, String opusUrl, String siteUrl,String publishDate, int index, int 
            favorite) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.opusUrl = opusUrl;
        this.siteUrl = siteUrl;
        this.publishDate = publishDate;
        this.favorite = favorite;
    }
    
    public List<Rect> getRects() {
        return rects;
    }
    
    public void setRects(List<Rect> rects) {
        this.rects = rects;
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
    
    public String getOpusUrl() {
        return opusUrl;
    }
    
    public void setOpusUrl(String opusUrl) {
        this.opusUrl = opusUrl;
    }
    
    public String getSiteUrl() {
        return siteUrl;
    }
    
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPublishDate() {
        return publishDate;
    }
    
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    
    public List<ImageDetails> getImages() {
        return images;
    }
    
    public void setImages(List<ImageDetails> images) {
        this.images = images;
    }
    
    public int getFavorite() {
        return favorite;
    }
    
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    
    public int getTypeInt() {
        return typeInt;
    }
    
    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }
    
    public int getPuzzHeight() {
        return puzzHeight;
    }
    
    public void setPuzzHeight(int puzzHeight) {
        this.puzzHeight = puzzHeight;
    }
    
    public int getMode() {
        return mode;
    }
    
    public void setMode(int mode) {
        this.mode = mode;
    }
    
    public float[] getScales() {
        return scales;
    }
    
    public void setScales(float[] scales) {
        this.scales = scales;
    }
    
    public Rect getRect() {
        return rect;
    }
    
    public void setRect(Rect rect) {
        this.rect = rect;
    }
    
    public PhotoGroup() {
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
        dest.writeString(this.opusUrl);
        dest.writeString(this.siteUrl);
        dest.writeString(this.publishDate);
        dest.writeString(this.type);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.rects);
        dest.writeInt(this.favorite);
        dest.writeInt(this.typeInt);
        dest.writeInt(this.puzzHeight);
        dest.writeInt(this.mode);
        dest.writeFloatArray(this.scales);
        dest.writeParcelable(this.rect, flags);
    }
    
    protected PhotoGroup(Parcel in) {
        this.name = in.readString();
        this.iconUrl = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.opusUrl = in.readString();
        this.siteUrl = in.readString();
        this.publishDate = in.readString();
        this.type = in.readString();
        this.images = in.createTypedArrayList(ImageDetails.CREATOR);
        this.rects = in.createTypedArrayList(Rect.CREATOR);
        this.favorite = in.readInt();
        this.typeInt = in.readInt();
        this.puzzHeight = in.readInt();
        this.mode = in.readInt();
        this.scales = in.createFloatArray();
        this.rect = in.readParcelable(Rect.class.getClassLoader());
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
