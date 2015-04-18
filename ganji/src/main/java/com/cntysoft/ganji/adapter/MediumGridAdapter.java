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
 * @Author：Tiger on 2015/4/15 17:39
 * @Email: ielxhtiger@163.com
 */
public class MediumGridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] entryName = {"本地服务","人才简历库","宠物","票务",
            "婚恋交友","教育培训","百宝箱","生活电话薄"};
    private int[] entryIcon = {R.drawable.ic_entry_09,R.drawable.ic_entry_23
            ,R.drawable.ic_entry_10,R.drawable.ic_entry_14,R.drawable.ic_entry_11
    ,R.drawable.ic_entry_13,R.drawable.ic_entry_08,R.drawable.ic_entry_07};
    
    public MediumGridAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_medium_table,parent,false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.entry_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.entry_icon);
        textView.setText(entryName[position]);
        imageView.setImageResource(entryIcon[position]);
        return convertView;
    }
}
