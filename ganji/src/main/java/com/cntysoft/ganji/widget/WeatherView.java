package com.cntysoft.ganji.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/11 11:49
 * @Email: ielxhtiger@163.com
 */
public class WeatherView extends LinearLayout implements View.OnTouchListener{

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
    private static final String TAG = "WeatherView";
    private SharedPreferences sp;
    private LinearLayout headLinearLayout;
    private LinearLayout containerView;
    private LinearLayout weatherLinearLayout; 
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private TextView textViewTips;
    private TextView textViewTime;
    
    private MarginLayoutParams marginLayoutParams;
    private long lastUpdateTime;
    private int headHeight;
    private boolean loadOnce = false;
    private int touchSlop;
    private boolean ablePull = false;
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

    private ScrollView scrollView;

    private VerticalDrawLayout verticalDrawLayout;

    public void setVerticalDrawLayout(VerticalDrawLayout verticalDrawLayout) {
        this.verticalDrawLayout = verticalDrawLayout;
    }

    public void setScrollView(ScrollView scrollView){
        this.scrollView = scrollView;
    }
    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //initView(context);
    }

   
    private void initView(Context context){
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        headLinearLayout = (LinearLayout) getChildAt(0);
        arrowImageView = (ImageView) headLinearLayout.findViewById(R.id.head_arrowImageView);
        progressBar = (ProgressBar) headLinearLayout.findViewById(R.id.head_progressBar);
        textViewTips = (TextView) headLinearLayout.findViewById(R.id.head_tipsTextView);
        textViewTime = (TextView) headLinearLayout.findViewById(R.id.head_lastUpdatedTextView);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        refreshUpdatedAtValue();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed && !loadOnce){
            initView(mContext);
            headHeight = -headLinearLayout.getHeight();
            marginLayoutParams = (MarginLayoutParams)headLinearLayout.getLayoutParams();
            marginLayoutParams.topMargin = headHeight;
//            if(paramsChangedListener != null){
//                paramsChangedListener.setNewParams(headHeight);
//            }
            containerView = (LinearLayout)getChildAt(1);
            containerView.setOnTouchListener(this);
            loadOnce = true;
        }
    }

    public int getHeadHeight(){
        return headHeight;
    }
    public void setAblePull(boolean able){
        this.ablePull =able;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(ablePull){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    yDown = event.getRawY();
                    Log.i(TAG,"down: "+ yDown);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int)(yMove - yDown);
                    Log.i(TAG,"move_distance: "+ distance);
                    if(distance <= 0 && marginLayoutParams.topMargin <= headHeight){
                        return false;
                    }
                    if(distance < touchSlop){
                        return false;
                    }
                    if(currentStatus != STATUS_REFRESHING){
                        if(marginLayoutParams.topMargin > 0){
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        }else{
                            currentStatus = STATUS_PULL_TO_REFRESH;
                        }
                        marginLayoutParams.topMargin = headHeight + distance / 2;
                        Log.i(TAG,(headHeight + distance / 2)+"");
                        headLinearLayout.setLayoutParams(marginLayoutParams);
//                        if(paramsChangedListener != null){
//                            paramsChangedListener.setNewParams(marginLayoutParams.topMargin);
//                        }
                        layoutView();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if(currentStatus == STATUS_RELEASE_TO_REFRESH){
                        new RefreshingTask().execute();
                    }else if(currentStatus == STATUS_PULL_TO_REFRESH){
                        new HideHeaderTask().execute();
                    }
                    Log.i(TAG,"up: "+ currentStatus);
                    break;
            }
            if(currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH){
                updateHeaderView();
                lastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                return true;
            }
        }
        return true;
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
    /**
     * 更新下拉头中的信息。
     */
    private void updateHeaderView() {
        if(lastStatus != currentStatus){
            if(currentStatus == STATUS_PULL_TO_REFRESH){
                textViewTips.setText(getResources().getString(R.string.pull_to_refresh));
                arrowImageView.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                rotateArrow();
            }else if(currentStatus == STATUS_RELEASE_TO_REFRESH){
                textViewTips.setText(getResources().getString(R.string.release_to_refresh));
                arrowImageView.setVisibility(VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            }else if(currentStatus == STATUS_REFRESHING){
                textViewTips.setText(getResources().getString(R.string.refreshing));
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
            }
            refreshUpdatedAtValue();
        }
    }
    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
     */
    public void finishRefreshing(){
        currentStatus = STATUS_REFRESH_FINISHED;
        sp.edit().putLong(UPDATED_AT,System.currentTimeMillis()).commit();
        new HideHeaderTask().execute();
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
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     *
     * @author guolin
     */
    class RefreshingTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            int topMargin = marginLayoutParams.topMargin;
            while(true){
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(topMargin);
            if (mListener != null) {
                mListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            marginLayoutParams.topMargin = topMargin[0];
            headLinearLayout.setLayoutParams(marginLayoutParams);
//            if(paramsChangedListener != null){
//                paramsChangedListener.setNewParams(topMargin[0]);
//            }
            layoutView();
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
            int topMargin = marginLayoutParams.topMargin;
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
            marginLayoutParams.topMargin = topMargin;
            headLinearLayout.setLayoutParams(marginLayoutParams);
//            if(paramsChangedListener != null){
//                paramsChangedListener.setNewParams(topMargin);
//            }
            layoutView();
            currentStatus = STATUS_REFRESH_FINISHED;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            marginLayoutParams.topMargin = topMargin[0];
            headLinearLayout.setLayoutParams(marginLayoutParams);
//            if(paramsChangedListener != null){
//                paramsChangedListener.setNewParams(topMargin[0]);
//            }
            layoutView();
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
    
    public interface ParamsChangedListener{
        void setNewParams(int params);
    }
    private ParamsChangedListener paramsChangedListener;
    public void setParamsChangedListener(ParamsChangedListener paramsChangedListener){
        this.paramsChangedListener = paramsChangedListener;
    }
    
    private void layoutView(){
        if(scrollView!=null){
            scrollView.layout(0,getHeight()+marginLayoutParams.topMargin,getWidth(),scrollView.getHeight()+marginLayoutParams.topMargin);
            verticalDrawLayout.invalidate();
        }
    }
}
