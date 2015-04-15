package com.cntysoft.ganji.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cntysoft.ganji.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：Tiger on 2015/4/9 14:38
 * @Email: ielxhtiger@163.com
 */
public class CityQueryUtil {
    
    private static String path = "data/data/com.cntysoft.ganji/files/grouplib.db";
    
    public static List<City> queryAllHotCity(){
        List<City> cityList = new ArrayList<City>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query("hot_city",new String[]{"short_name","pinyin"},null,null,null,null,"display_order");
        while(cursor.moveToNext()){
            City city = new City();
            String name = cursor.getString(cursor.getColumnIndex("short_name"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            city.setName(name);
            city.setPinyin(pinyin);
            city.setSortKey("$");
            cityList.add(city);
        }
        cursor.close();
        sqLiteDatabase.close();
        return cityList;
    }
    public static List<City> queryAllCity(){
        List<City> cityList = new ArrayList<City>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query("city",new String[]{"short_name","pinyin"},null,null,null,null,"pinyin");
        while(cursor.moveToNext()){
            City city = new City();
            String name = cursor.getString(cursor.getColumnIndex("short_name"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            city.setName(name);
            city.setPinyin(pinyin);
            city.setSortKey(String.valueOf(pinyin.toUpperCase().charAt(0)));
            cityList.add(city);
        }
        cursor.close();
        sqLiteDatabase.close();
        return cityList;
    }
}
