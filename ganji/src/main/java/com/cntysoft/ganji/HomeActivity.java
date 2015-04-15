package com.cntysoft.ganji;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.cntysoft.ganji.fragment.MainFragment;
import com.cntysoft.ganji.utils.CommonUtils;

public class HomeActivity extends FragmentActivity implements MainFragment.ViewClickListener{

    private SharedPreferences sp;
    private FragmentTabHost fragmentTabHost;

    private Class fragmentArray[] = {MainFragment.class,MainFragment.class,MainFragment.class
    ,MainFragment.class};
    private int[] imageArray = {R.drawable.tab_lastcategories_bg,R.drawable.tab_near_bg,
    R.drawable.tab_publish_bg,R.drawable.tab_personal_centre_bg};
    private String mTextviewArray[] = {"category", "nearby", "publish", "personalCenter"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        String city = sp.getString("city","");
        if(TextUtils.isEmpty(city)){
            goCity();
            finish();
        }           
        setContentView(R.layout.activity_home1);
        initView();
    }
    
    private void goCity(){
        Intent intent = new Intent(HomeActivity.this,CityActivity.class);
        startActivity(intent);
    }
    
    private void initView(){
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        int count = fragmentArray.length;
        for(int i = 0;i < count; i++){
            TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            fragmentTabHost.addTab(tabSpec,fragmentArray[i],null);            
        }
    }
    private View getTabItemView(int index){
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new AbsListView.LayoutParams(CommonUtils.dip2px(this,60), ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setBackgroundResource(imageArray[index]);
        return  imageView;
    }

    @Override
    public void switchActivity(int id) {
        switch (id){
            case 1:
                Toast.makeText(HomeActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                goCity();
                finish();
                break;
            case 2:
                
                break;
        }
    }
}
