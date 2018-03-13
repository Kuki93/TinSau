package com.example.helpme.mvpandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.Exif;
import com.example.helpme.mvpandroid.entity.image.ExifBean;
import com.example.helpme.mvpandroid.entity.image.ExifIFD;
import com.example.helpme.mvpandroid.entity.image.File;
import com.example.helpme.mvpandroid.entity.image.IFD0;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.entity.image.ItemContent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.entity.image.摘要;
import com.example.helpme.mvpandroid.module.image.ImageModelImpl;
import com.example.helpme.mvpandroid.widget.ZoomDragPhotoView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class PhotoViewPagerAdapter extends PagerAdapter implements ImageContract.ImageOkHttpCallBcak {
    
    private SparseArray<ImageDetailsRecyclerAdapter> mAdapters;
    private ArrayList<PhotoGroup> mPhotoGroups;
    private Context mContext;
    private SparseArray<SoftReference<RecyclerView>> cacheView;
    private ImageModelImpl mImageModel;
    private int size;
    private List<Integer> nums;
    
    private ImageContract.OnItemChildClickListener mOnItemChildClickListener;
    private ImageContract.OnDragPhototListener mOnDragPhototListener;
    
    public void setOnItemChildClickListener(ImageContract.OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }
    
    public void setOnDragPhototListener(ImageContract.OnDragPhototListener onDragPhototListener) {
        mOnDragPhototListener = onDragPhototListener;
    }
    
    public RecyclerView getPhotoRecyclerView(int position) {
        return  cacheView.get(position).get();
    }
    
    
    public ZoomDragPhotoView getPhotoView(int position) {
        return (ZoomDragPhotoView) mAdapters.get(position).getViewByPosition(0, R.id.photoview);
    }
    
    public PhotoViewPagerAdapter(ArrayList<PhotoGroup> photoGroups, Context context, List<Integer> nums, int size) {
        mPhotoGroups = photoGroups;
        mContext = context;
        this.nums = nums;
        this.size = size;
        cacheView = new SparseArray<>();
        mAdapters = new SparseArray<>();
        mImageModel = new ImageModelImpl();
    }
    
    public void addNewData(ArrayList<PhotoGroup> photoGroups, int size) {
        mPhotoGroups = photoGroups;
        this.size = size;
        notifyDataSetChanged();
    }
    
    private PhotoGroup getGroupByPosition(int position) {
        int count = nums.size();
        for (int i = 0; i < count; i++) {
            if (position < nums.get(i))
                return mPhotoGroups.get(i);
        }
        return null;
    }
    
    private ImageDetails getImageByPosition(int position) {
        int count = nums.size();
        for (int i = 0; i < count; i++) {
            if (position < nums.get(i))
                if (i == 0)
                    return mPhotoGroups.get(i).getImages().get(position);
                else
                    return mPhotoGroups.get(i).getImages().get(position - nums.get(i - 1));
        }
        return null;
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RecyclerView mRecyclerView = cacheView.get(position) != null ? cacheView.get(position).get() : null;
        if (mRecyclerView == null) {
            ImageDetails mImageDetails = getImageByPosition(position);
            StringBuilder url = new StringBuilder(GlobalConfig.TU_CHONG_RECOMMEND_BASE_URL);
            url.append("images/").append(mImageDetails.getImgId()).append("/exif?").append(GlobalConfig.TU_CHONG_URL);
            try {
                mImageModel.okhttp_get(true, position, ExifBean.class, this, url.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<ItemContent> contents = mImageDetails.getItems();
            if (getGroupByPosition(position).getTitle() != null)
                contents.add(new ItemContent(getGroupByPosition(position).getTitle(), null, 1));
            if (getGroupByPosition(position).getContent() != null)
                contents.add(new ItemContent(getGroupByPosition(position).getContent(), null, 2));
            contents.add(new ItemContent(4));
            mRecyclerView = new RecyclerView(mContext);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            ImageDetailsRecyclerAdapter mAdapter = new ImageDetailsRecyclerAdapter(contents);
            mAdapter.bindToRecyclerView(mRecyclerView);
            cacheView.put(position, new SoftReference(mRecyclerView));
            mAdapters.put(position, mAdapter);
            mAdapter.setOnItemChildClickListener(getListener(position));
            mAdapter.setOnDragPhototListener(mOnDragPhototListener);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                
                View view;
                
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (view == null)
                        view = linearLayoutManager.findViewByPosition(0);
                    if (view.getTop() < -60)
                        mOnItemChildClickListener.onScrolling(true);
                    else
                        mOnItemChildClickListener.onScrolling(false);
                }
            });
        }
        container.addView(mRecyclerView);
        return mRecyclerView;
    }
    
    @Override
    public int getCount() {
        return size;
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    
    
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapters.get(msg.arg1).addData(msg.arg2, (List<ItemContent>) msg.obj);
        }
    };
    
    public void onCloseHandler() {
        mHandler.removeCallbacksAndMessages(null);
    }
    
    @Override
    public void onSuccess(final Object data, final boolean refresh, final int position) {
        if (data != null && data instanceof ExifBean) {
            ExifBean exifBean = (ExifBean) data;
            if (exifBean.getResult().equals("SUCCESS")) {
                List<ItemContent> contents = new ArrayList<>();
                Exif exif = exifBean.getExif();
                if (exif.get摘要() != null) {
                    contents.add(new ItemContent("相机参数", null, 2));
                    for (摘要 摘要 : exif.get摘要()) {
                        contents.add(new ItemContent(摘要.getContent(), 摘要.getDesc(), 3));
                    }
                }
                if (exif.getFile() != null) {
                    contents.add(new ItemContent("File", null, 2));
                    for (File file : exif.getFile()) {
                        contents.add(new ItemContent(file.getContent(), file.getDesc(), 3));
                    }
                }
                if (exif.getIFD0() != null) {
                    contents.add(new ItemContent("IFD0", null, 2));
                    for (IFD0 ifd0 : exif.getIFD0()) {
                        contents.add(new ItemContent(ifd0.getContent(), ifd0.getDesc(), 3));
                    }
                }
                if (exif.getExifIFD() != null) {
                    contents.add(new ItemContent("ExifIFD", null, 2));
                    for (ExifIFD exifIFD : exif.getExifIFD()) {
                        contents.add(new ItemContent(exifIFD.getContent(), exifIFD.getDesc(), 3));
                    }
                }
                Message message = mHandler.obtainMessage(0, position, mAdapters.get(position).getItemCount() - 1,
                        contents);
                mHandler.sendMessage(message);
            }
        }
    }
    
    @Override
    public void onFail(final boolean refresh, int posititon, String errorInfo) {
        
    }
    
    
    @NonNull
    private BaseQuickAdapter.OnItemChildClickListener getListener(final int position) {
        return new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mOnItemChildClickListener.onPhotoViewClick();
            }
        };
    }
}
