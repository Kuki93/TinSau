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
public class ImageBean {

    private boolean is_history;
    private int counts;
    private List<FeedList> feedList;
    private String message;
    private boolean more;
    private String result;
    public void setIs_history(boolean is_history) {
         this.is_history = is_history;
     }
     public boolean getIs_history() {
         return is_history;
     }

    public void setCounts(int counts) {
         this.counts = counts;
     }
     public int getCounts() {
         return counts;
     }

    public void setFeedList(List<FeedList> feedList) {
         this.feedList = feedList;
     }
     public List<FeedList> getFeedList() {
         return feedList;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setMore(boolean more) {
         this.more = more;
     }
     public boolean getMore() {
         return more;
     }

    public void setResult(String result) {
         this.result = result;
     }
     public String getResult() {
         return result;
     }

}