package com.example.imoocmusicdemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import helps.RealmHelp;
import io.realm.Realm;


//存储系统的一些信息 应用打开运行
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //初始化Realm数据库
        Realm.init(this);
        RealmHelp.migration();
    }
}
