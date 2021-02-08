package helps;
/**
 * 1、用户登录
 *   （1）当用户登录时 利用SharePreferences保存登录用户标记（手机号码）
 *   （2）利用全局单例类UserHelper？？？？保存登录用户信息
 *        1、用户登录之后
 *        2、用户重新打开应用程序，检测sharePreferences是否存在登录用户标记
 *        ，存在则为UserHelp进行赋值 并进入主页 不存在进入 登录界面
 * 2、用户退出
 *   1、删除sharePreferences保存的用户标记 退出到登录界面
 */
public class UserHelper {
    private static UserHelper instance;
    private UserHelper(){};
    public  static  UserHelper getInstance(){
        if (instance==null){
            synchronized (UserHelper.class){
                if(instance==null){
                    instance=new UserHelper();
                }
            }
        }
        return instance;
    }
    private String phone;
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
