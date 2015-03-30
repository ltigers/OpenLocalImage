package com.cntysoft.weishop.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cntysoft.weishop.R;

import org.w3c.dom.Text;

/**
 * @Author：Tiger on 2015/3/26 17:11
 * @Email: ielxhtiger@163.com
 */
public class CommonSettingView extends RelativeLayout {
    private TextView textViewTitle;
    private TextView textViewName;
    private View viewLine;
    private String mSettingTitle;
    private String mSettingName;
    private String mSettingVisible;
    private void initView(Context context){
        View.inflate(context, R.layout.common_setting,this);
        textViewTitle = (TextView) findViewById(R.id.setting_title);
        textViewName =(TextView) findViewById(R.id.setting_name);
        viewLine = findViewById(R.id.setting_line);
        viewLine.setVisibility(VISIBLE);
    }
    public CommonSettingView(Context context) {
        super(context);
        initView(context);
    }

    public CommonSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        mSettingTitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cntysoft.weishop","settingTitle");
        mSettingName = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cntysoft.weishop","settingName");
        mSettingVisible = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cntysoft.weishop","settingVisible");
        if(!TextUtils.isEmpty(mSettingTitle)){
            textViewTitle.setText(mSettingTitle);
        }
        if(!TextUtils.isEmpty(mSettingName)){
            textViewName.setText(mSettingName);
        }else{
            textViewName.setText("");
        }
        if("VISIBLE".equals(mSettingVisible)){
            viewLine.setVisibility(VISIBLE);
        }else if("INVISIBLE".equals(mSettingVisible)){
            viewLine.setVisibility(INVISIBLE);
        }else{
            viewLine.setVisibility(GONE);
        }

    }

    public CommonSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setSettingName(String mSettingName) {
        this.mSettingName = mSettingName;
        textViewName.setText(mSettingName);
    }

    public void setSettingTitle(String mSettingTitle) {
        this.mSettingTitle = mSettingTitle;
        textViewTitle.setText(mSettingTitle);
    }
}
