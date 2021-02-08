package com.example.imoocmusicdemo.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.imoocmusicdemo.R;

import utils.UserUtils;
import views.InputView;

public class RegisterActivity extends BaseActivity{
    private InputView mInputPhone,mInputPassword,getmInputPasswordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    /**
    初始化View
     */
    private void initView(){
        initNavBar(true,"注册",false);
        mInputPhone=fd(R.id.input_phone);
        mInputPassword=fd(R.id.input_password);
        getmInputPasswordConfirm=fd(R.id.input_password_confirm);

    }
    /**
     * 注册按钮点击事件
     * 1、用户输入合法性验证
     * （1）输入手机号是否合法
     * （2）是否输入密码和确定密码，并且两次输入相同
     * （3）当前输入的手机号是否已经被注册
     * 2、保存用户输入的手机号和密码（MD5加密密码）
     */
    public  void onRegisterClick(View v){
        String phone=mInputPhone.getInputStr();
        String password=mInputPassword.getInputStr();
        String passwordConfirm=getmInputPasswordConfirm.getInputStr();
         boolean result=UserUtils.registerUser(this,phone,password,passwordConfirm);
        if(!result) return;
        //后退
        onBackPressed();
    }

}
