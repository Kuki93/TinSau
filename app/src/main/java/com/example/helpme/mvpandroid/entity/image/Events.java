package com.example.helpme.mvpandroid.entity.image;

import java.util.List;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
public class Events {
    private List<HotEvents> eventList;
    private boolean more;
    private String total;
    private String result;
    
    public List<HotEvents> getEventList() {
        return eventList;
    }
    
    public void setEventList(List<HotEvents> eventList) {
        this.eventList = eventList;
    }
    
    public boolean isMore() {
        return more;
    }
    
    public void setMore(boolean more) {
        this.more = more;
    }
    
    public String getTotal() {
        return total;
    }
    
    public void setTotal(String total) {
        this.total = total;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
}
