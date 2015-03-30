package com.cntysoft.weishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cntysoft.weishop.utils.StreamUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity {

    //private SharedPreferences sp;
    private static final int NETWORK_ERROR = 1;
    private static final int JSON_ERROR = 2;
    private static final int ENTER_LOGIN =3;
    private static final int SHOW_UPDATE_DIALOG = 4;
    private static final int URL_ERROR = 5;
    private String description;
    private String apkurl;
    private TextView tv_update_info;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NETWORK_ERROR:
                    enterLogin();
                    Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR:
                    enterLogin();
                    Toast.makeText(getApplicationContext(),"JSON解析异常",Toast.LENGTH_SHORT).show();
                    break;
                case ENTER_LOGIN:
                    enterLogin();
                    break;
                case SHOW_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case URL_ERROR:
                    enterLogin();
                    Toast.makeText(getApplicationContext(),"Url异常",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ExitApplication.newInstance().addActivity(this);
        //sp = getSharedPreferences("config",MODE_PRIVATE);

        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        /*服务器端编写好后执行
          checkUpdate();
        */
        enterLogin();

    }

    private void checkUpdate(){
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.server_url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtil.getStringFromStream(is);
                        Log.i("SplashActivity", "联网成功" + result);

                        JSONObject obj = new JSONObject(result);
                        // 得到服务器的版本信息
                        String version = (String) obj.get("version");

                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        if(getVersionName().equals(version)){
                            msg.what = ENTER_LOGIN;
                        }else{
                            msg.what = SHOW_UPDATE_DIALOG;
                        }

                    }
                }catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                }catch (IOException e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally {

                    long endTime = System.currentTimeMillis();
                    // 我们花了多少时间
                    long dTime = endTime - startTime;
                    // 2000
                    if (dTime < 2000) {
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private String getVersionName(){
        String version = "";
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(),0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    protected void enterLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ExitApplication.newInstance().removeActivty(this);
        // 关闭当前页面
        finish();

    }

    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示升级");
//		builder.setCancelable(false);//强制升级
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                enterLogin();
                dialog.dismiss();

            }
        }).setMessage(description);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载APK，并且替换安装
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // sdcard存在
                    // afnal
                    FinalHttp finalhttp = new FinalHttp();
                    finalhttp.download(apkurl, Environment
                                    .getExternalStorageDirectory().getAbsolutePath()+"/weishop2.0.apk",
                            new AjaxCallBack<File>() {

                                @Override
                                public void onFailure(Throwable t, int errorNo,
                                                      String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    tv_update_info.setVisibility(View.VISIBLE);
                                    //当前下载百分比
                                    int progress = (int) (current * 100 / count);
                                    tv_update_info.setText("下载进度："+progress+"%");
                                }

                                @Override
                                public void onSuccess(File t) {
                                    super.onSuccess(t);
                                    installAPK(t);
                                }
                                /**
                                 * 安装APK
                                 * @param t
                                 */
                                private void installAPK(File t) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");

                                    startActivity(intent);

                                }


                            });
                } else {
                    Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterLogin();// 进入主页面
            }
        });
        builder.show();
    }
}
