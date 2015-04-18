package com.cntysoft.ganji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cntysoft.ganji.adapter.SortAdapter;
import com.cntysoft.ganji.db.dao.CityQueryUtil;
import com.cntysoft.ganji.model.City;
import com.cntysoft.ganji.widget.SideView;

import java.util.ArrayList;
import java.util.List;


public class CityActivity extends Activity {

    private static final String TAG= "CityActivity";
    private SharedPreferences sp;
    private TextView textView;
    private TextView dialog;
    private LinearLayout linearLayout;
    private TextView textViewIndex;
    private ListView listView;
    private LinearLayout linearLayoutInput;
    private SideView sideView;
    
    private List<City> allCityList;
    private List<City> hotCityList;
    private List<City> cityList;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    private SortAdapter sortAdapter;
    private City city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        String chooseCity = sp.getString("city","");
        setContentView(R.layout.activity_city);
        textView = (TextView) findViewById(R.id.left_text_btn);
        if(!TextUtils.isEmpty(chooseCity)){
            textView.setVisibility(View.VISIBLE);
        }
        linearLayoutInput = (LinearLayout) findViewById(R.id.activity_city_search_input);
        dialog = (TextView) findViewById(R.id.dialog);
        linearLayout = (LinearLayout) findViewById(R.id.title_layout);
        textViewIndex = (TextView) findViewById(R.id.title_layout_catalog);
        listView = (ListView) findViewById(R.id.float_listview);
        sideView = (SideView) findViewById(R.id.sideView);
        initData();
        listView.setAdapter(sortAdapter);
        sideView.setTextView(dialog);
        linearLayoutInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this,SearchCityActivity.class);
                startActivity(intent);
            }
        });
        sideView.setOnTouchingLetterChangedListener(new SideView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if ("定位".equals(s)) {
                    s = "#";
                } else if ("热门".equals(s)) {
                    s = "$";
                }
                int posotion = sortAdapter.getPositionForSection(s.charAt(0));
                if (posotion != -1) {
                    listView.setSelection(posotion);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Log.i(TAG,"待做");
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    String currentCity = allCityList.get(position).getName();
                    editor.putString("city",currentCity);
                    editor.commit();
                    
                    Intent intent = new Intent(CityActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /**
                 * 将第一个可见view的首字母与下一个可见view首字母的ASCII比较
                 * 相同时显示分组的不动
                 * 不同时则根据第一个可见view的bottom与分组的高度比较
                 * 当bottom小于分组高度，改变分组的margintop
                 */
                int section = sortAdapter.getSectionForPosition(firstVisibleItem);
                int nextSection = section;
                if(visibleItemCount >= 2){
                     nextSection = sortAdapter.getSectionForPosition(firstVisibleItem + 1);
                }
                int nextSecposition = sortAdapter.getPositionForSection(nextSection);
                if(firstVisibleItem != lastFirstVisibleItem){
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
                    params.topMargin = 0;
                    linearLayout.setLayoutParams(params);
                    int index = sortAdapter.getPositionForSection(section);
                    String text =allCityList.get(index).getSortKey() ;
                    if(index == 0){
                         text = "定位当前所在城市";
                    }else if(index == 1){
                         text ="热门城市";
                    }
                    textViewIndex.setText(text);
                }
                if(nextSecposition == firstVisibleItem + 1){
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = linearLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) linearLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            linearLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                linearLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        if(allCityList.size() > 0){
            new Thread(){
                @Override
                public void run() {
                    try {
                        city.setName("正在定位中.....");
                        allCityList.set(0, city);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sortAdapter.updateListView(allCityList);
                            }
                        });
                        
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    city.setName("定位失败");
                    allCityList.set(0,city);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sortAdapter.updateListView(allCityList);
                        }
                    });
                }
            }.start();
        }
    }
    
    private void initData(){
        allCityList = new ArrayList<City>();
        hotCityList = CityQueryUtil.queryAllHotCity();
        cityList = CityQueryUtil.queryAllCity();
        city = new City();
        /*if(!TextUtils.isEmpty(citys)){
            
        }*/
        city.setName("正在定位中");
        city.setSortKey("#");
        city.setPinyin("dingwei");
        allCityList.add(0,city);
        allCityList.addAll(hotCityList);
        allCityList.addAll(cityList);
        sortAdapter = new SortAdapter(this,allCityList);
    }
}
