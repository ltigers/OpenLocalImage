package com.cntysoft.textwebview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity1 extends Activity {

    private EditText et = null;
    private Button btn = null;
    private WebView wv = null;
    private WebSettings ws = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main1);
        et = (EditText) this.findViewById(R.id.EditText01);
        btn = (Button) this.findViewById(R.id.Button01);
        wv = (WebView) this.findViewById(R.id.WebView);
        ws = wv.getSettings();
        ws.setAllowFileAccess(true);//设置允许访问文件数据  
        ws.setJavaScriptEnabled(true);//设置支持javascript脚本  
        ws.setBuiltInZoomControls(true);//设置支持缩放 
        wv.addJavascriptInterface(new Object(){
            public void openActivity(){
                Intent intent = new Intent(MainActivity1.this,MainActivity.class);
                startActivity(intent);
            }
        },"android");
        wv.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                //当有新连接时，使用当前的 WebView  
                Toast.makeText(MainActivity1.this,url,Toast.LENGTH_LONG).show();
                /*if(url.equals("#dianji")){
                    wv.setVisibility(View.GONE);
                }else{
                    view.loadUrl(url);
                }*/
                view.loadUrl(url);
                return true;
            }
        });
        wv.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                //构建一个Builder来显示网页中的alert对话框  
                Builder builder = new AlertDialog.Builder(MainActivity1.this);
                builder.setTitle("提示对话框");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub  
                        result.confirm();
                    }

                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Builder builder = new Builder(MainActivity1.this);
                builder.setTitle("带选择的对话框");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub  
                        result.confirm();
                    }

                });
                builder.setNeutralButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub  
                        result.cancel();
                    }

                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity1.this);
                final View v = inflater.inflate(R.layout.prom_dialog, null);
                //设置 TextView对应网页中的提示信息  
                ((TextView) v.findViewById(R.id.TextView_PROM)).setText(message);
                //设置EditText对应网页中的输入框  
                ((EditText) v.findViewById(R.id.EditText_PROM)).setText(defaultValue);
                Builder builder = new Builder(MainActivity1.this);
                builder.setTitle("带输入的对话框 ");
                builder.setView(v);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub  
                        String value = ((EditText) v.findViewById(R.id.EditText_PROM)).getText().toString();
                        result.confirm(value);
                    }

                });
                builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub  
                        result.cancel();
                    }

                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub  
                        result.cancel();
                    }

                });
                builder.create();
                builder.show();
                return true;
            }

            //设置网页加载的进度条  
            public void onProgressChanged(WebView view, int newProgress) {
                MainActivity1.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            //设置应用程序的标题  
            public void onReceivedTitle(WebView view, String title) {
                MainActivity1.this.setTitle(title);
                super.onReceivedTitle(view, title);
            }

        });
        btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub  
                //String url = et.getText().toString();
                //判断输入的内容是不是网址  
                /*if(URLUtil.isNetworkUrl(url)){
                    wv.loadUrl(url);
                }else{
                    et.setHint("输入的网址不合法，请重新输入");
//                  et.setText("输入的网址不合法，请重新输入");  
                }*/
                wv.loadUrl("file:///android_asset/text.html");
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack(); //goBack()表示返回WebView的上一页面   
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
