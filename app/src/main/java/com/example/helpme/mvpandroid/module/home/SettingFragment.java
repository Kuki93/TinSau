package com.example.helpme.mvpandroid.module.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.module.weather.WeatherActivity;
import com.example.helpme.mvpandroid.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created by helpme on 2018/2/26.
 * @Description
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    
    public SettingFragment() {
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
    public static SettingFragment newInstance(int sectionNumber) {
        return new SettingFragment();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfig.initVideoUrl(getContext());
    }
    
    @BindView(R.id.cache)
    LinearLayout cache;
    @BindView(R.id.sharpness)
    LinearLayout Sharpness;
    @BindView(R.id.weather)
    LinearLayout weather;
    @BindView(R.id.switcher)
    Switch mSwitch;
    @BindView(R.id.cacheSize)
    TextView cacheSize;
    
    private SharedPreferences.Editor edit;
    @OnClick(R.id.about)
    public void About() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);
        SharedPreferences preferences = getActivity().getSharedPreferences("helpme", Context.MODE_PRIVATE);
        edit = preferences.edit();
        
        mSwitch.setChecked(preferences.getBoolean(GlobalConfig.NEI_HAN_DUAN_ZI_720P, true));
        cache.setOnClickListener(this);
        Sharpness.setOnClickListener(this);
        weather.setOnClickListener(this);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalConfig.priority720P = isChecked;
                edit.putBoolean(GlobalConfig.NEI_HAN_DUAN_ZI_720P, isChecked);
                edit.commit();
            }
        });
        return rootView;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            cacheSize.setText(CacheUtil.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            try {
                cacheSize.setText(CacheUtil.getTotalCacheSize(getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    @Override
    public void onClick(View v) {
        if (v == cache) {
            CacheUtil.clearAllCache(getContext());
            try {
                cacheSize.setText(CacheUtil.getTotalCacheSize(getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(), "缓存已清理", Toast.LENGTH_SHORT).show();
        } else if (v == Sharpness) {
            mSwitch.toggle();
        } else if (v == weather) {
            startActivity(new Intent(getActivity(), WeatherActivity.class));
        }
    }
}
