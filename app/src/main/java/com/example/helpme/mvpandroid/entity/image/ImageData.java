package com.example.helpme.mvpandroid.entity.image;

import java.util.List;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
public class ImageData {
    private List<FeedList> post_list;
    private String result;
    
    public List<FeedList> getPost_list() {
        return post_list;
    }
    
    public void setPost_list(List<FeedList> post_list) {
        this.post_list = post_list;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
}
