package com.cntysoft.weishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.cntysoft.weishop.ui.CommonNavView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InformationActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private ProgressBar progressBar;
    private CommonNavView commonNavView;
    private ImageView navImageView;
    private SimpleAdapter simpleAdapter;
    private List<HashMap<String,Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ExitApplication.newInstance().addActivity(this);
        listView = (ListView) findViewById(R.id.info_lv);
        progressBar = (ProgressBar) findViewById(R.id.info_bar);
        commonNavView = (CommonNavView) findViewById(R.id.info_rl);
        navImageView =commonNavView.getImageView();
        //navImageView = (ImageView) findViewById(R.id.iv_left_arrow);
        getData();
        progressBar.setVisibility(View.GONE);
        simpleAdapter = new SimpleAdapter(this,data,R.layout.info_item,
                new String[]{"title","time"},new int[]{R.id.info_item_title,R.id.info_item_time});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("InformationActivity", data.get(i).get("id") + "");
                Intent intent = new Intent(InformationActivity.this,DetailInfomationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_right_in,R.anim.out);
            }
        });
        navImageView.setOnClickListener(this);
    }

    private void getData(){
        data = new ArrayList<HashMap<String,Object>>();
        for(int i = 0;i < 20; i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("title","微店完全免费,所有交易不收取任何手续费。交易次日,微店即会将货款提现至你的银行卡,让你及时回款。支持信用卡、储蓄卡、支付宝等多种方式付款,安全又方便。");
            map.put("time","2015-03-24 09:03");
            map.put("id",i);
            data.add(map);
        }
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
