package com.example.helpme.mvpandroid.entity.video;

import java.util.List;

/**
 * @Created by helpme on 2018/2/26.
 * @Description
 */
public class Image_list {
    private String url;
    private List<Url_list> url_list;
    private String uri;
    private int height;
    private int width;
    private boolean is_gif;
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public List<Url_list> getUrl_list() {
        return url_list;
    }
    
    public void setUrl_list(List<Url_list> url_list) {
        this.url_list = url_list;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public boolean isIs_gif() {
        return is_gif;
    }
    
    public void setIs_gif(boolean is_gif) {
        this.is_gif = is_gif;
    }
}
