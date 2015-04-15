package com.cntysoft.ganji.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/9 10:58
 * @Email: ielxhtiger@163.com
 */
public class SideView extends View {
    /**
     * 触摸事件
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
    
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public void  setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener){
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }
    
    private TextView mTextDialog;
    private  int choose = -1; //
    private Paint paint = new Paint();
    
    public static String[] indexLetter = {"定位","热门","A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    public void setTextView(TextView mTextDialog){
        this.mTextDialog = mTextDialog;
    }

    public SideView(Context context) {
        super(context);
    }

    public SideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private int singleHeight;
    private int width;
    private int height;
    @Override
    protected void onDraw(Canvas canvas) {
        if(width == 0 || height == 0 ){
            height = getHeight();
            width = getWidth();
            singleHeight = height / indexLetter.length;
        }         
        for(int i = 0 ; i < indexLetter.length; i++){
            paint.setColor(0xff404040);
            //paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(16);

            if(i == choose){
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(indexLetter[i]) / 2;
            float yPos = singleHeight * (i + 1);
            canvas.drawText(indexLetter[i],xPos,yPos,paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        int index = (int)(y / singleHeight);
        if(index < 0){
            index = 0;
        }
        switch (action){
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if(mTextDialog != null){
                    mTextDialog.setVisibility(INVISIBLE);
                }
                break;
            default:
                setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_alphabet));
                if(oldChoose != index){
                    if(index >= 0 && index < indexLetter.length){
                        if(listener != null){
                            listener.onTouchingLetterChanged(indexLetter[index]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setTextColor(0xffff694e);
                            mTextDialog.setText(indexLetter[index]);
                            mTextDialog.setTextSize(25);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = index;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }
}
