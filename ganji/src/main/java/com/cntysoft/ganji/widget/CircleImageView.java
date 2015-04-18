package com.cntysoft.ganji.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Author：Tiger on 2015/4/17 11:13
 * @Email: ielxhtiger@163.com
 */
public class CircleImageView extends ImageView {
    
    private int width;
    private int height;
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        width = getWidth();
        height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        this.measure(0,0);
        if(drawable.getClass()==NinePatchDrawable.class){
            return;
        }
        Bitmap b=((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap=b.copy(Bitmap.Config.ARGB_8888, true);
        
        int radius = width > height ? height / 2 :width / 2;      
        Bitmap circleBitmap = getCircleBitmap(bitmap,radius);
        canvas.drawBitmap(circleBitmap, width / 2 - radius, height / 2 - radius, null);
    }
    
    private Bitmap getCircleBitmap(Bitmap bitmap , int radius){
        Bitmap scaledBmp;
        int dimeter=radius*2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片 
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if(bmpHeight > bmpWidth){
            squareWidth=squareHeight=bmpWidth;
            x=0;
            y=(bmpHeight-bmpWidth)/2;
            squareBitmap=Bitmap.createBitmap(bitmap, x, y, squareWidth, squareHeight);
        }else if(bmpHeight<bmpWidth){
            squareWidth=squareHeight=bmpHeight;
            x=(bmpWidth-bmpHeight)/2;
            y=0;
            squareBitmap=Bitmap.createBitmap(bitmap, x, y, squareWidth, squareHeight);
        }else{
            squareBitmap=bitmap;
        }
        if(squareBitmap.getWidth()!= dimeter ||squareBitmap.getHeight()!=dimeter){
            scaledBmp=Bitmap.createScaledBitmap(squareBitmap, dimeter, dimeter, true);
        }else{
            scaledBmp=squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledBmp.getWidth(), scaledBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledBmp.getWidth(),scaledBmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledBmp.getWidth() / 2,
                scaledBmp.getHeight() / 2,
                scaledBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBmp, rect, rect, paint);
        bitmap = null;
        squareBitmap = null;
        scaledBmp = null;
        return output;
    }
}
