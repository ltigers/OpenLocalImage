package com.cntysoft.ganji;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TabHost;

import com.cntysoft.ganji.fragment.PublishFragment;
import com.cntysoft.ganji.widget.FragmentTabHost;


public class IMHomeActivity extends FragmentActivity {

    private ImageView preImageView;
    private FragmentTabHost fragmentTabHost;

    private Class fragmentArray[] = {PublishFragment.class,PublishFragment.class,PublishFragment.class
            ,PublishFragment.class};
    private int[] imageArray = {R.drawable.tab_talk_bg,R.drawable.tab_imnear_bg,
            R.drawable.tab_find_bg,R.drawable.tab_user_bg};
    private String mTextviewArray[] = {"message", "near", "find", "user"};
    private int width ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imhome);
        width = getWindowManager().getDefaultDisplay().getWidth();
         
        initView();
    }
    
    private void initView(){
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        int count = fragmentArray.length;
        for(int i = 0;i < count; i++){
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            fragmentTabHost.addTab(tabSpec,fragmentArray[i],null);
        }
        preImageView = (ImageView) findViewById(R.id.go_pre);
        preImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private View getTabItemView(int index){
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new AbsListView.LayoutParams(width / 5 - 4 , ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setBackgroundResource(imageArray[index]);
        return  imageView;
    }
}
