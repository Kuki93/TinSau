package com.example.helpme.mvpandroid.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.helpme.mvpandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

/**
 * @Created by helpme on 2018/2/15.
 * @Description
 */
public class WebActivity extends BaseActivity implements AdvancedWebView.Listener {
    
    @BindView(R.id.webview)
    AdvancedWebView mWebView;
    @BindView(R.id.toolbar_title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progress)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.toolbar_close)
    ImageView close;
    @BindView(R.id.toolbar_line)
    View line;
    @BindView(R.id.error_layout)
    LinearLayout error;
    
    
    String javascript1 = "javascript:function hideDiv1() {" +
            "document.getElementsByClassName('nav-narrow')[0].style.display='none';" +
            "document.getElementsByClassName('subnav-narrow')[0].style.display='none';" +
            "document.getElementsByClassName('widget-slide')[0].style.display='none';" +
            "document.getElementsByClassName('followerBtn follower')[0].style.display='none';}";
    
    String javascript2 = "javascript:function hideDiv2() {" +
            "document.getElementsByClassName('carousel-container')[0].style.display='none';" +
            "document.getElementsByClassName('red-envelope')[0].style.display='none';" +
            "document.getElementsByClassName('download-box')[0].style.display='none';" +
            "document.getElementsByClassName('comment-form')[0].style.display='none';" +
            "document.getElementsByClassName('share-user share-user-animation')[0].style.display='none';" +
            "document.getElementsByClassName('see-more-comment download-box')[0].style.display='none';}";
    
    private boolean loadError;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle(null);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        mWebView.setListener(this, this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title != null) {
                    mTitle.setText(title);
                    if (title.contains("网页无法打开") || title.contains("error"))
                        loadError = true;
                }
            }
            
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    //加载完毕进度条消失
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    //更新进度
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        
        mWebView.setWebViewClient(new WebViewClient() {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return url.contains("weixin") || super.shouldOverrideUrlLoading(view, url);
            }
            
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    mTitle.setText(title);
                    if (title.contains("网页无法打开") || title.contains("error"))
                        loadError = true;
                }
                if (loadError) {
                    error.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.GONE);
                }
            }
        });
        
        mWebView.loadUrl(getIntent().getStringExtra("url"));
        
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }
    
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }
    
    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }
    
    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        loadError = false;
    }
    
    @Override
    public void onPageFinished(String url) {
        int count = 0;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                count++;
            }
        }
            mWebView.loadUrl(javascript1);
            mWebView.loadUrl("javascript:hideDiv1();");
            mWebView.loadUrl(javascript2);
            mWebView.loadUrl("javascript:hideDiv2();");
        
        // /加载方法  
        if (mWebView.canGoBack()) {
            line.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        loadError = true;
    }
    
    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String
            contentDisposition, String userAgent) {
    }
    
    @Override
    public void onExternalPageRequest(String url) {
    }
    
    @Override
    protected int getStatusBarColor() {
        return R.color.colorTooltitle;
    }
    
}