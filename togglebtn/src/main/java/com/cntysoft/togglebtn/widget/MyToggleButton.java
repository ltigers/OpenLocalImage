package com.cntysoft.togglebtn.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cntysoft.togglebtn.R;

/**
 * @Author：Tiger on 2015/4/7 08:57
 * @Email: ielxhtiger@163.com
 * 
 * view 对象显示的屏幕上，有几个重要步骤：
 * 1、构造方法 创建 对象。
 * 2、测量view的大小。	onMeasure(int,int);
 * 3、确定view的位置 ，view自身有一些建议权，决定权在 父view手中。  onLayout();
 * 4、绘制 view 的内容 。 onDraw(Canvas)
 */
public class MyToggleButton extends View implements View.OnClickListener{

    /**
     * 做为背景的图片
     */
    private Bitmap backgroundBitmap;
    /**
     * 可以滑动的图片
     */
    private Bitmap slideBtn;
    private Paint paint;
    /**
     * 滑动按钮的左边界
     */
    private float slideBtn_left;


    public MyToggleButton(Context context) {
        super(context);
    }

    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideBtn = BitmapFactory.decodeResource(getResources(),R.drawable.slide_button);
        
        paint = new Paint();
        paint.setAntiAlias(true);
        
        setOnClickListener(this);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置当前view的大小
         * width  :view的宽度
         * height :view的高度   （单位：像素）
         */
        setMeasuredDimension(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());
    }

    /**
     * 当前开关的状态
     *  true 为开
     */
    private boolean currState = false;
    
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        // 绘制 背景
		/*
		 * backgroundBitmap	要绘制的图片
		 * left	图片的左边届
		 * top	图片的上边届
		 * paint 绘制图片要使用的画笔
		 */
        canvas.drawBitmap(backgroundBitmap,0,0,paint);

        //绘制 可滑动的按钮
        canvas.drawBitmap(slideBtn,slideBtn_left,0,paint);
    }

    /**
     * 判断是否发生拖动，
     * 如果拖动了，就不再响应 onclick 事件
     *
     */
    private boolean isDrag = false;
    
    @Override
    /**
     * onclick 事件在View.onTouchEvent 中被解析。
     * 系统对onclick 事件的解析，过于简陋，只要有down 事件  up 事件，系统即认为 发生了click 事件
     *
     */
    public void onClick(View v) {
        /*
		 * 如果没有拖动，才执行改变状态的动作
		 */
        if(!isDrag){
            currState = !currState;
            flushState();
        }
    }

    /**
     * down 事件时的x值,用于判断是否滑动
     */
    private int firstX;
    /**
     * touch 事件的上一个x值
     */
    private int lastX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX = lastX = (int)event.getX();
                isDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生拖动
                if(Math.abs(event.getX()-firstX)>5){
                    isDrag = true;
                }
                //计算 手指在屏幕上移动的距离
                int dis = (int) (event.getX() - lastX);

                //将本次的位置 设置给lastX
                lastX = (int) event.getX();

                //根据手指移动的距离，改变slideBtn_left 的值
                slideBtn_left = slideBtn_left+dis;
                break;
            case MotionEvent.ACTION_UP:
                //在发生拖动的情况下，根据最后的位置，判断当前开关的状态
                if(isDrag){
                    int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth(); // slideBtn
                    /*
				 * 根据 slideBtn_left 判断，当前应是什么状态
				 */
                    if (slideBtn_left > maxLeft / 2) { // 此时应为 打开的状态
                        currState = true;
                    } else {
                        currState = false;
                    }

                    flushState();
                }
                break;
        }
        flushView();
        return true;
    }

    /**
     * 刷新当前状态
     */
    private void flushState(){
        if(currState){
            slideBtn_left = backgroundBitmap.getWidth() - slideBtn.getWidth();
        }else{
            slideBtn_left = 0;
        }
        flushView();
    }

    /**
     * 刷新当前视图
     */
    private void flushView() {
        /*
		 * 对 slideBtn_left  的值进行判断 ，确保其在合理的位置 即 0<=slideBtn_left <=  maxLeft
		 * 
		 */ 
        int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth();

        //确保 slideBtn_left >= 0
        slideBtn_left = (slideBtn_left > 0)?slideBtn_left:0;
        //确保 slideBtn_left <=maxLeft
        slideBtn_left = (slideBtn_left < maxLeft)?slideBtn_left:maxLeft;
        invalidate();
    }
}
