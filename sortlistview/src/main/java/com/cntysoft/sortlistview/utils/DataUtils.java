package com.cntysoft.sortlistview.utils;

import android.text.TextUtils;

import com.cntysoft.sortlistview.adapter.SortAdapter;
import com.cntysoft.sortlistview.model.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author：Tiger on 2015/4/1 21:50
 * @Email: ielxhtiger@163.com
 */
public class DataUtils {
    public static CharacterParser characterParser;
    public static PinyinComparator pinyinComparator;
    static{
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }
    public static List<SortModel> filledData(String[] data){
        
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i< data.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(data[i]);
            String pinyin = characterParser.getSelling(data[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            sortModel.setPinyin(pinyin);
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private static void filterData(SortAdapter adapter , String filterStr,List<SortModel> SourceDateList){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
