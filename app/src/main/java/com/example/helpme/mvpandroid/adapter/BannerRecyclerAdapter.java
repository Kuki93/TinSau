package com.example.helpme.mvpandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.Banners;
import com.example.helpme.mvpandroid.widget.banner.BannerAdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/17.
 * @Description
 */
public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.ViewHolder> {
    
    private List<Banners> mList = new ArrayList<>();
    private BannerAdapterHelper mBannerAdapterHelper = new BannerAdapterHelper();
    private Fragment fragment;
    private int size;
    private ImageContract.OnBannerClickListener mOnBannerClickListener;
    
    public BannerRecyclerAdapter(List<Banners> mList, Fragment fragment, ImageContract.OnBannerClickListener
            onBannerClickListener) {
        this.mList = mList;
        this.fragment = fragment;
        size = mList.size();
        mOnBannerClickListener = onBannerClickListener;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_banner, parent, false);
        mBannerAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mBannerAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Glide.with(fragment).load(mList.get(position % size).getSrc()).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mList.get(position % size).getUrl();
                url = url.substring(url.lastIndexOf('=') + 1, url.length());
                url = "https://tuchong.com/events/" + url;
                Log.d("ssadas", "onClick  " + url);
                mOnBannerClickListener.onClick(position, url);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
    
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        
        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
        }
        
    }
    
}