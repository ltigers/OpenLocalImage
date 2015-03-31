package com.cntysoft.weishop.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Author：Tiger on 2015/3/31 14:09
 * @Email: ielxhtiger@163.com
 */
public class IconNumberView extends ImageView {

    private String number;
    public IconNumberView(Context context) {
        super(context);
    }

    public IconNumberView(Context context,AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0,0);
        if(drawable.getClass() == NinePatchDrawable.class){
            return;
        }
        Bitmap b=((BitmapDrawable)drawable).getBitmap();
        int x = b.getWidth()/2;
        if("0".equals(number)){
               return ;
        }
        paint.setColor(Color.RED);
        canvas.drawCircle((float)(x + Math.sqrt(x * x / 2)),(float)(x - Math.sqrt(x * x / 2)),(float)(x / 4),paint);
        //canvas.drawCircle((float)(2 * x),0,x/4,paint);
        paint.setColor(Color.WHITE);
        //为适应各种屏幕分辨率，字体大小取半径的3.5分之一，具体根据项目需要调节
        paint.setTextSize((float)(x/3.5));
        //去除锯齿效果
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        //字体加粗
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //字体位置设置为以圆心为中心
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number,(float)(x+Math.sqrt(x*x/2)),(float)(x-Math.sqrt(x*x/2))+x/9, paint);
    }
    public  void setNumber(String number){
        this.number = number;
        invalidate();
    }
}
