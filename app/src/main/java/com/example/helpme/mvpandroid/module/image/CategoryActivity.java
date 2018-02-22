package com.example.helpme.mvpandroid.module.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.base.BaseActivity;
import com.example.helpme.mvpandroid.entity.image.Categories;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {
    
    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    
    ArrayList<Categories> mCategories;
    int mCurrentIndex;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mCategories = getIntent().getParcelableArrayListExtra(GlobalConfig.IMAGE_CATEGORIES);
        mCurrentIndex = getIntent().getIntExtra("position", 0);
        CategoryFragmentAdapter mAdapter = new CategoryFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(mCurrentIndex);
    }
    
    class CategoryFragmentAdapter extends FragmentPagerAdapter {
        
        private CategoryFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public Fragment getItem(int position) {
            return ImageCategoryFragment.newInstance(mCategories.get(position).getTag_id());
        }
        
        @Override
        public int getCount() {
            return mCategories.size();
        }
        
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mCategories.get(position).getTag_name();
        }
    }
}
