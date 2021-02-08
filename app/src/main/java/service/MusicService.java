package service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.example.imoocmusicdemo.R;
import com.example.imoocmusicdemo.activitys.WelcomeActivity;

import helps.MediaPlayerHelp;
import models.MusicModel;

/**
 * 1、通过Service 连接PlayMusicView 和 MediaPlayHelper
 * 2、PlayMusicView --Service：
 *    1、播放音乐 暂停音乐
 *    2、启动Service、绑定Service 接除绑定Service
 *  3、MediaPlayHelper-- Service:
 *    1、播放音乐 暂停音乐
 *    2、监听音乐播放完成 停止Service
 */
public class MusicService extends Service {
    //不可以设置为0
    public static final int NOTIFICATION_ID=1;

    private MediaPlayerHelp mMediaPlayerHelp;
    private  MusicModel mMusicModel;
    public MusicService() {
    }
    public class MusicBind extends Binder{
        /**
         * 设置音乐（MusicModel）
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public  void setMusic(MusicModel musicModel){
            mMusicModel=musicModel;
            startForeground();
        }
        /**
         * 播放音乐
         */
        public void playMusic(){
            /**
             * 1、先判断当前音乐是否已经播放的音乐
             * 2、如果当前音乐已经播放 那么直接 执行start
             * 3、不是正在播放 重置播放状态setPath
             */
            if( mMediaPlayerHelp.getPath()!=null &&
                    mMediaPlayerHelp.getPath().equals(mMusicModel.getPath()))
            {
                mMediaPlayerHelp.start();
            }else {
                mMediaPlayerHelp.setPath(mMusicModel.getPath());
                mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMeidaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSelf();
                    }
                });
            }
        }
        /**
         * 暂停播放
         */
        public void stopMusic(){
            mMediaPlayerHelp.pause();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerHelp=MediaPlayerHelp.getInstance(this);
    }
    /**
     * 系统默认不允许不可见的后台服务播放音乐，
     * Notification,在通知栏中展示
     */
    /**
     * 设置服务在前台可见
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startForeground () {

        /**
         * 通知栏点击跳转的intent
         */
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, WelcomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        /**
         * 创建Notification
         */
        Notification notification = null;
        /**
         * android API 26 以上 NotificationChannel 特性适配
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = createNotificationChannel();
            notification = new Notification.Builder(this, channel.getId())
                    .setContentTitle(mMusicModel.getName())
                    .setContentText(mMusicModel.getAuthor())
                    .setSmallIcon(R.drawable.ic_launcher_project_baoerjie)
                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        } else {
            notification = new Notification.Builder(this)
                    .setContentTitle(mMusicModel.getName())
                    .setContentText(mMusicModel.getAuthor())
                    .setSmallIcon(R.drawable.ic_launcher_project_baoerjie)
                    .setContentIntent(pendingIntent)
                    .build();
        }
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void startForeground(){
//        /**
//         * 通知栏点击跳转的intent
//         */
//        PendingIntent pendingIntent=PendingIntent
//                .getActivity(this,0,new Intent(this, WelcomeActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);
//        /**
//         * 创建Notification
//         */
//        Notification notification=new Notification.Builder(this)
//                .setContentTitle(mMusicModel.getName())
//                .setContentText(mMusicModel.getAuthor())
//                .setSmallIcon(R.drawable.ic_launcher_project_baoerjie)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        /**
//         * 设置Notification 在前台展示
//         */
        startForeground(NOTIFICATION_ID,notification);
    }
//    主要修改的地方有以下三点：
//            1、android O 的 NotificationChannel 。 代码位于 MusicService 类 startForeground 方法中。
//            2、android 9.0 的网络访问问题。代码位于 AndroidManfest.xml 中 application 中 新增了 networkSecurityConfig。
//            3、android 9.0 前台权限问题。代码位于 AndroidManfest.xml 中 新增了
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel () {
        String channelId = "imooc";
        String channelName = "ImoocMusicService";
        String Description = "ImoocMusic";
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(Description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setShowBadge(false);
        return channel;
    }

}
