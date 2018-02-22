package com.example.helpme.mvpandroid.entity.image;

/**
 * @Created by helpme on 2018/2/13.
 * @Description
 */
public class ImageDetails {
    
    private String photoUrl;
    private String content;
    private String desc;
    private int width;
    private int height;
    private int type;
    private int favorite;
    
    public ImageDetails(String photoUrl, int favorite) {
        this.photoUrl = photoUrl;
        this.favorite = favorite;
    }
    
    public ImageDetails(int type) {
        this.type = type;
    }
    
    public ImageDetails(String photoUrl, String content, String desc, int type,int width,int height) {
        this.photoUrl = photoUrl;
        this.content = content;
        this.desc = desc;
        this.type = type;
        this.width = width;
        this.height = height;
    }
    
    public ImageDetails(String photoUrl, String content, String desc, int type) {
        this.photoUrl = photoUrl;
        this.content = content;
        this.desc = desc;
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
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getFavorite() {
        return favorite;
    }
    
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
