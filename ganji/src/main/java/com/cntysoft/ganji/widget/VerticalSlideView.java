package com.cntysoft.ganji.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cntysoft.ganji.R;

import android.os.Handler;

/**
 * @Author：Tiger on 2015/4/13 15:04
 * @Email: ielxhtiger@163.com
 */
public class VerticalSlideView extends FrameLayout implements View.OnClickListener{

    /**
     * 下拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;

    /**
     * 释放立即刷新
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 2;

    /**
     * 刷新完成或未刷新状态
     */
    public static final int STATUS_REFRESH_FINISHED = 3;

    /**
     * 下拉头部回滚的速度
     */
    public static final int SCROLL_SPEED = -20;

    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    /**
     * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
     */
    private static final String UPDATED_AT = "updated_at";
    private static final String TAG = "VierticalSlideView";
    private SharedPreferences sp;
    private LinearLayout mFirstView;
    private LinearLayout headView;
    private ScrollView mSecondView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private TextView textViewTips;
    private TextView textViewTime;
    private ImageView arrowUp;
    

    private MarginLayoutParams marginLayoutParams;
    private long lastUpdateTime;
    private int headHeight;
    private boolean loadOnce = false;
    private int touchSlop;
    private boolean ablePull = false;
    
    private int mFirstViewHeight;
    private int mFirstViewVisiableheight;
    /**
     *当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED 
     */
    private int currentStatus = STATUS_REFRESH_FINISHED; //空闲状态

    /**
     * 记录上一次的状态是什么，避免进行重复操作
     */
    private int lastStatus = currentStatus;

    /**
     * 手指按下时的屏幕纵坐标
     */
    private float yDown;
    private Context mContext;

    public VerticalSlideView(Context context) {
        this(context, null);
    }

    public VerticalSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initView(context);
    }
    
    private void initView(Context context){
        this.mContext = context;
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        mFirstView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.weather1,null);
        headView = (LinearLayout) mFirstView.findViewById(R.id.head_view);
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        textViewTips = (TextView) headView.findViewById(R.id.head_tipsTextView);
        textViewTime = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
        arrowUp = (ImageView) mFirstView.findViewById(R.id.constellation_up_arrow);
        arrowUp.setOnClickListener(this);
        measureView(mFirstView);
        mFirstViewHeight = mFirstView.getMeasuredHeight();
        addView(mFirstView,0);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++){
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    private int mSecondViewHeight;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mFirstView.layout(0,headHeight,getWidth(),mFirstViewVisiableheight);
        if(changed && !loadOnce){
            headHeight = -headView.getHeight();
            //marginLayoutParams = (MarginLayoutParams)headView.getLayoutParams();
            //marginLayoutParams.topMargin = headHeight;
            mFirstViewVisiableheight = mFirstViewHeight + headHeight;
            mFirstView.layout(0,headHeight,getWidth(),mFirstViewVisiableheight);
            
            mSecondView = (ScrollView) getChildAt(1);
            mSecondViewHeight = mSecondView.getHeight();
            loadOnce = true;
        }
    }
    
    private int mStartY;
    private int mNowMarginTop;
    
    private boolean isShown;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        Log.i(TAG,"isShown:"+isShown);
        Log.i(TAG,"touchSlop" +touchSlop);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();

                int dis = moveY - mStartY;
                if(!isShown){
                    if(dis < 0 || dis < touchSlop){
                        result = false;
                    }else{
                        if(mSecondView.getScrollY() == 0){
                            result = true;
                        }else{
                            result = false;
                        }
                    }
                }else{
                    result = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        int distance = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int nowY = (int) event.getRawY();  // 移动时，获取y
                // 如果当前不是显示状态t
                distance = nowY - mStartY;
                Log.i(TAG,"move: "+ distance);
                if(!isShown) {
                    viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
                    distance = (distance <= mFirstViewVisiableheight)?distance:mFirstViewVisiableheight;
                    setCurrent(distance);
                }else {
                    viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
                    if(distance < 0){
                        setCurrent(mFirstViewVisiableheight + distance);
                    }else{
                        if(currentStatus != STATUS_REFRESHING){
                            if(headHeight + distance / 2 >0){
                                currentStatus = STATUS_RELEASE_TO_REFRESH;
                            }else{
                                currentStatus = STATUS_PULL_TO_REFRESH;
                            }
                            mFirstView.layout(0, headHeight + distance / 2, getWidth(), mFirstViewVisiableheight + distance / 2);
                            setCurrent(mFirstViewVisiableheight + distance/2);                                      
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                distance =(int)event.getRawY() - mStartY;
                if(!isShown){
                    if(mNowMarginTop >= mFirstViewVisiableheight / 5){
                       viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
                       setCurrent(mFirstViewVisiableheight);
                       isShown = true;
                    }else{
                        viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
                        setCurrent(0);
                        isShown = false;
                    }
                }else if(isShown){
                    if(distance > 0){
                        if(currentStatus == STATUS_RELEASE_TO_REFRESH){
                            //mFirstView.layout(0,0,getWidth(),mFirstViewHeight);
                            //setCurrent(mFirstViewHeight);
                            //Log.i(TAG,"up:" + mFirstView);
                            //currentStatus = STATUS_REFRESHING;
                            //finishRefreshing();
                            new RefreshingTask().execute(distance / 2);
                        }else if(currentStatus == STATUS_PULL_TO_REFRESH){
                            mFirstView.layout(0,headHeight,getWidth(),mFirstViewVisiableheight);
                            setCurrent(mFirstViewVisiableheight);
                            //currentStatus = STATUS_REFRESH_FINISHED;
                        }
                    }else{
                        if(distance < 0){
                            setCurrent(0);
                            mFirstView.layout(0,headHeight,getWidth(),mFirstViewVisiableheight);
                            //viewDivider.setVisibility(VISIBLE);
                            isShown = false;
                            currentStatus = STATUS_REFRESH_FINISHED;
                        }
                    }
                }
                mStartY = 0;
                break;
        }
        if(currentStatus == STATUS_PULL_TO_REFRESH
                || currentStatus == STATUS_RELEASE_TO_REFRESH ){
            updateHeaderView();      
            lastStatus = currentStatus;
            Log.i(TAG,"lastStatus: "+ lastStatus);
        }
        return true;
    }
    
    private void setCurrent(int topMargin){

        if(topMargin <= 0) {
            topMargin = 0;
        }
        mNowMarginTop = topMargin;
        mSecondView.layout(0, mNowMarginTop, getWidth(), mSecondViewHeight + mNowMarginTop);
        invalidate();
    }
    /**
     * 更新下拉头中的信息。
     */
    private void updateHeaderView() {
        if(lastStatus != currentStatus){
            if(currentStatus == STATUS_PULL_TO_REFRESH){
                textViewTips.setText(getResources().getString(R.string.pull_to_refresh));
                arrowImageView.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                rotateArrow();
            }else if(currentStatus == STATUS_RELEASE_TO_REFRESH){
                textViewTips.setText(getResources().getString(R.string.release_to_refresh));
                arrowImageView.setVisibility(VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                rotateArrow();
            }else if(currentStatus == STATUS_REFRESHING){
                Log.i(TAG,"happen");
                textViewTips.setText(getResources().getString(R.string.refreshing));
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.INVISIBLE);
            }
            refreshUpdatedAtValue();
        }
    }
    public void finishRefreshing(){
        currentStatus = STATUS_REFRESH_FINISHED;
        sp.edit().putLong(UPDATED_AT,System.currentTimeMillis()).commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFirstView.layout(0, headHeight, getWidth(), mFirstViewVisiableheight);
                setCurrent(mFirstViewVisiableheight);
                viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
            }
        }, 50);
        
    }
    private void rotateArrow(){
        float pivotX = arrowImageView.getWidth() / 2f;
        float pivotY = arrowImageView.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if(currentStatus == STATUS_PULL_TO_REFRESH){
            fromDegrees = 180f;
            toDegrees = 360f;
        }else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        arrowImageView.startAnimation(animation);
    }
    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    private void refreshUpdatedAtValue() {
        lastUpdateTime = sp.getLong(UPDATED_AT, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            updateAtValue = getResources().getString(R.string.time_error);
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = getResources().getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        }
        textViewTime.setText(updateAtValue);
    }
    private Handler handler = new Handler();
    private View viewDivider;
    public void setViewDivider(View viewDivider){
        this.viewDivider = viewDivider;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.constellation_up_arrow:
                setCurrent(0);
                isShown = false;
                viewDivider.setBackgroundDrawable(new ColorDrawable(0xffefefef));
        }
    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     *
     * @author guolin
     */
    class RefreshingTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int topMargin = params[0];
            while(true){
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= -headHeight) {
                    topMargin = -headHeight;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(topMargin);
            if(mListener != null){
                mListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            final int mtop = topMargin[0];
            Log.i(TAG,"mtop"+ mtop);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFirstView.requestFocus();
                    mFirstView.layout(0,headHeight + mtop,getWidth(),mFirstViewVisiableheight +mtop);
                    setCurrent(mFirstViewVisiableheight + mtop);
                }
            },50);
            
        }
    }

    /**
     * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
     *
     * @author guolin
     */
    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin =0;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= headHeight) {
                    topMargin = headHeight;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
          
 
            currentStatus = STATUS_REFRESH_FINISHED;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {

        }
    }
    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param time
     *            指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
     */
    public interface PullToRefreshListener {

        /**
         * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
         */
        void onRefresh();
    }

    /**
     * 下拉刷新的回调接口
     */
    private PullToRefreshListener mListener;

    public void setOnRefreshListener(PullToRefreshListener listener) {
        mListener = listener;
    }

}
