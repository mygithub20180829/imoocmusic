package com.example.imoocmusicdemo.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.imoocmusicdemo.R;

import adapters.MusicGridAdapter;
import adapters.MusicListAdapter;
import helps.RealmHelp;
import models.MusicSourceModel;
import views.GridSpaceItemDecoration;

public class MainActivity extends BaseActivity {
    //项目 project
    //模块 module

    private RecyclerView mRvGrid,mRvList;
    private MusicGridAdapter mGridAdapter;
    private MusicListAdapter mListAdapter;
    private RealmHelp mRealmHelp;
    private MusicSourceModel mMusicSourceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }
    /**
    初始化数据
     */
    private  void initData(){
        mRealmHelp=new RealmHelp();
        //获得了项目中所有音乐源数据
        mMusicSourceModel=mRealmHelp.getMusicSource();
        //mRealmHelp.close();  不能在这里关闭 一直展示界面
    }
    private void initView(){
        initNavBar(false,"酷奇音乐",true);
        mRvGrid=fd(R.id.rv_grid);
//        表示同一行有三个元素
        mRvGrid.setLayoutManager(new GridLayoutManager(this,3));
        //RecyclerView里的一个抽象类
        //加入分割线 默认1dp
      //  mRvGrid.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        //getResources().getDimensionPixelSize(R.dimen.albumMarginSize)把得到的数字转化为像素
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize),mRvGrid));

        //TODO 使不能滑动
        mRvGrid.setNestedScrollingEnabled(false);

        //TODO 不理解Adapter是什么
        mGridAdapter=new MusicGridAdapter(this,mMusicSourceModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);
        /**
        1、假如已知高度的情况下 可以在布局中直接把RecyclerView的高度定义上
         2、不知道的情况下 手动计算 高度
         */
        mRvList=fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        // 加上分割线 让它垂直排列
        mRvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRvList.setNestedScrollingEnabled(false);
        mListAdapter=new MusicListAdapter(this,mRvList,mMusicSourceModel.getHot());
        mRvList.setAdapter(mListAdapter);
    }
    protected  void  onDestroy(){
        super.onDestroy();
        mRealmHelp.close();
    }

}
