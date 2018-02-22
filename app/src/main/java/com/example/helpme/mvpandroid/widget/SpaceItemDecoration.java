package com.example.helpme.mvpandroid.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    
    private int space;
    
    public SpaceItemDecoration(int space) {
        this.space = space;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) != -1)
            outRect.bottom = space;
    }
}