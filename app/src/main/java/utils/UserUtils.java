package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;


import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.imoocmusicdemo.R;
import com.example.imoocmusicdemo.activitys.LoginActivity;
import com.example.imoocmusicdemo.activitys.MainActivity;

import java.util.List;

import helps.RealmHelp;
import helps.UserHelper;
import io.realm.Realm;
import models.UserModel;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class UserUtils {
    /*
    验证登录用户输入合法性 引入框架里面的内容
     */

    /**
     * 验证用户登录
     * @param context
     * @param phone
     * @param password
     * @return
     */
    public static boolean validateLogin(Context context,String phone,String password){
        //简单的
       // RegexUtils.isMobileSimple(phone);
        //精确的
        //先验证手机号的输入合法性
       if(!RegexUtils.isMobileExact(phone)){
        Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
           return false;
       }
        if(TextUtils.isEmpty(password)){
          Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        /**
         * 先通过输入合法性后
         * 1、用户当前手机号是否被注册
         * 2、当前输入的手机号和密码是否匹配
         */
        if(!UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context,"当前手机号未注册",Toast.LENGTH_SHORT).show();
            return false;
        }
        RealmHelp realmHelp=new RealmHelp();
       boolean result=realmHelp.validateUser(phone,EncryptUtils.encryptMD5ToString(password));
       // TODO Realm使用后记得close
      // realmHelp.close();
       if(!result){
           Toast.makeText(context,"手机号或者密码不正确",Toast.LENGTH_SHORT).show();
           return  false;
       }
       //保存用户登录标记
       boolean isSave=SPUtils.saveUser(context,phone);
       if(!isSave){
           Toast.makeText(context,"系统错误，请稍后重试",Toast.LENGTH_SHORT).show();
           return false;
       }
       //保存用户标记，在全局单例类之中
        UserHelper.getInstance().setPhone(phone);
       //添加数据源
       realmHelp.setMusicSource(context);
        realmHelp.close();
        return true;
    }
    /**
    退出登录
     */
    public  static void logout(Context context){
        //删除sp保存的用户标记
        boolean isRemove=SPUtils.removeUser(context);
        if(!isRemove){
            Toast.makeText(context,"系统错误，请稍后重试",Toast.LENGTH_SHORT).show();
            return;
        }
        //删除数据源
        RealmHelp realmHelp=new RealmHelp();
        realmHelp.removeMusicSource();
        realmHelp.close();
        Intent intent =new Intent(context, LoginActivity.class);
        //添加 intent标志符号 清理task栈 并且新生成一个task栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //定义Activity动画 在startActivity之后
        ((Activity)context).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }

    /**注册用户
     * @param context
     * @param phone
     * @param password
     * @param passwordConfirm
     */
    public  static  boolean registerUser(Context context,String phone,String password,String passwordConfirm){
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(password)){
            Toast.makeText(context,"请确认密码不为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(passwordConfirm)){
            Toast.makeText(context,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        //用户当前输入的手机号是否已经被注册
        /**
         * 1、通过Realm获取当前已经注册的所有用户
         * 2、根据用户输入的手机号匹配查询的所有用于用户 可匹配则已经被注测
         */
        if(UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context,"该手机号已经存在",Toast.LENGTH_SHORT).show();
            return false;
        }
        UserModel userModel=new UserModel();
        userModel.setPhone(phone);
        //MD5加密 密码
        userModel.setPassword(EncryptUtils.encryptMD5ToString(password));
        saveUser(userModel);
        return true;
    }
    /**保存用户到数据库
     * @param userModel
     */
    public  static void saveUser(UserModel userModel){
        RealmHelp realmHelp=new RealmHelp();
        realmHelp.saveUser(userModel);
        realmHelp.close();
    }
    /**
     * 根据手机号判断用户是否存在
     */
    public static  boolean userExistFromPhone(String phone){
        boolean result =false;
        RealmHelp realmHelp=new RealmHelp();
        List<UserModel> allUser=realmHelp.getAllUser();
        for(UserModel userModel:allUser){
            if(userModel.getPhone().equals(phone)){
                //当前手机号已经存于数据库中
                result=true;
                break;
            }
        }
        realmHelp.close();
        return result;
    }
    /**
     * 验证是否存在已登录用户
     */
    public  static boolean validateUserLogin(Context context){
       return SPUtils.isLoginUser(context);
    }

    /**
     * 修改密码
     * 1、数据验证
     *    原密码是否输入
     *    新密码是否输入并且新密码与确定密码是否相同
     *    原密码输入是否正确
     *    （1）Realm 获取到当前登录的用户模型
     *    (2) 根据用户模型中保存的密码匹配用户原密码
     *  2、利用Realm模型自动更新的特性完成密码修改
     */
    public  static  boolean changePassword(Context context,String oldPassword,String password,String passwordcConfirm){
        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(context,"请输入原密码",Toast.LENGTH_SHORT).show();
            return false;
        }if(TextUtils.isEmpty(password) || !password.equals(passwordcConfirm)){
            Toast.makeText(context,"请确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        //验证原密码是否正确
       RealmHelp realmHelp=new RealmHelp();
        UserModel userModel=realmHelp.getUser();
        if(!EncryptUtils.encryptMD5ToString(oldPassword).equals(userModel.getPassword())){
            Toast.makeText(context,"原密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        realmHelp.changePassword(EncryptUtils.encryptMD5ToString(password));
        realmHelp.close();
        return true;
    }


}
