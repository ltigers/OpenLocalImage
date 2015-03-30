package com.cntysoft.weishop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cntysoft.weishop.utils.MD5Utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * Created by Administrator on 2015/3/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText loginAccount;
    private EditText loginPsw;
    private Button  loginBtn;
    private Button loginReg;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ExitApplication.newInstance().addActivity(this);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        loginAccount = (EditText) findViewById(R.id.login_edit_account);
        loginPsw = (EditText) findViewById(R.id.login_edit_pwd);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginReg = (Button) findViewById(R.id.login_reg);
        loginBtn.setOnClickListener(this);
        loginReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id){
            case R.id.login_btn:
                String account = loginAccount.getText().toString().trim();
                String psw =loginPsw.getText().toString().trim();
                if(TextUtils.isEmpty(account) || TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this,"登陆账号和密码不能为空",Toast.LENGTH_LONG).show();
                }else{
                    /*
                    把密码与账号传给服务器，待做
                    Boolean results = checkLogin(account,psw);
                    if(results == true){
                        Editor editor = sp.edit();
                        editor.putString("account",account);
                        psw = MD5Utils.md5Psw(psw);
                        editor.putString("psw",psw);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this,"登陆账号或密码错误",Toast.LENGTH_LONG).show();

                    }
                     */
                    /*
                    先写死
                     */
                    if("1".equals(account) && "1".equals(psw)){
                        Editor editor = sp.edit();
                        editor.putString("account",account);
                        psw = MD5Utils.md5Psw(psw);
                        editor.putString("psw",psw);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        ExitApplication.newInstance().removeActivty(this);
                        finish();
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(LoginActivity.this,"登陆账号或密码错误",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.login_reg:

                break;
        }
    }

    private Boolean result;
    private Boolean checkLogin(String account,String psw){

        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("account",account);
        ajaxParams.put("psw",MD5Utils.md5Psw(psw));
        finalHttp.post("http://www.cntysoft.com/login",ajaxParams,new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String str) {
                result = true;
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                result = false;
            }
        });
        return result;
    }
}
