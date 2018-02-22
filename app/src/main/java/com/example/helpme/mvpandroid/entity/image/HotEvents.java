/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.image;
import java.util.List;

/**
 * Auto-generated: 2018-02-17 23:35:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HotEvents {

    private String tag_id;
    private String tag_name;
    private String status;
    private String posts;
    private String title;
    private String url;
    private String prize_desc;
    private String prize_url;
    private String introduction_url;
    private int image_count;
    private long introduction_id;
    private int competition_type;
    private int dueDays;
    private boolean list_banner;
    private List<String> images;
    
    public int getImage_count() {
        return image_count;
    }
    
    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }
    
    public void setTag_id(String tag_id) {
         this.tag_id = tag_id;
     }
     public String getTag_id() {
         return tag_id;
     }

    public void setTag_name(String tag_name) {
         this.tag_name = tag_name;
     }
     public String getTag_name() {
         return tag_name;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setPosts(String posts) {
         this.posts = posts;
     }
     public String getPosts() {
         return posts;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setPrize_desc(String prize_desc) {
         this.prize_desc = prize_desc;
     }
     public String getPrize_desc() {
         return prize_desc;
     }

    public void setPrize_url(String prize_url) {
         this.prize_url = prize_url;
     }
     public String getPrize_url() {
         return prize_url;
     }

    public void setIntroduction_url(String introduction_url) {
         this.introduction_url = introduction_url;
     }
     public String getIntroduction_url() {
         return introduction_url;
     }

    public void setIntroduction_id(long introduction_id) {
         this.introduction_id = introduction_id;
     }
     public long getIntroduction_id() {
         return introduction_id;
     }

    public void setCompetition_type(int competition_type) {
         this.competition_type = competition_type;
     }
     public int getCompetition_type() {
         return competition_type;
     }

    public void setDueDays(int dueDays) {
         this.dueDays = dueDays;
     }
     public int getDueDays() {
         return dueDays;
     }
    
    public boolean isList_banner() {
        return list_banner;
    }
    
    public void setList_banner(boolean list_banner) {
        this.list_banner = list_banner;
    }
    
    public void setImages(List<String> images) {
         this.images = images;
     }
     public List<String> getImages() {
         return images;
     }

}