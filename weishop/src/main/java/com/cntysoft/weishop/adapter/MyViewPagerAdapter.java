package com.cntysoft.weishop.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * @Author：Tiger on 2015/3/21 14:16
 * @Email: ielxhtiger@163.com
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<GridView> array;
    private Context mContext;
    /**
     * 供外部调用（new）的方法
     * @param context  上下文
     * @param array    添加的序列对象
     */
    public MyViewPagerAdapter(Context context, List<GridView> array) {
        this.array = array;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(array.get(position));
        return array.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}
