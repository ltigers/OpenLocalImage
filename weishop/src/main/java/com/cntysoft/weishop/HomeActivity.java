package com.cntysoft.weishop;


import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cntysoft.weishop.adapter.ImageAdapter;
import com.cntysoft.weishop.adapter.MyViewPagerAdapter;
import com.cntysoft.weishop.ui.BadgeView;
import com.cntysoft.weishop.ui.IconNumberView;
import com.cntysoft.weishop.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";
    private static final int PER_PAGE_SIZE = 6;
    private ViewPager viewPager;
    private List<GridView> gridviewArrayList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageAdapter imageAdapter;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;
    private int mWidth;
    private  int mCurrentItem;
    private int itemCount;
    private ImageView imageViewPointer;
    private ImageView imageViewLeftBottom;
    private ImageView imageViewRightBottom;
    private BadgeView badgeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ExitApplication.newInstance().addActivity(this);
        initViews();
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        viewPager = (ViewPager) findViewById(R.id.vp_home);
        myViewPagerAdapter = new MyViewPagerAdapter(this,gridviewArrayList);
        viewPager.setAdapter(myViewPagerAdapter);
        imageViewLeft = (ImageView) findViewById(R.id.iv_left);
        imageViewRight = (ImageView) findViewById(R.id.iv_right);
        imageViewPointer = (ImageView) findViewById(R.id.iv_pointer);
        imageViewLeftBottom = (ImageView) findViewById(R.id.iv_left_bottom);
        badgeView = new BadgeView(this,imageViewLeftBottom);
        badgeView.setText("4");
        badgeView.setTextSize(CommonUtils.dip2px(this,6));
        badgeView.show();
        imageViewRightBottom = (ImageView) findViewById(R.id.iv_right_bottom);
        imageViewLeft.setAlpha(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentItem = viewPager.getCurrentItem();
                Log.i("HomeActivity", position + "  " + positionOffsetPixels +" "+mCurrentItem);
                if(positionOffsetPixels < mWidth/2 && itemCount - 1 == position +1){
                    int alpha = -positionOffsetPixels + mWidth/2;
                    imageViewRight.setAlpha(alpha);
                }else if(positionOffsetPixels > mWidth/2 && 0 == position){
                    int alpha =positionOffsetPixels - mWidth/2;
                    imageViewLeft.setAlpha(alpha);
                }

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    imageViewPointer.setImageResource(R.drawable.ic_kdwd_second_point);
                }else if(position == 0){
                    imageViewPointer.setImageResource(R.drawable.ic_kdwd_first_point);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        if(mCurrentItem == 0){
                            imageViewLeft.setAlpha(0);
                            imageViewRight.setAlpha(255);
                        }else if(mCurrentItem == itemCount - 1){
                            imageViewLeft.setAlpha(255);
                            imageViewRight.setAlpha(0);
                        }else if(mCurrentItem > 0 && mCurrentItem < itemCount-1){
                        imageViewLeft.setAlpha(255);
                        imageViewRight.setAlpha(255);
                    }
                }
            }
        });
        //mCurrentItem = viewPager.getCurrentItem();
        itemCount = myViewPagerAdapter.getCount();
        Log.i("HelloActivity",itemCount + "");
        imageViewLeft.setOnClickListener(this);
        imageViewRight.setOnClickListener(this);
        imageViewLeftBottom.setOnClickListener(this);
        imageViewRightBottom.setOnClickListener(this);
    }

    private void initViews(){
        final int[] imageArray =new int[] {
                R.drawable.ic_kdwd_to_addwei,R.drawable.ic_kdwd_to_shop,
                R.drawable.ic_kdwd_to_order,R.drawable.ic_kdwd_to_sale,
                R.drawable.ic_kdwd_to_customer,R.drawable.ic_kdwd_to_income,
                R.drawable.ic_kdwd_to_promotion,R.drawable.ic_kdwd_to_tuiguang,
                R.drawable.ic_kdwd_to_market,R.drawable.ic_kdwd_to_distributor
        };
        final int pageCount = (int)Math.ceil(imageArray.length / PER_PAGE_SIZE);
        gridviewArrayList = new ArrayList<GridView>();
        //Log.i("AAA",pageCount+"");
        for(int i = 0;i <= pageCount;i++){
            GridView gridView = new GridView(HomeActivity.this);
            imageAdapter = new ImageAdapter(imageArray,i,this);
            gridView.setAdapter(imageAdapter);
            gridView.setNumColumns(2);
            gridView.setTag(new Integer(i));
            gridView.setPadding(CommonUtils.dip2px(this,25),0,CommonUtils.dip2px(this,25),0);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridView.setGravity(Gravity.CENTER_HORIZONTAL);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setVerticalSpacing(CommonUtils.dip2px(HomeActivity.this, 15));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Integer j = (Integer) ((GridView)adapterView).getTag();
                    Log.i(TAG,"gridview is click" + j.intValue());
                    if (0 == j.intValue()){
                        Log.i(TAG,"first gridview  "+ i);
                        if(i == 5){
                           IconNumberView iconNumberView = (IconNumberView)((RelativeLayout)view).getChildAt(0);
                           iconNumberView.setImageBitmap(BitmapFactory.decodeResource(getResources(),imageArray[i]));
                           iconNumberView.setNumber("0");
                           //imageAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(HomeActivity.this,IncomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_right_in,R.anim.out);
                        }
                    }else if(j.intValue()==1){
                        Log.i(TAG,"second gridview  "+ i);
                    }
                }
            });
            gridviewArrayList.add(gridView);
        }
    }

    @Override
    public void onClick(View view) {
        mCurrentItem = viewPager.getCurrentItem();
        int id = view.getId();
        switch (id){
            case R.id.iv_left:
                if (mCurrentItem == 1){
                    imageViewLeft.setAlpha(0);
                }
                viewPager.setCurrentItem((mCurrentItem == 0)?0:(mCurrentItem - 1));
                break;
            case R.id.iv_right:
                if(mCurrentItem == itemCount -2){
                    imageViewRight.setAlpha(0);
                }
                viewPager.setCurrentItem((mCurrentItem == itemCount -1 )? (itemCount -1):(mCurrentItem + 1 ));
                break;
            case R.id.iv_left_bottom:
                badgeView.setText("0");
                badgeView.hide();
                Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_right_in,R.anim.out);
                break;
            case R.id.iv_right_bottom:
                Intent intent1 = new Intent(HomeActivity.this,SettingActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.pull_right_in,R.anim.out);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitBy2Click();
        }
        return false;
    }

    private static boolean isExit = false;
    private void exitBy2Click(){
        Timer timer = null;
        if(isExit == false){
            isExit = true;
            Toast.makeText(this, getString(R.string.ReclickExit), Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);
        }else{
            //timer.cancel();
            finish();
            System.exit(0);
        }
    }
}
