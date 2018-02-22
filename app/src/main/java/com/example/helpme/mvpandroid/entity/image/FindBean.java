/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.image;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto-generated: 2018-02-17 23:35:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FindBean {

    private List<Banners> banners;
    private List<HotEvents> hotEvents;
    private ArrayList<Categories> categories;
    private String result;
    public void setBanners(List<Banners> banners) {
         this.banners = banners;
     }
     public List<Banners> getBanners() {
         return banners;
     }

    public void setHotEvents(List<HotEvents> hotEvents) {
         this.hotEvents = hotEvents;
     }
     public List<HotEvents> getHotEvents() {
         return hotEvents;
     }
    
    public ArrayList<Categories> getCategories() {
        return categories;
    }
    
    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }
    
    public void setResult(String result) {
         this.result = result;
     }
     public String getResult() {
         return result;
     }

}