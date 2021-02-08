package com.example.imoocmusicdemo.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.imoocmusicdemo.R;

import utils.UserUtils;
import views.InputView;

/**
 *
 */
//NavigationBar 顶部
public class LoginActivity extends BaseActivity{
    private InputView mInputPhone,mInputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
//    初始化View
    private void initView(){
        initNavBar(false,"登录",false);
        mInputPhone=fd(R.id.input_phone);
        mInputPassword=fd(R.id.input_password);
    }
    /*
    跳转到注册页面的点击事件
     */
    public void onRegisterClick(View v){
        Intent intent =new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }
    /**
    登录按钮
     */
    public void onCommitClick(View v){
        //获取输入的手机号和 密码
        String phone=mInputPhone.getInputStr();
        String password=mInputPassword.getInputStr();
        //验证用户输入是否合法
        if(!UserUtils.validateLogin(this,phone,password)){
            return;
        }
        //跳转到应用
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
