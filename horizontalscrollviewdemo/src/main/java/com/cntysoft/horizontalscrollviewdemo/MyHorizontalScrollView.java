package com.cntysoft.horizontalscrollviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：Tiger on 2015/3/31 20:55
 * @Email: ielxhtiger@163.com
 */
public class MyHorizontalScrollView extends HorizontalScrollView implements View.OnClickListener {

    /**
     * 图片滚动时回调接口
     */
    public interface CurrentImageChangeListener{
        void onCurrentImgChanged(int position, View viewIndicator);
    }

    /**
     * 条目点击时的回调
     */
    public interface OnItemClickListener{
        void  onClick(View view, int position);
    }

    private CurrentImageChangeListener mListener;
    private OnItemClickListener onItemClickListener;

    public void setCurrentImageChangelistener(CurrentImageChangeListener listener){
        this.mListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private static final String TAG = "MyHorizontalScrollView";
    /**
     * HorizontalScrollView 的第一个直接子元素
     */
    private LinearLayout mContainer;

    /**
     * 子元素宽度
     */
    private int mChildWidth;

    /**
     * 子元素的高度
     */
    private int mChildHeight;

    /**
     * 当前最后一张图片的index
     */
    private int mCurrentIndex;

    /**
     * 当前第一张图片的下标
     */
    private int mFirstIndex;

    /**
     * 当前第一个View
     */
    private View mFirstView;

    /**
     * 数据适配器
     */
    private HorizontalScrollViewAdapter mAdapter;

    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen;
    /**
     * 屏幕的宽度
     */
    private int mScreenWitdh;

    /**
     * 保存View与位置的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
    public MyHorizontalScrollView(Context context, AttributeSet attrs){
        super(context,attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWitdh = displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    public void setAdapter(HorizontalScrollViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        final View view = mAdapter.getView(0,null,mContainer);
        if(mChildWidth == 0 && mChildHeight == 0){
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();
            Log.i(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
            mChildHeight = view.getMeasuredHeight();
            mCountOneScreen = (mScreenWitdh / mChildWidth == 0)?mScreenWitdh / mChildWidth+1:mScreenWitdh / mChildWidth+2;
            if(mCountOneScreen > mAdapter.getCount()){
                mCountOneScreen = mAdapter.getCount();
            }
        }
        //初始化第一屏幕的元素
        initFirstScreenChildren(mCountOneScreen);
    }

    public void initFirstScreenChildren(int mCountOneScreen){
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        for(int i = 0;i < mCountOneScreen; i++){
            View view = mAdapter.getView(i,null,mContainer);
            view.setOnClickListener(this);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }
        if (mListener != null)
        {
            notifyCurrentImgChanged();
        }
    }

    public void notifyCurrentImgChanged(){
        for (int i = 0;i < mCountOneScreen ;i++){
            mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        mListener.onCurrentImgChanged(mFirstIndex,mContainer.getChildAt(0));
    }
    public void onClick(View view){
        if(onItemClickListener != null){
            for (int i = 0; i< mCountOneScreen ;i++){
                mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
            onItemClickListener.onClick(view, mViewPos.get(view));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            int scrollX = getScrollX();
            if(scrollX >= mChildWidth){
                loadNextImg();
            }
            if(scrollX == 0){
                loadPreImg();
            }
        }
        return super.onTouchEvent(ev);
    }
    public void loadNextImg(){
        if(mCurrentIndex == mAdapter.getCount() - 1){
            return;
        }
        //移除第一张图片，且将水平滚动位置置0
        scrollTo(0,0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);
        //获取下一张图片，并且设置onclick事件，且加入容器中
        View view = mAdapter.getView(++mCurrentIndex,null,mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);
        mFirstIndex++;
        if(mListener != null){
            notifyCurrentImgChanged();
        }
    }

    public void loadPreImg(){
        //如果当前已经是第一张，则返回
        if (mFirstIndex == 0)
            return;
        //获得当前应该显示为第一张图片的下标
        int index = mCurrentIndex - mCountOneScreen;
        if(index >= 0){
            //移除最后一张
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            //水平滚动位置向左移动view的宽度个像素
            scrollTo(mChildWidth, 0);
            //当前位置--，当前第一个显示的下标--
            mCurrentIndex--;
            mFirstIndex--;
            //回调
            if (mListener != null)
            {
                notifyCurrentImgChanged();
            }
        }
    }
}
