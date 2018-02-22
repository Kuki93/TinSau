/**
 * Copyright 2018 bejson.com
 */
package com.example.helpme.mvpandroid.entity.image;

import java.util.List;

/**
 * Auto-generated: 2018-02-11 20:57:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FeedList {
    
    private long post_id;
    private String type;
    private String url;
    private String site_id;
    private String author_id;
    private String published_at;
    private String excerpt;
    private int favorites;
    private String content;
    private String title;
    private int image_count;
    private List<Title_image> title_image;
    private List<Images> images;
    private Site site;
    private String rqt_id;
    private int mode;
    private float[] scales;
    private int height;
    private int typeInt = 0;
    
    public List<Title_image> getTitle_image() {
        return title_image;
    }
    
    public void setTitle_image(List<Title_image> title_image) {
        this.title_image = title_image;
    }
    
    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }
    
    public long getPost_id() {
        return post_id;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
    
    public String getSite_id() {
        return site_id;
    }
    
    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }
    
    public String getAuthor_id() {
        return author_id;
    }
    
    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }
    
    public String getPublished_at() {
        return published_at;
    }
    
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    
    public String getExcerpt() {
        return excerpt;
    }
    
    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }
    
    public int getFavorites() {
        return favorites;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }
    
    public int getImage_count() {
        return image_count;
    }
    
    public void setImages(List<Images> images) {
        this.images = images;
    }
    
    public List<Images> getImages() {
        return images;
    }
    
    
    public void setSite(Site site) {
        this.site = site;
    }
    
    public Site getSite() {
        return site;
    }
    
    public void setRqt_id(String rqt_id) {
        this.rqt_id = rqt_id;
    }
    
    public String getRqt_id() {
        return rqt_id;
    }
    
    public int getTypeInt() {
        return typeInt;
    }
    
    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }
    
    public int getMode() {
        return mode;
    }
    
    public void setMode(int mode) {
        this.mode = mode;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public float[] getScales() {
        return scales;
    }
    
    public void setScales(float[] scales) {
        this.scales = scales;
    }
    
    public void setModeAndScales(int mode, float[] scales) {
        this.mode = mode;
        this.scales = scales;
    }
}