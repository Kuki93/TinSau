package com.example.helpme.mvpandroid.entity.video;

import java.util.List;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class GifImage {
    private int width ;
    private int height;
    private int r_width;
    private int r_height;
    private String uri;
    private List<Url_list> url_list;
    
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
    
    public int getR_width() {
        return r_width;
    }
    
    public void setR_width(int r_width) {
        this.r_width = r_width;
    }
    
    public int getR_height() {
        return r_height;
    }
    
    public void setR_height(int r_height) {
        this.r_height = r_height;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public List<Url_list> getUrl_list() {
        return url_list;
    }
    
    public void setUrl_list(List<Url_list> url_list) {
        this.url_list = url_list;
    }
}
