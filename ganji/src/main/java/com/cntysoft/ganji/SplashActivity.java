package com.cntysoft.ganji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cntysoft.ganji.utils.ConnectionHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        copyDB();
        final boolean iscon = ConnectionHelper.checkNetworkInfo(this);
        delayHome(iscon,this);
    }
    
    private void enterHome(){
        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void delayHome(final boolean isconn,final Context context){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enterHome();
                if(!isconn){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }
    
    private void copyDB(){
        
        try{
            File file = new File(getFilesDir(),"grouplib.db");
            if(file.exists() && file.length()>0){
                Log.i(TAG, "正常了，就不需要拷贝了");
            }else{
                InputStream is = getAssets().open("grouplib.db");
                FileOutputStream fos = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int len = 0;
                while((len = is.read(buffer))!= -1){
                    fos.write(buffer,0,len);
                }
                is.close();
                fos.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
