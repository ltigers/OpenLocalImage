package com.cntysoft.weishop;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.cntysoft.weishop.ui.CommonNavView;

public class DetailInfomationActivity extends Activity implements View.OnClickListener{

    private CommonNavView commonNavView;
    private ImageView navImageView;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_infomation);
        ExitApplication.newInstance().addActivity(this);
        commonNavView = (CommonNavView) findViewById(R.id.info_rl);
        navImageView =commonNavView.getImageView();
        webView = (WebView) findViewById(R.id.detail_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baidu.com");
        navImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_left_arrow){
            onBackPressed();
            ExitApplication.newInstance().removeActivty(this);
            finish();
            overridePendingTransition(0,R.anim.push_right_out);
        }
    }
}
