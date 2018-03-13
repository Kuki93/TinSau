package com.example.helpme.mvpandroid.entity.image;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/24.
 * @Description
 */
public class MessageEvent {
    
    public static class AddNewDataEvent {
        
        public ArrayList<PhotoGroup> newData;
        public boolean more;
        
        public AddNewDataEvent(ArrayList<PhotoGroup> newData, boolean more) {
            this.newData = newData;
            this.more = more;
        }
    }
    
    public static class SlideBackEvent {
        public int flag = 0;
        public int position;
        public int itemIndex;
        
        public SlideBackEvent(int position, int itemIndex, int flag) {
            this.flag = flag;
            this.position = position;
            this.itemIndex = itemIndex;
        }
    }
    
    public static class Empty {
        public int flag = 0;
        
        public Empty(int flag) {
            this.flag = flag;
        }
    }
}
