package com.cntysoft.horizontalscrollviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @Author：Tiger on 2015/3/31 20:36
 * @Email: ielxhtiger@163.com
 */
public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<Integer> Datas){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = Datas;
    }

    public int getCount(){
        return mDatas.size();
    }

    public  Object getItem(int position){
        return mDatas.get(position);
    }

    public int getItemId(int position){
        return position;
    }

    public View getView(int position , View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gallery_item,parent,false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.gallery_item_image);
            viewHolder.mText = (TextView) convertView.findViewById(R.id.gallery_item_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImg.setImageResource(mDatas.get(position));
        viewHolder.mText.setText("picture");
        return convertView;
    }

    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }
}
