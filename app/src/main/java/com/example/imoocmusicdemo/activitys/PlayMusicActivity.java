package com.example.imoocmusicdemo.activitys;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.imoocmusicdemo.R;

import helps.RealmHelp;
import jp.wasabeef.glide.transformations.BlurTransformation;
import models.MusicModel;
import views.PlayMusicView;

public class PlayMusicActivity extends BaseActivity {
    public static  final String MUSIC_ID="musicId";
    private ImageView mIvBg;
    private PlayMusicView mPlayMusicView;
    private  String mMusicId;
    private RealmHelp mRealmHelp;
    private MusicModel mMusicModel;
    private TextView mTvName,mTvAuthor;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        //隐藏statBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        initView();
    }
    private void initData(){
                mMusicId=getIntent().getStringExtra(MUSIC_ID);
                mRealmHelp=new RealmHelp();
                mMusicModel=mRealmHelp.getMusic(mMusicId);
    }
    public  void initView(){
        mIvBg=fd(R.id.iv_bg);
        mTvName=fd(R.id.tv_name);
        mTvAuthor=fd(R.id.tv_author);
        // glide-transformations
        Glide.with(this)
                .load(mMusicModel.getPoster())
               // .load("https://c-ssl.duitang.com/uploads/item/201807/16/20180716130754_KaPPe.thumb.700_0.jpeg")
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());
        // 模糊程度 和原来图片的宽高比
        mPlayMusicView=fd(R.id.play_music_view);
       // mPlayMusicView.setMusicIcon("https://c-ssl.duitang.com/uploads/item/201807/16/20180716130754_KaPPe.thumb.700_0.jpeg");
      //2  mPlayMusicView.setMusicIcon(mMusicModel.getPoster());
       // mPlayMusicView.playMusic("http://music.163.com/song/media/outer/url?id=1316563427.mp3");
        mPlayMusicView.setMusic(mMusicModel);
       //2 mPlayMusicView.playMusic(mMusicModel.getPath());
        mPlayMusicView.playMusic();
    }

    /**
     * 后退按钮点击事件
     */
    public void onBackClick(View view){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destory();
        mRealmHelp.close();
    }
}
