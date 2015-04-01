package com.cntysoft.weishop.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cntysoft.weishop.R;

import org.w3c.dom.Text;

/**
 * @Author：Tiger on 2015/4/1 16:25
 * @Email: ielxhtiger@163.com
 */
public class CommonPriceView extends RelativeLayout {
    
    private TextView  priceTitle;
    private TextView priceNum;
    //private  RelativeLayout priceRelative;
    private String price;
    
    private void initView(Context context){
        View.inflate(context, R.layout.price_type_layout, this);  
        priceTitle = (TextView) this.findViewById(R.id.price_title);
        priceNum = (TextView) this.findViewById(R.id.price_num); 
        //priceRelative = (RelativeLayout) findViewById(R.id.price_container);
        
    }
    public CommonPriceView(Context context,AttributeSet attrs){
        super(context,attrs);
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CommonPriceView);
        String title = typedArray.getString(R.styleable.CommonPriceView_priceTitle);
        price = typedArray.getString(R.styleable.CommonPriceView_priceNum);
        //typedArray.getColor(R.styleable.CommonPriceView_android_background,0xFF0000);
        float textsize = typedArray.getDimension(R.styleable.CommonPriceView_pricetextSize,16);
        priceTitle.setText(title);
        priceNum.setText(price);
        priceNum.setTextSize(textsize);
        typedArray.recycle();
    }
    
    private void setPrice(String price){
        priceNum.setText(price);
    }
    private  String getPrice(){
        return priceNum.getText().toString();
    }
}
