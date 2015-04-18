package com.cntysoft.ganji.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/17 09:50
 * @Email: ielxhtiger@163.com
 */
public class PersonCommonView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;
    private View view;
    private View topView;
    private View bottomView;
    private void initView(Context context){
        View.inflate(context, R.layout.item_personal_common,this);
        imageView = (ImageView) findViewById(R.id.personal_iv);
        textView = (TextView) findViewById(R.id.personal_tv);
        view = findViewById(R.id.personal_divider);
        topView = findViewById(R.id.personal_top_divider);
        bottomView = findViewById(R.id.personal_bottom_divider);
    }
    public PersonCommonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PersonCommonView);
        Drawable drawable = a.getDrawable(R.styleable.PersonCommonView_src);
        String title = a.getString(R.styleable.PersonCommonView_titleText);
        int divider = a.getInteger(R.styleable.PersonCommonView_dividerVisiable,0);
        int topdivider = a.getInteger(R.styleable.PersonCommonView_topdividerVisiable,0);
        int bottomdivider = a.getInteger(R.styleable.PersonCommonView_bottomdividerVisiable,0);
        imageView.setImageDrawable(drawable);
        textView.setText(title);
        setComVisiable(divider,view);
        setComVisiable(topdivider,topView);
        setComVisiable(bottomdivider,bottomView);
        if(a != null){
            a.recycle();
        }       
    }
    
    private void setComVisiable(int divider,View view){
        if(divider == 0){
            view.setVisibility(VISIBLE);
        }else if(divider == 4){
            view.setVisibility(INVISIBLE);
        }else if(divider == 8){
            view.setVisibility(GONE);
        }
       
    }
}
