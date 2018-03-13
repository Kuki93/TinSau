package com.example.helpme.mvpandroid.module.video;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    
    public VideoFragment() {
        // Required empty public constructor
    }
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber sectionNumber.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(int sectionNumber) {
        return new VideoFragment();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfig.initVideoUrl(getContext());
    }
    
    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);
        VideoFragment.VideoFragmentAdapter mAdapter = new VideoFragment.VideoFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }
            
            @Override
            public void onPageSelected(int position) {
                JZVideoPlayer.goOnPlayOnPause();
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {
                
            }
        });
        return rootView;
    }
    
    
    class VideoFragmentAdapter extends FragmentPagerAdapter {
        
        public VideoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public Fragment getItem(int position) {
            return DVideoFragment.newInstance(position);
        }
        
        @Override
        public int getCount() {
            return 4;
        }
        
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "推荐";
            else if (position == 1)
                return "视频";
            else if (position == 2)
                return "图片";
            else 
                return "段子";
        }
    }
}
