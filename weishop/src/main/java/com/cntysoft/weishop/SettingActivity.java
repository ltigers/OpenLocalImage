package com.cntysoft.weishop;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cntysoft.weishop.ui.CommonNavView;


public class SettingActivity extends ActionBarActivity implements View.OnClickListener{

    private CommonNavView commonNavView;
    private ImageView navImageView;
    private TextView exitTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ExitApplication.newInstance().addActivity(this);
        commonNavView = (CommonNavView) findViewById(R.id.info_rl);
        navImageView =commonNavView.getImageView();
        exitTextView = (TextView) findViewById(R.id.setting_exit);
        navImageView.setOnClickListener(this);
        exitTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_left_arrow:
                onBackPressed();
                ExitApplication.newInstance().removeActivty(this);
                finish();
                overridePendingTransition(0,R.anim.push_right_out);
                break;
            case R.id.setting_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage(getString(R.string.exit_message));
                builder.setPositiveButton(getString(R.string.exit_ok),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //android.os.Process.killProcess(android.os.Process.myPid());
                        ExitApplication.newInstance().exit();
                    }
                });
                builder.setNegativeButton(getString(R.string.exit_cancle),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

        }

    }
}