package views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.imoocmusicdemo.R;

import java.io.IOException;

import models.MusicModel;
import service.MainService;
import service.MusicService;

public class PlayMusicView extends FrameLayout {
    private boolean isPlaying,isBindService;
    private  Context mContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private MainService mainService=new MainService();
    private MusicModel mMusicModel;
    private View mView;
    private ImageView mIvIcon,mIvNeedle,mIvPlay,mBefore,mNext;
    private Animation mPlayMusicAnim,mPlayNeedleAnim,mStopNeedleAnim;
    private FrameLayout mFlPlayMusic;
   // private MediaPlayerHelp mMediaPlayerHelp;
   //2 private  String mPath;

    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private  void init(Context context){
        //TODO MediaPlayer
        this.mContext=context;
        //自定义的layout
       mView=LayoutInflater.from(mContext).inflate(R.layout.play_music,this,false);
       //FrameLayout里的方法
       mFlPlayMusic=mView.findViewById(R.id.fl_play_music);
       mFlPlayMusic.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               trigger();
           }
       });
       mIvNeedle=mView.findViewById(R.id.iv_needle);
        mIvIcon=mView.findViewById(R.id.iv_icon);
        mIvPlay=mView.findViewById(R.id.iv_play);
        mBefore=mView.findViewById(R.id.iv_before);
        mNext=mView.findViewById(R.id.iv_after);
        /**
         * 1、定义所需要执行的动画
         *    光盘转动
         *    指针指向光盘
         *    指针离开光盘
         * 2、startAnimation 让View执行动画
         * 在anim下创建自定义动画
         */
        //初始化动画
       mPlayMusicAnim= AnimationUtils.loadAnimation(mContext,R.anim.play_music_anim);
       mPlayNeedleAnim= AnimationUtils.loadAnimation(mContext,R.anim.play_needle_anim);
       mStopNeedleAnim= AnimationUtils.loadAnimation(mContext,R.anim.stop_needle_anim);

        addView(mView);
       // mMediaPlayerHelp=MediaPlayerHelp.getInstance(mContext);
        mIvPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });
        mBefore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMusic();
            }
        });
        mNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMusic();
            }
        });
    }
    /**
     * 切换播放状态
     */
    private  void trigger(){
        if(isPlaying){
            stopMusic();
        }else{
           //2 playMusic(mPath);
            playMusic();
        }
    }
    /**
     * 播放音乐
     */
//   2 public void playMusic(String path){
//   2    mPath=path;
        public void playMusic(){
        isPlaying=true;
        //让按钮隐藏
//        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);
        startMusicService();
//        /**
//         * 1、先判断当前音乐是否已经播放的音乐
//         * 2、如果当前音乐已经播放 那么直接 执行start
//         * 3、不是正在播放 重置播放状态setPath
//         */
//        if( mMediaPlayerHelp.getPath()!=null &&
//                mMediaPlayerHelp.getPath().equals(path))
//        {
//            mMediaPlayerHelp.start();
//        }else {
//            mMediaPlayerHelp.setPath(path);
//            mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMeidaPlayerHelperListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mMediaPlayerHelp.start();
//                }
//            });
//        }
    }
    /**
     * 停止播放音乐
     */
    public void stopMusic(){
        isPlaying=false;
        //让按钮显示
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);
        if(mMusicBind!=null) {
            mMusicBind.stopMusic();
        }
     //   mMediaPlayerHelp.pause();
    }
    /**
     * 设置光盘中显示的音乐封面
     */
    /**
     * 设置光盘内的图片
     * @param
     */

    public  void setMusicIcon(){
            Glide.with(mContext)
                    .load(mMusicModel.getPoster())
                    .into(mIvIcon);
    }
    public  void setMusic(MusicModel music){
        mMusicModel=music;
        setMusicIcon();
    }
    /**
     * 启动音乐服务
     */
    private void startMusicService(){
        //启动Service
        if(mServiceIntent==null){
            mServiceIntent=new Intent(mContext, MusicService.class);
            mContext.startService(mServiceIntent);
        }else{
            mMusicBind.playMusic();
        }
        //绑定 Service，先判断当前Service 没有绑定则绑定
        if(!isBindService){
            isBindService=true;
            mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 播放上一首功能
     */
    private void previousMusic(){
        //启动Service
        if(mServiceIntent==null){
            mServiceIntent=new Intent(mContext, MainService.class);
            mContext.startService(mServiceIntent);
        }else{
            try {
                if (mainService.isComplete()==true)
                    mainService.playNew(-1);
                else{
                    mainService.stopPlay();
                    mainService.playNew(-1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //绑定 Service，先判断当前Service 没有绑定则绑定
        if(!isBindService){
            isBindService=true;
            mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 播放下一首功能
     */
    private void nextMusic(){
        //启动Service
        if(mServiceIntent==null){
            mServiceIntent=new Intent(mContext, MainService.class);
            mContext.startService(mServiceIntent);
        }else{
            try {
                mainService.playNew(1);
//                if (mainService.isComplete()==true)
//                    mainService.playNew(1);
//                else{
//                    mainService.stopPlay();
//                    mainService.playNew(1);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //绑定 Service，先判断当前Service 没有绑定则绑定
        if(!isBindService){
            isBindService=true;
            mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 取消绑定
     */
    public void destory(){
        //如果绑定 接触绑定
       if(isBindService){
           isBindService=false;
           mContext.unbindService(conn);
       }
    }

    ServiceConnection conn=new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBind=(MusicService.MusicBind)service;
            mMusicBind.setMusic(mMusicModel);
            mMusicBind.playMusic();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
