package com.cntysoft.weishop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cntysoft.weishop.R;
import com.cntysoft.weishop.ui.IconNumberView;

/**
 * @Author：Tiger on 2015/3/21 14:26
 * @Email: ielxhtiger@163.com
 */
public class ImageAdapter extends BaseAdapter {

    private int[] mImageArray;
    private int page;
    public static final int PRE_PAGE_SIZE = 6;//每一页装载数据的大小
    private Context mContext;
    public ImageAdapter(int[] imageArray,int page,Context context){
        this.page = page;
        this.mContext = context;
        int i = page * PRE_PAGE_SIZE;
        int end = i + PRE_PAGE_SIZE;
        int j =0;
        mImageArray = new int[6];
        while(i < imageArray.length && i < end){
            mImageArray[j++] = imageArray[i++];
        }
    }
    @Override
    public int getCount() {
        return mImageArray.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int tag = page*PRE_PAGE_SIZE+i;
        if(tag == 5){
            //IconNumberView iconNumberView = new IconNumberView(mContext);
            View view1=LayoutInflater.from(mContext).inflate(R.layout.gv_item1,viewGroup,false);
            IconNumberView iconNumberView = (IconNumberView) view1.findViewById(R.id.iv_item);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),mImageArray[i]);
            iconNumberView.setImageBitmap(bitmap);
            iconNumberView.setNumber("1");
            //Log.i("sss",bitmap.getWidth()+"");
            return view1;
        }else{
           if (view == null){
               view = LayoutInflater.from(mContext).inflate(R.layout.gv_item,viewGroup,false);
           }
           ImageView imageView = (ImageView) view.findViewById(R.id.iv_item);
           imageView.setTag(tag);
           imageView.setImageResource(mImageArray[i]);
           return view;
        }
    }
}
