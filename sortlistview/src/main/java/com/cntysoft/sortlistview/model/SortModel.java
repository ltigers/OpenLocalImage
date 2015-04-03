package com.cntysoft.sortlistview.model;

/**
 * @Author：Tiger on 2015/4/1 21:40
 * @Email: ielxhtiger@163.com
 */
public class SortModel {
    private String name;   //显示的数据
    private String pinyin;
    private String sortLetters;  //显示数据拼音的首字母

    public  String getPinyin(){
        return pinyin;
    }
    public void setPinyin(String pinyin){
        this.pinyin = pinyin;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
