package com.cntysoft.weishop;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;


/**
 * @Author：Tiger on 2015/3/27 10:37
 * @Email: ielxhtiger@163.com
 */
public class ExitApplication extends Application {

    private static ExitApplication exitApplication;
    private List<Activity> activityList = new LinkedList<Activity>();
    private ExitApplication(){}
    public static ExitApplication newInstance(){
        if(exitApplication == null){
            exitApplication = new ExitApplication();
        }
        return exitApplication;
    }
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void removeActivty(Activity activity){
        activityList.remove(activity);
    }
    public void exit(){
        for(Activity activity : activityList){
            activity.finish();
        }
        System.exit(0);
    }

}