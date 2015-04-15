package com.cntysoft.ganji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cntysoft.ganji.db.dao.CityQueryUtil;
import com.cntysoft.ganji.model.City;

import java.util.ArrayList;
import java.util.List;


public class SearchCityActivity extends Activity {

    private ImageView backImageView;
    private TextView textView;
    private ListView listview;
    private EditText editText;
    
    private List<City> cityList;
    private List<String> filterList;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_city);
        
        backImageView = (ImageView) findViewById(R.id.left_image_btn);
        editText = (EditText) findViewById(R.id.center_edit);
        textView = (TextView) findViewById(R.id.activity_city_filter_warning);
        textView.setVisibility(View.GONE);
        listview = (ListView) findViewById(R.id.activity_city_filter_listview);
        cityList = CityQueryUtil.queryAllCity();
        filterList = new ArrayList<String>();
        //arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,filterList);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.search_city_item,R.id.search_city_tv,filterList);
        listview.setAdapter(arrayAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                  
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchCityActivity.this,CityActivity.class);
                startActivity(intent);
                finish();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                String currentCity = filterList.get(position);
                editor.putString("city",currentCity);
                editor.commit();

                Intent intent = new Intent(SearchCityActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    
    public void filterData(String filterStr){
        if(!TextUtils.isEmpty(filterStr)){
            filterList.clear();
            for(City city : cityList){
                String name = city.getName();
                String pinyin = city.getPinyin();
                if(name.startsWith(filterStr) || pinyin.startsWith(filterStr.toLowerCase())){
                    filterList.add(name);
                }
            }
        }else{
            filterList.clear();
        }
        if(filterList.size() == 0){
            listview.setVisibility(View.GONE);
            textView.setText("没找到");
            textView.setVisibility(View.VISIBLE);
            arrayAdapter.notifyDataSetChanged();
        }else{
            textView.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
