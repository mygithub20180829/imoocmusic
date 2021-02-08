package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import helps.MediaPlayerHelp;
import models.MusicModel;

public class MainService extends Service {

    private MusicModel musicModel=new MusicModel();
//    private MediaPlayerHelp MH=new MediaPlayerHelp();

    //列表循环
    public static final int MENU_RECICLE=1;
    //单曲循环
    public static final int SINGLE_RECICLE=2;
    //随机播放
    public static final int RADOM=3;
    //播放下一首
    public static final int OPER_NEXT=1;
    //播放上一首
    public static final int OPER_PREVIOUS=-1;

    /*当前歌曲播放状态——播放列表刚刚加载或没有加载，并未播放任何歌曲*/
    private static final int UNPLAY=-1;
    /*当前歌曲播放状态——当前歌曲被暂停*/
    private static final int PAUSE=1;
    /*当前歌曲播放状态——当前歌曲被停止*/
    private static final int STOP=2;
    /*当前歌曲播放状态——正在播放*/
    private static final int PLAYING=3;
    /*当前歌曲播放状态——播放完成*/
    private static final int PLAYCOM=4;

    //保存当前播放状态
    private int currMode=MENU_RECICLE;
    //用于显示播放列表的数据源
    private List<Map<String,Object>> musicList=new ArrayList<>();

    private MediaPlayer mPlayer;
    //当前播放的歌曲索引
    private int currIndex=-1;
    //当前歌曲的播放状态
    private int currStatus=UNPLAY;

    public MainService() {
        super();
        // TODO Auto-generated constructor stub
        mPlayer=new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                currStatus=PLAYCOM;
            }

        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new MainBinder();
    }

    public boolean isComplete(){
        return currStatus==PLAYCOM;
    }

    /**
     * 获得当前切换策略的内容
     * @return
     */
    public String getMode(){
        switch(currMode){
            case MENU_RECICLE:
                return "列表循环";
            case SINGLE_RECICLE:
                return "单曲循环";
            case RADOM:
                return "随机播放";
            default:
                return "";
        }
    }

    /**
     * 改变切换策略
     * @return
     */
    public String changeMode(){
        switch(currMode){
            case MENU_RECICLE:
                currMode=SINGLE_RECICLE;
                return "单曲循环";
            case SINGLE_RECICLE:
                currMode=RADOM;
                return "随机播放";
            case RADOM:
                currMode=MENU_RECICLE;
                return "列表循环";
            default:
                return "";
        }
    }

    /**
     * 找当前目录下的音乐文件
     * @param musicUrl 要找寻音乐文件的目录路径
     * @return
     */
    public List<Map<String,Object>> refeshMusicList(String musicUrl){
        File musicDir=new File(musicUrl);
        if(musicDir!=null&&musicDir.isDirectory()){
            File[] musicFile=musicDir.listFiles(new MusicFilter());
            if(musicFile!=null){
                musicList=new ArrayList<>();
                for(int i=0;i<musicFile.length;i++){
                    File currMusic=musicFile[i];
                    //获取当前目录的名称和绝对路径
                    String abPath=currMusic.getAbsolutePath();
                    String musicName=currMusic.getName();
                    Map<String,Object> currMusicMap=new HashMap<String,Object>();
                    currMusicMap.put("musicName", musicName);
                    currMusicMap.put("musicAbPath", abPath);
                    musicList.add(currMusicMap);
                }

            }else{
                musicList = new ArrayList<>();
            }
        }else{
            musicList = new ArrayList<>();
        }
        return musicList;
    }

    /**
     * 播放歌曲
     * @param musicPo 要播放的歌曲在歌曲列表中的索引
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws IOException
     */
    public void playMusic(int musicPo) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
        Map<String,Object> currMusic=musicList.get(musicPo);
        String musicUrl=(String)currMusic.get("musicAbPath");
        mPlayer.reset();
        mPlayer.setDataSource(musicUrl);
        mPlayer.prepare();
        mPlayer.start();
        currIndex=musicPo;
        currStatus=PLAYING;
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        mPlayer.stop();
        currStatus=STOP;
    }


    /**
     * 播放一首新的歌曲
     * @param operCode 播放操作（是上一首还是下一首）
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws IOException
     */
    public void playNew(int operCode) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
        musicList=refeshMusicList(musicModel.getPath());
        if(musicList.size()>0){
            switch(currMode){
                case MENU_RECICLE:
                    int newIndex=0;
                    switch(operCode){
                        case OPER_NEXT:
                            newIndex=currIndex+1;
                            if(newIndex>=musicList.size()){
                                newIndex=0;
                            }
                            playMusic(newIndex);
                            break;
                        case OPER_PREVIOUS:
                            newIndex=currIndex-1;
                            if(newIndex<0){
                                newIndex=musicList.size()-1;
                            }
                            playMusic(newIndex);
                            break;
                    }
                    playMusic(newIndex);
                    break;
                case SINGLE_RECICLE:
                    playMusic(currIndex);
                    break;
                case RADOM:
                    Random rd=new Random();
                    int randomIndex=rd.nextInt(musicList.size());
                    playMusic(randomIndex);
                    break;
            }
        }
    }
    /**
     * 暂停当前歌曲的播放
     */
    public void pause(){
        mPlayer.pause();
        currStatus=PAUSE;
    }

    /*
     * 释放当前播放的音乐资源
     */
    public void release(){
        mPlayer.release();
        currStatus=UNPLAY;
    }

    /**
     * 播放按钮要执行播放操作时调用
     * @throws IOException
     * @throws IllegalStateException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public void play() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
        if(musicList.size()>0){
            switch(currStatus){
                case UNPLAY:
                    playMusic(0);
                    break;
                case STOP:
                    mPlayer.prepare();
                    mPlayer.seekTo(0);
                case PAUSE:
                    mPlayer.start();
                    currStatus=PLAYING;
                    break;
            }
        }

    }
    /**
     * 改变当前播放进度
     * @param progress 新的播放进度
     */
    public void changePro(int progress){
        if(mPlayer.isPlaying()){
            mPlayer.seekTo(progress);
        }
    }

    /**
     * 获取当前音乐播放进度
     * @return 当前音乐的播放进度
     */
    public int getMusicPro(){
        if(mPlayer.isPlaying()){
            return mPlayer.getCurrentPosition();
        }else{
            return 0;
        }
    }
    /**
     * 获取当前音乐的总时长
     * @return
     */
    public int getMusicDur(){
        return mPlayer.getDuration();
    }

    private class MusicFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            // TODO Auto-generated method stub
            if(pathname.isFile()==true){
                String fileName=pathname.getName();
                String lowerName=fileName.toLowerCase();
                return lowerName.endsWith(".mp3");
            }else{
                return false;
            }

        }

    }







    public class MainBinder extends Binder {
        public MainService getService(){
            return MainService.this;
        }
    }
}
