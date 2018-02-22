package com.example.helpme.mvpandroid.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.image.Categories;

import java.util.List;
import java.util.Random;

/**
 * @Created by helpme on 2018/2/18.
 * @Description
 */
public class CategoryRecyclerAdapter extends BaseQuickAdapter<Categories, BaseViewHolder> {
    
    private String[] Material_Design_Colors = {"#ef5350", "#ec407a", "#ab47bc", "#7e57c2", "#5c6bc0", "#42a5f5",
            "#26c6da", "#26a69a", "#9ccc65", "#212121", "#d4e157", "#29b6f6", "#ff7043", "#78909c", "#ffca28", "#ffee58"};
    
    public CategoryRecyclerAdapter(int layoutResId, @Nullable List<Categories> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, Categories item) {
        ImageView circle = helper.getView(R.id.circle);
        GradientDrawable drawable = (GradientDrawable) circle.getBackground();
        switch (item.getTag_name()) {
            case "热门":
                circle.setImageResource(R.drawable.ic_tag_hot);
                drawable.setColor(Color.parseColor(Material_Design_Colors[0]));
                break;
            case "最新":
                circle.setImageResource(R.drawable.ic_tag_zx);
                drawable.setColor(Color.parseColor(Material_Design_Colors[1]));
                break;
            case "精选":
                circle.setImageResource(R.drawable.ic_tag_jx);
                drawable.setColor(Color.parseColor(Material_Design_Colors[2]));
                break;
            case "人像":
                circle.setImageResource(R.drawable.ic_tag_rx);
                drawable.setColor(Color.parseColor(Material_Design_Colors[3]));
                break;
            case "风光":
                circle.setImageResource(R.drawable.ic_tag_fg);
                drawable.setColor(Color.parseColor(Material_Design_Colors[4]));
                break;
            case "人文":
                circle.setImageResource(R.drawable.ic_tag_rw);
                drawable.setColor(Color.parseColor(Material_Design_Colors[5]));
                break;
            case "旅行":
                circle.setImageResource(R.drawable.ic_tag_lx);
                drawable.setColor(Color.parseColor(Material_Design_Colors[6]));
                break;
            case "建筑":
                circle.setImageResource(R.drawable.ic_tag_jz);
                drawable.setColor(Color.parseColor(Material_Design_Colors[7]));
                break;
            case "静物":
                circle.setImageResource(R.drawable.ic_tag_jw);
                drawable.setColor(Color.parseColor(Material_Design_Colors[8]));
                break;
            case "黑白":
                circle.setImageResource(R.drawable.ic_tag_bw);
                drawable.setColor(Color.parseColor(Material_Design_Colors[9]));
                break;
            default:
                Random random = new Random();
                circle.setImageResource(R.drawable.ic_tag_default);
                drawable.setColor(Color.parseColor(Material_Design_Colors[random.nextInt(16)]));
                break;
        }
        helper.setText(R.id.tag, item.getTag_name());
    }
}
