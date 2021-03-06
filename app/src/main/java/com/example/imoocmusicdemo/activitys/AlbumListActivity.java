package com.example.imoocmusicdemo.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.imoocmusicdemo.R;

import adapters.MusicListAdapter;
import helps.RealmHelp;
import models.AlbumModel;

public class AlbumListActivity extends BaseActivity {
    public static final String ALBUM_ID="albumId";
    private RecyclerView mRvList;
    private MusicListAdapter mAdapter;
    private String mAlbumId;
    private RealmHelp mRealmHelp;
    private AlbumModel mAlbumModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        initData();
        initView();
    }
    private void initData(){
        mAlbumId=getIntent().getStringExtra(ALBUM_ID);
        //查询当前歌单中的数据
        mRealmHelp=new RealmHelp();
       mAlbumModel=mRealmHelp.getAlbum(mAlbumId);
    }
    private  void initView(){
        initNavBar(true,"专辑列表",false);
        mRvList=fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter=new MusicListAdapter(this,null,mAlbumModel.getList());
        mRvList.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelp.close();
    }
}
