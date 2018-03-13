package com.example.helpme.mvpandroid.module.home;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.helpme.mvpandroid.R;

public class AboutActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView github = findViewById(R.id.github);
        github.setText(Html.fromHtml("概述&nbsp;&nbsp;&nbsp;<strong><a href='https://github.com/Kuki93/TinSau'>项目主页</a></strong>"));
        github.setMovementMethod(LinkMovementMethod.getInstance());
    
        TextView version = findViewById(R.id.version);
        version.setText(getVersion());
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "v" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }
    
}
