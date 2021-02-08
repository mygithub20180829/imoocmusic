package helps;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**音乐播放的几种方式
 *
 * 1、直接在Activity中去创建 播放音乐，音乐与Activity绑定
 * Activity退出时停止播放运行时播放音乐
 * 2、通过全局单例类与Application绑定 Application被杀死停止播放
 * 3、通过service 进行音乐播放  service
 */
public class MediaPlayerHelp {
    private static MediaPlayerHelp instance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private String mPath;
    private OnMeidaPlayerHelperListener onMeidaPlayerHelperListener;
    public void setOnMediaPlayerHelperListener(OnMeidaPlayerHelperListener onMeidaPlayerHelperListener) {
        this.onMeidaPlayerHelperListener = onMeidaPlayerHelperListener;
    }

    //TODO 完成单例器？？
    public static MediaPlayerHelp getInstance(Context context){
        if (instance==null){
            synchronized (MediaPlayerHelp.class){
                if(instance==null){
                    instance=new MediaPlayerHelp(context);
                }
            }
        }
        return instance;
    }
    private MediaPlayerHelp(Context context){
        mContext=context;
        mMediaPlayer=new MediaPlayer();
    }
    /**|
     * 1、setPath:当前需要播放音乐的地址
     * 2、start：播放音乐
     * 3、pause：暂停播放
     */
    /**
     * 当前需要播放的音乐
     * @param path
     */
    public void setPath(String path){
        /**
         * 1、音乐正在播放 (或者切换音乐) 重置播放状态
         * 2、设置播放音乐路径
         * 3、准备播放
         */
      //  mPath=path;
        /**  TODO 切换的错误 Caused by: java.lang.IllegalStateException
         *（错误逻辑）当音乐 进行切换时 如果音乐处于播放状态
         * 那么就去重置音乐播放状态 没有处于播放状态 不用重置
         * ||!path.equals(mPath)
         */
        //1、音乐正在播放 重置播放状态 (或者切换音乐)
        if(mMediaPlayer.isPlaying()||!path.equals(mPath)){
            mMediaPlayer.reset();
        }
        mPath=path;
        //2、设置播放音乐路径
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3、准备播放  prepareAsync异步 prepare同步
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(onMeidaPlayerHelperListener!=null){
                    onMeidaPlayerHelperListener.onPrepared(mp);
                }
            }
        });
        //监听音乐播放完成
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(onMeidaPlayerHelperListener!=null){
                    onMeidaPlayerHelperListener.onCompletion(mp);
                }
            }
        });
    }
    //返回path 正在播放的音乐路径
    public String getPath(){
        return mPath;
    }

    /**
     * 播放音乐
     */
    public  void start(){
        if(mMediaPlayer.isPlaying())
           return;
        else mMediaPlayer.start();

    }

    /**
     * 暂停播放
     */
    public void pause(){
        mMediaPlayer.pause();
    }

    public  interface  OnMeidaPlayerHelperListener{
        void onPrepared(MediaPlayer mp);
        void onCompletion(MediaPlayer mp);
    }

}
