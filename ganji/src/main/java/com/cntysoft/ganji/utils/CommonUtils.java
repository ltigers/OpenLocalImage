package com.cntysoft.ganji.utils;

import android.content.Context;

/**
 * @Author：Tiger on 2015/3/21 15:49
 * @Email: ielxhtiger@163.com
 */
public class CommonUtils {
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
