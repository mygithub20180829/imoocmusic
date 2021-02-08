package helps;

import android.content.Context;

import java.io.FileNotFoundException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import migration.Migration;
import models.AlbumModel;
import models.MusicModel;
import models.MusicSourceModel;
import models.UserModel;
import utils.DataUtils;

//TODO Realm的帮助类
public class RealmHelp {
    private Realm mRealm;
    public RealmHelp(){
        mRealm=Realm.getDefaultInstance();
    }
    /**
     *Realm数据库发生结构性（模型或者模型中的字段发生 新增 修改 删除）变化时
     * 我们需要对数据库进行迁移
     */
    /**
     * 告诉Realm数据需要迁移 并为Realm设置最新的配置
     * （在app打开时检测需不需要数据迁移）
     *
     */
    public static void migration(){
        RealmConfiguration conf=getRealmConf();
        //Realm设置最新的配置
        Realm.setDefaultConfiguration(conf);
        //告诉Realm数据需要迁移
        try {
            Realm.migrateRealm(conf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 返回RealmConfiguration
     * @return
     */
    private static RealmConfiguration getRealmConf(){
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new Migration())//迁移代码的执行类
                .build();
    }

    /**
     * 关闭数据库
     */
    public void close(){
        if(mRealm!=null&&!mRealm.isClosed()){
            mRealm.close();
        }
    }
    /**
     * 保存用户信息
     */
    public void saveUser(UserModel userModel){
        mRealm.beginTransaction();
        mRealm.insert(userModel);
        //可以更新主键内容
       // mRealm.insertOrUpdate(userModel);
        mRealm.commitTransaction();
    }
    /**
     * 返回所有用户
     */
    public List<UserModel> getAllUser(){
        RealmQuery<UserModel> query=mRealm.where(UserModel.class);
       RealmResults<UserModel> results= query.findAll();
       return results;
    }
    /**
     * 验证当前手机号和密码是否匹配
     */
    public  boolean validateUser(String phone,String password){
        boolean result=false;
        RealmQuery<UserModel> query=mRealm.where(UserModel.class);
        query.equalTo("phone",phone)
                .equalTo("password",password);
        //取出一个用户
       UserModel userModel=query.findFirst();
       if (userModel!=null){
           result=true;
       }
       return result;
    }
    /**
     * 获取当前用户
     */
    public  UserModel getUser(){
        RealmQuery<UserModel> query=mRealm.where(UserModel.class);
         UserModel userModel= query.equalTo("phone",UserHelper.getInstance().getPhone()).findFirst();
         return  userModel;
    }
    /**
     * 修改密码
     */
    public  void changePassword(String password){
       UserModel userModel= getUser();
       //开启事务
       mRealm.beginTransaction();
       userModel.setPassword(password);
       //关闭事务
       mRealm.commitTransaction();
    }
    /**
     * 1、用户登录 存放数据
     * 2、退出登录 删除数据
     */
    /**
     * 保存音乐源数据
     */
    public  void setMusicSource(Context context){
        //拿到资源文件中的数据
       String musicSourceJson=DataUtils.getJsonFromAssets(context,"DataSource.json");
       mRealm.beginTransaction();
       mRealm.createObjectFromJson(MusicSourceModel.class,musicSourceJson);
       mRealm.commitTransaction();
    }
    /**
     * 删除音乐源数据
     * 1、RealmResult delete
     * 2、Realm dalete 删除这个模型下所有数据
     */
    public  void removeMusicSource(){
        mRealm.beginTransaction();
        mRealm.delete(MusicSourceModel.class);
        mRealm.delete(MusicModel.class);
        mRealm.delete(AlbumModel.class);
        mRealm.commitTransaction();
    }
    /**
     * 返回音乐源数据
     */
    public MusicSourceModel getMusicSource(){
        return mRealm.where(MusicSourceModel.class).findFirst();
    }
    /**
     * 返回歌单
     */
    public  AlbumModel getAlbum(String albumId){
        return mRealm.where(AlbumModel.class).equalTo("albumId",albumId).findFirst();
    }
    /**
     * 返回音乐
     */
    public MusicModel getMusic(String musicId){
        return  mRealm.where(MusicModel.class).equalTo("musicId",musicId).findFirst();
    }
}
