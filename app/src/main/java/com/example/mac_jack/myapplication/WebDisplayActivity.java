package com.example.mac_jack.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebDisplayActivity extends AppCompatActivity {

    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_display);
        webView = (WebView)findViewById(R.id.webview);
        //获取sharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("hm", Context.MODE_PRIVATE);
        //获取editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

        Intent intent = getIntent();
        String string = intent.getStringExtra("weburl");
        //String string = intent.getDataString("weburl");
                //Log.e("1",string);
        webView.loadUrl(string);


    }
}
