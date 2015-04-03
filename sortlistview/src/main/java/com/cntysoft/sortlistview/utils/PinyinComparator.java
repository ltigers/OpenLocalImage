package com.cntysoft.sortlistview.utils;

import com.cntysoft.sortlistview.model.SortModel;

import java.util.Comparator;
/**
 * @Author：Tiger on 2015/4/1 21:39
 * @Email: ielxhtiger@163.com
 */
public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            //return o1.getSortLetters().compareTo(o2.getSortLetters());
            return o1.getPinyin().compareToIgnoreCase(o2.getPinyin());
        }
    }

}
