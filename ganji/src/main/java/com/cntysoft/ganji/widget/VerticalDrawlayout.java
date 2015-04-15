package com.cntysoft.ganji.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/11 18:12
 * @Email: ielxhtiger@163.com
 */
public class VerticalDrawLayout extends FrameLayout {

    private static final String TAG = "VerticalDrawLayout";
    private WeatherView weatherView;
    private ScrollView mFirstChild;
    private int weatherViewHeight;   // 高度
    private int mStartY;       // 开始拖拽的y值
    private int mNowMarginTop; // 保存当前的margintop

    private MarginLayoutParams marginParams;
    private boolean isShown;   // 记录是否显示
    
    private int realheight;
    //private int param;
    
    public VerticalDrawLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDrawLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        weatherView = (WeatherView) LayoutInflater.from(context).inflate(R.layout.weather,null);
        measureView(weatherView);
        weatherViewHeight = weatherView.getMeasuredHeight();
        addView(weatherView,0);
    }

    private void measureView(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(null == lp) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
        }

        int widthMeasureSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int height = lp.height;
        int heightMeasureSpec;

        if(height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean loadOnce = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        
        if(changed && !loadOnce){
            loadOnce = true;
            mFirstChild = (ScrollView)getChildAt(1);
            FitstViewHeight = mFirstChild.getHeight();
            weatherView.setScrollView(mFirstChild);
            weatherView.setVerticalDrawLayout(this);
            realheight = weatherViewHeight + weatherView.getHeadHeight();
            /*weatherView.setParamsChangedListener(new WeatherView.ParamsChangedListener() {
                @Override
                public void setNewParams(int params) {  
                    Log.i(TAG,"参数改变");
                    setCurrent(weatherViewHeight,params);
                }
            });*/
            //marginParams = (MarginLayoutParams) mFirstChild.getLayoutParams();
        }
    }

    // 当布局加载完毕后
    // 这时getChildCount()才能取出值
    // 在前面getChildCount()为0
    // 因此这里开始才能使用getChildAt()
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() <= 0) {
            System.err.println("no child was found!");
            return;
        }

        // 获取第一个布局文件中定义的第一个子view
        // 为什么是1呢？ 因为我们上面手工添加了一个。
        
    }

    // 滑动时，设置效果和属性
    private void setCurrent(int topMargin,int param) {
        Log.i(TAG,"topMargin: "+ topMargin + "param: "+ param);
        if(topMargin > weatherViewHeight + param) {
            topMargin = weatherViewHeight + param;
        }

        if(topMargin <= 0) {
            topMargin = 0;
        }
        //marginParams.topMargin = topMargin;
       // mFirstChild.setLayoutParams(marginParams);
        //Log.i(TAG,"margin:"+marginParams.topMargin);
        mNowMarginTop = topMargin;
        mFirstChild.layout(0,topMargin,getWidth(),FitstViewHeight+topMargin);
        invalidate();
        //mFirstChild.scrollTo(0,topMargin);
    }

   private  int FitstViewHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG,"ChildCount: "+getChildCount());
        for(int i = 0;i < getChildCount();i++){
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int distance = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //mStartY = (int) event.getY(); // 当按下时，记录按下的y值
                Log.i(TAG,"Down: "+ mStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                int nowY = (int) event.getRawY();  // 移动时，获取y
                // 如果当前不是显示状态t
                distance = nowY - mStartY;
                if(!isShown) {
                    distance = (distance <= realheight)?distance:realheight;
                    setCurrent(distance,0);
                }else {
                    if(distance < 0){
                      setCurrent(realheight + distance,0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                
                if(!isShown && mNowMarginTop >= realheight / 5){
                    setCurrent(realheight,0);
                    isShown = true;
                }else{
                    setCurrent(0,0);
                    isShown = false;
                }
                mStartY = 0;
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"isshow:" + isShown);
        if(isShown){
            weatherView.setAblePull(true);
        }else{
            weatherView.setAblePull(false);
        }
        boolean result = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                
                int dis = moveY - mStartY;
                if(!isShown){
                    if(dis < 0){
                        result = false;
                    }else{
                        if(mFirstChild.getScrollY() == 0){
                          result = true;
                        }else{
                            result = false;
                        }
                    }
                }else{
                    if(dis > 0){
                        result = false;
                        Log.i(TAG,"触发子view");
                    }else{
                        result = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }
}
