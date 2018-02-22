package com.example.helpme.mvpandroid.widget;

import android.content.Context;

import com.example.helpme.mvpandroid.R;
import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

/**
 * @Created by helpme on 2018/2/1.
 * @Description
 */
public class DividerItemDecoration extends Y_DividerItemDecoration {
    
    private Context mContext;
    private float dx;
    private int startPadding, endPadding;
    
    public DividerItemDecoration(Context context, float dx) {
        super(context);
        mContext = context;
        this.dx = dx;
        this.startPadding = 20;
        this.endPadding = 13;
    }
    
    public DividerItemDecoration(Context context, float dx, int startPadding, int endPadding) {
        super(context);
        mContext = context;
        this.dx = dx;
        this.startPadding = startPadding;
        this.endPadding = endPadding;
    }
    
    
    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider;
        switch (itemPosition) {
            default:
                divider = new Y_DividerBuilder().setBottomSideLine(true, mContext.getResources().getColor(R.color
                        .colorDivider), dx, startPadding, endPadding).create();
                break;
        }
        return divider;
    }
}
