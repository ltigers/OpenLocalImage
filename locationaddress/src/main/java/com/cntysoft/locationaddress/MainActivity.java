package com.cntysoft.locationaddress;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        getl();
        textView.setText(latitude +"  "+longitude);
    }
    private double latitude = 0.0;
    private double longitude = 0.0;

    void getl() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.e(TAG, "gps");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }else {
                LocationListener locationListener = new LocationListener() {

                    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {

                    }

                    // Provider被enable时触发此函数，比如GPS被打开
                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    // Provider被disable时触发此函数，比如GPS被关闭
                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            Log.e("Map",
                                    "Location changed : Lat: "
                                            + location.getLatitude() + " Lng: "
                                            + location.getLongitude());
                            latitude = location.getLatitude(); // 纬度
                            longitude = location.getLongitude(); // 经度
                        }
                    }
                };
            Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0,
                        locationListener);
                if (location1 != null) {
                    Log.e(TAG, "net");
                    latitude = location1.getLatitude(); // 经度
                    longitude = location1.getLongitude(); // 纬度
                }
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,"ddddd");
                        Log.e(TAG, "latitude === " + latitude);
                        Log.e(TAG, "longitude  === "+longitude);
                    }
                }.start();
            }
        }
}
