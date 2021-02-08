package com.example.imoocmusicdemo.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.imoocmusicdemo.R;

import java.util.Timer;
import java.util.TimerTask;

import utils.UserUtils;

//延迟三秒 跳转界面
public class WelcomeActivity extends BaseActivity {
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }
    /**
    初始化
     */
    private void init(){
        final boolean isLogin=UserUtils.validateUserLogin(this);
      mTimer=new Timer();
      mTimer.schedule(new TimerTask() {
          @Override
          public void run() {
              if(isLogin){
                  toMain();
              }
              else{
                  toLogin();
              }
          }
      },3000);
    }
    //跳转到主界面
    private  void toMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    //跳转到登录界面
    private  void toLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


}
