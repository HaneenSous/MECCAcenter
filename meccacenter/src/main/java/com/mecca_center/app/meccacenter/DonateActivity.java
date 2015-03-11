package com.mecca_center.app.meccacenter;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_donate)
public class DonateActivity extends ActionBarActivity {

    @ViewById
    WebView donatepage;

    @ViewById
    Toolbar toolbar;

    @AfterViews
    public  void LoadPage(){
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        donatepage.getSettings().setJavaScriptEnabled(true);
        donatepage.getSettings().setLoadWithOverviewMode(true);
        donatepage.getSettings().setLoadsImagesAutomatically(true);

        donatepage.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
                return true;
            }
        });

        donatepage.loadUrl("http://mecca-center.com/images/mohid/donation.html");

    }

}
