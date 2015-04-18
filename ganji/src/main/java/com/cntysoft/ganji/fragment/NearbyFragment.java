package com.cntysoft.ganji.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/17 15:38
 * @Email: ielxhtiger@163.com
 */
public class NearbyFragment extends Fragment {

    private View view;
    
    private LinearLayout fullTimeJob;
    private LinearLayout partTimeJob;
    private LinearLayout lifeServ;
    private LinearLayout handWup;
    private LinearLayout friendShip;
    private LinearLayout handHouse;
    private LinearLayout fangChan;
    private RelativeLayout relativeLayout;
    private int width;
    private int height;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        if(view == null){
            view = inflater.inflate(R.layout.fragment_nearby,null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.nearContainer);
        width = relativeLayout.getWidth();
        height = relativeLayout.getHeight();
        fullTimeJob = (LinearLayout) view.findViewById(R.id.fulltime_job);
        partTimeJob = (LinearLayout) view.findViewById(R.id.parttime_job);
        lifeServ = (LinearLayout) view.findViewById(R.id.homeserv);
        handWup = (LinearLayout) view.findViewById(R.id.handwupin);
        friendShip = (LinearLayout) view.findViewById(R.id.friendship);
        handHouse = (LinearLayout) view.findViewById(R.id.handhouse);
        fangChan = (LinearLayout) view.findViewById(R.id.fangchan);
        
        startAnimation(fangChan,600,0.15f,0.2f);
        startAnimation(handHouse,500,0.2f,0.05f);
        startAnimation(friendShip,400,0.1f,-0.15f);
        startAnimation(handWup,300,-0.1f,-0.15f);
        startAnimation(lifeServ,200,-0.2f,0.05f);
        startAnimation(partTimeJob,100,-0.15f,0.2f);
        startAnimation(fullTimeJob,0,0.0f,0.2f);

    }
    private void startAnimation(View view,int offset,float x,float y){
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,x,Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_PARENT,y,Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setDuration(100);
        translateAnimation.setFillAfter(true);
        translateAnimation.setStartOffset(offset);
        translateAnimation.setRepeatCount(0);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f,0f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setStartOffset(offset);
        scaleAnimation.setRepeatCount(0);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimation);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(set);
        //animation.startNow();
    }
}
