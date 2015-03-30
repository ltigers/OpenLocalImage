package com.cntysoft.weishop.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cntysoft.weishop.R;

/**
 * @Author：Tiger on 2015/3/24 10:02
 * @Email: ielxhtiger@163.com
 */
public class CommonNavView extends RelativeLayout {
    private ImageView imageView;
    private TextView textView;

    public void initView(Context context){
        View.inflate(context, R.layout.common_nav,this);
        imageView = (ImageView) findViewById(R.id.iv_left_arrow);
        textView = (TextView) findViewById(R.id.nav_title);
    }

    public CommonNavView(Context context) {
        super(context);
        initView(context);
    }

    public CommonNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cntysoft.weishop","navTitle");
        textView.setText(title);
    }

    public CommonNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ImageView getImageView(){
        return imageView;
    }
}
