package com.cntysoft.ganji.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

/**
 * @Author：Tiger on 2015/4/9 09:06
 * @Email: ielxhtiger@163.com
 */
public class ConnectionHelper {
    
    public static boolean checkNetworkInfo(final Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(mobile == State.CONNECTED || mobile == State.CONNECTING){
            return true;
        }
        if(wifi == State.CONNECTED || wifi == State.CONNECTING){
            return true;
        }
        return false;
    }
}
