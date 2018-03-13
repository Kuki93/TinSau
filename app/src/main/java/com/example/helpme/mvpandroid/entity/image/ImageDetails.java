package com.example.helpme.mvpandroid.entity.image;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Created by helpme on 2018/2/13.
 * @Description
 */
public class ImageDetails implements Parcelable {
    
    private List<ItemContent> items;
   
    private long imgId;
    
    public ImageDetails(List<ItemContent> items, long imgId) {
        this.items = items;
        this.imgId = imgId;
    }
    
    public List<ItemContent> getItems() {
        return items;
    }
    
    public void setItems(List<ItemContent> items) {
        this.items = items;
    }
    
    public long getImgId() {
        return imgId;
    }
    
    public void setImgId(long imgId) {
        this.imgId = imgId;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.items);
        dest.writeLong(this.imgId);
    }
    
    public ImageDetails() {
    }
    
    protected ImageDetails(Parcel in) {
        this.items = in.createTypedArrayList(ItemContent.CREATOR);
        this.imgId = in.readLong();
    }
    
    public static final Creator<ImageDetails> CREATOR = new Creator<ImageDetails>() {
        @Override
        public ImageDetails createFromParcel(Parcel source) {
            return new ImageDetails(source);
        }
        
        @Override
        public ImageDetails[] newArray(int size) {
            return new ImageDetails[size];
        }
    };
}
