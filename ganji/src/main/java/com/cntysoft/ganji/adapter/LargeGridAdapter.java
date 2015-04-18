package com.cntysoft.ganji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/15 16:53
 * @Email: ielxhtiger@163.com
 */
public class LargeGridAdapter extends BaseAdapter {
    private Context mContext;
    private String[] entryName = {"全职工作","兼职工作","生活家政","房产","二手车","二手物品"};

    public LargeGridAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return entryName.length;
    }

    @Override
    public Object getItem(int position) {
        return entryName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_large_table,parent,false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.entry_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.entry_icon);
        textView.setText(entryName[position]);
        imageView.setImageResource(R.drawable.near_fangchan_default);
        convertView.setBackgroundColor(0xFF20C968);
        return convertView;
    }
}
