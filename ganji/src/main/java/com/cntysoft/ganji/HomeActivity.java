package com.cntysoft.ganji;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.cntysoft.ganji.fragment.MainFragment;
import com.cntysoft.ganji.fragment.NearbyFragment;
import com.cntysoft.ganji.fragment.PersonalCenterFragment;
import com.cntysoft.ganji.fragment.PublishFragment;
import com.cntysoft.ganji.widget.FragmentTabHost;
import com.cntysoft.ganji.utils.CommonUtils;

public class HomeActivity extends FragmentActivity implements MainFragment.ViewClickListener{

    private SharedPreferences sp;
    private FragmentTabHost fragmentTabHost;
    private ImageView nextImageView;

    private Class fragmentArray[] = {MainFragment.class,NearbyFragment.class,PublishFragment.class
    ,PersonalCenterFragment.class};
    private int[] imageArray = {R.drawable.tab_lastcategories_bg,R.drawable.tab_near_bg,
    R.drawable.tab_publish_bg,R.drawable.tab_personal_centre_bg};
    private String mTextviewArray[] = {"category", "nearby", "publish", "personalCenter"};
    private int width ;
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
        width = getWindowManager().getDefaultDisplay().getWidth();
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
        nextImageView = (ImageView) findViewById(R.id.go_next);
        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,IMHomeActivity.class);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        exitDialog();
    }
    
    private Dialog dialog;
    private TextView exitCancle;
    private TextView exitOk;
    private void exitDialog(){
        dialog = new Dialog(this,R.style.dialog);
        View exitView = View.inflate(this,R.layout.dialog_exit,null);
        exitCancle = (TextView) exitView.findViewById(R.id.exit_cancle);
        exitOk = (TextView) exitView.findViewById(R.id.exit_ok);
        exitCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exitOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dialog.setContentView(exitView, new ViewGroup.LayoutParams(width - 60, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();
    }
}
